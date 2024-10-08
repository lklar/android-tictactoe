package com.example.tictactoe.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentNameSelectionScreenBinding

class NameSelectionScreen : Fragment() {

    companion object {
        fun newInstance() = MainScreen()
    }

    private val viewModel: GameViewModel by activityViewModels()

    private lateinit var binding: FragmentNameSelectionScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.playerNames.observe(viewLifecycleOwner, Observer<Pair<String,String>> { (name1, name2) ->
            binding.p1NameSelection.setText(name1)
            binding.p2NameSelection.setText(name2)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_name_selection_screen, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.startButton.setOnClickListener {
            viewModel.setPlayerNames(
                binding.p1NameSelection.text.toString(),
                binding.p2NameSelection.text.toString()
            )
            viewModel.startGame()
            findNavController().navigate(R.id.action_nameSelectionScreen_to_mainScreen)
        }

        binding.aboutButton.setOnClickListener {
            findNavController().navigate(R.id.action_nameSelectionScreen_to_aboutScreen)
        }

//        binding.p1NameSelection.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                viewModel.setPlayerNames(s.toString(), null)
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//
//        binding.p2NameSelection.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                viewModel.setPlayerNames(null, s.toString())
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })

        return binding.root
    }

}