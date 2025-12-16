package kim.nzxy.soon.toolWindow

import com.intellij.ui.JBColor
import com.intellij.ui.TableCellState
import com.intellij.ui.components.JBTextArea
import com.intellij.util.SmartList
import kim.nzxy.soon.entity.HighlightLoc
import kim.nzxy.soon.entity.SoonInfo
import kim.nzxy.soon.util.PinyinMatch
import java.awt.Component
import javax.swing.JTable
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableCellRenderer

class SoonTableModel(private var data: SmartList<SoonInfo>) : AbstractTableModel() {
    private val showingData: SmartList<SoonInfo> = SmartList()
    private var keywords: String? = null
    private val render = SoonTableCellMutLineRenderer()

    init {
        showingData.addAll(data)
    }

    private val columnNames = arrayOf("分组", "标题", "优先级", "状态")

    fun getRenderer(): TableCellRenderer = render

    override fun getRowCount(): Int = showingData.size
    override fun getColumnCount(): Int = columnNames.size
    override fun getColumnName(column: Int): String = columnNames[column]

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        val rowData = showingData[rowIndex]
        return getValueAt(rowData, columnIndex)
    }

    fun getValueAt(rowData: SoonInfo, columnIndex: Int): String? {
        return when (columnIndex) {
            0 -> rowData.group
            1 -> rowData.title
            2 -> rowData.priority.shortTitle
            3 -> rowData.status.shortTitle
            else -> null
        }
    }

    override fun getColumnClass(columnIndex: Int): Class<*> = String::class.java

    override fun isCellEditable(rowIndex: Int, columnIndex: Int) = true

    fun update(newData: SmartList<SoonInfo>) {
        data = newData
        refilter()
    }


    private fun removeAllElements() {
        val idx = showingData.size - 1
        if (idx >= 0) {
            showingData.clear()
            fireTableRowsDeleted(0, idx)
        }
    }

    fun refilter(keywords: String?) {
        this.keywords = keywords
        refilter()
    }

    private fun refilter() {
        removeAllElements()
        if (keywords.isNullOrEmpty()) {
            showingData.addAll(data)
            fireTableRowsInserted(0, showingData.size - 1)
            render.resetHighlighting(setOf())
            EventBus.redraw.fire()
            return
        }
        val k = keywords!!
        val highlighted = mutableSetOf<HighlightLoc>()
        for (i in 0 until data.size) {
            var matched = false
            val target = data[i]
            for (n in 0 until columnCount) {
                val value = getValueAt(target, n)
                if (value != null && PinyinMatch.match(k, value)) {
                    highlighted.add(HighlightLoc(showingData.size, n))
                    matched = true
                }
            }
            if (matched) {
                showingData.add(target)
            }
        }
        if (showingData.isNotEmpty()) {
            fireTableRowsInserted(0, showingData.size - 1)
        }
        render.resetHighlighting(highlighted)
        EventBus.redraw.fire()
    }
}

class SoonTableCellMutLineRenderer() : TableCellRenderer, JBTextArea() {
    private var highlighted: Set<HighlightLoc> = emptySet()
    private val cellState = TableCellState()

    init {
        border = null
        isOpaque = true
    }


    fun resetHighlighting(highlighted: Set<HighlightLoc>) {
        this.highlighted = highlighted
    }

    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        cellState.collectState(table, isSelected, hasFocus, row, column)
        if (value != null) {
            this.text = value.toString()
        }
        cellState.updateRenderer(this)
        if (!isSelected && highlighted.contains(HighlightLoc(row, column))) {
            this.background = JBColor.YELLOW
        }
        alignmentX = CENTER_ALIGNMENT
        alignmentY = CENTER_ALIGNMENT
        return this
    }

}