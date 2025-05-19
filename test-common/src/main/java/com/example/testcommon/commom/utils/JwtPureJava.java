package com.example.testcommon.commom.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtPureJava {
    private static final String SECRET = "your-256-bit-secret";
    private static final long EXPIRATION_MS = 86400000; // 24小时

    public static void main(String[] args) {
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", "123");
        claims.put("role", "admin");

        String token = generateToken(claims);
        System.out.println("Generated Token: " + token);

        boolean isValid = validateToken(token);
        System.out.println("Token valid: " + isValid);
    }

    public static String generateToken(Map<String, String> claims) {
        // Header
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String encodedHeader = base64UrlEncode(header.getBytes());

        // Payload
        long currentTime = System.currentTimeMillis();
        String payload = String.format(
                "{\"iat\":%d,\"exp\":%d,\"userId\":\"%s\",\"role\":\"%s\"}",
                currentTime,
                currentTime + EXPIRATION_MS,
                claims.get("userId"),
                claims.get("role")
        );
        String encodedPayload = base64UrlEncode(payload.getBytes());

        // Signature
        String signingInput = encodedHeader + "." + encodedPayload;
        String signature = hmacSha256(signingInput, SECRET);

        return signingInput + "." + signature;
    }

    public static boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            // Verify signature
            String signingInput = parts[0] + "." + parts[1];
            String signature = hmacSha256(signingInput, SECRET);
            if (!signature.equals(parts[2])) {
                return false;
            }

            // Verify expiration
            String payload = new String(base64UrlDecode(parts[1]));
            long exp = Long.parseLong(payload.split("\"exp\":")[1].split(",")[0]);
            return System.currentTimeMillis() < exp;
        } catch (Exception e) {
            return false;
        }
    }

    private static String hmacSha256(String data, String key) {
        try {
            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            sha256.init(secretKey);
            byte[] hash = sha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64UrlEncode(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC", e);
        }
    }

    private static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    private static byte[] base64UrlDecode(String data) {
        return Base64.getUrlDecoder().decode(data);
    }
}
