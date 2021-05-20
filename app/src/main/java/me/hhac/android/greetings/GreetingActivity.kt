package me.hhac.android.greetings

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_greeting.*
import me.hhac.android.greetings.models.*
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient


class GreetingActivity : AppCompatActivity() {

    private var token = "b3777048-7ea7-417c-a038-e0c38b96e3be"

    var listView: ListView? = null

    var messages : ArrayList<CommentResponseDto> = ArrayList()


    private var stompClient: StompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP,
    "ws://${if (isEmulator()) BuildConfig.URL_EMULATOR else BuildConfig.URL_DEVICE}:${BuildConfig.PORT}/wit-forum/websocket?access_token=${token}")

    private var disposable : Disposable? = null
    private var disposableSend : Disposable? = null
    private var greetingText = ""

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_greeting)
        listView = findViewById(R.id.listview);
        val adapter = ArrayAdapter<CommentResponseDto>(this,R.layout.activity_greeting,messages)
        listView?.adapter = adapter


    }

    override fun onDestroy() {
        disposableSend?.dispose()
        disposable?.dispose()
        stompClient.disconnect()
        super.onDestroy()
    }

    fun onClickConnect(v: View) {
        stompClient.connect()

        disposable = stompClient.topic("/discussion/comment")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ topicMessage ->
                Toast.makeText(this, "message received", Toast.LENGTH_LONG).show()
                val greeting = moshi.adapter(BaseApiResponse::class.java).fromJson(topicMessage.payload)
                greetingText += "\n" + greeting?.data

                val commentReponse = moshi.adapter(CommentResponseDto::class.java).fromJsonValue(greeting?.data)
                greeting_text.text = commentReponse?.commentText
                messages.add(commentReponse!!)
            }, {
                Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_LONG).show()
                greeting_text.text = "ws://${if (isEmulator()) BuildConfig.URL_EMULATOR else BuildConfig.URL_DEVICE}:${BuildConfig.PORT}/wit-forum/websocket?access_token=${token}"

            })

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
        val commentDto = CommentDto(hello_message.text.toString(),3)

        val json = moshi.adapter(CommentDto::class.java).toJson(commentDto)

        disposableSend?.dispose()
        disposableSend = stompClient.send("/wit/user-all", json).subscribe()
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


