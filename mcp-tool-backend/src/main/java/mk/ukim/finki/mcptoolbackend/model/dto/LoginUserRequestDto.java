package mk.ukim.finki.mcptoolbackend.model.dto;

public record LoginUserRequestDto(
    String username,
    String password
) {
}
