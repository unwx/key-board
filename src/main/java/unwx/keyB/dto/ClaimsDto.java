package unwx.keyB.dto;

import com.auth0.jwt.interfaces.Claim;

import java.util.Map;

public class ClaimsDto {

    private Map<String, Claim> claims;

    public ClaimsDto(Map<String, Claim> claims) {
        this.claims = claims;
    }

    public Map<String, Claim> getClaims() {
        return claims;
    }

    public void setClaims(Map<String, Claim> claims) {
        this.claims = claims;
    }
}
