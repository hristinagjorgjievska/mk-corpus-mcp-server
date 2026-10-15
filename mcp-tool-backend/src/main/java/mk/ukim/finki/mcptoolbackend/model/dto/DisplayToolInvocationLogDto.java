package mk.ukim.finki.mcptoolbackend.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import mk.ukim.finki.mcptoolbackend.model.domain.ToolInvocationLog;

public record DisplayToolInvocationLogDto(
    Long id,
    String toolName,
    String arguments,
    String resultSummary,
    boolean successful,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime occurredAt,
    Long searchRunId
) {
    public static DisplayToolInvocationLogDto from(ToolInvocationLog log) {
        SearchRun searchRun = log.getSearchRun();
        return new DisplayToolInvocationLogDto(
            log.getId(),
            log.getToolName(),
            log.getArguments(),
            log.getResultSummary(),
            log.isSuccessful(),
            log.getOccurredAt(),
            searchRun == null ? null : searchRun.getId()
        );
    }

    public static List<DisplayToolInvocationLogDto> from(List<ToolInvocationLog> logs) {
        return logs
            .stream()
            .map(DisplayToolInvocationLogDto::from)
            .toList();
    }
}
