package com.example.testapptwo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import android.content.Intent
import android.view.ContextMenu
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

const val EXTRA_MESSAGE = "com.example.testapptwo.MESSAGE"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button : Button = findViewById(R.id.button)
        var textView : TextView = findViewById(R.id.textView)
//        button.setOnClickListener {
//            textView.text = "Hi"
//        }
        registerForContextMenu(btnContextMenu)

        btnContextMenu.setOnLongClickListener {
            openContextMenu(btnContextMenu)
            true
        }
        var custom : TextView = findViewById(R.id.my_title)
        custom.text = "Hi"
    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextOne)
        val message = editText.text.toString()
        val rest = RestTemplate()
        doAsync{
            val resp =
                rest.getForObject("https://translate.google.ru/?sl=en&tl=ar&text=hi&op=translate", String.javaClass)
            // convert resp to response entity
            Toast.makeText(applicationContext, resp.toString(), Toast.LENGTH_LONG).show()
        }
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle("Context Menu")
        menu?.add(0, v?.id!!, 0, "Call")
        menu?.add(0, v?.id!!, 1, "SMS")
        menu?.add(1, v?.id!!, 0, "Search")
    }
}