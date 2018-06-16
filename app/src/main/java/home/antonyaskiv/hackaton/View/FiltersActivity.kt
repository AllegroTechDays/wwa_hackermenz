package home.antonyaskiv.hackaton.View

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import home.antonyaskiv.hackaton.Model.BikeType
import home.antonyaskiv.hackaton.R
import kotlinx.android.synthetic.main.activity_filters.*

/**
 * Created by AntonYaskiv on 16.06.2018.
 */
class FiltersActivity : AppCompatActivity() {
    var timeLeft: String = "0"
    var timeRight: String = "100"
    var speedLeft: String = "0"
    var speedRight: String = "60"
    lateinit var inte: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_filters)
        inte = Intent(this, MapActivity::class.java)
        inte.putExtra("type", "filter")
        inte.putExtra("lng_to", intent.getStringExtra("lng_to"))
        inte.putExtra("lat_to", intent.getStringExtra("lat_to"))
        inte.putExtra("lng_from", intent.getStringExtra("lng_from"))
        inte.putExtra("lat_from", intent.getStringExtra("lat_from"))
        if (intent.hasExtra("distance") && intent.getStringExtra("distance") != null)
            intent.putExtra("distance", intent.getStringExtra("distance"))

        rangeSeekBar2.setData(listOf(SimpleDataClass("Min", 0), SimpleDataClass("10", 10), SimpleDataClass("20", 20), SimpleDataClass("30", 30), SimpleDataClass("40", 40), SimpleDataClass("50", 50), SimpleDataClass("60", 60), SimpleDataClass("70", 70), SimpleDataClass("80", 80), SimpleDataClass("90", 90), SimpleDataClass("Max", 100)))
        rangeSeekBar1.setData(listOf(SimpleDataClass("Min", 0), SimpleDataClass("10", 10), SimpleDataClass("20", 20), SimpleDataClass("30", 30), SimpleDataClass("40", 40), SimpleDataClass("50", 50), SimpleDataClass("Max", 60)))
        rangeSeekBar2.callbackAction = { bar, leftThumb, rightThumb, fromUser ->
            timeLeft = leftThumb.toString()
            timeRight = rightThumb.toString()
        }
        rangeSeekBar1.callbackAction = { bar, leftThumb, rightThumb, fromUser ->
            speedLeft = leftThumb.toString()
            speedRight = rightThumb.toString()
        }
        spinner_bike.adapter = ArrayAdapter<BikeType>(this, android.R.layout.simple_spinner_item, BikeType.values())
    }

    fun ToMap(view: View) {
        inte.putExtra("timeLeft", timeLeft)
        inte.putExtra("timeRight", timeRight)
        inte.putExtra("speedLeft", speedLeft)
        inte.putExtra("speedRight", speedRight)
        inte.putExtra("BikeType", spinner_bike.selectedItem.toString())
        startActivity(inte)

    }


    data class SimpleDataClass(private val label: String, val value: Int) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readInt())

        override fun toString() = label
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(label)
            parcel.writeInt(value)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SimpleDataClass> {
            override fun createFromParcel(parcel: Parcel): SimpleDataClass {
                return SimpleDataClass(parcel)
            }

            override fun newArray(size: Int): Array<SimpleDataClass?> {
                return arrayOfNulls(size)
            }
        }
    }
}