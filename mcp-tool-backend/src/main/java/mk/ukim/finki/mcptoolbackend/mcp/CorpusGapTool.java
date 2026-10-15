package mk.ukim.finki.mcptoolbackend.mcp;

import java.util.List;
import mk.ukim.finki.mcptoolbackend.negotiation.NegotiationAgent;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class CorpusGapTool {
    private final NegotiationAgent agent;

    public CorpusGapTool(NegotiationAgent agent) {
        this.agent = agent;
    }

    @Tool(name = "find_corpus_gaps", description = "Finds topics missing from the donated corpus, searches the website "
            + "for them and prepares a DRAFT donation batch for human approval.")
    public List<NegotiationAgent.Message> findCorpusGaps() {
        return agent.negotiate();
    }
}