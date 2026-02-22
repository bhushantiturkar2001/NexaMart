package com.nexamart.utils;

import java.util.Random;

public class OtpUtil {

	public static String generateOtp() {
		int otpLength = 6;

		Random random = new Random();

		StringBuilder otp = new StringBuilder(otpLength); // StringBuilder to efficiently build the OTP Not thread safe

		for (int i = 0; i < otpLength; i++) {
			otp.append(random.nextInt(10)); // Append a random digit (0-9)
		}

		return otp.toString();
	}

}
