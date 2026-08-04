package project.handson2.network

import project.handson2.models.ChatResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET

interface PythonApiService {
    @FormUrlEncoded
    @POST("chat")
    suspend fun sendMessage(
        @Field("message") message: String,
        @Field("session_id") sessionId: String
    ): ChatResponse

    @GET("health")
    suspend fun healthCheck(): Map<String, String>
}