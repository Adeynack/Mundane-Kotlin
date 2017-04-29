@file:kotlin.jvm.JvmName("KotlinExtensionsKt")
@file:JvmMultifileClass

package github.adeynack.kotlin.extensions

/**
 * Allow to specifying a string containing double quotes (") by writing it by doubling single-quotes.
 *
 * @example q("This is an ''example''.") == "This is an \"example\"."
 *
 */
fun q(source: String): String {
    return source.replace("''", "\"")
}
