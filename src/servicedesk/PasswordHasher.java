package servicedesk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {

    // Hash the password using the SHA-256 algorithm.
    public static String hashPassword(String password) {
        try {
            // Get an instance of MessageDigest with the SHA-256 algorithm. 
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString(); // Return hashed password
        } catch (NoSuchAlgorithmException e) {
            // Log the error
            e.printStackTrace();
            return null; // Return null if hashing algorithm is not available. 
        }
    }
}
