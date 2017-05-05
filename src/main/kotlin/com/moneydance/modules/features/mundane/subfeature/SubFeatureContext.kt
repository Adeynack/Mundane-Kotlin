package com.moneydance.modules.features.mundane.subfeature

import com.infinitekind.moneydance.model.Account
import com.infinitekind.moneydance.model.AccountBook
import com.moneydance.apps.md.controller.FeatureModule
import com.moneydance.apps.md.controller.FeatureModuleContext
import com.moneydance.apps.md.controller.Main
import com.moneydance.apps.md.extensionapi.AccountEditor
import com.moneydance.apps.md.view.HomePageView
import com.moneydance.apps.md.view.gui.MoneydanceGUI
import github.adeynack.kotlin.extensions.asA
import java.awt.Image

interface SubFeatureContext : FeatureModuleContext, Logger {

    val baseContext: FeatureModuleContext?

    fun addLogger(logger: Logger)

    fun <T> getStorage(subKey: String, default: () -> T): Storage<T>

    val mdGUI: MoneydanceGUI

    fun registerFeature(subFeature: SubFeature)

}

class MDSubFeatureContext(
    override val baseContext: FeatureModuleContext,
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

    override val mdGUI: MoneydanceGUI
        get() = baseContext.asA<Main>().ui.asA<MoneydanceGUI>()

    override fun registerFeature(subFeature: SubFeature) {
        registerFeature(
            mainFeatureModule,
            subFeature.key,
            subFeature.image ?: mainFeatureModule.iconImage,
            subFeature.name)
    }

}

class UninitializedSubFeatureContext : SubFeatureContext {

    private val logger = SystemErrLogger()
    private val uninitialisedMessage = "Context is being used before it is initialized."

    override val baseContext: FeatureModuleContext? = null

    override fun addLogger(logger: Logger) = throw IllegalStateException(uninitialisedMessage)

    override fun <T> getStorage(subKey: String, default: () -> T): Storage<T> = throw IllegalStateException(uninitialisedMessage)

    override val mdGUI: MoneydanceGUI
        get() = throw IllegalStateException(uninitialisedMessage)

    override fun registerFeature(subFeature: SubFeature) = throw IllegalStateException(uninitialisedMessage)

    override fun registerFeature(p0: FeatureModule?, p1: String?, p2: Image?, p3: String?) = throw IllegalStateException(uninitialisedMessage)

    override fun getRootAccount(): Account = throw IllegalStateException(uninitialisedMessage)

    override fun showURL(p0: String?) = throw IllegalStateException(uninitialisedMessage)

    override fun getVersion(): String = throw IllegalStateException(uninitialisedMessage)

    override fun registerHomePageView(p0: FeatureModule?, p1: HomePageView?) = throw IllegalStateException(uninitialisedMessage)

    override fun registerAccountEditor(p0: FeatureModule?, p1: Int, p2: AccountEditor?) = throw IllegalStateException(uninitialisedMessage)

    override fun getCurrentAccountBook(): AccountBook = throw IllegalStateException(uninitialisedMessage)

    override fun getBuild(): Int = throw IllegalStateException(uninitialisedMessage)

    override fun info(message: String) {
        logger.info("$message  << WARNING >> $uninitialisedMessage")
    }

    override fun error(message: String, error: Throwable?) {
        logger.error("$message  << WARNING >> $uninitialisedMessage", error)
    }

}
