package mk.ukim.finki.mcptoolbackend.repository;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.model.domain.SearchRun;
import mk.ukim.finki.mcptoolbackend.model.enums.SearchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRunRepository extends JpaRepository<SearchRun, Long> {
    List<SearchRun> findAllByOrderByCreatedAtDesc();

    List<SearchRun> findAllByStatus(SearchStatus status);
}
