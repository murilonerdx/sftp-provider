package com.murilonerdx.transfer;


import com.jcraft.jsch.ChannelSftp;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.exceptions.SftpException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class SftpFileTransfer implements FileTransfer {

	private final GateConnection connection;

	public SftpFileTransfer(GateConnection connection) {
		this.connection = connection;
	}

	@Override
	public void upload(Path localFile, String remotePath) throws SftpException {
		try (InputStream in = Files.newInputStream(localFile)) {
			ChannelSftp channel = connection.getChannel();
			channel.put(in, remotePath);
		} catch (Exception e) {
			throw new SftpException("Erro ao enviar arquivo", e);
		}
	}

	@Override
	public void download(String remotePath, Path localFile) throws SftpException {
		try (OutputStream out = Files.newOutputStream(localFile)) {
			ChannelSftp channel = connection.getChannel();
			channel.get(remotePath, out);
		} catch (Exception e) {
			throw new SftpException("Erro ao baixar arquivo", e);
		}
	}

	@Override
	public void disconnect() throws Exception {
		connection.disconnect();
	}

	@Override
	public void delete(String remotePath) {

	}

	@Override
	public List<String> listFiles(String remoteDir) throws SftpException {
		try {
			ChannelSftp channel = connection.getChannel();
			@SuppressWarnings("unchecked")
			Vector<ChannelSftp.LsEntry> entries = (Vector<ChannelSftp.LsEntry>) channel.ls(remoteDir);
			return entries.stream()
					.map(ChannelSftp.LsEntry::getFilename)
					.collect(Collectors.toList());
		} catch (SftpException e) {
			throw new SftpException("Erro ao listar arquivos", e);
		} catch (com.jcraft.jsch.SftpException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void createDirectories(String remotePath) throws SftpException {
		ChannelSftp channel = connection.getChannel();

		String[] folders = remotePath.split("/");
		String currentPath = "";
		try {
			for (String folder : folders) {
				if (folder.isEmpty()) continue;
				currentPath += "/" + folder;
				try {
					channel.cd(currentPath);
				} catch (com.jcraft.jsch.SftpException e) {
					if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
						channel.mkdir(currentPath);
						channel.cd(currentPath);
					} else {
						throw e;
					}
				}
			}
		} catch (Exception e) {
			throw new SftpException("Erro ao criar diretórios: " + remotePath, e);
		}
	}

	@Override
	public void deleteDirectory(String remoteDir) throws SftpException {
		ChannelSftp channel = connection.getChannel();
		try {
			@SuppressWarnings("unchecked")
			Vector<ChannelSftp.LsEntry> files = channel.ls(remoteDir);
			for (ChannelSftp.LsEntry entry : files) {
				String filename = entry.getFilename();
				if (".".equals(filename) || "..".equals(filename)) {
					continue;
				}
				String fullPath = remoteDir + "/" + filename;
				if (entry.getAttrs().isDir()) {
					deleteDirectory(fullPath); // recursivo
				} else {
					channel.rm(fullPath);
				}
			}
			channel.rmdir(remoteDir);
		} catch (SftpException | com.jcraft.jsch.SftpException e) {
			throw new SftpException("Erro ao deletar diretório: " + remoteDir, e);
		}
	}

	@Override
	public boolean exists(String remotePath) throws SftpException, com.jcraft.jsch.SftpException {
		ChannelSftp channel = connection.getChannel();
		try {
			channel.lstat(remotePath);
			return true;
		} catch (com.jcraft.jsch.SftpException e) {
			// Código 2 indica que o arquivo/pasta não existe
			if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
				return false;
			}
			throw e;
		}
	}
}

