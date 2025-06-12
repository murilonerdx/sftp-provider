package com.murilonerdx.transfer;


import com.murilonerdx.exceptions.SftpException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileTransfer {
	List<String> listFiles(String path) throws Exception;
	void upload(Path localFile, String remotePath) throws Exception;
	void download(String remoteFile, Path localPath) throws Exception;
	void disconnect() throws Exception;
	void delete(String remotePath) throws IOException;
	void createDirectories(String remotePath) throws SftpException, IOException, com.jcraft.jsch.SftpException;
	void deleteDirectory(String remoteDir) throws SftpException, IOException;
	boolean exists(String remotePath) throws SftpException, com.jcraft.jsch.SftpException, IOException;
}
