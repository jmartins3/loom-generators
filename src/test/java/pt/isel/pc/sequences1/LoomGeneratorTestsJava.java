package pt.isel.pc.sequences1;

import org.junit.jupiter.api.Test;
import pt.isel.pc.sequences1.Sequence;

import java.util.List;

import static pt.isel.pc.sequences1.Sequence.*;

public class LoomGeneratorTestsJava {
	
	@Test
	public void javaSequenceTest() {
		SequenceBlock<Integer> block = seq -> {
			int curr = 2;
			var s = seq;
			while (true) {
				seq.yield(curr);
				curr += 2;
			}
		};
		
		Sequence<Integer> evens = create(block);
		
		var evensLimited = evens.limit(20);
		
		evensLimited.forEach((t)-> System.out.println(t) );
		evensLimited.forEach((t)-> System.out.println(t) );
	 
	}
	
	private static class Pair<T,U> {
		public final T t;
		public final U u;
		
		public Pair(T t, U u) {
			this.t = t;
			this.u = u;
		}
		
		@Override
		public String toString() {
			return String.format("{ %s: %s }", t, u );
		}
	}
	
	@Test
	public void javaSequenceZipTest() {
		Sequence<String> strings = create(s-> {
				s.yield("Joao");
				s.yield("Jorge");
				s.yield("Pedro");
		});
		
		var pairs =
			iterate(1, n ->n+1)
			.zip(strings, (i, s) -> new Pair(i,s));
							 
		
		pairs.forEach((t)-> System.out.println(t) );
		pairs.forEach((t)-> System.out.println(t) );
	}
	
	private Sequence<Pair<Integer,Integer>> combs(int min, int max) {
		return Sequence.create(s -> {
			for(int i = min; i <= max; ++i) {
				for (int j = i +1; j <= max; j++) {
					s.yield(new Pair(i, j));
				}
			}
		});
	}
	
	
	@Test
	public void javaSequenceCombinationsTest() {
		var combs = combs(10, 20);
		combs.forEach((t)-> System.out.println(t) );
		combs.forEach((t)-> System.out.println(t) );
	}
	
	@Test
	public void javaSequenceCombinationsWithFlatmapTest() {
		var combs =
			range(10,20).flatMap(n1 ->
				range(n1 +1,20).flatMap( n -> List.of(new Pair(n1, n)) )
			);
		
		combs.forEach((t)-> System.out.println(t) );
		combs.forEach((t)-> System.out.println(t) );
	}
	
	@Test
	public void  hanoiTowerGeneratorTest() {
		var hanoiSeq = Sequence.create(new SequenceBlock<String>() {
			SequenceIterator<String> s;
			
			void hanoi(int n, char start, char end, char aux) {
				if (n > 0) {
					hanoi(n - 1, start, aux, end);
					s.yield(start + " to " + end);
					hanoi(n - 1, aux, end, start);
				}
			}
			
			@Override
			public void exec(SequenceIterator<String> seq) {
				s = seq;
				hanoi(10, 'A', 'B', 'C');
			}
		});
		
		
		Sequence<Pair<Integer,String>> moves =
				iterate(1, n -> n+1)
				.zip(hanoiSeq, (n, s) -> new Pair(n,s));
		
		for (var move : moves) {
			System.out.println(move);
		}
	}
}
