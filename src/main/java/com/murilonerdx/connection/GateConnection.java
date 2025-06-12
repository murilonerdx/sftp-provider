package com.murilonerdx.connection;


import com.jcraft.jsch.ChannelSftp;
import com.murilonerdx.exceptions.SftpException;

public interface GateConnection {
	void connect() throws SftpException, Exception;
	void disconnect() throws Exception;
	ChannelSftp getChannel() throws SftpException;
}