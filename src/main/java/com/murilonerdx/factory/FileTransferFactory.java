package com.murilonerdx.factory;

import com.murilonerdx.connection.FtpConnection;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.exceptions.FileTransferException;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.transfer.FtpFileTransfer;
import com.murilonerdx.transfer.SftpFileTransfer;

/**
 * Factory class responsible for creating appropriate {@link FileTransfer} instances
 * based on the specified protocol.
 * <p>
 * This factory provides a centralized way to create the correct file transfer implementation
 * without having to directly instantiate the concrete classes. It supports FTP and SFTP
 * protocols and can be extended to support additional protocols in the future.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create a connection
 * GateConnection connection = new FtpConnection("ftp.example.com", 21, "user", "pass");
 *
 * // Create the appropriate file transfer implementation
 * FileTransfer transfer = FileTransferFactory.create("ftp", connection);
 *
 * // Use the transfer object
 * transfer.uploadFile("/local/path/file.txt", "/remote/path/file.txt");
 * }</pre>
 *
 * @author MurilonerdX
 */
public class FileTransferFactory {

	/**
	 * Creates and returns a {@link FileTransfer} implementation based on the specified protocol.
	 *
	 * @param protocol   the protocol to use for file transfer ("ftp" or "sftp")
	 * @param connection the established connection to the server
	 * @return a {@link FileTransfer} implementation suitable for the specified protocol
	 * @throws FileTransferException if there's an error creating the appropriate file transfer implementation
	 * @throws IllegalArgumentException if the specified protocol is not supported
	 */
	public static FileTransfer create(String protocol, GateConnection connection) {
		try {
			return switch (protocol.toLowerCase()) {
				case "ftp" -> new FtpFileTransfer((FtpConnection) connection);
				case "sftp" -> new SftpFileTransfer(connection);
				default -> throw new IllegalArgumentException("Unsupported protocol: " + protocol);
			};
		} catch (Exception e) {
			throw new FileTransferException("Error creating FileTransfer", e);
		}
	}
}
