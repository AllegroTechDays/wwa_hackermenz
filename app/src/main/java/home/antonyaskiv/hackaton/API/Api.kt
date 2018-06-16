package home.antonyaskiv.hackaton.API

import home.antonyaskiv.hackaton.Model.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by AntonYaskiv on 16.06.2018.
 */
interface Api
{
    @GET("activities")
    fun getActivitys(@QueryMap map: HashMap<String,String>) : Call<List<Response>>
}