package K1classes

abstract class Filosopher {
    abstract fun think()
}

// Only open classes can be extended
open class Socrates: Filosopher() {
    fun converse() { // final
        print("asking anoying questions...")
    }
    open fun read() { // open
        print("write...")
    }
    override fun think() { // also open
        print("thinking...")
    }
}

class Plato: Socrates() {
    override fun read() {
        print("read socrates")
    }
    override fun think() {
        print("think like socrates")
    }
}

fun main(args: Array<String>) {
    val f = Plato()
    f.converse() // asking anoying questions..
    f.think() // think like socrates
}