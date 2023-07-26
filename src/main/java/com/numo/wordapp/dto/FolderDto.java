package com.numo.wordapp.dto;

import com.numo.wordapp.model.Folder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class FolderDto {
    @Getter
    @Setter
    public static class Request{
        int folder_id;
        String user_id;
        String folder_name;
        String memo;

        public Folder toEntity(){
            return Folder.builder()
                    .userId(user_id)
                    .folderName(folder_name)
                    .memo(memo)
                    .build();
        }
    }

    @Getter
    public static class Response{
        int folder_id;
        String folder_name;
        String memo;

        public Response(Folder folder){
            this.folder_id = folder.getFolderId();
            this.folder_name = folder.getFolderName();
            this.memo = folder.getMemo();
        }
    }
}
