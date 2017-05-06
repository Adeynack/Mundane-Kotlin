package com.moneydance.modules.features.mundane

import com.infinitekind.moneydance.model.Account
import com.infinitekind.moneydance.model.AccountBook
import com.infinitekind.moneydance.model.AccountBookListener
import com.infinitekind.moneydance.model.AccountListener
import com.infinitekind.moneydance.model.MDFileListener
import com.moneydance.apps.md.controller.AppEventManager
import com.moneydance.apps.md.controller.FeatureModule
import com.moneydance.modules.features.mundane.jsonExport.JsonExportGsonSubFeature
import com.moneydance.modules.features.mundane.subfeature.MDSubFeatureContext
import com.moneydance.modules.features.mundane.subfeature.SubFeature
import com.moneydance.modules.features.mundane.subfeature.SubFeatureContext
import github.adeynack.kotlin.extensions.orElse
import github.adeynack.kotlin.extensions.toMap
import javax.swing.SwingUtilities

@Suppress("unused") // used at runtime by Moneydance
class Main : FeatureModule() {

    private val features: Map<String, SubFeature> = listOf(
        JsonExportGsonSubFeature()
    ).toMap { it.key }

    private val context: SubFeatureContext by lazy {
        MDSubFeatureContext(super.getContext(), this)
    }

    override fun init() {
        super.init()
        context.info("Feature module is initializing. Initialized sub-feature context.")
        features.values.forEach(context::registerFeature)
    }

    override fun getName(): String = "Mundane"

    override fun invoke(s: String?) {
        features[s]?.let {
            context.info("Invoking sub-feature \"${it.key}\".")
            it.invoke(context)
        } orElse {
            context.error("Invoked with \"$s\" which does not map to a known sub-feature")
        }
    }

    override fun cleanup() {
        features.values.forEach(SubFeature::cleanup)
        super.cleanup()
    }

    private fun logEvent(source: String, eventName: String?, information: String? = null) {
        val i = information?.let { " --> $it" } ?: ""
        context.info("⦿ $source ! $eventName$i")

        // If after an event the base context differs from the one currently used, log it (this is vital to know!)
        if (!context.isBasedOn(super.getContext())) {
            context.error("---------========= BASE CONTEXT CHANGED =========---------")
        }
    }

    override fun handleEvent(appEvent: String?) {
        logEvent("App", appEvent)
        try {
            when (appEvent) {

                AppEventManager.HOME_SELECTED -> {
                    context.info("Adding account book listeners.")
                    val book = context.currentAccountBook
                    book.addListener(accountBookListener)
                    book.addAccountListener(accountListener)
                    book.addFileListener(fileListener)
                }

                AppEventManager.FILE_OPENED -> {
                    // Call the `initialize` of every feature.
                    features.values.forEach { f ->
                        SwingUtilities.invokeLater {
                            // todo: Does that really needs to be invoked later with Swing?
                            f.initialize(context)
                        }
                    }
                    // todo : Remove this (there for debugging reasons)
                    SwingUtilities.invokeLater {
                        context.info("Automatically invoking the ForceLabel sub feature.")
                        invoke("Force Label")
                    }
                }

                AppEventManager.FILE_CLOSING -> {
                    context.info("Removing account book listeners.")
                    val book = context.currentAccountBook
                    book.removeListener(accountBookListener)
                    book.removeAccountListener(accountListener)
                    book.removeFileListener(fileListener)
                }

            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        super.handleEvent(appEvent)
    }

    private val accountBookListener = object : AccountBookListener() {

        private val source = "Account Book"

        override fun accountBookDataUpdated(accountBook: AccountBook?) {
            logEvent(source, "Data Updated", "Book name = \"${accountBook?.name}\"")
        }

        override fun accountBookDataReplaced(accountBook: AccountBook?) {
            logEvent(source, "Data Replaced", "Book name = \"${accountBook?.name}\"")
        }

    }

    private val accountListener = object : AccountListener {

        private val source = "Account"

        override fun accountDeleted(a1: Account?, a2: Account?) {
            logEvent(source, "Delete", "Accounts \"${a1?.fullAccountName}\" and \"${a2?.fullAccountName}\"")
        }

        override fun accountBalanceChanged(a: Account?) {
            logEvent(source, "Balance Changed", "Account \"${a?.fullAccountName}\"")
        }

        override fun accountModified(a: Account?) {
            logEvent(source, "Modified", "Account \"${a?.fullAccountName}\"")
        }

        override fun accountAdded(a1: Account?, a2: Account?) {
            logEvent(source, "Added", "Accounts \"${a1?.fullAccountName}\" and \"${a2?.fullAccountName}\"")
        }

    }

    private val fileListener = object : MDFileListener {

        private val source = "File"

        override fun dirtyStateChanged(a: Account?) {
            logEvent(source, "Dirty State Changed", "Account \"${a?.fullAccountName}\"")
        }

    }

}
