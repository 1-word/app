package com.numo.wordapp.test;

import com.numo.wordapp.dto.SynonymDto;
import com.numo.wordapp.dto.WordDto;
import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import com.numo.wordapp.util.ModelMapperUtil;

import java.util.List;

public class MappingTest {
    public enum updateType{
        ALL("all", 0){
            @Override
            public String updateByWordType(Word word){
                return "a";
            }
        },
        MEMO("memo", 1){
            @Override
            public String updateByWordType(Word word){
                return "b";
            }
        };

        String typeName;
        int type;

        updateType(String typeName, int type) {
            this.typeName = typeName;
            this.type = type;
        }

        public abstract String updateByWordType(Word word);
    }

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

        //System.out.println( WordDto.updateType.ALL.name());
        //System.out.println(WordDto.updateType.ALL.ordinal());
        //Word convertwObject = new ModelMapperUtil().of(wrdto, Word.class);
        //Synonym sObject = new ModelMapperUtil().of(sydto, Synonym.class);

        //System.out.println(convertwObject);
        //System.out.println(wrdto);
    }
}
