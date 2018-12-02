package com.project.EMRChain.Payload.Auth;


public class JwtAuthenticationResponse
{
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
