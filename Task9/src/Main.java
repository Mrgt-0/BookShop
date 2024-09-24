
public class Main {
    public static void main(String[] args) {
        ThreadStates.runTask1();

        TwoThreads.runTask2();

        ProducerConsumer.runTask3();

        SystemTimeThread.runTask4(5);
    }
}