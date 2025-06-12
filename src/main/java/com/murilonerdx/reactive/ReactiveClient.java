package com.murilonerdx.reactive;

import com.murilonerdx.transfer.FileTransfer;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.nio.file.Path;

public class ReactiveClient {
	private final FileTransfer fileTransfer;

	public ReactiveClient(FileTransfer fileTransfer) {
		this.fileTransfer = fileTransfer;
	}

	public Flux<String> listFiles(String remoteDir) {
		return Mono.fromCallable(() -> fileTransfer.listFiles(remoteDir))
				.flatMapMany(Flux::fromIterable);
	}

	public Mono<Void> uploadFile(String localPath, String remotePath) {
		return Mono.fromRunnable(() -> {
			try {
				fileTransfer.upload(Path.of(localPath), remotePath);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	public Mono<Void> downloadFile(String remotePath, String localPath) {
		return Mono.fromRunnable(() -> {
			try {
				fileTransfer.download(remotePath, Path.of(localPath));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	public Mono<Void> deleteFile(String remotePath) {
		return Mono.fromRunnable(() -> {
			try {
				fileTransfer.delete(remotePath);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	public Mono<Void> createDirectories(String remotePath) {
		return Mono.fromRunnable(() -> {
			try {
				fileTransfer.createDirectories(remotePath);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	public Mono<Void> deleteDirectory(String remoteDir) {
		return Mono.fromRunnable(() -> {
			try {
				fileTransfer.deleteDirectory(remoteDir);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	public Mono<Boolean> exists(String remotePath) {
		return Mono.fromCallable(() -> fileTransfer.exists(remotePath));
	}

	public Mono<Void> disconnect() {
		return Mono.fromRunnable(() -> {
			try {
				fileTransfer.disconnect();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
}

