package K6higherorder

// Classic strategy pattern

interface CompressionStrategy {
    fun compress(file: String): String
}

class ZipCompression: CompressionStrategy {
    override fun compress(file: String): String = "$file.zip"
}

class RarCompression: CompressionStrategy {
    override fun compress(file: String): String = "$file.rar"
}

class Compressor(var compressionStrategy: CompressionStrategy) {
    fun createArchive(file: String): String = compressionStrategy.compress(file)
}

// Higher order function strategy pattern

fun String.createArchive(compress: (String) -> String): String =
    compress(this)

fun main(args: Array<String>) {
    val file = "Backupfile"

    val compressor = Compressor(ZipCompression())
    println(compressor.createArchive(file)) // Backupfile.zip
    compressor.compressionStrategy = RarCompression()
    println(compressor.createArchive(file)) // Backupfile.rar

    println(file.createArchive { "$it.zip" }) // Backupfile.zip
    println(file.createArchive { "$it.rar" }) // Backupfile.rar
}