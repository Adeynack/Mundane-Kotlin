package com.moneydance.modules.features.mundane

import com.infinitekind.moneydance.model.*
import com.moneydance.apps.md.controller.FeatureModule
import com.moneydance.modules.features.mundane.jsonExport.JsonExportGsonSubFeature
import com.moneydance.modules.features.mundane.subfeature.SubFeature
import com.moneydance.modules.features.mundane.subfeature.SubFeatureContext
import github.adeynack.kotlin.extensions.q
import github.adeynack.kotlin.extensions.toMap
import javax.swing.SwingUtilities

@Suppress("unused") // used at runtime by Moneydance
class Main : FeatureModule() {

    private val features = listOf(
            JsonExportGsonSubFeature()
    ).toMap { it.name }

    private lateinit var context: SubFeatureContext

    override fun init() {
        super.init()
        context = SubFeatureContext(super.getContext(), this)
        features.values.forEach(context::registerFeature)
    }

    override fun getName(): String = "Mundane"

    override fun invoke(s: String?) {
        features[s]?.invoke(context) ?: context.error(q("Invoked with ''$s'' which does not map to a known sub-feature"))
//        features[s].let {
//            if (it == null) context.error(q("Invoked with ''$s'' which does not map to a known sub-feature"))
//            else it.invoke(context)
//        }
    }

    override fun cleanup() {
        features.values.forEach(SubFeature::cleanup)
        super.cleanup()
    }

    override fun handleEvent(appEvent: String?) {
        when (appEvent) {

            "md:account:root" -> {
                context.info("Main::handleEvent appEvent = $appEvent")
                val book = context.currentAccountBook
                book.addListener(accountBookListener)
                book.addAccountListener(accountListener)
                book.addFileListener(fileListener)
            }

            "md:file:opened" -> {
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

            else -> {
                context.info("Main::handleEvent appEvent = $appEvent")
            }

        }
        super.handleEvent(appEvent)
    }

    private val accountBookListener = object : AccountBookListener() {

        override fun accountBookDataUpdated(accountBook: AccountBook?) {
            context.info("AccountBookListener::accountBookDataUpdated accountBook = $accountBook")
        }

        override fun accountBookDataReplaced(accountBook: AccountBook?) {
            context.info("AccountBookListener::accountBookDataReplaced accountBook = $accountBook")
        }

    }

    private val accountListener = object : AccountListener {

        override fun accountDeleted(a1: Account?, a2: Account?) {
            context.info("AccountListener::accountDeleted account = $a1 account1 = $a2")
        }

        override fun accountBalanceChanged(a: Account?) {
            context.info("AccountListener::accountBalanceChanged account = $a")
        }

        override fun accountModified(a: Account?) {
            context.info("AccountListener::accountModified account = $a")
        }

        override fun accountAdded(a1: Account?, a2: Account?) {
            context.info("AccountListener::accountAdded account = $a1 account1 = $a2")
        }

    }

    @Suppress("ObjectLiteralToLambda")
    private val fileListener = object : MDFileListener {

        override fun dirtyStateChanged(a: Account?) {
            context.info("MDFileListener::dirtyStateChanged account = $a")
        }

    }

}
