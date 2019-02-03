package K10coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// When we create function used in concurrent code we usually want to extend CoroutineScope so that we use structured
// concurrency and don´t accidentally create any coroutines in the global scope.
fun CoroutineScope.produceNumbers() =
    produce {
        var x = 1
        while (true) {
            delay(1000L)
            send(x++)
        }
    }

fun main() {

    // Pipelines is a pattern when one function receives a channel and does some processing and returns a channel.
    fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> =
        produce {
            for (x in numbers) send(x * x)
        }

    runBlocking {
        val numbers = produceNumbers()
        val squares = square(numbers) // Pipeline
        for (i in 1..5) println(squares.receive())
        println("Done!")
        coroutineContext.cancelChildren()
    }
    // 1
    // 'wait a second...'
    // 4
    // 'wait a second...'
    // 9
    // 'wait a second...'
    // 16
    // 'wait a second...'
    // 25
    // Done!

    // Fan out:
    // Multiple coroutines receive from the same channel.

    fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>): Job =
        launch {
            for (msg in channel) { // receive (Fan out)
                println("Processor $id received $msg")
            }
        }

    runBlocking {
        val producer = produceNumbers()
        repeat(5) { launchProcessor(it, producer) }
        delay(9500)
        producer.cancel() // closes the channel and therefore the processors
        println("Done!")
    }
    // Processor 0 received 1
    // Processor 1 received 2
    // Processor 2 received 3
    // Processor 3 received 4
    // Processor 4 received 5
    // Processor 0 received 6
    // Processor 1 received 7
    // Processor 2 received 8
    // Processor 3 received 9
    // Done!

    // Fan in:
    // Multiple coroutines send to the same channel.

    suspend fun sendNumber(channel: SendChannel<Int>, num: Int, time: Long) {
        while (true) {
            delay(time)
            channel.send(num) // send (Fan in)
        }
    }

    runBlocking {
        val channel = Channel<Int>()
        launch { sendNumber(channel, 1, 1000L) }
        launch { sendNumber(channel, 2, 2000L) }
        repeat(8) {
            println(channel.receive())
        }
        coroutineContext.cancelChildren()
    }
    // 1
    // 2
    // 1
    // 1
    // 2
    // 1
    // 1
    // 2

    // Buffered channels:
    // Until now we have only seen unbuffered channels, i.e they only transfer data when a sending coroutine and a
    // receiving coroutine meet. But we can also specify a buffering capacity to the channel, and thus let the sender
    // buffer a specific amount when there is no receiving end.
    fun CoroutineScope.produceBufferedNumbers(bufferSize: Int): ReceiveChannel<Int> =
        produce(capacity = bufferSize) {
            for (i in 1..10) {
                send(i)
                println("sent $i")
                delay(100L)
            }
        }

    runBlocking {
        val numbersChannel = produceBufferedNumbers(4)
        numbersChannel.consumeEach {
            println("received $it")
            delay(1000L)
        }
    }
    // sent 1
    // received 1
    // sent 2
    // sent 3
    // sent 4
    // sent 5 (Here we buffered 4 sent values without receiving them)
    // received 2 (Now the buffer is decremented so we can send one item again)
    // sent 6
    // received 3
    // sent 7
    // received 4
    // sent 8
    // received 5
    // sent 9
    // received 6
    // sent 10
    // received 7 (Here we receive the rest of the buffer before terminating)
    // received 8
    // received 9
    // received 10

}