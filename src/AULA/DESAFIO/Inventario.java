package AULA.DESAFIO;

public class Inventario {
	private int quantidadeChaves;
	private int dinheiro;

	public Inventario() {
		this.quantidadeChaves = 0;
		this.dinheiro = 0;
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

	public void adicionarDinheiro(int v) {
		this.dinheiro += v;
	}

	public boolean gastarDinheiro(int v) {
		if (this.dinheiro >= v) {
			this.dinheiro -= v;
			return true;
		}
		return false;
	}

	public int getDinheiro() {
		return dinheiro;
	}
}
