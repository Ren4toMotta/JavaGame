package AULA.DESAFIO;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Moldura extends JFrame{
	 public Moldura() {
	        this.setTitle("Sobreviva Ao Apocalipse");
	        this.setIconImage(new ImageIcon("res/PLAYERS/NPC/Zombie-Tileset---_0176_Capa-177.png").getImage());
	        this.setAlwaysOnTop(true);
	        this.setLayout(new BorderLayout());
	        this.setResizable(false);
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        Painel centro = new Painel("Centro");
	        Painel sul = new Painel("Sul", centro);
	        
	        this.add(centro, BorderLayout.CENTER);
	        this.add(sul, BorderLayout.SOUTH);
	        this.pack();
	        this.setLocationRelativeTo(null);
	        this.setVisible(true);    
	 }
}
