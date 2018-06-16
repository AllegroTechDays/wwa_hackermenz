package home.antonyaskiv.hackaton.View

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import home.antonyaskiv.hackaton.R
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        next_way.setOnClickListener {

        }

        navigate.setOnClickListener {

        }

    }

}