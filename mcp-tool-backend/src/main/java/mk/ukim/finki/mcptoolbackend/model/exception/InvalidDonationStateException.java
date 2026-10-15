package mk.ukim.finki.mcptoolbackend.model.exception;

import mk.ukim.finki.mcptoolbackend.model.enums.DonationStatus;

public class InvalidDonationStateException extends RuntimeException {
    public InvalidDonationStateException(Long id, DonationStatus status) {
        super("The donation batch with id %d cannot perform this operation in status %s.".formatted(id, status));
    }
}
