/**
 * Created by Oleksandr on 07.03.2017.
 */
public class Fibonacci extends Thread{
    int mCount;
    int mNumb;
    StringBuilder mBuilder;

    public Fibonacci(int count, int numb){
        mCount = count;
        mNumb = numb;
        mBuilder = new StringBuilder();
    }

    public void run(){
        long timer = -System.currentTimeMillis();
        for (int i = 1; i < mCount + 1; i++){

            mBuilder.append(fib(i)).append("  ");
        }
        timer += System.currentTimeMillis();
        System.out.println("Time: " + timer / 1000.0 + "  seconds");
        System.out.println("Thread[" + mNumb + "]:  " + mBuilder);
    }

    public long  fib(int i) {
        if (i == 1) return 1;
        if (i == 2) return 1;
        return fib(i - 1) + fib(i - 2);
    }

}

