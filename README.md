
# sftp-provider Library Documentation

## Overview

`sftp-provider` is a versatile Java library providing easy-to-use abstractions and implementations for file transfer protocols, specifically **SFTP** and **FTP**. It supports both synchronous (blocking) and reactive (non-blocking) operations, making it ideal for modern cloud-native and enterprise applications.

---

## Features

- SFTP and FTP protocol support
- Synchronous (blocking) API
- Reactive (non-blocking) API based on Project Reactor
- Support for file upload, download, delete, list, and move operations
- Configuration-driven connection setup with flexible options
- Exception handling and error feedback for robustness
- Extensible design allowing custom implementations
- Examples covering all major use cases

---

## Installation

Add the dependency to your Maven `pom.xml`:

```xml
<dependency>
    <groupId>io.github.murilonerdx</groupId>
    <artifactId>sftp-provider</artifactId>
    <version>1.11.0</version>
</dependency>

```

Or Gradle:

```groovy
implementation 'io.github.murilonerdx:sftp-provider:1.11.0'

```

---

## Configuration

Typical connection setup parameters include:

- Hostname or IP address
- Port number
- Username and password or private key
- Timeout settings
- Root directories

These can be passed via constructors or configuration classes in the library.

---

## API Overview

### 1. Using the factory

```java
import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.factory.FileTransferFactory;
import com.murilonerdx.transfer.FileTransfer;

// Create a connection
GateConnection connection = new SftpConnection("sftp.example.com", 22, "user", "pass");

// Get the appropriate implementation through the factory
FileTransfer transfer = FileTransferFactory.create("sftp", connection);

// Use the transfer object
transfer.upload(Path.of("/local/file.txt"), "/remote/path/file.txt");

```
### 1.2 Synchronous API
```java
import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.factory.FileTransferFactory;
import com.murilonerdx.sync.SyncClient;

// Create an SFTP connection
GateConnection connection = new SftpConnection("sftp.example.com", 22, "username", "password");
// Create a file transfer instance for SFTP
FileTransfer transfer = FileTransferFactory.create("sftp", connection);

// Use synchronous client for blocking operations
SyncClient client = new SyncClient(transfer);

try {
	
	// Upload a file
    client.uploadFile("/local/path/file.txt", "/remote/path/file.txt");

	// List files in a directory
	List<String> files = client.listFiles("/remote/path");
    System.out.println("Files: " + files);

	// Download a file
    client.downloadFile("/remote/path/file.txt", "/local/download/directory");

	// Check if a file exists
	boolean exists = client.exists("/remote/path/file.txt");
    System.out.println("File exists: " + exists);

	// Create directories
    client.createDirectories("/remote/new/directory/structure");

	// Delete a file
    client.deleteFile("/remote/path/file-to-delete.txt");

	// Delete a directory recursively
    client.deleteDirectory("/remote/directory/to/delete");
} catch (Exception e) {
		e.printStackTrace();
} finally {
		// Always disconnect when done
		try {
		    client.disconnect();
        } catch (Exception e) {
		    e.printStackTrace();
        }
}

```

### 2. Reactive FTP API

```java
import com.murilonerdx.connection.FtpConnection;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.factory.FileTransferFactory;
import com.murilonerdx.reactive.ReactiveClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Create an FTP connection
GateConnection connection = new FtpConnection("ftp.example.com", 21, "username", "password");

// Create a file transfer instance for FTP
FileTransfer transfer = FileTransferFactory.create("ftp", connection);

// Use reactive client for non-blocking operations
ReactiveClient client = new ReactiveClient(transfer);

// Upload a file reactively
client.uploadFile("/local/path/file.txt", "/remote/path/file.txt")
    .then(client.listFiles("/remote/path"))
		.doOnNext(filename -> System.out.println("Found file: " + filename))
		.then(client.downloadFile("/remote/path/file.txt", "/local/download/path.txt"))
		.then(client.exists("/remote/path/file.txt")
        .doOnNext(exists -> System.out.println("File exists: " + exists)))
		.then(client.createDirectories("/remote/new/directory"))
		.then(client.deleteFile("/remote/path/file-to-delete.txt"))
		.then(client.disconnect())
		.doOnError(e -> System.err.println("Error: " + e.getMessage()))
		.subscribe();

```
### 2.1 . SFTP Connection

```java
import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.connection.GateConnection;

// Basic connection
GateConnection sftpConnection = new SftpConnection(
		"sftp.example.com",  // hostname
		22,                  // port (standard SFTP port)
		"username",          // username
		"password"           // password
);

		// Using builder pattern (if implemented)
		SftpConfig config = SftpConfig.builder()
				.host("sftp.example.com")
				.port(22)
				.username("username")
				.password("password")
				.timeout(10000)
				.strictHostKeyChecking(false)
				.build();

		GateConnection sftpConnection = new SftpConnection(config);

```

### 2.2 . FTP Connection

```java
import com.murilonerdx.connection.FtpConnection;
import com.murilonerdx.connection.GateConnection;

// Basic connection
GateConnection ftpConnection = new FtpConnection(
		"ftp.example.com",   // hostname
		21,                  // port (standard FTP port)
		"username",          // username
		"password"           // password
);

		// Using builder pattern (if implemented)
		FtpConfig config = FtpConfig.builder()
				.host("ftp.example.com")
				.port(21)
				.username("username")
				.password("password")
				.passiveMode(true)
				.binaryTransfer(true)
				.build();

		GateConnection ftpConnection = new FtpConnection(config);

```

### 2.3 . Advanced Usage

```java
import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.transfer.SftpFileTransfer;
import java.nio.file.Path;

// Create connection and transfer objects directly
SftpConnection connection = new SftpConnection("sftp.example.com", 22, "username", "password");
		SftpFileTransfer transfer = new SftpFileTransfer(connection);

// Use the transfer object directly
transfer.upload(Path.of("/local/file.txt"), "/remote/path/file.txt");
		List<String> files = transfer.listFiles("/remote/path");
transfer.disconnect();

```
### 3. FTP Support

Same as SFTP but with `FtpClient` or `ReactiveFtpClient`.

---

## Detailed Functionalities

### Connection Management

- Connect and disconnect methods with error handling
- Support for key-based and password-based authentication
- Connection pooling and reuse (if implemented)

### File Operations

- Upload: Upload a file or stream to remote server
- Download: Download a file or stream from remote server
- Delete: Remove a file or directory
- List: List files and directories in a given path
- Move/Rename: Rename or move files remotely

---

## Examples

### FTP Operations Only
```java
import com.murilonerdx.connection.FtpConnection;
import com.murilonerdx.transfer.FtpFileTransfer;
import java.nio.file.Path;
import java.util.List;

public class FtpExample {
    public static void main(String[] args) {
        try {
            // Create FTP connection with server details
            FtpConnection connection = new FtpConnection(
                "ftp.example.com",  // hostname
                21,                 // port
                "username",         // username
                "password"          // password
            );
            
            // Create the FTP transfer handler
            FtpFileTransfer ftpTransfer = new FtpFileTransfer(connection);
            
            // Upload a file
            ftpTransfer.upload(Path.of("/local/path/document.pdf"), "/remote/docs/document.pdf");
            
            // List files in a directory
            List<String> files = ftpTransfer.listFiles("/remote/docs");
            System.out.println("Files in directory: " + files);
            
            // Download a file
            ftpTransfer.download("/remote/docs/report.xlsx", Path.of("/local/downloads/report.xlsx"));
            
            // Create a directory structure
            ftpTransfer.createDirectories("/remote/docs/2023/reports");
            
            // Check if file exists
            boolean fileExists = ftpTransfer.exists("/remote/docs/document.pdf");
            System.out.println("File exists: " + fileExists);
            
            // Delete a file
            ftpTransfer.delete("/remote/docs/old-file.txt");
            
            // Delete a directory and all its contents
            ftpTransfer.deleteDirectory("/remote/docs/archive");
            
            // Always disconnect when done
            ftpTransfer.disconnect();
            
        } catch (Exception e) {
            System.err.println("FTP operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

```
### SFTP Synchronous Operations
```java
import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.sync.SyncClient;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.factory.FileTransferFactory;
import java.util.List;

public class SftpSyncExample {
    public static void main(String[] args) {
        try {
            // Create a connection to the SFTP server
            SftpConnection connection = new SftpConnection(
                "sftp.example.com",  // hostname
                22,                  // port
                "username",          // username
                "password"           // password
            );
            
            // Create a FileTransfer instance via the factory
            FileTransfer transfer = FileTransferFactory.create("sftp", connection);
            
            // Wrap with the sync client for a cleaner API
            SyncClient client = new SyncClient(transfer);
            
            // Create a directory structure if it doesn't exist
            client.createDirectories("/remote/projects/2023");
            
            // Upload a file to the remote server
            client.uploadFile("/local/reports/quarterly.pdf", "/remote/projects/2023/quarterly.pdf");
            
            // List all files in the directory
            List<String> files = client.listFiles("/remote/projects/2023");
            System.out.println("Files found: " + files);
            
            // Check if a specific file exists
            if (client.exists("/remote/projects/2023/quarterly.pdf")) {
                // Download the file to a local directory
                client.downloadFile("/remote/projects/2023/quarterly.pdf", "/local/downloads");
                System.out.println("File downloaded successfully");
            }
            
            // Delete a specific file
            client.deleteFile("/remote/projects/2023/old-draft.txt");
            
            // Delete a directory and all its contents recursively
            client.deleteDirectory("/remote/projects/2022");
            
            // Always disconnect when done
            client.disconnect();
            
        } catch (Exception e) {
            System.err.println("SFTP operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

```
### SFTP Reactive Operations
```java
import com.murilonerdx.connection.SftpConnection;
import com.murilonerdx.connection.GateConnection;
import com.murilonerdx.transfer.FileTransfer;
import com.murilonerdx.factory.FileTransferFactory;
import com.murilonerdx.reactive.ReactiveClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SftpReactiveExample {
    public static void main(String[] args) {
        try {
            // Create SFTP connection
            GateConnection connection = new SftpConnection(
                "sftp.example.com",  // hostname
                22,                  // port
                "username",          // username
                "password"           // password
            );
            
            // Create SFTP transfer via factory
            FileTransfer transfer = FileTransferFactory.create("sftp", connection);
            
            // Create the reactive client
            ReactiveClient client = new ReactiveClient(transfer);
            
            // Define a workflow of operations in a reactive chain
            Mono<Void> workflow = client.createDirectories("/remote/data/incoming")
                // Upload a file after directory is created
                .then(client.uploadFile("/local/data/sensor-data.json", "/remote/data/incoming/sensor-data.json"))
                // List files after upload is complete
                .then(Mono.defer(() -> {
                    System.out.println("Upload completed, listing files...");
                    return Mono.empty();
                }))
                .then(client.listFiles("/remote/data/incoming")
                    .doOnNext(file -> System.out.println("Found file: " + file))
                    .then())
                // Check if file exists
                .then(client.exists("/remote/data/incoming/sensor-data.json")
                    .doOnNext(exists -> {
                        if (exists) {
                            System.out.println("Verified: file exists on server");
                        } else {
                            System.out.println("Warning: file not found on server");
                        }
                    })
                    .then())
                // Download the file
                .then(client.downloadFile("/remote/data/incoming/sensor-data.json", "/local/downloads/sensor-data.json"))
                .then(Mono.defer(() -> {
                    System.out.println("Download completed");
                    return Mono.empty();
                }))
                // Clean up old files if needed
                .then(client.deleteFile("/remote/data/incoming/old-data.json"))
                // Disconnect when done
                .then(client.disconnect());
            
            // Execute the workflow and wait for completion
            workflow.doOnError(e -> {
                    System.err.println("Error in SFTP operations: " + e.getMessage());
                    e.printStackTrace();
                })
                .doOnSuccess(v -> System.out.println("All SFTP operations completed successfully"))
                .block(); // Only block at the application's top level
                
        } catch (Exception e) {
            System.err.println("Failed to initialize SFTP: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

```

### Complete Example: Combining FTP and SFTP
```java
import com.murilonerdx.connection.*;
import com.murilonerdx.transfer.*;
import com.murilonerdx.factory.FileTransferFactory;
import com.murilonerdx.sync.SyncClient;
import com.murilonerdx.reactive.ReactiveClient;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.List;

public class ComprehensiveExample {
    public static void main(String[] args) {
        try {
            // Scenario: Copy files from FTP server to SFTP server
            
            // 1. Setup FTP connection (source)
            GateConnection ftpConnection = new FtpConnection(
                "ftp-source.example.com", 21, "ftp-user", "ftp-password");
            FileTransfer ftpTransfer = FileTransferFactory.create("ftp", ftpConnection);
            SyncClient ftpClient = new SyncClient(ftpTransfer);
            
            // 2. Setup SFTP connection (destination)
            GateConnection sftpConnection = new SftpConnection(
                "sftp-destination.example.com", 22, "sftp-user", "sftp-password");
            FileTransfer sftpTransfer = FileTransferFactory.create("sftp", sftpConnection);
            ReactiveClient sftpClient = new ReactiveClient(sftpTransfer);
            
            // 3. List files from FTP server
            List<String> ftpFiles = ftpClient.listFiles("/source/data");
            System.out.println("Found " + ftpFiles.size() + " files to transfer");
            
            // 4. Download and upload each file
            for (String fileName : ftpFiles) {
                String remotePath = "/source/data/" + fileName;
                String localTempPath = "/tmp/" + fileName;
                
                // Download from FTP synchronously
                System.out.println("Downloading: " + fileName);
                ftpClient.downloadFile(remotePath, "/tmp");
                
                // Upload to SFTP reactively
                System.out.println("Uploading: " + fileName);
                sftpClient.uploadFile(localTempPath, "/destination/data/" + fileName)
                    .block(); // Block just for this demo
                
                // Delete temp file (in a real app, use Files.deleteIfExists)
                System.out.println("Transferred: " + fileName);
            }
            
            // 5. Verify files on SFTP server
            sftpClient.listFiles("/destination/data")
                .doOnNext(file -> System.out.println("Verified file on destination: " + file))
                .blockLast(); // Block just for this demo
            
            // 6. Clean up and disconnect
            ftpClient.disconnect();
            sftpClient.disconnect().block();
            
            System.out.println("File transfer complete");
            
        } catch (Exception e) {
            System.err.println("Error during file transfer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

```
---

## Error Handling

- Custom exceptions thrown for connection failures, authentication errors, and I/O issues
- Reactive API signals errors via onError signals
- Use try-catch blocks or reactive error handlers accordingly

---

## Contribution & Support

Feel free to fork the repository and submit pull requests. For issues or feature requests, open GitHub issues.

---

## License

This project is licensed under the MIT License.

---

