package com.moneydance.modules.features.mundane.subfeature

import java.awt.Image

/**
 * A feature inside of the extension.
 */
interface SubFeature {

    /**
     * @return the name used for this feature in the GUI.
     */
    val name: String

    /**
     * @return a string key identifying the feature (inside of Mundane).
     */
    val key: String
        get() = this.javaClass.simpleName

    /**
     * @return the image for the feature -or- `null` for using a default image.
     */
    val image: Image?
        get() = null

    /**
     * Activate the functionality of the feature.
     *
     * @param context the Moneydance context.
     */
    fun invoke(context: SubFeatureContext)

    /**
     * Warm up the feature. This is called on application event `md:file:opened`.
     * eg: This is the time to check configuration and apply any automatic behaviour (automatically open a frame, start
     * a listener, etc.).
     *
     * @param context the Moneydance context.
     */
    fun initialize(context: SubFeatureContext) {}

    /**
     * Invoked when the mother [com.moneydance.apps.md.controller.FeatureModule]'s `cleanup` method is.
     */
    fun cleanup() {}

}
