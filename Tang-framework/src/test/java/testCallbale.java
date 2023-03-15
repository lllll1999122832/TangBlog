import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class testCallbale {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable callable=()->{
            System.out.println("hello");
            return "tang";
        };
        FutureTask<String>futureTask=new FutureTask<>(callable);
        new Thread(futureTask).start();
        String s = futureTask.get();
        System.out.println(s);
    }
}
