package com.example.testapptwo

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.testapptwo.entity.User
import com.example.testapptwo.repository.DataBaseHandler
import kotlinx.android.synthetic.main.activity_menu_point.*
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MenuPoint : AppCompatActivity() {

    private val client = OkHttpClient()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_point)
        val message = intent.getStringExtra(MENU_POINT_MESSAGE)
        val textView = findViewById<TextView>(R.id.textViewPoint).apply {
            text = message
        }

        val context = this
        val db = DataBaseHandler(context)
        buttonWrite.setOnClickListener {
            if (textViewName.text.toString().isNotEmpty() &&
                    textViewAge.text.toString().isNotEmpty()
            ) {
                val user = User()
                user.name = textViewName.text.toString()
                user.age = textViewAge.text.toString().toInt()
                db.insertData(user)
            }
            else {
                Toast.makeText(context, "Please Fill All Data's", Toast.LENGTH_SHORT).show()
            }
        }
        buttonRead.setOnClickListener {
            val data = db.readData()
            textViewInfo.text = SpannableStringBuilder("")
            for (i in 0 until data.size) {
                textViewInfo.append(
                        data[i].id.toString() + " " + data[i].name + " " + data[i].age + "\n"
                )
            }
        }

        buttonSend.setOnClickListener {
            sendGet("https://api.github.com/users/Evin1-/repos")
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sendGet(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                textViewInfo.text = SpannableStringBuilder(response.body()?.string())
            }
        //println(response.body()?.string())
        })
    }
}