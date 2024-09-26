package task3;

import java.util.LinkedList;

public class Buffer {
    private final int capacity;
    private final LinkedList<Integer> queue;

    public Buffer(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public synchronized void produce(int data) {
        while (queue.size() == capacity) {
            try {
                System.out.println("Buffer is full. Producer is waiting...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        queue.add(data);
        System.out.println("Producer produced: " + data);
        notifyAll();
    }

    public synchronized int consume() {
        while (queue.isEmpty()) {
            try {
                System.out.println("Buffer is empty. Consumer is waiting...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int data = queue.removeFirst();
        System.out.println("Consumer consumed: " + data);
        notifyAll();
        return data;
    }
}
