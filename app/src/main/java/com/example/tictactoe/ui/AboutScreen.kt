package com.example.tictactoe.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.databinding.FragmentAboutScreenBinding
import com.example.tictactoe.BuildConfig
import com.example.tictactoe.R

class AboutScreen : Fragment() {

    companion object {
        fun newInstance() = AboutScreen()
    }

    //private val viewModel: AboutScreenViewModel by viewModels()

    private var _binding: FragmentAboutScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.textVersionDisplay.text = BuildConfig.VERSION_NAME
        val developers = resources.getStringArray(R.array.developers_array)
        binding.textDevelopersDisplay.text = developers.joinToString("\n")
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_aboutScreen_to_nameSelectionScreen)
        }
        return view
    }
}