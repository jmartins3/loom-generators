// To access the *non-public* Continuation API
// ONLY for learning purposes

@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package pt.isel.pc

import org.junit.jupiter.api.Assertions.assertEquals
import pt.isel.pc.sequences.MySequence.*
import org.junit.jupiter.api.Test
import pt.isel.pc.sequences.MySequence
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime


class LoomGeneratorTestsKotlin {
    @Test
    fun `my sequence test`() {
        val evens = MySequence {
            var curr = 2

            while (true) {
                yield(curr) // next pair number
                curr += 2
            }
        }

        val evensLimited = evens
            .limit(20)

        evensLimited
            .forEach { println("$it") }
        evensLimited
            .forEach { println("$it") }
    }

    @Test
    fun `hanoi tower generator test`() {
        var hanoiSeq = MySequence<String> {
            fun hanoi(n: Int, start: Char, end: Char, aux: Char) {
                if (n > 0) {
                    hanoi(n-1 , start, aux, end)
                    yield(start + " to " + end )
                    hanoi(n - 1, aux, end, start)
                }
            }

            hanoi(24, 'A', 'B', 'C')
        }

        /*
        var moves = iterate (1)  { n -> n +1}
                    .zip(hanoiSeq) { n, s -> Pair(n, s) }
        */

        var count = 0
        val millis = measureTimeMillis {

            for (move in hanoiSeq) {
                count++
            }
        }
        assertEquals(16*1024*1024-1, count)
        println("hanoi tower generator with loom test done in $millis milliseconds")

    }

    @Test
    fun `hanoi tower generator with kotlin sequences test`() {

        val hanoiSeq = sequence <String> {

            suspend fun SequenceScope<String>.hanoi(n: Int, start: Char, end: Char, aux: Char) {
                if (n > 0) {
                    hanoi(n-1 , start, aux, end)
                    yield(start + " to " + end )
                    hanoi(n - 1, aux, end, start)
                }
            }
            hanoi(24, 'A', 'B', 'C')
        }


        var count = 0

        var millis = measureTimeMillis {
            for (move in hanoiSeq) {
                count++
            }
        }

        assertEquals(16*1024*1024-1, count)
        println("hanoi tower kotlin generator test done in $millis milliseconds")

    }

    @Test
    fun `hanoi tower generator with kotlin sequences test2`() {

        fun hanoi(n: Int, start: Char, end: Char, aux: Char): Sequence<String> = sequence {
            if (n > 0) {
                yieldAll(hanoi(n - 1, start, aux, end))
                yield(start + " to " + end)
                yieldAll(hanoi(n - 1, aux, end, start))
            }
            yieldAll(sequenceOf())
        }

        val hanoiSeq = hanoi(24, 'A', 'B', 'C')
        var count = 0

        var millis = measureTimeMillis {
            for (move in hanoiSeq) {
                count++
            }
        }


        assertEquals(16 * 1024 * 1024 - 1, count)
        println("hanoi tower kotlin 2 generator test done in $millis milliseconds")
    }

}


