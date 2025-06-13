package com.murilonerdx.connection;

import com.jcraft.jsch.*;
import com.murilonerdx.exceptions.SftpException;

/**
 * Provides an implementation of {@link GateConnection} interface using the SFTP protocol.
 * <p>
 * This class manages the lifecycle of an SFTP connection using the JSch library, allowing
 * to connect and disconnect to an SFTP server, and to retrieve the active SFTP channel.
 * </p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * SftpConnection sftpConn = new SftpConnection("sftp.example.com", 22, "username", "password");
 * try {
 *     sftpConn.connect();
 *     ChannelSftp sftp = sftpConn.getChannel();
 *     // Perform SFTP operations with the channel
 * } catch (SftpException e) {
 *     // Handle SFTP errors
 * } finally {
 *     sftpConn.disconnect();
 * }
 * }</pre>
 *
 * <p><b>Note:</b> SSH host key checking is disabled by default for demonstration purposes.
 * Consider enabling strict host key checking for production environments.</p>
 *
 * @author MurilonerdX
 */
public class SftpConnection implements GateConnection {

	/** Remote SFTP server host. */
	private final String host;

	/** SFTP server port number. */
	private final int port;

	/** Username for SFTP authentication. */
	private final String username;

	/** Password for SFTP authentication. */
	private final String password;

	/** SSH session object. */
	private Session session;

	/** Active SFTP channel instance. */
	private ChannelSftp channelSftp;

	/**
	 * Constructs an {@code SftpConnection} with given server and user information.
	 *
	 * @param host     the SFTP server hostname or IP address
	 * @param port     the port to connect to
	 * @param username the authentication username
	 * @param password the authentication password
	 */
	public SftpConnection(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * Establishes an SFTP connection using the provided credentials.
	 *
	 * @throws SftpException if the connection or authentication fails
	 */
	@Override
	public void connect() throws SftpException {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
			// Host key checking is disabled for demonstration purposes
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(10_000);

			Channel channel = session.openChannel("sftp");
			channel.connect(5_000);

			channelSftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			throw new SftpException("Failed to connect to SFTP server", e);
		}
	}

	/**
	 * Disconnects the SFTP channel and the SSH session, releasing all resources.
	 */
	@Override
	public void disconnect() {
		if (channelSftp != null && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}
		if (session != null && session.isConnected()) {
			session.disconnect();
		}
	}

	/**
	 * Returns the connected {@link ChannelSftp} instance for file operations.
	 *
	 * @return the active {@link ChannelSftp}
	 * @throws SftpException if the SFTP channel is not connected
	 */
	@Override
	public ChannelSftp getChannel() throws SftpException {
		if (channelSftp == null || !channelSftp.isConnected()) {
			throw new SftpException("SFTP channel is not connected");
		}
		return channelSftp;
	}
}
