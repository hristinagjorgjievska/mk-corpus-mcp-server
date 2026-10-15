package mk.ukim.finki.mcptoolbackend.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;

public record DisplayResourceDto(
    Long id,
    Long searchRunId,
    String externalId,
    String title,
    String content,
    String sourceUrl,
    String language,
    Double macedonianConfidence,
    Integer wordCount,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fetchedAt,
    DisplayResourceAnalysisDto analysis,
    Long donationBatchId
) {
    public static DisplayResourceDto from(Resource resource) {
        return new DisplayResourceDto(
            resource.getId(),
            resource.getSearchRun().getId(),
            resource.getExternalId(),
            resource.getTitle(),
            resource.getContent(),
            resource.getSourceUrl(),
            resource.getLanguage(),
            resource.getMacedonianConfidence(),
            resource.getWordCount(),
            resource.getFetchedAt(),
            DisplayResourceAnalysisDto.from(resource.getAnalysis()),
            resource.getDonationBatch() == null ? null : resource.getDonationBatch().getId()
        );
    }

    public static List<DisplayResourceDto> from(List<Resource> resources) {
        return resources
            .stream()
            .map(DisplayResourceDto::from)
            .toList();
    }
}
