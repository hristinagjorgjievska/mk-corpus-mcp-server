package mk.ukim.finki.mcptoolbackend.service.application;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayToolInvocationLogDto;

public interface ToolInvocationLogApplicationService {
    List<DisplayToolInvocationLogDto> findAll();

    List<DisplayToolInvocationLogDto> findBySearchRun(Long searchRunId);
}
