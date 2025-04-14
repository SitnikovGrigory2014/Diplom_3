package helpers;
import java.util.Random;

public class UserGenerator {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    public static String generateRandomName() {
        return "user_" + getRandomString(8);
    }

    public static String generateRandomEmail() {
        return "user_" + getRandomString(8) + "@example.com";
    }

    public static String generateRandomPassword(int length) {
        return getRandomString(length);
    }

    private static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
