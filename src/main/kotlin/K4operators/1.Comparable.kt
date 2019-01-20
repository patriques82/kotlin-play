package K4operators

class Stock(val name: String, val pe: Int, val size: Int) {
    // this operator implements: ==
    override operator fun equals(other: Any?): Boolean {
        val stock = other as? Stock ?: return false
        return (stock.pe == this.pe) and (stock.size == this.size)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + pe
        result = 31 * result + size
        return result
    }
}

// this operator implements: > < >= <=
operator fun Stock.compareTo(other: Stock): Int =
    if (pe == other.pe) // equals
        size.compareTo(other.size)
    else
        pe.compareTo(other.pe)


fun main(args: Array<String>) {
    val apple = Stock("Apple", 7, 2)
    val micro = Stock("Microsoft", 7, 2)
    val google = Stock("Google", 8, 3)
    val empty: Stock? = null
    // [apple == micro] translates to [apple?.equals(micro) ?: (micro == null)]
    println(apple == micro) // true
    println(empty == null) // true

    // [micro > google] translates to [micro.compareTo(google) > 0]
    println(micro > google) // false

    // [micro <= google] translates to [micro.compareTo(google) <= 0] etc..
    println(micro <= apple) // true

}