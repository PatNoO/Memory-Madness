package com.example.memory_madness.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.GameViewModel
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentWinBinding

class WinFragment : Fragment(R.layout.fragment_win) {

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var binding: FragmentWinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWinBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        gameViewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        playerViewModel.player.observe(viewLifecycleOwner){player ->
            binding.tvStatsFw.text = player.name
        }

        binding.tvMovesFw.text = gameViewModel.moves.value.toString()
        binding.tvTimeFw.text = gameViewModel.timerCount.value.toString()
    }

}