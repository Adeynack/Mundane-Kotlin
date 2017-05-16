package com.moneydance.modules.features.mundane.subfeature

import github.adeynack.kotlin.extensions.orElse

interface Storage<T> {

    fun set(value: T): Unit

    fun update(updater: (T) -> T)

    fun get(): T

}

class JsonLocalStorage<T>(
    private val key: String,
    private val default: () -> T,
    private val context: SubFeatureContext,
    private val clazz: Class<T>
) : Storage<T> {

    private val localStorage = context.currentAccountBook.localStorage

    private var cached: T? = null

    override fun set(value: T) {
        val toJson = context.toJson(value)
        context.info("""Saving to local storage with key "$key" and value $toJson""")
        localStorage.put(key, toJson)
        cached = value
    }

    override fun update(updater: (T) -> T) {
        val current = get()
        val modified = updater(current)
        if (modified != current) {
            set(modified)
        }
    }

    override fun get(): T = cached ?: getFromStorage()

    private fun getFromStorage(): T {
        val storageValue = localStorage.getString(key, null)?.let { content ->
            try {
                val value = context.fromJson(content, clazz)
                context.info("""Loaded from local storage from key "$key" Read value: $content""")
                value
            } catch (t: Throwable) {
                val d = default()
                context.error("""Failed to parse value from local storage under key "$key" with value $content. Using default ${context.toJson(d)}""", t)
                d
            }
        } orElse {
            val d = default()
            context.info("""No storage with key "key" found. Using default ${context.toJson(d)}.""")
            d
        }
        cached = storageValue
        return storageValue
    }

}
