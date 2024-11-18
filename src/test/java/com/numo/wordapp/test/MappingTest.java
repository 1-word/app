//package com.numo.wordapp.test;
//
//import com.numo.wordapp.dto.word.WordDto;
//import com.numo.wordapp.model.word.Word;
//
//import java.util.List;
//
//public class MappingTest {
//    public enum updateType{
//        ALL("all", 0){
//            @Override
//            public String updateByWordType(Word word){
//                return "a";
//            }
//        },
//        MEMO("memo", 1){
//            @Override
//            public String updateByWordType(Word word){
//                return "b";
//            }
//        };
//
//        String typeName;
//        int type;
//
//        updateType(String typeName, int type) {
//            this.typeName = typeName;
//            this.type = type;
//        }
//
//        public abstract String updateByWordType(Word word);
//    }
//
//    public static void main(String[] args) {
//        WordRequestDto wrdto = new WordRequestDto();
//        SynonymDto.Request sydto = new SynonymDto.Request();
//        sydto.setSynonym_id(1);
//        sydto.setSynonym("synonym");
//        sydto.setMemo("memo");
//
//        wrdto.setWord_id(1);
//        wrdto.setWord("word");
//        wrdto.setMean("mean");
//        wrdto.setRead("read");
//        wrdto.setMemo("memo");
//        wrdto.setSynonyms(List.of(sydto));
//
//        //System.out.println( WordDto.updateType.ALL.name());
//        //System.out.println(WordDto.updateType.ALL.ordinal());
//        //Word convertwObject = new ModelMapperUtil().of(wrdto, Word.class);
//        //Synonym sObject = new ModelMapperUtil().of(sydto, Synonym.class);
//
//        //System.out.println(convertwObject);
//        //System.out.println(wrdto);
//    }
//}
