package com.linkedin.learning.samples.util;


import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Threads(5)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
public class ScramblerBenchMarker {
    final static int maxAnagrams = 25;
    @Param("MyNameIsWhat")
    String sourceText;
    List<String> listOfStrings;

    public static String scrambleWithRandom(String valueToScramble) {
        char[] options = valueToScramble.toCharArray();
        int[] positions = new int[options.length];
        Random randomizer = new Random();
        StringBuilder sb = new StringBuilder();
        int next = 0;
        for (int i = 0; i < options.length; i++) {
            do {
                next = randomizer.nextInt(valueToScramble.length());
                if (positions[next] == 0) {
                    sb.append(options[next]);
                    positions[next]++;
                    break;
                }
            } while (positions[next] > 0);
        }
        return sb.toString();
    }

    public static String scrambleWithThreadLocalRandom(String valueToScramble) {
        char[] options = valueToScramble.toCharArray();
        int[] positions = new int[options.length];
        Random randomizer = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        int next = 0;
        for (int i = 0; i < options.length; i++) {
            do {
                next = randomizer.nextInt(valueToScramble.length());
                if (positions[next] == 0) {
                    sb.append(options[next]);
                    positions[next]++;
                    break;
                }
            } while (positions[next] > 0);
        }
        return sb.toString();
    }

    @Benchmark
    public static String scrambleRandom(ScramblerBenchMarker stringsToScramble) {
        return scrambleWithRandom(stringsToScramble.sourceText);
    }

    @Benchmark
    public static synchronized String scrambleRandomWithSynchronizer(ScramblerBenchMarker stringsToScramble) {
        return scrambleWithRandom(stringsToScramble.sourceText);
    }

    @Benchmark
    public static String scrambleThreadLocalRandom(ScramblerBenchMarker stringsToScramble) {
        return scrambleWithThreadLocalRandom(stringsToScramble.sourceText);
    }

    @Benchmark
    public static synchronized String scrambleThreadLocalRandomWithSynchronizer(ScramblerBenchMarker stringsToScramble) {
        return scrambleWithThreadLocalRandom(stringsToScramble.sourceText);
    }

    @Benchmark
    public static List<String> serialScrambleWithRandom(ScramblerBenchMarker stringsToScramble) {
        return stringsToScramble.listOfStrings.stream()
                .map(ScramblerBenchMarker::scrambleWithRandom)
                .collect(Collectors.toList());
    }

    @Benchmark
    public static List<String> serialScrambleWithThreadLocalRandom(ScramblerBenchMarker stringsToScramble) {
        return stringsToScramble.listOfStrings.stream()
                .map(ScramblerBenchMarker::scrambleWithThreadLocalRandom)
                .collect(Collectors.toList());
    }

    @Benchmark
    public static List<String> parallelScrambleWithThreadLocalRandom(ScramblerBenchMarker stringsToScramble) {
        return stringsToScramble.listOfStrings.parallelStream()
                .map(ScramblerBenchMarker::scrambleWithThreadLocalRandom)
                .collect(Collectors.toList());
    }

    @Benchmark
    public static List<String> parallelScrambleWithRandom(ScramblerBenchMarker stringsToScramble) {
        return stringsToScramble.listOfStrings.parallelStream()
                .map(ScramblerBenchMarker::scrambleWithRandom)
                .collect(Collectors.toList());
    }

    public static void main(String[] params) throws IOException {
        Main.main(params);
    }

    @Setup(Level.Invocation)
    public void initListOfStrings() {
        listOfStrings = Arrays.asList("MyNameIsWhat", "MyNameIsWho", "MyNameIs", "MeShady", "Tango", "Alpha", "Yankee", "Oscar");
    }

}
