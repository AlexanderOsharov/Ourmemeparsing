package com.example.ourmemeparsing

import android.os.NetworkOnMainThreadException
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException


/** Зачем object, почему object, за что object???
 * Кажется, простой класс (ParseService) тут подошел бы лучше. Хотя если по задумке тут Singleton
 * просто нужен, то и object пойдет, наверное
 */
object parse {

    /** Есть куча вопросов к этому методу, ниже идеи что и как можно исправить **/
    @Throws(IOException::class)
    fun parsing() {
//        val policy = ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
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

    /** Kotlin-way **/
    fun parsingWithNoNullable1() {
        runCatching {
            val doc = Jsoup.connect("https://vk.com/memzavod1523l").maxBodySize(0).get().apply {
                body().select(".wall_text").forEach { element ->
                    Log.d("ELEMENT", element.text())
                }
            }
        }.onFailure {
            Log.d("EXCEPTION", it.stackTrace.toString())
        }
    }

    fun parsingWithNoNullable2() {
        val news = try {
            Jsoup.connect("https://vk.com/memzavod1523l").maxBodySize(0).get()
                .body().select(".wall_text").forEach { element ->
                    Log.d("ELEMENT", element.text())
                }
        } catch (exception: NetworkOnMainThreadException) {
            Log.d("EXCEPTION", exception.message.toString())
        }
    }

    /** Теперь вернемся к тому, почему же вообще мы ловим тут network on main thread
     * и зачем нужны страшные строчки (а они вообще не нужны и очень даже вредны по множеству причин
     * val policy = ThreadPolicy.Builder().permitAll().build()
     * StrictMode.setThreadPolicy(policy)
     */

    suspend fun idealWayWithWorkNotOnMainThread() {
        val doc = withContext(Dispatchers.IO) {
            Jsoup.connect("https://vk.com/memzavod1523l").maxBodySize(0).get()
        }.apply {
            body().select(".wall_text").forEach { element ->
                Log.d("ELEMENT", element.text())
            }
        }
        /** Не изучал либу, если нужно чистить ресурсы, то просто у doc потом вызовем нужный метод
         * Если не нужно, ниже метод, который не создает лишнюю переменную
         */
    }

    suspend fun idealWayWithWorkNotOnMainThreadVariant2() {
        withContext(Dispatchers.IO) {
            Jsoup.connect("https://vk.com/memzavod1523l").maxBodySize(0).get()
                .body().select(".wall_text").forEach { element ->
                Log.d("ELEMENT", element.text())
            }
        }
    }
}