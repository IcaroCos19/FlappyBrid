import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import jdk.jshell.execution.StreamingExecutionControl;

public class Jogo extends JPanel implements ActionListener, KeyListener{
    int largBorda = 360;
	int altBorda = 640;
	
	//Imagens
	
	Image backgroundImg;
	Image birdImg;
	Image canoCima;
	Image canoBaixo;

    //passaralho
    int birdX = largBorda/8;
    int birdY = altBorda/2;
    int birdlarg = 34;
    int birdalt = 24;

    class Bird{
        int x = birdX;
        int y = birdY;
        int largura = birdlarg;
        int altura = birdalt;
        Image img;

        Bird(Image img){
            this.img = img;
        }

    }

    //Canos
    int pipeX = largBorda;
    int pipeY = 0;
    int largPipe = 64;
    int altPipe = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int largura = largPipe;
        int altura = altPipe;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }
    //logica
    Bird passaro;
    int veloX = -4;
    int veloY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;
    double score = 0;


	
	
	
	public Jogo() {
		setPreferredSize(new Dimension(largBorda, altBorda));
		//setBackground(Color.blue);

        setFocusable(true);
        addKeyListener(this);
		
		backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
		birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
		canoCima = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
		canoBaixo = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //passarin
        passaro = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();
        //colocar os canos
        placePipesTimer = new Timer(1500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();
        //timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
	}

    public void placePipes(){
        int randomPipeY = (int) (pipeY - altPipe/4 - Math.random()*(altPipe/2));
        int openSpace = altBorda / 6;
        Pipe topPipe = new Pipe(canoCima);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bPipe = new Pipe(canoBaixo);
        bPipe.y = topPipe.y + altBorda + openSpace;
        pipes.add(bPipe);


    }
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
		
	}
	
	public void draw(Graphics g) {

		//fundo
		g.drawImage(backgroundImg, 0, 0, largBorda, altBorda, null );

        //passarinho
        g.drawImage(passaro.img, passaro.x, passaro.y, passaro.largura, passaro.altura, null);

        //canos
        for(int i=0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.largura, pipe.altura, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over" + String.valueOf((int)score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int)score),10,35);
        }

	}
    public void move(){
        //passaro
        veloY += gravity;
        passaro.y += veloY;
        passaro.y = Math.max(passaro.y,0);

        //canos
        for(int i=0; i<pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += veloX;

            if(!pipe.passed && passaro.x> pipe.x + pipe.largura){
                pipe.passed = true;
                score += 0.5;
            }

            if(collision(passaro, pipe)){
                gameOver= true;
            }
        }

        if(passaro.y > altBorda){
            gameOver = true;
        }


        
    }

    public boolean collision(Bird a, Pipe b){
        return a.x < b.x + b.largura&&
            a.x + a.largura > b.x &&
            a.y < b.y + b.altura&&
            a.y + a.altura > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
        
    }

   

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            veloY = -9;
            if(gameOver){
                passaro.y = birdY;
                veloY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();

            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e){}
    
    public void keyReleased(KeyEvent e) {}
}
