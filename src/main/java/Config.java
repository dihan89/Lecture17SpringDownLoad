import downLoaderFiles.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
//@ComponentScan("downLoaderFiles")
//@Component
public class Config {

    private final String str = "urls.txt";

    @Bean(name = "urls")
    public String getUrls(){
        return str;
    }
    @Bean
    @Autowired
    public PrepareToDowload getPrepareDownload(String urls){
        return new PrepareToDowload(urls);
    }
    @Bean
    @Scope("prototype")
    public Downloader getDownLoader(){
        return new Downloader();

    }

}
