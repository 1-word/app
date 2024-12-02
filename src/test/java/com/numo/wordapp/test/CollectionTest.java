package com.numo.wordapp.test;

import com.numo.wordapp.dto.word.detail.ReadWordDetailGroupKey;
import com.numo.wordapp.dto.word.detail.ReadWordDetailResponseDto;
import com.numo.wordapp.dto.word.detail.ReadWordDetailListResponseDto;
import com.numo.wordapp.dto.word.detail.WordDetailResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectionTest {
    List<WordDetailResponseDto> dtoList = null;

    @BeforeEach
    void setUp() {
        WordDetailResponseDto dto1 = WordDetailResponseDto.builder()
                .wordDetailId(1L)
                .wordGroupId(3L)
                .groupName("동사")
                .title("기본")
                .content("put")
                .build();

        WordDetailResponseDto dto2 = WordDetailResponseDto.builder()
                .wordDetailId(2L)
                .wordGroupId(3L)
                .groupName("동사")
                .title("과거")
                .content("put")
                .build();

        WordDetailResponseDto dto3 = WordDetailResponseDto.builder()
                .wordDetailId(3L)
                .wordGroupId(3L)
                .groupName("동사")
                .title("과거분사")
                .content("put")
                .build();

        WordDetailResponseDto dto4 = WordDetailResponseDto.builder()
                .wordDetailId(4L)
                .wordGroupId(5L)
                .groupName("자동사")
                .title("")
                .content("[사람이] (배로)가다, 전진하다")
                .build();

        WordDetailResponseDto dto5 = WordDetailResponseDto.builder()
                .wordDetailId(1L)
                .wordGroupId(6L)
                .groupName("명사")
                .title("")
                .content("밀기, 떠밀기")
                .build();

        dtoList = List.of(dto1, dto2, dto3, dto4, dto5);
    }

    @Test
    void collection1() {
        Map<Long, List<WordDetailResponseDto>> collect = dtoList.stream()
                .collect(Collectors.groupingBy(
                        WordDetailResponseDto::wordGroupId
                ));

        System.out.println(collect);
    }

    @Test
    void collection2() {
        Map<Long, List<ReadWordDetailResponseDto>> collect = dtoList.stream()
                .collect(Collectors.groupingBy(
                        WordDetailResponseDto::wordGroupId,
                        Collectors.mapping(entry ->
                                new ReadWordDetailResponseDto(
                                        entry.wordDetailId(),
                                        entry.title(),
                                        entry.content(),
                                        entry.createTime(),
                                        entry.updateTime()
                                ), Collectors.toList()
                        )
                ));

        System.out.println(collect);
    }

    @Test
    void collection3() {
        Map<ReadWordDetailGroupKey, List<WordDetailResponseDto>> collect = dtoList.stream()
                .collect(Collectors.groupingBy(
                        entry -> new ReadWordDetailGroupKey(entry.wordGroupId(), entry.wordId(), entry.groupName()),
                        Collectors.toList()));

        System.out.println(collect);
    }

    @Test
    void collection4() {
        List<ReadWordDetailListResponseDto> collect = dtoList.stream()
                .collect(Collectors.groupingBy(
                        entry -> new ReadWordDetailGroupKey(entry.wordGroupId(), entry.wordId(), entry.groupName()),
                        Collectors.mapping(entry ->
                                new ReadWordDetailResponseDto(
                                        entry.wordDetailId(),
                                        entry.title(),
                                        entry.content(),
                                        entry.createTime(),
                                        entry.updateTime()
                                ), Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(entry -> new ReadWordDetailListResponseDto(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();
        ;

        System.out.println(collect);
    }

    @Test
    void collection5() {
        Map<String, List<ReadWordDetailResponseDto>> collect = dtoList.stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.wordGroupId() + "_" + entry.groupName(),
                        Collectors.mapping(entry ->
                                new ReadWordDetailResponseDto(
                                        entry.wordDetailId(),
                                        entry.title(),
                                        entry.content(),
                                        entry.createTime(),
                                        entry.updateTime()
                                ), Collectors.toList()
                        )
                ));

        System.out.println(collect);
    }

}
