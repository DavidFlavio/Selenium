package Pje.Migrador.TJCE.modelo.Entity;

public class Servidores {
			
		private String nome ;
		private Boolean status = true;
		
//		List<String> servidores = new ArrayList<String>();
		
		public Servidores() {
			
		}
		
		public Servidores (String nome) {
			this.nome = nome;
		}
			
		public Servidores(String nome, Boolean status) {
			this.nome = nome;
			this.status = status;
		}

		public String getNome() {
			return nome;
		}

		
		public Boolean getStatus() {
			return status;
		}

			
	}

