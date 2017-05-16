package github.adeynack.kotlin.swing

import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.Icon

interface SimpleAction : Action {

    companion object {

        operator fun invoke(name: String, f: () -> Unit): SimpleAction = object : AbstractAction(name), SimpleAction {
            override fun invoke() = f()
        }

        operator fun invoke(name: String, icon: Icon, f: () -> Unit): SimpleAction = object : AbstractAction(name, icon), SimpleAction {
            override fun invoke() = f()
        }

    }

    operator fun invoke()

    override fun actionPerformed(e: ActionEvent?) = this()

}
