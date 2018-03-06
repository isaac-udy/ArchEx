package com.isaacudy.archex.example

import com.isaacudy.archex.LiveViewModel

data class MessageState(
    val message: String = ""
)

class MessageViewModel : LiveViewModel<MessageState>(MessageState("")) {

    private var selectedMessage = 0

    private val messages = listOf(
        "Hi",
        "Hello",
        "Bonjour",
        "Hola",
        "Blah blah",
        "About to loop"
    )

    init {
        state = state.copy(
            message = messages[selectedMessage]
        )
    }

    fun nextMessage() {
        selectedMessage++
        if (selectedMessage >= messages.size) {
            selectedMessage = 0
        }
        state = state.copy(
            message = messages[selectedMessage]
        )
    }

}