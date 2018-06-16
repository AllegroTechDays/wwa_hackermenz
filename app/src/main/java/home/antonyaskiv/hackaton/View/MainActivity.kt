package home.antonyaskiv.hackaton

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.model.LatLng
import home.antonyaskiv.hackaton.Model.LocationRequest
import home.antonyaskiv.hackaton.Model.Request
import home.antonyaskiv.hackaton.Presenters.MainPresenter
import home.antonyaskiv.hackaton.View.CategoryActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var destination: LatLng? = null

    private var tabActive: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tab_distance.setOnClickListener { v ->
            ll_distance.visibility = View.VISIBLE
            ll_destination.visibility = View.GONE
            tabActive = 0
        }

        tab_destination.setOnClickListener { v ->
            ll_distance.visibility = View.GONE
            ll_destination.visibility = View.VISIBLE
            tabActive = 1
        }

        var autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                destination = LatLng(place.latLng.latitude, place.latLng.longitude)
                Log.i("TagTest", "Place: " + place.name)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("TagTest", "An error occurred: $status")
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 1) {
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
        var intent = Intent(this, CategoryActivity::class.java)
        if (tabActive == 0) {
            var distance = et_distance.text
            var withReturn = cb_with_return.isChecked
            var sex = rg_sex.checkedRadioButtonId
            intent.putExtra("distance", distance)
            intent.putExtra("withReturn", withReturn)
            intent.putExtra("sex", checkSex())
            startActivity(intent)



        } else if (tabActive == 1 && destination != null) {

            intent.putExtra("sex", checkSex())
            intent.putExtra("lng", destination!!.longitude.toString())
            intent.putExtra("lat", destination!!.latitude.toString())
            startActivity(intent)
        } else
            Toast.makeText(this, "Error with data", Toast.LENGTH_SHORT).show()
    }

    private fun checkSex() : String {
        return "M"
    }


}
