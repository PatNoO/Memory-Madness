package com.example.memory_madness.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.Fragments.game_play.EasyFragment
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
    ): View? {
        binding = FragmentHomeMenuBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnStartFhm.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, EasyFragment(), "fragment_easy")
                commit()
            }
        }

        binding.btnDifficultyFhm.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fcv_game_plan_am, DifficultyFragment(), " fragmen_difficulty")
                commit()
            }
        }

        binding.btnEndgameFhm.setOnClickListener {
            requireActivity().finish()
        }
    }

}