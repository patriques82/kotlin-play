package K10coroutines

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Channels is a way to allow coroutines to communicate without sharing state.

fun main() {

    // A channel consist of a suspending send and suspending receive.
    runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x)
        }
        repeat(5) { println("received: ${channel.receive()}") }
        println("Done!")
    }
    // received: 1
    // received: 2
    // received: 3
    // received: 4
    // received: 5
    // Done!

    // A channel can also be closed to indicate that no more elements are coming.
    runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x)
            channel.close() // we're done sending
        }
        for (y in channel) println("received: $y") // We use a `for` loop to iterate over the channel until the channel is closed.
        println("Done!")
    }
    // received: 1
    // received: 2
    // received: 3
    // received: 4
    // received: 5
    // Done!

    // There is also a more idiomatic way of doing this using a coroutine builder 'produce', that creates an
    // ReceiveChannel, and on the ReceiveChannel using a extension function 'consumeEach'.
    runBlocking {
        val numberChannel: ReceiveChannel<Int> = produce { for(x in 1..5) send(x) }
        numberChannel.consumeEach { println("received: $it") }
        println("Done!")
    }
    // received: 1
    // received: 2
    // received: 3
    // received: 4
    // received: 5
    // Done!

}