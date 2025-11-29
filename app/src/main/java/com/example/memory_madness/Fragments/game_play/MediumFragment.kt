package com.example.memory_madness.Fragments.game_play

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.CardManager
import com.example.memory_madness.GameViewModel
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentMediumBinding


class MediumFragment : Fragment() {

    private lateinit var binding : FragmentMediumBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var playerViewModel : PlayerViewModel

    private val cardId : MutableList<Int> = mutableListOf(R.drawable.card1,R.drawable.card2,R.drawable.card3,R.drawable.card4,R.drawable.card5,
        R.drawable.card6,R.drawable.card7,R.drawable.card8,R.drawable.card9)

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

        val shuffledCardId = ArrayList<Int>()
        for (i in cardId) {
            shuffledCardId.add(i)
            shuffledCardId.add(i)
        }
        shuffledCardId.shuffle()

        for ( i in shuffledCardId.indices){
            val imageViewId = containerId[i]
            val imageId = shuffledCardId[i]
            val cardInfo = CardManager (
                isFlipped = false,
                isMatched = false,
                containerId = imageViewId,
                cardId = imageId
            )
            imageViewId.tag = cardInfo
        }

        for (imageViewId in containerId ) {
            imageViewId.setOnClickListener { view ->

                gameViewModel.currentCard.value = view.tag as CardManager

                gameViewModel.currentCard.value?.let { currentCard ->

                    if (currentCard.isFlipped || currentCard.isMatched ) return@setOnClickListener

                    currentCard.containerId.setImageResource(currentCard.cardId)
                    currentCard.isFlipped = true

                    if (gameViewModel.turnedCard.value == null) {
                        gameViewModel.turnedCard.value == currentCard
                        return@setOnClickListener
                    }

                    gameViewModel.increaseMoves()

                    gameViewModel.

                    val turnedCard = gameViewModel.turnedCard.value
                    currentCard.isFlipped = true

                    if (currentCard.cardId == turnedCard!!.cardId)
                        Toast.makeText(requireActivity(), " MATCH ! ", Toast.LENGTH_SHORT).show()
                    currentCard.isMatched = true
                    turnedCard.isMatched = true
                    gameViewModel.turnedCard.value = null

                }


            }
        }

    }

}