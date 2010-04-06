package org.nds.jam.security.encoding;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

public class SSHA {
	public static void main(String[] args) {
		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
		String encodedPassword = encoder.encodePassword(args[0], null);
		System.out.println("{SHA}" + encodedPassword);
	}
}
