package mk.ukim.finki.mcptoolbackend.service.application.impl;

import java.util.List;
import java.util.Optional;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayResourceDto;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplaySearchRunDto;
import mk.ukim.finki.mcptoolbackend.model.dto.RunSearchRequestDto;
import mk.ukim.finki.mcptoolbackend.service.application.SearchRunApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.SearchRunService;
import org.springframework.stereotype.Service;

@Service
public class SearchRunApplicationServiceImpl implements SearchRunApplicationService {
    private final SearchRunService searchRunService;

    private static final int DEFAULT_LIMIT = 10;

    public SearchRunApplicationServiceImpl(SearchRunService searchRunService) {
        this.searchRunService = searchRunService;
    }

    @Override
    public List<DisplaySearchRunDto> findAll() {
        return DisplaySearchRunDto.from(searchRunService.findAll());
    }

    @Override
    public Optional<DisplaySearchRunDto> findById(Long id) {
        return searchRunService.findById(id)
                .map(DisplaySearchRunDto::from);
    }

    @Override
    public Optional<DisplaySearchRunDto> run(RunSearchRequestDto request) {
        int limit = request.limit() == null ? DEFAULT_LIMIT : request.limit();

        return Optional.of(DisplaySearchRunDto.from(
                searchRunService.run(request.query(), limit)
        ));
    }

    @Override
    public List<DisplayResourceDto> findResources(Long searchRunId) {
        return DisplayResourceDto.from(searchRunService.findResources(searchRunId));
    }
}
