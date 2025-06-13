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

public class FtpFileTransfer implements FileTransfer {

	private final FTPClient client;

	public FtpFileTransfer(FtpConnection connection) {
		this.client = connection.getClient();
	}

	@Override
	public List<String> listFiles(String remoteDir) throws Exception {
		FTPFile[] files = client.listFiles(remoteDir);
		return Arrays.stream(files)
				.map(FTPFile::getName)
				.collect(Collectors.toList());
	}

	@Override
	public void upload(Path localFile, String remotePath) throws Exception {
		try (InputStream input = Files.newInputStream(localFile)) {
			client.storeFile(remotePath, input);
		}
	}

	@Override
	public void download(String remoteFile, Path localPath) throws Exception {
		try (OutputStream output = Files.newOutputStream(localPath)) {
			client.retrieveFile(remoteFile, output);
		}
	}

	@Override
	public void disconnect() throws Exception {
		client.logout();
		client.disconnect();
	}

	@Override
	public void delete(String remotePath) throws IOException {
		client.deleteFile(remotePath);
	}

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
					throw new IOException("Falha ao criar diretório: " + path);
				}
			}
		}
	}

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
					throw new IOException("Falha ao deletar arquivo: " + fullPath);
				}
			}
		}
		boolean removed = client.removeDirectory(remoteDir);
		if (!removed) {
			throw new IOException("Falha ao remover diretório: " + remoteDir);
		}
	}

	@Override
	public boolean exists(String remotePath) throws IOException {
		FTPFile[] files = client.listFiles(remotePath);
		return files != null && files.length > 0;
	}
}
