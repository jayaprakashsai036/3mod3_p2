package project.handson2.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.UUID

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("chat_session", Context.MODE_PRIVATE)

    fun getSessionId(): String? {
        return prefs.getString(Constants.SESSION_ID_KEY, null)
    }

    fun saveSessionId(sessionId: String) {
        prefs.edit().putString(Constants.SESSION_ID_KEY, sessionId).apply()
    }

    fun generateSessionId(): String {
        return UUID.randomUUID().toString()
    }
}