import java.time.LocalDateTime

// Prints out Chat message in the correct format ( user @ time : text )
class ChatMessage(private val text: String, private val sender: String) {
    private val time = LocalDateTime.now()

    override fun toString(): String {
        return "$sender @ $time : $text"
    }

}