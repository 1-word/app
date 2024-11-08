package com.numo.wordapp.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.numo.wordapp.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
@Schema
public class UserDto {
        @Schema(description = "회원가입")
        @Getter
        public static class Request{
                @NotNull
                @Size(min = 3, max = 50)
                @Schema(description = "유저 아이디")
                private String user_id;

                @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
                @NotNull
                @Size(min = 3, max = 100)
                @Schema(description = "비밀번호")
                private String password;

                @NotNull
                @Size(min = 3, max = 50)
                @Schema(description = "닉네임")
                private String username;
        }

        @Getter
        public static class Response{
                private String user_id;
                private String username;

                public Response(User user){
                        this.user_id = user.getUserId();
                        this.username = user.getUsername();
                }
        }
}
