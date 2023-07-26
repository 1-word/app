package com.numo.wordapp.service;

import com.numo.wordapp.dto.FolderDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.Folder;
import com.numo.wordapp.model.Word;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WordService {
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
    @Transactional
    String updateByWord(WordDto.Request dto, String type);

    @Transactional
    Word setByWord(WordDto.Request dto);

    String removeByWord(WordDto.Request dto);

    //Word getBySearchWord(int word_id);

    List<Word> getBySearchWord(String user_id, String data);

    List<Word> getByAllWord(String user_id);

    List<Word> getByFolderWord(String user_id, int folder_id);
}
