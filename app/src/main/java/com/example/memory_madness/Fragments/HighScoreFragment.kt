package com.example.memory_madness.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.databinding.FragmentHightScoreBinding


class HighScoreFragment : Fragment() {

    private lateinit var playerViewModel: PlayerViewModel

    private lateinit var binding : FragmentHightScoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        playerViewModel = ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHightScoreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}