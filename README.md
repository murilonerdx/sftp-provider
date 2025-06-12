
# 📁 SFTP File Client - Java (Síncrono e Reativo)

Uma biblioteca Java para **operações com SFTP** (upload, download, listagem e manipulação de arquivos e diretórios) com suporte completo a **interfaces síncronas e reativas (Reactor)**. Ideal para aplicações modernas que demandam integração robusta com servidores SFTP.

---

## 🚀 Funcionalidades

✅ Conexão com servidor SFTP (via JSch)  
📁 Listagem de arquivos em diretórios remotos  
📤 Upload de arquivos do sistema local para o servidor  
📥 Download de arquivos do servidor para o sistema local  
📂 Criação de diretórios remotos, incluindo caminhos aninhados  
❌ Deleção de arquivos e diretórios  
🔍 Verificação de existência de arquivos ou pastas  
♻️ Implementação reativa com `Mono` e `Flux` para uso com WebFlux ou microserviços escaláveis

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- JSch (Java Secure Channel)
- Project Reactor (Mono / Flux)
- Maven
- Docker + Docker Compose

---

## 📦 Como Usar
### 0. Baixar dependência:
```yaml
<dependency>
  <groupId>io.github.murilonerdx</groupId>
  <artifactId>sftp-provider</artifactId>
  <version>1.5.0</version>
</dependency>
```

### 1. Configurar a conexão:

```java


SftpConfig config = new SftpConfig()
		.host("localhost")
		.port(2222)
		.username("user")
		.password("senha");

SftpConnection connection = config.buildConnection();
connection.connect();
```

### 2. Utilizar API Síncrona:

```java
FileTransfer transfer = new SftpFileTransfer(connection);
SftpClient client = new SftpClient(transfer);

client.upload("local.txt", "/remote/dir/local.txt");
List<String> arquivos = client.listFiles("/remote/dir");
client.disconnect();
```

### 3. Utilizar API Reativa:

```java
ReactiveSftpClient reactive = new ReactiveSftpClient(transfer);

reactive.listFiles("/remote/dir")
    .doOnNext(System.out::println)
    .blockLast();

reactive.createDirectories("/remote/novaPasta").block();
reactive.disconnect().block();
```

---

## 📂 Estrutura do Projeto

```
src/
│
├── config           # SftpConfig (dados da conexão)
├── connection       # JschSftpConnection (conexão e autenticação)
├── transfer         # SftpFileTransfer (upload/download)
├── sync             # SftpClient (API síncrona)
├── reactive         # ReactiveSftpClient (API reativa)
└── exceptions       # Tratamento de erros customizados
```

---

## 📄 Licença

Este projeto está licenciado sob os termos da licença **MIT**.

---

## 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se livre para abrir issues, pull requests ou sugerir melhorias.
