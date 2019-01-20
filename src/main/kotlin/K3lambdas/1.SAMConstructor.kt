package K3lambdas

fun run(r: Runnable) {
    // do something..
    r.run()
}

fun main(args: Array<String>) {
    val r = Runnable { println("Runnable") } // SAM-Constructor used for Java8 functional interfaces
    run(r) // Runnable
}