// Stores messages sent to Chat, implements a limit of 40 messages at a time. Does not add commands, nor blank messages.
object ChatHistory : ChatObservable {
    val HistoryList = mutableListOf<ChatMessage>() // History storage
    private val chatObservers = mutableSetOf<ChatObserver>() // Observer storage
    fun insert(message: ChatMessage) {
        if (HistoryList.size <= 40 && message.toString().isNotBlank() && !message.toString().startsWith("!")) {
            HistoryList.add(message)
            chatObservers.forEach { it.historyUpdate(message) }
        } else if ((HistoryList.size > 40 && message.toString().isNotBlank() && !message.toString().startsWith("!"))) {
            HistoryList.removeAt(0)
            HistoryList.add(message)
            chatObservers.forEach { it.historyUpdate(message) }
        }
    }

    override fun register(who: ChatObserver) {
        chatObservers.add(who)
    }

    override fun deregister(who: ChatObserver) {
        chatObservers.remove(who)
    }
}

interface ChatObserver {
    fun historyUpdate(message: ChatMessage)
}

interface ChatObservable {
    fun register(who: ChatObserver)
    fun deregister(who: ChatObserver)
}

object ChatConsole : ChatObserver { // Prints user messages to ChatConsole. Singleton.
    override fun historyUpdate(message: ChatMessage) {
        System.out.println(message)
    }
}


