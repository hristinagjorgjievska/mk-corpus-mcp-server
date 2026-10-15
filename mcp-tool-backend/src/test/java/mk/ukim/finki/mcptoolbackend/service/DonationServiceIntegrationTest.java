package mk.ukim.finki.mcptoolbackend.service;

import mk.ukim.finki.mcptoolbackend.integration.vezilka.DonationReceipt;
import mk.ukim.finki.mcptoolbackend.integration.vezilka.TextDonationRequest;
import mk.ukim.finki.mcptoolbackend.integration.vezilka.VezilkaClient;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import mk.ukim.finki.mcptoolbackend.model.dto.CreateDonationBatchDto;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayDonationBatchDto;
import mk.ukim.finki.mcptoolbackend.model.enums.DonationStatus;
import mk.ukim.finki.mcptoolbackend.model.exception.InvalidDonationStateException;
import mk.ukim.finki.mcptoolbackend.repository.ResourceRepository;
import mk.ukim.finki.mcptoolbackend.repository.SearchRunRepository;
import mk.ukim.finki.mcptoolbackend.service.application.DonationApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
public class DonationServiceIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
        .withDatabaseName("mcptool_test")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @MockitoBean
    private VezilkaClient vezilkaClient;

    @Autowired
    private DonationApplicationService donationApplicationService;

    @Autowired
    private SearchRunRepository searchRunRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    void testDonationWorkflow() {

        SearchRun searchRun = searchRunRepository.save(new SearchRun("време"));
        Resource resource = resourceRepository.save(new Resource(
                searchRun,
                null,
                "Наслов",
                "Текст на македонски јазик",
                "https://example.mk/test"
        ));

        when(vezilkaClient.submitTextDonation(any()))
                .thenReturn(new DonationReceipt("ref-123", "Received"));

        DisplayDonationBatchDto created = donationApplicationService
                .create(new CreateDonationBatchDto(List.of(resource.getId()))).orElseThrow();

        assertThat(created.status()).isEqualTo(DonationStatus.DRAFT);
        assertThat(created.resourceIds()).containsExactly(resource.getId());

        assertThatThrownBy(() -> donationApplicationService.submit(created.id()))
                .isInstanceOf(InvalidDonationStateException.class);

        DisplayDonationBatchDto approved = donationApplicationService.approve(created.id()).orElseThrow();

        assertThat(approved.status()).isEqualTo(DonationStatus.APPROVED);

        DisplayDonationBatchDto submitted = donationApplicationService.submit(created.id()).orElseThrow();

        assertThat(submitted.status()).isEqualTo(DonationStatus.SUBMITTED);
        assertThat(submitted.vezilkaReference()).isEqualTo("ref-123");
        assertThat(submitted.submittedAt()).isNotNull();

        verify(vezilkaClient, times(1)).submitTextDonation(any());

        ArgumentCaptor<TextDonationRequest> argumentCaptor = ArgumentCaptor.forClass(TextDonationRequest.class);

        verify(vezilkaClient).submitTextDonation(argumentCaptor.capture());
        TextDonationRequest sent = argumentCaptor.getValue();

        assertThat(sent.content()).contains("Извор: example");
        assertThat(sent.sourceUrl()).isEqualTo("https://example.mk/test");
    }


    @Test
    void donationContentIsLimitedTo30Percent() {
        String longContent = "Ова е македонски текст за тестирање. ".repeat(100);

        SearchRun searchRun = searchRunRepository.save(new SearchRun("тест"));
        Resource resource = resourceRepository.save(new Resource(
                searchRun,
                null,
                "Долг текст",
                longContent,
                "https://example.mk/dolg"
        ));

        when(vezilkaClient.submitTextDonation(any()))
                .thenReturn(new DonationReceipt("ref-456", "Received"));

        DisplayDonationBatchDto created = donationApplicationService
                .create(new CreateDonationBatchDto(List.of(resource.getId()))).orElseThrow();
        donationApplicationService.approve(created.id());
        donationApplicationService.submit(created.id());

        ArgumentCaptor<TextDonationRequest> captor = ArgumentCaptor.forClass(TextDonationRequest.class);
        verify(vezilkaClient).submitTextDonation(captor.capture());
        String sent = captor.getValue().content();

        String excerpt = sent.substring(0, sent.indexOf("\n\nИзвор:"));
        assertThat(excerpt.length()).isLessThanOrEqualTo((int) (longContent.length() * 0.3) + 3);

        assertThat(sent).contains("Извор: example (example.mk)");
        assertThat(sent).endsWith("https://example.mk/dolg");
    }
}
