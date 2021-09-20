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
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import coil.api.load
import com.gb.lesson2.R
import com.gb.lesson2.databinding.DetailFragmentBinding
import com.gb.lesson2.ui.main.model.*
import com.gb.lesson2.ui.main.viewmodel.AppState
import com.gb.lesson2.ui.main.viewmodel.DetailViewModel
import com.gb.lesson2.ui.main.viewmodel.MainViewModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class DetailFragment : Fragment() {

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment =
            DetailFragment().apply { arguments = bundle }
    }

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
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

        val weather = arguments?.getParcelable(WEATHER_EXTRA) ?: Weather()

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            renderData(state)
        }

        viewModel.getWeatherFromRemoteSource(weather)
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
            }
            is AppState.Success -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                val weather = state.weather.first();

                viewModel.saveWeather(weather)

                with(binding) {
                    imageView.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")

                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    weatherCondition.text = weather.condition

                    cityName.text = weather.city.name
                    cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
                }
            }
            is AppState.Error -> {
                binding.loadingLayout.show()
                binding.mainView.showSnackBar(
                    "Error: ${state.error}",
                    "Reload",
                    { viewModel.getWeatherFromRemoteSource(Weather()) }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}