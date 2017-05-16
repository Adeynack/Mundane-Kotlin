package github.adeynack.kotlin.swing

import java.awt.Component
import java.awt.FlowLayout
import javax.swing.Action
import javax.swing.JButton
import javax.swing.JPanel

class FlowPanel(align: Int, hgap: Int, vgap: Int, content: List<Component>)
    : JPanel(FlowLayout(align, hgap, vgap)) {

    companion object {

        // Default values taken from java.awt.FlowLayout.FlowLayout()
        private val defaultHGap = 5
        private val defaultVGap = 5

        fun left(content: List<Component>) = FlowPanel(FlowLayout.LEFT, content)
        fun left(vararg content: Component) = FlowPanel(FlowLayout.LEFT, *content)

        fun center(content: List<Component>) = FlowPanel(FlowLayout.CENTER, content)
        fun center(vararg content: Component) = FlowPanel(FlowLayout.CENTER, *content)

        fun right(content: List<Component>) = FlowPanel(FlowLayout.RIGHT, content)
        fun right(vararg content: Component) = FlowPanel(FlowLayout.RIGHT, *content)

        fun left(vararg actions: Action) = FlowPanel(FlowLayout.LEFT, actions.map(::JButton))
        fun center(vararg actions: Action) = FlowPanel(FlowLayout.CENTER, actions.map(::JButton))
        fun right(vararg actions: Action) = FlowPanel(FlowLayout.RIGHT, actions.map(::JButton))

    }

    constructor(content: List<Component>) : this(FlowLayout.CENTER, defaultHGap, defaultVGap, content)

    constructor(align: Int, content: List<Component>) : this(align, defaultHGap, defaultVGap, content)

    constructor(align: Int, hgap: Int, content: List<Component>) : this(align, hgap, defaultVGap, content)

    constructor(vararg content: Component) : this(FlowLayout.CENTER, defaultHGap, defaultVGap, content.toList())

    constructor(align: Int, vararg content: Component) : this(align, defaultHGap, defaultVGap, content.toList())

    constructor(align: Int, hgap: Int, vararg content: Component) : this(align, hgap, defaultVGap, content.toList())

    init {
        content.forEach { add(it) }
    }

}
