package org.nds.mail.service.gmail;

public final class GmailHelper {

	public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	public enum GmailProtocol {
		IMAP("imaps", "imap.gmail.com", 993), POP3("pop3", "pop.gmail.com", 995);

		private final String protocol;
		private final String host;
		private final int port;

		private GmailProtocol(String protocol, String host, int port) {
			this.protocol = protocol;
			this.host = host;
			this.port = port;
		}

		public String getProtocol() {
			return protocol;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}
	}
}
