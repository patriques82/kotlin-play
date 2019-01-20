package K5delegation

// Decorator pattern: Attach additional responsibilities to an object dynamically.
// Decorators provide a flexible alternative to subclassing for extending functionality.

interface Cake {
    fun mix()
    fun bake()
    fun present()
}

class BirthdayCake: Cake {
    override fun mix() {
        println("bread mix")
        println("whip cream")
    }
    override fun bake() {
        println("bake bread")
    }
    override fun present() {
        println("put cream on bread")
        println("put cake on table")
    }
}

class SugarCake: Cake {
    override fun mix() {
        println("bread mix")
    }
    override fun bake() {
        println("bake bread")
    }
    override fun present() {
        println("put cake on table")
    }
}

open class CakeDecorator(val delegateCake: Cake): Cake by delegateCake

// In Java this would require to implement all cake methods,
// in Kotlin, using the "by" keyword, you only override the function you want to modify, the rest is delegated
class FrostingDecorator(val cake: Cake): CakeDecorator(cake) {
    override fun present() {
        cake.present()
        println("put frosting on top")
    }
}

class CinnamonDecorator(val cake: Cake): CakeDecorator(cake) {
    override fun mix() {
        println("add cinnamon to mix")
        cake.mix()
    }
}

fun processCake(cake: Cake): Cake {
    cake.mix()
    cake.bake()
    cake.present()
    return cake
}

fun main(args: Array<String>) {
    val cinnemonSugarCake = CinnamonDecorator(SugarCake())
    val bithdayCakeWithFrosting = FrostingDecorator(BirthdayCake())
    val sugarCakeWithFrosting = FrostingDecorator(SugarCake())

    processCake(cinnemonSugarCake)
    // add cinnamon to mix
    // bread mix
    // bake bread
    // put cake on table
    processCake(bithdayCakeWithFrosting)
    // bread mix
    // whip cream
    // bake bread
    // put cream on bread
    // put cake on table
    // put frosting on top
    processCake(sugarCakeWithFrosting)
    // bread mix
    // bake bread
    // put cake on table
    // put frosting on top
}