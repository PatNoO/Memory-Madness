package com.example.memory_madness.Fragments.game_play

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.memory_madness.CardManager
import com.example.memory_madness.R
import com.example.memory_madness.databinding.FragmentEasyBinding


class EasyFragment : Fragment() {

    private lateinit var binding: FragmentEasyBinding

    private var cardInfo: CardManager = CardManager(
        isFlipped = false,
        isMatched = false,
        cardId = 0
    )
    private val imagesId: MutableList<Int> = mutableListOf(R.drawable.card1,R.drawable.card2,R.drawable.card3,R.drawable.card4,R.drawable.card5,
        R.drawable.card6)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEasyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val containerCard = listOf(binding.card1Fe,binding.card2Fe,binding.card3Fe,binding.card4Fe,binding.card5Fe,binding.card6Fe,
            binding.card7Fe,binding.card8Fe,binding.card9Fe,binding.card10Fe,binding.card11Fe,binding.card12Fe)

        val shuffledCardIds = ArrayList<Int>()
        for (id in imagesId) {
            shuffledCardIds.add(id)
            shuffledCardIds.add(id)
        }

        shuffledCardIds.shuffle()


        for ( i in 0 until shuffledCardIds.size) {
            val imageId = shuffledCardIds[i]
            containerCard [i].tag = imageId
            val tag = containerCard [i].tag
            Log.i("!!!", "Tag : ${tag} ")
        }

        var clicks = 0
            for (i in 0 until containerCard.size){
                containerCard[i].setOnClickListener {

                    if (!containerCard[i].equals(cardInfo.isFlipped) || !containerCard[i].equals(cardInfo.isMatched) ) {
                        containerCard[i].setBackgroundResource(shuffledCardIds[i])
                        cardInfo.isMatched = true
                        clicks++
                    }


                }

        }


    }

    fun shuffleCards (containerCard: List<ImageView>) {
//        var shuffledCardIds = ArrayList<Int>()
//        for (id in imagesId) {
//            shuffledCardIds.add(id)
//            shuffledCardIds.add(id)
//        }
//        shuffledCardIds.shuffle()
//        shuffledCardIds = imagesId as ArrayList<Int>
//
//        setCardIdOnCardContainer(shuffledCardIds,containerCard)
    }

    fun setCardIdOnCardContainer (shuffledCardIds : ArrayList<Int>,containerCard: List<ImageView>  ) {

//       for ( i in 0 until shuffledCardIds.size) {
//           val imageId = shuffledCardIds[i]
//           containerCard [i].tag = imageId
//           val tag = containerCard [i].tag
//           Log.i("!!!", "Tag : ${tag} ")
//       }

    }


}