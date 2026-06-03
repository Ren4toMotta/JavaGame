package AULA.DESAFIO;

import java.awt.Graphics2D;

public class tileMap {
	Tiles pecaDoCenario;
	int [][] cenarioValido;
	private String cenaValida;
	int [][] cenarioTopEsq	={	{6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6},// cenario 1
								{3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0},
								{3,1,1,1,1,1,1,1,1,0,1,1,1,1,1,0},
								{3,1,1,1,1,1,1,1,0,0,1,1,1,1,1,0},
								{3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0},
								{3,1,1,1,1,1,1,1,0,1,1,1,4,1,1,0},
								{3,1,1,1,1,1,1,1,1,0,0,1,1,1,1,0},
								{3,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
								{6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6},
								{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}
		};

	int [][] cenarioMeioCima={	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},// cenario 2
            					{0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            					{0,1,1,0,1,1,1,1,1,1,3,1,1,1,1,3},
            					{0,1,1,1,1,1,1,1,1,3,3,3,1,1,1,0},
            					{0,0,1,1,1,1,1,1,1,1,3,1,1,1,1,0},
            					{0,1,1,1,1,1,1,1,1,1,1,1,0,0,5,0},
            					{0,0,0,0,0,1,1,1,0,0,0,1,0,1,1,0},
            					{1,1,1,1,1,1,1,1,1,1,1,1,0,4,1,0},
            					{0,1,1,1,1,1,1,1,1,1,1,1,0,1,1,0},
            					{0,0,0,0,6,0,0,0,0,0,0,0,0,0,0,0}
		};

	int [][] cenarioTopoDir	={	{0,0,0,0,0,0,0,2,2,2,2,0,0,3,3,3},//cenario 3
            					{1,1,1,1,1,1,1,1,2,2,1,1,1,1,3,3},
            					{1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,3},
            					{0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0},
            					{0,1,1,1,1,1,1,1,1,0,0,0,0,0,1,0},
            					{0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0},
            					{0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0},
            					{0,5,0,0,0,0,0,0,0,0,0,0,1,1,3,0},
            					{0,1,1,1,1,1,1,1,1,2,4,0,1,1,3,0},
            					{0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0}
		};
	int [][] cenarioBasDir	={	{0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0},// cenario 4
								{0,4,2,1,1,1,1,1,1,0,1,1,1,1,1,3},
								{1,1,2,1,1,1,1,0,1,0,1,1,1,1,1,3},
								{0,1,3,1,1,1,1,0,1,0,1,1,1,1,1,0},
								{0,1,3,1,0,1,1,1,1,0,1,1,1,1,1,3},
								{0,1,3,0,0,1,1,0,1,0,1,1,1,1,1,0},
								{0,1,1,1,0,0,1,1,0,0,1,1,1,1,1,3},
								{1,1,1,1,1,1,1,1,1,1,1,1,1,1,3,0},
								{0,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2},
								{0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2}
		};
	int [][] cenarioMeioBaixo={ {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},// cenario 5
            					{0,0,3,3,3,3,3,1,1,1,1,1,1,1,1,0},
            					{5,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1},
            					{1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0},
            					{1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,0},
            					{1,1,1,1,1,0,1,1,1,0,0,0,0,0,0,0},
            					{1,1,1,1,0,1,1,1,0,2,2,2,2,2,2,0},
            					{5,1,1,0,1,1,1,1,0,2,4,1,1,1,1,1},
            					{0,0,0,1,1,1,1,0,2,2,2,2,2,2,2,0},
            					{0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0}	
	};
	int [][] cenarioBasEsq= {	{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},// cenario 6
							  	{0,2,1,1,1,0,1,1,1,1,1,1,1,3,3,0},
					            {0,2,2,1,1,0,1,1,1,1,1,1,1,3,5,1},
					            {0,2,1,1,1,1,1,1,1,1,1,1,1,5,1,1},
					            {0,4,4,4,1,1,1,1,1,1,1,1,1,1,1,1},
					            {0,4,4,4,1,0,1,1,1,1,1,1,1,1,1,1},
					            {0,4,4,4,1,0,2,2,1,1,1,1,1,5,1,1},
					            {0,1,1,1,1,0,1,2,1,2,2,1,1,1,5,1},
					            {0,1,1,1,1,0,1,2,2,2,1,1,1,1,1,0},
					            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
			
	};


	public tileMap() {
		this.cenarioValido = this.cenarioTopEsq;
		this.setCenaValida("TE");
		this.pecaDoCenario = new Tiles();
	}
	
	public void desenhar(Graphics2D d2) {
		int pecaDaMatriz;
		for (int col=0; col < this.cenarioValido[0].length; col++ ) {
			for (int lin = 0; lin < this.cenarioValido.length; lin++) {
				pecaDaMatriz = this.cenarioValido[lin][col];
				this.pecaDoCenario.carregaPecaDaMatriz(pecaDaMatriz);
				this.pecaDoCenario.desenhaTile(d2, lin, col);
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
