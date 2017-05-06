package com.moneydance.modules.features.mundane.subfeature

interface Storage<T> {

    fun set(value: T): Unit

    fun update(updater: (T) -> T)

    fun get(): T

}

class JsonLocalStorage<T>(
    private val key: String,
    private val default: () -> T,
    private val context: SubFeatureContext
) : Storage<T> {

    private val localStorage = context.currentAccountBook.localStorage

    private var cached: T? = null

    override fun set(value: T) {
        val toJson = "" // todo: Transform `value` into JSON
        context.info("Saving to local storage with key \"$key\" and value $toJson")
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
            val jsonDeserializationSuccess = true // todo: Parse `content` as JSON and deserialize to `T`
            if (jsonDeserializationSuccess) {
                val deserializedContent = default()
                context.info("Loaded local storage from key \"$key\" Read value: $deserializedContent")
                deserializedContent
            } else {
                val d = default()
                context.info("Failed to parse settings from local storage under key \"$key\" with value $content")
                d
            }
        } ?: default()
        cached = storageValue
        return storageValue
    }

}
