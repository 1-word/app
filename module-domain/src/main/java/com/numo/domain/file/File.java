package com.numo.domain.file;

import com.numo.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String fileId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    String path;
    String oriName;
    String extension;
    @ColumnDefault("true")
    boolean isSecret;

//    public void checkPermission(Long userId) {
//        if (isSecret) {
//            if (!Objects.equals(userId, user.getUserId())) {
//                throw new CustomException(ErrorCode.FILE_ACCESS_DENIED);
//            }
//        }
//    }
}
