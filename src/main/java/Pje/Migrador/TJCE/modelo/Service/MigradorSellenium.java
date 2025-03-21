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
	
	@Test
	public void abrirIp3(List<Servidores> servidores, String extensao) throws IOException {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		WebDriver navegador = new ChromeDriver();
		
		List<Servidores> servidoresTeste = servidores;

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
			
			// Espera explícita para o campo de usuário
            WebDriverWait wait = new WebDriverWait(navegador, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_username")));

			navegador.findElement(By.id("j_username")).sendKeys(usuario.getUsuario());
			navegador.findElement(By.id("j_password")).sendKeys(usuario.getSenha());
			WebElement enter = navegador.findElement(By.xpath("//input[@value='Entrar']"));
			enter.click();
			
			WebElement menu = navegador.findElement(By.xpath("//span[text()='Administração']"));
			
			Actions mouse = new Actions(navegador);
			
			mouse.moveToElement(menu).perform();

//			WebDriverWait paciencia = new WebDriverWait(navegador, Duration.ofMillis(3000));
						
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
		
//			WebDriverWait paciencia2 = new WebDriverWait(navegador, Duration.ofSeconds(10));
			
			
			
//			for (String janela : driver.getWindowHandles()) {
//			    driver.switchTo().window(janela); // Muda para a aba atual
//
//			    WebDriverWait wait = new WebDriverWait(driver, 10); // Define um tempo limite máximo de 10 segundos
//			    WebElement botaoAdicionar = wait.until(ExpectedConditions.elementToBeClickable(By.id("formTransmissaoProcessoLote:j_idt94:j_idt106"))); // Espera até que o botão esteja clicável
//
//			    botaoAdicionar.click(); // Clica no botão
//			}
			
			indiceServidor++;
			try {
				Thread.sleep(1000); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

}
