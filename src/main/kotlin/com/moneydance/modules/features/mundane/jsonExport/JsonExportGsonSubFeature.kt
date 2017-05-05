package com.moneydance.modules.features.mundane.jsonExport

import com.google.gson.GsonBuilder
import com.infinitekind.moneydance.model.Account
import com.moneydance.modules.features.mundane.subfeature.SubFeature
import com.moneydance.modules.features.mundane.subfeature.SubFeatureContext

class JsonExportGsonSubFeature : SubFeature {

    override val name: String
        get() = "Output accounts as JSON (GSON)"

    override fun invoke(context: SubFeatureContext) {
        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.setPrettyPrinting().create()
        val rootAccount = GsonAccount(context.rootAccount)
        val json = gson.toJson(rootAccount)
        println(json)
    }

    data class GsonAccount(val name: String, val subAccounts: List<GsonAccount>) {

        constructor(a: Account) : this(
                name = a.accountName,
                subAccounts = a.subAccounts.map { GsonAccount(it) }
        )

    }

}
