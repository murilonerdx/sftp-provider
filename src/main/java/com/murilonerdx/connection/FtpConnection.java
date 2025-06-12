package com.murilonerdx.connection;

import org.apache.commons.net.ftp.FTPClient;
public class FtpConnection {

	private final FTPClient client = new FTPClient();
	private final String host;
	private final int port;
	private final String user;
	private final String password;

	public FtpConnection(String host, int port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public void connect() throws Exception {
		client.connect(host, port);
		client.login(user, password);
		client.enterLocalPassiveMode();
		client.setFileType(FTPClient.BINARY_FILE_TYPE);
	}

	public void disconnect() throws Exception {
		if (client.isConnected()) {
			client.logout();
			client.disconnect();
		}
	}

	public FTPClient getClient() {
		return client;
	}
}

