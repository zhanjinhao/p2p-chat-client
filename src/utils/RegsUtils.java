package utils;

import java.util.regex.Pattern;

public class RegsUtils {

	private static String emailReg = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

	public static boolean isValidEmail(String email) {
		return Pattern.matches(emailReg, email);
	}

}