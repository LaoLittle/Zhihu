package org.laolittle.plugin

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object Zhihu : KotlinPlugin(
    JvmPluginDescription(
        id = "org.laolittle.plugin",
        name = "Zhihu",
        version = "1.0",
    ) {
        author("LaoLittle")
    }
) {
    override fun onEnable() {

    }
}