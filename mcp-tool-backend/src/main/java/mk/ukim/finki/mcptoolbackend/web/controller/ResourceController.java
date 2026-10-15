package mk.ukim.finki.mcptoolbackend.web.controller;

import mk.ukim.finki.mcptoolbackend.model.dto.DisplayResourceDto;
import mk.ukim.finki.mcptoolbackend.model.dto.ResourceFilterDto;
import mk.ukim.finki.mcptoolbackend.service.application.ResourceApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The resource browser API: paged, filterable access to everything the MCP
 * tool has collected, plus the per-resource analysis trigger.
 */
@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    private final ResourceApplicationService resourceApplicationService;

    public ResourceController(ResourceApplicationService resourceApplicationService) {
        this.resourceApplicationService = resourceApplicationService;
    }

    @GetMapping
    public ResponseEntity<Page<DisplayResourceDto>> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) Long searchRunId,
        @RequestParam(required = false) Double minMacedonianConfidence,
        @RequestParam(required = false) Boolean analyzed,
        @RequestParam(required = false) Boolean donated,
        @RequestParam(required = false) String search
    ) {
        ResourceFilterDto filter = new ResourceFilterDto(searchRunId, minMacedonianConfidence, analyzed, donated, search);
        return ResponseEntity.ok(resourceApplicationService.findAll(filter, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayResourceDto> findById(@PathVariable Long id) {
        return resourceApplicationService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/analyze")
    public ResponseEntity<DisplayResourceDto> analyze(@PathVariable Long id) {
        return resourceApplicationService
            .analyze(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<DisplayResourceDto> deleteById(@PathVariable Long id) {
        return resourceApplicationService
            .deleteById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
