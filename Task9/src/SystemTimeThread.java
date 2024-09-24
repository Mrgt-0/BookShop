public class SystemTimeThread extends Thread{
    private int interval;

    public SystemTimeThread(int interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Current time: " + System.currentTimeMillis());
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void runTask4(int interval) {
        SystemTimeThread thread = new SystemTimeThread(5);
        thread.start();
    }
}
