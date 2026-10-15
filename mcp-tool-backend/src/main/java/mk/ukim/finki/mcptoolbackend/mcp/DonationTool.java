package mk.ukim.finki.mcptoolbackend.mcp;

import mk.ukim.finki.mcptoolbackend.model.dto.CreateDonationBatchDto;
import mk.ukim.finki.mcptoolbackend.model.dto.DisplayDonationBatchDto;
import mk.ukim.finki.mcptoolbackend.service.application.DonationApplicationService;
import mk.ukim.finki.mcptoolbackend.service.domain.ToolInvocationLogService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MCP tool for donating a resource to doniraj.vezilka.ai.
 */
@Service
public class DonationTool {
    private final DonationApplicationService donationApplicationService;
    private final ToolInvocationLogService toolInvocationLogService;

    public DonationTool(DonationApplicationService donationApplicationService,
                        ToolInvocationLogService toolInvocationLogService) {
        this.donationApplicationService = donationApplicationService;
        this.toolInvocationLogService = toolInvocationLogService;
    }

    @Tool(name = "donate_resource",
        description = "Donate a single stored resource to doniraj.vezilka.ai by creating a donation batch, "
            + "approving it and submitting it. Returns the resulting donation batch with its Vezilka reference.")
    public DisplayDonationBatchDto donateResource(
        @ToolParam(description = "The id of the stored resource to donate") Long id) {
        DisplayDonationBatchDto created = donationApplicationService
                .create(new CreateDonationBatchDto(List.of(id)))
                .orElseThrow();

        donationApplicationService.approve(created.id());

        DisplayDonationBatchDto result = donationApplicationService.submit(created.id()).orElseThrow();

        toolInvocationLogService.log(
                "donate_resource",
                String.format("{\"id\":%d}", id),
                String.format("batchId=%d, status=%s, reference=%s", result.id(), result.status(), result.vezilkaReference()),
                true,
                null
        );

        return result;
    }
}
