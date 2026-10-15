package mk.ukim.finki.mcptoolbackend.repository;

import jakarta.transaction.Transactional;
import mk.ukim.finki.mcptoolbackend.config.JpaConfig;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@Transactional
@Testcontainers
public class SearchRunRepositoryTest {
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

    @Autowired
    private SearchRunRepository searchRunRepository;

    @Test
    void testFindAllByOrderByCreatedAtDesc() throws InterruptedException{
        searchRunRepository.saveAndFlush(new SearchRun("време"));
        Thread.sleep(10);
        searchRunRepository.saveAndFlush(new SearchRun("спорт"));
        Thread.sleep(10);
        searchRunRepository.saveAndFlush(new SearchRun("избори"));

        List<SearchRun> result = searchRunRepository.findAllByOrderByCreatedAtDesc();

        assertThat(result).extracting(SearchRun::getQuery)
                .containsExactly("избори", "спорт", "време");

    }
}
