package home.antonyaskiv.hackaton.Application

import android.app.Application
import home.antonyaskiv.hackaton.API.Api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by AntonYaskiv on 16.06.2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val interceptor = HttpLoggingInterceptor()

        interceptor!!.setLevel(HttpLoggingInterceptor.Level.BODY);
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl("https://api.rowerowapolska.pl/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        service = retrofit!!.create(Api::class.java)
    }

    companion object {
        var retrofit: Retrofit? = null
        var service: Api? = null

    }
}