package AULA.DESAFIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class GameLoop extends Thread implements Runnable, ActionListener{
	private int FPS = 60;
	private Timer controleDoTempoDoJogo;
	private long contadorDeFPS;
	private Painel CenaDoJogo;
	private EscutadorTeclado ET;
	private int desdeUltimoTiro = 0;
	private static final int COOLDOWN_TIRO = 12;
	private String cenaAnterior = null;


	public GameLoop(Painel cenaDoJogo, EscutadorTeclado eT) {
		System.out.println("GameLoop instanciado!");
		this.CenaDoJogo = cenaDoJogo;
		this.ET = eT;
	}
	@Override
	public void run() {
		this.contadorDeFPS = 0;
		this.controleDoTempoDoJogo = new Timer(1000, this);
		this.controleDoTempoDoJogo.start();
		//----------------------------------------
		double frameRate = 1000000000/this.FPS;
		double tempoDecorrido = 0;
		long tempoUltimaMedidaDoLoop = System.nanoTime();
		long tempoAtualDoLoop;
		//----------------------------------------
		while (this.isAlive()){
			tempoAtualDoLoop = System.nanoTime();
			tempoDecorrido = tempoDecorrido +
				(tempoAtualDoLoop - tempoUltimaMedidaDoLoop)/frameRate;
			tempoUltimaMedidaDoLoop = tempoAtualDoLoop;

			if (tempoDecorrido >=1) {
				try {
					tick();
				} catch (Throwable t) {
					System.err.println("[GameLoop] erro no tick:");
					t.printStackTrace();
				}
				CenaDoJogo.repaint();
				if(CenaDoJogo.getParent() != null) CenaDoJogo.getParent().repaint();

				this.contadorDeFPS++;
				tempoDecorrido = 0;
			}
		}
	}

	private void tick() {
		if (CenaDoJogo.gameOver) {
			if (ET.reiniciar) reiniciar();
			return;
		}

		// movimento do player
		String direcao = "";
		if (ET.movePraCima) 	direcao = "cima";
		if (ET.movePraBaixo) 	direcao = "baixo";
		if (ET.movePraDir) 		direcao = "direita";
		if (ET.movePraEsq) 		direcao = "esquerda";

		VerificadorDeColisao colisao = new VerificadorDeColisao();
		boolean bateu = colisao.OcorreuColisao(this.CenaDoJogo.Jogador, this.CenaDoJogo.cenario, direcao);
		if (!bateu) {
			CenaDoJogo.Jogador.atualizaPosicaoJogador(ET.movePraEsq, ET.movePraCima,
					ET.movePraDir, ET.movePraBaixo);
		}

		// se a cena mudou, limpa tiros (nao atravessam mapas)
		String cenaAtual = CenaDoJogo.cenario.getCenaValida();
		if (cenaAnterior != null && !cenaAnterior.equals(cenaAtual)) {
			synchronized (CenaDoJogo.tiros) {
				CenaDoJogo.tiros.clear();
			}
		}
		cenaAnterior = cenaAtual;

		CenaDoJogo.cenario.pecaDoCenario.atualizaAnimacaoAgua();

		// disparar
		desdeUltimoTiro++;
		int dx = 0, dy = 0;
		if (ET.atiraEsq)  dx -= 1;
		if (ET.atiraDir)  dx += 1;
		if (ET.atiraCima) dy -= 1;
		if (ET.atiraBaixo)dy += 1;
		if ((dx != 0 || dy != 0) && desdeUltimoTiro >= COOLDOWN_TIRO) {
			Tiro t = new Tiro(CenaDoJogo.Jogador.getCentroX(),
							  CenaDoJogo.Jogador.getCentroY(), dx, dy);
			synchronized (CenaDoJogo.tiros) {
				CenaDoJogo.tiros.add(t);
			}
			desdeUltimoTiro = 0;
		}

		// atualizar tiros + colisao com zumbis
		ArrayList<Zumbi> zumbis = CenaDoJogo.cenario.getZumbisAtuais();
		synchronized (CenaDoJogo.tiros) {
			for (int i = 0; i < CenaDoJogo.tiros.size(); i++) {
				Tiro t = CenaDoJogo.tiros.get(i);
				t.atualizar(CenaDoJogo.cenario);
				if (!t.ativo) continue;
				synchronized (zumbis) {
					for (int j = 0; j < zumbis.size(); j++) {
						Zumbi z = zumbis.get(j);
						if (z.vivo && t.areaColisao.intersects(z.areaColisao)) {
							z.recebeDano();
							t.ativo = false;
							break;
						}
					}
				}
			}
			// remove inativos
			for (int i = CenaDoJogo.tiros.size() - 1; i >= 0; i--) {
				if (!CenaDoJogo.tiros.get(i).ativo) CenaDoJogo.tiros.remove(i);
			}
		}

		// atualizar zumbis + colisao com player
		synchronized (zumbis) {
			for (int i = 0; i < zumbis.size(); i++) {
				Zumbi z = zumbis.get(i);
				z.atualizar(CenaDoJogo.Jogador, CenaDoJogo.cenario);
				if (z.vivo && z.areaColisao.intersects(CenaDoJogo.Jogador.AreaColisao)) {
					CenaDoJogo.Jogador.recebeDano(1);
				}
			}
			for (int i = zumbis.size() - 1; i >= 0; i--) {
				if (!zumbis.get(i).vivo) zumbis.remove(i);
			}
		}

		CenaDoJogo.Jogador.tickInvuln();

		if (!CenaDoJogo.Jogador.estaVivo()) {
			CenaDoJogo.gameOver = true;
		}
	}

	private void reiniciar() {
		CenaDoJogo.Jogador.resetar();
		CenaDoJogo.cenario.setCenaValida("TE");
		CenaDoJogo.cenario.resetarZumbis();
		synchronized (CenaDoJogo.tiros) {
			CenaDoJogo.tiros.clear();
		}
		desdeUltimoTiro = 0;
		cenaAnterior = "TE";
		CenaDoJogo.gameOver = false;
		ET.reiniciar = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("FPS GameLoop: " + this.contadorDeFPS);
		this.contadorDeFPS = 0;
	}
}
