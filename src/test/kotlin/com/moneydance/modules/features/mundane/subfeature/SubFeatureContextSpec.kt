package com.moneydance.modules.features.mundane.subfeature

import com.infinitekind.moneydance.model.Account
import com.infinitekind.moneydance.model.AccountBook
import com.moneydance.apps.md.controller.FeatureModule
import com.moneydance.apps.md.controller.FeatureModuleContext
import com.moneydance.apps.md.extensionapi.AccountEditor
import com.moneydance.apps.md.view.HomePageView
import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.awt.Image

object SubFeatureContextSpec : Spek({

    describe("Default implementation of `FeatureModuleContext`: `MDSubFeatureContext`") {

        fun createContext(): SubFeatureContext {
            val baseContext = object : FeatureModuleContext {

                override fun getRootAccount(): Account? = null

                override fun showURL(p0: String?) {}

                override fun registerFeature(p0: FeatureModule?, p1: String?, p2: Image?, p3: String?) {}

                override fun getVersion(): String? = null

                override fun registerHomePageView(p0: FeatureModule?, p1: HomePageView?) {}

                override fun registerAccountEditor(p0: FeatureModule?, p1: Int, p2: AccountEditor?) {}

                override fun getCurrentAccountBook(): AccountBook? = null

                override fun getBuild(): Int = 0
            }
            val module = object : FeatureModule() {
                override fun getName(): String = "xyz"

                override fun invoke(p0: String?) {}
            }
            return MDSubFeatureContext(baseContext, module)
        }

        describe("JSON") {

            it("must deserialize to data class from JSON string") {
                val context = createContext()
                val f = context.fromJson<Foo>("{\"bar\":[\"abc\",\"ijk\",\"xyz\"]}")
                f shouldEqual Foo(listOf("abc", "ijk", "xyz"))
            }

            it("must serialize from data class") {
                val context = createContext()
                val f = Foo(listOf("abc", "ijk", "xyz"))
                val s = context.toJson(f)
                s shouldEqual "{\"bar\":[\"abc\",\"ijk\",\"xyz\"]}"
            }

        }

    }

})

data class Foo(val bar: List<String>)
