@file:JvmName("SwingExtensionsKt")
@file:JvmMultifileClass

package github.adeynack.kotlin.swing

import java.awt.Dimension

/**
 *  DSL for creating a [Dimension] object.
 *
 *      // Example: Inside of a JFrame
 *      // This is equivalent to the Java call: setSize(new Dimension(400, 500))
 *      size = 400 x 500
 *
 */
infix fun Int.x(y: Int) = Dimension(this, y)
