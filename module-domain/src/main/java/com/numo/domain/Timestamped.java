package com.numo.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {
    @CreatedDate
    private LocalDateTime createTime;   //생성 날짜

    @LastModifiedDate
    private LocalDateTime updateTime;     //업데이트 날짜

    public String getCreateTimeString() {
       return getTimeString(createTime);
    }

    public String getUpdateTimeString() {
       return getTimeString(updateTime);
    }

    public static String getTimeString(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return getFormatTime(time, "yyyy-MM-dd");
    }

    private static String getFormatTime(LocalDateTime time, String format) {
        return time.format(DateTimeFormatter.ofPattern(format));
    }

}
