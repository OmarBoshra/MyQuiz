package com.example.myquiz.dependencyInjection

import com.example.myquiz.interfaces.ApiInterface
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetroModule {


    val baseURL = "https://app.check24.de/vg2-quiz/"

@Singleton
@Provides
fun getRetrofitInstance (): Retrofit{

    return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseURL)
        .build()

}

@Singleton
@Provides
fun getApiInterface (retrofit:Retrofit): ApiInterface {

    return retrofit.create(ApiInterface::class.java)

}

}