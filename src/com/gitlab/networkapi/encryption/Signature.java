package com.gitlab.networkapi.encryption;

import com.gitlab.networkapi.exceptions.ExceptionCodes;

import java.security.*;
import java.util.Base64;

public class Signature {
    public static byte[] generateSignature(Key privateKey, String signatureString) {
        try {
            java.security.Signature signature = java.security.Signature.getInstance("SHA1withRSA");
            signature.initSign((PrivateKey) privateKey, new SecureRandom());

            byte[] signatureStringBytes = signatureString.getBytes();

            signature.update(signatureStringBytes);

            byte[] signatureBytes = signature.sign();

            return Base64.getEncoder().encode(signatureBytes);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.NO_SUCH_ALGORITHM);
        } catch (InvalidKeyException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.INVALID_SIGNATURE_PRIVATE_KEY);
        } catch (SignatureException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.SIGNATURE_SIGN_EXCEPTION);
        }

        // If we get here then return empty signature
        return new byte[0];
    }
}
