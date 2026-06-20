package AULA.DESAFIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Tiro {
	public static final int RAIO = 6;
	public static final double VELOCIDADE = 9.0;

	public double posX, posY;
	public double vx, vy;
	public boolean ativo = true;
	public Rectangle areaColisao;

	public Tiro(int x, int y, int dx, int dy) {
		this.posX = x;
		this.posY = y;
		double mag = Math.sqrt(dx * dx + dy * dy);
		if (mag == 0) {
			this.ativo = false;
			this.vx = 0;
			this.vy = 0;
		} else {
			this.vx = (dx / mag) * VELOCIDADE;
			this.vy = (dy / mag) * VELOCIDADE;
		}
		this.areaColisao = new Rectangle((int)posX - RAIO, (int)posY - RAIO, RAIO * 2, RAIO * 2);
	}

	public void atualizar(tileMap cena) {
		if (!ativo) return;
		this.posX += this.vx;
		this.posY += this.vy;
		this.areaColisao.x = (int)posX - RAIO;
		this.areaColisao.y = (int)posY - RAIO;

		int larguraCenario = cena.cenarioValido[0].length * 48;
		int alturaCenario  = cena.cenarioValido.length * 48;
		if (posX < 0 || posY < 0 || posX > larguraCenario || posY > alturaCenario) {
			this.ativo = false;
			return;
		}
		if (VerificadorDeColisao.colideComTileEm(this.areaColisao, cena)) {
			this.ativo = false;
		}
	}

	public void desenhar(Graphics2D d2) {
		d2.setColor(Color.YELLOW);
		d2.fillOval((int)posX - RAIO, (int)posY - RAIO, RAIO * 2, RAIO * 2);
		d2.setColor(Color.ORANGE);
		d2.drawOval((int)posX - RAIO, (int)posY - RAIO, RAIO * 2, RAIO * 2);
	}
}
