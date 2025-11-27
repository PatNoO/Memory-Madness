package com.example.memory_madness.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.Player
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentDifficultyBinding
import com.example.memory_madness.databinding.FragmentEasyBinding

class DifficultyFragment : Fragment() {
    private var player: Player? = null
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var binding: FragmentDifficultyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        player = playerViewModel.player.value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDifficultyBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEasyFd.setOnClickListener {
            Log.i("!!!", "Player : ${player.toString()}")
        }

        binding.btnMediumFd.setOnClickListener {

        }

        binding.btnHardFd.setOnClickListener {

        }

    }

}