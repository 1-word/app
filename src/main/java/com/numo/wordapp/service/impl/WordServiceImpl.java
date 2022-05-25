package com.numo.wordapp.service.impl;

import com.numo.wordapp.dto.SynonymDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.repository.SynonymRepository;
import com.numo.wordapp.repository.WordRepository;

import com.numo.wordapp.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/*
 클래스: WordService
 내용: Word, Synonym 데이터 CRUD 관리
 작성자: 정현경
 작성일: 2022.05.25
 수정이력: 2022.05.25 WordService 작성_정현경
*/

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private SynonymRepository synonymRepository;

    @Override
    @Transactional
    public String updateByWord(WordDto.Request dto){
        Optional<Word> wordUpdate = wordRepository.findById(dto.getWord_id());  //db에서 조회를 하면 영속성 유지..

        wordUpdate.ifPresent(selectWord->{
            selectWord.setWord(dto.getWord());
            selectWord.setMean(dto.getMean());
            selectWord.setWread(dto.getWread());
            selectWord.setMemo(dto.getMemo());

            wordRepository.save(selectWord);
        });
        return "완료";
    }

    @Override
    @Transactional
    public String setByWord(WordDto.Request dto){
        Word word = dto.toEntity();
        List<SynonymDto.Request> synonyms = dto.getSynonyms();
        for (SynonymDto.Request synonym : synonyms) {
            Synonym s = synonym.toEntity(word);
            word.addSynonym(synonym.toEntity(word));
        }
        wordRepository.save(word);
        return "데이터를 저장완료하였습니다.";
    }

    @Override
    public String removeByWord(int id){
        Optional<Word> word = wordRepository.findById(id);

        word.ifPresent(removeWord->{
            wordRepository.delete(removeWord);
        });

        return "데이터 삭제를 완료하였습니다.";
    }

    /*

    메소드: getBySearchWord(int word_id)
    파라미터: word 기본 키 값
    리턴 값: Word
    기능: 검색한 단어(Word) 및 유의어(Synonym) 조회
    주의사항: 양방향 참조이기때문에 DTO를 이용해서 리턴해야함 (무한 참조 발생 가능)
    작성자: 정현경
    작성일: 2022.05.25
    */

    @Override
    public Word getBySearchWord(int word_id){
        return wordRepository.findById(word_id).get();
    }

    /*
    메소드: getByAllWord()
    리턴 값: List<Word>
    기능: 모든 단어(Word) 및 유의어(Synonym) 조회
    주의사항: 양방향 참조이기때문에 DTO를 이용해서 리턴해야함 (무한 참조 발생 가능)
    작성자: 정현경
    작성일: 2022.05.25
    */

    @Override
    public  List<Word> getByAllWord(){
        return   wordRepository.getByAllWord();
    }

}
