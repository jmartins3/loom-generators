package pt.isel.pc.sequences1;

import org.junit.jupiter.api.Test;

import java.util.List;

import static pt.isel.pc.sequences1.Sequence.*;

public class LoomGeneratorTestsJava {
	
	@Test
	public void javaSequenceTest() {
		var sn = new SequenceRunnable() {
			public void run() {
				int curr = 2;
				
				while (true) {
					this.yield(curr);
					curr += 2;
				}
			}
		};
		
		Sequence<Integer> evens = create(sn);
		
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
		Sequence<String> strings = create(new SequenceRunnable() {
			@Override
			public void run() {
				this.yield("Joao");
				this.yield("Jorge");
				this.yield("Pedro");
			}
		});
		
		var pairs =
			iterate(1, n ->n+1)
			.zip(strings, (i, s) -> new Pair(i,s));
							 
		
		pairs.forEach((t)-> System.out.println(t) );
		
		pairs.forEach((t)-> System.out.println(t) );
		
	}
	
	private Sequence<Pair<Integer,Integer>> combs(int min, int max) {
		return Sequence.create(new SequenceRunnable() {
			@Override
			public void run() {
				for(int i = min; i <= max; ++i) {
					for (int j = i +1; j <= max; j++) {
						this.yield(new Pair(i, j));
					}
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
	
		Sequence<Pair<Integer,Integer>> combs =
			range(10,20).flatMap(n1 ->
				range(n1 +1,20).flatMap( n -> List.of(new Pair(n1, n)) )
			);
		
		combs.forEach((t)-> System.out.println(t) );
		combs.forEach((t)-> System.out.println(t) );
	}
	
	
	
	@Test
	public void  hanoiTowerGeneratorTest() {
		Sequence<String> hanoiSeq = Sequence.create(new SequenceRunnable() {
			void hanoi(int n, char start, char end, char aux) {
				if (n > 0) {
					hanoi(n-1 , start, aux, end);
					this.yield(start + " to " + end );
					hanoi(n - 1, aux, end, start);
				}
			}
			
			@Override
			public void run() {
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
