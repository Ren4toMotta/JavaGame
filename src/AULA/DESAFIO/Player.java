package AULA.DESAFIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Player{
	Image[]imgPlayerDown = new Image[3];
	Image[]imgPlayerRight = new Image[3];
	Image[]imgPlayerLeft = new Image[3];
	Image[]imgPlayerUp = new Image[3];
	Image imagemPlayer;
	private int frameJogador = 0;
	Rectangle AreaColisao;
	public int posX, posY;
	private int Larg, Altu;
	public int passo = 5;
	private boolean olhandoEsquerda = false;
	public Inventario Inv = new Inventario();

	public static final int VIDA_MAX = 5;
	public int vida = VIDA_MAX;
	public int tempoInvuln = 0;
	public static final int DURACAO_INVULN = 30;

	public Player() {
		this.posX = 200;
		this.posY = 100;
		this.Larg = 48;
		this.Altu = 48;
		AreaColisao = new Rectangle();
		this.AreaColisao.x = this.posX + 3;
		this.AreaColisao.y = this.posY + this.Altu/2;
		this.AreaColisao.width = this.Larg - 20;
		this.AreaColisao.height = this.Altu/2;

		for (int i = 0; i < 3; i++) {
			this.imgPlayerDown[i] = new ImageIcon("res/PCAF/down" + (i+1) + ".png").getImage();
			this.imgPlayerRight[i] = new ImageIcon("res/PCAF/right" + (i+1) + ".png").getImage();
			this.imgPlayerUp[i] = new ImageIcon("res/PCAF/up" + (i+1) + ".png").getImage();
		}
		this.imagemPlayer = this.imgPlayerDown[this.frameJogador];
	}

	public void desenhaJogador(Graphics2D d2) {
		// pisca durante invulnerabilidade pra dar feedback visual
		if (tempoInvuln > 0 && (tempoInvuln / 4) % 2 == 0) return;
		if (olhandoEsquerda) {
	        d2.drawImage(imagemPlayer, posX + Larg, posY, -Larg, Altu, null);
	    } else {
	        d2.drawImage(imagemPlayer, posX, posY, Larg, Altu, null);
	    }
	}

	public void atualizaPosicaoJogador(boolean ME, boolean MC, boolean MD, boolean MB) {
		if (ME)	 this.posX -= passo;
		if (MD)	 this.posX += passo;
		if (MC)	 this.posY -= passo;
		if (MB)	 this.posY += passo;

		this.AreaColisao.x = this.posX + 3;
		this.AreaColisao.y = this.posY + this.Altu/2;
	}
	public void atualizaSprite(boolean moveEsq, boolean moveCima,
			boolean moveDir, boolean moveBaixo) {
		this.frameJogador++;
		if (moveEsq) {
			olhandoEsquerda = true;
			if (frameJogador >= this.imgPlayerRight.length)
	            frameJogador = 0;
	        this.imagemPlayer = this.imgPlayerRight[frameJogador];

		}
		if (moveDir) {
			olhandoEsquerda = false;
			if (frameJogador >= this.imgPlayerRight.length)
			frameJogador = 0;

			this.imagemPlayer = this.imgPlayerRight[frameJogador];
		}
		if (moveCima)	{
			if (frameJogador >= this.imgPlayerUp.length)
			frameJogador = 0;

			this.imagemPlayer = this.imgPlayerUp[frameJogador];
		}
		if (moveBaixo)	{
			if (frameJogador >= this.imgPlayerDown.length)
			frameJogador = 0;

			this.imagemPlayer = this.imgPlayerDown[frameJogador];
		}
	}

	public int getCentroX() {
		return this.posX + this.Larg / 2;
	}

	public int getCentroY() {
		return this.posY + this.Altu / 2;
	}

	public void recebeDano(int dano) {
		if (tempoInvuln > 0 || vida <= 0) return;
		this.vida -= dano;
		this.tempoInvuln = DURACAO_INVULN;
		if (this.vida < 0) this.vida = 0;
	}

	public void tickInvuln() {
		if (this.tempoInvuln > 0) this.tempoInvuln--;
	}

	public boolean estaVivo() {
		return this.vida > 0;
	}

	public void resetar() {
		this.posX = 200;
		this.posY = 100;
		this.AreaColisao.x = this.posX + 3;
		this.AreaColisao.y = this.posY + this.Altu/2;
		this.vida = VIDA_MAX;
		this.tempoInvuln = 0;
		this.Inv = new Inventario();
	}
}
