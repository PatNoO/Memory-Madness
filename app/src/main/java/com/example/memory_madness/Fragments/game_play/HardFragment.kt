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
import com.example.memory_madness.databinding.FragmentHardBinding


class HardFragment : Fragment() {

    private lateinit var binding : FragmentHardBinding

    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var gameViewModel: GameViewModel

    private val cardId: MutableList<Int> = mutableListOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5,
        R.drawable.card6, R.drawable.card7, R.drawable.card8, R.drawable.card9
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        gameViewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var containerId = listOf(
            binding.card1Fh,
            binding.card2Fh,
            binding.card3Fh,
            binding.card4Fh,
            binding.card5Fh,
            binding.card6Fh,
            binding.card7Fh,
            binding.card8Fh,
            binding.card9Fh,
            binding.card10Fh,
            binding.card11Fh,
            binding.card12Fh,
            binding.card13Fh,
            binding.card14Fh,
            binding.card15Fh,
            binding.card16Fh,
            binding.card17Fh,
            binding.card18Fh
        )

        val shuffledCardId = ArrayList<Int>()
        for (i in cardId){
            shuffledCardId[i]
            shuffledCardId[i]
        }
        shuffledCardId.shuffle()


    }



}