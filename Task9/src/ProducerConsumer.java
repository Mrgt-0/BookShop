import java.util.LinkedList;
import java.util.Random;

public class ProducerConsumer {
    private static final int BUFFER_SIZE = 10;
    private static LinkedList<Integer> buffer = new LinkedList<>();

    public static void runTask3() {
        Thread producer = new Thread(() -> {
            while (true) {
                synchronized (buffer) {
                    while (buffer.size() == BUFFER_SIZE) {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int randomNumber = new Random().nextInt(100);
                    buffer.add(randomNumber);
                    System.out.println("Producer produced: " + randomNumber);
                    buffer.notifyAll();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                synchronized (buffer) {
                    while (buffer.isEmpty()) {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int number = buffer.removeFirst();
                    System.out.println("Consumer consumed: " + number);
                    buffer.notifyAll();
                }
            }
        });

        producer.start();
        consumer.start();
    }
}
