package K7generics

// Generic parameters

interface Drug {
    fun putInBody()
}

open class CocaLeaf: Drug {
    override fun putInBody() {
        println("Eating leaves...")
    }
}

open class Cocain: CocaLeaf() {
    override fun putInBody() {
        println("Snorting cocain...")
    }
}

class Meth: Cocain() {
    override fun putInBody() {
        println("Inhaling meth smoke...")
    }
}

// Base type

interface Consumer<in T> {
    fun consume(value: T) // in position
}

class MethUser: Consumer<Meth> {
    override fun consume(value: Meth) {
        println("Cutting some pieces of rock...")
        value.putInBody()
    }
}

class CocaLeafUser: Consumer<CocaLeaf> {
    override fun consume(value: CocaLeaf) {
        println("Picking some leaves...")
        value.putInBody()
    }
}

class CokeUser: Consumer<Cocain> {
    override fun consume(value: Cocain) {
        println("Cutting some lines with creditcard...")
        value.putInBody()
    }
}

// Contravariance:
//
// Consumer<Meth>
//      ^    |
//      |    v
// Consumer<Cocain>
//      ^    |
//      |    v
// Consumer<CocaLeaf>

// This function can be called with CocaLeafUser and CokeUser since both are subclasses of Consumer<Meth>
fun consumeMeth(consumer: Consumer<Meth>, meth: Meth) {
    consumer.consume(meth)
}

fun main(args: Array<String>) {
    val cocaLeafUser = CocaLeafUser()
    val cokeUser = CokeUser()
    val methUser = MethUser()
    val meth = Meth()

    consumeMeth(cocaLeafUser, meth) // without 'in' modifier this is not possible
    // Picking some leaves...
    // Inhaling meth smoke...
    consumeMeth(cokeUser, meth)
    // Cutting some lines with creditcard...
    // Inhaling meth smoke...
    consumeMeth(methUser, meth)
    // Cutting some pieces of rock...
    // Inhaling meth smoke...
}