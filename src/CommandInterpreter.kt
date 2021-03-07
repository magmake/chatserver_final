import java.io.*
import java.util.*

// Handles user creation, Observer-registrations and if-else-statements depending on user input.
class CommandInterpreter(input: InputStream, output: OutputStream) : Runnable, ChatObserver {
    override fun historyUpdate(message: ChatMessage) { // Implements ChatObserver interface.
        userPrint.println(message)
    }

    //Variable creation and commandSet map containing help-menu
    private var sentMessageCount = 1
    private var userInput = Scanner(input)
    private var userPrint = PrintStream(output)
    private var done = false
    private val commandSet = mapOf(
        Pair("!help", "shows help"),
        Pair("!quit", "quitting"),
        Pair("!history", "show chat history"),
        Pair("!user", "my user"),
        Pair("!users", "online users"),
        Pair("!^]", "escape character")
    )
    private var user = "" // Chat user is initialised as blank.
    override fun run() {
        try { // Start of try-catch to eliminate red-errors, if closed prematurely.
            ChatHistory.register(this) // Registering CI
            ChatHistory.register(ChatConsole) // Registering ChatConsole as observer.
            ChatHistory.register(TopChatter) // Registering TopChatter as observer.
            userPrint.println("Welcome to Chat Server.")
            userPrint.println("Please type !user 'username', to start.")
            while (!done) { // Start of main loop
                val command = userInput.nextLine()
                // start of IF-ELSE-statements
                if (command == "!quit") {
                    userPrint.println("Goodbye!")
                    done = true
                    Users.removeUser(user) // Removes user from Users-list
                } else if (command == "!^]") { // Escape-character, breaks loop if used. Removes user from observer list.
                    userPrint.println("Breaking connection!")
                    break
                } else if (command == "!help") { // Prints out commandSet (help) map.
                    for ((key, value) in commandSet) {
                        userPrint.println("$key = $value")
                    }
                } else if (command == "!users") { // Prints out users if users exist
                    if (Users.userList.isNotEmpty()) {
                        (Users.userList.forEach { userPrint.println(it) })
                    } else userPrint.println("There are no active users.")
                } else if (command == "!history") { // Prints out history if there is content.
                    if (ChatHistory.HistoryList.isNotEmpty()) {
                        (ChatHistory.HistoryList.forEach { userPrint.println("from: $it") })
                    } else
                        userPrint.println("History is empty!")
                } else if (command.startsWith("!user")) { // Adding user
                    Users.addUser(command.substringAfter("!user")) // removes !user-prefix
                    if (Users.newUser) { // Checking if user was added to Users-list
                        user = command.substringAfter("!user")
                        userPrint.println("User $user set")
                    } else if (!Users.newUser) {
                        userPrint.println("Cannot add user, already exists.")
                    } else if (user != "") { // Check if username is not blank
                        userPrint.println("Username already set")
                        Users.newUser = false
                    }
                } else if (user == "") {
                    userPrint.println("Can't send message, username not set")
                }
                if (!command.startsWith("!") && user != "") { // If user types normal text, inserts to chat history.
                    if (!command.isBlank()) {
                        Users.topChatter[user] = sentMessageCount++
                        ChatHistory.insert(ChatMessage(command, sender = user))
                    }

                }
            }
        } catch
            (IOException: Throwable) { // Catching error, if needed
            System.out.println("Premature Closing of terminal!: $IOException!")
            userPrint.close() // Closing of terminal
            ChatHistory.deregister(this)
            ChatHistory.deregister(TopChatter)
            Users.removeUser(user) // Removes username from Users-list
            done
        }


    }


}


