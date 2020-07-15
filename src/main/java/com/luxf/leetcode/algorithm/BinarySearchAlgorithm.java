package com.luxf.leetcode.algorithm;

/**
 * @author 小66
 * @date 2020-07-10 13:31
 **/
public class BinarySearchAlgorithm {

    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 5, 7};
        int index = binarySearch(arr, 7);
        System.out.println("index = " + index);
    }

    /**
     * 二分查找算法的前提是有序！
     */
    private static int binarySearch(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int low = 0, high = nums.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                // 向右查找, 如果不 + 1, 则有可能死循环
                low = mid + 1;
            } else {
                // 向左查找, 如果不 - 1, 则有可能死循环
                high = mid - 1;
            }
        }
        return -1;
    }
}
