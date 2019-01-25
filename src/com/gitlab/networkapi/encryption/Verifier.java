package com.gitlab.networkapi.encryption;

import com.gitlab.networkapi.config.Config;
import com.gitlab.networkapi.encryption.setup.Setup;
import com.gitlab.networkapi.exceptions.ExceptionCodes;

import java.security.*;
import java.security.Signature;
import java.util.Base64;

public class Verifier {
    public boolean verifySignature(String signatureString) {
        byte[] decodedSignature = Base64.getDecoder().decode(signatureString);

        try {
            Signature signature = Signature.getInstance("SHA1withRSA");

            Key serverPublicKey = KeyHandler.readPublic(Setup.KEY_TYPE.SERVER, Setup.keyFile);

            signature.initVerify((PublicKey) serverPublicKey);

            signature.update(Config.encryptionSignature.getBytes());

            return signature.verify(decodedSignature);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.NO_SUCH_ALGORITHM);
        } catch (InvalidKeyException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.INVALID_KEY);
        } catch (SignatureException ex) {
            System.out.println(ex.getMessage());
            System.exit(ExceptionCodes.SIGNATURE_EXCEPTION);
        }

        return false;
    }
}
