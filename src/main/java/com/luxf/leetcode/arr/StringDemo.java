package com.luxf.leetcode.arr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 小66
 * @date 2020-07-05 18:25
 **/
public class StringDemo {
    public static void main(String[] args) {
//        String[] strings = {"flower", "flow", "dog"};
//        String commonPrefix = longestCommonPrefix(strings);
//        System.out.println("commonPrefix = " + commonPrefix);

//        String str = "civilwartestingwhetherthatnaptionoranynartionsoconceivedandsod";
//        String palindrome = longestPalindrome(str);
//        System.out.println("palindrome = " + palindrome);
//        String words = reverseWords("a good   example");
//        System.out.println("words = " + words);
//
//        String reverseString = reverseString("civilwartestingwhetherthatnaptionoranynartionsoconceivedandsod");
//        System.out.println("reverseString = " + reverseString);

//        int[] arr = {5, 25, 75};
//        int[] ints = twoSum(arr, 100);
//        System.out.println("ints = " + Arrays.toString(ints));

//        int[] arr = {5, 3, 4, 1, 2, 3, 1, 6};
//        int newLength = removeElement(arr, 3);
//        System.out.println("newLength = " + newLength);
//        int[] arr = {2, 0, 2, 1, 0, 1, 3, 1, 2, 3, 3, 5};
//        int maxConsecutiveOnes = findMaxConsecutiveOnes(arr, 0);
//        System.out.println("maxConsecutiveOnes = " + maxConsecutiveOnes);
        int[] arr = {1, 2, 3, 4, 5};
        int minLength = minSubArrayLen(11, arr);
        System.out.println("minLength = " + minLength);
    }

    /**
     * 最长公共前缀、
     * 如果是取最长的公共内容,就需要双指针来处理、
     *
     * @param strings arr
     * @return prefix
     */
    private static String longestCommonPrefix(String[] strings) {
        List<String> asList = Arrays.asList(strings);
        asList.sort(Comparator.comparingInt(String::length));

        String shortString = asList.get(0);

        return getCommonPrefix(asList, shortString);
    }

    private static String getCommonPrefix(List<String> asList, String shortString) {
        long count = asList.stream().filter(str -> !str.startsWith(shortString)).count();
        if (count > 0) {
            return getCommonPrefix(asList, shortString.substring(0, shortString.length() - 1));
        } else {
            return shortString;
        }
    }

    /**
     * 获取最长的回文子串: "babad"->"bab"/"aba"都是有效答案、 "ac"->"a"  "b"->"b"
     * <p>
     * TODO: 字符串太长, 会出现OOM错误、如何优化算法？
     */
    private static String longestPalindrome(String str) {
        return getPalindrome(str, 0, str.length());
    }

    /**
     * 输入: "the sky is blue"
     * 输出: "blue is sky the"
     *
     * @param str str
     */
    public static String reverseWords(String str) {
        String[] split = str.split(" ");
        List<String> stringList = Stream.of(split).filter(s -> !" ".equals(s) && !"".equals(s)).collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        for (int i = stringList.size() - 1; i >= 0; i--) {
            builder.append(stringList.get(i)).append(" ");
        }
        return builder.toString().trim();
    }

    public static int arrayPairSum(int[] nums) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int num : nums) {
            list.add(num);
        }
        list.sort(Comparator.naturalOrder());
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i % 2 == 0) {
                result.add(list.get(i));
            }
        }
        return result.stream().reduce((integer, integer2) -> integer + integer2).get();
    }

    public static int[] twoSum(int[] numbers, int target) {
        if (numbers.length == 2 && (numbers[0] + numbers[1] == target)) {
            return new int[]{1, 2};
        }
        int targetIndex = getTargetIndex(numbers, target, 0, numbers.length);
        if (targetIndex == numbers.length) {
            targetIndex -= 1;
        }
        for (int i = 0; i < targetIndex; i++) {
            for (int j = i + 1; j <= targetIndex; j++) {
                if (numbers[i] + numbers[j] == target) {
                    return new int[]{i + 1, j + 1};
                }
            }
        }
        return null;
    }

    private static int getTargetIndex(int[] numbers, int target, int start, int end) {
        if (numbers[numbers.length - 1] <= target) {
            return numbers.length - 1;
        }
        int middle = (start + end) / 2;
        if (end - start <= 1) {
            return end;
        }
        if (numbers[middle] >= target) {
            return getTargetIndex(numbers, target, start, middle);
        } else {
            return getTargetIndex(numbers, target, middle, end);
        }
    }

    /**
     * 考虑空间限制：如果我们不使用额外的数组，只是在原数组上进行操作呢？
     * <p>
     * TODO: 快慢双指针、
     * 采用快慢指针的思想：初始化一个快指针 fast 和一个慢指针 slow，fast 每次移动一步，而 slow 只当 fast 指向的值不等于 val 时才移动一步。
     *
     * @param arr     arr
     * @param element 需要被移除的元素、
     * @return 被移除后数组的新长度！
     */
    private static int removeElement(int[] arr, int element) {
        int slow = 0;
        for (int fast = 0; fast < arr.length; fast++) {
            if (arr[fast] != element) {
                arr[slow] = arr[fast];
                slow++;
            }
        }
        // 新长度是 slow、 但是 arr[].length 没有变化, 如果要返回新的数组, 就必须使用额外的数组！
        return slow;
    }


    /**
     * 根据给定数组(nums), 计算其中给定数字(target)的最大连续个数。
     *
     * @return maxConsecutiveLength
     */
    private static int findMaxConsecutiveOnes(int[] nums, int target) {
        int max = 0;
        int tempMax = 0;
        for (int num : nums) {
            if (num != target) {
                if (max < tempMax) {
                    max = tempMax;
                }
                tempMax = 0;
            } else {
                tempMax++;
            }
        }
        return tempMax < max ? max : tempMax;
    }

    /**
     * 给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的子数组(连续的)，并返回其长度。如果不存在符合条件的子数组，返回 0。
     * TODO: 利用双指针, 完成一个连续的区间获取、(滑动窗口)
     *
     * @param s    sum
     * @param nums arr
     * @return minLength
     */
    private static int minSubArrayLen(int s, int[] nums) {

        return getMinLength(s, nums);
//        return getMinLength(s, nums, 1);
    }

    private static int getMinLength(int s, int[] nums) {
        int l = nums.length;
        if (l == 0) {
            return 0;
        }
        int start = 0;
        int end = 0;
        int sum = 0;
        int ans = 0;
        while (end < l) {
            sum += nums[end];
            while (sum >= s) {
                if (ans > end - start + 1 || ans == 0) {
                    ans = end - start + 1;
                }
                sum -= nums[start];
                start++;
            }
            end++;
        }
        return ans;
    }

    /**
     * 该方法适合取等于、改成大于等于在数据量过大的时候会超时、
     */
    private static int getMinLength(int s, int[] nums, int minLength) {
        if (minLength > nums.length) {
            return 0;
        }
        if (minLength == 1) {
            for (int num : nums) {
//                if (num >= s) {
                if (num == s) {
                    return minLength;
                }
            }
            minLength++;
        }
        // 利用双指针、取连续的区间
        for (int start = 0, end = minLength; end <= nums.length; start++, end++) {
            int sum = 0;
            for (int j = start; j < end; j++) {
                sum += nums[j];
            }
//            if (sum >= s) {
            if (sum == s) {
                int[] result = new int[minLength];
                System.arraycopy(nums, start, result, 0, minLength);
                System.out.println("result = " + Arrays.toString(result));
                return minLength;
            }
        }
        return getMinLength(s, nums, minLength + 1);
    }

    /**
     * 利用双指针算法,完成 字符串翻转！
     *
     * @param str
     * @return
     */
    public static String reverseString(String str) {
        char[] charArray = str.toCharArray();
        for (int start = 0, end = charArray.length - 1; start < end; start++, end--) {
            char temp = charArray[start];
            charArray[start] = charArray[end];
            charArray[end] = temp;
        }
        StringBuilder builder = new StringBuilder();
        for (char c : charArray) {
            builder.append(c);
        }
        return builder.toString();
    }

    private static String getPalindrome(String str, int start, int subLength) {
        if (subLength == 1) {
            return str.substring(0, 1);
        }
        if (str.length() < (start + subLength)) {
            return getPalindrome(str, 0, subLength - 1);
        }
        String substring = subString(str, start, subLength);
        if (validatePalindrome(substring)) {
            return substring;
        }
        return getPalindrome(str, start + 1, subLength);
    }

    private static String subString(String str, int beginIndex, int length) {
        return str.substring(beginIndex, beginIndex + length);
    }

    private static boolean validatePalindrome(String substring) {
        int length = substring.length();
        int middle = length / 2;
        if (length % 2 == 0) {
            String left = substring.substring(0, middle);
            String right = substring.substring(middle);
            StringBuilder builder = new StringBuilder(right);
            right = builder.reverse().toString();
            return left.equals(right);
        } else {
            String left = substring.substring(0, middle);
            String right = substring.substring(middle + 1);
            StringBuilder builder = new StringBuilder(right);
            right = builder.reverse().toString();
            return left.equals(right);
        }
    }
}
