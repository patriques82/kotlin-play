package K7generics

// Generic parameters

open class Movie {
    open fun watch() {
        println("Inserting dvd... Turning on tv... Lay down on couch...")
    }
}

class TropicalThunder: Movie() {
    override fun watch() {
        super.watch()
        println("So stupid... Laughing...")
    }
}

// Base type

interface Producer<out T> {
    fun produce(): T // out position
}

class BenStiller: Producer<TropicalThunder> {
    override fun produce(): TropicalThunder {
        return TropicalThunder()
    }
}

// Covariance:
//
// Producer<Movie>
//      ^    ^
//      |    |
// Producer<TropicalThunder>

// This function can be called with BenStiller since he is a subclass of Producer<Movie>
fun produceMovie(producer: Producer<Movie>): Movie =
    producer.produce()

fun main(args: Array<String>) {
    val benStiller = BenStiller()

    val movie = produceMovie(benStiller) // without 'out' modifier this is not possible
    movie.watch()
    // Inserting dvd... Turning on tv... Lay down on couch...
    // So stupid... Laughing...
}