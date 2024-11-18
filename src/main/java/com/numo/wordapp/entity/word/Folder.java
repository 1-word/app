package com.numo.wordapp.entity.word;

import com.numo.wordapp.entity.Timestamped;
import com.numo.wordapp.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Folder extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String folderName;

    //230923 폴더 컬러 추가
    private String background;
    private String color;

    private String memo;

    @Builder
    public Folder(Long folderId, User user, String folderName, String memo, String color, String background) {
        this.folderId = folderId;
        this.user = user;
        this.folderName = folderName;
        this.memo = memo;
        this.color = color;
        this.background = background;
    }

//    public void update() {
//        this.folderName =
//    }

}
