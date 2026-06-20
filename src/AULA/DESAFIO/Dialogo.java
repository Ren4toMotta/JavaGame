package AULA.DESAFIO;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Dialogo {
	public enum Estado { FECHADO, FALA, MENU, MENSAGEM }

	private static final String[] FALAS = {
		"Tempos dificeis, soldado...",
		"Os mortos andam, o ceu nao tem mais cor.",
		"Sobrou pouca gente honesta. Eu vendo,",
		"voce escolhe. Que tal dar uma olhada?"
	};

	private static final String[] OPCOES = {
		"Shotgun         ($ 5)",
		"Cura completa   ($ 2)",
		"Sair"
	};

	private static final int CUSTO_SHOTGUN = 5;
	private static final int CUSTO_CURA = 2;
	private static final int TICKS_POR_CHAR = 2;
	private static final int DURACAO_MSG = 90;

	private Estado estado = Estado.FECHADO;
	private int falaAtual = 0;
	private int charsRevelados = 0;
	private int contadorChar = 0;
	private int opcaoSel = 0;
	private String mensagem = "";
	private int tempoMsg = 0;

	public boolean estaAberto() { return estado != Estado.FECHADO; }
	public Estado getEstado() { return estado; }

	public void abrir() {
		estado = Estado.FALA;
		falaAtual = 0;
		charsRevelados = 0;
		contadorChar = 0;
	}

	public void fechar() {
		estado = Estado.FECHADO;
	}

	public void tick() {
		if (estado == Estado.FALA) {
			String texto = FALAS[falaAtual];
			if (charsRevelados < texto.length()) {
				contadorChar++;
				if (contadorChar >= TICKS_POR_CHAR) {
					charsRevelados++;
					contadorChar = 0;
				}
			}
		} else if (estado == Estado.MENSAGEM) {
			tempoMsg--;
			if (tempoMsg <= 0) fechar();
		}
	}

	// avanca fala ou completa typewriter; usado quando ESPACO eh apertado
	public void confirmar(Player p) {
		if (estado == Estado.FALA) {
			String texto = FALAS[falaAtual];
			if (charsRevelados < texto.length()) {
				charsRevelados = texto.length();
			} else if (falaAtual + 1 < FALAS.length) {
				falaAtual++;
				charsRevelados = 0;
			} else {
				estado = Estado.MENU;
				opcaoSel = 0;
			}
		} else if (estado == Estado.MENU) {
			executarOpcao(p);
		} else if (estado == Estado.MENSAGEM) {
			fechar();
		}
	}

	public void mover(int dy) {
		if (estado != Estado.MENU) return;
		opcaoSel = (opcaoSel + dy + OPCOES.length) % OPCOES.length;
	}

	private void executarOpcao(Player p) {
		switch (opcaoSel) {
			case 0: comprarShotgun(p); break;
			case 1: comprarCura(p); break;
			default: fechar(); break;
		}
	}

	private void comprarShotgun(Player p) {
		if (p.temShotgun) {
			mostrarMsg("Voce ja tem a shotgun.");
		} else if (!p.Inv.gastarDinheiro(CUSTO_SHOTGUN)) {
			mostrarMsg("Dinheiro insuficiente. Precisa de $" + CUSTO_SHOTGUN + ".");
		} else {
			p.temShotgun = true;
			mostrarMsg("Shotgun adquirida. Boa caca.");
		}
	}

	private void comprarCura(Player p) {
		if (p.vida >= Player.VIDA_MAX) {
			mostrarMsg("Voce ja esta cheio de vida.");
		} else if (!p.Inv.gastarDinheiro(CUSTO_CURA)) {
			mostrarMsg("Dinheiro insuficiente. Precisa de $" + CUSTO_CURA + ".");
		} else {
			p.vida = Player.VIDA_MAX;
			mostrarMsg("Vida restaurada.");
		}
	}

	private void mostrarMsg(String m) {
		this.mensagem = m;
		this.tempoMsg = DURACAO_MSG;
		this.estado = Estado.MENSAGEM;
	}

	public void desenhar(Graphics2D d2, int largura, int altura) {
		int caixaX = 40;
		int caixaW = largura - 80;
		int caixaH = 130;
		int caixaY = altura - caixaH - 30;

		d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		d2.setColor(new Color(15, 15, 22));
		d2.fillRoundRect(caixaX, caixaY, caixaW, caixaH, 16, 16);
		d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		d2.setColor(new Color(180, 30, 30));
		d2.drawRoundRect(caixaX, caixaY, caixaW, caixaH, 16, 16);
		d2.drawRoundRect(caixaX + 2, caixaY + 2, caixaW - 4, caixaH - 4, 14, 14);

		d2.setColor(new Color(255, 220, 80));
		d2.setFont(new Font("Monospaced", Font.BOLD, 14));
		d2.drawString("MERCADOR", caixaX + 20, caixaY + 22);

		if (estado == Estado.FALA) {
			String texto = FALAS[falaAtual];
			String visivel = texto.substring(0, Math.min(charsRevelados, texto.length()));
			d2.setColor(Color.WHITE);
			d2.setFont(new Font("Monospaced", Font.BOLD, 20));
			d2.drawString(visivel, caixaX + 24, caixaY + 60);
			boolean completo = charsRevelados >= texto.length();
			d2.setColor(completo ? new Color(255, 220, 80) : new Color(140, 140, 140));
			d2.setFont(new Font("Monospaced", Font.BOLD, 14));
			String rod = completo
					? ((falaAtual + 1 < FALAS.length) ? "[ESPACO] continuar" : "[ESPACO] ver loja")
					: "[ESPACO] pular";
			FontMetrics fm = d2.getFontMetrics();
			d2.drawString(rod, caixaX + caixaW - fm.stringWidth(rod) - 16, caixaY + caixaH - 14);
		} else if (estado == Estado.MENU) {
			d2.setColor(Color.WHITE);
			d2.setFont(new Font("Monospaced", Font.BOLD, 18));
			// label generica; valor real desenhado por desenharComJogador
			for (int i = 0; i < OPCOES.length; i++) {
				int y = caixaY + 78 + i * 22;
				if (i == opcaoSel) {
					d2.setColor(new Color(255, 220, 80));
					d2.drawString("> " + OPCOES[i], caixaX + 24, y);
				} else {
					d2.setColor(new Color(200, 200, 200));
					d2.drawString("  " + OPCOES[i], caixaX + 24, y);
				}
			}
			d2.setColor(new Color(140, 140, 140));
			d2.setFont(new Font("Monospaced", Font.BOLD, 14));
			String rod = "[CIMA/BAIXO] escolher  [ESPACO] confirmar";
			FontMetrics fm = d2.getFontMetrics();
			d2.drawString(rod, caixaX + caixaW - fm.stringWidth(rod) - 16, caixaY + caixaH - 14);
		} else if (estado == Estado.MENSAGEM) {
			d2.setColor(new Color(255, 220, 120));
			d2.setFont(new Font("Monospaced", Font.BOLD, 20));
			d2.drawString(mensagem, caixaX + 24, caixaY + 70);
			d2.setColor(new Color(140, 140, 140));
			d2.setFont(new Font("Monospaced", Font.BOLD, 14));
			String rod = "[ESPACO] fechar";
			FontMetrics fm = d2.getFontMetrics();
			d2.drawString(rod, caixaX + caixaW - fm.stringWidth(rod) - 16, caixaY + caixaH - 14);
		}
	}

	// versao do menu que mostra o saldo real do jogador
	public void desenharComJogador(Graphics2D d2, int largura, int altura, Player p) {
		desenhar(d2, largura, altura);
		if (estado == Estado.MENU) {
			int caixaX = 40;
			int caixaW = largura - 80;
			int caixaH = 130;
			int caixaY = altura - caixaH - 30;
			d2.setColor(new Color(15, 15, 22));
			d2.fillRect(caixaX + 4, caixaY + 36, caixaW - 8, 22);
			d2.setColor(new Color(120, 220, 120));
			d2.setFont(new Font("Monospaced", Font.BOLD, 18));
			d2.drawString("Seu saldo: $ " + p.Inv.getDinheiro(), caixaX + 24, caixaY + 52);
		}
	}
}
