package com.moneydance.modules.kotlinmd

import com.moneydance.apps.md.controller.FeatureModuleContext
import com.moneydance.apps.md.view.gui.MoneydanceGUI

class SubFeatureContext(baseContext: FeatureModuleContext) : FeatureModuleContext by baseContext, Logger {

    private var loggers: Set<Logger> = setOf(SystemErrLogger())

    //
    // Logging
    //

    fun addLogger(logger: Logger) {
        loggers += logger
    }

    override fun info(message: String): Unit = loggers.forEach { it.info(message) }

    override fun error(message: String, error: Throwable?): Unit = loggers.forEach { it.error(message, error) }

    //
    // Storage
    //

    fun <T> getStorage(subKey: String, default: () -> T): Storage<T> {
        return JsonLocalStorage("Mundane:$subKey", default, this)
    }

    //
    // Other
    //

    val mdGUI: MoneydanceGUI = (baseContext as com.moneydance.apps.md.controller.Main).ui as MoneydanceGUI

}
