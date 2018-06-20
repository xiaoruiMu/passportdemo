package com.example.passportdemo.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Sums callable
 *
 * @author muxiaorui
 * @create 2018-06-07 11:28
 **/
public class Sums {

    static class Sum implements Callable<Long> {
        private final long from;
        private final long to;

        Sum(long from, long to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public Long call() {
            long acc = 0;
            for (long i = from; i <= to; i++) {
                acc = acc + i;
            }
            System.out.println(Thread.currentThread().getName() + " : " + acc);
            return acc;
        }
    }

    public static void main(String[] args) throws Exception {
        List list=new ArrayList(){  new Sum(0,10)};
        ExecutorService executor = Executors.newFixedThreadPool(3);
        List<Future<Long>> results = executor.invokeAll(new ArrayList<Sum>(
                new Sum(0, 10), new Sum(0, 1_000), new Sum(0, 1_000_000)
        ));
        executor.shutdown();

        for (Future<Long> result : results) {
            System.out.println(result.get());
        }
    }
}
