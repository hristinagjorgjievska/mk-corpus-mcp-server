package mk.ukim.finki.mcptoolbackend.repository;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.domain.ToolInvocationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolInvocationLogRepository extends JpaRepository<ToolInvocationLog, Long> {
    List<ToolInvocationLog> findAllByOrderByOccurredAtDesc();

    List<ToolInvocationLog> findAllBySearchRunIdOrderByOccurredAtAsc(Long searchRunId);
}
