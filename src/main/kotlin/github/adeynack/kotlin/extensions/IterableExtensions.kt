@file:JvmName("KotlinExtensionsKt")
@file:JvmMultifileClass

package github.adeynack.kotlin.extensions

/**
 * Create a map from an [Iterable] by providing a function extracting the key from each of its element.
 *
 * @param T type of the elements of the [Iterable]. Also the type of the values of the resulting [Map].
 * @param K type of the keys of the resulting [Map] (provided by [keyExtractor]).
 *
 * @param keyExtractor function generating a key from an element of the [Iterable].
 *
 * @return a [Map] where the values are the elements of the [Iterable] and the keys were extracted
 *         by [keyExtractor].
 */
inline fun <T, K> Iterable<T>.toMap(keyExtractor: (T) -> K): Map<K, T> {
    return this.map { keyExtractor(it) to it }.toMap()
}

/**
 * Create a map from an [Iterable] by providing a function extracting the key from each of its element.
 *
 * @param T type of the elements of the [Iterable]. Also the type of the values of the resulting [Map].
 * @param K type of the keys of the resulting [Map] (provided by [keyExtractor]).
 *
 * @param keyExtractor function generating a key from an element of the [Iterable].
 *
 * @return a [Map] where the values are the elements of the [Iterable] and the keys were extracted
 *         by [keyExtractor].
 */
inline fun <T, K, V> Iterable<T>.toMap(keyExtractor: (T) -> K, valueExtractor: (T) -> V): Map<K, V> {
    return this.map { keyExtractor(it) to valueExtractor(it) }.toMap()
}
