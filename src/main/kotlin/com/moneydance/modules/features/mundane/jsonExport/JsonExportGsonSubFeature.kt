package com.moneydance.modules.features.mundane.jsonExport

import com.infinitekind.moneydance.model.Account
import com.moneydance.modules.features.mundane.subfeature.SubFeature
import com.moneydance.modules.features.mundane.subfeature.SubFeatureContext
import com.moneydance.modules.features.mundane.subfeature.toJson

class JsonExportGsonSubFeature : SubFeature {

    override val name: String
        get() = "Output accounts as JSON (GSON)"

    override fun invoke(context: SubFeatureContext) {
        val rootAccount = GsonAccount(context.rootAccount)
        val json = context.toJson(rootAccount)
        context.info("\n$json")
    }

    data class GsonAccount(val name: String, val subAccounts: List<GsonAccount>) {

        constructor(a: Account) : this(
            name = a.accountName,
            subAccounts = a.subAccounts.map { GsonAccount(it) }
        )

    }

}
