package kim.nzxy.soon.util

import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.util.ObjectUtils
import com.intellij.util.ui.UIUtil
import java.awt.Color
import javax.swing.JTable

object ColorUtil {

    val colorScheme = EditorColorsManager.getInstance().schemeForCurrentUITheme
    val font = colorScheme.getFont(EditorFontType.BOLD)


    fun doGetGridColor(): Color {
        return ObjectUtils.chooseNotNull(
            colorScheme.getColor(EditorColors.INDENT_GUIDE_COLOR),
            UIUtil.getTableGridColor()
        )
    }

    fun doGetForeground(): Color {
        return ObjectUtils.chooseNotNull(colorScheme.defaultForeground, UIUtil.getTableForeground())
    }

    fun doGetBackground(): Color {
        return ObjectUtils.chooseNotNull(colorScheme.defaultBackground, UIUtil.getTableBackground())
    }

    fun doGetSelectionForeground(): Color {
        return ObjectUtils.chooseNotNull(
            colorScheme.getColor(EditorColors.SELECTION_FOREGROUND_COLOR),
            UIUtil.getTableSelectionForeground(true)
        )
    }

    fun doGetSelectionBackground(): Color {
        return ObjectUtils.chooseNotNull(
            colorScheme.getColor(EditorColors.SELECTION_BACKGROUND_COLOR),
            UIUtil.getTableSelectionBackground(true)
        )
    }

    fun setUpTableColors(table: JTable) {
        table.setBackground(doGetBackground())
        table.setForeground(doGetForeground())
        table.setGridColor(doGetGridColor())
        table.setSelectionForeground(doGetSelectionForeground())
        table.setSelectionBackground(doGetSelectionBackground())
    }
}
