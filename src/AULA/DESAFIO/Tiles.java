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
	private Image[] imgWater = new Image[3];
	private int frameAgua = 0;              
    private int contadorAgua = 0;           
    private final int VELOCIDADE_AGUA = 35;
	private boolean colisao;

	
	public Tiles() {
			this.carregaImagemTile();
	}
	
	public void desenhaTile(Graphics2D d2, int linha, int coluna) {
		this.posX = coluna * this.largura;
		this.posY = linha * this.altura;
		d2.drawImage(this.imgAtual, this.posX, this.posY, this.largura, this.altura, null);		
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

