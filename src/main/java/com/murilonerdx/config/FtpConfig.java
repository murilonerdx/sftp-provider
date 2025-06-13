package com.murilonerdx.config;

import com.murilonerdx.connection.FtpConnection;
import com.murilonerdx.reactive.ReactiveClient;
import com.murilonerdx.sync.SyncClient;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.transfer.FtpFileTransfer;

import java.util.Objects;

/**
 * Configuration class for creating FTP connections and file transfer clients.
 * <p>
 * This class allows you to configure the FTP host, port, username, and password
 * in order to establish a connection and instantiate file transfer tools.
 * </p>
 *
 * <p>
 * Supports both synchronous ({@link SyncClient}) and reactive ({@link ReactiveClient}) clients.
 * </p>
 */
public class FtpConfig {
	private String host;
	private int port = 22;
	private String user;
	private String password;

	/**
	 * Creates a new FTP configuration with the specified host, port, username, and password.
	 *
	 * @param host     FTP server host.
	 * @param port     FTP server port.
	 * @param user     Username for FTP login.
	 * @param password Password for FTP login.
	 */
	public FtpConfig(String host, int port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	/**
	 * Default constructor.
	 */
	public FtpConfig() {
	}

	/**
	 * Starts building a new {@link FtpConfig} instance using a builder pattern.
	 *
	 * @return a new instance of {@link FtpConfig}.
	 */
	public static FtpConfig newBuilder() {
		return new FtpConfig();
	}

	/**
	 * Sets the FTP server host.
	 *
	 * @param host the host address.
	 * @return the current {@link FtpConfig} instance.
	 */
	public FtpConfig host(String host) {
		this.host = host;
		return this;
	}

	/**
	 * Sets the FTP server port.
	 *
	 * @param port the port number.
	 * @return the current {@link FtpConfig} instance.
	 */
	public FtpConfig port(int port) {
		this.port = port;
		return this;
	}

	/**
	 * Sets the FTP username.
	 *
	 * @param user the username.
	 * @return the current {@link FtpConfig} instance.
	 */
	public FtpConfig username(String user) {
		this.user = user;
		return this;
	}

	/**
	 * Sets the FTP password.
	 *
	 * @param password the password.
	 * @return the current {@link FtpConfig} instance.
	 */
	public FtpConfig password(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Builds and returns a new {@link FtpConnection} using the configured parameters.
	 *
	 * @return an active {@link FtpConnection}.
	 * @throws Exception if any required parameter is missing or the connection fails.
	 */
	public FtpConnection build() throws Exception {
		return new FtpConnection(host, port, user, password);
	}

	/**
	 * Creates a new {@link FileTransfer} instance based on the current FTP configuration.
	 *
	 * @return a ready-to-use {@link FileTransfer} instance.
	 * @throws Exception if host, username, or password are missing.
	 */
	public FileTransfer transfer() throws Exception {
		if (Objects.isNull(host) || Objects.isNull(user) || Objects.isNull(password)) {
			throw new Exception("Parameters cannot be null: host, username, password");
		}

		FtpConnection connection = new FtpConnection(host, port, user, password);
		return new FtpFileTransfer(connection);
	}

	/**
	 * Creates a new {@link SyncClient} using the provided {@link FileTransfer} instance.
	 *
	 * @param transfer the {@link FileTransfer} to be used by the client.
	 * @return a new instance of {@link SyncClient}.
	 */
	public SyncClient client(FileTransfer transfer) {
		return new SyncClient(transfer);
	}

	/**
	 * Creates a new {@link ReactiveClient} using the provided {@link FileTransfer} instance.
	 *
	 * @param transfer the {@link FileTransfer} to be used by the client.
	 * @return a new instance of {@link ReactiveClient}.
	 */
	public ReactiveClient reactiveClient(FileTransfer transfer) {
		return new ReactiveClient(transfer);
	}
}
