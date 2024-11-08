package com.numo.wordapp.repository.word;

import com.numo.wordapp.dto.word.WordDto;
import com.numo.wordapp.entity.word.GttsCode;
import com.numo.wordapp.entity.word.QWord;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.entity.word.detail.QWordDetailMain;
import com.numo.wordapp.entity.word.detail.QWordDetailSub;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@RequiredArgsConstructor
public class WordRepositoryImpl implements WordRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QWord qWord = QWord.word1;
    QWordDetailMain qWordDetailMain = QWordDetailMain.wordDetailMain;
    QWordDetailSub qWordDetailSub = QWordDetailSub.wordDetailSub;

    /**
     * 해당하는 유저의 단어 데이터 조회, 폴더 아이디와 마지막 단어 아이디가 없는지 확인 후 동적으로 쿼리 생성
     * @param pageable 페이징 값, default 20
     * @param readDto <br>
     * * user_id 로그인 유저(토큰) 아이디 <br>
     * * folder_id 폴더 아이디 <br>
     * * last_word_id 마지막으로 출력된 단어 아이디 <br>
     * @return 페이징한 유저 데이터
     */
    @Override
    public Slice<Word> findByPageWord(Pageable pageable, WordDto.Read readDto) {
        List<Word> results = queryFactory.selectDistinct(qWord)
                .from(qWord)
                .leftJoin(qWord.folder)
                .leftJoin(qWord.wordDetailMains)
                .where(
                        qWord.userId.eq(readDto.getUser_id()),
                        eqFolderId(readDto.getFolder_id()),
                        eqMemorization(readDto.getMemorization()),
                        eqLanguage(readDto.getLanguage()),
                        ltUpdateTime(readDto.getLast_word_id()),
                        likeSearchText(readDto.getSearch_text())
                )
                .orderBy(qWord.update_time.desc())
                .orderBy(qWord.wordId.desc())
                .limit(pageable.getPageSize()+1)
                .fetch();
        return checkLastPage(pageable, results);
    }

    /**
     * 검색 문자열이 있으면 검색하는 쿼리를 작성한다.
     * @param searchText 검색 문자열
     * @return 단어, 상세 내용 검색하는 쿼리 리턴
     */
    private BooleanExpression likeSearchText(String searchText){
        String likeText = "%" + searchText + "%";
        if (searchText == null) return null;
        return likeWordSearchText(likeText).or(likeDetailSearchText(likeText));
    }

    /**
     * 단어 상세 내용에 검색 문자열이 있는 wordId를 조회 후 해당 wordId만 조회하는 쿼리를 작성한다.
     * @param searchText 검색 문자열
     * @return 단어 상세 내용에 검색 문자열이 있는 wordId를 조회하는 쿼리 리턴
     */
    private BooleanExpression likeDetailSearchText(String searchText){
        return qWord.wordId.in(findByDetailWord(searchText));
    }

    /**
     * 단어에 검색 문자열이 있는지 확인하는 쿼리를 작성한다.
     * @param searchText 검색 문자열
     * @return 단어 검색 쿼리 리턴
     */
    private BooleanExpression likeWordSearchText(String searchText){
        return qWord.word.like(searchText)
                .or(qWord.mean.like(searchText))
                .or(qWord.read.like(searchText))
//                .or(qWord.memo.like(searchText))
                ;
    }

    /**
     * 단어 상세 내용에 해당하는 텍스트가 있는지 확인 후 해당하는 wordId를 리턴한다.
     * @param searchText 검색할 내용
     * @return wordId
     */
    private List<Integer> findByDetailWord(String searchText){
        return queryFactory.selectDistinct(qWordDetailMain.word.wordId)
                .from(qWordDetailMain)
                .leftJoin(qWordDetailMain.wordDetailSub, qWordDetailSub)
                .where(
                        qWordDetailMain.content.like(searchText)
//                                .or(qWordDetailMain.memo.like(searchText))
                                .or(qWordDetailSub.content.like(searchText))
//                                .or(qWordDetailSub.memo.like(searchText))
                )
                .fetch();
    }

    /**
     * 다음 페이지가 있는지 확인하여 페이지 데이터 추가 후 리턴한다.
     * @param pageable 페이지 변수
     * @param results DB에서 return한 데이터
     * @return page 데이터를 포함한 데이터
     */
    private Slice<Word> checkLastPage(Pageable pageable, List<Word> results){
        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()){
            hasNext = true;
            results.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 폴더 아이디 확인 후 해당하는 쿼리 작성
     * @param folder_id 폴더 아이디, 폴더가 아닌 전체 데이터를 확인할 경우 -1
     * @return 폴더 아이디가 있는 경우에만 폴더 검색하는 쿼리 리턴
     */
    private BooleanExpression eqFolderId(int folder_id){
        if (folder_id == -1) return null;
        return qWord.folder.folderId.eq(folder_id);
    }

    /**
     * 업데이트 시간 내림차순으로 페이징 처리를 위해 마지막으로 조회한 단어의 아이디의 업데이트 시간을 가져와 해당 데이터보다 이전에 저장된 데이터를 가져온다.(No Offset)
     * @param last_word_id 마지막으로 조회한 단어
     * @return 처음 조회 시 null, 업데이트 시간 비교하는 쿼리 리턴
     */
    private BooleanExpression ltUpdateTime(int last_word_id){
        if (last_word_id == -1) return null;
        return qWord.update_time.lt(
                queryFactory.select(qWord.update_time)
                        .from(qWord)
                        .where(qWord.wordId.eq(last_word_id)
                        )
        );
    }

    /**
     * 암기 여부를 확인하는 쿼리 작성
     * @param status Y | N
     * @return 암기 여부를 비교하는 쿼리 리턴
     */
    private BooleanExpression eqMemorization(String status){
        if (status == null || status == "") return null;
        return qWord.memorization.eq(status);
    }

    private BooleanExpression eqLanguage(String lang){
        if (lang == null || lang == "") return null;
        return qWord.lang.eq(GttsCode.valueOf(lang));
    }
}
