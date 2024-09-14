import javax.swing.JFrame;

public class App {

	public static void main(String[] args) {
		int largBorda = 360;
		int altBorda = 640;
		
		JFrame frame = new JFrame("Flappy Bird");
		//frame.setVisible(true);
		frame.setSize(largBorda, altBorda);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Jogo jogo = new Jogo();
		frame.add(jogo);
		frame.pack();
        jogo.requestFocus();
		frame.setVisible(true);

	}

}

