package com.murilonerdx.connection;

import com.jcraft.jsch.ChannelSftp;
import com.murilonerdx.exceptions.SftpException;

/**
 * Represents a generic connection contract for file transfer protocols such as FTP and SFTP.
 * <p>
 * Implementations of this interface provide methods to connect and disconnect from a remote
 * file server, as well as retrieving a channel (for example, an SFTP channel).
 * </p>
 *
 * <p>
 * Typical implementations include {@link com.murilonerdx.connection.FtpConnection} and
 * {@link com.murilonerdx.connection.SftpConnection}.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * GateConnection connection = new SftpConnection("sftp.example.com", 22, "user", "pass");
 * connection.connect();
 * ChannelSftp channel = connection.getChannel();
 * // Perform operations...
 * connection.disconnect();
 * }</pre>
 *
 * @author MurilonerdX
 */
public interface GateConnection {

	/**
	 * Establishes a connection to the remote server.
	 *
	 * @throws SftpException if a SFTP-specific error occurs
	 * @throws Exception if a general connection error occurs
	 */
	void connect() throws SftpException, Exception;

	/**
	 * Closes the connection to the remote server and releases any resources.
	 *
	 * @throws Exception if an error occurs during disconnection
	 */
	void disconnect() throws Exception;

	/**
	 * Retrieves the SFTP channel for file operations.
	 * <p>
	 * For FTP implementations, this method may return {@code null} as SFTP is not supported.
	 * </p>
	 *
	 * @return a {@link ChannelSftp} instance, or {@code null} if not applicable
	 * @throws SftpException if an error occurs when retrieving the SFTP channel
	 */
	ChannelSftp getChannel() throws SftpException;
}
