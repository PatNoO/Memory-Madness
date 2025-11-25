package com.example.memory_madness.Fragments.game_play

import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.memory_madness.CardManager
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentEasyBinding


class EasyFragment : Fragment() {

    private lateinit var binding: FragmentEasyBinding

    private val cardId: MutableList<Int> = mutableListOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5,
        R.drawable.card6
    )

    private var isTurnedOver: Boolean = false
    private var isMatched: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEasyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val containerCard = listOf(
            binding.card1Fe,
            binding.card2Fe,
            binding.card3Fe,
            binding.card4Fe,
            binding.card5Fe,
            binding.card6Fe,
            binding.card7Fe,
            binding.card8Fe,
            binding.card9Fe,
            binding.card10Fe,
            binding.card11Fe,
            binding.card12Fe
        )

        val shuffledCardIds = ArrayList<Int>()
        for (id in cardId) {
            shuffledCardIds.add(id)
            shuffledCardIds.add(id)
            Log.i("!!!", "Id images : ${id}")
        }

        shuffledCardIds.shuffle()


        for (i in shuffledCardIds.indices) {
            val imageView: ImageView = containerCard[i]
            val imageId: Int = shuffledCardIds[i]
            val cardInfo: CardManager = CardManager(
                isFlipped = false,
                isMatched = false,
                cardId = imageId,
                containerId = imageView
            )

            imageView.tag = cardInfo
        }

        var firstCard: CardManager
        var secondCard: CardManager
        for (imageView in containerCard) {
            imageView.setOnClickListener { view ->

            }

        }
    }
}






