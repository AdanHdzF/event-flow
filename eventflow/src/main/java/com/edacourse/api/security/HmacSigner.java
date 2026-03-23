package com.edacourse.api.security;

public interface HmacSigner {
	String sign(String payload, String secret);

	boolean verify(String payload, String secret, String signature);
}
