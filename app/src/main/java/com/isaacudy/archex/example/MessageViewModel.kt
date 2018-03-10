package com.isaacudy.archex.example

import com.isaacudy.archex.LiveEvent
import com.isaacudy.archex.LiveViewModel
import com.isaacudy.archex.MutableLiveEvent

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

    private val _toast = MutableLiveEvent<String>()
    val toast = _toast as LiveEvent<String>

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

    fun peekNextMessage(){
        var nextMessage = selectedMessage + 1
        if(nextMessage >= messages.size){
            nextMessage = 0
        }
        _toast.set(messages[nextMessage])
    }

}