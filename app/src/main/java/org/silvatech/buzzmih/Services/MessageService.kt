package org.silvatech.buzzmih.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.silvatech.buzzmih.Controller.App
import org.silvatech.buzzmih.Models.Channel
import org.silvatech.buzzmih.Models.Message
import org.silvatech.buzzmih.Utilities.URL_GET_CHANNELS
import org.silvatech.buzzmih.Utilities.URL_GET_MESSAGES

object MessageService {

    val channels = ArrayList<Channel>()
    val message = ArrayList<Message>()


    fun getChannels(complete: (Boolean) -> Unit) {

        val channelsRequest =
            object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener { response ->


                try {

                    for (x in 0 until response.length()) {

                        val channel = response.getJSONObject(x)
                        val name = channel.getString("name")
                        val chanDesc = channel.getString("description")
                        val channelId = channel.getString("_id")

                        val newChannel = Channel(name, chanDesc, channelId)
                        this.channels.add(newChannel)
                    }

                    complete(true)

                } catch (e: JSONException) {
                    Log.d("JSON", "EXE:" + e.localizedMessage)
                    complete(false)
                }


            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not retrieve channels")
                complete(false)

            }) {

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                    return headers
                }
            }
        App.prefs.requestQueue.add(channelsRequest)
    }


    fun getMessages(channelId: String, complete: (Boolean) -> Unit){

        val url = "$URL_GET_MESSAGES$channelId"

        val messsagesRequest = object : JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->

            clearMessages()

            try {
                for (x in 0 until response.length()){

                    val message = response.getJSONObject(x)
                    val messsageBody = message.getString("messageBody")
                    val channelId = message.getString("channelId")
                    val id = message.getString("_id")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userAvatarColor = message.getString("userAvatarColor")
                    val timeStamp =message.getString("timeStamp")

                    val newMessage = Message(messsageBody, userName, channelId, userAvatar, userAvatarColor,id, timeStamp)

                    this.message.add(newMessage)
                }
                complete(true)

            }catch (e: JSONException){

                Log.d("JSON", "EXE:" + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener {

            Log.d("ERROR", "Could not retrieve channels")
            complete(false)

        }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }
        App.prefs.requestQueue.add(messsagesRequest)
    }

    fun clearMessages(){
        message.clear()
    }

    fun clearChannels(){
        channels.clear()
    }
}