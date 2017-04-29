package github.adeynack.kotlin.extensions

import org.amshove.kluent.shouldEqual
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object StringExtensionsSpec : Spek({

    describe("q") {

        it("should return the same string when no single nor double quotes are present") {
            q("Foo") shouldEqual "Foo"
        }

        it("should return the same string when double-quotes are present") {
            q("Foo is \"Bar\".") shouldEqual "Foo is \"Bar\"."
        }

        it("should return the double-quoted version of the string when two single quotes are used") {
            q("Foo is ''Bar''.") shouldEqual "Foo is \"Bar\"."
        }

        it("should leave the single quotes as they are") {
            q("Foo is 'Bar'.") shouldEqual "Foo is 'Bar'."
        }

        it("should transform 3 single quotes into a double quote and a single quote") {
            q("Foo is '''Bar'''.") shouldEqual "x/**/Foo is \"'Bar\"'."
        }

    }

})
