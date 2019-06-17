/*
 *   Copyright © 2017-2019 PSPDFKit GmbH. All rights reserved.
 *
 *   THIS SOURCE CODE AND ANY ACCOMPANYING DOCUMENTATION ARE PROTECTED BY INTERNATIONAL COPYRIGHT LAW
 *   AND MAY NOT BE RESOLD OR REDISTRIBUTED. USAGE IS BOUND TO THE PSPDFKIT LICENSE AGREEMENT.
 *   UNAUTHORIZED REPRODUCTION OR DISTRIBUTION IS SUBJECT TO CIVIL AND CRIMINAL PENALTIES.
 *   This notice may not be removed from this file.
 */

package com.example.camasyssign;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class StringUtils {

    /**
     * Returns a SHA-1 hash of passed in string.
     * @param string String to be hashed.
     * @return HEX SHA-1
     */
    public static String sha1(String string) {
        try {
            byte[] data = string.getBytes();
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(data, 0, data.length);
            return byteToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Device is missing SHA-1!");
        }
    }

    /**
     * Converts byte array to hex string.
     */
    public static String byteToHex(final byte[] array) {
        Formatter formatter = new Formatter();
        for (byte b : array) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
