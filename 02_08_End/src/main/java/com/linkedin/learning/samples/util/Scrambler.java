package com.linkedin.learning.samples.util;


import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Scrambler {

    final static int maxAnagrams = 25;

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
            }while(positions[next] > 0);
        }
        return sb.toString();
    }

    public synchronized static String scrambleWithThreadLocalRandom(String valueToScramble) {
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
            }while(positions[next] > 0);
        }
        return sb.toString();
    }

    public static Optional scramble(String toScramble, List<String> listOfStrings, int mode) throws InterruptedException {
        switch (mode) {
            case 1: return Optional.of(scrambleRandom(toScramble));
            case 2: return Optional.of(scrambleThreadLocalRandom(toScramble));
            case 3: return Optional.of(scrambleWithSynchronizer(toScramble));
            case 4: return Optional.of(scrambleWithForLoop(toScramble));
            case 5: return Optional.of(syncScrambleWithForLoop(toScramble));
            case 6: return Optional.of(serialScrambleWithRandom(listOfStrings));
            case 7: return Optional.of(serialScrambleWithThreadLocalRandom(listOfStrings));
            case 8: return Optional.of(parallelScrambleWithThreadLocalRandom(listOfStrings));
            case 9: return Optional.of(parallelScrambleWithRandom(listOfStrings));
        }
        return Optional.empty();
    }


    public static String scrambleRandom(String stringToScramble){
        return scrambleWithRandom(stringToScramble);
    }

    public static String scrambleThreadLocalRandom(String stringToScramble){
        return scrambleWithThreadLocalRandom(stringToScramble);
    }

    public static synchronized String scrambleWithSynchronizer(String stringToScramble){
        return scrambleWithRandom(stringToScramble);
    }

    public static List<String> scrambleWithForLoop(String stringToScramble) throws InterruptedException {
        List<String> anagrams = new ArrayList<>(maxAnagrams);
        long startTime = System.currentTimeMillis();
        for(int i =0; i<maxAnagrams; i++){
            anagrams.add(Scrambler.scrambleWithRandom(stringToScramble));
            Thread.sleep(3000);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: "+(endTime-startTime));
        return anagrams;
    }

    public static synchronized List<String> syncScrambleWithForLoop(String stringToScramble) throws InterruptedException {
        List<String> anagrams = new ArrayList<>(maxAnagrams);
        for(int i =0; i<maxAnagrams; i++){
            anagrams.add(scrambleWithRandom(stringToScramble));
        }
        return anagrams;
    }

    public static List<String> serialScrambleWithRandom(List<String> stringsToScramble){
        return stringsToScramble.stream()
                .map(Scrambler::scrambleWithRandom)
                .collect(Collectors.toList());
    }

    public static List<String> serialScrambleWithThreadLocalRandom(List<String> stringsToScramble){
        return stringsToScramble.stream()
                .map(Scrambler::scrambleWithThreadLocalRandom)
                .collect(Collectors.toList());
    }

    public static List<String> parallelScrambleWithThreadLocalRandom(List<String> stringsToScramble){
        return stringsToScramble.parallelStream()
                .map(Scrambler::scrambleWithThreadLocalRandom)
                .collect(Collectors.toList());
    }

    public static List<String> parallelScrambleWithRandom(List<String> stringsToScramble){
        return stringsToScramble.parallelStream()
                .map(Scrambler::scrambleWithRandom)
                .collect(Collectors.toList());
    }

}
