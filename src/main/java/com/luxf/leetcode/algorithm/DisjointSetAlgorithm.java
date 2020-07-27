package com.luxf.leetcode.algorithm;

/**
 * 并查集：不相交集类算法、  并查集是树形的数据结构, 用于处理不相交集合的合并(union)和查询(find)问题。
 * 并查集解析基本思想：初始化,一个森林每个都为独立。通常用数组表示，每个值初始为-1。各自为根,即 arr[i] = -1、
 * <p>
 * (a,b)：表示a,b的集合合并(b -> a)！b指向a、
 * TODO: 原本arr[a]= -1, arr[b]= -1; 合并后 arr[a]= -1, arr[b]= a;
 * 合并时有2种情况：
 * 1、a,b都是独立的(没有和其他合并)、直接指向即可、 ()
 * 2、如果有集合(可能有父亲，可能自己是根)，那么不能直接操作a,b(因为a,b可能已经指向其他.),那么只能操作a,b的祖先。
 * <p>
 * 树的高度+1的化那么整个元素查询的效率都会降低！
 * 因此通常是：小数指向大树(或者低树指向高树)，这个使得查询效率能够增加！
 * <p>
 * TODO: 如何查看a,b是否在一个集合？
 * 查看是否在一个集合, 只需要查看节点根祖先的结果是否相同即可。
 * 因为只有根的数值是负的，而其他都是正数表示指向的元素。所以只需要递归找到不为正数进行比较即可！
 * <p>
 * <p>
 * TODO: 路径压缩可以降低运行时间、
 * 当我们调用递归的时候，可以顺便进行压缩路径。
 * 因为我们查找一个元素其实只需要直到它的祖先, 所以当他距离祖先近, 那么下次查询就很快。
 *
 * @author 小66
 * @date 2020-07-16 18:20
 **/
public class DisjointSetAlgorithm {
    //TODO: 差存储元素的arr? 没看懂咋具体实现 不相交集类、

    private int[] set;

    public DisjointSetAlgorithm(int elementNum) {
        set = new int[elementNum];
        // 将数组元素初始化为 -1 (默认每个index都是一颗树)、
        for (int i = 0; i < elementNum; i++) {
            set[i] = -1;
        }
    }

    /**
     * 简单的find()实现、
     *
     * @param x element
     * @return x的根节点(祖先或自己)的index
     */
    public int simpleFind(int x) {
        if (set[x] < 0) {
            return x;
        }
        return simpleFind(set[x]);
    }

    /**
     * 简单的union()实现、arr[root1]<0, arr[root2]<0  不实用,基本理念是这样求并！
     *
     * @param root1
     * @param root2
     */
    public void simpleUnion(int root1, int root2) {
        set[root2] = root1;
    }

    /**
     * 通过灵巧求并算法,可以更好的合并、
     * 灵巧求并分为：1、按大小求并。2、按高度求并！
     * <p>
     * TODO: 按大小求并时, 需要对树的大小计算。合并后的树的大小,为合并前2个树的大小的和。
     * 例如：arr[a] =-1, arr[b] =-1; union(a,b)后, arr[a] =-2, arr[b] =a;
     */
    public void unionBySize(int root1, int root2) {
        // 注意：arr[root]是负数、
        if (set[root1] < set[root2]) {
            // 树的大小求和、
            set[root1] = set[root1] + set[root2];
            // 将小的树指向大的树、
            set[root2] = root1;
        } else {
            set[root2] = set[root1] + set[root2];
            set[root1] = root2;
        }
    }

    /**
     * TODO: 按高度求并, 当两树的高度相等时, 高度才增1、 否则就使用浅的树成为深的树的子树、
     * 注意：由于该高度是负数(越小树越深)、
     */
    public void unionByHeight(int root1, int root2) {
        // root1 更深、更高
        if (set[root1] < set[root2]) {
            set[root2] = root1;
        } else {
            // 高度相等时, 深度增1、
            if (set[root1] == set[root2]) {
                set[root2]--;
            }
            // root2为新的根、 root1 -> root2、
            set[root1] = root2;
        }
    }

    /**
     * TODO：利用 路径压缩技巧 进行find()操作、
     * 路径压缩：在x执行find()的过程中, x的所有父节点, 都直接指向根节点！
     *
     * @param x
     * @return 此函数会返回x元素对应的根节点元素、
     */
    public int compressFind(int x) {
        if (set[x] < 0) {
            return x;
        }
        // 相比于普通的find()、此处使set[x]等于返回值。将x的所有父节点、最终指向到根节点！
        // 将 set[x] 指向 compressFind()的最终返回值--> root节点、
        return set[x] = compressFind(set[x]);
    }
}

