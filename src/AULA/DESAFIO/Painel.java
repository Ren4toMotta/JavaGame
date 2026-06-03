package AULA.DESAFIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Painel extends JPanel{
	private String Posicao;
	Player Jogador = new Player(); 	//instancia o Jogador no Painel
	GameLoop GL; 	//cria o Loop do Jogo
	EscutadorTeclado ET; 	//cria o escutador de teclado
	SpriteLoop SL; 	//cria o loop do Sprite
	tileMap cenario; 	//cria um TileMap
	private Painel painelCentro;
	
	public Painel(String Posicao) {
		this.Posicao = Posicao;
		if (this.Posicao.equals("Centro")) {
			this.setBackground(Color.black);
			this.setPreferredSize(new Dimension(768,480));
			ET = new EscutadorTeclado();
			this.addKeyListener(ET);
			this.setFocusable(true);
			//instancia Loop do Jogo
			GL = new GameLoop(this, ET);	
			GL.start();
			//instancia Loop do Sprite
			SL = new SpriteLoop(this, ET);
			SL.start();	
			//carrega o mapa da cena do jogo (matriz)
			this.cenario = new tileMap();
		} else {
			this.setBackground(Color.yellow);
			this.setPreferredSize(new Dimension(768,100));
			Jogador.Inv.getQtdChaves();
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
			//desenha cenario do jogo
			this.cenario.desenhar(D2);
			Jogador.desenhaJogador(D2);
		} 
		else if(this.Posicao.equals("Sul") && painelCentro != null) {
			int chaves = painelCentro.Jogador.Inv.getQtdChaves();
			
			D2.setColor(Color.BLACK);
			D2.setFont(new Font("Arial", Font.BOLD,24));
			D2.drawString("Chaves: "+chaves,20 ,55);
		}
	}
}
