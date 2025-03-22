package Pje.Migrador.TJCE.modelo.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Pje.Migrador.TJCE.modelo.Entity.Servidores;
import Pje.Migrador.TJCE.modelo.Entity.Usuario;
import Pje.Migrador.TJCE.modelo.Utils.Util;

public class MigradorSellenium {

	Usuario usuario = new Usuario();
	private List<String> janelas = new ArrayList<>(); // nova variavel para guardar as janelas abertas
	private List<Servidores> servidoresTeste;
	
	private WebDriver navegador;
	private WebDriverWait wait = new WebDriverWait(navegador, Duration.ofSeconds(10)); 
	private WebElement menu;
	
	@Test
	public void abrirIp3(List<Servidores> servidores, String extensao) throws IOException {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.navegador = new ChromeDriver();
		
		this.servidoresTeste = servidores;

		int indiceServidor = 0;
		for (Servidores servidor : servidoresTeste) {
			String url = "http://ip3-sajpg-" + servidoresTeste.get(indiceServidor).getNome() + ":8080/ip3-sajpg/paginas/principal.jsf";
			
			if (servidoresTeste.indexOf(servidor) == 0) {
				navegador.get(url);
			} else {
				((JavascriptExecutor) navegador).executeScript("window.open('" + url + "','_blank');");
				ArrayList<String> tabs = new ArrayList<>(navegador.getWindowHandles());
                navegador.switchTo().window(tabs.get(tabs.size() - 1));
			}
			
			janelas.add(navegador.getWindowHandle()); // Adiciona a janela à lista
			
			wait = new WebDriverWait(navegador, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_username")));

			navegador.findElement(By.id("j_username")).sendKeys(usuario.getUsuario());
			navegador.findElement(By.id("j_password")).sendKeys(usuario.getSenha());
			WebElement enter = navegador.findElement(By.xpath("//input[@value='Entrar']"));
			enter.click();
			
			menu = navegador.findElement(By.xpath("//span[text()='Administração']"));
			Actions mouse = new Actions(navegador);
			mouse.moveToElement(menu).perform();

			WebElement menuMigracaoLote = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Migração em Lote']")));
			menuMigracaoLote.click();
			
			String listaProcessosServidor = "/Files/Parte_" + servidoresTeste.get(indiceServidor).getNome() + extensao;
			String conteudoArquivo = Util.lerConteudoArquivo(listaProcessosServidor);

			Util.copiarParaClipboard(conteudoArquivo);
			
			WebElement textAreaMigracaoLote = navegador.findElement(By.id("formTransmissaoProcessoLote:processos"));
			textAreaMigracaoLote.clear();
			textAreaMigracaoLote.sendKeys(Keys.CONTROL, "v");
			
			WebElement botaoPesquisar = navegador.findElement(By.id("formTransmissaoProcessoLote:cadastrar"));//(By.xpath("//span[text()='Pesquisar']"));
			botaoPesquisar.click();
		
			indiceServidor++;
			try {
				Thread.sleep(1000); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//"formTransmissaoProcessoLote:j_idt94:j_idt106"

	}

	public void adcionarProcessos() {
		if(janelas.isEmpty()) {
			System.out.println("Não existe Janelas abertas!");
		}
		
		for (String janela : janelas) {
			
			navegador.switchTo().window(janela);
			
			try {
				WebElement botaoAdicionar = new WebDriverWait(navegador, Duration.ofSeconds(10))
						.until(ExpectedConditions.elementToBeClickable(By.id("formTransmissaoProcessoLote:j_idt94:j_idt106"))); 

				((JavascriptExecutor) navegador).executeScript("arguments[0].scrollIntoView(true);", botaoAdicionar);
				botaoAdicionar.click();
				
				WebElement confirmar = new WebDriverWait(navegador, Duration.ofSeconds(2))
						.until(ExpectedConditions.elementToBeClickable(By.id("formTransmissaoProcessoLote:confirm")));
				confirmar.click();

			} catch (Exception e) {
				System.err.println("Erro ao rolar e adicionar na janela " + janela + ": " + e.getMessage());
			}
		}
	}
	
	public void irParaIniciar() {
		if(janelas.isEmpty()) {
			System.out.println("Não existe Janelas abertas!");
		}
		
		for (String janela : janelas) {
			navegador.switchTo().window(janela);
			
			menu = navegador.findElement(By.xpath("//span[text()='Migração']"));

			try {
				((JavascriptExecutor) navegador).executeScript("window.scrollTo(0, 0);"); 
//				((JavascriptExecutor) navegador).executeScript("arguments[0].scrollIntoView(true);", menu);
				Actions mouse = new Actions(navegador);
				mouse.moveToElement(menu).perform();
				
				WebElement menuMigracaoLote = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Gerenciar']")));
				menuMigracaoLote.click();					

			} catch (Exception e) {
				System.err.println("Erro ao rolar e adicionar na janela " + janela + ": " + e.getMessage());
			}
		}
	}
}
