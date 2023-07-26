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

    private String memo;

    @Builder
    public Folder(int folderId, String userId, String folderName, String memo) {
        this.folderId = folderId;
        this.userId = userId;
        this.folderName = folderName;
        this.memo = memo;
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
}
