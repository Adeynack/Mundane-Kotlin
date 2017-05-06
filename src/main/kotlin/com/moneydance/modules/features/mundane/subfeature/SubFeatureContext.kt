package com.moneydance.modules.features.mundane.subfeature

import com.moneydance.apps.md.controller.FeatureModule
import com.moneydance.apps.md.controller.FeatureModuleContext
import com.moneydance.apps.md.controller.Main
import com.moneydance.apps.md.view.gui.MoneydanceGUI
import github.adeynack.kotlin.extensions.asA

interface SubFeatureContext : FeatureModuleContext, Logger {

    /**
     * Checks if the current [SubFeatureContext] is using [context] as its base.
     */
    fun isBasedOn(context: FeatureModuleContext?): Boolean

    /**
     * Adds a [Logger] object to which every calls to the [Logger] methods on this [SubFeatureContext] will be routed.
     */
    fun addLogger(logger: Logger)

    /**
     * Generates a new [Storage] object.
     *
     * @param subKey identifies the [Storage] in the system (has to be unique for every [Storage], usually the name of
     * the [com.moneydance.modules.features.mundane.subfeature.SubFeature].
     *
     * @param default a function providing the default value of the object to store. This is used when no value already
     * exist for that storage.
     */
    fun <T> getStorage(subKey: String, default: () -> T): Storage<T>

    /**
     * Gets the [MoneydanceGUI] that some UI components of Moneydance need to be created.
     */
    val mdGUI: MoneydanceGUI

    /**
     * Registers a [SubFeature] to Moneydance.
     *
     * @see [FeatureModuleContext.registerFeature]
     */
    fun registerFeature(subFeature: SubFeature)

}

class MDSubFeatureContext(
    private val baseContext: FeatureModuleContext,
    private val mainFeatureModule: FeatureModule
) : SubFeatureContext,
    FeatureModuleContext by baseContext {

    private var loggers: Set<Logger> = setOf(SystemErrLogger())

    //
    // Logging
    //

    override fun addLogger(logger: Logger) {
        loggers += logger
    }

    override fun info(message: String): Unit = loggers.forEach { it.info(message) }

    override fun error(message: String, error: Throwable?): Unit = loggers.forEach { it.error(message, error) }

    //
    // Storage
    //

    override fun <T> getStorage(subKey: String, default: () -> T): Storage<T> {
        return JsonLocalStorage("Mundane:$subKey", default, this)
    }

    //
    // Other
    //

    override fun isBasedOn(context: FeatureModuleContext?): Boolean {
        return baseContext === context
    }

    override val mdGUI: MoneydanceGUI
        get() = baseContext.asA<Main>().ui.asA<MoneydanceGUI>()

    override fun registerFeature(subFeature: SubFeature) {
        info("Registering sub-feature \"${subFeature.key}\" (\"${subFeature.name}\")")
        registerFeature(
            mainFeatureModule,
            subFeature.key,
            subFeature.image ?: mainFeatureModule.iconImage,
            subFeature.name)
    }

}
