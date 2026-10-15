package mk.ukim.finki.mcptoolbackend.repository;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Extends {@link JpaSpecificationExecutor} so the paged, multi-criteria
 * resource browser can be implemented with type-safe {@code Specification}s.
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {
    List<Resource> findAllBySearchRunId(Long searchRunId);

    List<Resource> findAllByIdIn(List<Long> ids);

    long countByAnalysisIsNotNull();

    long countByDonationBatchIsNotNull();

    List<Resource> findAllBySourceUrlIn(List<String> sourceUrls);
}
