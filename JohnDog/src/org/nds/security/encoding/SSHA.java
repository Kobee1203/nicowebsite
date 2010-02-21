package org.nds.security.encoding;

import org.springframework.security.providers.encoding.ShaPasswordEncoder;

public class SSHA {
	public static void main(String[] args) {
		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
		String encodedPassword = encoder.encodePassword(args[0],null);
		System.out.println("{SHA}"+encodedPassword);
	}
}
