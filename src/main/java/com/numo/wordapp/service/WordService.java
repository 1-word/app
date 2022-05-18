package com.numo.wordapp.service;

import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.repository.SynonymRepository;
import com.numo.wordapp.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class WordService {
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private SynonymRepository synonymRepository;

    public String setByWord(String _word, String _mean, String _wread, String _memo){
        System.out.println(_word+ _mean+ _wread+ _memo);
        wordRepository.save(Word.builder()
                .word(_word)
                .mean(_mean)
                .wread(_wread)
                .memo(_memo)
                .build());
        return "데이터를 저장완료하였습니다.";
    }

   /*public Map<String, Object> getByAllWord(){
        Map<String, Object> map = wordRepository.getByAllword().get(0);
        System.out.println(map);
        return map;
    }*/

    public List<Word> getByAllWord(){
        List<Word> data = wordRepository.getByAllword();
        System.out.println(data);
        getByAllSynonym();  //유의어 출력
        return data;
    }

    public Word getBySearchWord(int word_id){
        return wordRepository.findById(word_id).get();
    }

    public List<Synonym> getByAllSynonym(){
        List<Synonym> data = synonymRepository.getByAllSynonym();
        System.out.println(data);
        return data;
    }
}
