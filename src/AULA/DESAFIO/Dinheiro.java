package AULA.DESAFIO;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Dinheiro {
	public static final int LARG = 36, ALTU = 21;
	private static final double AMPLITUDE = 5.0;
	private static final double PERIODO = 70.0;
	private static Image imagem;

	public int posX, posY;
	public int valor = 1;
	public boolean coletada = false;
	public Rectangle areaColisao;
	private int tickAnim;

	public Dinheiro(int centroX, int centroY) {
		this.posX = centroX - LARG / 2;
		this.posY = centroY - ALTU / 2;
		this.areaColisao = new Rectangle(posX, posY, LARG, ALTU);
		this.tickAnim = (int)(Math.random() * PERIODO);
		carregar();
	}

	private static synchronized void carregar() {
		if (imagem == null) imagem = new ImageIcon("res/PLAYERS/MONEY.png").getImage();
	}

	public void tick() {
		tickAnim++;
		int offY = (int) Math.round(Math.sin(2 * Math.PI * tickAnim / PERIODO) * AMPLITUDE);
		areaColisao.x = posX;
		areaColisao.y = posY + offY;
	}

	public void desenhar(Graphics2D d2) {
		if (coletada || imagem == null) return;
		int offY = (int) Math.round(Math.sin(2 * Math.PI * tickAnim / PERIODO) * AMPLITUDE);
		// sombra elipse abaixo, encolhe quando esta no alto
		float sombraEscala = 1.0f - (offY + (float)AMPLITUDE) / (2f * (float)AMPLITUDE) * 0.4f;
		int sLarg = (int)(LARG * sombraEscala);
		int sX = posX + (LARG - sLarg) / 2;
		d2.setColor(new Color(0, 0, 0, 90));
		d2.fillOval(sX, posY + ALTU + 4, sLarg, 5);

		d2.drawImage(imagem, posX, posY + offY, LARG, ALTU, null);
	}
}
