package home.antonyaskiv.hackaton

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import home.antonyaskiv.hackaton.Model.LocationRequest
import home.antonyaskiv.hackaton.Model.Request
import home.antonyaskiv.hackaton.Presenters.MainPresenter


class MainActivity : AppCompatActivity() {
    private val mainPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun Click(view: View) {
        /*  var request: Request = Request(locationRequest = LocationRequest("21.0472149", "52.2393962", "21.0156042", "52.2243564"))

          mainPresenter.getActivities(0, request)*/
        startActivity(Intent(this, FiltersActivity::class.java))
    }

    fun ClickRandom(view: View) {
        var request: Request = Request(locationRequest = LocationRequest("21.0472149", "52.2393962", "21.0156042", "52.2243564"))

        mainPresenter.getRandomRoad(request)

    }


}
