package AULA.DESAFIO;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class tileMap {
	Tiles pecaDoCenario;
	int [][] cenarioValido;
	private String cenaValida;
	private Map<String, ArrayList<Zumbi>> zumbisPorCena;
	private Map<String, ArrayList<Dinheiro>> moedasPorCena;
	public NPC npcTD;
	private int totalChaves;
	private int[][][] mapasIniciais;
	int [][] cenarioTopEsq	={	{6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6},// cenario 1
								{3,1,1,1,1,1,1,1,1,1,1,1,9,1,1,0},
								{3,1,1,1,1,1,1,1,1,0,1,1,1,1,1,0},
								{3,1,1,1,1,1,1,1,0,0,1,1,1,1,1,0},
								{3,1,1,1,1,1,1,1,1,1,1,8,1,1,1,0},
								{3,1,1,1,1,1,1,1,0,1,1,1,4,1,1,0},
								{3,1,1,1,1,1,1,1,1,0,0,1,1,1,1,0},
								{3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								{6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6},
								{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}
		};

	int [][] cenarioMeioCima={	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},// cenario 2
            					{0,1,1,1,1,7,1,1,1,1,1,1,1,1,1,1},
            					{0,1,1,0,1,1,1,1,1,1,3,1,1,1,1,3},
            					{0,1,1,1,1,1,1,1,1,3,3,3,1,1,1,0},
            					{0,0,1,1,1,1,1,1,1,1,3,1,1,1,1,0},
            					{0,1,1,1,1,1,1,1,1,1,1,1,0,0,5,0},
            					{0,0,0,0,0,1,1,1,0,0,0,1,0,1,1,0},
            					{1,1,1,1,1,1,1,1,1,1,1,1,0,4,1,0},
            					{0,1,1,1,1,1,1,1,1,1,1,8,0,1,1,0},
            					{0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0}
		};

	int [][] cenarioTopoDir	={	{0,0,0,0,0,0,0,2,2,2,2,0,0,3,3,3},//cenario 3
            					{1,1,1,8,1,1,1,1,2,2,1,1,1,1,3,3},
            					{1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,3},
            					{0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0},
            					{0,1,1,1,1,1,1,1,1,0,0,0,0,0,1,0},
            					{0,1,1,1,0,1,1,1,1,1,9,1,1,1,1,0},
            					{0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0},
            					{0,5,0,0,0,0,0,0,0,0,0,0,1,1,3,0},
            					{0,1,1,1,1,1,1,1,1,2,4,1,1,1,3,0},
            					{0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0}
		};
	int [][] cenarioBasDir	={	{0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0},// cenario 4
								{0,4,2,1,9,1,1,1,1,0,1,1,1,1,1,3},
								{1,1,2,1,1,1,1,0,1,0,1,1,1,1,1,3},
								{0,1,3,1,1,1,1,0,1,0,1,1,1,1,1,0},
								{0,1,3,1,0,1,1,1,1,0,1,1,7,1,1,3},
								{0,1,3,0,0,1,1,0,1,0,1,1,1,1,1,0},
								{0,1,1,1,0,0,1,1,0,0,1,1,1,1,1,3},
								{1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,0},
								{0,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2},
								{0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2}
		};
	int [][] cenarioMeioBaixo={ {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},// cenario 5
            					{0,0,3,3,3,3,3,1,1,1,1,1,1,1,1,0},
            					{5,1,1,1,1,1,1,1,0,1,1,1,8,1,1,1},
            					{1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0},
            					{1,1,1,1,1,1,0,1,1,1,9,1,1,1,1,0},
            					{1,1,1,1,1,0,1,1,1,0,0,0,0,1,0,0},
            					{1,1,1,1,0,1,1,1,0,2,2,2,2,1,2,0},
            					{5,1,1,0,1,1,1,1,0,2,4,1,1,1,1,1},
            					{0,0,0,1,1,1,1,0,2,2,2,2,2,2,2,0},
            					{0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0}
	};
	int [][] cenarioBasEsq= {	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},// cenario 6
							  	{0,2,1,1,1,0,1,1,7,1,1,1,1,3,3,0},
					            {0,2,2,1,1,0,1,1,1,1,1,1,1,3,5,1},
					            {0,2,1,1,1,1,1,1,1,1,1,1,1,5,1,1},
					            {0,4,4,4,1,1,1,1,1,1,1,1,1,1,1,1},
					            {0,4,4,4,1,0,1,1,1,1,1,1,1,1,1,1},
					            {0,4,4,4,1,0,2,2,1,1,1,1,1,5,1,1},
					            {0,1,1,1,1,0,1,2,1,2,2,8,1,1,5,1},
					            {0,1,1,1,1,0,1,2,2,2,1,1,1,1,1,0},
					            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}

	};


	public tileMap() {
		this.cenarioValido = this.cenarioTopEsq;
		this.setCenaValida("TE");
		this.pecaDoCenario = new Tiles();
		this.resetarZumbis();
		// posicionado no canto inf-direito do TD (col~9, lin~5)
		this.npcTD = new NPC(9 * 48 + 4, 5 * 48 - 16);
		// guarda copia pristina dos mapas (pra restaurar chaves/portas ao reiniciar)
		this.mapasIniciais = clonarMapas();
		this.totalChaves = contarChaves();
	}

	private int[][][] mapas() {
		return new int[][][]{ cenarioTopEsq, cenarioMeioCima, cenarioTopoDir,
				cenarioBasDir, cenarioMeioBaixo, cenarioBasEsq };
	}

	private int[][][] clonarMapas() {
		int[][][] orig = mapas();
		int[][][] copia = new int[orig.length][][];
		for (int i = 0; i < orig.length; i++) {
			copia[i] = new int[orig[i].length][];
			for (int l = 0; l < orig[i].length; l++) copia[i][l] = orig[i][l].clone();
		}
		return copia;
	}

	// restaura chaves coletadas e portas abertas ao estado inicial (usado no restart)
	public void restaurarMapas() {
		int[][][] atual = mapas();
		for (int i = 0; i < atual.length; i++)
			for (int l = 0; l < atual[i].length; l++)
				System.arraycopy(mapasIniciais[i][l], 0, atual[i][l], 0, atual[i][l].length);
	}

	private int contarChaves() {
		int n = 0;
		for (int[][] cena : mapasIniciais)
			for (int[] linha : cena)
				for (int v : linha) if (v == 4) n++;
		return n;
	}

	public int getTotalChaves() {
		return totalChaves;
	}

	public int totalZumbisVivos() {
		int total = 0;
		for (ArrayList<Zumbi> lista : zumbisPorCena.values()) {
			for (Zumbi z : lista) if (z.vivo) total++;
		}
		return total;
	}

	public int zumbisVivosNa(String chave) {
		ArrayList<Zumbi> lista = zumbisPorCena.get(chave);
		if (lista == null) return 0;
		int total = 0;
		for (Zumbi z : lista) if (z.vivo) total++;
		return total;
	}

	public void resetarZumbis() {
		this.zumbisPorCena = new HashMap<>();
		this.moedasPorCena = new HashMap<>();
		for (String chave : new String[]{"TE", "MC", "TD", "BD", "MB", "BE"}) {
			this.zumbisPorCena.put(chave, SpawnZumbis.criaZumbisDaCena(chave));
			this.moedasPorCena.put(chave, new ArrayList<>());
		}
	}

	public ArrayList<Zumbi> getZumbisAtuais() {
		return this.zumbisPorCena.get(this.cenaValida);
	}

	public ArrayList<Dinheiro> getMoedasAtuais() {
		return this.moedasPorCena.get(this.cenaValida);
	}
	
	public void desenhar(Graphics2D d2) {
		int pecaDaMatriz;
		// passada 1: todos os tiles (carros so desenham fundo)
		for (int col=0; col < this.cenarioValido[0].length; col++ ) {
			for (int lin = 0; lin < this.cenarioValido.length; lin++) {
				pecaDaMatriz = this.cenarioValido[lin][col];
				this.pecaDoCenario.carregaPecaDaMatriz(pecaDaMatriz);
				this.pecaDoCenario.desenhaTile(d2, lin, col);
			}
		}
		// passada 2: carros por cima, podem ter overflow lateral
		for (int lin = 0; lin < this.cenarioValido.length; lin++) {
			for (int col = 0; col < this.cenarioValido[0].length; col++) {
				int v = this.cenarioValido[lin][col];
				if (v >= 7 && v <= 9) {
					this.pecaDoCenario.desenhaCarro(d2, lin, col, v);
				}
			}
		}
	}
	public String getCenaValida() {
		return cenaValida;
	}
/* DISPOSIÇÃO DOS CENARIOS ESTA NA SEGUINTE ORDEM LÓGICA
 *   [1] [2] [3]
 *   [6] [5] [4]
 */
	public void setCenaValida(String cenaValida) {
		this.cenaValida = cenaValida;
		switch (this.cenaValida) {
		case "TE":
			this.cenarioValido = this.cenarioTopEsq;
			break;
		case "MC":
			this.cenarioValido = this.cenarioMeioCima;
			break;
		case "TD":
			this.cenarioValido = this.cenarioTopoDir;
			break;
		case "BD":
			this.cenarioValido = this.cenarioBasDir;
			break;
		case "MB":
			this.cenarioValido = this.cenarioMeioBaixo;
			break;
		case "BE":
			this.cenarioValido = this.cenarioBasEsq;
			break;
		}

	}

	

}
