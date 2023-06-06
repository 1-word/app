package com.numo.wordapp.service.impl;

import com.numo.wordapp.comm.advice.exception.UserNotFoundCException;
import com.numo.wordapp.dto.SynonymDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.comm.advice.exception.ErrorCode;
import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.repository.SynonymRepository;
import com.numo.wordapp.repository.WordRepository;

import com.numo.wordapp.service.WordService;
import com.numo.wordapp.util.ProcessBuilderUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 클래스: WordServiceImpl
 내용: Word, Synonym 데이터 CRUD 관리
 작성자: 정현경
 작성일: 2022.05.25
 수정이력: 2022.05.25 WordServiceImpl 작성_정현경
          2022.06.22 CURD 작성_정현경
          2022.09.11 update 로직 수정/SynonymService삭제 후 해당 기능 update로 이전_정현경
*/

@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final SynonymRepository synonymRepository;

    public WordServiceImpl(WordRepository wordRepository,
                           SynonymRepository synonymRepository){
        this.wordRepository = wordRepository;
        this.synonymRepository = synonymRepository;
    }

    /*
    * 메소드: updateByWord(WordDto.Request dto)
    * 파라미터: 요청받은 dto값
    * 기능: 데이터 수정(유의어 수정은 synonymServiceImpl에서)
    * 유의사항: word의 칼럼이 추가되면 update시에 word.set{colName}() 추가 필요
    * 작성자: 정현경
    * 작성일: 2022.06.22
    * 수정일: 2023.06.04
    * 수정내용: 2023.06.04 - 업데이트할 유의어 갯수가 더 많으면 에러났던 이슈 수정
    * */
    @Override
    @Transactional
    public String updateByWord(WordDto.Request dto){
        // 1. 해당 단어 유효한지 검색
        Word word = wordRepository.findById(dto.getWord_id()).orElseThrow(() -> new UserNotFoundCException(ErrorCode.UserNotFound.getDescription()));  //db에서 조회를 하면 영속성 유지..

        // 2 단어 업데이트
        //단어 테이블 컬럼 추가 시 아래 코드 작성 필요
        word.setWord(dto.getWord());
        word.setMean(dto.getMean());
        word.setWread(dto.getWread());
        word.setMemo(dto.getMemo());
        wordRepository.save(word);

        // 3. 유의어 업데이트
        List<Synonym> existingSynonyms = word.getSynonyms();
        List<SynonymDto.Request> inputSynonyms = dto.getSynonyms();

        // 현재 유의어 갯수 확인
        for (int i=0; i < existingSynonyms.size(); i++){
            Synonym existingSynonym = existingSynonyms.get(i);
            if (i < inputSynonyms.size()){
                SynonymDto.Request inputSynonym = inputSynonyms.get(i);
                existingSynonym.setSynonym(inputSynonym.getSynonym());
                existingSynonym.setMemo(inputSynonym.getMemo());
                synonymRepository.save(existingSynonym);
            }else{  // 현재 유의어 갯수보다 적으면 delete 실행
                synonymRepository.deleteSynonym(existingSynonym.getSynonym_id());
            }
        }

        // 현재 유의어 갯수보다 update할 유의어 갯수가 더 많으면 insert 실행
        for (int i = existingSynonyms.size(); i < inputSynonyms.size(); i++) {
            SynonymDto.Request inputSynonym = inputSynonyms.get(i);
            Synonym newSynonym = Synonym.builder()
                            .synonym(inputSynonym.getSynonym())
                            .memo(inputSynonym.getMemo())
                            .word(word)
                            .build();
            synonymRepository.save(newSynonym);
        }

        return "저장완료";
    }

    /*
    * 메소드: setByWord(WordDto.Request dto)
    * 파라미터: 요청받은 dto값
    * 리턴 값: String
    * 기능: 데이터 저장
    * 작성자: 정현경
    * 작성일: 2022.06.22
    * */

    @Override
    @Transactional
    public Word setByWord(WordDto.Request dto){
        Word word = dto.toEntity();
        List<SynonymDto.Request> synonyms = dto.getSynonyms();
        for (SynonymDto.Request synonym : synonyms) {
            word.addSynonym(synonym.toEntity(word));
        }
        String fileName = word.getWord() + "_" + System.currentTimeMillis();
        word.setSoundPath(fileName);
        return wordRepository.save(word);
    }

    /*
    * 메소드: removeByWord(int id)
    * 파라미터: word 기본 키 값
    * 리턴 값: String
    * 기능: 해당하는 키 값 데이터 삭제
    * 작성자: 정현경
    * 작성일: 2022.06.22
    * */

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

    /*@Override
    public Word getBySearchWord(int word_id){
        //wordRepository.findByWord_idContainingOrWordContainingOrMeanContainingOrWreadContainingOrMemoContaining(word_id, word, mean, wread, memo);
        //return wordRepository.findById(word_id).get();
    }*/

    @Override
    public List<Word> getBySearchWord(String user_id, String data){
        //wordRepository.findByWord_idContainingOrWordContainingOrMeanContainingOrWreadContainingOrMemoContaining(word_id, word, mean, wread, memo);
        //return wordRepository.findById(word_id).get();
        return wordRepository.getBySearchWord(user_id, data);
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
    public List<Word> getByAllWord(String user_id){
        //return wordRepository.getByAllWord(Sort.by(Sort.Direction.DESC, "word.word_id", "synonym_id"));
        return wordRepository.getByAllWord(user_id);
    }

}
