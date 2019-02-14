package javaspringexamples.ThreadExamples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * This example represents how to create a pool of threads using ForkJoinPool
 * and extending the RecursiveTask class.
 * 
 * @author mounir.sahrani@gmail.com
 *
 */
public class ListInitializerRecursiveTask extends RecursiveTask<List<Integer>> {

	private int[] data;
	private int begin;
	private int end;

	public ListInitializerRecursiveTask(int[] data, int _begin, int _end) {
		this.data = data;
		begin = _begin;
		end = _end;
	}

	/**
	 * Method compute() to override for RecursiveTask objects, we initiate the
	 * list recursively.
	 */
	@Override
	protected List<Integer> compute() {
		List<Integer> l = new ArrayList<>();

		int middle = (end - begin) / 2;
		if (middle > 500) {
			ListInitializerRecursiveTask li1 = new ListInitializerRecursiveTask(data, begin, (begin + middle));
			li1.fork();
			ListInitializerRecursiveTask li2 = new ListInitializerRecursiveTask(data, (begin + middle), end);
			l = li2.compute();
			l.addAll(li1.join());
		} else {
			for (int i = begin; i < end; i++) {
				l.add(data[i] % 2);
			}
		}
		return l;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		int lenght = 10000;
		ListInitializerRecursiveTask li1 = new ListInitializerRecursiveTask(IntStream.range(0, lenght).toArray(), 0,
				lenght);

		// Initiating the ForkJoinPool
		ForkJoinPool fjp = new ForkJoinPool();
		// Getting the list initiated recursively
		List<Integer> l = fjp.invoke(li1);

		System.out.println(l.size());
		System.out.println(l);
	}
}
