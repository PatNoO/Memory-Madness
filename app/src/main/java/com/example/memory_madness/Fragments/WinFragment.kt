package com.example.memory_madness.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.Fragments.game_play.EasyFragment
import com.example.memory_madness.Fragments.game_play.HardFragment
import com.example.memory_madness.Fragments.game_play.MediumFragment
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

            binding.tvStatsFw.text = "Score"

        val totalMoves = gameViewModel.moves.value
        val totalTime = gameViewModel.timerCount.value


        val minutes = totalTime?.div(60)
        val seconds = totalTime?.rem(60)
        binding.tvMovesFw.text = "Moves : $totalMoves "
        binding.tvTimeFw.text = "Time : $minutes : $seconds"


        binding.btnPlayAgainFw.setOnClickListener {
            gameViewModel.resetCount()
            gameViewModel.resetMoves()
            if (playerViewModel.player.value?.difficulty == "easy"){
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, EasyFragment())
                    commit()
                }
            } else if (playerViewModel.player.value?.difficulty == "medium") {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, MediumFragment())
                    commit()
                }
            }else if (playerViewModel.player.value?.difficulty == "hard") {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, HardFragment())
                    commit()
                }
            } else {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fcv_game_plan_am, EasyFragment())
                    commit()
                }
            }
        }

        binding.btnHomeFw.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HomeMenuFragment())
                commit()
            }
        }
    }

}