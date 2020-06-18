package video.downloader.plus.common.di

import video.downloader.plus.common.configuration.Environment
import org.koin.dsl.module

val environmentModule = module {
    single<Environment> { Environment.Production }
}