package project.handson2

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import project.handson2.utils.SessionManager
import com.google.android.material.snackbar.Snackbar

class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputEditText: EditText
    private lateinit var sendButton: Button

    private lateinit var sessionManager: SessionManager
    private lateinit var sessionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        sessionManager = SessionManager(this)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        sessionId = sessionManager.getSessionId() ?: run {
            val newId = sessionManager.generateSessionId()
            sessionManager.saveSessionId(newId)
            newId
        }

        setupRecyclerView()
        setupInputListeners()
        observeMessages()
        observeErrors()

        if (viewModel.messages.value.isNullOrEmpty()) {
            viewModel.addBotMessage("Hello! I'm your AI assistant. How can I help you today?")
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        adapter = ChatAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        recyclerView.adapter = adapter
    }

    private fun setupInputListeners() {
        inputEditText = findViewById(R.id.inputEditText)
        sendButton = findViewById(R.id.sendButton)

        sendButton.setOnClickListener {
            sendMessage()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun sendMessage() {
        val message = inputEditText.text.toString().trim()
        if (message.isNotEmpty()) {
            viewModel.addUserMessage(message)
            inputEditText.text.clear()
            viewModel.sendMessageToBackend(message, sessionId)
        }
    }

    private fun observeMessages() {
        viewModel.messages.observe(this) { messages ->
            adapter.submitList(messages)
            if (messages.isNotEmpty()) {
                recyclerView.post {
                    recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun observeErrors() {
        viewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    errorMessage,
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.clearError()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            adapter.setLoading(isLoading)
            sendButton.isEnabled = !isLoading
            inputEditText.isEnabled = !isLoading
            sendButton.alpha = if (isLoading) 0.5f else 1.0f
        }
    }
}