package com.example.memory_madness.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.Fragments.game_play.EasyFragment
import com.example.memory_madness.Fragments.game_play.HardFragment
import com.example.memory_madness.Fragments.game_play.MediumFragment
import com.example.memory_madness.Player
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentHomeMenuBinding

class HomeMenuFragment : Fragment() {
    private lateinit var binding : FragmentHomeMenuBinding
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeMenuBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Lets player start the game over again
         */
        binding.btnStartFhm.setOnClickListener {
           if (playerViewModel.player.value?.difficulty == "easy"){
               parentFragmentManager.beginTransaction().apply {
                   replace(R.id.fcv_game_plan_am, EasyFragment())
                   commit()
               }
           }else if (playerViewModel.player.value?.difficulty == "medium"){
               parentFragmentManager.beginTransaction().apply {
                   replace(R.id.fcv_game_plan_am, MediumFragment())
                   commit()
               }
           }else if (playerViewModel.player.value?.difficulty == "hard"){
               parentFragmentManager.beginTransaction().apply {
                   replace(R.id.fcv_game_plan_am, HardFragment())
                   commit()
               }
           } else {
               Toast.makeText(requireActivity(), "Välj svårighetsgrad", Toast.LENGTH_SHORT).show()
           }
        }
        /**
         * Player gets sent to difficulty menu fragment
         */
        binding.btnDifficultyFhm.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, DifficultyFragment())
                commit()
            }
        }
        binding.btnHighScoreFhm.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, HighScoreFragment())
                commit()
            }
        }

        /**
         * Player Ends Game and Quits the app overall
         */
        binding.btnEndgameFhm.setOnClickListener {
            requireActivity().finish()
        }
    }

}