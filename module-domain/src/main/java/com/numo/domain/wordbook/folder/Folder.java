package com.numo.domain.wordbook.folder;

import com.numo.domain.base.Timestamped;
import com.numo.domain.user.User;
import com.numo.domain.wordbook.WordBookRole;
import com.numo.domain.wordbook.folder.dto.FolderUpdateDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
@Deprecated
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

    @Column(unique = true)
    private String link;

    private boolean isShared;

    @Enumerated(EnumType.STRING)
    private WordBookRole anyoneBasicRole;

    @Enumerated(EnumType.STRING)
    private WordBookRole memberBasicRole;

    public void update(FolderUpdateDto updateDto) {
        this.folderName = updateDto.folderName();
        this.color = updateDto.color();
        this.background = updateDto.background();
        this.memo = updateDto.memo();
    }

    public void startSharing() {
        isShared = true;
    }

    public void cancelSharing() {
        isShared = false;
    }

    public boolean isOwner(Long userId) {
        return Objects.equals(userId, user.getUserId());
    }

}
