package mk.ukim.finki.mcptoolbackend.service.application;

import java.util.Optional;
import mk.ukim.finki.mcptoolbackend.model.dto.LoginUserRequestDto;
import mk.ukim.finki.mcptoolbackend.model.dto.LoginUserResponseDto;
import mk.ukim.finki.mcptoolbackend.model.dto.RegisterUserRequestDto;
import mk.ukim.finki.mcptoolbackend.model.dto.RegisterUserResponseDto;

public interface UserApplicationService {
    Optional<RegisterUserResponseDto> register(RegisterUserRequestDto registerUserRequestDto);

    Optional<LoginUserResponseDto> login(LoginUserRequestDto loginUserRequestDto);

    Optional<RegisterUserResponseDto> findByUsername(String username);
}
