package br.ester.sp.guiademotel.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FirebaseUtil {
	// variavel para guardar as credenciais de acesso
	private Credentials credenciais;
	//variavel para acessar e manipular o storage
	private Storage storage;
	// constante para o nome do bucket
	private final String BUCKET_NAME = "motelguide-ester.appspot.com";
	
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/"+BUCKET_NAME+"/o/";
	
	private final String SUFFIX = "?alt=media";
	
	private final String DOWNLOAD_URL = PREFIX + "%s" + SUFFIX;
	
	public FirebaseUtil() {
		Resource resource = new ClassPathResource("chavefirebase.json");
		
		try {
			//gera uma credencial no Firebase atráves da chave do arquivo
			credenciais = GoogleCredentials.fromStream(resource.getInputStream());
			
			storage = StorageOptions.newBuilder().setCredentials(credenciais).build().getService();
					} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		
		}
	}
	//método para extrair a extensão do arquivo
	private String getExtensao(String nomeArquivo) {
		// extrai o trecho do arquivo onde está a extensão
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
	}
	// método que faz o upload
	public String upload(MultipartFile arquivo) throws IOException {
		//gera um nome aleatório para o arquivo
		String nomeArquivo = UUID.randomUUID().toString()+ getExtensao(arquivo.getOriginalFilename());
		// criar um blobId através do nome gerado para o arquivo
		BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);
		// criar um blobInfo através do blobId
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
		// gravar o blobInfo no Storage passando os bytes do arquivo
		storage.create(blobInfo, arquivo.getBytes());
		// retorna a URL do arquivo gerado no Storage
		return String.format(DOWNLOAD_URL, nomeArquivo);
	}
	
	//método que exclui o arquivo do Storage
	public void deletar(String nomeArquivo) {
		//retirar o prefixo e o sufixo da String
		nomeArquivo = nomeArquivo.replace(PREFIX, "").replace(SUFFIX, "");
		// obter um Blob através do nome
		Blob blob = storage.get(BlobId.of(BUCKET_NAME, nomeArquivo));
		// deleta através do blob
		storage.delete(blob.getBlobId());
	}
}
