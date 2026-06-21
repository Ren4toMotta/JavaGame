package AULA.DESAFIO;

public class Inventario {
	private int quantidadeChaves;
	private int chavesColetadas; // total cumulativo coletado (nao diminui ao usar)
	private int dinheiro;

	public Inventario() {
		this.quantidadeChaves = 0;
		this.chavesColetadas = 0;
		this.dinheiro = 0;
	}

	public void adicionarChaves() {
		this.quantidadeChaves++;
		this.chavesColetadas++;
		System.out.println("Chave Coletada! Total: "+this.quantidadeChaves);
	}

	public int getChavesColetadas() {
		return chavesColetadas;
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
