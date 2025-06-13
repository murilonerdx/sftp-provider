package com.murilonerdx.sync;

import com.murilonerdx.transfer.FileTransfer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Provides a synchronous, blocking client interface for file transfer operations.
 * <p>
 * This class wraps a {@link FileTransfer} implementation and exposes straightforward,
 * blocking methods for common remote file management tasks, such as uploading, downloading,
 * listing files, deleting, and managing directories.
 * <br>
 * Use this client in applications where asynchronous or reactive paradigms are not required.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * FileTransfer transfer = FileTransferFactory.create("sftp", connection);
 * SyncClient client = new SyncClient(transfer);
 *
 * client.uploadFile("local.txt", "/remote/dir/remote.txt");
 * List<String> files = client.listFiles("/remote/dir");
 * System.out.println(files);
 * client.disconnect();
 * }</pre>
 *
 * @author MurilonerdX
 */
public class SyncClient {

	/** The underlying file transfer implementation. */
	private final FileTransfer fileTransfer;

	/**
	 * Constructs a new synchronous client using the provided file transfer implementation.
	 *
	 * @param fileTransfer the instantiated {@link FileTransfer} to use
	 */
	public SyncClient(FileTransfer fileTransfer) {
		this.fileTransfer = fileTransfer;
	}

	/**
	 * Lists the files in the specified remote directory.
	 *
	 * @param remoteDir the path of the remote directory
	 * @return a {@link List} of file names in the remote directory
	 * @throws Exception if the file listing fails
	 */
	public List<String> listFiles(String remoteDir) throws Exception {
		return fileTransfer.listFiles(remoteDir);
	}

	/**
	 * Uploads a file to the remote server.
	 *
	 * @param localPath  the file location on the local machine
	 * @param remotePath the desired path on the remote server
	 * @throws Exception if the upload fails
	 */
	public void uploadFile(String localPath, String remotePath) throws Exception {
		fileTransfer.upload(Path.of(localPath), remotePath);
	}

	/**
	 * Downloads a file from the remote server into a local directory, preserving the filename.
	 * <p>
	 * The local directory is created automatically if it does not exist.
	 * </p>
	 *
	 * @param remotePath     the path of the file on the remote server
	 * @param localDirectory the local directory to download into
	 * @throws Exception if the download fails
	 */
	public void downloadFile(String remotePath, String localDirectory) throws Exception {
		String fileName = Path.of(remotePath).getFileName().toString();
		Path fullLocalPath = Path.of(localDirectory, fileName);
		Files.createDirectories(Path.of(localDirectory));
		fileTransfer.download(remotePath, fullLocalPath);
	}

	/**
	 * Deletes a file from the remote server.
	 *
	 * @param remotePath the path of the file to delete on the remote server
	 * @throws Exception if the deletion fails
	 */
	public void deleteFile(String remotePath) throws Exception {
		fileTransfer.delete(remotePath);
	}

	/**
	 * Creates directories in the remote server.
	 *
	 * @param remotePath the path of directories to create
	 * @throws Exception if the directory creation fails
	 */
	public void createDirectories(String remotePath) throws Exception {
		fileTransfer.createDirectories(remotePath);
	}

	/**
	 * Deletes a directory and its contents from the remote server.
	 *
	 * @param remoteDir the path of the directory to delete
	 * @throws Exception if the deletion fails
	 */
	public void deleteDirectory(String remoteDir) throws Exception {
		fileTransfer.deleteDirectory(remoteDir);
	}

	/**
	 * Checks if a given file or directory exists on the remote server.
	 *
	 * @param remotePath the path to check on the remote server
	 * @return {@code true} if the file or directory exists, otherwise {@code false}
	 * @throws Exception if the existence check fails
	 */
	public boolean exists(String remotePath) throws Exception {
		return fileTransfer.exists(remotePath);
	}

	/**
	 * Disconnects from the remote file server.
	 *
	 * @throws Exception if the disconnect operation fails
	 */
	public void disconnect() throws Exception {
		fileTransfer.disconnect();
	}
}
