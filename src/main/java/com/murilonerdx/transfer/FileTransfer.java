package com.murilonerdx.transfer;

import com.murilonerdx.exceptions.SftpException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Interface for abstracting file transfer operations between a local machine and a remote server.
 * <p>
 * Implementations of this interface provide file management and transfer capabilities
 * over different protocols (such as FTP, SFTP, etc.), ensuring a unified API for file operations.
 * </p>
 *
 * <h2>Main Operations Provided:</h2>
 * <ul>
 *   <li>Listing files in remote directories</li>
 *   <li>Uploading and downloading files</li>
 *   <li>Creating and deleting files and directories</li>
 *   <li>Checking if a specific remote file or directory exists</li>
 *   <li>Disconnecting from the remote server</li>
 * </ul>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * FileTransfer transfer = FileTransferFactory.create("sftp", connection);
 * List<String> files = transfer.listFiles("/remote/dir");
 * transfer.upload(Paths.get("local.txt"), "/remote/dir/remote.txt");
 * transfer.disconnect();
 * }</pre>
 *
 * @author MurilonerdX
 */
public interface FileTransfer {
	/**
	 * Lists the files in a given remote directory.
	 *
	 * @param path the remote directory path
	 * @return a list of file names in the specified directory
	 * @throws Exception if the operation fails
	 */
	List<String> listFiles(String path) throws Exception;

	/**
	 * Uploads a local file to a remote path.
	 *
	 * @param localFile  the path to the local file
	 * @param remotePath the remote destination path
	 * @throws Exception if the upload fails
	 */
	void upload(Path localFile, String remotePath) throws Exception;

	/**
	 * Downloads a remote file to a local path.
	 *
	 * @param remoteFile the remote file path
	 * @param localPath  the local destination file path
	 * @throws Exception if the download fails
	 */
	void download(String remoteFile, Path localPath) throws Exception;

	/**
	 * Disconnects from the remote server and releases any resources in use.
	 *
	 * @throws Exception if the disconnect operation fails
	 */
	void disconnect() throws Exception;

	/**
	 * Deletes a file on the remote server.
	 *
	 * @param remotePath the path to the remote file to delete
	 * @throws IOException if the deletion fails
	 */
	void delete(String remotePath) throws IOException;

	/**
	 * Creates directories on the remote server.
	 *
	 * @param remotePath the path of the directories to create
	 * @throws SftpException if there is an SFTP-specific error
	 * @throws IOException if an I/O error occurs
	 * @throws com.jcraft.jsch.SftpException if an error occurs at the JSch/SFTP level
	 */
	void createDirectories(String remotePath) throws SftpException, IOException, com.jcraft.jsch.SftpException;

	/**
	 * Deletes a directory and its contents on the remote server.
	 *
	 * @param remoteDir the path of the directory to delete
	 * @throws SftpException if there is an SFTP-specific error
	 * @throws IOException if an I/O error occurs
	 */
	void deleteDirectory(String remoteDir) throws SftpException, IOException;

	/**
	 * Checks if a file or directory exists on the remote server.
	 *
	 * @param remotePath the path to check
	 * @return {@code true} if the file or directory exists; {@code false} otherwise
	 * @throws SftpException if there is an SFTP-specific error
	 * @throws com.jcraft.jsch.SftpException if an error occurs at the JSch/SFTP level
	 * @throws IOException if an I/O error occurs
	 */
	boolean exists(String remotePath) throws SftpException, com.jcraft.jsch.SftpException, IOException;
}
