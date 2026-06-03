package AULA.DESAFIO;

public class Inventario {
	private int quantidadeChaves;
	
	public Inventario() {
		this.quantidadeChaves = 0;
	}
	
	public void adicionarChaves() {
		this.quantidadeChaves++;
		System.out.println("Chave Coletada! Total: "+this.quantidadeChaves);
		
	}
	public boolean usarChave() {
		if(this.quantidadeChaves >0) {
			this.quantidadeChaves--;
			System.out.println("Chave Usada! Restante: "+this.quantidadeChaves);
			return true;
		}
		return false;
	}
	
	public int getQtdChaves() {
		return quantidadeChaves;
	}
		
}
