package com.example.memory_madness.Fragments.game_play

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.GameViewModel
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentMediumBinding


class MediumFragment : Fragment() {

    private lateinit var binding : FragmentMediumBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var playerViewModel : PlayerViewModel

//    private lateinit var cardId = MutableList(R.drawable.)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        gameViewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediumBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val containerId = listOf(
            binding.card1Fm,
            binding.card2Fm,
            binding.card3Fm,
            binding.card4Fm,
            binding.card5Fm,
            binding.card6Fm,
            binding.card7Fm,
            binding.card8Fm,
            binding.card9Fm,
            binding.card10Fm,
            binding.card11Fm,
            binding.card12Fm,
            binding.card13Fm,
            binding.card14Fm,
            binding.card15Fm,
            binding.card16Fm,
            binding.card17Fm,
            binding.card18Fm
        )
    }

}