package mk.ukim.finki.mcptoolbackend.web.controller;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.dto.CreateDonationBatchDto;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayDonationBatchDto;
import mk.ukim.finki.mcptoolbackend.service.application.DonationApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The donation API: build batches of resources and push them through the
 * DRAFT -> APPROVED -> SUBMITTED workflow towards doniraj.vezilka.ai.
 */
@RestController
@RequestMapping("/api/donations")
public class DonationController {
    private final DonationApplicationService donationApplicationService;

    public DonationController(DonationApplicationService donationApplicationService) {
        this.donationApplicationService = donationApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<DisplayDonationBatchDto>> findAll() {
        return ResponseEntity.ok(donationApplicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayDonationBatchDto> findById(@PathVariable Long id) {
        return donationApplicationService
            .findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<DisplayDonationBatchDto> add(@RequestBody CreateDonationBatchDto request) {
        return donationApplicationService
            .create(request)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<DisplayDonationBatchDto> approve(@PathVariable Long id) {
        return donationApplicationService
            .approve(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<DisplayDonationBatchDto> submit(@PathVariable Long id) {
        return donationApplicationService
            .submit(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
