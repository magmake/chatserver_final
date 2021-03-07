//User-storage singleton
object Users {
    var userList = HashSet<String>()
    var topChatter = mutableMapOf<String, Int>()
    var newUser: Boolean = false
    fun addUser(username: String): Boolean { // Attempts to add user, if name does not exist in the userList.
        if (userList.contains(username) || username.isBlank()) {
            newUser = false // Boolean to store information, if user was not added -> false. If user was added -> true
            return newUser
        } else
            newUser = true
        userList.add(username)
        return newUser
    }

    fun removeUser(username: String) {
        userList.remove(username)
    }
}

//Implementing TopChatter singleton
object TopChatter : ChatObserver {
    override fun historyUpdate(message: ChatMessage) {
        System.out.println("Active users:")
        for ((key, value) in Users.topChatter) { // Prints out chat users and the amount of messages sent.
            println("User:$key, Messages Sent $value")
        }
        Users.topChatter.toList().sortedByDescending { (value) -> value }.take(4).map { it }.forEach { println(it) }
        // Prints out TopChatter map as a raw list to console, 4 top key,value (user, messages) combinations.
    }

}
