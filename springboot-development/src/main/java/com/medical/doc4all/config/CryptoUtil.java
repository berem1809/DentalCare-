package com.medical.doc4all.config;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class CryptoUtil {
    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private final SecretKey masterKey;

    public CryptoUtil(byte[] masterKeyBytes) {
        this.masterKey = new SecretKeySpec(masterKeyBytes, AES);
    }

    // produce random IV
    public byte[] generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // returns Cipher initialized for encryption with IV
    public Cipher initEncryptCipher(byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, masterKey, spec);
        return cipher;
    }

    // returns Cipher for decryption with same iv
    public Cipher initDecryptCipher(byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, masterKey, spec);
        return cipher;
    }

    // compute sha256 hex (helper)
    public static String sha256Hex(InputStream in) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] buf = new byte[8192];
        int r;
        while ((r = in.read(buf)) != -1) md.update(buf, 0, r);
        byte[] digest = md.digest();
        return HexFormat.of().formatHex(digest);
    }
}

