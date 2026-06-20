package AULA.DESAFIO;

import java.util.ArrayList;

public class SpawnZumbis {
	private static final int TILE = 48;
	private static final int MARGEM = 4;

	public static ArrayList<Zumbi> criaZumbisDaCena(String chave) {
		ArrayList<Zumbi> lista = new ArrayList<>();
		switch (chave) {
		case "TE":
			lista.add(novo(7, 8));
			lista.add(novo(5, 14));
			lista.add(novo(3, 14));
			break;
		case "MC":
			lista.add(novo(4, 4));
			lista.add(novo(7, 6));
			lista.add(novo(8, 8));
			break;
		case "TD":
			lista.add(novo(4, 3));
			lista.add(novo(5, 5));
			lista.add(novo(6, 7));
			break;
		case "BD":
			lista.add(novo(3, 4));
			lista.add(novo(4, 5));
			lista.add(novo(7, 11));
			break;
		case "MB":
			lista.add(novo(3, 1));
			lista.add(novo(4, 2));
			lista.add(novo(7, 13));
			break;
		case "BE":
			lista.add(novo(4, 4));
			lista.add(novo(7, 2));
			lista.add(novo(8, 10));
			break;
		}
		return lista;
	}

	private static Zumbi novo(int linha, int coluna) {
		return new Zumbi(coluna * TILE + MARGEM, linha * TILE + MARGEM);
	}
}
