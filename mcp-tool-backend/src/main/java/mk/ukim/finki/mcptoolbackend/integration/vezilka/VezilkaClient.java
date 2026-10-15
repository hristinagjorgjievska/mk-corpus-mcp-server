package mk.ukim.finki.mcptoolbackend.integration.vezilka;

import mk.ukim.finki.mcptoolbackend.model.enums.DonationStatus;

/**
 * The integration seam towards <a href="https://doniraj.vezilka.ai">doniraj.vezilka.ai</a>.
 */
public interface VezilkaClient {
    /**
     * Submits one text donation and returns Vezilka's receipt for it.
     */
    DonationReceipt submitTextDonation(TextDonationRequest request);

    /**
     * Checks what happened to a previously submitted donation.
     *
     * @param vezilkaReference the reference from the {@link DonationReceipt}
     */
    DonationStatus checkStatus(String vezilkaReference);
}
