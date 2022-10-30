package com.example.ourmemeparsing

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

object parse {
    @Throws(IOException::class)
    fun parsing() {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var doc: Document? = null
        try {
            doc = Jsoup.connect("https://vk.com/memzavod1523l").maxBodySize(0).get()
            val listNews = doc.body().select(".wall_text")
            for (element in listNews) {
                println(element.text())
            }
        } catch (e: Exception) {
            println(e)
        }
    }

}