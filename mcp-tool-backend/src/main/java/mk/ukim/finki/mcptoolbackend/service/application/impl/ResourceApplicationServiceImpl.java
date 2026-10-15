package mk.ukim.finki.mcptoolbackend.service.application.impl;

import java.util.Optional;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayResourceDto;
import mk.ukim.finki.mcptoolbackend.model.dto.ResourceFilterDto;
import mk.ukim.finki.mcptoolbackend.service.application.ResourceApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ResourceApplicationServiceImpl implements ResourceApplicationService {
    private final ResourceService resourceService;

    public ResourceApplicationServiceImpl(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public Page<DisplayResourceDto> findAll(ResourceFilterDto filter, int page, int size) {
        return resourceService.findAll(filter, PageRequest.of(page, size))
                .map(DisplayResourceDto::from);
    }

    @Override
    public Optional<DisplayResourceDto> findById(Long id) {
        return resourceService.findById(id)
                .map(DisplayResourceDto::from);
    }

    @Override
    public Optional<DisplayResourceDto> deleteById(Long id) {
        return resourceService.deleteById(id)
                .map(DisplayResourceDto::from);
    }

    @Override
    public Optional<DisplayResourceDto> analyze(Long id) {
        return Optional.of(DisplayResourceDto.from(resourceService.analyze(id)));
    }
}
