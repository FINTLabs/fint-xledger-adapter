package no.fint.xledger.graphql;

public abstract class GraphQLRepository {
    protected String quote(String input) {
        return String.format("\"%s\"", input);
    }

    protected String nullOrQuote(String input) {
        if (input == null) return "null";
        return quote(input);
    }
}
