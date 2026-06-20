package AULA.DESAFIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Zumbi {
	public static final int LARG = 40, ALTU = 40;
	public static final int VIDA_INICIAL = 2;
	public static final int PASSO = 2;

	public int posX, posY;
	public int vida = VIDA_INICIAL;
	public boolean vivo = true;
	public Rectangle areaColisao;

	public Zumbi(int x, int y) {
		this.posX = x;
		this.posY = y;
		this.areaColisao = new Rectangle(x, y, LARG, ALTU);
	}

	public void atualizar(Player p, tileMap cena) {
		if (!vivo) return;

		int alvoX = p.getCentroX() - LARG / 2;
		int alvoY = p.getCentroY() - ALTU / 2;
		int dx = Integer.compare(alvoX, posX);
		int dy = Integer.compare(alvoY, posY);

		if (dx != 0) {
			int novoX = posX + dx * PASSO;
			Rectangle teste = new Rectangle(novoX, posY, LARG, ALTU);
			if (!VerificadorDeColisao.colideComTileEm(teste, cena)) {
				posX = novoX;
			}
		}
		if (dy != 0) {
			int novoY = posY + dy * PASSO;
			Rectangle teste = new Rectangle(posX, novoY, LARG, ALTU);
			if (!VerificadorDeColisao.colideComTileEm(teste, cena)) {
				posY = novoY;
			}
		}
		areaColisao.x = posX;
		areaColisao.y = posY;
	}

	public void recebeDano() {
		this.vida--;
		if (this.vida <= 0) this.vivo = false;
	}

	public void desenhar(Graphics2D d2) {
		if (!vivo) return;
		d2.setColor(new Color(40, 110, 40));
		d2.fillRect(posX, posY, LARG, ALTU);
		d2.setColor(new Color(20, 60, 20));
		d2.drawRect(posX, posY, LARG, ALTU);
		// olhos pra ficar menos abstrato
		d2.setColor(Color.RED);
		d2.fillOval(posX + 8, posY + 12, 6, 6);
		d2.fillOval(posX + LARG - 14, posY + 12, 6, 6);
	}
}
