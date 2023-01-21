package com.numo.wordapp.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.numo.wordapp.security.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {

        @Getter
        public static class Request{
                @NotNull
                @Size(min = 3, max = 50)
                private String user_id;

                @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                @NotNull
                @Size(min = 3, max = 100)
                private String password;

                @NotNull
                @Size(min = 3, max = 50)
                private String nickname;
        }

        @Getter
        public static class Response{
                private String user_id;
                private String nickname;

                public Response(User user){
                        this.user_id = user.getUserId();
                        this.nickname = user.getNickname();
                }
        }
}
