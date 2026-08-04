package project.handson2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import project.handson2.models.ChatMessage
import project.handson2.network.RetrofitClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

class ChatViewModel : ViewModel() {

    private val _messages = MutableLiveData<MutableList<ChatMessage>>(mutableListOf())
    val messages: LiveData<MutableList<ChatMessage>> = _messages

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData("")
    val error: LiveData<String> = _error

    private val apiService = RetrofitClient.pythonApiService

    fun addUserMessage(content: String) {
        val currentMessages = _messages.value ?: mutableListOf()
        currentMessages.add(ChatMessage(content, true))
        _messages.value = currentMessages
    }

    fun addBotMessage(content: String) {
        val currentMessages = _messages.value ?: mutableListOf()
        currentMessages.add(ChatMessage(content, false))
        _messages.value = currentMessages
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun clearError() {
        _error.value = ""
    }

    fun sendMessageToBackend(message: String, sessionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = ""

            try {
                val response = withTimeout(30000) {
                    withContext(Dispatchers.IO) {
                        apiService.sendMessage(message, sessionId)
                    }
                }

                if (response.response.isNotEmpty()) {
                    addBotMessage(response.response)
                } else {
                    addBotMessage("I received your message but I'm not sure how to respond. Could you please rephrase?")
                }

            } catch (e: TimeoutCancellationException) {
                _error.value = "The server took too long to respond. Please try again."
                addBotMessage("I apologize, but the server is taking too long to respond. Please try again later.")
            } catch (e: SocketTimeoutException) {
                _error.value = "Connection timeout. Please check your network."
                addBotMessage("I'm having trouble connecting to the server. Please check your internet connection.")
            } catch (e: ConnectException) {
                _error.value = "Cannot connect to server. Make sure Python backend is running."
                addBotMessage("⚠️ Cannot connect to the AI server. Please make sure the backend is running on port 5000.")
            } catch (e: IOException) {
                _error.value = "Network error: ${e.message}"
                addBotMessage("I'm having network issues. Please check your connection and try again.")
            } catch (e: Exception) {
                _error.value = "Error: ${e.message ?: "Unknown error"}"
                addBotMessage("I encountered an unexpected error. Please try again.")
            } finally {
                _isLoading.value = false
            }
        }
    }
}