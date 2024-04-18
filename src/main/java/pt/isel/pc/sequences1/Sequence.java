// To access the *non-public* Continuation API
// ONLY for learning purposes
//@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package pt.isel.pc.sequences1;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Sequence<T> extends Iterable<T> {
    
    static <T> Sequence<T> create(SequenceBlock<T> generator) {
        ContinuationScope scope = new ContinuationScope("SequenceScope");
        return () -> new SequenceIterator<T>(generator, scope);
    }
    
    class SequenceIterator<T> implements  Iterator<T> {
        private T nextValue  = null;
        private Continuation cont;
        private ContinuationScope scope;
        
        
        public SequenceIterator(SequenceBlock<T> generator, ContinuationScope scope) {
            var t = this;
            cont = new Continuation(scope, () -> generator.exec(this));
            this.scope = scope;
        }
        
        @Override
        public boolean hasNext() {
            if (nextValue != null) return true;
            cont.run();
            return !cont.isDone() && nextValue != null;
        }
        
        @Override
        public T next() {
            if (!hasNext()) throw new IllegalStateException();
            var value = nextValue;
            nextValue = null;
            return value;
        }
        
        void yield(T value) {
            nextValue = value;
            Continuation.yield(scope);
        }
    }
   
    // generators
    
    static <T> Sequence<T> iterate(T initial, UnaryOperator<T> func) {
        return  Sequence.create(s -> {
            T next = initial;
            while (true) {
                var n = next;
                next = func.apply(next);
                s.yield(n);
            }
        });
    }
    
    static Sequence<Integer>  range(int min, int max) {
        return Sequence.create(seq -> {
            int curr = min;
            while (curr <= max) {
                seq.yield(curr);
                curr += 1;
            }
           
        });
    }
    
   
    default Sequence<T> limit(int lim) {
        var src = this;
        return  Sequence.create(s -> {
            Iterator<T> srcIt = src.iterator();
            int curr = 0;
            
            while (curr++ < lim && srcIt.hasNext()) {
                s.yield(srcIt.next());
            }
          
        });
    }
    
    default <U,V>  Sequence<V> zip(Sequence<U> other , BiFunction<T,U,V> combiner )  {
        var src  = this;
        return  Sequence.create(s -> {
           
            var itOther = other.iterator();
            var itSrc = src.iterator();
            while(itSrc.hasNext() && itOther.hasNext()) {
                s.yield(combiner.apply(itSrc.next(), itOther.next()));
            }
        });
    }
    
    default <U>  Sequence<U> flatMap(Function<T,Iterable<U>> mapper )  {
        var src  = this;
        return  Sequence.create(s -> {
            for(var t : src) {
                for (var u : mapper.apply(t)) {
                    s.yield(u);
                }
            }
        });
    }
}
