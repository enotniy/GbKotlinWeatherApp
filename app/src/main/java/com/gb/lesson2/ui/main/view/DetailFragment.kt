package com.gb.lesson2.ui.main.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gb.lesson2.R
import com.gb.lesson2.databinding.DetailFragmentBinding
import com.gb.lesson2.ui.main.model.*

class DetailFragment : Fragment() {

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment =
            DetailFragment().apply { arguments = bundle }
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val localResultBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            Log.d("DEBUG", "onReceive message ${Thread.currentThread()}")

            when (intent?.getStringExtra(RESULT_EXTRA)) {
                SUCCESS_RESULT -> {
                    intent.getParcelableExtra<WeatherDTO.FactDTO>(FACT_WEATHER_EXTRA)?.let {
                        displayWeather(it)
                    }
                }
                else -> {
                    binding.mainView.showSnackBar("Error", "Try again", { view ->
                    })
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("DEBUG", "Register Receiver ${Thread.currentThread()}")
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(localResultBroadcastReceiver, IntentFilter(DETAILS_INTENT_FILTER))
    }

    override fun onDestroy() {
        Log.d("DEBUG", "Unregister Receiver ${Thread.currentThread()}")

        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(localResultBroadcastReceiver)
        super.onDestroy()
    }

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

                getWeather(city.lat, city.lon)
            }


//            WeatherLoader(
//                weather.city.lat,
//                weather.city.lon,
//                object : WeatherLoader.WeatherLoaderListener {
//                    override fun onLoaded(weatherDTO: WeatherDTO) {
//                        requireActivity().runOnUiThread {
//                            displayWeather(weatherDTO.)
//                        }
//                    }
//
//                    override fun onFailed(throwable: Throwable) {
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(
//                                requireContext(),
//                                throwable.localizedMessage,
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
//                }).goToInternet()
        }
    }

    private fun getWeather(lat: Double, lon: Double) {
        binding.mainView.hide()
        binding.loadingLayout.show()

        Log.d("DEBUG", "Start service ${Thread.currentThread()}")
        requireActivity().startService(
            Intent(requireContext(), MainService::class.java).apply {
                putExtra(LATITUDE_EXTRA, lat)
                putExtra(LONGITUDE_EXTRA, lon)
            }
        )
    }

    private fun displayWeather(fact: WeatherDTO.FactDTO) {
        binding.mainView.show()
        binding.loadingLayout.hide()

        with(binding) {
            temperatureValue.text = fact.temp.toString()
            feelsLikeValue.text = fact.feels_like.toString()
            weatherCondition.text = fact.condition.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}