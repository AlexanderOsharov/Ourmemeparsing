package com.example.ourmemeparsing

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ourmemeparsing.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val start_btn: Button = findViewById(R.id.button1)
//        start_btn.setOnClickListener(View.OnClickListener {
//            try {
//                parse.parsing()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        })
//    }

    private val binding : ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configureViews()
    }

    private fun configureViews() {
        with(binding) {
            button1.setOnClickListener(onClick2)
        }
    }

    private val onCLick1 = OnClickListener {
        lifecycleScope.launch {
            parse.idealWayWithWorkNotOnMainThread()
        }
    }

    /** Еще вариант без корутин стандартными средствами Java и Android **/

    private val handler = Handler(Looper.getMainLooper())

    private val onClick2 = OnClickListener {
        thread(start = true) {
            parse.parsingWithNoNullable2()
            /** По ТЗ это не нужно, просто демонстрация,
             * как быть если из нового потока нужно было бы данные отправить
             * на главный поток (допустим, для отрисовки UI (через handler)
             */
            handler.post {
                Toast.makeText(this, "Logged the data successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}