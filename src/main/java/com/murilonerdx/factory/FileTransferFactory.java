package com.murilonerdx.factory;

import com.murilonerdx.connection.FtpConnection;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.exceptions.FileTransferException;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.transfer.FtpFileTransfer;
import com.murilonerdx.transfer.SftpFileTransfer;

public class FileTransferFactory {
	public static FileTransfer create(String protocol, GateConnection connection) {
		try {
			return switch (protocol.toLowerCase()) {
				case "ftp" -> new FtpFileTransfer((FtpConnection) connection);
				case "sftp" -> new SftpFileTransfer(connection);
				default -> throw new IllegalArgumentException("Unsupported protocol: " + protocol);
			};
		} catch (Exception e) {
			throw new FileTransferException("Erro ao criar FileTransfer", e);
		}
	}
}
