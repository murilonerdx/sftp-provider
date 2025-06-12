package com.murilonerdx.config;


import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.sync.SyncClient;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.transfer.SftpFileTransfer;

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

	public GateConnection build() {
		return new SftpConnection(host, port, username, password);
	}

	public FileTransfer transfer(GateConnection conn) {
		return new SftpFileTransfer(conn);
	}

	public SyncClient client(FileTransfer transfer) {
		return new SyncClient(transfer);
	}
}

