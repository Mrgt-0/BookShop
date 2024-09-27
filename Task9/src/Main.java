import task1.ThreadStates;
import task2.TwoThreads;
import task3.ProducerConsumerApp;
import task4.SystemTimeThread;

public class Main {
    public static void main(String[] args) {
        ThreadStates.runTask1();

        TwoThreads.runTask2();

        ProducerConsumerApp.runTask3();

        SystemTimeThread.runTask4(5);
    }
}