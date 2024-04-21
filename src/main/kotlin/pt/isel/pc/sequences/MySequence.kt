// To access the *non-public* Continuation API
// ONLY for learning purposes
@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package pt.isel.pc.sequences

import jdk.internal.vm.Continuation
import jdk.internal.vm.ContinuationScope

class MySequence<T>(val block : MySequence<T>.SequenceIterator.() -> Unit) : Iterable<T> {

    private val scope = ContinuationScope("SequenceScope")

    inner class SequenceIterator : Iterator<T> {
        private var nextValue : T? = null

        private val code : Runnable = Runnable {
            block.invoke(this)
        }

        private var cont = Continuation(scope, code)

        override fun hasNext(): Boolean {
            if (nextValue != null) return true
            cont.run()
            return !cont.isDone && nextValue != null
        }

        override fun next(): T {
            if (!hasNext()) throw IllegalStateException()
            val value = nextValue
            nextValue = null
            return value!!
        }

        fun yield(t: T) {
            nextValue = t
            Continuation.yield(scope)
        }
    }

    override fun iterator(): Iterator<T> {
        return SequenceIterator()
    }

    // generators

    companion object {
         fun <T> iterate(initial: T, func: ( (T) -> T))  =
            MySequence  {
                var next = initial
                while(true) {
                    val n = next
                    next = func(next)
                    yield(n)
                }
            }


        fun range(min: Int, max: Int): MySequence<Int> =
            MySequence {
                var curr = min
                while (curr <= max) {
                    yield(curr)
                    curr += 1
                }
            }
    }


    fun  limit(limit: Int) : MySequence<T>  {
        val src = this
        return  MySequence<T>()  {
            var curr = 0;
            val itSrc = src.iterator()
            while (curr++ < limit && itSrc.hasNext()) {
                yield(itSrc.next());
            }
        }
    }

    fun <U,V> zip(other : MySequence<U>, combiner: ((t: T, u: U) -> V) ): MySequence<V>  {
        var src  = this
        return  MySequence {
            val itOther = other.iterator()

            val itSrc = src.iterator()
            while(itSrc.hasNext() && itOther.hasNext()) {
                yield(combiner(itSrc.next(), itOther.next()));
            }
        }
    }
}