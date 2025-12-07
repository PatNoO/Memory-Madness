package com.example.memory_madness.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.memory_madness.data_class.Player
import com.example.memory_madness.view_model.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentDifficultyBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DifficultyFragment : Fragment() {
    private lateinit var player: Player
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var binding: FragmentDifficultyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        player = playerViewModel.player.value!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDifficultyBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playerViewModel.player.observe(viewLifecycleOwner) { (_, difficulty) ->
            binding.tvDifficultyCurrentFd.text = difficulty
        }


        /**
         * Player sets difficulty yo Easy
         */
        binding.btnEasyFd.setOnClickListener {
            player.difficulty = "easy"
            playerViewModel.setDifficulty(player)
            lifecycleScope.launch {
                delay(800)
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                    commit()
                }
            }
        }
        /**
         * Player sets difficulty yo Medium
         */
        binding.btnMediumFd.setOnClickListener {
            player.difficulty = "medium"
            playerViewModel.setDifficulty(player)
            lifecycleScope.launch {
                delay(800)
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                    commit()
                }
            }

        }
        /**
         * Player sets difficulty yo Hard
         */
        binding.btnHardFd.setOnClickListener {
            player.difficulty = "hard"
            playerViewModel.setDifficulty(player)
            lifecycleScope.launch {
                delay(800)
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                    commit()
                }
            }

        }
        /**
         * Player goes back to home menu fragment
         */
        binding.btnBackFd.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }

    }

}