package com.luxf.leetcode.algorithm;

import java.util.HashMap;

/**
 * AVL树：带有平衡条件的二叉查询树、每个节点的左子树和右子树的高度差不能大于1的二叉查询树
 * <p>
 * TODO: 树的基本定义、 根(root)节点、树叶(leaf)、深度(depth)、高度(height)
 * 根节点：树的顶端、(树由1个根节点和N个子节点组成)。
 * 树叶：没有儿子的子节点。
 * 深度：从根节点(root)到当前子节点的唯一路径的长。
 * 高度：从当前节点到该节点的一片树叶的最长路径的长。
 * TODO: 一棵树的深度等于它最深的树叶的深度, 该深度总是等于这棵树的高。
 * <p>
 * 树：需要重写比较器{@link Comparable#compareTo(Object)}, 来决定放在左边还是右边、
 * <p>
 * 删除root节点(包含子树的root节点)时,左右子树都不是null的情况下的具体实现：详见{@link #remove(Comparable, AvlBinaryNode)}方法、
 *
 * @author 小66
 * @date 2020-07-11 19:42
 **/
public class MyBalanceBinarySearchTree<E extends Comparable<E>> {
    private int size = 0;
    /**
     * AVL树允许的高差、
     */
    private static final int ALLOWED_HEIGHT_DIFFERENCE = 1;

    private AvlBinaryNode<E> root = null;

    public MyBalanceBinarySearchTree() {
    }

    public void addElement(E element) {
        // 都是从root节点开始递归添加、
        root = insert(element, root);
    }

    public boolean removeElement(E element) {
        boolean contains = contains(element, root);
        if (contains) {
            remove(element, root);
        }
        return contains;
    }

    public int size() {
        return size;
    }

    public int getTreeHeight() {
        return root.height;
    }

    public boolean contains(E element) {
        return contains(element, root);
    }

    public E findMin() {
        return findMin(root);
    }

    public E findMax() {
        return findMax(root);
    }

    private E findMax(AvlBinaryNode<E> node) {
        if (node == null) {
            return null;
        }
        if (node.right == null) {
            return node.element;
        } else {
            return findMax(node.right);
        }
    }

    private E findMin(AvlBinaryNode<E> node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            return node.element;
        } else {
            return findMin(node.left);
        }
    }

    private boolean contains(E element, AvlBinaryNode<E> node) {
        if (element == null || node == null) {
            return false;
        }
        int compareTo = element.compareTo(node.element);
        if (compareTo == 0) {
            return true;
        } else if (compareTo > 0) {
            return contains(element, node.right);
        } else {
            return contains(element, node.left);
        }
    }

    private AvlBinaryNode<E> insert(E element, AvlBinaryNode<E> node) {
        if (node == null) {
            // node == null时, 才会新增、
            size++;
            return new AvlBinaryNode<>(element, null, null);
        }
        int compareTo = element.compareTo(node.element);

        // 如果已存在, 暂时不考虑重复节点的问题、
        if (compareTo == 0) {
            return node;
        }
        /**
         *  此处递归赋值很巧妙, 如果递归返回前的值, 进行了单旋、双旋的操作,
         *  返回的节点就会发生改变, 此处对返回值进行赋值, 则就更改了关联的节点！
         */
        if (compareTo > 0) {
            // 插到树的右边、
            node.right = insert(element, node.right);
        } else {
            // 插到树的左边、
            node.left = insert(element, node.left);
        }
        // 计算高度值、
        node.height = getMaxHeight(node);

        // 利用单旋转和双旋转 保证符合AVL树：每个节点的左子树和右子树的高度差不能大于1。
        return balance(node);

        // 如果不进行旋转变化为AVL树, 则递归结束后, 最终返回的node就是传递进来的root节点、
        // return node;
    }

    /**
     * 最复杂的情况就是删除的节点, 左子树和右子树都不为null的情况、
     * TODO: 删除操作没有进行AVL平衡、 递归调用balance()方法处理一下？
     *
     * @return 子树的新的根节点、
     */
    private AvlBinaryNode<E> remove(E element, AvlBinaryNode<E> node) {
        if (node == null) {
            return null;
        }
        int compareTo = element.compareTo(node.element);
        if (compareTo > 0) {
            node.right = remove(element, node.right);
        } else if (compareTo < 0) {
            node.left = remove(element, node.left);
        } else if (node.left != null && node.right != null) {
            // 当被删除的节点的左子树和右子树都不为null时、--> 此时的该节点相当于是一颗子树的root节点
            /**
             *  TODO: 删除root节点的巧妙之处、
             *  node.element = findMin(node.right); 不可以替换为：E minElement = findMin(node.right);
             *  TODO: 实际上此时删除的是右边最小的Node、但是将右侧最小Node的E对象赋值给了当前节点、--> 看似子树root节点被删除, 实际只是更换了root节点的E对象的值, 而root节点的地址值没有变换
             */
            node.element = findMin(node.right);
            node.right = remove(node.element, node.right);
        } else {
            node = node.left != null ? node.left : node.right;
        }
        return node;
    }

    /**
     * 进行AVL旋转后, 也许会改变root节点、
     *
     * @param node
     * @return
     */
    private AvlBinaryNode<E> balance(AvlBinaryNode<E> node) {
        if (node == null) {
            return null;
        }
        // 左旋
        if (getHeight(node.left) - getHeight(node.right) > ALLOWED_HEIGHT_DIFFERENCE) {
            // 左单旋转
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                node = rotateWithLeftChild(node);
            } else {
                // 左双旋转 --> 先node节点的左节点子树右单旋、然后node节点左单旋。
                node = doubleRotateWithLeftChild(node);
            }
        } else if (getHeight(node.right) - getHeight(node.left) > ALLOWED_HEIGHT_DIFFERENCE) {
            // 右旋
            // 右单旋转
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                node = rotateWithRightChild(node);
            } else {
                // 右双旋转
                node = doubleRotateWithRightChild(node);
            }
        }
        return node;
    }

    /**
     * TODO: (3,2,1)的树和(3,1,2)的树结构不同：
     * 1：(3,2,1)只需要进行一次左单旋即可满足AVL树。
     * 2：(3,1,2)需要先对(1,2)的'2'节点进行右单旋, 旋转为(3,2,1), 再对(3,2,1)的'3'节点进行左单旋即可满足AVL树。--> 左双旋
     */
    private AvlBinaryNode<E> doubleRotateWithLeftChild(AvlBinaryNode<E> node) {
        node.left = rotateWithRightChild(node.left);
        return rotateWithLeftChild(node);
    }

    /**
     * TODO：(1,2,3)和(1,3,2)的结构也不同、
     * 1：(1,2,3)只需要进行一次右单旋即可满足AVL树。
     * 2：(1,3,2)需要先将(3,2)的'3'节点进行左单旋, 旋转为(1,2,3), 再对(1,2,3)的'1'节点进行右单旋即可满足AVL树。--> 右双旋
     */
    private AvlBinaryNode<E> doubleRotateWithRightChild(AvlBinaryNode<E> node) {
        node.right = rotateWithLeftChild(node.right);
        return rotateWithRightChild(node);
    }

    /**
     * 左侧单旋转、
     *
     * @param avlNode 左子树、右子树高差大于1的当前节点(递归来看：缩小的root节点)
     * @return
     */
    private AvlBinaryNode<E> rotateWithLeftChild(AvlBinaryNode<E> avlNode) {
        // 左旋时, avlNode右子节点不变
        AvlBinaryNode<E> newAvlNode = avlNode.left;
        avlNode.left = newAvlNode.right;
        newAvlNode.right = avlNode;
        // 重新计算高度, 先后顺序不能更新
        avlNode.height = getMaxHeight(avlNode);
        newAvlNode.height = getMaxHeight(newAvlNode);
        return newAvlNode;
    }

    /**
     * 右侧单旋转、
     *
     * @param avlNode 左子树、右子树高差大于1的当前节点(递归来看：缩小的root节点)
     */
    private AvlBinaryNode<E> rotateWithRightChild(AvlBinaryNode<E> avlNode) {
        // 右旋时, avlNode的左子节点不变、
        AvlBinaryNode<E> newAvlNode = avlNode.right;
        avlNode.right = newAvlNode.left;
        newAvlNode.left = avlNode;
        // 重新计算高度, 先后顺序不能更新
        avlNode.height = getMaxHeight(avlNode);
        newAvlNode.height = getMaxHeight(newAvlNode);
        return newAvlNode;
    }

    /**
     * 获取当前节点的最大高度(左、右节点的最大高度 +1)、
     *
     * @param node AvlBinaryNode
     * @return height
     */
    private int getMaxHeight(AvlBinaryNode<E> node) {
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    /**
     * 获取当前节点的高度, 如果是null, 则返回 -1、
     */
    private int getHeight(AvlBinaryNode<E> node) {
        return node == null ? -1 : node.height;
    }

    private static class AvlBinaryNode<E> {
        E element;
        // left child
        AvlBinaryNode<E> left;
        // right child
        AvlBinaryNode<E> right;
        // 节点高度、
        int height = 0;

        private AvlBinaryNode(E element, AvlBinaryNode<E> left, AvlBinaryNode<E> right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        /**
         * 4,5,1,3,2 --> 进行AVL树的双旋测试、
         * 3,1,2 --> 双旋测试、
         */
        MyBalanceBinarySearchTree<Integer> searchTree = new MyBalanceBinarySearchTree<>();
        searchTree.addElement(3);
        searchTree.addElement(1);
        searchTree.addElement(2);
        searchTree.addElement(4);
        searchTree.addElement(5);
        searchTree.addElement(6);
        searchTree.addElement(7);
        boolean contains = searchTree.contains(0);
        Integer minElement = searchTree.findMin();
        Integer maxElement = searchTree.findMax();
        int treeHeight = searchTree.getTreeHeight();
        System.out.println("treeHeight = " + treeHeight);
        System.out.println("minElement = " + minElement);
        System.out.println("maxElement = " + maxElement);
        System.out.println("contains = " + contains);
        System.out.println("searchTree.size() = " + searchTree.size());

        boolean removeElement = searchTree.removeElement(4);
        System.out.println("removeElement = " + removeElement);
    }
}
