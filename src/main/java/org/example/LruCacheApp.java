package org.example;

import java.util.HashMap;
import java.util.Map;

public class LruCacheApp {
    private static class Node {
        int key; int value;
        Node prev; Node next;
        Node(int key, int value) { this.key = key; this.value = value; }
    }

    private final int capacity;
    private final Map<Integer, Node> map = new HashMap<>();
    private final Node head = new Node(-1, -1);
    private final Node tail = new Node(-1, -1);

    public LruCacheApp(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addToHead(Node node) {
        node.next = head.next;
        node.next.prev = node;
        head.next = node;
        node.prev = head;
    }

    public int get(int key) {
        Node node = map.get(key);
        if (node == null) return -1;
        removeNode(node);
        addToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        Node node = map.get(key);
        if (node != null) {
            node.value = value;
            removeNode(node);
            addToHead(node);
        } else {
            if (map.size() >= capacity) {
                Node lruNode = tail.prev;
                removeNode(lruNode);
                map.remove(lruNode.key);
                System.out.println("[Eviction] Removed key: " + lruNode.key);
            }
            Node newNode = new Node(key, value);
            map.put(key, newNode);
            addToHead(newNode);
        }
    }

    public void printCacheState() {
        Node current = head.next;
        System.out.print("Cache Order (Most -> Least Recent): ");
        while (current != tail) {
            System.out.print("[" + current.key + ": " + current.value + "] ");
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== Project 2: LRU Cache ===");
        LruCacheApp cache = new LruCacheApp(3);
        cache.put(1, 100);
        cache.put(2, 200);
        cache.put(3, 300);
        cache.printCacheState();
        cache.get(1);
        cache.put(4, 400);
        cache.printCacheState();
    }
}