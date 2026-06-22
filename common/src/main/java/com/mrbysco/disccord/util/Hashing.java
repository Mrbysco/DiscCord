package com.mrbysco.disccord.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
	/**
	 * This method is used to generate SHA-256 hash of the input string
	 *
	 * @param input String to hash
	 * @return byte[] of the hashed string
	 * @throws NoSuchAlgorithmException if the algorithm is not found
	 */
	public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
		// Static getInstance method is called with hashing SHA
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		// digest() method called
		// to calculate message digest of an input
		// and return array of byte
		return md.digest(input.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * This method converts the byte array to a hex string
	 *
	 * @param hash byte[] to convert
	 * @return String of the hex value
	 */
	public static String toHexString(byte[] hash) {
		// Convert byte array into signum representation
		BigInteger number = new BigInteger(1, hash);

		// Convert message digest into hex value
		StringBuilder hexString = new StringBuilder(number.toString(16));

		// Pad with leading zeros
		while (hexString.length() < 64) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
	}

	/**
	 * This method is used to generate SHA-256 hash of the input string
	 *
	 * @param input String to hash
	 * @return String of the hashed input
	 */
	public static String Sha256(String input) {
		try {
			return toHexString(getSHA(input));
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}
}
