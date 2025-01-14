package ru.improve.openfy.api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPostRequest {

    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @Size(min = 8)
    private String password;

    @NotNull
    @Size(min = 1, max = 50)
    private String nickname;
}
