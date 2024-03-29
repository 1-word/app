package com.numo.wordapp.dto;

import com.numo.wordapp.model.word.Folder;
import lombok.Getter;
import lombok.Setter;

public class FolderDto{
    @Getter
    @Setter
    public static class Request{
        int folder_id;
        String user_id;
        String folder_name;
        String memo;
        String color;
        String background;

        public Folder toEntity(){
            return Folder.builder()
                    .userId(user_id)
                    .folderName(folder_name)
                    .color(color)
                    .background(background)
                    .memo(memo)
                    .build();
        }
    }

    @Getter
    public static class Response{
        int folder_id;
        String folder_name;
        String color;
        String background;
        String memo;

        public Response(Folder folder){
            this.folder_id = folder.getFolderId();
            this.folder_name = folder.getFolderName();
            this.color = folder.getColor();
            this.background = folder.getBackground();
            this.memo = folder.getMemo();
        }
    }

}
