package video.downloader.plus.common.di

import video.downloader.plus.common.configuration.Environment
import video.downloader.plus.domain.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(get<Environment>().url)
            .build()
    }
    single { get<Retrofit>().create(ApiService::class.java) }
}