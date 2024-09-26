package task3;

import java.util.Random;

public class Producer implements Runnable{
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            int data = new Random().nextInt(100);
            buffer.produce(data);
        }
    }
}
