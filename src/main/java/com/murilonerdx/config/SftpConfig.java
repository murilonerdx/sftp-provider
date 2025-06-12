package com.murilonerdx.config;


import com.murilonerdx.connection.JschSftpConnection;
import com.murilonerdx.connection.SftpConnection;

public class SftpConfig {
	private String host;
	private int port = 22;
	private String username;
	private String password;

	public SftpConfig host(String host) {
		this.host = host;
		return this;
	}

	public SftpConfig port(int port) {
		this.port = port;
		return this;
	}

	public SftpConfig username(String username) {
		this.username = username;
		return this;
	}

	public SftpConfig password(String password) {
		this.password = password;
		return this;
	}

	public SftpConnection buildConnection() {
		return new JschSftpConnection(host, port, username, password);
	}
}

