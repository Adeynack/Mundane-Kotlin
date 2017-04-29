package com.moneydance.modules.features.mundane.subfeature

import com.moneydance.apps.md.controller.FeatureModule
import com.moneydance.apps.md.controller.FeatureModuleContext
import com.moneydance.apps.md.controller.Main
import com.moneydance.apps.md.view.gui.MoneydanceGUI
import github.adeynack.kotlin.extensions.asA

class SubFeatureContext(
        baseContext: FeatureModuleContext,
        private val mainFeatureModule: FeatureModule
) : FeatureModuleContext by baseContext, Logger {

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

    val mdGUI: MoneydanceGUI = baseContext.asA<Main>().asA<MoneydanceGUI>()

    fun registerFeature(subFeature: SubFeature) {
        registerFeature(
                mainFeatureModule,
                subFeature.key,
                subFeature.image ?: mainFeatureModule.iconImage,
                subFeature.name)
    }

}
