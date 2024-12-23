package com.numo.wordapp.repository.word.query;

import com.numo.wordapp.dto.folder.FolderInWordCountDto;
import com.numo.wordapp.dto.sentence.wordDailySentence.DailyWordDetailDto;
import com.numo.wordapp.dto.sentence.DailyWordDto;
import com.numo.wordapp.dto.sentence.DailyWordListDto;
import com.numo.wordapp.dto.word.read.ReadWordRequestDto;
import com.numo.wordapp.dto.word.WordDto;
import com.numo.wordapp.dto.word.detail.WordDetailResponseDto;
import com.numo.wordapp.entity.word.type.GttsCode;
import com.numo.wordapp.entity.word.QWord;
import com.numo.wordapp.entity.word.type.ReadType;
import com.numo.wordapp.entity.word.detail.QWordDetail;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WordCustomRepositoryImpl implements WordCustomRepository {
    private final JPAQueryFactory queryFactory;

    private QWord qWord = QWord.word1;
    private QWordDetail qWordDetail = QWordDetail.wordDetail;

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
    public Slice<WordDto> findWordBy(Pageable pageable, Long userId, Long lastWordId, ReadWordRequestDto readDto) {
        List<WordDto> results = queryFactory.select(Projections.constructor(
                        WordDto.class,
                        qWord.wordId,
                        qWord.folder.folderId,
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
                .leftJoin(qWord.folder)
                .leftJoin(qWord.sound)
                .leftJoin(qWord.user)
                .where(
                        qWord.user.userId.eq(userId),
                        eqFolderId(readDto.folderId()),
                        eqMemorization(readDto.memorization()),
                        eqLanguage(readDto.lang()),
                        createPageConditionWithReadType(readDto.readType(), lastWordId),
                        likeSearchText(readDto.searchText())
                )
                .orderBy(wordSort(readDto.readType()).toArray(OrderSpecifier[]::new))
                // 다음 페이지가 있는지 확인하기 위해 다음 데이터도 함께 조회
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, results);
    }

    /**
     * wordId에 해당하는 단어 데이터를 찾는다
     * @param userId 유저 아이디
     * @param wordId 단어 고유 번호
     * @return wordId에 해당하는 단어 데이터
     */
    @Override
    public WordDto findWordByWordId(Long userId, Long wordId) {
        return queryFactory.select(Projections.constructor(
                        WordDto.class,
                        qWord.wordId,
                        qWord.folder.folderId,
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
                .leftJoin(qWord.folder)
                .leftJoin(qWord.sound)
                .leftJoin(qWord.user)
                .where(
                        qWord.wordId.eq(wordId),
                        qWord.user.userId.eq(userId)
                ).fetchOne();
    }

    @Override
    public List<WordDetailResponseDto> findWordDetailByIds(List<Long> wordIds) {
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

    @Override
    public Map<Long, FolderInWordCountDto> countFolderInWord(Long userId) {
        Map<Long, FolderInWordCountDto> result = queryFactory.select(Projections.constructor(
                        FolderInWordCountDto.class,
                        qWord.folder.folderId,
                        Expressions.as(Wildcard.count, "count")
                )).from(qWord)
                .where(
                        qWord.folder.folderId.isNotNull(),
                        qWord.user.userId.eq(userId)
                )
                .groupBy(qWord.folder.folderId)
                .fetch()
                .stream()
                .collect(Collectors.toMap(FolderInWordCountDto::folderId, Function.identity()));

        return result;
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
                        createOrConditionWith(words, qWord.word)
                )
                .fetch();

        // detail에서 가져오기
        List<DailyWordDetailDto> detailDtos = findByDetailWord(userId, words);
        return new DailyWordListDto(wordDtos, detailDtos);
    }

    /**
     * word 조회 sort 생성
     * @param type 조회 타입
     * @return 조회 타입에 따른 order by 쿼리
     */
    private List<OrderSpecifier<?>> wordSort(ReadType type) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        if (type == ReadType.update) {
            orderSpecifierList.add(new OrderSpecifier<>(Order.DESC, qWord.updateTime));
        } else if (type == ReadType.current) {
            orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, qWord.wordId));
        } else {
            orderSpecifierList.add(new OrderSpecifier<>(Order.ASC, qWord.wordId));
        }

        return orderSpecifierList;
    }

    /**
     * word 조회 시 noOffSet을 위한 query 추가
     * @param type 조회 타입
     * @param lastWordId 마지막으로 조회한 단어 아이디
     * @return 조회 타입에 따른 마지막으로 조회한 아이디의 쿼리
     */
    private BooleanExpression createPageConditionWithReadType(ReadType type, Long lastWordId) {

        if (type == ReadType.update) {
            return ltUpdateTime(lastWordId);
        } else if (type == ReadType.current) {
            return gtWordId(lastWordId);
        }

        return gtWordId(lastWordId);
    }

    /**
     * 해당 필드에 대한 or 조건문을 만든다.
     * @param input 문자열 리스트
     * @return 해당 필드에 대한 or 조건문
     */
    private BooleanExpression createOrConditionWith(List<String> input, StringPath field) {
        BooleanExpression result = null;
        for (String s : input) {
            if (result == null) {
                result = field.eq(s);
                continue;
            }
            result = result.or(field.eq(s));
        }
        return result;
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
     * @param words 검색할 내용
     * @return wordId
     */
    private List<DailyWordDetailDto> findByDetailWord(Long userId, List<String> words){
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
                        qWordDetail.word.user.userId.eq(userId),
                        createOrConditionWith(words, qWordDetail.title)
                                .or(createOrConditionWith(words, qWordDetail.content))
                )
                .fetch();
    }

    /**
     * 단어 상세 내용에 해당하는 텍스트가 있는지 확인 후 해당하는 wordId를 리턴한다.
     * @param searchText 검색할 내용
     * @return wordId
     */
    private List<Long> findByDetailWord(String searchText){
        return queryFactory.selectDistinct(qWordDetail.word.wordId)
                .from(qWordDetail)
                .where(
                        qWordDetail.content.like(searchText)
//                                .or(qWordDetail.memo.like(searchText))
                )
                .fetch();
    }

    /**
     * 다음 페이지가 있는지 확인하여 페이지 데이터 추가 후 리턴한다.
     * @param pageable 페이지 변수
     * @param results DB에서 return한 데이터
     * @return page 데이터를 포함한 데이터
     */
    private Slice<WordDto> checkLastPage(Pageable pageable, List<WordDto> results){
        boolean hasNext = false;
        // page의 크기보다 데이터의 크기가 크다면, 다음 페이지가 존재한다.
        if (results.size() > pageable.getPageSize()){
            // 추가로 조회된 데이터는 삭제한다.
            results.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }

    /**
     * 폴더 아이디 확인 후 해당하는 쿼리 작성
     * @param folderId 폴더 아이디, 폴더가 아닌 전체 데이터를 확인할 경우 -1
     * @return 폴더 아이디가 있는 경우에만 폴더 검색하는 쿼리 리턴
     */
    private BooleanExpression eqFolderId(Long folderId){
        if (folderId == null) {
            return null;
        }
        return qWord.folder.folderId.eq(folderId);
    }

    /**
     * 마지막으로 조회한 단어의 아이디의 다음 단어 데이터를 조회한다.
     * @param lastWordId 마지막으로 조회한 단어
     * @return 처음 조회 시 null, 단어 아이디를 바교하는 쿼리 리턴
     */
    private BooleanExpression gtWordId(Long lastWordId){
        if (lastWordId == null) {
            return null;
        }
        return qWord.wordId.gt(lastWordId);
    }

    /**
     * 업데이트 시간 내림차순으로 페이징 처리를 위해 마지막으로 조회한 단어의 아이디의 업데이트 시간을 가져와 해당 데이터보다 이전에 저장된 데이터를 가져온다.(No Offset)
     * @param lastWordId 마지막으로 조회한 단어
     * @return 처음 조회 시 null, 업데이트 시간 비교하는 쿼리 리턴
     */
    private BooleanExpression ltUpdateTime(Long lastWordId){
        if (lastWordId == null) {
            return null;
        }
        return qWord.updateTime.lt(
                queryFactory.select(qWord.updateTime)
                        .from(qWord)
                        .where(qWord.wordId.eq(lastWordId)
                        )
        );
    }

    /**
     * 암기 여부를 확인하는 쿼리 작성
     * @param status Y | N
     * @return 암기 여부를 비교하는 쿼리 리턴
     */
    private BooleanExpression eqMemorization(String status){
        if (status == null || status.isEmpty()) {
            return null;
        }
        return qWord.memorization.eq(status);
    }

    private BooleanExpression eqLanguage(String lang){
        if (lang == null || lang.isEmpty()) {
            return null;
        }
        return qWord.lang.eq(GttsCode.valueOf(lang));
    }
}
