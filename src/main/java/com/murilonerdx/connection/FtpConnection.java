package com.murilonerdx.connection;

import com.jcraft.jsch.ChannelSftp;
import com.murilonerdx.exceptions.SftpException;
import org.apache.commons.net.ftp.*;

/**
 * Represents an FTP connection implementation for the {@link GateConnection} interface.
 * <p>
 * This class allows creating and managing FTP connections using the Apache Commons Net
 * {@link FTPClient}. It provides methods for connecting and disconnecting to remote FTP servers,
 * as well as retrieving the underlying {@link FTPClient} instance.
 * </p>
 * <p>
 * <b>Note:</b> The method {@link #getChannel()} returns {@code null} because FTP does not use the
 * SFTP protocol.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * try (FtpConnection ftpConnection = new FtpConnection("ftp.example.com", 21, "user", "password")) {
 *     FTPClient client = ftpConnection.getClient();
 *     // Use the FTPClient as needed
 * } catch (Exception e) {
 *     // handle exception
 * }
 * }</pre>
 *
 * @author MurilonerdX
 */
public class FtpConnection implements GateConnection {

	/** The FTP client instance from Apache Commons Net. */
	private final FTPClient client = new FTPClient();

	/** FTP server host address. */
	private final String host;

	/** FTP server port. */
	private final int port;

	/** Username for authentication. */
	private final String user;

	/** Password for authentication. */
	private final String password;

	/**
	 * Creates a new {@code FtpConnection} and establishes a connection.
	 *
	 * @param host     the FTP server hostname or IP address
	 * @param port     the port to connect to
	 * @param user     the username for authentication
	 * @param password the password for authentication
	 * @throws Exception if any error occurs while connecting
	 */
	public FtpConnection(String host, int port, String user, String password) throws Exception {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;

		connect();
	}

	/**
	 * Establishes a connection to the FTP server and logs in with the provided credentials.
	 *
	 * @throws Exception if unable to connect or login
	 */
	@Override
	public void connect() throws Exception {
		client.connect(host, port);
		client.login(user, password);
		client.enterLocalPassiveMode();
		client.setFileType(FTPClient.BINARY_FILE_TYPE);
	}

	/**
	 * Logs out and disconnects from the FTP server, if connected.
	 *
	 * @throws Exception if an error occurs while disconnecting
	 */
	@Override
	public void disconnect() throws Exception {
		if (client.isConnected()) {
			client.logout();
			client.disconnect();
		}
	}

	/**
	 * Returns {@code null} as SFTP channels are not supported in FTP connections.
	 *
	 * @return always {@code null}
	 */
	@Override
	public ChannelSftp getChannel() throws SftpException {
		return null;
	}

	/**
	 * Returns the underlying {@link FTPClient} instance for advanced operations.
	 *
	 * @return the current {@link FTPClient}
	 */
	public FTPClient getClient() {
		return client;
	}
}
