// To access the *non-public* Continuation API
// ONLY for learning purposes

@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package pt.isel.pc

import pt.isel.pc.sequences.Sequence
import pt.isel.pc.sequences.Sequence.*
import org.junit.jupiter.api.Test
import pt.isel.pc.sequences.Sequence.Companion.iterate

class LoomGeneratorTestsKotlin {
    @Test
    fun `my sequence test`() {
        val evens = Sequence {
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
        var hanoiSeq = Sequence<String> {
            fun hanoi(n: Int, start: Char, end: Char, aux: Char) {
                if (n > 0) {
                    hanoi(n-1 , start, aux, end)
                    yield(start + " to " + end )
                    hanoi(n - 1, aux, end, start)
                }
            }

            hanoi(10, 'A', 'B', 'C')
        }

        var moves = iterate (1)  { n -> n +1}
                    .zip(hanoiSeq) { n, s -> Pair(n, s) }

        for (move in moves) {
            println("${move.first}: ${move.second}")
        }
    }
}


