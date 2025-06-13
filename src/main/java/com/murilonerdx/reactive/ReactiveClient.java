package com.murilonerdx.reactive;

import com.murilonerdx.transfer.FileTransfer;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

/**
 * A reactive wrapper for the {@link FileTransfer} interface providing non-blocking file operations
 * through Project Reactor's {@link Mono} and {@link Flux}.
 * <p>
 * This class adapts the blocking {@link FileTransfer} implementations to work in a reactive context,
 * allowing for better scalability and resource utilization in asynchronous applications.
 * All blocking operations are executed on a separate thread pool to avoid blocking the caller thread.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * // Create a FileTransfer implementation
 * FileTransfer transfer = FileTransferFactory.create("sftp", connection);
 *
 * // Wrap it with ReactiveClient
 * ReactiveClient client = new ReactiveClient(transfer);
 *
 * // Use reactive methods
 * client.uploadFile("local.txt", "/remote/path/file.txt")
 *     .then(client.listFiles("/remote/path"))
 *     .doOnNext(System.out::println)
 *     .subscribe();
 * }</pre>
 *
 * @author MurilonerdX
 */
public class ReactiveClient {
	/** The underlying file transfer implementation */
	private final FileTransfer fileTransfer;

	/**
	 * Creates a new ReactiveClient with the provided file transfer implementation.
	 *
	 * @param fileTransfer the file transfer implementation to wrap
	 */
	public ReactiveClient(FileTransfer fileTransfer) {
		this.fileTransfer = fileTransfer;
	}

	/**
	 * Lists files in the remote directory in a reactive way.
	 *
	 * @param remoteDir the remote directory path to list
	 * @return a {@link Flux} emitting each file name found in the directory
	 */
	public Flux<String> listFiles(String remoteDir) {
		return Mono.fromCallable(() -> fileTransfer.listFiles(remoteDir))
				.flatMapMany(Flux::fromIterable);
	}

	/**
	 * Uploads a file to the remote server asynchronously.
	 *
	 * @param localPath  the path to the local file
	 * @param remotePath the target path on the remote server
	 * @return a {@link Mono} that completes when the upload is finished
	 */
	public Mono<Void> uploadFile(String localPath, String remotePath) {
		return Mono.fromCallable(() -> {
			try {
				fileTransfer.upload(Path.of(localPath), remotePath);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return null;
		});
	}

	/**
	 * Downloads a file from the remote server asynchronously.
	 *
	 * @param remotePath the path of the file on the remote server
	 * @param localPath  the target path on the local system
	 * @return a {@link Mono} that completes when the download is finished
	 */
	public Mono<Void> downloadFile(String remotePath, String localPath) {
		return Mono.fromCallable(() -> {
			try {
				fileTransfer.download(remotePath, Path.of(localPath));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return null;
		});
	}

	/**
	 * Deletes a file on the remote server asynchronously.
	 *
	 * @param remotePath the path of the file to delete on the remote server
	 * @return a {@link Mono} that completes when the deletion is finished
	 */
	public Mono<Void> deleteFile(String remotePath) {
		return Mono.fromCallable(() -> {
			try {
				fileTransfer.delete(remotePath);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return null;
		});
	}

	/**
	 * Creates directories on the remote server asynchronously.
	 *
	 * @param remotePath the path of directories to create on the remote server
	 * @return a {@link Mono} that completes when the operation is finished
	 */
	public Mono<Void> createDirectories(String remotePath) {
		return Mono.fromCallable(() -> {
			try {
				fileTransfer.createDirectories(remotePath);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}

	/**
	 * Deletes a directory and its contents on the remote server asynchronously.
	 *
	 * @param remoteDir the path of the directory to delete on the remote server
	 * @return a {@link Mono} that completes when the deletion is finished
	 */
	public Mono<Void> deleteDirectory(String remoteDir) {
		return Mono.fromCallable(() -> {
			try {
				fileTransfer.deleteDirectory(remoteDir);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}

	/**
	 * Checks if a file or directory exists on the remote server asynchronously.
	 *
	 * @param remotePath the path to check on the remote server
	 * @return a {@link Mono} that emits true if the path exists, false otherwise
	 */
	public Mono<Boolean> exists(String remotePath) {
		return Mono.fromCallable(() -> fileTransfer.exists(remotePath));
	}

	/**
	 * Disconnects from the remote server asynchronously.
	 *
	 * @return a {@link Mono} that completes when the disconnection is finished
	 */
	public Mono<Void> disconnect() {
		return Mono.fromCallable(() -> {
			try {
				fileTransfer.disconnect();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}
}
