package mk.ukim.finki.mcptoolbackend.model.dto;

import mk.ukim.finki.mcptoolbackend.model.domain.User;
import mk.ukim.finki.mcptoolbackend.model.enums.Role;

public record RegisterUserResponseDto(
    String username,
    String name,
    String surname,
    String email,
    Role role
) {
    public static RegisterUserResponseDto from(User user) {
        return new RegisterUserResponseDto(
            user.getUsername(),
            user.getName(),
            user.getSurname(),
            user.getEmail(),
            user.getRole()
        );
    }
}
