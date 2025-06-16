package com.example.task12drawoverotherapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ImageFilterActivity : AppCompatActivity() {
    private lateinit var filterView: FilterImageView
    private lateinit var posterDuf: Button
    private lateinit var lighting: Button
    private lateinit var blend: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_filter)

        filterView = findViewById(R.id.filterImageView)
        posterDuf = findViewById(R.id.btnPorterDuff)
        lighting = findViewById(R.id.btnLighting)
        blend = findViewById(R.id.btnBlendMode)


        posterDuf.setOnClickListener {
            filterView.setFilter("porterduff")
        }

        lighting.setOnClickListener {
            filterView.setFilter("lighting")
        }

        blend.setOnClickListener {
            filterView.setFilter("blendmode")
        }
    }
}