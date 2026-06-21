package AULA.DESAFIO;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
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
	boolean venceu = false;
	Introducao intro = new Introducao();
	Dialogo dialogo = new Dialogo();
	private Painel painelCentro;
	private Image imgHealth, imgHealthPart;
	private Image imgGunSlot, imgEmptySlot, imgShotgunSlot;

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
			this.imgShotgunSlot = new ImageIcon("res/UI/SHOTGUN_SLOT.png").getImage();
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
			if (intro != null && intro.estaAtiva()) {
				intro.desenhar(D2, getWidth(), getHeight());
				return;
			}
			this.cenario.desenhar(D2);
			ArrayList<Dinheiro> ms = this.cenario.getMoedasAtuais();
			synchronized (ms) {
				for (int i = 0; i < ms.size(); i++) ms.get(i).desenhar(D2);
			}
			ArrayList<Zumbi> zs = this.cenario.getZumbisAtuais();
			synchronized (zs) {
				for (int i = 0; i < zs.size(); i++) zs.get(i).desenhar(D2);
			}
			if ("TD".equals(cenario.getCenaValida()) && cenario.npcTD != null) {
				cenario.npcTD.desenhar(D2);
				if (cenario.npcTD.playerEstaProximo(Jogador) && !dialogo.estaAberto()) {
					int vivos = cenario.zumbisVivosNa("TD");
					String dica = vivos == 0
							? "[ESPACO] conversar"
							: "Faltam " + vivos + " zumbis aqui";
					desenhaDicaInteracao(D2,
							cenario.npcTD.posX + NPC.LARG / 2,
							cenario.npcTD.posY - 6,
							dica,
							vivos == 0);
				}
			}
			Jogador.desenhaJogador(D2);
			synchronized (tiros) {
				for (int i = 0; i < tiros.size(); i++) tiros.get(i).desenhar(D2);
			}
			if (dialogo.estaAberto()) dialogo.desenharComJogador(D2, getWidth(), getHeight(), Jogador);
			if (gameOver) desenhaGameOver(D2);
			if (venceu) desenhaConcluido(D2);
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
		d2.setFont(new Font("Monospaced", Font.BOLD, 18));
		d2.drawString("CHAVES: " + p.Inv.getQtdChaves(), margem, 26);
		d2.setColor(new Color(120, 220, 120));
		d2.drawString("$ " + p.Inv.getDinheiro(), margem, 50);
		d2.setColor(Color.WHITE);

		// HEALTH sprite (40x6 -> 120x18, escala 3x)
		int yHealth = 76;
		int largHealth = 120, altHealth = 18;
		if (imgHealth != null) {
			d2.drawImage(imgHealth, margem, yHealth, largHealth, altHealth, null);
		}

		// HEALTH_PART (4x6 -> 12x18) repetido por ponto de vida
		int xParte = margem + largHealth + 10;
		int largParte = 12, altParte = 18;
		int gap = 4;
		for (int i = 0; i < p.vida; i++) {
			if (imgHealthPart != null) {
				d2.drawImage(imgHealthPart,
						xParte + i * (largParte + gap), yHealth,
						largParte, altParte, null);
			}
		}
	}

	private void desenhaSlots(Graphics2D d2) {
		int largSlot = 96, altSlot = 66;
		int gap = 10;
		int totalSlots = 4;
		int largTotal = totalSlots * largSlot + (totalSlots - 1) * gap;
		int x = getWidth() - largTotal - 24;
		int y = (getHeight() - altSlot) / 2;

		Image[] slots = new Image[4];
		slots[0] = imgGunSlot;
		slots[1] = (painelCentro != null && painelCentro.Jogador.temShotgun) ? imgShotgunSlot : imgEmptySlot;
		slots[2] = imgEmptySlot;
		slots[3] = imgEmptySlot;

		int armaSelecionada = (painelCentro != null) ? painelCentro.Jogador.armaAtual : 0;

		for (int i = 0; i < totalSlots; i++) {
			int sx = x + i * (largSlot + gap);
			if (slots[i] != null) d2.drawImage(slots[i], sx, y, largSlot, altSlot, null);
			if (i == armaSelecionada) {
				d2.setColor(new Color(255, 220, 80));
				d2.setStroke(new BasicStroke(3));
				d2.drawRoundRect(sx - 2, y - 2, largSlot + 4, altSlot + 4, 6, 6);
				d2.setStroke(new BasicStroke(1));
			}
			d2.setColor(Color.WHITE);
			d2.setFont(new Font("Monospaced", Font.BOLD, 12));
			d2.drawString(String.valueOf(i + 1), sx + 4, y + 14);
		}
	}

	private void desenhaDicaInteracao(Graphics2D d2, int cx, int cy, String texto, boolean liberado) {
		d2.setFont(new Font("Monospaced", Font.BOLD, 14));
		FontMetrics fm = d2.getFontMetrics();
		int w = fm.stringWidth(texto);
		d2.setColor(new Color(0, 0, 0, 180));
		d2.fillRoundRect(cx - w/2 - 8, cy - 16, w + 16, 22, 8, 8);
		d2.setColor(liberado ? new Color(255, 220, 80) : new Color(220, 80, 80));
		d2.drawString(texto, cx - w/2, cy);
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

	private void desenhaConcluido(Graphics2D d2) {
		int w = getWidth(), h = getHeight();
		// fundo escurecido
		d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.72f));
		d2.setColor(new Color(8, 20, 8));
		d2.fillRect(0, 0, w, h);
		d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

		int cx = w / 2;
		int yTitulo = h / 2 - 40;

		// raios comemorativos atras do titulo
		d2.setColor(new Color(255, 215, 60, 35));
		for (int i = 0; i < 12; i++) {
			double ang = Math.PI * 2 * i / 12;
			int x2 = cx + (int) (Math.cos(ang) * w);
			int y2 = yTitulo + (int) (Math.sin(ang) * w);
			d2.setStroke(new BasicStroke(18));
			d2.drawLine(cx, yTitulo, x2, y2);
		}
		d2.setStroke(new BasicStroke(1));

		// titulo principal com "sombra" dourada
		String titulo = "VOCE VENCEU!";
		d2.setFont(new Font("Arial", Font.BOLD, 70));
		FontMetrics fm = d2.getFontMetrics();
		int xt = (w - fm.stringWidth(titulo)) / 2;
		d2.setColor(new Color(120, 80, 0));
		d2.drawString(titulo, xt + 4, yTitulo + 4);
		d2.setColor(new Color(255, 210, 50));
		d2.drawString(titulo, xt, yTitulo);

		// subtitulo positivo
		d2.setColor(new Color(150, 240, 150));
		d2.setFont(new Font("Arial", Font.BOLD, 26));
		String sub = "Voce sobreviveu ao apocalipse e reuniu todas as chaves!";
		fm = d2.getFontMetrics();
		d2.drawString(sub, (w - fm.stringWidth(sub)) / 2, yTitulo + 46);

		// estatisticas da partida
		d2.setColor(Color.WHITE);
		d2.setFont(new Font("Monospaced", Font.BOLD, 20));
		String stats = "Chaves: " + Jogador.Inv.getChavesColetadas()
				+ "   Dinheiro: $ " + Jogador.Inv.getDinheiro();
		fm = d2.getFontMetrics();
		d2.drawString(stats, (w - fm.stringWidth(stats)) / 2, yTitulo + 88);

		// chamada para reiniciar
		d2.setColor(new Color(230, 230, 230));
		d2.setFont(new Font("Arial", Font.PLAIN, 20));
		String rein = "Pressione R para jogar de novo";
		fm = d2.getFontMetrics();
		d2.drawString(rein, (w - fm.stringWidth(rein)) / 2, yTitulo + 128);
	}
}
