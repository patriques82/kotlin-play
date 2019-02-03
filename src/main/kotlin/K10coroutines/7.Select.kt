package K10coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select

fun CoroutineScope.one() = produce {
    while (true) {
        delay(300)
        send(1)
    }
}

fun CoroutineScope.two() = produce {
    while (true) {
        delay(500)
        send(2)
    }
}

fun main() {

    // Select expression makes it possible to await multiple suspending functions simultaneously and select the first one
    // that becomes available.
    runBlocking {
        val oneProducer = one()
        val twoProducer = two()
        repeat(7) {
            select<Unit> { // <Unit> means that this select expression does not produce any result
                oneProducer.onReceive { println("selected $it") }
                twoProducer.onReceive { println("selected $it") }
            }
        }
        coroutineContext.cancelChildren()
        println("Done!")
    }
    // selected 1
    // selected 2
    // selected 1
    // selected 1
    // selected 2
    // selected 1
    // selected 2
    // Done!

    // Selecting on close:
    // Select is biased to the first clause, so when values exist on both channels the first channel gets selected.
    runBlocking {
        val oneProducer = produce<Int> { repeat(4) { send(1) } }
        val twoProducer = produce<Int> { repeat(4) { send(2) } }
        repeat(8) {
            val value = select<String> {
                oneProducer.onReceiveOrNull { // We use onReceiveOrNull clause to know when channel is closed
                    if (it == null) "channel 'a' is closed"
                    else "received on a $it"
                }
                twoProducer.onReceiveOrNull {
                    if (it == null) "channel 'b' is closed"
                    else "received on b $it"
                }
            }
            println(value)
        }
        delay(1000L)
        coroutineContext.cancelChildren()
        println("Done!")
    }
    // received on a 1
    // received on a 1
    // received on b 2 (since we are using a unbuffered channel, oneProducer gets suspended from time to time on send)
    // received on a 1
    // received on a 1
    // received on b 2
    // channel 'a' is closed
    // channel 'a' is closed
    // Done!

    // Selecting on send:
    // Select expression has an onSend function that can exploit the select bias.
    fun CoroutineScope.produceNumbers(backup: SendChannel<Int>) = produce<Int> {
        for (num in 1..10) {
            delay(100)
            select<Unit> {
                onSend(num) {}
                backup.onSend(num) {} // if we cant send to primary channel we send to backup
            }
        }
    }

    runBlocking {
        val backup = Channel<Int>()
        launch { // fast consumer (no delay)
            backup.consumeEach { println("Backup channel got $it") }
        }
        produceNumbers(backup).consumeEach {
            println("Primary channel got $it")
            delay(300) // slow consumer (will miss 2 of 3 values)
        }
        coroutineContext.cancelChildren()
        println("Done!")
    }
    // Primary channel got 1
    // Backup channel got 2
    // Backup channel got 3
    // Primary channel got 4
    // Backup channel got 5
    // Backup channel got 6
    // Primary channel got 7
    // Backup channel got 8
    // Backup channel got 9
    // Primary channel got 10
    // Done!

}