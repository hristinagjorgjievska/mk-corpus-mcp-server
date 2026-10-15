package mk.ukim.finki.mcptoolbackend.web.controller;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.negotiation.NegotiationAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/negotiation")
public class NegotiationController {
    private final NegotiationAgent negotiationAgent;

    public NegotiationController(NegotiationAgent negotiationAgent) {
        this.negotiationAgent = negotiationAgent;
    }

    @PostMapping
    public List<NegotiationAgent.Message> negotiate() {
        return negotiationAgent.negotiate();
    }
}