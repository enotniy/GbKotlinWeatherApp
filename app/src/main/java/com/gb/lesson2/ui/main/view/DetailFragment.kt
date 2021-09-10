package com.gb.lesson2.ui.main.view

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.gb.lesson2.BuildConfig
import com.gb.lesson2.R
import com.gb.lesson2.databinding.DetailFragmentBinding
import com.gb.lesson2.ui.main.model.City
import com.gb.lesson2.ui.main.model.Weather
import com.gb.lesson2.ui.main.model.WeatherDTO
import com.gb.lesson2.ui.main.model.WeatherLoader
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class DetailFragment : Fragment() {

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment =
            DetailFragment().apply { arguments = bundle }
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        _binding = DetailFragmentBinding.bind(view)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Weather>(WEATHER_EXTRA)?.let { weather ->
            weather.city.also { city ->
                binding.cityName.text = city.name
                binding.cityCoordinates.text = "${city.lat} - ${city.lon}"
            }

            WeatherLoader(
                weather.city.lat,
                weather.city.lon,
                object : WeatherLoader.WeatherLoaderListener {
                    override fun onLoaded(weatherDTO: WeatherDTO) {
                        requireActivity().runOnUiThread {
                            displayWeather(weatherDTO)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                throwable.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }).goToInternet()
        }
    }

    private fun displayWeather(weather: WeatherDTO) {
        with(binding) {
            temperatureValue.text = weather.fact?.temp.toString()
            feelsLikeValue.text = weather.fact?.feels_like.toString()
            weatherCondition.text = weather.fact?.condition.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}