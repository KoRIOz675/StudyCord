package fr.isep.studycord.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private String username;

    private String email;

    private String role;

    private String school;
}
