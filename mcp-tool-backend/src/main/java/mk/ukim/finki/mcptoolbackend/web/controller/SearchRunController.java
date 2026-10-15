package mk.ukim.finki.mcptoolbackend.web.controller;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayResourceDto;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplaySearchRunDto;
import mk.ukim.finki.mcptoolbackend.model.dto.RunSearchRequestDto;
import mk.ukim.finki.mcptoolbackend.service.application.SearchRunApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The search-run API: list past runs, inspect one, and trigger a new search
 * against the assigned website. {@code POST /run} does the same work the
 * {@code search_resources} MCP tool does.
 */
@RestController
@RequestMapping("/api/search-runs")
public class SearchRunController {
    private final SearchRunApplicationService searchRunApplicationService;

    public SearchRunController(SearchRunApplicationService searchRunApplicationService) {
        this.searchRunApplicationService = searchRunApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<DisplaySearchRunDto>> findAll() {
        return ResponseEntity.ok(searchRunApplicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplaySearchRunDto> findById(@PathVariable Long id) {
        return searchRunApplicationService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/run")
    public ResponseEntity<DisplaySearchRunDto> run(@RequestBody RunSearchRequestDto request) {
        return searchRunApplicationService
            .run(request)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}/resources")
    public ResponseEntity<List<DisplayResourceDto>> findResources(@PathVariable Long id) {
        return ResponseEntity.ok(searchRunApplicationService.findResources(id));
    }
}
