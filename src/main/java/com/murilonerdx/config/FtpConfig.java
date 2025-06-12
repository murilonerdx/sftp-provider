package com.murilonerdx.config;


import com.murilonerdx.connection.FtpConnection;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.reactive.ReactiveClient;
import com.murilonerdx.sync.SyncClient;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.transfer.FtpFileTransfer;

import java.util.Objects;

public class FtpConfig {
	private String host;
	private int port = 22;
	private String user;
	private String password;

	public FtpConfig host(String host) {
		this.host = host;
		return this;
	}

	public FtpConfig port(int port) {
		this.port = port;
		return this;
	}

	public FtpConfig username(String user) {
		this.user = user;
		return this;
	}

	public FtpConfig password(String password) {
		this.password = password;
		return this;
	}

	public GateConnection build() {
		return new FtpConnection(host, port, user, password);
	}

	public FileTransfer transfer(GateConnection conn) throws Exception {
		if(Objects.isNull(host) || Objects.isNull(user) || Objects.isNull(password)) {
			throw new Exception("parameters cannot be null, host, password, username");
		}

		return new FtpFileTransfer(new FtpConnection(host, port, user, password));
	}

	public SyncClient client(FileTransfer transfer) {
		return new SyncClient(transfer);
	}

	public ReactiveClient reactiveClient(FileTransfer transfer) {
		return new ReactiveClient(transfer);
	}
}

