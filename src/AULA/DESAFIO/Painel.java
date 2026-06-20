package AULA.DESAFIO;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
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
			this.setBackground(Color.yellow);
			this.setPreferredSize(new Dimension(768,100));
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
			int chaves = painelCentro.Jogador.Inv.getQtdChaves();

			D2.setColor(Color.BLACK);
			D2.setFont(new Font("Arial", Font.BOLD,24));
			D2.drawString("Chaves: "+chaves,20 ,55);

			desenhaBarraDeVida(D2, painelCentro.Jogador);
		}
	}

	private void desenhaBarraDeVida(Graphics2D d2, Player p) {
		int x = 240, y = 35, larg = 300, alt = 30;
		d2.setColor(Color.DARK_GRAY);
		d2.fillRect(x, y, larg, alt);
		double frac = Math.max(0.0, (double) p.vida / Player.VIDA_MAX);
		d2.setColor(frac > 0.5 ? new Color(40, 180, 40) : frac > 0.25 ? Color.ORANGE : Color.RED);
		d2.fillRect(x, y, (int)(larg * frac), alt);
		d2.setColor(Color.BLACK);
		d2.drawRect(x, y, larg, alt);
		d2.setFont(new Font("Arial", Font.BOLD, 18));
		d2.drawString("HP " + p.vida + "/" + Player.VIDA_MAX, x + larg + 12, y + 22);
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
