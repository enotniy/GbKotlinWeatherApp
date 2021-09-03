package com.gb.lesson2.ui.main.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gb.lesson2.R
import com.gb.lesson2.databinding.DetailFragmentBinding
import com.gb.lesson2.databinding.MainFragmentBinding
import com.gb.lesson2.ui.main.model.Weather
import com.gb.lesson2.ui.main.viewmodel.MainViewModel
import com.gb.lesson2.ui.main.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

class DetailFragment : Fragment() {

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.getParcelable<Weather>(WEATHER_EXTRA)

        if (weather != null) {
            binding.message.text = "${weather.city.name}" +
                    "\n lat/lon ${weather.city.lat} ${weather.city.lon}" +
                    "\n температура ${weather.temperature}" +
                    "\n ощущается как ${weather.feelsLike}"
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}