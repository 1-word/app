package com.numo.wordapp.entity.word;

import com.numo.wordapp.dto.folder.FolderUpdateDto;
import com.numo.wordapp.entity.Timestamped;
import com.numo.wordapp.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    public void update(FolderUpdateDto updateDto) {
        this.folderName = updateDto.folderName();
        this.color = updateDto.color();
        this.background = updateDto.background();
        this.memo = updateDto.memo();
    }

    private boolean isOwner(Long userId) {
        return Objects.equals(userId, user.getUserId());
    }

}
