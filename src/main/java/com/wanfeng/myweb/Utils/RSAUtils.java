package com.wanfeng.myweb.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RSAUtils {
    public String encode() throws Exception {
        return getCorrespondingPath(System.currentTimeMillis());
    }

    public String getCorrespondingPath(long timestamp) throws Exception {
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLgd2OAkcGVtoE3ThUREbio0Eg\n" +
                "Uc/prcajMKXvkCKFCWhJYJcLkcM2DKKcSeFpD/j6Boy538YXnR6VhcuUJOhH2x71\n" +
                "nzPjfdTcqMz7djHum0qSZA0AyCBDABUqCrfNgCiJ00Ra7GmRj+YCK1NJEuewlb40\n" +
                "JNrRuoEUXpabUzGB8QIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        byte[] decodedBytes = Base64.getDecoder().decode(publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replace("\n", "").trim());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec spec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, spec);

        byte[] encryptedBytes = cipher.doFinal(String.format("refresh_%d", timestamp).getBytes(StandardCharsets.UTF_8));
        return Arrays.toString(encryptedBytes).replace(" ", "").replaceAll(".", "%02x");
    }
}