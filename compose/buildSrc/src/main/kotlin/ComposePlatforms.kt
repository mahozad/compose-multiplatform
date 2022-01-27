/*
 * Copyright 2020-2021 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

import java.util.*

enum class ComposePlatforms(vararg val alternativeNames: String) {
    KotlinMultiplatform("Common"),
    Desktop("Jvm"),
    AndroidDebug("Android"),
    AndroidRelease("Android"),
    Js("Web"),
    MacosX64("Macos"),
    MacosArm64("Macos"),
    UikitX64("UiKit"),
    UikitArm64("UiKit");

    fun matches(nameCandidate: String): Boolean =
        listOf(name, *alternativeNames).any { it.equals(nameCandidate, ignoreCase = true) }

    companion object {
        // Temporary, only in release/1.1 branch, to allow build on CI
        val ALL = EnumSet.of(
            ComposePlatforms.Desktop
        )

        val JVM_BASED = EnumSet.of(
            ComposePlatforms.Desktop
        )

        /**
         * Maps comma separated list of platforms into a set of [ComposePlatforms]
         * The function is case- and whitespace-insensetive.
         *
         * Special value: all
         */
        fun parse(platformsNames: String): Set<ComposePlatforms> {
            val platforms = EnumSet.noneOf(ComposePlatforms::class.java)
            val unknownNames = arrayListOf<String>()

            for (name in platformsNames.split(",").map { it.trim() }) {
                if (name.equals("all", ignoreCase = true)) {
                    return ALL
                }

                val publication = ALL.firstOrNull { it.matches(name) }
                if (publication != null) {
                    platforms.add(publication)
                } else {
                    unknownNames.add(name)
                }
            }

        // Temporary, only in release/1.1 branch, to allow build on CI
        //   if (unknownNames.isNotEmpty()) {
        //        error("Unknown platforms: ${unknownNames.joinToString(", ")}")
        //    }

            return platforms
        }
    }
}
