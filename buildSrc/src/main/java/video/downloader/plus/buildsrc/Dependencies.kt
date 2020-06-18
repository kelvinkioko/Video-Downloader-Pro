package video.downloader.plus.buildsrc

object Dependencies {

    object AndroidBuildTools {
        const val version = "3.6.3"
        const val artifact = "com.android.tools.build:gradle"
        const val gradle_plugin = "$artifact:$version"
    }

    /*
     * Under normal simple implementation this is how I do it
     * implementation 'androidx.appcompat:appcompat:1.1.0'
     *
     * But now with this form of gradle dependency management (From here on referred to as GDM) this is how I do it
     * P.S at the point of doing this I haven't come across a faster or automated way of doing this so for now my fingers will have to toil as hard as possible
     *
     * For another example scroll down to retrofit
     */

    const val junit = "junit:junit:4.13"

    const val okhttp = "com.squareup.okhttp3:okhttp:3.12.1"

    const val cicView = "de.hdodenhof:circleimageview:2.2.0"

    object AndroidX {
        const val core_ktx = "androidx.core:core-ktx:1.3.0"
        const val appcompat = "androidx.appcompat:appcompat:1.1.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"

        const val ext_junit = "androidx.test.ext:junit:1.1.1"
        const val espresso =  "androidx.test.espresso:espresso-core:3.2.0"

        object Lifecycle {
            private const val lifecycle_version = "2.2.0"

            const val extensions = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
            const val compiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
            const val common = "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
        }

        object Navigation {
            private const val version = "2.3.0-alpha06"

            const val dynamic_features_fragment = "androidx.navigation:navigation-dynamic-features-fragment:$version"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val test = "androidx.navigation:navigation-testing:$version"
            const val safe_args_gradle_plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Recycler {
            // RecyclerView
            private const val recyclerview_version = "1.2.0-alpha03"
            const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerview_version"
        }
    }

    object Koin {
        private const val version = "2.1.5"
        const val core = "org.koin:koin-core:$version"
        const val android_core = "org.koin:koin-android-scope:$version"
        const val viewmodel = "org.koin:koin-android-viewmodel:$version"
        const val test = "org.koin:koin-core:$version"
    }

    object Kotlin {
        private const val version = "1.3.72"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"

        object Coroutines {
            private const val version = "1.3.5"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val android ="org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        }

        object GradlePlugin {
            const val artifact = "org.jetbrains.kotlin:kotlin-gradle-plugin"
            const val gradle_plugin = "$artifact:$version"

        }
    }

    /*
     * Before (GDM)
     * // -- Retrofit2
     * def retrofit2_version = "2.6.0"
     * implementation "com.squareup.retrofit2:retrofit:$retrofit2_version"
     * implementation "com.squareup.retrofit2:converter-gson:$retrofit2_version"
     *
     * After GDM SEE BELOW
     */

    object Moshi {
        private const val version = "1.9.2"
        const val core = "com.squareup.moshi:moshi:$version"
        const val kotlin_codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
    }

    object Retrofit {
        // -- Retrofit2
        private const val retrofit2_version = "2.8.1"
        const val retrofit =  "com.squareup.retrofit2:retrofit:$retrofit2_version"
        const val converter =  "com.squareup.retrofit2:converter-gson:$retrofit2_version"
        const val moshi_converter =  "com.squareup.retrofit2:converter-moshi:$retrofit2_version"
    }

    object RoomDB {
        // -- Room DB
        private const val room_version = "1.1.1"
        const val room_runtime = "android.arch.persistence.room:runtime:$room_version"
        const val room_compiler =  "android.arch.persistence.room:compiler:$room_version"
    }

    object Material {
        private const val material_version = "1.1.0"
        const val google_material = "com.google.android.material:material:$material_version"
    }

    object Glide {
        //glide
        private const val glide_version = "4.11.0"
        const val glide = "com.github.bumptech.glide:glide:$glide_version"
        const val compiler = "com.github.bumptech.glide:compiler:$glide_version"
    }

    object Canary {
        // Leak Canary (detecting memory leaks)
        private const val leak_canary_version = "2.0-alpha-3"
        const val leak_canary = "com.squareup.leakcanary:leakcanary-android:$leak_canary_version"
    }

    private const val flowbinding_version = "0.11.1"
    const val FlowBinding = "io.github.reactivecircus.flowbinding:flowbinding-android:$flowbinding_version"

}