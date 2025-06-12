package com.murilonerdx.connection;

import com.jcraft.jsch.ChannelSftp;
import com.murilonerdx.exceptions.SftpException;
import org.apache.commons.net.ftp.*;
public class FtpConnection implements GateConnection{

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

	@Override
	public void connect() throws Exception {
		client.connect(host, port);
		client.login(user, password);
		client.enterLocalPassiveMode();
		client.setFileType(FTPClient.BINARY_FILE_TYPE);
	}

	@Override
	public void disconnect() throws Exception {
		if (client.isConnected()) {
			client.logout();
			client.disconnect();
		}
	}

	@Override
	public ChannelSftp getChannel() throws SftpException {
		return null;
	}

	public FTPClient getClient() {
		return client;
	}
}

