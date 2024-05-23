
package com.bernardomg.security.authentication.jwt.token.test.config;

public final class JwtTokens {

    public static final String EMPTY            = "eyJhbGciOiJIUzUxMiJ9..BZwJ1TnPaPg1jkp7l8Y7qj-jHXWkTP6TXYAElGiAlFz-bWL6aPq8-T8nWAT0G_QkZYQ2bPizJdSPcNuAXCLwvQ";

    public static final String EXPIRED          = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1ODA1MTE2MDB9.uQag93K8bdiHFFMJAx-9s0JTZ6-3fQOWfLbdXwimbg84A68BulI1RMftGZyZt7UmL_QoEQb3Z6lG_aL_4P_Kkg";

    public static final String WITH_AUDIENCE    = "eyJhbGciOiJIUzUxMiJ9.eyJhdWQiOlsiYXVkaWVuY2UiXX0.MC9oB7dYxYp6yrzZwKazSZ484NUwTQrs_sgAMeBdVPMpF0aU83GRF3fLD8FhiamG5f0yWH6LxsWUUARVqdA6pA";

    public static final String WITH_ISSUED_AT   = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE1ODA1MTE2MDB9.IhZC1OlKyZIDBf7v1_4Eo_yjzX6f4pujK4Rr4T-d3ArtSg0qzHhyPVImWhsMeT2BGh6ET03bfzPfty4BTlgfOQ";

    public static final String WITH_ISSUER      = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJpc3N1ZXIifQ.PH60Xpv3vcGpi50DTUVZE0lGKi5r2wsJwLQWk2Vm_Wg8YBQdTfobnoYie8wfgsyKNoM_j21B_riBWMIyEcYTDA";

    public static final String WITH_NOT_BEFORE  = "eyJhbGciOiJIUzUxMiJ9.eyJuYmYiOjE1ODA1MTE2MDB9.ykDKUQ8B2odCv9xjODCXmHhczE4JrDGp8VWQ0KEu3nf5nBXIiBOXpDP64k1CLajGFUJxHjRgQDO9CTpRzBvSQA";

    public static final String WITH_PERMISSIONS = "eyJhbGciOiJIUzUxMiJ9.eyJwZXJtaXNzaW9ucyI6eyJEQVRBIjpbIlJFQUQiXX19.QlVE0vPANQLHnkZtpnIQoMiFD2bvPzaeYn_ahAUckdXMX_IUg3f3IznK9lfCgjdsqFn5egtjpeOyKaHRX_e11A";

    public static final String WITH_SUBJECT     = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0In0.8fhjRsjPswScvhcPnN56SHmpKtqO53EpCmCIZg0SDp_sjeaMEAV4GfqPK5spRYdSZPKfdjz1FxvF7re9Sc6nZg";

    private JwtTokens() {
        super();
    }

}
