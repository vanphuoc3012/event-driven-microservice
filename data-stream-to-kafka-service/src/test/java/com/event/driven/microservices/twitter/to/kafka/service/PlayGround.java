package com.event.driven.microservices.twitter.to.kafka.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlayGround {
    public static int sockMerchant(int n, List<Integer> ar) {
        // Write your code here
        int count = 0;
        Collections.sort(ar);
        int currentColor = ar.get(0);
        int countColor = 0;

        for (Integer color : ar) {
            if (color == currentColor) {
                countColor++;
            } else {
                System.out.println("Else: " + Math.floorDiv(countColor, 2));
                count += Math.floorDiv(countColor, 2);
                currentColor = color;
                countColor = 1;
            }
        }
        count += Math.floorDiv(countColor, 2);
        return count;
    }

    public static int sockMerchant2(int n, List<Integer> ar) {
        // Write your code here
        Collectors.groupingBy(i -> i, Collectors.toSet());
        return 0;
    }

    public static int sockMerchant3(int n, List<Integer> ar) {
        // Write your code here
        int[] freqs = new int[n + 1];
        int count = 0;

        for (int color : ar) {
            freqs[color]++;
        }

        for (int freq : freqs) {
            count += Math.floorDiv(freq, 2);
        }
        return count;
    }

    @Test
    void test() {
        Integer[] a = {50, 49, 38, 49, 78, 36, 25, 96, 10, 67, 78, 58, 98, 8, 53, 1, 4, 7, 29, 6, 59, 93, 74, 3, 67, 47, 12, 85, 84, 40, 81, 85, 89, 70, 33, 66, 6, 9, 13, 67, 75, 42, 24, 73, 49, 28, 25, 5, 86, 53, 10, 44, 45, 35, 47, 11, 81, 10, 47, 16, 49, 79, 52, 89, 100, 36, 6, 57, 96, 18, 23, 71, 11, 99, 95, 12, 78, 19, 16, 64, 23, 77, 7, 19, 11, 5, 81, 43, 14, 27, 11, 63, 57, 62, 3, 56, 50, 9, 13, 45};
        int i = sockMerchant(100, Arrays.asList(a));
        System.out.println(i);
        int i2 = sockMerchant3(100, Arrays.asList(a));
        System.out.println(i2);
        Assertions.assertThat(i2).isEqualTo(28);
    }
}
