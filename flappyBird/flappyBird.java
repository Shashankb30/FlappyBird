import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class flappyBird extends JPanel implements ActionListener,KeyListener {
    int boardWidth=360;
    int boardHeight=640;

    //Images
     Image backGroundImg;
     Image birdImg;
     Image topPipeImage;
     Image bottomPipeImage;

     //bird
     int birdX=boardWidth/8;
     int birdY=boardHeight/2;
     int birdWidth=34;
     int birdHeight=24;

     class Bird{
        int x=birdX;
        int y=birdY;
        int width=birdWidth;
        int height=birdHeight;
        Image img;
     
        Bird(Image img){
            this.img=img;
        }
    }
    //pipes
    int pipeX= boardWidth;
    int pipeY=0;
    int pipeWidth= 64;
    int pipeHeight=512;

    class Pipe{
        int x =pipeX;
        int y=pipeY;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed= false;

        Pipe(Image img){
            this.img=img;
        }
    }

    //game logic
    Bird bird;
    int velocityX= -4;
    int velocityY=0;
    int gravity=1;
    
    ArrayList<Pipe> pipes;
    Random random= new Random();

    Timer gameLoop;
    Timer placePipesTimer;
    boolean gameOver=false;
    double score = 0;


    flappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load
        backGroundImg=new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg=new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImage=new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImage=new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
    
        //bird
        bird=new Bird(birdImg);
        pipes= new ArrayList<Pipe>();
        //place pipes image
        placePipesTimer= new Timer(1500,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();  

        //game timer
        gameLoop= new Timer(1000/60,this);
        gameLoop.start();
    }

    public void placePipes(){
        int randPipeY=(int)(pipeY - pipeHeight/4- Math.random()*(pipeHeight/2));
        int openingSpace= boardHeight/4;

        Pipe topPipe= new Pipe(topPipeImage);
        topPipe.y= randPipeY;       
        pipes.add(topPipe);
        
        Pipe bottomPipe= new Pipe(bottomPipeImage);
        bottomPipe.y=topPipe.y+ pipeHeight+openingSpace;
        pipes.add(bottomPipe);
    }
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //bg
        g.drawImage(backGroundImg,0,0,boardWidth,boardHeight,null);
    
        //bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);
         //pipe

        for(int i=0; i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        } 

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("GameOver: "+ String.valueOf((int)score),10,35);
        }else{
            g.drawString(String.valueOf((int)score),10,35); 
        }
    }

    public void move(){
        //bird
        velocityY+=gravity;
        bird.y+= velocityY;
        bird.y=Math.max(bird.y,0);

        //pipe
        for(int i=0;i<pipes.size();i++){
            Pipe pipe=pipes.get(i);
            pipe.x+= velocityX;

            if(collision(bird,pipe)){
                gameOver =true;
            }

            if(!pipe.passed&& bird.x> pipe.x+ pipe.width){
                pipe.passed =true;
                score+= 0.5;
            }

            if(bird.y> boardHeight){
                gameOver=true;
            }
        }
    }

    public boolean collision(Bird a,Pipe b){
        return a.x<b.x+ b.width&& //a's top left corn doesnt reach b's top right corn
               a.x+a.width>b.x && //a's top right corn doesnt reach b's top left corn
               a.y<b.y+b.height && //a's top left corn doesnt reach b's bott left corn
               a.y+a.height>b.y; //a's bott left corn doesnt reach b's top left corn
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       repaint();
       move();
       if(gameOver){
        placePipesTimer.stop();
        gameLoop.stop();
       }
    }

    @Override
    public void keyPressed(KeyEvent e) {
       if(e.getKeyCode()==KeyEvent.VK_SPACE){
        velocityY=-9;
        if(gameOver){
            //reset the game
            bird.y=birdY;
            velocityY=0;
            pipes.clear();
            score= 0;
            gameOver=false;
            gameLoop.start();
            placePipesTimer.start();
        }
       }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

   

    @Override
    public void keyReleased(KeyEvent e) {
       
    }
}
