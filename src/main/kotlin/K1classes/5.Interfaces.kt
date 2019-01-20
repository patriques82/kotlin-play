package K1classes

interface Explodable {
    fun assemble()
    fun explode() = println("Boooooomm!")
}

interface Angry {
    fun explode() = println("Arrrrghhh!")
}

class Terrorist : Angry, Explodable {
    override fun assemble() {
        println("Strapping on bombvest...")
    }

    override fun explode() {
        super<Angry>.explode() // call interface method
        super<Explodable>.explode()
    }

}

fun main(args: Array<String>) {
    val terrorist = Terrorist()
    terrorist.assemble() // Strapping on bombvest...
    terrorist.explode() // Arrrrghhh!
                        // Boooooomm!
}