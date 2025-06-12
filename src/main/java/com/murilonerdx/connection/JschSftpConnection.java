package com.murilonerdx.connection;

import com.jcraft.jsch.*;
import com.murilonerdx.exceptions.SftpException;

public class JschSftpConnection implements SftpConnection {

	private final String host;
	private final int port;
	private final String username;
	private final String password;

	private Session session;
	private ChannelSftp channelSftp;

	public JschSftpConnection(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	@Override
	public void connect() throws SftpException {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(10_000);

			Channel channel = session.openChannel("sftp");
			channel.connect(5_000);

			channelSftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			throw new SftpException("Falha ao conectar SFTP", e);
		}
	}

	@Override
	public void disconnect() {
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	@Override
	public ChannelSftp getChannel() throws SftpException {
		if (channelSftp == null || !channelSftp.isConnected()) {
			throw new SftpException("Canal SFTP não está conectado");
		}
		return channelSftp;
	}
}

