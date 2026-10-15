package mk.ukim.finki.mcptoolbackend.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import mk.ukim.finki.mcptoolbackend.model.domain.ResourceAnalysis;

public record DisplayResourceAnalysisDto(
    Long id,
    Long resourceId,
    String summary,
    String keywords,
    Integer sentenceCount,
    Double macedonianConfidence,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime analyzedAt
) {
    public static DisplayResourceAnalysisDto from(ResourceAnalysis analysis) {
        if (analysis == null) {
            return null;
        }
        return new DisplayResourceAnalysisDto(
            analysis.getId(),
            analysis.getResource().getId(),
            analysis.getSummary(),
            analysis.getKeywords(),
            analysis.getSentenceCount(),
            analysis.getMacedonianConfidence(),
            analysis.getAnalyzedAt()
        );
    }
}
