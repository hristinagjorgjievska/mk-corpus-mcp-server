package mk.ukim.finki.mcptoolbackend.service.application.impl;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayToolInvocationLogDto;
import mk.ukim.finki.mcptoolbackend.service.application.ToolInvocationLogApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.ToolInvocationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ToolInvocationLogApplicationServiceImpl implements ToolInvocationLogApplicationService {
    private final ToolInvocationLogService toolInvocationLogService;

    public ToolInvocationLogApplicationServiceImpl(ToolInvocationLogService toolInvocationLogService) {
        this.toolInvocationLogService = toolInvocationLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisplayToolInvocationLogDto> findAll() {
        return DisplayToolInvocationLogDto.from(toolInvocationLogService.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisplayToolInvocationLogDto> findBySearchRun(Long searchRunId) {
        return DisplayToolInvocationLogDto.from(toolInvocationLogService.findBySearchRun(searchRunId));
    }
}
