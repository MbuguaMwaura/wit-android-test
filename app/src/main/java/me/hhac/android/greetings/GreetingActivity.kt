package me.hhac.android.greetings

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_greeting.*
import me.hhac.android.greetings.models.Greeting
import me.hhac.android.greetings.models.HelloMessage
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient


class GreetingActivity : AppCompatActivity() {

    private var stompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP,
    "ws://${if (isEmulator()) BuildConfig.URL_EMULATOR else BuildConfig.URL_DEVICE}:${BuildConfig.PORT}/gs-guide-websocket/websocket")

    private var disposable : Disposable? = null
    private var disposableSend : Disposable? = null
    private var greetingText = ""

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)

        disposable = stompClient.topic("/topic/greetings")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ topicMessage ->
                val greeting = moshi.adapter(Greeting::class.java).fromJson(topicMessage.payload)
                greetingText += "\n" + greeting?.content
                greeting_text.text = greetingText
            }, {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            })

    }

    override fun onDestroy() {
        disposableSend?.dispose()
        disposable?.dispose()
        stompClient.disconnect()
        super.onDestroy()
    }

    fun onClickConnect(v: View) {
        stompClient.connect()

        connectBtn.isEnabled = false
        disconnectBtn.isEnabled = true
    }

    fun onClickDisconnect(v: View) {
        stompClient.disconnect()

        if (!stompClient.isConnected) {
            connectBtn.isEnabled = true
            disconnectBtn.isEnabled = false
        }
    }

    fun onClickSend(v: View) {

        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)

        if(!stompClient.isConnected) {
            Toast.makeText(this, "Press the Connect button First", Toast.LENGTH_SHORT).show()
            return
        }

        val helloMessage = HelloMessage(hello_message.text.toString())

        val json = moshi.adapter(HelloMessage::class.java).toJson(helloMessage)

        disposableSend?.dispose()
        disposableSend = stompClient.send("/app/hello", json).subscribe()
    }

    private fun isEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator"))
    }
}


