package AULA.DESAFIO;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Moldura extends JFrame{
	 public Moldura() {
	        this.setTitle("Cap12 - TABULEIRO");
	        this.setAlwaysOnTop(true);
	        this.setLayout(new BorderLayout());
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
