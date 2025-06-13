package com.murilonerdx.config;

import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.sync.SyncClient;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.transfer.SftpFileTransfer;

/**
 * Configuration class for creating SFTP connections and file transfer clients.
 * <p>
 * This class allows you to configure the host, port, username, and password
 * for an SFTP server and provides utility methods to build a connection and
 * create file transfer and synchronization clients.
 * </p>
 */
public class SftpConfig {
	private String host;
	private int port = 22;
	private String username;
	private String password;

	/**
	 * Sets the SFTP server host.
	 *
	 * @param host the host address.
	 * @return the current {@link SftpConfig} instance.
	 */
	public SftpConfig host(String host) {
		this.host = host;
		return this;
	}

	/**
	 * Sets the SFTP server port. Defaults to 22 if not explicitly set.
	 *
	 * @param port the port number.
	 * @return the current {@link SftpConfig} instance.
	 */
	public SftpConfig port(int port) {
		this.port = port;
		return this;
	}

	/**
	 * Sets the SFTP username.
	 *
	 * @param username the username.
	 * @return the current {@link SftpConfig} instance.
	 */
	public SftpConfig username(String username) {
		this.username = username;
		return this;
	}

	/**
	 * Sets the SFTP password.
	 *
	 * @param password the password.
	 * @return the current {@link SftpConfig} instance.
	 */
	public SftpConfig password(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Builds and returns a {@link GateConnection} instance using the SFTP protocol.
	 *
	 * @return a {@link SftpConnection} configured with the current parameters.
	 */
	public GateConnection build() {
		return new SftpConnection(host, port, username, password);
	}

	/**
	 * Creates a {@link FileTransfer} implementation based on the given SFTP connection.
	 *
	 * @param conn an established {@link GateConnection} (SFTP).
	 * @return a {@link SftpFileTransfer} instance for transferring files.
	 * @throws Exception if the transfer initialization fails.
	 */
	public FileTransfer transfer(GateConnection conn) throws Exception {
		return new SftpFileTransfer(conn);
	}

	/**
	 * Creates a new {@link SyncClient} using the given file transfer implementation.
	 *
	 * @param transfer the {@link FileTransfer} to use for synchronization.
	 * @return a {@link SyncClient} for performing synchronous file operations.
	 */
	public SyncClient client(FileTransfer transfer) {
		return new SyncClient(transfer);
	}
}
