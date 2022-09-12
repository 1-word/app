package com.numo.wordapp.test;

import com.numo.wordapp.dto.SynonymDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.util.ModelMapperUtil;

import java.util.List;

public class MappingTest {
    public static void main(String[] args) {
        WordDto.Request wrdto = new WordDto.Request();
        SynonymDto.Request sydto = new SynonymDto.Request();
        sydto.setSynonym_id(1);
        sydto.setSynonym("synonym");
        sydto.setMemo("memo");

        wrdto.setWord_id(1);
        wrdto.setWord("word");
        wrdto.setMean("mean");
        wrdto.setWread("wread");
        wrdto.setMemo("memo");
        wrdto.setSynonyms(List.of(sydto));

        Word convertwObject = new ModelMapperUtil().of(wrdto, Word.class);
        Synonym sObject = new ModelMapperUtil().of(sydto, Synonym.class);

        System.out.println(convertwObject);
        System.out.println(wrdto);
    }
}
