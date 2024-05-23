
package com.bernardomg.security.authentication.jwt.token.test.config;

public final class JwtTokens {

    public static final String EMPTY        = "eyJhbGciOiJIUzUxMiJ9..BZwJ1TnPaPg1jkp7l8Y7qj-jHXWkTP6TXYAElGiAlFz-bWL6aPq8-T8nWAT0G_QkZYQ2bPizJdSPcNuAXCLwvQ";

    public static final String EXPIRED      = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MTY0Nzc3ODd9.S7VeWVlG1m1pRjuskTZDrckG9Kdv4k2qSQDjoJNT_QHn1mtEzfCkyuT5mMPn_I292WqFYNCSfwMCGk3zFgWduQ";

    public static final String WITH_ISSUER  = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJpc3N1ZXIifQ.PH60Xpv3vcGpi50DTUVZE0lGKi5r2wsJwLQWk2Vm_Wg8YBQdTfobnoYie8wfgsyKNoM_j21B_riBWMIyEcYTDA";

    public static final String WITH_SUBJECT = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJqZWN0In0.8fhjRsjPswScvhcPnN56SHmpKtqO53EpCmCIZg0SDp_sjeaMEAV4GfqPK5spRYdSZPKfdjz1FxvF7re9Sc6nZg";

    private JwtTokens() {
        super();
    }

}
