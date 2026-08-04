package project.handson2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import project.handson2.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startChatButton = findViewById<Button>(R.id.startChatButton)
        startChatButton.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }
}