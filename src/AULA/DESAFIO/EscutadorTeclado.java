package AULA.DESAFIO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EscutadorTeclado implements KeyListener{
	public boolean movePraBaixo, movePraCima, movePraEsq, movePraDir;
	public boolean atiraCima, atiraBaixo, atiraEsq, atiraDir;
	public boolean reiniciar;
	public boolean espaco;
	public boolean espacoConsumido;
	public boolean trocaPraPistola;
	public boolean trocaPraShotgun;

	@Override
	public void keyTyped(KeyEvent e) {
		// NÃO SERÁ UTILIZADA, MAS NÃO PODE SER APAGADA
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int numTecla = e.getKeyCode();
		switch (numTecla) {
		case KeyEvent.VK_A:
			this.movePraEsq = true;
			break;
		case KeyEvent.VK_W:
			this.movePraCima = true;
			break;
		case KeyEvent.VK_D:
			this.movePraDir = true;
			break;
		case KeyEvent.VK_S:
			this.movePraBaixo = true;
			break;
		case KeyEvent.VK_LEFT:
			this.atiraEsq = true;
			break;
		case KeyEvent.VK_UP:
			this.atiraCima = true;
			break;
		case KeyEvent.VK_RIGHT:
			this.atiraDir = true;
			break;
		case KeyEvent.VK_DOWN:
			this.atiraBaixo = true;
			break;
		case KeyEvent.VK_R:
			this.reiniciar = true;
			break;
		case KeyEvent.VK_SPACE:
			if (!this.espaco) this.espacoConsumido = false;
			this.espaco = true;
			break;
		case KeyEvent.VK_1:
			this.trocaPraPistola = true;
			break;
		case KeyEvent.VK_2:
			this.trocaPraShotgun = true;
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int numTecla = e.getKeyCode();
		switch (numTecla) {
		case KeyEvent.VK_A:
			this.movePraEsq = false;
			break;
		case KeyEvent.VK_W:
			this.movePraCima = false;
			break;
		case KeyEvent.VK_D:
			this.movePraDir = false;
			break;
		case KeyEvent.VK_S:
			this.movePraBaixo = false;
			break;
		case KeyEvent.VK_LEFT:
			this.atiraEsq = false;
			break;
		case KeyEvent.VK_UP:
			this.atiraCima = false;
			break;
		case KeyEvent.VK_RIGHT:
			this.atiraDir = false;
			break;
		case KeyEvent.VK_DOWN:
			this.atiraBaixo = false;
			break;
		case KeyEvent.VK_R:
			this.reiniciar = false;
			break;
		case KeyEvent.VK_SPACE:
			this.espaco = false;
			this.espacoConsumido = false;
			break;
		default:
			break;
		}
	}
}
