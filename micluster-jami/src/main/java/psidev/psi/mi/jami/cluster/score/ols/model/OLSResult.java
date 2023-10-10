package psidev.psi.mi.jami.cluster.score.ols.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.net.URL;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class OLSResult {
    private final Embedded _embedded;
    private final Map<Link.Type, Link> _links;
    private final Page page;

    @Data
    @Builder
    @Jacksonized
    public static class Embedded {
        private final List<Term> terms;
    }

    @Data
    @Builder
    @Jacksonized
    public static class Term {
        private final String iri;
        private final String lang;
        private final List<String> description;
        private final List<String> synonyms;
        private final Map<String, List<String>> annotation;
        private final String label;
        private final String ontology_name;
        private final String ontology_prefix;
        private final URL ontology_iri;
        private final boolean is_obsolete;
        private final String term_replaced_by;
        private final boolean is_defining_ontology;
        private final boolean has_children;
        private final boolean is_root;
        private final String short_form;
        private final String obo_id;
        private final List<String> in_subset;
        private final List<DefinitionCitation> obo_definition_citation;
        private final List<XRef> obo_xref;
        private final List<Synonym> obo_synonym;
        private final boolean is_preferred_root;
        private final Map<Link.Type, Link> _links;
    }

    @Data
    @Builder
    @Jacksonized
    public static class DefinitionCitation {
        private final String definition;
        private final List<XRef> oboXrefs;
    }

    @Data
    @Builder
    @Jacksonized
    public static class XRef {
        private final String database;
        private final String id;
        private final String description;
        private final URL url;
    }

    @Data
    @Builder
    @Jacksonized
    public static class Synonym {
        private final String name;
        private final String scope;
        private final String type;
        private final List<XRef> xrefs;
    }

    @Data
    @Builder
    @Jacksonized
    public static class Link {
        public enum Type {
            self,
            parents,
            ancestors,
            hierarchicalParents,
            hierarchicalAncestors,
            jstree,
            children,
            descendants,
            hierarchicalChildren,
            hierarchicalDescendants,
            graph,

            first,
            prev,
            next,
            last;
        }

        private final URL href;
    }

    @Data
    @Builder
    @Jacksonized
    public static class Page {
        private final int size;
        private final int totalElements;
        private final int totalPages;
        private final int number;

    }
}
