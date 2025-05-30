package com.numo.api.domain.wordbook.word.repository.query;

import com.numo.api.domain.dailySentence.dto.DailyWordDto;
import com.numo.api.domain.dailySentence.dto.DailyWordListDto;
import com.numo.api.domain.dailySentence.dto.wordDailySentence.DailyWordDetailDto;
import com.numo.api.domain.wordbook.detail.dto.WordDetailResponseDto;
import com.numo.api.domain.wordbook.word.dto.WordDto;
import com.numo.api.domain.wordbook.word.dto.read.ReadWordRequestDto;
import com.numo.api.global.comm.page.SliceUtil;
import com.numo.domain.wordbook.detail.QWordDetail;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.domain.wordbook.type.SortType;
import com.numo.domain.wordbook.word.QWord;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class WordCustomRepositoryImpl implements WordCustomRepository {
    private final JPAQueryFactory queryFactory;

    private final QWord qWord = QWord.word1;
    private final QWordDetail qWordDetail = QWordDetail.wordDetail;

    /**
     * 해당하는 유저의 단어 데이터 조회, 폴더 아이디와 마지막 단어 아이디가 없는지 확인 후 동적으로 쿼리 생성
     *
     * @param pageable 페이징 값, default 20
     * @param readDto  <br>
     *                 * user_id 로그인 유저(토큰) 아이디 <br>
     *                 * folder_id 폴더 아이디 <br>
     *                 * last_word_id 마지막으로 출력된 단어 아이디 <br>
     * @return 페이징한 유저 데이터
     */
    @Override
    public Slice<WordDto> findWordBy(Long wordBookId, Pageable pageable, Long lastWordId, ReadWordRequestDto readDto) {
        List<WordDto> results = queryFactory.select(Projections.constructor(
                        WordDto.class,
                        qWord.wordId,
                        qWord.wordBook.id,
                        qWord.word,
                        qWord.mean,
                        qWord.read,
                        qWord.memo,
                        qWord.sound.word,
                        qWord.memorization,
                        qWord.lang,
                        qWord.updateTime,
                        qWord.createTime
                ))
                .from(qWord)
                .leftJoin(qWord.wordBook)
                .leftJoin(qWord.sound)
                .leftJoin(qWord.user)
                .where(
                        eqWordBookId(wordBookId),
                        eqMemorization(readDto.memorization()),
                        eqLanguage(readDto.lang()),
                        createPageConditionWithReadType(readDto.sort(), lastWordId),
                        likeSearchText(readDto.searchText())
                )
                .orderBy(wordSort(readDto.sort(), readDto.seed()).toArray(OrderSpecifier[]::new))
                // 다음 페이지가 있는지 확인하기 위해 다음 데이터도 함께 조회
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return SliceUtil.of(results, pageable);
    }

    /**
     * wordId에 해당하는 단어 데이터를 찾는다
     *
     * @param wordId 단어 고유 번호
     * @return wordId에 해당하는 단어 데이터
     */
    @Override
    public WordDto findWordByWordId(Long wordId) {
        return queryFactory.select(Projections.constructor(
                        WordDto.class,
                        qWord.wordId,
                        qWord.wordBook.id,
                        qWord.word,
                        qWord.mean,
                        qWord.read,
                        qWord.memo,
                        qWord.sound.word,
                        qWord.memorization,
                        qWord.lang,
                        qWord.updateTime,
                        qWord.createTime
                ))
                .from(qWord)
                .leftJoin(qWord.wordBook)
                .leftJoin(qWord.sound)
                .where(
                        qWord.wordId.eq(wordId)
                ).fetchOne();
    }

    @Override
    public List<WordDetailResponseDto> findWordDetailByWordIds(List<Long> wordIds) {
        List<WordDetailResponseDto> results = queryFactory.select(Projections.constructor(
                        WordDetailResponseDto.class,
                        qWordDetail.word.wordId,
                        qWordDetail.wordDetailId,
                        qWordDetail.wordGroup.wordGroupId,
                        qWordDetail.wordGroup.name,
                        qWordDetail.title,
                        qWordDetail.content,
                        qWordDetail.createTime,
                        qWordDetail.updateTime
                ))
                .from(qWordDetail)
                .leftJoin(qWordDetail.wordGroup)
                .where(
                        qWordDetail.word.wordId.in(wordIds)
                )
                .fetch();
        return results;
    }

    /**
     * word 리스트에 해당하는 단어를 모두 찾는다.
     *
     * @param userId 유저 아이디
     * @param words  찾을 단어 리스트
     * @return 단어와 단어장 데이터 리스트(detail 포함x)
     */
    @Override
    public DailyWordListDto findDailyWordBy(Long userId, List<String> words) {
        // word에서 가져오기
        List<DailyWordDto> wordDtos = queryFactory.select(Projections.constructor(
                        DailyWordDto.class,
                        qWord.wordId,
                        qWord.word
                ))
                .from(qWord)
                .where(
                        qWord.user.userId.eq(userId),
                        qWord.word.in(words)
                )
                .fetch();

        // detail에서 가져오기
        List<DailyWordDetailDto> detailDtos = findByDetailWord(words);
        return new DailyWordListDto(wordDtos, detailDtos);
    }

    /**
     * word 조회 sort 생성
     *
     * @param type 조회 타입
     * @return 조회 타입에 따른 order by 쿼리
     */
    private List<OrderSpecifier<?>> wordSort(SortType type, String seed) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        if (type == SortType.updated) {
            orderSpecifierList.add(new OrderSpecifier<>(Order.DESC, qWord.updateTime));
        } else if (type == SortType.created) {
            orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, qWord.wordId));
        } else if (type == SortType.random) {
            int value = seed == null ? "".hashCode() : seed.hashCode();
            orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, Expressions.numberTemplate(Double.class, "function('rand', {0})", value)));
        }

        return orderSpecifierList;
    }

    /**
     * word 조회 시 noOffSet을 위한 query 추가
     *
     * @param type       조회 타입
     * @param lastWordId 마지막으로 조회한 단어 아이디
     * @return 조회 타입에 따른 마지막으로 조회한 아이디의 쿼리
     */
    private BooleanExpression createPageConditionWithReadType(SortType type, Long lastWordId) {
        if (lastWordId == null) {
            return null;
        }

        if (type == SortType.updated) {
            return ltUpdateTime(lastWordId);
        } else if (type == SortType.created) {
            return gtWordId(lastWordId);
        }

        return gtWordId(lastWordId);
    }

    /**
     * 검색 문자열이 있으면 검색하는 쿼리를 작성한다.
     *
     * @param searchText 검색 문자열
     * @return 단어, 상세 내용 검색하는 쿼리 리턴
     */
    private BooleanExpression likeSearchText(String searchText) {
        String likeText = "%" + searchText + "%";
        if (searchText == null) return null;
        return likeWordSearchText(likeText).or(likeDetailSearchText(likeText));
    }

    /**
     * 단어 상세 내용에 검색 문자열이 있는 wordId를 조회 후 해당 wordId만 조회하는 쿼리를 작성한다.
     *
     * @param searchText 검색 문자열
     * @return 단어 상세 내용에 검색 문자열이 있는 wordId를 조회하는 쿼리 리턴
     */
    private BooleanExpression likeDetailSearchText(String searchText) {
        return qWord.wordId.in(findByDetailWord(searchText));
    }

    /**
     * 단어에 검색 문자열이 있는지 확인하는 쿼리를 작성한다.
     *
     * @param searchText 검색 문자열
     * @return 단어 검색 쿼리 리턴
     */
    private BooleanExpression likeWordSearchText(String searchText) {
        return qWord.word.like(searchText)
                .or(qWord.mean.like(searchText))
                .or(qWord.read.like(searchText))
//                .or(qWord.memo.like(searchText))
                ;
    }

    /**
     * 단어 상세 내용에 해당하는 텍스트가 있는지 확인 후 해당하는 wordId를 리턴한다.
     *
     * @param words 검색할 내용
     * @return wordId
     */
    private List<DailyWordDetailDto> findByDetailWord(List<String> words) {
        return queryFactory.selectDistinct(Projections.constructor(
                        DailyWordDetailDto.class,
                        qWordDetail.wordDetailId,
                        qWordDetail.word.wordId,
                        qWordDetail.title,
                        qWordDetail.content
                ))
                .from(qWordDetail)
                .leftJoin(qWordDetail.word)
                .where(
                        qWordDetail.title.in(words)
                                .or(qWordDetail.content.in(words))
                )
                .fetch();
    }

    /**
     * 단어 상세 내용에 해당하는 텍스트가 있는지 확인 후 해당하는 wordId를 리턴한다.
     *
     * @param searchText 검색할 내용
     * @return wordId
     */
    private List<Long> findByDetailWord(String searchText) {
        return queryFactory.selectDistinct(qWordDetail.word.wordId)
                .from(qWordDetail)
                .where(
                        qWordDetail.content.like(searchText)
//                                .or(qWordDetail.memo.like(searchText))
                )
                .fetch();
    }

    /**
     * 해당 단어장의 단어 조회 조건문
     *
     * @param wordBookId 단어장 아이디
     * @return 해당 단어장의 단어 조회 쿼리 리턴
     */
    private BooleanExpression eqWordBookId(Long wordBookId) {
        if (wordBookId == null) {
            return null;
        }
        return qWord.wordBook.id.eq(wordBookId);
    }

    /**
     * 마지막으로 조회한 단어의 아이디의 다음 단어 데이터를 조회한다.
     *
     * @param lastWordId 마지막으로 조회한 단어
     * @return 처음 조회 시 null, 단어 아이디를 바교하는 쿼리 리턴
     */
    private BooleanExpression gtWordId(Long lastWordId) {
        if (lastWordId == null) {
            return null;
        }
        return qWord.wordId.gt(lastWordId);
    }

    /**
     * 업데이트 시간 내림차순으로 페이징 처리를 위해 마지막으로 조회한 단어의 아이디의 업데이트 시간을 가져와 해당 데이터보다 이전에 저장된 데이터를 가져온다.(No Offset)
     *
     * @param lastWordId 마지막으로 조회한 단어
     * @return 처음 조회 시 null, 업데이트 시간 비교하는 쿼리 리턴
     */
    private BooleanExpression ltUpdateTime(Long lastWordId) {
        if (lastWordId == null) {
            return null;
        }
        return qWord.updateTime.lt(
                queryFactory.select(qWord.updateTime)
                        .from(qWord)
                        .where(qWord.wordId.eq(lastWordId))
        );
    }

    /**
     * 암기 여부를 확인하는 쿼리 작성
     *
     * @param status Y | N
     * @return 암기 여부를 비교하는 쿼리 리턴
     */
    private BooleanExpression eqMemorization(String status) {
        if (status == null || status.isEmpty()) {
            return null;
        }
        return qWord.memorization.eq(status);
    }

    private BooleanExpression eqLanguage(String lang) {
        if (lang == null || lang.isEmpty()) {
            return null;
        }
        return qWord.lang.eq(GttsCode.valueOf(lang));
    }
}
