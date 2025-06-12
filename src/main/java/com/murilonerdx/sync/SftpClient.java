package com.murilonerdx.sync;

import com.murilonerdx.transfer.FileTransfer;

import java.nio.file.Path;
import java.util.List;

public class SftpClient {

	private final FileTransfer fileTransfer;

	public SftpClient(FileTransfer fileTransfer) {
		this.fileTransfer = fileTransfer;
	}

	public List<String> listFiles(String remoteDir) throws Exception {
		return fileTransfer.listFiles(remoteDir);
	}

	public void uploadFile(String localPath, String remotePath) throws Exception {
		fileTransfer.upload(Path.of(localPath), remotePath);
	}

	public void downloadFile(String remotePath, String localPath) throws Exception {
		fileTransfer.download(remotePath, Path.of(localPath));
	}

	public void deleteFile(String remotePath) throws Exception {
		fileTransfer.delete(remotePath);
	}

	public void createDirectories(String remotePath) throws Exception {
		fileTransfer.createDirectories(remotePath);
	}

	public void deleteDirectory(String remoteDir) throws Exception {
		fileTransfer.deleteDirectory(remoteDir);
	}

	public boolean exists(String remotePath) throws Exception {
		return fileTransfer.exists(remotePath);
	}

	public void disconnect() throws Exception {
		fileTransfer.disconnect();
	}
}
