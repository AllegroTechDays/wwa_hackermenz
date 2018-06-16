package home.antonyaskiv.hackaton.View

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import home.antonyaskiv.hackaton.Model.LocationRequest
import home.antonyaskiv.hackaton.Model.Request
import home.antonyaskiv.hackaton.Model.Response
import home.antonyaskiv.hackaton.Presenters.MainPresenter
import home.antonyaskiv.hackaton.R
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    var mainPresenter = MainPresenter(this)
    lateinit var type: TypeOf
    private var lng_to: String? = null

    private var lat_to: String? = null

    private var lng_from: String? = null

    private var lat_from: String? = null

    private var distance: String? = null

    var googleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar!!.hide()
        if (intent.hasExtra("type")) {
            when (intent.getStringExtra("type")) {
                "random" -> type = TypeOf.Random
                "filter" -> type = TypeOf.Filter
                "speed" -> type = TypeOf.Speed
            }
        }
        when (type) {
            TypeOf.Random -> {
                lng_to = intent.getStringExtra("lng_to")
                lat_to = intent.getStringExtra("lat_to")
                lng_from = intent.getStringExtra("lng_from")
                lat_from = intent.getStringExtra("lat_from")
                if (intent.hasExtra("distance") && intent.getStringExtra("distance") != null) {
                    distance = intent.getStringExtra("distance")
                    mainPresenter.getRandomRoad(Request(locationRequest = LocationRequest(lng_from!!, lat_from!!, lng_to!!, lat_to!!), distance_to = distance, distance_from = "0"))

                } else
                    mainPresenter.getRandomRoad(Request(locationRequest = LocationRequest(lng_from!!, lat_from!!, lng_to!!, lat_to!!)))

            }
            TypeOf.Speed -> {
                lng_to = intent.getStringExtra("lng_to")
                lat_to = intent.getStringExtra("lat_to")
                lng_from = intent.getStringExtra("lng_from")
                lat_from = intent.getStringExtra("lat_from")
                if (intent.hasExtra("distance") && intent.getStringExtra("distance") != null) {
                    distance = intent.getStringExtra("distance")
                    mainPresenter.getSpeedRoad(Request(locationRequest = LocationRequest(lng_from!!, lat_from!!, lng_to!!, lat_to!!), distance_to = distance, distance_from = "0"))

                } else
                    mainPresenter.getSpeedRoad(Request(locationRequest = LocationRequest(lng_from!!, lat_from!!, lng_to!!, lat_to!!)))

            }
            TypeOf.Filter -> {
                lng_to = intent.getStringExtra("lng_to")
                lat_to = intent.getStringExtra("lat_to")
                lng_from = intent.getStringExtra("lng_from")
                lat_from = intent.getStringExtra("lat_from")
                var timeLeft = intent.getStringExtra("timeLeft")
                var timeRight = intent.getStringExtra("timeRight")
                var speedLeft = intent.getStringExtra("speedLeft")
                var speedRight = intent.getStringExtra("speedRight")
                var BikeType = intent.getStringExtra("BikeType")
                if (intent.hasExtra("distance") && intent.getStringExtra("distance") != null) {
                    distance = intent.getStringExtra("distance")
                    mainPresenter.getFilterRoad(Request(
                            locationRequest = LocationRequest(lng_from!!, lat_from!!, lng_to!!, lat_to!!),
                            duration_from = secundToMinutes(timeLeft),
                            duration_to = secundToMinutes(timeRight)
                            , averageSpeed_from = speedLeft,
                            averageSpeed_to = speedRight,
                            bikeType = BikeType, distance_to = distance, distance_from = "0"))
                } else {

                    mainPresenter.getFilterRoad(Request(
                            locationRequest = LocationRequest(lng_from!!, lat_from!!, lng_to!!, lat_to!!),
                            duration_from = secundToMinutes(timeLeft),
                            duration_to = secundToMinutes(timeRight)
                            , averageSpeed_from = speedLeft,
                            averageSpeed_to = speedRight,
                            bikeType = BikeType))
                }
            }

        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        next_way.setOnClickListener {
            mainPresenter.getRandomRoad(Request(locationRequest = LocationRequest(lng_from!!, lat_from!!, lng_to!!, lat_to!!)))

        }

        navigate.setOnClickListener {
            mainPresenter.openNavigation()
        }

    }

    fun secundToMinutes(sec: String): String {
        var k = sec.toInt() * 60
        return k.toString()

    }

    fun ClickRandom(view: View) {
        var request: Request = Request(locationRequest = LocationRequest("21.0472149", "52.2393962", "21.0156042", "52.2243564"))

        mainPresenter.getRandomRoad(request)

    }

    fun showDetailes(res: Response) {
        value_distance.text = res.distance.toString()
        value_time.text = res.duration.toString()
        value_speed.text = res.averageSpeed.toString()

    }

    fun drawLines(coordination: List<List<Double>>) {
        for (i in 1..(coordination.size - 1)) {
            googleMap!!.addPolyline(PolylineOptions().add(LatLng(coordination.get(i - 1).get(1), coordination.get(i - 1).get(0)),
                    LatLng(coordination.get(i).get(1), coordination.get(i).get(0)))
                    .width(5F)
                    .color(Color.BLUE))
        }
        val location = CameraUpdateFactory.newLatLngZoom(LatLng(coordination.get(0).get(1), coordination.get(0).get(0)), 10f)
        googleMap!!.moveCamera(location)
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
    }

    fun showToast() {
        Toast.makeText(this, "Some  Error ", Toast.LENGTH_SHORT).show()
    }

}

enum class TypeOf {
    Random,
    Filter,
    Speed
}