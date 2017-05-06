package github.adeynack.kotlin.extensions

import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object IterableExtensionsSpec : Spek({

    describe("toMap") {

        data class Model(val k: String, val v: Int)

        val model1 = Model("one", 1)
        val model2 = Model("two", 2)
        val models = listOf(model1, model2)

        describe("with key-extractor") {

            it("returns a Map with the proper extracted keys") {
                models.toMap { it.k } shouldEqual mapOf(
                    "one" to model1,
                    "two" to model2
                )
            }
        }

        describe("with key and value extractors") {

            it("returns a Map with the proper extracted keys and values") {
                models.toMap({ it.k }, { it.v }) shouldEqual mapOf(
                    "one" to 1,
                    "two" to 2
                )
            }

        }
    }

})
