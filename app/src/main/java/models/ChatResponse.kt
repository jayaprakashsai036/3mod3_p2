package project.handson2.models

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("response")
    val response: String = "",

    @SerializedName("session_id")
    val sessionId: String? = null,

    @SerializedName("context")
    val context: Map<String, Any>? = null,

    @SerializedName("error")
    val error: String? = null
)