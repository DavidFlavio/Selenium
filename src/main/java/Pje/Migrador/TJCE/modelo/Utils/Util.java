package Pje.Migrador.TJCE.modelo.Utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {

	public static void copiarParaClipboard(String conteudo) {
		StringSelection stringSelection = new StringSelection(conteudo);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	public static List<String> lerArquivo(String arquivo) throws IOException {
		List<String> linhas = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(arquivo));
		String linha;
		while ((linha = reader.readLine()) != null) {
			linhas.add(linha);
		}
		reader.close();
		return linhas;
	}

	public static String lerConteudoArquivo(String caminhoArquivo) {
		
	    try (InputStream inputStream = Util.class.getResourceAsStream(caminhoArquivo)){
	         if (inputStream == null) {
	            throw new IOException("Arquivo não encontrado: " + caminhoArquivo);
	        }
	        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name()).useDelimiter("\\A")) {
	            return scanner.hasNext() ? scanner.next() : "";
	        }
	    } catch (IOException e) {
	        System.err.println("Erro ao ler o arquivo: " + e.getMessage());
	        return ""; 
	    }
	}

	public static int contarLinhas(String arquivo) throws IOException {
	    int totalLinhas = 0;
	    
	    try (InputStream inputStream = Util.class.getResourceAsStream(arquivo)) {
	        if (inputStream == null) {
	            throw new FileNotFoundException("Arquivo não encontrado no classpath: " + arquivo);
	        }
	        
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
	            while (reader.readLine() != null) {
	                totalLinhas++;
	            }
	        }
	    }

	    return totalLinhas;
	}

	public static List<List<String>> dividirLista(List<String> linhas, int numServidores) {
		numServidores = 6;
		int tamanho = linhas.size();
		int tamanhoPorServidor = tamanho / numServidores;
		List<List<String>> dadosPorServidor = new ArrayList<>();

		int inicio = 0;
		for (int i = 0; i < numServidores; i++) {
			int fim = (i == numServidores - 1) ? tamanho : inicio + tamanhoPorServidor;
			dadosPorServidor.add(linhas.subList(inicio, fim));
			inicio = fim;
		}

		return dadosPorServidor;
	}

}
