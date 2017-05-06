@file:JvmName("KotlinExtensionsKt")
@file:JvmMultifileClass

package github.adeynack.kotlin.extensions

/**
 * Casts any reference to another type [T].
 *
 * This allows casts to be chained to other calls, instead of creating bubbles of `as` casts in parenthesis.
 *
 *     // The normal Kotlin way:
 *     val casted = (((annie as Employee).supervisor as Person).pet as Dog)
 *
 *     // The `asA` way:
 *     val casted = annie.asA<Employee>().supervisor.asA<Person>().pet.asA<Dog>()
 *
 */
inline fun <reified T> Any.asA(): T = this as T

/**
 * If `this` is null, execute [block] and return its non-null value.
 *
 * This is intended to provide a value absolutely. It's equivalent to the `?:` operator, except that it allows the
 * execution of a code block, which the operator does not allow.
 *
 * Without extra actions to perform when null (or not):
 *
 *     val i: Int = features["foo"]?.invokeAndReturnSuccessCode() ?: -1
 *
 * With actions to perform on non-null, using `let` is possible:
 *
 *     val i: Int = features["foo"]?.let {
 *       println("Found parameter 'foo' with value '$it')
 *       it.invokeAndReturnSuccessCode()
 *     } ?: -1
 *
 * But, if actions are needed when the value is `null`, this function becomes handy:
 *
 *     val i: Int = features["foo"]?.invokeAndReturnSuccessCode().orElse {
 *       println("ERROR: Feature 'foo' does not exist.")
 *       -1
 *     }
 *
 * Complete example, with `let` and `orElse` (this time, infixed):
 *
 *     val i: Int = features["foo"]?.let {
 *       println("Found parameter 'foo' with value '$it')
 *       it.invokeAndReturnSuccessCode()
 *     } orElse {
 *       println("ERROR: Feature 'foo' does not exist.")
 *       -1
 *     }
 *
 */
inline infix fun <T> T?.orElse(block: () -> T): T = this ?: block()

/**
 * If `this` is null, execute [block] and return its potentially still `null` value.
 *
 * This is intended to try provide a value, but from logic that can still yield `null`.
 *
 */
inline infix fun <T> T?.orElseWith(block: () -> T?): T? = this ?: block()
