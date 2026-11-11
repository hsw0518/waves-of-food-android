package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityLocationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LocationActivity : AppCompatActivity() {
    private var selectedCityIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setAccessibilityHeading(binding.locationHeading, true)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val cities = resources.getStringArray(R.array.delivery_cities)
        selectedCityIndex = (savedInstanceState?.getInt(KEY_CITY) ?: 0).coerceIn(cities.indices)
        binding.citySelector.text = cities[selectedCityIndex]
        binding.citySelector.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.location_heading)
                .setSingleChoiceItems(cities, selectedCityIndex) { dialog, index ->
                    selectedCityIndex = index
                    binding.citySelector.text = cities[index]
                    dialog.dismiss()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CITY, selectedCityIndex)
    }

    private companion object {
        const val KEY_CITY = "selected_city"
    }
}
