package com.gb.lesson2.ui.main.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gb.lesson2.R
import com.gb.lesson2.databinding.MainFragmentBinding
import com.gb.lesson2.ui.main.viewmodel.MainViewModel
import com.gb.lesson2.ui.main.viewmodel.AppState
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val myAdapter: MainAdapter by lazy { MainAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        _binding = MainFragmentBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myAdapter.listener = MainAdapter.OnItemViewClickListener { weather ->
            activity?.supportFragmentManager?.let { fragmentManager ->
                fragmentManager.beginTransaction()
                    .replace(R.id.container, DetailFragment.newInstance(Bundle().apply {
                        putParcelable(DetailFragment.WEATHER_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commit()
            }
        }

        binding.recyclerview.adapter = myAdapter
        binding.mainFragmentFAB.setOnClickListener {
            viewModel.onLanguageChange()
        }

        viewModel.liveData.observe(viewLifecycleOwner, { state ->
            renderData(state)
        })

        viewModel.liveDataIsRus.observe(viewLifecycleOwner, { isRus ->
            if (isRus) {
                binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            } else {
                binding.mainFragmentFAB.setImageResource(R.drawable.ic_world)
            }
            viewModel.getWeatherFromLocalSource()
        })
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> binding.loadingLayout.show()
            is AppState.Success -> {
                binding.loadingLayout.hide()
                myAdapter.weatherData = state.weather
            }
            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.mainFragmentFAB.showSnackBar(
                    "Error: ${state.error}",
                    "Reload",
                    { viewModel.getWeatherFromLocalSource() }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}