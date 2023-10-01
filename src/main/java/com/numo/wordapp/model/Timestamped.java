package com.numo.wordapp.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {
    @CreatedDate
    private LocalDateTime create_time;   //생성 날짜

    @LastModifiedDate
    private LocalDateTime update_time;     //업데이트 날짜

    public String getUpdate_time() {
        return getDateTime(update_time);
    }

    public void setUpdate_time(LocalDateTime update_time) {
        this.update_time = update_time;
    }

    public String getCreate_time(){
        return getDateTime(create_time);
    }

    public String getDateTime(LocalDateTime time){
        String date;
        try {
            date = time.format(DateTimeFormatter.ISO_DATE);
        }catch (Exception e){
            date = "";
        }
        return date;
    }
}
