package K1classes

interface Vehicle {
    val fuelCost: Int // also properties can be abstract
    fun drive(km: Int)
}

class Car(override val fuelCost: Int): Vehicle {
    var totalCost = 0
    override fun drive(km: Int) {
        totalCost += km * fuelCost
    }
}

class Taxi(override val fuelCost: Int): Vehicle {
    private val flatRate = 300
    override fun drive(km: Int) {
        println("the ride will cost ${flatRate + km * fuelCost}")
    }
}

fun main(args: Array<String>) {
    val car = Car(10)
    val taxi = Taxi(12)

    car.drive(5)
    println(car.totalCost) // 50

    taxi.drive(20) // the ride will cost 540
}