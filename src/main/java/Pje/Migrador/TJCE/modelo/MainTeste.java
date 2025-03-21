package Pje.Migrador.TJCE.modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Pje.Migrador.TJCE.modelo.Entity.Servidores;
import Pje.Migrador.TJCE.modelo.Service.DistribuiProcessosServidor;
import Pje.Migrador.TJCE.modelo.Service.MigradorSellenium;

public class MainTeste {

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		String arquivoOriginal = "/File/ListaMigracao.txt";
		String extensao = ".txt";
		
		List<Servidores> servidores = new ArrayList<Servidores>();
		servidores.add(new Servidores("Bravo"));
		servidores.add(new Servidores("Charlie"));
		servidores.add(new Servidores("Delta"));
		servidores.add(new Servidores("Echo"));
		servidores.add(new Servidores("Golf"));
		servidores.add(new Servidores("Hotel"));
		servidores.add(new Servidores("India"));
		servidores.add(new Servidores("Juliet"));
		servidores.add(new Servidores("Kilo"));
		servidores.add(new Servidores("Lima"));
		servidores.add(new Servidores("Mike"));
		servidores.add(new Servidores("November"));
		servidores.add(new Servidores("Oscar"));
		servidores.add(new Servidores("Papa"));
		servidores.add(new Servidores("Romeo"));
		servidores.add(new Servidores("Tango"));
		servidores.add(new Servidores("Uniform"));
		servidores.add(new Servidores("Victor"));
		
		DistribuiProcessosServidor dps = new DistribuiProcessosServidor();
		MigradorSellenium migrador = new MigradorSellenium();
		
		Integer cursor = 9 ;
		while(cursor != 0) {
			System.out.println("""
				-------MENU MIGRADOR----------------------
					Escolha uma opção:
					1 - Dividir Lista para Servidores.
					2 - Distribuir listas e iniciar carga.
					0 - Encerrar
				--------------------------------------------
				Digite aqui: 
					""");
			cursor = scan.nextInt();
			switch (cursor) {
			case 1: {
				dps.dividirArquivoPorServidores(arquivoOriginal, servidores, extensao);
				break;
				}							
			case 2: {
				try {
					migrador.abrirIp3(servidores, extensao);
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			case 0:{
				System.out.println("Muito Obrigado!!");
				scan.close();
				break;
			}
			default:
				throw new IllegalArgumentException("Valor Inválido");
			}
		}
		
		
		
	}

}
