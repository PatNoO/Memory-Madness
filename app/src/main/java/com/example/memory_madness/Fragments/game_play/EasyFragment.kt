package com.example.memory_madness.Fragments.game_play


import android.content.Context
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

    interface EasyFragListener {

        fun updatePlayer (moves : Int, time : Double)

    }

    private var ownerActivity : EasyFragListener? = null
    private lateinit var binding: FragmentEasyBinding
    private val cardId: MutableList<Int> = mutableListOf(
        R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4, R.drawable.card5,
        R.drawable.card6
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            ownerActivity = context as EasyFragListener
            Log.i("!!!", "EasyFragListener is Implemented")
        } catch (e: Exception){
            Log.e("!!!", " !! ATTENTION !!  EasyFragListener is NOT Implemented")
        }

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
        }

        shuffledCardIds.shuffle()


        for (i in shuffledCardIds.indices) {
            val imageViewId: ImageView = containerCard[i] // View binding
            val imageId: Int = shuffledCardIds[i]       // Images Drawable
            val cardInfo = CardManager(
                isFlipped = false,
                isMatched = false,
                cardId = imageId,
                containerId = imageViewId
            )

            imageViewId.tag = cardInfo
        }


        var firstCard: CardManager? = null
        var matchCount = 0
        var isBusy = false
        var moves = 0

        for (imageViewId in containerCard) {
            imageViewId.setOnClickListener { view ->


                if (isBusy) {
                    return@setOnClickListener
                }

                val card = view.tag as CardManager

                if (card.isFlipped || card.isMatched) return@setOnClickListener

                card.containerId.setImageResource(card.cardId)
                card.isFlipped = true

                if (firstCard == null) {
                    firstCard = card
                    return@setOnClickListener
                }

                moves++
                binding.tvMovesFe.text = "Time : \n $moves"

                if (firstCard!!.cardId == card.cardId) {
                    Toast.makeText(requireContext(), "Match !", Toast.LENGTH_SHORT).show()

                    firstCard!!.isMatched = true
                    card.isMatched = true
                    firstCard = null
                    matchCount++

                    if (matchCount == 6) {
                        Toast.makeText(requireContext(), "You Won ", Toast.LENGTH_SHORT).show()
                        ownerActivity?.updatePlayer(moves, 0.0)
                    }

                } else {
                    isBusy = true
                    card.containerId.postDelayed(
                        {
                            firstCard!!.containerId.setImageResource(R.drawable.card_backround)
                            card.containerId.setImageResource(R.drawable.card_backround)

                            firstCard!!.isFlipped = false
                            card.isFlipped = false
                            firstCard = null
                            isBusy = false
                        },
                        500
                    )

                }


            }




        }
    }
}






