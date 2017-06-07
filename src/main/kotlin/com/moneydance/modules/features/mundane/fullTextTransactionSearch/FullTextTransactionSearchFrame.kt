package com.moneydance.modules.features.mundane.fullTextTransactionSearch

import com.github.adeynack.kotti.collections.filterByOneOf
import com.infinitekind.moneydance.model.ParentTxn
import com.infinitekind.util.DateUtil
import com.moneydance.awt.AwtUtil
import com.moneydance.modules.features.mundane.splits
import com.moneydance.modules.features.mundane.subfeature.Storage
import com.moneydance.modules.features.mundane.subfeature.SubFeatureContext
import github.adeynack.kotlin.swing.FlowPanel
import github.adeynack.kotlin.swing.SimpleAction
import github.adeynack.kotlin.swing.x
import net.miginfocom.layout.AC
import net.miginfocom.layout.CC
import net.miginfocom.layout.LC
import net.miginfocom.swing.MigLayout
import java.awt.Color
import java.awt.Color.BLACK
import java.awt.Color.WHITE
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.VK_ENTER
import java.awt.event.KeyEvent.VK_ESCAPE
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
import javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED

class FullTextTransactionSearchFrame(
    private val context: SubFeatureContext,
    private val settings: Storage<FullTextTransactionSearchSettings>
) : JFrame() {

    companion object {
        private val RESULT_COLOR_DATE = Color(51, 98, 175)
        private val RESULT_COLOR_DESCRIPTION = Color(139, 179, 244)
        private val RESULT_COLOR_SOURCE = Color(192, 209, 237)
        private val RESULT_COLOR_DESTINATION = Color(5, 44, 107)
    }

    private val actionClose = SimpleAction("Close") { dispose() }
    private val actionSearch = SimpleAction("Search") { performQuery() }

    private val txtSearchInput = JTextField(settings.get().lastSearchQuery).apply {
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                when (e.keyCode) {
                    VK_ENTER -> actionSearch()
                    VK_ESCAPE -> actionClose()
                }
            }
        })
    }

    private val scrResults = JScrollPane(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED)

    private val pnlButtons = FlowPanel.right(actionClose)

    init {
        contentPane = JPanel(MigLayout(
            LC(),
            AC().grow().fill().gap()
                .shrink(),
            AC().shrink().gap()
                .grow().fill().gap()
                .shrink()
        )).apply {
            add(txtSearchInput)
            add(JButton(actionSearch), CC().wrap())

            add(scrResults, CC().spanX().wrap())

            add(pnlButtons, CC().spanX().wrap())
        }
        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = 500 x 400
        AwtUtil.centerWindow(this)
    }

    private fun performQuery() {
        val query = txtSearchInput.text
        settings.update { it.copy(lastSearchQuery = query) }
        scrResults.setViewportView(
            JPanel(MigLayout(
                LC().gridGap("4px", "2px"),
                AC(),
                AC()
            )).apply {
                context.currentAccountBook.transactionSet
                    .filterIsInstance<ParentTxn>()
                    .filterByOneOf(
                        { it.description.contains(query) },
                        { it.attachmentKeys.any { it.contains(query) } },
                        { it.hasKeywordSubstring(query, false) }
                    )
                    .forEach { t ->
                        fun cell(text: String, bg: Color, fg: Color) = JLabel(text).apply {
                            background = bg
                            foreground = fg
                            isOpaque = true
                        }
                        add(cell(DateUtil.convertIntDateToLong(t.dateInt).toString(), RESULT_COLOR_DATE, WHITE),
                            CC().growX())
                        add(cell(t.description, RESULT_COLOR_DESCRIPTION, BLACK))
                        add(cell(t.account.fullAccountName, RESULT_COLOR_SOURCE, BLACK))
                        add(FlowPanel.left(t.splits.map {
                            cell("${it.amount / 100.0} to ${it.account.fullAccountName}",
                                RESULT_COLOR_DESTINATION, WHITE)
                        }), CC().growX().wrap())
                    }
            }
        )
    }

}
