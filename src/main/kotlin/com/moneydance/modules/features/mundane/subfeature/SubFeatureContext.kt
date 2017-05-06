package com.moneydance.modules.features.mundane.subfeature

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moneydance.apps.md.controller.FeatureModule
import com.moneydance.apps.md.controller.FeatureModuleContext
import com.moneydance.apps.md.controller.Main
import com.moneydance.apps.md.view.gui.MoneydanceGUI
import github.adeynack.kotlin.extensions.asA

/**
 * The business logic and technical context of a sub-feature.
 *
 * A facade to all utilities a sub-feature needs to perform its duties.
 *
 */
interface SubFeatureContext : FeatureModuleContext, Logger {

    /**
     * Checks if the current [SubFeatureContext] is using [context] as its base.
     */
    fun isBasedOn(context: FeatureModuleContext?): Boolean

    /**
     * Adds a [Logger] object to which every calls to the [Logger] methods on this [SubFeatureContext] will be routed.
     */
    fun addLogger(logger: Logger)

    /**
     * Generates a new [Storage] object.
     *
     * @param subKey identifies the [Storage] in the system (has to be unique for every [Storage], usually the name of
     * the [com.moneydance.modules.features.mundane.subfeature.SubFeature].
     *
     * @param default a function providing the default value of the object to store. This is used when no value already
     * exist for that storage.
     */
    fun <T> getStorage(subKey: String, default: () -> T): Storage<T>

    /**
     * Gets the [MoneydanceGUI] that some UI components of Moneydance need to be created.
     */
    val mdGUI: MoneydanceGUI

    /**
     * Registers a [SubFeature] to Moneydance.
     *
     * @see [FeatureModuleContext.registerFeature]
     */
    fun registerFeature(subFeature: SubFeature)

    /**
     * Converts a JSON [String] to an object of type [T].
     *
     * @param T type of the object to deserialize.
     *
     * @param json the value as a JSON [String].
     * @param valueType type (Java [Class] object]) of the object to serialize.
     *
     * @returns the deserialized value of type [T]. It will return `null` if the JSON value was `null` itself.
     *
     * @throws Exception when the implementation fails to deserialize the value (eg: when the [String] is not valid JSON
     * or if the JSON [String] does not match the properties type [T]).
     *
     */
    fun <T> fromJsonForType(json: String, valueType: Class<T>): T?

    /**
     * Converts an object of type [T] to a JSON [String].
     *
     * @param T type of the object to serialize.
     *
     * @param value the value as its concrete type [T].
     * @param valueType type (Java [Class] object]) of the object to serialize.
     *
     * @return the serialized JSON [String] of the value.
     *
     * @throws Exception when the implementation fails to serialize the value.
     *
     */
    fun <T> toJsonForType(value: T?, valueType: Class<T>): String

}

//
// Some extension functions to avoid passing the type object manually
// todo: Find a way to use `inline fun <reified T>` with interfaces.
//

inline fun <reified T> SubFeatureContext.fromJson(json: String): T? {
    return fromJsonForType(json, T::class.java)
}

inline fun <reified T> SubFeatureContext.toJson(value: T?): String {
    return toJsonForType(value, T::class.java)
}

/**
 * Runtime implementation of [SubFeatureContext].
 */
class MDSubFeatureContext(
    private val baseContext: FeatureModuleContext,
    private val mainFeatureModule: FeatureModule
) : SubFeatureContext,
    FeatureModuleContext by baseContext {

    private var loggers: Set<Logger> = setOf(SystemErrLogger())

    //
    // Logging
    //

    override fun addLogger(logger: Logger) {
        loggers += logger
    }

    override fun info(message: String): Unit = loggers.forEach { it.info(message) }

    override fun error(message: String, error: Throwable?): Unit = loggers.forEach { it.error(message, error) }

    //
    // Storage
    //

    override fun <T> getStorage(subKey: String, default: () -> T): Storage<T> {
        return JsonLocalStorage("Mundane:$subKey", default, this)
    }

    //
    // JSON
    //

    private val gson: Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    override fun <T> fromJsonForType(json: String, valueType: Class<T>): T? {
        return gson.fromJson(json, valueType)
    }

    override fun <T> toJsonForType(value: T?, valueType: Class<T>): String {
        return gson.toJson(value)
    }

//
    // Other
    //

    override fun isBasedOn(context: FeatureModuleContext?): Boolean {
        return baseContext === context
    }

    override val mdGUI: MoneydanceGUI
        get() = baseContext.asA<Main>().ui.asA<MoneydanceGUI>()

    override fun registerFeature(subFeature: SubFeature) {
        info("Registering sub-feature \"${subFeature.key}\" (\"${subFeature.name}\")")
        registerFeature(
            mainFeatureModule,
            subFeature.key,
            subFeature.image ?: mainFeatureModule.iconImage,
            subFeature.name)
    }

}
