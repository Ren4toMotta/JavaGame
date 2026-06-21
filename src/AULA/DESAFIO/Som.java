package AULA.DESAFIO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class Som {
	private static byte[] bytesTiro;
	private static byte[] bytesHit;
	private static byte[] bytesMorte;
	private static byte[] bytesMoeda;
	private static boolean tentouCarregar = false;

	private static synchronized void carregar() {
		if (tentouCarregar) return;
		tentouCarregar = true;
		bytesTiro  = ler("res/SOUNDS/laserShoot.wav");
		bytesHit   = ler("res/SOUNDS/hitHurt.wav");
		bytesMorte = ler("res/SOUNDS/died.wav");
		bytesMoeda = ler("res/SOUNDS/pickupCoins.wav");
	}

	private static byte[] ler(String caminho) {
		try {
			return Files.readAllBytes(new File(caminho).toPath());
		} catch (Exception e) {
			System.err.println("[Som] falha ao carregar " + caminho + ": " + e.getMessage());
			return null;
		}
	}

	public static void tocarTiro()  { carregar(); tocar(bytesTiro,  "som-tiro"); }
	public static void tocarHit()   { carregar(); tocar(bytesHit,   "som-hit"); }
	public static void tocarMorte() { carregar(); tocar(bytesMorte, "som-morte"); }
	public static void tocarMoeda() { carregar(); tocar(bytesMoeda, "som-moeda"); }

	private static void tocar(byte[] bytes, String nomeThread) {
		if (bytes == null) return;
		new Thread(() -> {
			try {
				AudioInputStream in = AudioSystem.getAudioInputStream(new ByteArrayInputStream(bytes));
				Clip clip = AudioSystem.getClip();
				clip.open(in);
				clip.addLineListener((LineEvent ev) -> {
					if (ev.getType() == LineEvent.Type.STOP) {
						clip.close();
					}
				});
				clip.start();
			} catch (Exception e) {
				System.err.println("[Som] falha ao tocar " + nomeThread + ": " + e.getMessage());
			}
		}, nomeThread).start();
	}
}
