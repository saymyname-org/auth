package ru.improve.openfy.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPatchRequest {

    private String name;

    private String secondName;

    private String nickname;

    private LocalDate birthdate;
}
