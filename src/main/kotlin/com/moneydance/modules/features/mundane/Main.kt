package com.moneydance.modules.features.mundane

import com.moneydance.apps.md.controller.FeatureModule
import javax.swing.JOptionPane
import javax.swing.JOptionPane.INFORMATION_MESSAGE

class Main : FeatureModule() {

    override fun init() {
        super.init()
        context.registerFeature(this, "Mundane", null, "Mundane")
    }

    override fun getName(): String = "Mundane"

    override fun invoke(s: String?) {
        JOptionPane.showMessageDialog(null, "The best plugin ever, invoked with \"$s\".", "Mundane!", INFORMATION_MESSAGE)
    }

}