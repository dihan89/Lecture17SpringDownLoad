package downLoaderFiles;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.*;


//@Component
public class PrepareToDowload {
    private final Queue<URL> urls = new LinkedList<>();

    public PrepareToDowload(String iFile) {
        File file = new File(iFile);
        if (file.exists() && file.isFile()) {
            String urlStr;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while ((urlStr = reader.readLine()) != null){
                    URL url ;
                    try {
                        url = new URL(urlStr);
                        url.toURI();
                        //String fileName = Paths.get(urlStr).getFileName().toString();
                        urls.add(url);
                        System.out.println(url);
                    }catch (Exception exc){
                        exc.printStackTrace();
                        System.out.println("ERROR" + urlStr);
                    }
                }

            } catch (IOException exc) {
                exc.printStackTrace();
            }
        } else
            System.out.println("Input file is not exist!");
    }
    public URL getNextURL(){
        return urls.isEmpty()? null : urls.poll();
    }
}
