package AULA.DESAFIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Zumbi {
	public static final int LARG = 40, ALTU = 40;
	public static final int VIDA_INICIAL = 2;
	public static final int PASSO = 2;
	private int tickMov = 0; // usado para deixar o zumbi um pouco mais lento
	private static final int TICKS_POR_FRAME = 10;
	private static final int DURACAO_DANO = 15;

	public int posX, posY;
	public int vida = VIDA_INICIAL;
	public boolean vivo = true;
	public Rectangle areaColisao;

	private static final Image[] sprUp = new Image[3];
	private static final Image[] sprRight = new Image[3];
	private static final Image[] sprDown = new Image[3];
	private static final Image[] sprUpDmg = new Image[3];
	private static final Image[] sprRightDmg = new Image[3];
	private static final Image[] sprDownDmg = new Image[3];
	private static boolean spritesCarregados = false;

	private int frame = 0;
	private int contadorFrame = 0;
	private int tempoDano = 0;
	private String direcao = "down";
	private boolean olhandoEsquerda = false;

	public Zumbi(int x, int y) {
		this.posX = x;
		this.posY = y;
		this.areaColisao = new Rectangle(x, y, LARG, ALTU);
		carregarSprites();
	}

	private static synchronized void carregarSprites() {
		if (spritesCarregados) return;
		String base = "res/ZOMBIE/Zombie-Tileset---_0";
		String suf = "_Capa-";
		// 0430..0432 = down (frente); 0433..0435 = left (lado); 0436..0438 = up (costas)
		for (int i = 0; i < 3; i++) {
			sprDown[i]  = new ImageIcon(base + (430 + i) + suf + (431 + i) + ".png").getImage();
			sprRight[i] = new ImageIcon(base + (433 + i) + suf + (434 + i) + ".png").getImage();
			sprUp[i]    = new ImageIcon(base + (436 + i) + suf + (437 + i) + ".png").getImage();
		}
		String baseDmg = "res/ZOMBIE/ZOMBIE_DAMAGED/Zombie-Tileset---_0";
		// 0439..0441 = down; 0442..0444 = left; 0445..0447 = up
		for (int i = 0; i < 3; i++) {
			sprDownDmg[i]  = new ImageIcon(baseDmg + (439 + i) + suf + (440 + i) + ".png").getImage();
			sprRightDmg[i] = new ImageIcon(baseDmg + (442 + i) + suf + (443 + i) + ".png").getImage();
			sprUpDmg[i]    = new ImageIcon(baseDmg + (445 + i) + suf + (446 + i) + ".png").getImage();
		}
		spritesCarregados = true;
	}

	public void atualizar(Player p, tileMap cena) {
		if (!vivo) return;
		if (++tickMov % 4 == 0) return; // pula 1 a cada 4 frames -> ~25% mais lento

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

		if (Math.abs(alvoY - posY) > Math.abs(alvoX - posX)) {
			direcao = (dy < 0) ? "up" : "down";
		} else if (dx != 0) {
			direcao = "right";
			// sprite base "right" eh na verdade left; espelhar quando movendo pra direita
			olhandoEsquerda = (dx > 0);
		}

		contadorFrame++;
		if (contadorFrame >= TICKS_POR_FRAME) {
			frame = (frame + 1) % 3;
			contadorFrame = 0;
		}
		if (tempoDano > 0) tempoDano--;
	}

	public void recebeDano() {
		this.vida--;
		this.tempoDano = DURACAO_DANO;
		if (this.vida <= 0) this.vivo = false;
	}

	public void desenhar(Graphics2D d2) {
		if (!vivo) return;
		boolean dmg = tempoDano > 0;
		Image img;
		switch (direcao) {
			case "up":    img = dmg ? sprUpDmg[frame]    : sprUp[frame];    break;
			case "right": img = dmg ? sprRightDmg[frame] : sprRight[frame]; break;
			default:      img = dmg ? sprDownDmg[frame]  : sprDown[frame];  break;
		}
		if (img == null) {
			d2.setColor(new Color(40, 110, 40));
			d2.fillRect(posX, posY, LARG, ALTU);
			return;
		}
		if (olhandoEsquerda && direcao.equals("right")) {
			d2.drawImage(img, posX + LARG, posY, -LARG, ALTU, null);
		} else {
			d2.drawImage(img, posX, posY, LARG, ALTU, null);
		}
	}
}
