package com.luxf.leetcode.arr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 小66
 * @date 2020-07-05 13:15
 **/
public class MiddleIndex {

    public static void main(String[] args) {
        MiddleIndex index = new MiddleIndex();
//        int middleIndex = index.getMiddleIndex();
//        System.out.println("middleIndex = " + middleIndex);
//        int searchInsertIndex = index.getSearchInsertIndex(2);
//        System.out.println("searchInsertIndex = " + searchInsertIndex);
//        int[][] intervals = {{2, 3}, {4, 6}, {5, 7}, {3, 4}};
//        int[][] merge = index.merge(intervals);
//        System.out.println("merge = " + Arrays.deepToString(merge));
        int[][] matrix = {{0, 1, 2, 0}, {3, 0, 5, 2}, {1, 3, 1, 5}, {2, 3, 4, 5}};
        index.setZeroes(matrix);
    }

    /**
     * [
     * [1,1,0],
     * [1,0,1],
     * [1,1,1]
     * ]
     *
     * @param matrix 矩阵
     */
    private void setZeroes(int[][] matrix) {
        // y,x的坐标：["0,2", "1,1"]
        List<String> coordinate = new ArrayList<>();
        for (int y = 0; y < matrix.length; y++) {
            int[] ints = matrix[y];
            for (int x = 0; x < ints.length; x++) {
                int xValue = ints[x];
                if (xValue == 0) {
                    coordinate.add(y + "," + x);
                }
            }
        }

        List<Integer> values = new ArrayList<>();
        List<Integer> keys = new ArrayList<>();
        for (String str : coordinate) {
            String v = str.split(",")[1];
            String key = str.split(",")[0];
            values.add(Integer.parseInt(v));
            keys.add(Integer.parseInt(key));
        }
        for (int i = 0; i < matrix.length; i++) {
            // 将x轴的值变为0、
            for (Integer next : values) {
                matrix[i][next] = 0;
            }
            for (int j = 0; j < matrix[i].length; j++) {
                // 将y轴的值变为0、
                if (keys.contains(i)) {
                    matrix[i][j] = 0;
                }
            }
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    private int[][] merge(int[][] intervals) {

        int[][] result = new int[intervals.length][2];
        List<Integer> changeIndex = new ArrayList<>();
        List<Integer> originalZero = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            int[] interval = intervals[i];
            int start = interval[0];
            int end = interval[1];
            if (originalZero.size() == 0 && start == 0 && end == 0) {
                originalZero.add(i);
            }
            for (int j = i + 1; j < intervals.length; j++) {
                boolean changeFlag = false;
                int[] ints = intervals[j];
                int startInts = ints[0];
                int endInts = ints[1];
                if (start >= startInts && start <= endInts) {
                    start = startInts;
                    changeFlag = true;
                }
                if (end <= endInts && end >= startInts) {
                    end = endInts;
                    changeFlag = true;
                }
                if (start < startInts && end > endInts) {
                    changeFlag = true;
                }
                if (changeFlag) {
                    changeIndex.add(j);
                }
            }
            if (!changeIndex.contains(i)) {
                result[i] = new int[]{start, end};
            }
        }
        List<String> stringList = new ArrayList<>();
        for (int[] ints1 : result) {
            int i1 = ints1[0];
            int i2 = ints1[1];
            stringList.add(i1 + "," + i2);
        }
        stringList = stringList.stream().distinct().collect(Collectors.toList());
        if (originalZero.size() == 0 || stringList.stream().filter(str -> str.startsWith("0,") || str.endsWith(",0")).count() > 1) {
            stringList.remove("0,0");
        }
        result = new int[stringList.size()][2];
        for (int i = 0; i < stringList.size(); i++) {
            String stringInteger = stringList.get(i);
            int[] ints = new int[2];
            String[] split = stringInteger.split(",");
            ints[0] = Integer.parseInt(split[0]);
            int i1 = Integer.parseInt(split[1]);
            ints[1] = i1;
            result[i] = ints;
        }
        if (result.length < intervals.length) {
            return merge(result);
        }
        return result;
    }

    public int getSearchInsertIndex(int target) {
        int[] nums = {3, 5, 2, 1};
        List<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }
        list = list.parallelStream().distinct().sorted().collect(Collectors.toList());
        return getTargetIndex(target, list, 0, list.size() - 1);
    }

    private int getTargetIndex(int target, List<Integer> list, int start, int end) {
        int max = list.get(end);
        if (max > target) {
            /**
             *  通过二分法递归、
             */
            int middle = (start + end) / 2;
            if (list.get(middle) > target) {
                if (middle == 0) {
                    list.add(0, target);
                    System.out.println("list = " + list);
                    return 0;
                }
                return getTargetIndex(target, list, start, middle - 1);
            }
            return getTargetIndex(target, list, middle + 1, end);
        } else if (max == target) {
            System.out.println("list = " + list);
            return end;
        }
        list.add(end + 1, target);
        System.out.println("list = " + list);
        return end + 1;
    }

    private int getMiddleIndex() {
        int[] nums = {-1, -1, -1, 0, 1, 1};
        return computeIndex(nums, 0);
    }

    /**
     * 效率不高、
     *
     * @param nums
     * @param middle
     */
    private int computeIndex(int[] nums, int middle) {
        if (middle >= nums.length) {
            return -1;
        }
        int leftSum = 0;
        for (int i = 0; i < middle; i++) {
            leftSum += nums[i];
        }
        int rightSum = 0;
        for (int i = middle + 1; i < nums.length; i++) {
            rightSum += nums[i];
        }
        if (leftSum == rightSum) {
            return middle;
        }

        return computeIndex(nums, middle + 1);
    }
}
