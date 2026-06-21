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
	private static final int COOLDOWN_PISTOLA = 12;
	private static final int COOLDOWN_SHOTGUN = 22;
	private String cenaAnterior = null;
	private boolean atiraCimaAnt, atiraBaixoAnt;


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
		if (CenaDoJogo.intro != null && CenaDoJogo.intro.estaAtiva()) {
			CenaDoJogo.intro.tick();
			if (ET.espaco && !ET.espacoConsumido) {
				CenaDoJogo.intro.avancar();
				ET.espacoConsumido = true;
			}
			return;
		}
		if (CenaDoJogo.gameOver) {
			if (ET.reiniciar) reiniciar();
			return;
		}

		// dialogo do NPC: pausa o resto do jogo
		if (CenaDoJogo.dialogo.estaAberto()) {
			CenaDoJogo.dialogo.tick();
			// navegacao no menu (borda de subida de cima/baixo)
			if (ET.atiraCima && !atiraCimaAnt) CenaDoJogo.dialogo.mover(-1);
			if (ET.atiraBaixo && !atiraBaixoAnt) CenaDoJogo.dialogo.mover(1);
			if (ET.espaco && !ET.espacoConsumido) {
				CenaDoJogo.dialogo.confirmar(CenaDoJogo.Jogador);
				ET.espacoConsumido = true;
			}
			atiraCimaAnt = ET.atiraCima;
			atiraBaixoAnt = ET.atiraBaixo;
			// ainda anima NPC
			if (CenaDoJogo.cenario.npcTD != null) CenaDoJogo.cenario.npcTD.tick();
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

		// animacao do NPC e abertura de dialogo
		if (CenaDoJogo.cenario.npcTD != null) {
			CenaDoJogo.cenario.npcTD.tick();
			if ("TD".equals(CenaDoJogo.cenario.getCenaValida())
					&& CenaDoJogo.cenario.zumbisVivosNa("TD") == 0
					&& CenaDoJogo.cenario.npcTD.playerEstaProximo(CenaDoJogo.Jogador)
					&& ET.espaco && !ET.espacoConsumido) {
				CenaDoJogo.dialogo.abrir();
				ET.espacoConsumido = true;
			}
		}
		atiraCimaAnt = ET.atiraCima;
		atiraBaixoAnt = ET.atiraBaixo;

		// troca de arma
		if (ET.trocaPraPistola) {
			CenaDoJogo.Jogador.armaAtual = 0;
			ET.trocaPraPistola = false;
		}
		if (ET.trocaPraShotgun) {
			if (CenaDoJogo.Jogador.temShotgun) CenaDoJogo.Jogador.armaAtual = 1;
			ET.trocaPraShotgun = false;
		}

		// disparar
		desdeUltimoTiro++;
		int dx = 0, dy = 0;
		if (ET.atiraEsq)  dx -= 1;
		if (ET.atiraDir)  dx += 1;
		if (ET.atiraCima) dy -= 1;
		if (ET.atiraBaixo)dy += 1;
		int cooldown = (CenaDoJogo.Jogador.armaAtual == 1) ? COOLDOWN_SHOTGUN : COOLDOWN_PISTOLA;
		if ((dx != 0 || dy != 0) && desdeUltimoTiro >= cooldown) {
			int cx = CenaDoJogo.Jogador.getCentroX();
			int cy = CenaDoJogo.Jogador.getCentroY();
			synchronized (CenaDoJogo.tiros) {
				if (CenaDoJogo.Jogador.armaAtual == 1) {
					// shotgun: 3 projeteis em cone (~30 graus)
					double base = Math.atan2(dy, dx);
					double[] offsets = { -Math.PI / 12, 0, Math.PI / 12 };
					for (double off : offsets) {
						double ang = base + off;
						int tdx = (int) Math.round(Math.cos(ang) * 1000);
						int tdy = (int) Math.round(Math.sin(ang) * 1000);
						CenaDoJogo.tiros.add(new Tiro(cx, cy, tdx, tdy));
					}
				} else {
					CenaDoJogo.tiros.add(new Tiro(cx, cy, dx, dy));
				}
			}
			Som.tocarTiro();
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
		java.util.ArrayList<Dinheiro> moedas = CenaDoJogo.cenario.getMoedasAtuais();
		synchronized (zumbis) {
			for (int i = 0; i < zumbis.size(); i++) {
				Zumbi z = zumbis.get(i);
				z.atualizar(CenaDoJogo.Jogador, CenaDoJogo.cenario, zumbis);
				if (z.vivo && z.areaColisao.intersects(CenaDoJogo.Jogador.AreaColisao)) {
					CenaDoJogo.Jogador.recebeDano(1);
				}
			}
			for (int i = zumbis.size() - 1; i >= 0; i--) {
				if (!zumbis.get(i).vivo) {
					Zumbi morto = zumbis.get(i);
					int cx = morto.posX + Zumbi.LARG / 2;
					int cy = morto.posY + Zumbi.ALTU / 2;
					synchronized (moedas) {
						moedas.add(new Dinheiro(cx, cy));
					}
					zumbis.remove(i);
				}
			}
		}

		// atualizar moedas + coleta pelo player
		synchronized (moedas) {
			for (int i = 0; i < moedas.size(); i++) {
				Dinheiro m = moedas.get(i);
				m.tick();
				if (!m.coletada && m.areaColisao.intersects(CenaDoJogo.Jogador.AreaColisao)) {
					CenaDoJogo.Jogador.Inv.adicionarDinheiro(m.valor);
					m.coletada = true;
					Som.tocarMoeda();
				}
			}
			for (int i = moedas.size() - 1; i >= 0; i--) {
				if (moedas.get(i).coletada) moedas.remove(i);
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
