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
