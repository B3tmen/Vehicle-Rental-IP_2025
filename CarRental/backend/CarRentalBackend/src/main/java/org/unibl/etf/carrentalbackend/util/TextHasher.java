package org.unibl.etf.carrentalbackend.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class TextHasher {
    private static final int BCRYPT_STRENGTH = 12;

    public static String getSHA256Hash(String plainText) {
        return DigestUtils.sha256Hex(plainText);
    }

    public static String getBCryptHash(String plainText) {
        String salt = BCrypt.gensalt(BCRYPT_STRENGTH);
        return BCrypt.hashpw(plainText, salt);
    }

    public static boolean verifyBCrypt(String plainText, String hash) {
        return BCrypt.checkpw(plainText, hash);
    }
}
