package com.example.janken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.janken.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        binding.gu.setOnClickListener{tapped(it)}
        binding.choki.setOnClickListener{tapped(it)}
        binding.pa.setOnClickListener { tapped(it) }

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit {
            clear()
        }


    }


    fun tapped(view: View?){
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("myhand",view?.id)
        startActivity(intent)
    }
}
