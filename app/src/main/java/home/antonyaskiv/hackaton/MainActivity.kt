package home.antonyaskiv.hackaton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import home.antonyaskiv.hackaton.API.CallBackResponse
import home.antonyaskiv.hackaton.API.HCallBack
import home.antonyaskiv.hackaton.Application.App.Companion.service
import home.antonyaskiv.hackaton.Model.LocationRequest
import home.antonyaskiv.hackaton.Model.Request
import home.antonyaskiv.hackaton.Model.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getActivities()
    }

    fun getActivities() {
        var request: Request = Request(locationRequest = LocationRequest("21.0472149", "52.2393962", "21.0156042", "52.2243564"))

        service!!.getActivitys(fromLocationToHashMap(request.locationRequest))
                .enqueue(HCallBack(object : CallBackResponse<List<Response>>() {
                    override fun onSuccess(t: List<Response>) {
                        Log.d("Tag", "Yes")
                    }

                    override fun onError(code: Int) {
                        Log.d("Tag", "Error")
                    }

                    override fun onFail() {
                        Log.d("Tag", "Fail")
                    }
                }))
    }

    fun fromLocationToHashMap(locationRequest: LocationRequest): HashMap<String, String> {
        var map: HashMap<String, String> = HashMap()
        //var longitude_from: String, var latitude_from: String, var longitude_to: String, var latitude_to: String
        map["location[longitude_from]"] = locationRequest.longitude_from
        map["location[latitude_from]"] = locationRequest.latitude_from
        map["location[longitude_to]"] = locationRequest.longitude_to
        map["location[latitude_to]"] = locationRequest.latitude_to
        return map
    }
}
