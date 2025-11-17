package k23cnt2.nmt.day02.tight_loosely_coupling;

import java.util.Arrays;

// Loosely coupling: Thuật toán BubbleSort cài đặt interface SortAlgorithm
public class LooselyBubbleSortAlgorithm implements SortAlgorithm {

    @Override
    public void sort(int[] array) {
        System.out.println("Sorted using bubble sort algorithm");
        Arrays.stream(array).sorted().forEach(System.out::println);
    }
}
