package com.numo.wordapp.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {
    @CreatedDate
    private LocalDateTime createTime;   //생성 날짜

    @LastModifiedDate
    private LocalDateTime updateTime;     //업데이트 날짜

//    public String getUpdateTime() {
//        return getDateTime(updateTime);
//    }
//
//    public String getCreateTime(){
//        return getDateTime(createTime);
//    }
//
//    private String getDateTime(LocalDateTime time){
//        String date;
//        try {
//            date = time.format(DateTimeFormatter.ISO_DATE);
//        }catch (Exception e){
//            date = "";
//        }
//        return date;
//    }
}
