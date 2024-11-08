package com.numo.wordapp.dto.word;

import com.numo.wordapp.entity.word.Word;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
public class PageDto {
    int current;
    int next;
//    int previous
    boolean hasNext;
//    int max;
    int lastWid;

    public PageDto(Slice<Word> page){
        this.current = page.getNumber();
        this.hasNext = page.hasNext();
//        this.max = page.getTotalPages();
        // 다음 페이지가 없으면 현재(마지막) 페이지가 next
        this.next = this.hasNext? page.getNumber() + 1 : page.getNumber();
    }

    public PageDto(Slice<Word> page, int lastWid){
        this(page);
        this.lastWid = lastWid;
    }

    public static PageDto getPageInstance(Slice<Word> pageWord, List<WordDto.Response> dto){
        return new PageDto(pageWord, dto.size() > 0 ? dto.get(dto.size()-1).getWord_id() : -1);
    }
}
