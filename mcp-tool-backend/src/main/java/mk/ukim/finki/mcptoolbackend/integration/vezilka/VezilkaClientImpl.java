package mk.ukim.finki.mcptoolbackend.integration.vezilka;

import mk.ukim.finki.mcptoolbackend.model.enums.DonationStatus;
import mk.ukim.finki.mcptoolbackend.model.exception.VezilkaIntegrationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class VezilkaClientImpl implements VezilkaClient{

    private static final String TEXT_PATH = "/api/public/v1/donations/text/";
    private static final String DONATION_PATH = "/api/public/v1/donations/{id}/";

    private final RestClient restClient;

    public VezilkaClientImpl(VezilkaProperties properties, RestClient.Builder builder){

        this.restClient = builder
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Donation-Api-Key", properties.apiKey())
                .build();
    }

    @Override
    public DonationReceipt submitTextDonation(TextDonationRequest request) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("source_url", request.sourceUrl());
        item.put("text", request.content());
        item.put("page_title", request.title());
        item.put("page_language", "mk");
        item.put("retrieved_at", Instant.now().toString());

        try{
            Map response = restClient.post()
                    .uri(TEXT_PATH)
                    .body(Map.of("items", List.of(item)))
                    .retrieve()
                    .body(Map.class);

            Map result = ((List<Map>) response.get("results")).getFirst();

            if ("accepted".equals(result.get("status")))
                return new DonationReceipt((String) result.get("id"), "accepted");

            return new DonationReceipt(null, (String) result.get("rejectionReason"));
        }
        catch (RestClientException exception){
            throw new VezilkaIntegrationException("Донацијата кон Везилка е неуспешна.", exception);
        }
    }

    @Override
    public DonationStatus checkStatus(String vezilkaReference) {
        try{
            Map result = restClient.get()
                    .uri(DONATION_PATH, vezilkaReference)
                    .retrieve()
                    .body(Map.class);

            String status;
            if (result == null) {
                status = null;
            }
            else {
                status = (String) result.get("status");
            }

            if ("accepted".equals(status))
                return DonationStatus.ACCEPTED;
            else if ("rejected".equals(status)){
                return DonationStatus.REJECTED;
            }
            else {
                return DonationStatus.SUBMITTED;
            }
        }
        catch (RestClientException exception){
            throw new VezilkaIntegrationException("Проверката на статусот кај Везилка е неуспешна.", exception);
        }
    }
}
