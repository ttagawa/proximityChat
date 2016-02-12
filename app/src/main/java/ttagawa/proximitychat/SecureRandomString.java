package ttagawa.proximitychat;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by tyler on 2/11/16.
 */
public class SecureRandomString {
    private SecureRandom random = new SecureRandom();

    public String nextString() {
        return new BigInteger(130, random).toString(32);
    }

}
