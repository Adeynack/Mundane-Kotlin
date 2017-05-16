package com.moneydance.modules.features.mundane.subfeature

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

abstract class SingletonFrameSubFeature : SubFeature {

    protected abstract fun createFrame(context: SubFeatureContext): JFrame

    var frame: JFrame? = null
        private set

    override fun invoke(context: SubFeatureContext) {
        val f: JFrame = frame ?: initializeFrame(context)
        f.isVisible = true
        f.toFront()
        f.requestFocus()
    }

    private fun initializeFrame(context: SubFeatureContext): JFrame {
        val f = createFrame(context)
        f.addWindowListener(object : WindowAdapter() {
            override fun windowClosed(e: WindowEvent?) {
                frame = null
            }
        })
        frame = f
        return f
    }

    fun close() {
        frame?.isVisible = false
        frame?.dispose()
        frame = null
    }

}