package com.lourdesibarra.bruflix.data

import com.lourdesibarra.bruflix.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response


class ApiInterceptor : Interceptor {

    companion object {
        const val API_KEY = "api_key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl: HttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        return chain.proceed(requestBuilder.build())
    }
}