package psidev.psi.mi.jami.cluster.score.ols.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class SimpleOLSNode {
    private final String name;
    private final String id;
    private final List<SimpleOLSNode> children;
}
