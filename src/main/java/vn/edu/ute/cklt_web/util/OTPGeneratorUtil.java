package vn.edu.ute.cklt_web.util;

import java.security.SecureRandom;

public final class OTPGeneratorUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int OTP_BOUND = 1_000_000;

    private OTPGeneratorUtil() {
    }

    public static String generateOtp() {
        return String.format("%06d", SECURE_RANDOM.nextInt(OTP_BOUND));
    }
}
