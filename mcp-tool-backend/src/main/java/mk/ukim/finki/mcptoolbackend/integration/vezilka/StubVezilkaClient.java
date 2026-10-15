package mk.ukim.finki.mcptoolbackend.integration.vezilka;

import mk.ukim.finki.mcptoolbackend.model.enums.DonationStatus;
import org.springframework.stereotype.Component;

/**
 * Placeholder so the application boots before the assignment is implemented.
 */
//@Component
public class StubVezilkaClient implements VezilkaClient {
    private final VezilkaProperties vezilkaProperties;

    public StubVezilkaClient(VezilkaProperties vezilkaProperties) {
        this.vezilkaProperties = vezilkaProperties;
    }

    @Override
    public DonationReceipt submitTextDonation(TextDonationRequest request) {
        throw new UnsupportedOperationException("Not implemented: VezilkaClient.submitTextDonation().");
    }

    @Override
    public DonationStatus checkStatus(String vezilkaReference) {
        throw new UnsupportedOperationException("Not implemented: VezilkaClient.checkStatus().");
    }
}
