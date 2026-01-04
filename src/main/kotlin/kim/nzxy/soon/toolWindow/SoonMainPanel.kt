package kim.nzxy.soon.toolWindow

import com.intellij.icons.AllIcons
import com.intellij.ide.actions.RefreshAction
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.filterField.FilterSearchTextField
import com.intellij.ui.table.JBTable
import com.intellij.util.SmartList
import com.intellij.util.ui.ComboBoxCellEditor
import kim.nzxy.soon.entity.SoonPriority
import kim.nzxy.soon.entity.SoonStatus
import kim.nzxy.soon.services.SoonDataService
import kim.nzxy.soon.util.ColorUtil
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import javax.swing.AbstractAction
import javax.swing.JComponent
import javax.swing.KeyStroke
import javax.swing.event.DocumentEvent
import javax.swing.table.TableColumn


class SoonMainPanel() : JBPanel<SoonMainPanel>() {
    val table = MainTable()

    init {
        layout = BorderLayout()

        add(ToolbarPanel(this), BorderLayout.NORTH)
        add(JBScrollPane(table), BorderLayout.CENTER)

        registerEscKeyPress()
    }

    fun registerEscKeyPress() {
        val esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)
        val inputMap = this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        val actionKey = "ESC_PRESSED"
        inputMap.put(esc, actionKey)
        this.actionMap.put(actionKey, object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                EventBus.escKeyClick.fire()
            }
        })
    }
}

class ToolbarPanel(comp: JComponent) : JBPanel<ToolbarPanel>() {
    init {
        val cardLayout = CardLayout()
        val filterSearch = FilterSearchTextField()
        filterSearch.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                EventBus.filterTextChange.fire(filterSearch.text)
            }
        })
        layout = cardLayout
        val actionGroup = DefaultActionGroup()
        val refreshAction = object : RefreshAction("Refresh", "Refresh the table", AllIcons.Actions.Refresh) {
            override fun actionPerformed(e: AnActionEvent) {
                EventBus.refresh.fire()
            }

            override fun update(e: AnActionEvent) {
                e.presentation.isEnabled = true
            }
        }
        val searchAction = object : FindAction() {
            override fun actionPerformed(e: AnActionEvent) {
                cardLayout.show(this@ToolbarPanel, "search")
                filterSearch.requestFocus()
            }
        }

        refreshAction.registerShortcutOn(comp)
        searchAction.registerShortcutOn(comp)

        actionGroup.add(refreshAction)
        actionGroup.add(searchAction)
        val toolbar = ActionManager.getInstance().createActionToolbar(
            "SoonToolBar", actionGroup, true
        )

        toolbar.targetComponent = comp
        add(toolbar.component, "toolbar")
        EventBus.escKeyClick.add(object : SimpleEventListener {
            override fun fire() {
                cardLayout.show(this@ToolbarPanel, "toolbar")
                filterSearch.text = ""
            }
        })
        add(filterSearch, "search")
    }
}

abstract class FindAction() : AnAction("Search", "Search", AllIcons.Actions.Search), DumbAware {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = true
    }

    fun registerShortcutOn(component: JComponent?) {
        val shortcutSet = ActionManager.getInstance().getAction(IdeActions.ACTION_FIND).shortcutSet
        this.registerCustomShortcutSet(shortcutSet, component)
    }
}

class MainTable : JBTable() {
    val tableModel = SoonTableModel(SmartList())

    init {
        model = tableModel
        font = ColorUtil.font
        autoCreateRowSorter = true
        refresh()
        EventBus.refresh.add(object : SimpleEventListener {
            override fun fire() {
                refresh()
            }
        })
        EventBus.filterTextChange.add(object : TextEventListener {
            override fun fire(text: String?) {
                tableModel.refilter(text)
            }
        })
        EventBus.redraw.add(object : SimpleEventListener {
            override fun fire() {
                redraw()
            }
        })
        customizeColumn()
        ColorUtil.setUpTableColors(this)
    }

    private fun refresh() {
        val info = service<SoonDataService>().findAll()
        tableModel.update(info)
    }

    private fun redraw() {
        this.revalidate()
        this.repaint()
    }

    private fun customizeColumn() {
        val titleColumn = columnModel.getColumn(0)
        val priorityColumn = columnModel.getColumn(1)
        val statusColumn = columnModel.getColumn(2)

        titleColumn.cellRenderer = tableModel.getRenderer()
        priorityColumn.cellRenderer = tableModel.getRenderer()
        statusColumn.cellRenderer = tableModel.getRenderer()

        val statusValues = SoonStatus.entries.map { it.fullTitle }
        statusColumn.cellEditor = object : ComboBoxCellEditor() {
            override fun getComboBoxItems(): List<String> = statusValues
        }
        fixedWidth(statusColumn, 48)

        val priorityValues = SoonPriority.entries.map { it.fullTitle }
        priorityColumn.cellEditor = object : ComboBoxCellEditor() {
            override fun getComboBoxItems(): List<String> = priorityValues
        }
        fixedWidth(priorityColumn, 48)
    }

    @Suppress("SameParameterValue")
    private fun fixedWidth(column: TableColumn, width: Int) {
        column.width = width
        column.minWidth = width
        column.maxWidth = width
    }
}