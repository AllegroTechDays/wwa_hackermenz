package home.antonyaskiv.hackaton

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import home.antonyaskiv.hackaton.API.CallBackResponse
import home.antonyaskiv.hackaton.API.HCallBack
import home.antonyaskiv.hackaton.Application.App.Companion.service
import home.antonyaskiv.hackaton.Model.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tab_distance.setOnClickListener{ v ->
            ll_distance.visibility = View.VISIBLE
            ll_destination.visibility = View.GONE
            tabActive = 0
        }

        tab_destination.setOnClickListener{ v ->
            ll_distance.visibility = View.GONE
            ll_destination.visibility = View.VISIBLE
            tabActive = 1
        }

        var autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                destination = LatLng(place.latLng.latitude,place.latLng.longitude)
                Log.i("TagTest", "Place: " + place.name)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("TagTest", "An error occurred: $status")
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                Log.i("TagTest", "Place: " + place.name)
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                // TODO: Handle the error.
                Log.i("TagTest", status.statusMessage)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    fun Click(view: View) {
        if(tabActive == 0) {
            var distance = et_distance.text
            var withReturn = cb_with_return.isChecked
            var sex = rg_sex.checkedRadioButtonId
        } else if (tabActive == 1){
            var sex = rg_sex.checkedRadioButtonId
        }
    }

    fun ClickRandom(view: View) {
        var request: Request = Request(locationRequest = LocationRequest("21.0472149", "52.2393962", "21.0156042", "52.2243564"))

        mainPresenter.getRandomRoad(request)

    }

                    override fun onFail() {
                        Log.d("Tag", "Fail")
                    }
                }))
    }


    private fun startMaps(t: List<Response>) {
        var uri = "https://www.google.com/maps/dir/?api=1&origin=52.224356,21.01560&destination=52.249067,21.06931&waypoints="

        var size = t[0].path.coordinates.size
        var step = size / 9
        var i = 0
        do {
            uri += t[0].path.coordinates[i][1].toString() + "," + t[0].path.coordinates[i][0].toString() + "|"
            i = i + step

        } while (i < size)

        uri += "&travelmode=BICYCLING"

        // val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=18.519513,73.868315&destination=18.518496,73.879259&waypoints=18.520561,73.872435|18.519254,73.876614|18.52152,73.877327|18.52019,73.879935&travelmode=driving")
        val gmmIntentUri = Uri.parse(uri)
        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        intent.`package` = "com.google.android.apps.maps"
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            try {
                val unrestrictedIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                startActivity(unrestrictedIntent)
            } catch (innerEx: ActivityNotFoundException) {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show()
            }

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
            var k = ""
            for (a in request.bikeType!!) {
                when (a) {
                    BikeType.City -> k += "BIKE_CITY,"
                    BikeType.Electric -> k += "BIKE_ELECTRIC,"
                    BikeType.Mountain -> k += "BIKE_MOUNTAIN,"
                    BikeType.Road -> k += "BIKE_ROAD,"
                    BikeType.Stationary -> k += "BIKE_STATIONARY,"
                    BikeType.Trekking -> k += "BIKE_TREKKING,"
                }
            }
            hashMap["bikeType"] = k
        }
        if (request.sourceMetadataRequest != null && request.sourceMetadataRequest!!.createdAt_from != null)
            hashMap["sourceMetadata:createdAt_from"] = request.sourceMetadataRequest!!.createdAt_from!!
        if (request.sourceMetadataRequest != null && request.sourceMetadataRequest!!.createdAt_to != null)
            hashMap["sourceMetadata:createdAt_to"] = request.sourceMetadataRequest!!.createdAt_to!!

        if (request.userMetadataRequest != null && request.userMetadataRequest!!.sex != null)
            hashMap["userMetadata:sex"] = request.userMetadataRequest!!.sex!!.name
        return hashMap


    }
}
