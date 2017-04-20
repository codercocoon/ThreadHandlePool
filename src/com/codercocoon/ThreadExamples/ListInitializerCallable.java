package com.codercocoon.ThreadExamples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

/**
 * This example represents how to create a pool of threads using ExecutorService
 * and Callable interfaces.
 * 
 * @author contact@codercocoon.com
 *
 */
public class ListInitializerCallable implements Callable<List<Integer>> {

	int[] data;

	public ListInitializerCallable(int[] data) {
		this.data = data;
	}

	/**
	 * Method call() to override for Callable objects.
	 */
	@Override
	public List<Integer> call() throws Exception {
		ArrayList<Integer> l = new ArrayList<>();
		for (int i = 0; i < data.length; i++) {
			l.add(data[i] % 2);
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

		ListInitializerCallable li1 = new ListInitializerCallable(IntStream.range(0, lenght / 2).toArray());
		ListInitializerCallable li2 = new ListInitializerCallable(IntStream.range((lenght / 2), lenght).toArray());

		// Fixing the pool to 2 threads
		ExecutorService es = Executors.newFixedThreadPool(2);
		Future<List<Integer>> f1 = es.submit(li1);
		Future<List<Integer>> f2 = es.submit(li2);

		// Getting the list returned of the 1st thread.
		List<Integer> l1 = f1.get();
		// Getting the list returned of the 2nd thread.
		List<Integer> l2 = f2.get();
		// Merging the two lists.
		l1.addAll(l2);

		System.out.println(l1.size());
		System.out.println(l1);

		// Shutting down the ExecutorService
		es.shutdown();
	}

}
