package AULA.DESAFIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;

public class Tiro {
	public static final int RAIO = 6;
	public static final double VELOCIDADE = 9.0;
	private static final int LARG_SPRITE = 32;
	private static final int ALTU_SPRITE = 16;
	private static final int TICKS_POR_FRAME = 3;

	private static final Image[] frames = new Image[5];
	private static boolean carregado = false;

	public double posX, posY;
	public double vx, vy;
	public boolean ativo = true;
	public Rectangle areaColisao;

	private int frame = 0;
	private int contadorFrame = 0;
	private double angulo;

	public Tiro(int x, int y, int dx, int dy) {
		carregarSprites();
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
		this.angulo = Math.atan2(this.vy, this.vx);
		this.areaColisao = new Rectangle((int)posX - RAIO, (int)posY - RAIO, RAIO * 2, RAIO * 2);
	}

	private static synchronized void carregarSprites() {
		if (carregado) return;
		String base = "res/PLAYERS/PLAYER_SHOOTING/Zombie-Tileset---_0";
		String suf = "_Capa-";
		for (int i = 0; i < 5; i++) {
			frames[i] = new ImageIcon(base + (370 + i) + suf + (371 + i) + ".png").getImage();
		}
		carregado = true;
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

		contadorFrame++;
		if (contadorFrame >= TICKS_POR_FRAME) {
			frame = (frame + 1) % frames.length;
			contadorFrame = 0;
		}
	}

	public void desenhar(Graphics2D d2) {
		Image img = frames[frame];
		if (img == null) return;
		AffineTransform original = d2.getTransform();
		AffineTransform tx = new AffineTransform();
		tx.translate(posX, posY);
		tx.rotate(angulo);
		tx.translate(-LARG_SPRITE / 2.0, -ALTU_SPRITE / 2.0);
		d2.setTransform(tx);
		d2.drawImage(img, 0, 0, LARG_SPRITE, ALTU_SPRITE, null);
		d2.setTransform(original);
	}
}
