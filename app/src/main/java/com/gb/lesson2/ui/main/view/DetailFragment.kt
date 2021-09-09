package com.gb.lesson2.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gb.lesson2.R
import com.gb.lesson2.databinding.DetailFragmentBinding
import com.gb.lesson2.ui.main.model.Weather

class DetailFragment : Fragment() {

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        const val WEATHER_API_KEY = "46fe3a8c-5ac7-4650-8578-40ab8b2a1082"
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Weather>(WEATHER_EXTRA)?.let { weather ->
            weather.city.also { city ->
                binding.city.text = city.name
                binding.lat.text = city.lat.toString()
                binding.lon.text = city.lon.toString()
            }
            with(binding) {
                temperature.text = weather.temperature.toString()
                feelsLike.text = weather.feelsLike.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}