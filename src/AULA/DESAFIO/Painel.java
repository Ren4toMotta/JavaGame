package AULA.DESAFIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Painel extends JPanel{
	private static final long serialVersionUID = 1L;
	private String Posicao;
	Player Jogador = new Player();
	GameLoop GL;
	EscutadorTeclado ET;
	SpriteLoop SL;
	tileMap cenario;
	ArrayList<Tiro> tiros = new ArrayList<>();
	boolean gameOver = false;
	private Painel painelCentro;
	private Image imgHealth, imgHealthPart;
	private Image imgGunSlot, imgEmptySlot;

	public Painel(String Posicao) {
		this.Posicao = Posicao;
		if (this.Posicao.equals("Centro")) {
			this.setBackground(Color.black);
			this.setPreferredSize(new Dimension(768,480));
			ET = new EscutadorTeclado();
			this.addKeyListener(ET);
			this.setFocusable(true);
			this.cenario = new tileMap();
			GL = new GameLoop(this, ET);
			GL.start();
			SL = new SpriteLoop(this, ET);
			SL.start();
		} else {
			this.setBackground(Color.BLACK);
			this.setPreferredSize(new Dimension(768,110));
			this.imgHealth = new ImageIcon("res/UI/HEALTH.png").getImage();
			this.imgHealthPart = new ImageIcon("res/UI/HEALTH_PART.png").getImage();
			this.imgGunSlot = new ImageIcon("res/UI/GUN_SLOT.png").getImage();
			this.imgEmptySlot = new ImageIcon("res/UI/EMPTY_SLOT.png").getImage();
		}
	}
	public Painel(String Posicao, Painel centro) {
		this(Posicao);
		this.painelCentro = centro;
	}
	public void paintComponent(Graphics D) {
		Graphics2D D2 = (Graphics2D) D;
		D2.setColor(this.getBackground());
		D2.fillRect(0, 0, this.getWidth(), this.getHeight());
		if (this.Posicao.equals("Centro")) {
			this.cenario.desenhar(D2);
			ArrayList<Zumbi> zs = this.cenario.getZumbisAtuais();
			synchronized (zs) {
				for (int i = 0; i < zs.size(); i++) zs.get(i).desenhar(D2);
			}
			Jogador.desenhaJogador(D2);
			synchronized (tiros) {
				for (int i = 0; i < tiros.size(); i++) tiros.get(i).desenhar(D2);
			}
			if (gameOver) desenhaGameOver(D2);
		}
		else if(this.Posicao.equals("Sul") && painelCentro != null) {
			D2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			desenhaHUDEsquerda(D2, painelCentro.Jogador);
			desenhaSlots(D2);
		}
	}

	private void desenhaHUDEsquerda(Graphics2D d2, Player p) {
		int margem = 24;

		d2.setColor(Color.WHITE);
		d2.setFont(new Font("Monospaced", Font.BOLD, 22));
		d2.drawString("CHAVES: " + p.Inv.getQtdChaves(), margem, 32);

		// HEALTH sprite (40x6 -> 160x24, escala 4x)
		int yHealth = 52;
		int largHealth = 160, altHealth = 24;
		if (imgHealth != null) {
			d2.drawImage(imgHealth, margem, yHealth, largHealth, altHealth, null);
		}

		// HEALTH_PART (4x6 -> 16x24, escala 4x) repetido por ponto de vida
		int xParte = margem + largHealth + 16;
		int largParte = 16, altParte = 24;
		int gap = 6;
		for (int i = 0; i < p.vida; i++) {
			if (imgHealthPart != null) {
				d2.drawImage(imgHealthPart,
						xParte + i * (largParte + gap), yHealth,
						largParte, altParte, null);
			}
		}
	}

	private void desenhaSlots(Graphics2D d2) {
		// SLOT (32x22 -> 96x66, escala 3x)
		int largSlot = 96, altSlot = 66;
		int gap = 10;
		int qtdEmpty = 3;
		int totalSlots = 1 + qtdEmpty;
		int largTotal = totalSlots * largSlot + (totalSlots - 1) * gap;
		int x = getWidth() - largTotal - 24;
		int y = (getHeight() - altSlot) / 2;
		if (imgGunSlot != null) {
			d2.drawImage(imgGunSlot, x, y, largSlot, altSlot, null);
		}
		for (int i = 0; i < qtdEmpty; i++) {
			int sx = x + (i + 1) * (largSlot + gap);
			if (imgEmptySlot != null) {
				d2.drawImage(imgEmptySlot, sx, y, largSlot, altSlot, null);
			}
		}
	}

	private void desenhaGameOver(Graphics2D d2) {
		d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
		d2.setColor(Color.BLACK);
		d2.fillRect(0, 0, getWidth(), getHeight());
		d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

		d2.setColor(Color.RED);
		d2.setFont(new Font("Arial", Font.BOLD, 64));
		String linha1 = "GAME OVER";
		FontMetrics fm = d2.getFontMetrics();
		int x = (getWidth() - fm.stringWidth(linha1)) / 2;
		int y = getHeight() / 2;
		d2.drawString(linha1, x, y);

		d2.setColor(Color.WHITE);
		d2.setFont(new Font("Arial", Font.PLAIN, 22));
		String linha2 = "Pressione R para reiniciar";
		fm = d2.getFontMetrics();
		x = (getWidth() - fm.stringWidth(linha2)) / 2;
		d2.drawString(linha2, x, y + 50);
	}
}
