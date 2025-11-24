package com.example.memory_madness

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var player : Player? = Player("Default",0.0,0)
//    private var player2 : Player? = Player("Default2",0.0,0)


    private val startLancher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val playerUpdated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                 result.data?.getSerializableExtra("player_updated", Player ::class.java)
            } else {
                result.data?.getSerializableExtra("player_updated") as Player
            }
            player = playerUpdated
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
       val intent = Intent(this, StartActivity::class.java)
        intent.putExtra("player", player)
        startLancher.launch(intent)
        onPause()
    }
}