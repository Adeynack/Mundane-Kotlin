package github.adeynack.kotlin.extensions

import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object AnyExtensionsSpec : Spek({

    describe("orElse") {

        fun getValue(i: Int): String? =
            if (i == 0) null
            else "I am soooooo not null"

        context("could be used for simple side-effects") {

            // here, both `let` and `orElse` return `Unit`.

            it("side effects when the input is null") {
                var nullSideEffect = false
                var validSideEffect = false

                getValue(0)?.let {
                    validSideEffect = true
                } orElse {
                    nullSideEffect = true
                }

                nullSideEffect shouldEqual true
                validSideEffect shouldEqual false
            }

            it("side effects when the input is valid") {
                var nullSideEffect = false
                var validSideEffect = false

                getValue(1)?.let {
                    validSideEffect = true
                } orElse {
                    nullSideEffect = true
                }

                nullSideEffect shouldEqual false
                validSideEffect shouldEqual true
            }
        }

        context("could be used to return a final non-null value") {

            it("returns the right output when the input is null") {
                val output: Int = getValue(0)?.length orElse {
                    val somethingVeryComplexToCalculateThatTakesMultipleLines = 0 - 2
                    somethingVeryComplexToCalculateThatTakesMultipleLines + 1
                }
                output shouldEqual -1
            }

            it("returns the right output when the input is valid") {
                val output: Int = getValue(1)?.length orElse {
                    val somethingVeryComplexToCalculateThatTakesMultipleLines = 0 - 2
                    somethingVeryComplexToCalculateThatTakesMultipleLines + 1
                }
                output shouldEqual 21 // length of "I am soooooo not null"
            }

        }

    }

    describe("orElseWith") {

        fun getValue(i: Int): String? =
            if (i == 0) null
            else "I am soooooo not null"

        // cannot be used for simple side-effects, since the block here needs to return at least `Unit?`. It cannot have
        // no return statement.

        context("can only be used to return a potentially null output of the same type as the input") {

            it("returns the right output when the input is valid") {
                val output: Int? = getValue(1)?.length orElseWith {
                    getValue(0)?.let { it.length / 3 }
                }
                output shouldEqual 21 // length of "I am soooooo not null"
            }

            it("returns `null` when the input is null and the `orElseWith` block returns a `null` as well") {
                val output: Int? = getValue(0)?.length orElseWith {
                    getValue(0)?.let { it.length / 3 }
                }
                output shouldEqual null
            }

            it("returns the alternative output when the input is null and the `orElseWith` block returns a valid value") {
                val output: Int? = getValue(0)?.length orElseWith {
                    getValue(1)?.let { it.length / 3 }
                }
                output shouldEqual 7
            }

        }

    }

})
