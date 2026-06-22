package AULA.DESAFIO;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Introducao {
	private static final String TEXTO =
			"A cidade caiu.\n" +
			"Os mortos vivos tomaram as ruas\n" +
			"e devoraram tudo o que encontraram.\n" +
			"\n" +
			"Voce e o ultimo sobrevivente.\n" +
			"Empunhe sua arma e nao desista.\n" +
			"\n" +
			"OBJETIVO: derrotar todos os zumbis e encontrar todos os dispositivos." +
			"\n" +
			"Boa sorte.";

	private static final int TICKS_POR_CHAR = 2;

	private boolean ativa = true;
	private int charsRevelados = 0;
	private int contador = 0;
	private Image imgPlayer;

	public Introducao() {
		this.imgPlayer = new ImageIcon("res/PCAF/down1.png").getImage();
	}

	public boolean estaAtiva() { return ativa; }

	public void tick() {
		if (charsRevelados < TEXTO.length()) {
			contador++;
			if (contador >= TICKS_POR_CHAR) {
				charsRevelados++;
				contador = 0;
			}
		}
	}

	public void avancar() {
		if (charsRevelados < TEXTO.length()) {
			charsRevelados = TEXTO.length();
		} else {
			ativa = false;
		}
	}

	public void desenhar(Graphics2D d2, int largura, int altura) {
		d2.setColor(new Color(10, 10, 14));
		d2.fillRect(0, 0, largura, altura);

		desenhaPlayer(d2, largura, altura);
		desenhaCaixaTexto(d2, largura, altura);
	}

	private void desenhaPlayer(Graphics2D d2, int largura, int altura) {
		int largPlayer = 192, altPlayer = 192;
		int x = 60;
		int y = (altura - altPlayer) / 2;
		// chao circular pra dar contexto
		d2.setColor(new Color(30, 30, 40));
		d2.fillOval(x - 10, y + altPlayer - 40, largPlayer + 20, 60);
		if (imgPlayer != null) {
			d2.drawImage(imgPlayer, x, y, largPlayer, altPlayer, null);
		}
	}

	private void desenhaCaixaTexto(Graphics2D d2, int largura, int altura) {
		int caixaX = 300;
		int caixaY =80;
		int caixaW = largura - caixaX - 50;
		int caixaH = altura - 160;

		d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
		d2.setColor(new Color(20, 20, 28));
		d2.fillRoundRect(caixaX, caixaY, caixaW, caixaH, 16, 16);
		d2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		d2.setColor(new Color(180, 30, 30));
		d2.drawRoundRect(caixaX, caixaY, caixaW, caixaH, 16, 16);
		d2.drawRoundRect(caixaX + 2, caixaY + 2, caixaW - 4, caixaH - 4, 14, 14);

		d2.setColor(Color.WHITE);
		d2.setFont(new Font("Monospaced", Font.BOLD, 18));
		String visivel = TEXTO.substring(0, Math.min(charsRevelados, TEXTO.length()));
		FontMetrics fm = d2.getFontMetrics();
		int largDisponivel = caixaW - 40;
		List<String> linhas = quebrarComWordWrap(visivel, fm, largDisponivel);
		int linhaY = caixaY + 32;
		for (String linha : linhas) {
			d2.drawString(linha, caixaX + 20, linhaY);
			linhaY += fm.getHeight() + 2;
		}

		// piscar cursor enquanto digita
		if (charsRevelados < TEXTO.length() && (System.currentTimeMillis() / 250) % 2 == 0) {
			String ultima = linhas.isEmpty() ? "" : linhas.get(linhas.size() - 1);
			int cx = caixaX + 20 + fm.stringWidth(ultima) + 2;
			int cy = linhaY - fm.getHeight() - 2 + 6;
			d2.fillRect(cx, cy, 9, fm.getAscent());
		}

		// rodape com instrucao
		d2.setColor(charsRevelados < TEXTO.length() ? new Color(140, 140, 140) : new Color(255, 220, 80));
		d2.setFont(new Font("Monospaced", Font.BOLD, 16));
		String rodape = charsRevelados < TEXTO.length()
				? "[ESPACO] pular texto"
				: "[ESPACO] iniciar jogo";
		FontMetrics fmR = d2.getFontMetrics();
		int rx = caixaX + caixaW - fmR.stringWidth(rodape) - 16;
		int ry = caixaY + caixaH - 14;
		d2.drawString(rodape, rx, ry);
	}

	private static List<String> quebrarLinhas(String s) {
		List<String> out = new ArrayList<>();
		int i = 0;
		while (i <= s.length()) {
			int j = s.indexOf('\n', i);
			if (j < 0) { out.add(s.substring(i)); break; }
			out.add(s.substring(i, j));
			i = j + 1;
		}
		return out;
	}

	private static List<String> quebrarComWordWrap(String texto, FontMetrics fm, int largMax) {
		List<String> resultado = new ArrayList<>();
		for (String paragrafo : quebrarLinhas(texto)) {
			if (paragrafo.isEmpty()) { resultado.add(""); continue; }
			StringBuilder atual = new StringBuilder();
			for (String palavra : paragrafo.split(" ")) {
				String tentativa = atual.length() == 0 ? palavra : atual + " " + palavra;
				if (fm.stringWidth(tentativa) <= largMax) {
					atual.setLength(0);
					atual.append(tentativa);
				} else {
					if (atual.length() > 0) resultado.add(atual.toString());
					atual.setLength(0);
					atual.append(palavra);
				}
			}
			resultado.add(atual.toString());
		}
		return resultado;
	}
}
