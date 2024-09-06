import javax.swing.*;

public class app{
    public static void main(String[] args) throws Exception{
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame=new JFrame("FlappyBird");
        //frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        flappyBird flappybird= new flappyBird();
        frame.add(flappybird);
        frame.pack();
        frame.requestFocus();
        frame.setVisible(true);
    } 
}