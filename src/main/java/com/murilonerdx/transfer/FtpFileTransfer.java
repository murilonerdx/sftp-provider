package com.murilonerdx.transfer;

import com.murilonerdx.connection.FtpConnection;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FTP implementation of the {@link FileTransfer} interface.
 * <p>
 * This class provides file transfer operations over the FTP protocol
 * using Apache Commons Net FTPClient. It supports common operations such as
 * listing, uploading, downloading, and deleting files and directories on
 * an FTP server.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * FtpConnection connection = new FtpConnection("ftp.example.com", 21, "user", "pass");
 * FtpFileTransfer transfer = new FtpFileTransfer(connection);
 *
 * // List files in a directory
 * List<String> files = transfer.listFiles("/remote/dir");
 *
 * // Upload a file
 * transfer.upload(Paths.get("/local/file.txt"), "/remote/dir/file.txt");
 *
 * // Disconnect when done
 * transfer.disconnect();
 * }</pre>
 *
 * @author MurilonerdX
 */
public class FtpFileTransfer implements FileTransfer {

	/** The FTP client used for all file operations. */
	private final FTPClient client;

	/**
	 * Creates a new FTP file transfer handler using the provided connection.
	 *
	 * @param connection an established FTP connection
	 */
	public FtpFileTransfer(FtpConnection connection) {
		this.client = connection.getClient();
	}

	/**
	 * Lists all files in the specified remote directory.
	 *
	 * @param remoteDir the remote directory path to list
	 * @return a list of file names in the remote directory
	 * @throws Exception if an error occurs during the file listing
	 */
	@Override
	public List<String> listFiles(String remoteDir) throws Exception {
		FTPFile[] files = client.listFiles(remoteDir);
		return Arrays.stream(files)
				.map(FTPFile::getName)
				.collect(Collectors.toList());
	}

	/**
	 * Uploads a local file to the remote server.
	 * <p>
	 * This method ensures that:
	 * <ul>
	 *   <li>The file is transferred in binary mode to prevent corruption</li>
	 *   <li>The remote directory structure is created if it doesn't exist</li>
	 * </ul>
	 *
	 * @param localFile  the path to the local file to upload
	 * @param remotePath the destination path on the remote server
	 * @throws Exception if the upload fails
	 */
	@Override
	public void upload(Path localFile, String remotePath) throws Exception {
		// Set binary file type to prevent file corruption
		client.setFileType(FTPClient.BINARY_FILE_TYPE);

		// Ensure the remote directory exists
		String remoteDir = remotePath.substring(0, remotePath.lastIndexOf('/'));
		createDirectories(remoteDir); // creates directories recursively

		// Perform the file upload
		try (InputStream input = Files.newInputStream(localFile)) {
			boolean success = client.storeFile(remotePath, input);
			if (!success) {
				throw new IOException("Failed to upload file: " + remotePath);
			}
		}
	}

	/**
	 * Downloads a file from the remote server to the local file system.
	 *
	 * @param remoteFile the path of the file on the remote server
	 * @param localPath  the destination path on the local file system
	 * @throws Exception if the download fails
	 */
	@Override
	public void download(String remoteFile, Path localPath) throws Exception {
		try (OutputStream output = Files.newOutputStream(localPath)) {
			boolean success = client.retrieveFile(remoteFile, output);
			if (!success) {
				throw new IOException("Failed to download file: " + remoteFile);
			}
		}
	}

	/**
	 * Logs out and disconnects from the FTP server.
	 *
	 * @throws Exception if the disconnect operation fails
	 */
	@Override
	public void disconnect() throws Exception {
		client.logout();
		client.disconnect();
	}

	/**
	 * Deletes a file on the remote server.
	 *
	 * @param remotePath the path of the file to delete on the remote server
	 * @throws IOException if the deletion fails
	 */
	@Override
	public void delete(String remotePath) throws IOException {
		client.deleteFile(remotePath);
	}

	/**
	 * Creates a directory structure on the remote server.
	 * <p>
	 * This method creates directories recursively, similar to the {@code mkdir -p} command
	 * in Unix systems.
	 *
	 * @param remotePath the path of directories to create
	 * @throws IOException if directory creation fails
	 */
	@Override
	public void createDirectories(String remotePath) throws IOException {
		String[] folders = remotePath.split("/");
		StringBuilder path = new StringBuilder();
		for (String folder : folders) {
			if (folder.isEmpty()) continue;
			path.append("/").append(folder);
			if (!client.changeWorkingDirectory(path.toString())) {
				boolean created = client.makeDirectory(path.toString());
				if (!created) {
					throw new IOException("Failed to create directory: " + path);
				}
			}
		}
	}

	/**
	 * Recursively deletes a directory and all its contents on the remote server.
	 *
	 * @param remoteDir the path of the directory to delete
	 * @throws IOException if the deletion fails
	 */
	@Override
	public void deleteDirectory(String remoteDir) throws IOException {
		FTPFile[] files = client.listFiles(remoteDir);
		for (FTPFile file : files) {
			String name = file.getName();
			if (".".equals(name) || "..".equals(name)) {
				continue;
			}
			String fullPath = remoteDir + "/" + name;
			if (file.isDirectory()) {
				deleteDirectory(fullPath);
			} else {
				boolean deleted = client.deleteFile(fullPath);
				if (!deleted) {
					throw new IOException("Failed to delete file: " + fullPath);
				}
			}
		}
		boolean removed = client.removeDirectory(remoteDir);
		if (!removed) {
			throw new IOException("Failed to remove directory: " + remoteDir);
		}
	}

	/**
	 * Checks if a file or directory exists on the remote server.
	 *
	 * @param remotePath the path to check on the remote server
	 * @return {@code true} if the file or directory exists, {@code false} otherwise
	 * @throws IOException if the check fails
	 */
	@Override
	public boolean exists(String remotePath) throws IOException {
		FTPFile[] files = client.listFiles(remotePath);
		return files != null && files.length > 0;
	}
}
