package mk.ukim.finki.mcptoolbackend.service.domain.impl;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import mk.ukim.finki.mcptoolbackend.model.domain.ToolInvocationLog;
import mk.ukim.finki.mcptoolbackend.repository.ToolInvocationLogRepository;
import mk.ukim.finki.mcptoolbackend.service.domain.ToolInvocationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ToolInvocationLogServiceImpl implements ToolInvocationLogService {
    private final ToolInvocationLogRepository toolInvocationLogRepository;

    public ToolInvocationLogServiceImpl(ToolInvocationLogRepository toolInvocationLogRepository) {
        this.toolInvocationLogRepository = toolInvocationLogRepository;
    }

    @Override
    @Transactional
    public ToolInvocationLog log(String toolName, String arguments, String resultSummary, boolean successful, SearchRun searchRun) {
        ToolInvocationLog log = new ToolInvocationLog(toolName, arguments, resultSummary, successful);
        log.setSearchRun(searchRun);
        return toolInvocationLogRepository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToolInvocationLog> findAll() {
        return toolInvocationLogRepository.findAllByOrderByOccurredAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToolInvocationLog> findBySearchRun(Long searchRunId) {
        return toolInvocationLogRepository.findAllBySearchRunIdOrderByOccurredAtAsc(searchRunId);
    }
}
