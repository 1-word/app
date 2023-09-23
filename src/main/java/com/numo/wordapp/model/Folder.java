package com.numo.wordapp.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "folder")
public class Folder extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private int folderId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "folder_name")
    private String folderName;

    //230923 폴더 컬러 추가
    private String background;
    private String color;

    private String memo;

    @Builder
    public Folder(int folderId, String userId, String folderName, String memo, String color, String background) {
        this.folderId = folderId;
        this.userId = userId;
        this.folderName = folderName;
        this.memo = memo;
        this.color = color;
        this.background = background;
    }

    public int getFolderId() {
        return folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor(){ return color;}

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBackground() {
        return background;
    }
}
