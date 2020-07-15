package com.luxf.leetcode.algorithm;

import java.util.*;

/**
 * 冒泡排序,选择排序,插入排序,归并排序,堆排序,快速排序的具体实现。冒泡排序是最差的算法、
 *
 * @author 小66
 * @date 2020-07-13 9:12
 **/
public class SortAlgorithm {

    public static void main(String[] args) {
        int[] arr = {3, 1, 2, 4, 5, 6, 2, 3, 1, 7, 8, 9};
        quickSort(arr);
        System.out.println("arr = " + Arrays.toString(arr));
    }

    private static void differentSortTime() {
        Random random = new Random();
        int arrLength = 100000;
        int[] arr = new int[arrLength];
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < arrLength; i++) {
            int nextInt = random.nextInt(i + 1);
            arr[i] = nextInt;
            integerList.add(nextInt);
        }
        int[] bubbleArr = Arrays.copyOf(arr, arr.length);
        int[] selectArr = Arrays.copyOf(arr, arr.length);
        int[] insertArr = Arrays.copyOf(arr, arr.length);
        int[] mergeArr = Arrays.copyOf(arr, arr.length);
        int[] heapArr = Arrays.copyOf(arr, arr.length);
        int[] quickArr = Arrays.copyOf(arr, arr.length);

        // 107、
        long listStart = System.currentTimeMillis();
        integerList.sort(Comparator.comparing(i -> i));
        long listEnd = System.currentTimeMillis();
        System.out.println("listSortTime = " + (listEnd - listStart));

        // 19、 归并排序和最大二叉堆排序时间消耗接近、
        long mergeStart = System.currentTimeMillis();
        mergeSort(mergeArr);
        long mergeEnd = System.currentTimeMillis();
        System.out.println("mergeSortTime = " + (mergeEnd - mergeStart));

        // 15、
        long maxHeapStart = System.currentTimeMillis();
        maxHeapSort(heapArr);
        long maxHeapEnd = System.currentTimeMillis();
        System.out.println("maxHeapSortTime = " + (maxHeapEnd - maxHeapStart));

        // 38、
        long quickStart = System.currentTimeMillis();
        quickSort(quickArr);
        long quickEnd = System.currentTimeMillis();
        System.out.println("quickSortTime = " + (quickEnd - quickStart));

        // 18597
        long bubbleStart = System.currentTimeMillis();
        bubbleSort(bubbleArr);
        long bubbleEnd = System.currentTimeMillis();
        System.out.println("bubbleSortTime = " + (bubbleEnd - bubbleStart));

        // 8334
        long selectStart = System.currentTimeMillis();
        selectSort(selectArr);
        long selectEnd = System.currentTimeMillis();
        System.out.println("selectSortTime = " + (selectEnd - selectStart));

        // 531
        long insertStart = System.currentTimeMillis();
        insertSort(insertArr);
        long insertEnd = System.currentTimeMillis();
        System.out.println("insertSortTime = " + (insertEnd - insertStart));
    }

    /**
     * 冒泡排序：类似于水中冒泡，较大的数沉下去，较小的数慢慢冒起来，假设从小到大，即为较大的数慢慢往后排，较小的数慢慢往前排。
     * 直观表达，每一趟遍历，将一个最大的数移到序列末尾。
     */
    public static int[] bubbleSort(int[] arr) {
        // 比较次数
        int compareCount = 0;
        // 赋值次数、
        int assignCount = 0;

        // i < arr.length 和 i < arr.length -1 的区别：最后一遍(i < arr.length)的时候, 内部for循环已经进不去
        for (int i = 0; i < arr.length - 1; i++) {
            // 跳出外层循环的标识、
            boolean breakFlag = true;
            // j < arr.length - 1 - i：因为最后'i'个元素已经是大于arr[j], 不需要在比较、
            for (int j = 0; j < arr.length - 1 - i; j++) {
                compareCount++;
                if (arr[j] > arr[j + 1]) {
                    assignCount += 3;
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                    breakFlag = false;
                }
            }
            // 如果没有在发生swap操作,则表示已完成排序、提前跳出for循环、
            if (breakFlag) {
                break;
            }
        }
        System.out.println("assignCount = " + assignCount);
        System.out.println("compareCount = " + compareCount);
        return arr;
    }

    /**
     * 冒泡排序的另一种写法、
     */
    public static int[] bubbleSort2(int[] arr) {
        // 比较次数
        int compareCount = 0;
        // 赋值次数、
        int assignCount = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                compareCount++;
                if (arr[i] > arr[j]) {
                    assignCount += 3;
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
            System.out.println("arr = " + Arrays.toString(arr));
        }
        System.out.println("assignCount = " + assignCount);
        System.out.println("compareCount = " + compareCount);
        return arr;
    }

    /**
     * 选择排序：每一次遍历待排序的序列，记录最小（大）值的下标。和待排序第一个元素进行比较，判断是否交换、
     */
    public static int[] selectSort(int[] arr) {
        // 比较次数
        int compareCount = 0;
        // 赋值次数、
        int assignCount = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                compareCount++;
                if (arr[minIndex] > arr[j]) {
                    assignCount++;
                    minIndex = j;
                }
            }
            // arr[i]：待排序的元素(被选中的待排序元素)、 arr[minIndex]：arr[i]后面的最小元素、
            compareCount++;
            if (arr[i] > arr[minIndex]) {
                assignCount += 3;
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
        System.out.println("assignCount = " + assignCount);
        System.out.println("compareCount = " + compareCount);
        return arr;
    }

    /**
     * 插入排序：在一个有序的序列中找到待排序元素的位置，比如将3插入2，4，6这个有序序列中，先与6和4比较，直到和2比较之后，找到适合插入的位置(2之后)。
     * TODO：第一个元素默认有序。
     */
    public static void insertSort(int[] arr) {
        // 赋值次数、
        int assignCount = 0;
        for (int i = 0; i < arr.length; i++) {
            // 需要被插入有序序列的元素、
            int insertElement = arr[i];
            // 插入的位置、
            int j;
            /**
             * j > 0 && arr[j - 1] > insertElement ：相较于{@link #insertSort2(int[])} 优化比较的写法、 减少了x--的执行, 优化执行效率
             */
            for (j = i; j > 0 && arr[j - 1] > insertElement; j--) {
                // 把大于需要插入的元素往后移动。最后不大于insertElement的数的位置就空出来
                arr[j] = arr[j - 1];
                assignCount++;
            }
            // 插入元素、
            arr[j] = insertElement;
            assignCount++;
        }
        System.out.println("assignCount = " + assignCount);
    }

    public static void insertSort2(int[] arr) {
        // 比较次数
        int compareCount = 0;
        // 赋值次数、
        int assignCount = 0;
        for (int i = 0; i < arr.length; i++) {
            // 需要被插入有序序列的元素、
            int insertElement = arr[i];
            // 插入的位置、
            int x = i;
            for (int j = i; j > 0; j--) {
                compareCount++;
                if (arr[j - 1] > insertElement) {
                    // 把大于需要插入的元素往后移动。最后不大于insertElement的数的位置就空出来
                    arr[j] = arr[j - 1];
                    assignCount++;
                    x--;
                }
            }
            // 插入元素、
            arr[x] = insertElement;
            assignCount++;
        }
        System.out.println("assignCount = " + assignCount);
        System.out.println("compareCount = " + compareCount);
    }

    /**
     * 并归排序：采用经典的分治策略。
     * 分治法将问题递归分(divide)成一些小的问题然后求解，而治(conquer)的阶段则将分的阶段得到的各答案递归合并。
     * 空间换时间：多使用一个tempArr的内存、
     *
     * @param arr
     */
    public static void mergeSort(int[] arr) {
        // 避免递归时频繁开辟空间、
        int[] tempArr = new int[arr.length];
        binaryMergeSort(arr, 0, arr.length - 1, tempArr);
    }

    private static void binaryMergeSort(int[] arr, int left, int right, int[] tempArr) {
        if (left < right) {
            int middle = (left + right) / 2;
            // 将数组递归拆分为左右2部分, 直到左右都只有1个元素时、
            binaryMergeSort(arr, left, middle, tempArr);
            binaryMergeSort(arr, middle + 1, right, tempArr);
            // 合并、
            merge(arr, left, middle, right, tempArr);
        }
    }

    /**
     * 由于每次执行merge()都要创建临时数组、因此将在排序前, 先建好一个长度等于原数组长度的临时数组, 避免递归中频繁开辟空间。
     *
     * @param tempArr 等长的临时数组, 避免递归中频繁开辟空间、
     */
    private static void merge(int[] arr, int left, int middle, int right, int[] tempArr) {
        // 临时数组的指针、
        int pointer = 0;
        // 左序列的指针、
        int lp = left;
        // 右序列的指针、
        int rp = middle + 1;

        /*for (; lp <= middle && rp <= right; ) {// 这个for循环只有条件、
            if (arr[lp] < arr[rp]) {
                tempArr[pointer++] = arr[lp];
                lp++;
            } else {
                tempArr[pointer++] = arr[rp];
                rp++;
            }
        }*/
        // 左右序列, 有序的取数据, 存入临时的数组中、 使用while和for循环都可以、
        while (lp <= middle && rp <= right) {
            if (arr[lp] < arr[rp]) {
                tempArr[pointer++] = arr[lp];
                lp++;
            } else {
                tempArr[pointer++] = arr[rp];
                rp++;
            }
        }

        // 如果左边没取完, 将左边的依次添加到临时数组、
        while (lp <= middle) {
            tempArr[pointer++] = arr[lp++];
        }
        // 如果右边没取完, 将右边的依次添加到临时数组、
        while (rp <= right) {
            tempArr[pointer++] = arr[rp++];
        }

        // 将临时数组的内容, 复制到原来的数组对应的位置去、两种方式赋值数组：
        /*for (int k = 0; k < pointer; k++) {
            arr[left++] = tempArr[k];
        }*/
        System.arraycopy(tempArr, 0, arr, left, pointer);
    }

    /**
     * 利用二叉堆(完全二叉树的样子-->实际上是数组)实现 -> 堆排序：
     * 空间换时间：多使用一个tempArr的内存、
     * <p>
     * TODO: 二叉堆排序, 需要先将arr转换为一个二叉堆{@link BinaryHeap}、
     *
     * @param arr
     */
    public static void heapSort(int[] arr) {
        /**
         * 可以将{@link BinaryHeap#buildHeap()}方法改进一下, 直接将 arr转换为二叉堆、
         * 可以不用创建二叉堆的实例对象、详见{@link SortAlgorithm#minHeapSort(int[])}
         */
        BinaryHeap binaryHeap = new BinaryHeap(arr);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = binaryHeap.removeMin();
        }
    }

    public static void minHeapSort(int[] arr) {
        buildMinHeap(arr);
        // 如果是生成最大堆, 则无需newArr、内存消耗更少
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            newArr[i] = removeMin(arr, arr.length - 1 - i);
        }
        System.arraycopy(newArr, 0, arr, 0, arr.length);
    }

    /**
     * 最大堆的排序相对于最小堆排序, 减少newArr创建的内存消耗、
     *
     * @param arr
     */
    public static void maxHeapSort(int[] arr) {
        buildMaxHeap(arr);
        for (int i = 0; i < arr.length; i++) {
            int lastIndex = arr.length - 1 - i;
            arr[lastIndex] = removeMax(arr, lastIndex);
        }
    }

    /**
     * 将arr转换为最大二叉堆、--> 遍历将二叉堆(优先队列)的第一个元素取出即可、
     *
     * @param arr
     */
    private static void buildMaxHeap(int[] arr) {
        // 该二叉堆的[0]位置没有空出、
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            maxHeapPercolateDown(arr, i, arr.length);
        }
    }

    /**
     * 将arr转换为最小二叉堆、--> 遍历将二叉堆(优先队列)的第一个元素取出即可、
     *
     * @param arr
     */
    private static void buildMinHeap(int[] arr) {
        // 该二叉堆的[0]位置没有空出、
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            minHeapPercolateDown(arr, i, arr.length);
        }
    }

    /**
     * 最大堆的下滤方式
     */
    private static void maxHeapPercolateDown(int[] arr, int hole, int length) {
        int temp = arr[hole];
        // 较大儿子节点的index、
        int childIndex;
        // 当前节点和儿子节点比、
        for (; (hole * 2 + 1) < length; hole = childIndex) {
            childIndex = hole * 2 + 1;
            // 如果childIndex == length-1、则没有右儿子存在！
            if (childIndex != length - 1 && arr[childIndex + 1] > arr[childIndex]) {
                // 右儿子比左儿子更大、则修改childIndex
                childIndex++;
            }
            // 比较父节点和较大儿子的大小、
            if (arr[childIndex] < temp) {
                // 如果父节点大于儿子节点, 跳出循环！
                break;
            }
            // 将较大的儿子节点放在空闲位置、TODO: 不要进行位置交换(3次赋值)、减少开销
            arr[hole] = arr[childIndex];
        }
        // 如果儿子节点小, 则就不需要下滤、把temp元素插入到此空闲位置即可。
        arr[hole] = temp;
    }

    /**
     * 最小堆的下滤方式、
     */
    private static void minHeapPercolateDown(int[] arr, int hole, int length) {
        int temp = arr[hole];
        // 较小儿子节点的index
        int childIndex;
        // 当前节点和儿子节点比、
        for (; (hole * 2 + 1) < length; hole = childIndex) {
            childIndex = hole * 2 + 1;
            // 如果childIndex == length-1、则没有右儿子存在！
            if (childIndex != length - 1 && arr[childIndex + 1] < arr[childIndex]) {
                // 右儿子比左儿子小、则修改childIndex
                childIndex++;
            }
            // 比较父节点和较小儿子的大小、
            if (arr[childIndex] > temp) {
                // 如果儿子节点大于父节点, 跳出循环！
                break;
            }
            // 将较小的儿子节点放在空闲位置、TODO: 不要进行位置交换(3次赋值)、减少开销
            arr[hole] = arr[childIndex];
        }
        // 如果父节点小, 则就不需要下滤、把temp元素插入到此空闲位置即可。
        arr[hole] = temp;
    }

    /**
     * 移除最大的root元素、
     *
     * @param arr
     * @param lastIndex lastIndex是动态改变的、
     * @return removed element
     */
    private static int removeMax(int[] arr, int lastIndex) {
        int removeElement = arr[0];
        // 下滤：把没有进行下滤的最后一个元素,放在root节点、
        arr[0] = arr[lastIndex];
        // 从[0]开始下滤(最大堆的root节点开始)、
        maxHeapPercolateDown(arr, 0, lastIndex);
        return removeElement;
    }

    /**
     * 移除最小的root元素、
     *
     * @param arr
     * @param lastIndex 不是数组最后的索引、是数组长度和移除元素个数(下滤次数)的差：arr.length - (1 + i)
     * @return removed element
     */
    private static int removeMin(int[] arr, int lastIndex) {
        int removeElement = arr[0];
        // 下滤：把没有进行下滤的最后一个元素,放在root节点、
        arr[0] = arr[lastIndex];
        // 从[0]开始下滤(最小堆的root节点开始)、
        minHeapPercolateDown(arr, 0, lastIndex);
        return removeElement;
    }

    /**
     * 快速排序：基于一种叫做"二分"的思想, 利用双指针开始排序、
     *
     * @param arr
     */
    public static void quickSort(int[] arr) {
        medianQuickSort(arr, 0, arr.length - 1);
    }

    /**
     * TODO: 最好采用三数中值分割法, 如果取最左为基准位, 输入是反序, 那么快速排序消耗时间则是成倍的、不推荐基准位取最左元素、
     * 正常情况下, 最左基准位和三数中值基准位排序时间差不多、
     */
    private static void leftPivotQuickSort(int[] arr, int left, int right) {
        // 递归出口、
        if (left >= right) {
            return;
        }
        // 基准位、默认取最左
        int pivot = arr[left];
        int i = left;
        int j = right;
        while (i < j) {
            // TODO: 利用双指针与基准位的值比较大小, 最后交换双指针位置的值
            // 依次往左递减, 只需左侧取等即可、没有必要两端都取等
            // while (i < j && arr[j] >= pivot) {
            while (arr[j] > pivot) {
                j--;
            }
            // 依次往右递增、
            while (i < j && arr[i] <= pivot) {
                i++;
            }
            // 交换位置、
            if (i < j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // 将pivot放置到左边的最后(右边的最前)、最后将基准位与i和j相等的位置交换
        arr[left] = arr[i];
        arr[i] = pivot;
        // 递归排序pivot左边部分
        leftPivotQuickSort(arr, left, j - 1);
        // 递归排序pivot右边部分
        leftPivotQuickSort(arr, j + 1, right);
    }

    /**
     * 三数中值分割法快速排序、
     */
    private static void medianQuickSort(int[] arr, int left, int right) {
        // 递归出口、
        if (left >= right) {
            return;
        }
        // 基准位：中位数的index、
        int medianIndex = getMedianIndex(arr, left, right);
        int pivot = arr[medianIndex];
        int i = left;
        int j = right;
        while (i < j) {
            // TODO: 利用双指针与基准位的值比较大小, 最后交换双指针位置的值
            // 依次往左递减, 只需左侧取等即可、没有必要两端都取等
            // while (i < j && arr[j] >= pivot) {
            while (arr[j] > pivot) {
                j--;
            }
            // 依次往右递增、
            while (i < j && arr[i] <= pivot) {
                i++;
            }
            // 交换位置、
            if (i < j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        // 将pivot放置到左边的最后(右边的最前)、最后将基准位与i和j相等的位置交换
        // TODO: 不加该条件,直接交换位置(会把大的数字交换到小的前面),不只是顺序乱、存在数据丢失的情况。
        //  如果 medianIndex > i, 则已被提前进行交换; 如果 medianIndex < i 则有可能没有被交换、 以leftIndex为基准点的方式,肯定没有被交换！
        // 因为以leftIndex为基准点的方式, 不会出现 arr[i] > arr[left]的情况、
        if (medianIndex < i) {
            arr[medianIndex] = arr[i];
            arr[i] = pivot;
        }
        // 递归排序pivot左边部分
        medianQuickSort(arr, left, j - 1);
        // 递归排序pivot右边部分
        medianQuickSort(arr, j + 1, right);
    }

    /**
     * 三数中值分割法, 获取三数中值的index、
     *
     * @param arr   array
     * @param left  >= 0
     * @param right <= length-1
     * @return 中位数的index、
     */
    private static int getMedianIndex(int[] arr, int left, int right) {
        if (right - left < 2) {
            return left;
        }
        int middle = (left + right) / 2;
        int center = arr[middle];
        int low = arr[left];
        int high = arr[right];
        return high > low ? (high > center ? (center > low ? middle : left) : right) :
                (low > center ? (center > high ? middle : right) : left);
    }
}
