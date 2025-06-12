package com.murilonerdx.connection;


import com.jcraft.jsch.ChannelSftp;
import com.murilonerdx.exceptions.SftpException;

public interface SftpConnection {
	void connect() throws SftpException;
	void disconnect();
	ChannelSftp getChannel() throws SftpException;
}