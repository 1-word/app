package com.numo.wordapp.conf;

import com.numo.wordapp.repository.WordRepository;
import com.numo.wordapp.service.WordService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    //private DataSoure dataSoure;
    // private EntityManager em;
    /*private final WordRepository wordRepository;

    public Config(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Bean
    public WordService wordService(){
        return new WordService(wordRepository);
    }*/

}
