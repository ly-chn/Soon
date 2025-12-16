package kim.nzxy.soon.toolWindow

import com.intellij.util.EventDispatcher
import java.util.*

object EventBus {
    val refresh = SimpleEventPublisher()
    val escKeyClick = SimpleEventPublisher()
    val redraw = SimpleEventPublisher()
    val filterTextChange = TextEventPublisher()
}

class TextEventPublisher {
    private val dispatcher = EventDispatcher.create(TextEventListener::class.java)
    fun add(listener: TextEventListener) {
        dispatcher.addListener(listener)
    }

    fun fire(text: String?) {
        dispatcher.multicaster.fire(text)
    }
}

class SimpleEventPublisher {
    private val dispatcher = EventDispatcher.create(SimpleEventListener::class.java)
    fun add(listener: SimpleEventListener) {
        dispatcher.addListener(listener)
    }

    fun fire() {
        dispatcher.multicaster.fire()
    }
}

interface SimpleEventListener : EventListener {
    fun fire()
}

interface TextEventListener : EventListener {
    fun fire(text: String?)
}