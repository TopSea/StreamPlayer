package top.topsea.streamplayer.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming

interface ChatApi {
    @Streaming
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/stream")
    fun streamChat(@Body json: RequestBody): Call<Any>

    @Streaming
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("/chat")
    fun normalChat(result: RequestBody): Call<Any>
}