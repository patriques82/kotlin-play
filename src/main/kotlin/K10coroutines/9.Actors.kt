package K10coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach

// Actors:
// The actor model solves deadlocks by, instead of sharing memory (shared state) to communicate, it lets actors
// communicate to share memory. An actor is a combination of a coroutine containing a state and a channel to communicate
// with other coroutines. An actor works on a ActorScope<T> which both implements CoroutineScope and ReceiveChannel<T>
// so it can receive messages directly whithout explicitly reading from its channel.

// Messages
sealed class CounterMsg
object IncCounter : CounterMsg() // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a two-way message to get the counter

// The actor
fun CoroutineScope.counterActor(): SendChannel<CounterMsg> = actor { // this: ActorScope<CounterMsg>
    var counter = 0 // actor state
    consumeEach { msg -> // actor's implicit mailbox channel
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter) // received response object
        }
    }
}
// An 'actor' coroutine builder is a dual of the 'produce' coroutine builder. An actor is associated with the channel
// that it receives messages from, while a producer is associated with the channel that it sends elements to.

fun main() {

    runBlocking {
        val counterActor = counterActor()
        GlobalScope.massiveRun {
            counterActor.send(IncCounter) // send 100000 InCounter messages to actor
        }
        val response = CompletableDeferred<Int>()
        counterActor.send(GetCounter(response))
        println("Counter = ${response.await()}")
        counterActor.close() // Unlike the produce coroutine builder, you need to explicitly stop the actor.
    }
    // Completed 100000 actions in 177 ms
    // Counter = 100000

}