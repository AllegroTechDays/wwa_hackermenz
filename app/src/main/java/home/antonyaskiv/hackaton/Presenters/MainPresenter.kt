package home.antonyaskiv.hackaton.Presenters

import android.content.Intent
import android.net.Uri
import android.util.Log
import home.antonyaskiv.hackaton.API.CallBackResponse
import home.antonyaskiv.hackaton.API.HCallBack
import home.antonyaskiv.hackaton.Application.App
import home.antonyaskiv.hackaton.Model.FinishedAtDayNumber
import home.antonyaskiv.hackaton.Model.LocationRequest
import home.antonyaskiv.hackaton.Model.Request
import home.antonyaskiv.hackaton.Model.Response
import home.antonyaskiv.hackaton.View.MapActivity
import java.util.*


/**
 * Created by AntonYaskiv on 16.06.2018.
 */
class MainPresenter(val activity: MapActivity) {
    fun getActivities(value: Int, request: Request) {

        App.service!!.getActivitys(fromLocationToHashMap(request.locationRequest))
                .enqueue(HCallBack(object : CallBackResponse<List<Response>>() {
                    override fun onSuccess(t: List<Response>) {
                        startMaps(t[value].path.coordinates)
                    }

                    override fun onError(code: Int) {
                        Log.d("Tag", "Error")
                    }

                    override fun onFail() {
                        Log.d("Tag", "Fail")
                    }
                }))
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) + start

    var s_request: Request? = null
    var coordinate: List<List<Double>>? = null
    fun getRandomRoad(request: Request) {
        s_request = request
        App.service!!.getActivitys(fromParametersToMap(request))
                .enqueue(HCallBack(object : CallBackResponse<List<Response>>() {
                    override fun onSuccess(t: List<Response>) {
                        if (t.size > 0) {
                            var res = t[(0..t.size).random()]
                            activity.showDetailes(res)
                            coordinate = res.path.coordinates
                        } else {
                            activity.showToast();
                        }
                        //TODO MAp
                        // startMaps(t[(0..t.size).random()].path.coordinates)
                    }

                    override fun onError(code: Int) {
                        Log.d("Tag", "Error")
                    }

                    override fun onFail() {
                        Log.d("Tag", "Fail")
                    }
                }))
    }

    companion object {
        var id = 0
    }

    fun getSpeedRoad(request: Request) {
        s_request = request
        App.service!!.getActivitys(fromParametersToMap(request))
                .enqueue(HCallBack(object : CallBackResponse<List<Response>>() {
                    override fun onSuccess(t: List<Response>) {
                        var min_duration = 100000

                        var count = 0;
                        for (res in t) {
                            if (res.duration < min_duration) {
                                min_duration = res.duration
                                id = count;
                            }

                            count++
                        }
                        if (t.isNotEmpty()) {
                            var res = t[id]
                            activity.showDetailes(res)
                            coordinate = res.path.coordinates
                        } else {
                            activity.showToast();
                        }
                        //TODO MAp
                        // startMaps(t[(0..t.size).random()].path.coordinates)
                    }

                    override fun onError(code: Int) {
                        Log.d("Tag", "Error")
                    }

                    override fun onFail() {
                        Log.d("Tag", "Fail")
                    }
                }))
    }

    fun getFilterRoad(request: Request) {
        s_request = request
        App.service!!.getActivitys(fromParametersToMap(request))
                .enqueue(HCallBack(object : CallBackResponse<List<Response>>() {
                    override fun onSuccess(t: List<Response>) {
                        if (t.size > 0) {
                            var res = t[(0..t.size).random()]
                            activity.showDetailes(res)
                            coordinate = res.path.coordinates
                        } else {
                            activity.showToast();
                        }
                        //TODO MAp
                        // startMaps(t[(0..t.size).random()].path.coordinates)
                    }

                    override fun onError(code: Int) {
                        Log.d("Tag", "Error")
                    }

                    override fun onFail() {
                        Log.d("Tag", "Fail")
                    }
                }))
    }

    private fun startMaps(t: List<List<Double>>) {
        var count = 0
        if (t.isNotEmpty()) {
            var address = "http://maps.google.com/maps?saddr=" + s_request!!.locationRequest.latitude_from + "," + s_request!!.locationRequest.longitude_from + "&daddr=" + t[0][1].toString() + "," + t[0][0].toString()

            for (i in 1..(t.size - 1)) {
                var k = (t.size - 1) / 20
                if (i % k == 0 && count < 20) {
                    count++
                    address += "+to:" + t[i][1].toString() + "," + t[i][0].toString()
                }
            }
            address += "+to:" + s_request!!.locationRequest.latitude_to + "," + s_request!!.locationRequest.longitude_to

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
            activity.startActivity(intent)

        }
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

    fun fromParametersToMap(request: Request): HashMap<String, String> {
        var map: HashMap<String, String> = HashMap()
        //var longitude_from: String, var latitude_from: String, var longitude_to: String, var latitude_to: String
        map["location[longitude_from]"] = request.locationRequest.longitude_from
        map["location[latitude_from]"] = request.locationRequest.latitude_from
        map["location[longitude_to]"] = request.locationRequest.longitude_to
        map["location[latitude_to]"] = request.locationRequest.latitude_to
        map = ifnotnulladd(map, request)
        return map
    }

    private fun ifnotnulladd(hashMap: HashMap<String, String>, request: Request): HashMap<String, String> {

        if (request.distance_from != null)
            hashMap["distance_from"] = request.distance_from!!
        if (request.distance_to != null)
            hashMap["distance_to"] = request.distance_to!!
        if (request.duration_from != null)
            hashMap["duration_from"] = request.duration_from!!
        if (request.duration_to != null)
            hashMap["duration_to"] = request.duration_to!!
        if (request.maxSpeed_from != null)
            hashMap["maxSpeed_from"] = request.maxSpeed_from!!
        if (request.maxSpeed_to != null)
            hashMap["maxSpeed_to"] = request.maxSpeed_to!!
        if (request.averageSpeed_from != null)
            hashMap["averageSpeed_from"] = request.averageSpeed_from!!
        if (request.averageSpeed_to != null)
            hashMap["averageSpeed_to"] = request.averageSpeed_to!!
        if (request.sourceMetadataRequest != null && request.sourceMetadataRequest!!.finishedAtDayNumber != null) {
            var k = ""
            for (a in request.sourceMetadataRequest!!.finishedAtDayNumber!!) {
                when (a) {
                    FinishedAtDayNumber.Zero -> k += "$a,"
                    FinishedAtDayNumber.One -> k += "$a,"
                    FinishedAtDayNumber.Two -> k += "$a"
                }
            }
            hashMap["sourceMetadata:finishedAtDayNumber"] = k
        }
        if (request.bikeType != null) {
            var k = request.bikeType
            var K = k!!.toUpperCase()
            hashMap["bikeType"] = "BIKE_$K"
        }
        if (request.sourceMetadataRequest != null && request.sourceMetadataRequest!!.createdAt_from != null)
            hashMap["sourceMetadata:createdAt_from"] = request.sourceMetadataRequest!!.createdAt_from!!
        if (request.sourceMetadataRequest != null && request.sourceMetadataRequest!!.createdAt_to != null)
            hashMap["sourceMetadata:createdAt_to"] = request.sourceMetadataRequest!!.createdAt_to!!

        if (request.userMetadataRequest != null && request.userMetadataRequest!!.sex != null)
            hashMap["userMetadata:sex"] = request.userMetadataRequest!!.sex!!.name
        return hashMap


    }

    fun openNavigation() {
        startMaps(coordinate!!)
    }
}