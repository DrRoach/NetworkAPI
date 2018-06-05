package NetworkAPI.Encryption;

import NetworkAPI.Exceptions.ExceptionCodes;

import java.security.*;
import java.util.Base64;

;

public class Verifier {
    public boolean verifySignature(String signatureString) {
        byte[] decodedSignature = Base64.getDecoder().decode(signatureString);

        try {
            Signature signature = Signature.getInstance("SHA1withRSA");

            Key serverPublicKey = KeyHandler.readPublic("server");

            signature.initVerify((PublicKey) serverPublicKey);

            signature.update("SUPER_SECRET".getBytes());

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
