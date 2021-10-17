package downLoaderFiles;

//import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


//@Component
@Scope("prototype")
public class Downloader {

    private final int bufferSize = 1024;
    public Downloader(){
    }

    public boolean download(URL url, String nameFile){
        System.out.println(this);
        try (BufferedInputStream inputStream = new BufferedInputStream(
                url.openStream());
             FileOutputStream fileOS = new FileOutputStream(nameFile)) {
            byte data[] = new byte[bufferSize];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, bufferSize)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            return false;
        }
        System.out.println("+");
        return true;
    }

}
