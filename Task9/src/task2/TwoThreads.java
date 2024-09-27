package task2;

public class TwoThreads{
        private static final Object lock = new Object();
        private static int threadNumber = 1;

        public static void runTask2() {
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    synchronized (lock) {
                        System.out.println("Thread " + threadNumber);
                        threadNumber = (threadNumber == 1) ? 2 : 1;
                        lock.notifyAll();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    synchronized (lock) {
                        System.out.println("Thread " + threadNumber);
                        threadNumber = (threadNumber == 1) ? 2 : 1;
                        lock.notifyAll();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            thread1.start();
            thread2.start();
    }
}
