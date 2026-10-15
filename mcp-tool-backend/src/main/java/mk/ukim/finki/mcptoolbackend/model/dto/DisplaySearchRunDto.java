package mk.ukim.finki.mcptoolbackend.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import mk.ukim.finki.mcptoolbackend.model.enums.SearchStatus;

public record DisplaySearchRunDto(
    Long id,
    String query,
    SearchStatus status,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startedAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime finishedAt,
    Integer resultCount,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt
) {
    public static DisplaySearchRunDto from(SearchRun searchRun) {
        return new DisplaySearchRunDto(
            searchRun.getId(),
            searchRun.getQuery(),
            searchRun.getStatus(),
            searchRun.getStartedAt(),
            searchRun.getFinishedAt(),
            searchRun.getResultCount(),
            searchRun.getCreatedAt()
        );
    }

    public static List<DisplaySearchRunDto> from(List<SearchRun> searchRuns) {
        return searchRuns
            .stream()
            .map(DisplaySearchRunDto::from)
            .toList();
    }
}
