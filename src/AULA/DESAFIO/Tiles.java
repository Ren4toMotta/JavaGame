package AULA.DESAFIO;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Tiles {
	private final int largura = 48, altura = 48; //constantes
	private int posX, posY;
	private String caminhoImg;
	private Image imgAtual;
	private Image imgGrass, imgSand, imgWall, imgWall2;
	private Image imgWhite, imgGray;
	private Image imgCarDestroyed, imgCarGray, imgCarOrange;
	private Image[] imgWater = new Image[3];
	private int frameAgua = 0;
    private int contadorAgua = 0;
    private final int VELOCIDADE_AGUA = 35;
	private boolean colisao;
	private boolean ehCarro;

	
	public Tiles() {
			this.carregaImagemTile();
	}
	
	public void desenhaTile(Graphics2D d2, int linha, int coluna) {
		this.posX = coluna * this.largura;
		this.posY = linha * this.altura;
		if (this.ehCarro) {
			// na primeira passada do tileMap so desenha o chao; carros sao desenhados depois
			d2.drawImage(this.imgSand, this.posX, this.posY, this.largura, this.altura, null);
		} else {
			d2.drawImage(this.imgAtual, this.posX, this.posY, this.largura, this.altura, null);
		}
	}

	public void desenhaCarro(Graphics2D d2, int linha, int coluna, int valor) {
		Image img;
		switch (valor) {
			case 7: img = this.imgCarDestroyed; break;
			case 8: img = this.imgCarGray; break;
			case 9: img = this.imgCarOrange; break;
			default: return;
		}
		// 33x18 -> 72x40 (escala ~2.2x), centralizado no tile, com overflow lateral
		int largCarro = 72, altCarro = 40;
		int cx = coluna * this.largura + this.largura / 2;
		int cy = linha * this.altura + this.altura / 2;
		d2.drawImage(img, cx - largCarro / 2, cy - altCarro / 2, largCarro, altCarro, null);
	}
	
	private void carregaImagemTile() {
		ImageIcon icon;
		icon = new ImageIcon("res/TILES/grass1.png");
		this.imgGrass = icon.getImage();
		icon = new ImageIcon("res/TERRAIN/grass1.png");
		this.imgSand = icon.getImage();

		icon = new ImageIcon("res/tiles/wall1.png");
		this.imgWall = icon.getImage();		
		icon = new ImageIcon("res/tiles/white.png");
		this.imgWhite= icon.getImage();
		icon = new ImageIcon("res/tiles/gray.png");
		this.imgGray = icon.getImage();	
		icon = new ImageIcon("res/TERRAIN/wall2.png");
		this.imgWall2 = icon.getImage();
		
		for(int i = 0; i < 3; i++) {
			this.imgWater[i] = new ImageIcon
			("res/TERRAIN/water"+(i+1)+".png").getImage();
		}

		this.imgCarDestroyed = new ImageIcon("res/TERRAIN/BROKEN_CARS/DESTROYED_CAR.png").getImage();
		this.imgCarGray      = new ImageIcon("res/TERRAIN/BROKEN_CARS/GRAY_CAR.png").getImage();
		this.imgCarOrange    = new ImageIcon("res/TERRAIN/BROKEN_CARS/ORANGE_CAR.png").getImage();
	}
	
	public void atualizaAnimacaoAgua() {
        this.contadorAgua++;
        if (this.contadorAgua >= VELOCIDADE_AGUA) {
            this.frameAgua++;
            if (this.frameAgua >= imgWater.length)
                this.frameAgua = 0;
            this.contadorAgua = 0;
        }
    }
	
	public void carregaPecaDaMatriz(int valorDaPeca) {
		this.ehCarro = false;
		if (valorDaPeca == 0) {
			this.imgAtual = this.imgWall;
			this.colisao = true;
		}
		if (valorDaPeca == 1) {
			this.imgAtual = this.imgSand;
			this.colisao = false;
		}
		if (valorDaPeca == 2) {
			this.imgAtual = this.imgWater[frameAgua];
			this.colisao = true;
		}
		if (valorDaPeca == 3) {
			this.imgAtual = this.imgGrass;
			this.colisao = false;
		}
		if (valorDaPeca == 4) {
			this.imgAtual = this.imgWhite;
			this.colisao = false;
		}
		if (valorDaPeca == 5) {
			this.imgAtual = this.imgGray;
			this.colisao = true;
		}
		if(valorDaPeca == 6) {
			this.imgAtual = this.imgWall2;
			this.colisao = true;
		}
		if (valorDaPeca == 7) {
			this.imgAtual = this.imgCarDestroyed;
			this.colisao = true;
			this.ehCarro = true;
		}
		if (valorDaPeca == 8) {
			this.imgAtual = this.imgCarGray;
			this.colisao = true;
			this.ehCarro = true;
		}
		if (valorDaPeca == 9) {
			this.imgAtual = this.imgCarOrange;
			this.colisao = true;
			this.ehCarro = true;
		}
		
		//if (this.colisao == true) 	this.imgAtual = this.imgGray;
		//else						this.imgAtual = this.imgWhite;
	}
	public boolean isColisao() {
		return colisao;
	}
	public void setColisao(boolean colisao) {
		this.colisao = colisao;
	}

}

