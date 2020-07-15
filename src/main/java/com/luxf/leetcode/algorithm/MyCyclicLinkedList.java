package com.luxf.leetcode.algorithm;

import java.util.LinkedList;

/**
 * JDK1.8版本中, {@link LinkedList}是双向链表、
 * 单向链表：只有next节点、没有存储prev节点！
 * <p>
 * TODO: 自定义实现双向循环链表
 *
 * @author 小66
 * @date 2020-07-10 18:56
 *
 **/
public class MyCyclicLinkedList<E> {

    private Node<E> first;
    private Node<E> last;
    private int size = 0;

    public MyCyclicLinkedList() {

    }

    /**
     * 双向循环链表 -> 添加至last节点、
     *
     * @param e
     * @return
     */
    public boolean addLast(E e) {
        // 第一次add时, 初始化first、last都是第一个元素(自己)
        final Node<E> f = first;
        final Node<E> l = last;
        // 不同于单向链表, 此处需要将next节点设置为 first、
        Node<E> newNode = new Node<>(last, e, first);
        last = newNode;
        if (f == null) {
            first = newNode;
        } else {
            // 原始的first的prev节点更新为当前新增的Node、
            f.prev = newNode;
        }
        // 原始的last的next节点更改为当前新增的Node、
        if (l != null) {
            l.next = newNode;
        }
        size++;
        return true;
    }

    /**
     * 双向循环链表 -> 获取并移除first节点
     *
     * @return
     */
    public E pollFirst() {
        if (first == null) {
            return null;
        }
        final Node<E> f = first;
        final E e = f.item;
        first = f.next;
        // 指定first.prev节点和last.next节点
        first.prev = last;
        last.next = first;
        // 让GC工作、
        f.next = null;
        f.prev = null;
        f.item = null;
        size--;
        return e;
    }

    public E getFirst() {
        return first == null ? null : first.item;
    }

    /**
     * 通过Node节点, 维护链表
     *
     * @param <E>
     */
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    public static void main(String[] args) {
        MyCyclicLinkedList<String> cyclicLinkedList = new MyCyclicLinkedList<>();
        cyclicLinkedList.addLast("A");
        cyclicLinkedList.addLast("B");
        cyclicLinkedList.addLast("C");
        cyclicLinkedList.addLast("D");
        String first = cyclicLinkedList.getFirst();
        System.out.println("cyclicLinkedList.size = " + cyclicLinkedList.size);
        String pollFirst = cyclicLinkedList.pollFirst();
        System.out.println("pollFirst = " + pollFirst);
        System.out.println("cyclicLinkedList.size = " + cyclicLinkedList.size);

    }

    private static void linkedListTest() {
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("A");
        linkedList.add("B");

        String removeLast = linkedList.removeLast();
        System.out.println("removeLast = " + removeLast);
    }
}
