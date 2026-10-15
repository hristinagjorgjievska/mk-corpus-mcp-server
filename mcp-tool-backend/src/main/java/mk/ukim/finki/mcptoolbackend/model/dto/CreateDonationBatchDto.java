package mk.ukim.finki.mcptoolbackend.model.dto;

import java.util.List;

/**
 * Request body for {@code POST /api/donations/add}: the ids of the resources
 * to group into a new (DRAFT) donation batch.
 */
public record CreateDonationBatchDto(
    List<Long> resourceIds
) {
}
