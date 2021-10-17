import downLoaderFiles.*;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DownloaderTest {
    private final static String iFile = "urls.txt";
    //private final static String oFile = "/files";

    class Task implements Runnable {

        private final PrepareToDowload prepareToDowload;
        private final Downloader downloader;
        Task(PrepareToDowload prepareToDowload){
            this.prepareToDowload = prepareToDowload;
            downloader = new Downloader();
        }
        @Override
        public void run() {
            URL currentURL = getURL();
            while (currentURL != null){
                System.out.println(currentURL);
                downloader.download(currentURL, Paths.get(currentURL.getPath()).getFileName().toString());
                currentURL = getURL();

            }
           // Thread.currentThread().interrupt();
        }
        private URL getURL(){
            synchronized (prepareToDowload){
               return prepareToDowload.getNextURL();
            }
        }

    }

    @Test
    public void test () throws MalformedURLException {
        Downloader dwloader = new Downloader();
        dwloader.download(new URL("https://traveltimes.ru/wp-content/uploads/2021/08/bolsh.jpg"), "1.jpg");

    }

    @Test
    public void test2 () throws MalformedURLException, InterruptedException {
        PrepareToDowload pr = new PrepareToDowload(iFile);
        Task task1 = new Task(pr);
        Task task2 = new Task(pr);
        Task task3 = new Task(pr);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(task1);
        executorService.execute(task2);
        executorService.execute(task3);
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }


    @Test
    public void testSpring () throws  InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        //PrepareToDowload pr =(PrepareToDowload) context.getBean("getPrepareDownload");
        PrepareToDowload pr =  context.getBean(PrepareToDowload.class);
        Task task1 = new Task(pr);
        Task task2 = new Task(pr);
        Task task3 = new Task(pr);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(task1);
        executorService.execute(task2);
        executorService.execute(task3);
        executorService.awaitTermination(30, TimeUnit.SECONDS);

    }


    @Test
    public void testSpring2 () throws  InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        class TaskImpl implements Runnable {
            private final PrepareToDowload prepareToDowload;
            private final Downloader downloader;
            TaskImpl(PrepareToDowload prepareToDowload){
                this.prepareToDowload = prepareToDowload;
                downloader = context.getBean(Downloader.class);
            }
            @Override
            public void run() {
                URL currentURL = getURL();
                while (currentURL != null){
                    System.out.println(currentURL);
                    downloader.download(currentURL, Paths.get(currentURL.getPath()).getFileName().toString());
                    currentURL = getURL();
                }
             //   Thread.currentThread().interrupt();
            }
            private URL getURL(){
                synchronized (prepareToDowload){
                    return prepareToDowload.getNextURL();
                }
            }

        }



        PrepareToDowload pr =  context.getBean(PrepareToDowload.class);
        Runnable task1 = new TaskImpl(pr);
        Runnable task2 = new TaskImpl(pr);
        Runnable task3 = new TaskImpl(pr);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(task1);
        executorService.execute(task2);
        executorService.execute(task3);
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);






    }


}



