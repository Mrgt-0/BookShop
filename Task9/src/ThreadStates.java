import java.util.InputMismatchException;

import static java.lang.Thread.currentThread;

public class ThreadStates{
    public static void runTask1(){
        Thread thread=new Thread(()->{
            System.out.println("Threat state: " + currentThread().getState());
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("Thread state: " + currentThread().getState()); // RUNNABLE
            try {
                synchronized (ThreadStates.class) {
                    ThreadStates.class.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread state: " + currentThread().getState()); // BLOCKED
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread state: " + currentThread().getState()); // WAITING
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread state: " + currentThread().getState()); // TIMED_WAITING
            System.out.println("Thread state: " + currentThread().getState()); // TERMINATED
        });

        thread.start();
    }
}
