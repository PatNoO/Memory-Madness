package com.example.memory_madness

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.memory_madness.databinding.ActivityStartBinding


class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding
    private lateinit var player : Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        player = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getSerializableExtra("player", Player::class.java)
        } else {
            intent.getSerializableExtra("player") as Player
        })!!

        binding.btnStartAs.setOnClickListener {

            if (binding.etInputNameAs.text.isNullOrEmpty()){
                return@setOnClickListener
            }

            player.name = binding.etInputNameAs.text.toString()

            

        }

    }
}