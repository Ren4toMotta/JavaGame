package AULA.DESAFIO;
import java.awt.Color;
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

	
	public Player() {
		//atributos do Jogador
		this.posX = 200;
		this.posY = 100;
		this.Larg = 48;
		this.Altu = 48;
		AreaColisao = new Rectangle();
		//atributos da Area de Colisao do Jogador
		this.AreaColisao.x = this.posX + 3;
		this.AreaColisao.y = this.posY + this.Altu/2;;
		this.AreaColisao.width = this.Larg - 20;
		this.AreaColisao.height = this.Altu/2;
		
		for (int i = 0; i < 3; i++) {
			this.imgPlayerDown[i] = new ImageIcon("res/PCAF/down" + (i+1) + ".png").getImage();
			this.imgPlayerRight[i] = new ImageIcon("res/PCAF/right" + (i+1) + ".png").getImage();
			//this.imgPlayerLeft[i] = new ImageIcon("res/PLAYERS/left" + (i+1) + ".png").getImage(); // como n tem sprite dele andando pra esquerda tem q inventa no código
			this.imgPlayerUp[i] = new ImageIcon("res/PCAF/up" + (i+1) + ".png").getImage();
		}		
		this.imagemPlayer = this.imgPlayerDown[this.frameJogador];
	}
	
	public void desenhaJogador(Graphics2D d2) {
		//d2.fillRect(this.AreaColisao.x, this.AreaColisao.y, this.AreaColisao.width, this.AreaColisao.height); //   <-- "liga" a visualização da colisao do personagem
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
		this.AreaColisao.y = this.posY + this.Altu/2;;
		
		//System.out.println("Coluna :" + (int)this.AreaColisao.x/48);
		//System.out.println("Linha  :" + (int)this.AreaColisao.y/48);
		
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
}
