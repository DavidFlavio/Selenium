package Pje.Migrador.TJCE.modelo.Service;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import Pje.Migrador.TJCE.modelo.Entity.Servidores;
import Pje.Migrador.TJCE.modelo.Utils.Util;

public class DistribuiProcessosServidor {
		
	
	public void dividirArquivoPorServidores(String arquivoOriginal, List<Servidores> servidores, String extensao) {
		try {
			Path diretorioOriginal = Paths.get("src/main/Java/Files");
			Files.createDirectories(diretorioOriginal);
			
			// Primeiro passo: contar o número total de linhas no arquivo
			long totalLinhas = Util.contarLinhas(arquivoOriginal);
			System.out.println("Total de linhas no arquivo: " + totalLinhas);

			// Calcular quantas linhas cada arquivo deve ter
			int linhasPorArquivo = (int) Math.ceil((double) totalLinhas / servidores.size());
			System.out.println("Cada arquivo terá aproximadamente " + linhasPorArquivo + " linhas");

			// Criar leitor do arquivo
			InputStream inputStream = Util.class.getResourceAsStream(arquivoOriginal);
			if (inputStream == null) {
			    throw new FileNotFoundException("Arquivo não encontrado no classpath: " + arquivoOriginal);
			}
			BufferedReader leitor = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

			String linha;
			int contadorLinhas = 0;
			int indiceServidor = 0;
			PrintWriter escritor = null;

			// Processo de divisão do arquivo
			while ((linha = leitor.readLine()) != null) {
				// Se estamos no início ou atingimos o limite de linhas
				if (contadorLinhas % linhasPorArquivo == 0) {
					if (escritor != null) {
						escritor.close();
					}

					// Não crie mais arquivos do que a quantidade de servidores
					if (indiceServidor < servidores.size()) {
						
						String nomeServidor = servidores.get(indiceServidor).getNome();
						Path caminhoNovoArquivo = diretorioOriginal.resolve("Parte_" + nomeServidor + extensao);
						
						escritor = new PrintWriter(new OutputStreamWriter(
								new FileOutputStream(caminhoNovoArquivo.toFile(), true), StandardCharsets.UTF_8));
						
						System.out.println("Criando arquivo: " + caminhoNovoArquivo);
						indiceServidor++;
					}
				}

				
				if (escritor != null) {
					escritor.println(linha);
				}
				contadorLinhas++;
			}

			// Fecha os recursos
			if (escritor != null) {
				escritor.close();
			}
			leitor.close();

			System.out.println((servidores.size()) + " arquivos criados com aproximadamente "
					+ linhasPorArquivo + " linhas cada.");

		} catch (IOException e) {
			System.err.println("Erro ao processar o arquivo: " + e.getMessage());
			e.printStackTrace();
		}
	}


}

