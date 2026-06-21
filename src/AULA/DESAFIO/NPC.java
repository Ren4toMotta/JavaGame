package AULA.DESAFIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class NPC {
	public static final int LARG = 144, ALTU = 81;
	private static final int TICKS_POR_FRAME = 22;
	private static final Image[] frames = new Image[3];
	private static boolean carregado = false;

	public int posX, posY;
	public Rectangle areaProximidade;
	private int frame = 0;
	private int contador = 0;

	public NPC(int x, int y) {
		this.posX = x;
		this.posY = y;
		// area onde player precisa estar pra interagir
		this.areaProximidade = new Rectangle(x - 30, y - 30, LARG + 60, ALTU + 60);
		carregarSprites();
	}

	private static synchronized void carregarSprites() {
		if (carregado) return;
		String base = "res/PLAYERS/NPC/Zombie-Tileset---_0";
		String suf = "_Capa-";
		for (int i = 0; i < 3; i++) {
			frames[i] = new ImageIcon(base + (176 + i) + suf + (177 + i) + ".png").getImage();
		}
		carregado = true;
	}

	public void tick() {
		contador++;
		if (contador >= TICKS_POR_FRAME) {
			frame = (frame + 1) % 3;
			contador = 0;
		}
	}

	public boolean playerEstaProximo(Player p) {
		return areaProximidade.intersects(p.AreaColisao);
	}

	public void desenhar(Graphics2D d2) {
		if (frames[frame] != null) {
			d2.drawImage(frames[frame], posX, posY, LARG, ALTU, null);
		}
	}
}
