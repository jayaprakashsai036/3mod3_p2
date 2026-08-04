package project.handson2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import project.handson2.models.ChatMessage

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()
    private var isLoading = false

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
        private const val VIEW_TYPE_LOADING = 3
    }

    fun submitList(newMessages: List<ChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    fun setLoading(loading: Boolean) {
        if (this.isLoading != loading) {
            this.isLoading = loading
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading && position == messages.size) {
            VIEW_TYPE_LOADING
        } else if (messages[position].isUser) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_BOT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_USER -> {
                UserViewHolder(inflater.inflate(R.layout.item_user_message, parent, false))
            }
            VIEW_TYPE_LOADING -> {
                LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false))
            }
            else -> {
                BotViewHolder(inflater.inflate(R.layout.item_bot_message, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < messages.size) {
            val message = messages[position]
            when (holder) {
                is UserViewHolder -> holder.bind(message.content)
                is BotViewHolder -> holder.bind(message.content)
            }
        }
    }

    override fun getItemCount(): Int = messages.size + if (isLoading) 1 else 0

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)

        fun bind(message: String) {
            messageText.text = message
        }
    }

    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)

        fun bind(message: String) {
            messageText.text = message
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}