package com.moneydance.modules.features.mundane.fullTextTransactionSearch

import com.moneydance.modules.features.mundane.subfeature.SingletonFrameSubFeature
import com.moneydance.modules.features.mundane.subfeature.SubFeatureContext
import com.moneydance.modules.features.mundane.subfeature.getStorage
import javax.swing.JFrame

object FullTextTransactionSearchFeature : SingletonFrameSubFeature() {

    override val name: String
        get() = "Full Text Transaction Search"

    override fun createFrame(context: SubFeatureContext): JFrame =
        FullTextTransactionSearchFrame(
            context,
            context.getStorage(key, { FullTextTransactionSearchSettings() })
        )

}

data class FullTextTransactionSearchSettings(
    val lastSearchQuery: String = ""
)
