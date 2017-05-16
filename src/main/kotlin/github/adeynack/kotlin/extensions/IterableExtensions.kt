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

/**
 * Returns a list containing only elements matching one of the given [predicates].
 *
 *     val l = 1..10
 *     l.
 */
inline fun <reified T> Iterable<T>.filterByOneOf(vararg predicates: (T) -> Boolean): List<T> {
    return filter { element: T ->
        predicates.any { pred -> pred(element) }
    }
}

/**
 * Iterate all elements extracted from [f], from 0 to the [Int] it is called upon.
 *
 *     value.countChildren().tabulate(value::getChildrenAt)
 *
 */
inline fun <T> Int.tabulate(crossinline f: (Int) -> T): Iterable<T> {
    val end = this
    return object: Iterable<T> {
        override fun iterator(): Iterator<T> {
            return object : Iterator<T> {

                private var i = 0

                override fun hasNext(): Boolean = i < end

                override fun next(): T =
                    if (hasNext()) {
                        val result = f(i)
                        i += 1
                        result
                    } else {
                        throw NoSuchElementException("All items have been iterated -or- iterator had no element")
                    }
            }
        }
    }
}