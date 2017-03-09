/**
 * Created by Oleksandr on 07.03.2017.
 */
public class app {
    public static void main(String[] args){
        int mCountThread = 10;
        int mCountFib = 40;

        Thread[] fib = new Fibonacci[mCountThread];

        for (int i = 0; i < mCountThread; i++){
            fib[i] = new Fibonacci(mCountFib, i + 1);
            fib[i].start();
        }
    }
}
