package home.antonyaskiv.hackaton.View

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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
    var googleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (intent.hasExtra("type")) {
            when (intent.getStringExtra("type")) {
                "random" -> type = TypeOf.Random
                "filter" -> type = TypeOf.Filter
                "speed" -> type = TypeOf.Speed
            }
        }
        when (type) {
            TypeOf.Random -> {
                var lng_to = intent.getStringExtra("lng_to")
                var lat_to = intent.getStringExtra("lat_to")
                var lng_from = intent.getStringExtra("lng_from")
                var lat_from = intent.getStringExtra("lat_from")

                mainPresenter.getRandomRoad(Request(locationRequest = LocationRequest(lng_from,lat_from,lng_to,lat_to)))


            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        next_way.setOnClickListener {

        }

        navigate.setOnClickListener {

        }

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
    fun drawLines(coordination : List<List<Double>>){
        for(i in 1..(coordination.size-1)) {
            googleMap!!.addPolyline(PolylineOptions().add(LatLng(coordination.get(i - 1).get(1), coordination.get(i - 1).get(0)),
                    LatLng(coordination.get(i).get(1), coordination.get(i).get(0)))
                    .width(5F)
                    .color(Color.RED))
        }
        val location = CameraUpdateFactory.newLatLngZoom(LatLng(coordination.get(0).get(1), coordination.get(0).get(0)), 10f)
        googleMap!!.moveCamera(location)
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
    }

}

enum class TypeOf {
    Random,
    Filter,
    Speed
}