package com.luxf.leetcode.arr;

/**
 * 二分查找：如果集合是无序的，我们可以总是在应用二分查找之前先对其进行排序。
 * TODO: 循环能干的事，递归都能干；递归能干的事，循环不一定能干。如果使用循环并不困难的话，最好使用循环。
 *       二分法尽量不使用递归, 由于递归需要系统堆栈, 所以空间消耗要比非递归代码要大很多。如果递归深度太大,可能系统撑不住。
 * @author 小66
 * @date 2020-07-06 18:23
 **/
public class BinarySearch {
    public static void main(String[] args) {
//        int[] arr = {-1, 0, 3, 5, 9, 12};
//        int search = binarySearch(arr, 9);
//        System.out.println("search = " + search);

//        int sqrt = sqrt(23);
//        System.out.println("sqrt = " + sqrt);
        int[] arr = {1, 2, 1};
        int peakElement = findPeakElement(arr);
        System.out.println("peakElement = " + peakElement);

    }

    /**
     * 有序的（升序）整型数组, 不存在target时,返回 -1、
     */
    private static int binarySearch(int[] nums, int target) {
        return getTargetIndex(nums, target, 0, nums.length - 1);
    }

    /**

     */
    private static int getTargetIndex(int[] nums, int target, int start, int end) {
        int max = nums[end];
        if (max > target) {
            /**
             *  通过二分法递归、
             */
            int middle = (start + end) / 2;
            if (nums[middle] > target) {
                if (middle == 0) {
                    return -1;
                }
                return getTargetIndex(nums, target, start, middle - 1);
            }
            return getTargetIndex(nums, target, middle + 1, end);
        } else if (max == target) {
            return end;
        }
        return -1;
    }

    /**
     * 力扣二分查询模板写法-1
     */
    private static int binarySearchTemplate_1(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                // 向右查找
                left = mid + 1;
            } else {
                // 向左查找
                right = mid - 1;
            }
        }

        // left > right
        return -1;
    }

    /**
     * 二分查找的高级模板。它用于查找需要访问数组中当前索引及其直接右邻居索引的元素或条件。
     */
    private static int binarySearchTemplate_2(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0, right = nums.length;
        while (left < right) {
            // int mid = (left + right) / 2; 有啥区别？
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        // Post-processing:
        // End Condition: left == right
        if (left != nums.length && nums[left] == target) {
            return left;
        }
        return -1;
    }

    /**
     * 二分查找的另一种独特形式。 它用于搜索需要访问当前索引及其在数组中的直接左右邻居索引的元素或条件。
     */
    private static int binarySearchTemplate_3(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0, right = nums.length - 1;
        while (left + 1 < right) {
            // Prevent (left + right) overflow
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid;
            } else {
                right = mid;
            }
        }

        // Post-processing:
        // End Condition: left + 1 == right
        if (nums[left] == target) {
            return left;
        }
        if (nums[right] == target) {
            return right;
        }
        return -1;
    }


    /**
     * 平方根：由于返回类型是整数，结果只保留整数的部分、  TODO: 数字太大会超时, 需要优化算法
     */
    private static int sqrt(int x) {
        int left = 0;
        int right = x;
        while (left <= right) {
            int middle = (left + right) / 2;
            if (middle * middle < x) {
                if ((middle + 1) * middle + 1 > x) {
                    return middle;
                }
                left = middle + 1;
            } else if (middle * middle == x) {
                return middle;
            } else {
                if ((middle - 1) * middle - 1 < x) {
                    return middle - 1;
                }
                right = middle - 1;
            }
        }
        return 0;
    }

    public static int findPeakElement(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }
        if (nums.length == 2) {
            return nums[0] > nums[1] ? 0 : 1;
        }
        int first = 0;
        int last = 0;
        for (int num : nums) {
            if (nums[0] > num) {
                first++;
            }
            if (nums[nums.length - 1] > num) {
                last++;
            }
        }
        if (first == nums.length - 1) {
            return 0;
        }
        if (last == nums.length - 1) {
            return nums.length - 1;
        }
        // 上面的代码需要优化到下面这个for循环内部、
        for (int left = 1, right = nums.length - 2; left <= right; left++, right--) {
            boolean leftFlag = nums[left - 1] < nums[left] && nums[left] > nums[left + 1];
            if (leftFlag) {
                return left;
            }
            boolean rightFlag = nums[right - 1] < nums[right] && nums[right] > nums[right + 1];
            if (rightFlag) {
                return right;
            }
        }
        return -1;
    }
}