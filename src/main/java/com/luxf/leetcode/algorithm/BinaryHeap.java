package com.luxf.leetcode.algorithm;

/**
 * 二叉堆：优先队列的实现方式、
 * TODO: 该二叉堆的[0]是空出来的、未被占用, 如果不空出来, 需要修改部分和index相关的代码
 *
 * @author 小66
 * @date 2020-07-14 19:02
 **/
public class BinaryHeap {
    private int currentSize;
    private int[] array;

    public BinaryHeap(int[] arr) {
        currentSize = arr.length;
        array = new int[arr.length + 2];
        // 由于array的第一个位置是空出来的、
        System.arraycopy(arr, 0, array, 1, arr.length);

//        // 可以直接调用N次insert()直接生成符合结构性质和堆序性质的二叉堆、
//        // 不要初始化currentSize的值、
//        for (int element : arr) {
//            insert(element);
//        }
        // 生成二叉堆、
        buildMinHeap();
    }

    /**
     * TODO: 插入前, 先判断是否需要扩容
     * 插入二叉堆：需要进行上滤(从最后一个比较到第一个节点)
     *
     * @param element
     */
    public void insert(int element) {
        // 上滤、
        // 默认该元素放在(实际没有赋值)数组的第一个空闲位置(完全二叉树的最后一个节点)、
        int hole = ++currentSize;
        // 和父节点比较大小, 如果父节点大, 就需要把父节点的位置空出来、然后 把hole变为 hole = hole/2
        for (; element < array[hole / 2]; hole /= 2) {
            // 把父节点放在空闲的位置、TODO: 不要进行位置交换(3次赋值)、减少开销
            array[hole] = array[hole / 2];
        }
        // 如果父节点小, 则就不需要更换位置、把当前元素插入到此空闲位置即可。
        array[hole] = element;
    }

    /**
     * 移除二叉堆：需要进行下滤
     *
     * @return 最小堆的root节点、
     */
    public int removeMin() {
        // 因为array[0]空出来的、也可以不空出来
        if (array.length < 2) {
            throw new RuntimeException("");
        }
        int removeElement = array[1];
        // 下滤：把最后一个元素,放在root节点、
        array[1] = array[currentSize--];
        // 从[1]开始下滤(root节点开始)--> array[0]是空出来的、
        minHeapPercolateDown(1);
        return removeElement;
    }

    /**
     * 把一个元素,放在从某个位置(array#index)开始进行下滤(从上往下比较)、
     *
     * @param hole
     */
    private void minHeapPercolateDown(int hole) {
        // 和上滤类似, 此位置[hole]暂时空闲、
        int temp = array[hole];
        int childIndex;
        // 当前节点和儿子节点比、
        for (; hole * 2 <= currentSize; hole = childIndex) {
            childIndex = hole * 2;
            // 如果childIndex == currentSize、则没有右儿子存在！
            if (childIndex != currentSize && array[childIndex + 1] < array[childIndex]) {
                // 右儿子比左儿子小、则修改较小节点的index、
                childIndex++;
            }
            // 比较父节点和较小儿子的大小、
            if (array[childIndex] > temp) {
                // 如果儿子节点大于父节点, 跳出循环！
                break;
            }
            // 将较小的儿子节点放在空闲位置、TODO: 不要进行位置交换(3次赋值)、减少开销
            array[hole] = array[childIndex];
        }
        // 如果父节点小, 则就不需要下滤、把temp元素插入到此空闲位置即可。
        array[hole] = temp;
    }

    /**
     * 将不规则的arr构建为二叉堆、
     */
    private void buildMinHeap() {
        // 根据二叉堆的结构性质, 此处 currentSize / 2 是为了保证每个节点都有儿子节点、
        for (int i = currentSize / 2; i > 0; i--) {
            minHeapPercolateDown(i);
        }
    }

    public int[] getArray() {
        return array;
    }
}
