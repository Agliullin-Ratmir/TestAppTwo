package com.example.testapptwo

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.testapptwo.views.CustomComponent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom.view.*

const val MENU_POINT_MESSAGE = "com.example.testapptwo.MENU_POINT"

class DisplayMessageActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)
        var message = intent.getStringExtra(EXTRA_MESSAGE)
        if (message != null) {
            saveAmountOfComponents(message.toString().toInt())
        }
        val textView = findViewById<TextView>(R.id.textView2).apply {
            text = message
        }
        var layout = findViewById<LinearLayout>(R.id.Linear)
        if (message == null) {
            message = getAmountOfComponents().toString()
        }
        for (i in 1..message.toString().toInt()) {// set to sharedpreferences message
            var param : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT)
            setCustomComponents(layout, i, param)
        }
    }

    /**
     * set textViews to the ScrollPanel
     */
    fun setTextComponents(layout: LinearLayout, i : Int, param : RelativeLayout.LayoutParams) {
        val textView: TextView = TextView(this)
        textView.id = i
        textView.text = "Text View №" + i
        if (i != 1) {
            param.addRule(RelativeLayout.BELOW, i - 1)
            textView.layoutParams = param
        }
        layout.addView(textView)
    }

    fun saveAmountOfComponents(amount : Int) {
        val sharedPreference =  getSharedPreferences("AMOUNT",Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.clear()
        editor.remove("amount")
        editor.putInt("amount", amount)
        editor.commit()
    }

    fun getAmountOfComponents() : Int {
        val sharedPreference =  getSharedPreferences("AMOUNT",Context.MODE_PRIVATE)
        return sharedPreference.getInt("amount",1)
    }

    /**
     * set CustomComponents to the ScrollPanel
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setCustomComponents(layout: LinearLayout, i : Int, param : RelativeLayout.LayoutParams) {
        var customLayout = CustomComponent(this, null)
        customLayout.my_title.text = "customLayout title №" + i
        var editable = SpannableStringBuilder("customLayout edit №" + i )
        customLayout.my_edit.text = editable
        if (i != 1) {
            param.addRule(RelativeLayout.BELOW, i - 1)
            customLayout.layoutParams = param
        }
        registerForContextMenu(customLayout)

        customLayout.setOnLongClickListener {
            openContextMenu(customLayout)
            true
        }
        customLayout.id = i
        layout.addView(customLayout)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle("Context Menu")
        menu?.add(0, v?.id!!, 0, "Call" + v?.id)
        menu?.add(0, v?.id!!, 1, "SMS" + v?.id)
        menu?.add(1, v?.id!!, 0, "Search" + v?.id)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when {
            item?.title.contains("Call") -> {
                val intentMenuPoint = Intent(this, MenuPoint::class.java).apply {
                    putExtra(MENU_POINT_MESSAGE, item?.title)
                }
                startActivity(intentMenuPoint)
                Toast.makeText(applicationContext, item?.title, Toast.LENGTH_LONG).show()
                return true
            }
            item?.title.contains("SMS") -> {
                Toast.makeText(applicationContext, item?.title, Toast.LENGTH_LONG).show()
                return true
            }
            item?.title.contains("Search") -> {
                Toast.makeText(applicationContext, item?.title, Toast.LENGTH_LONG).show()
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
}