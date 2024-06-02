import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class Driver extends JPanel implements Runnable, KeyListener, MouseListener
{

    int FPS = 60;
    Thread thread;
    int screenWidth = 800;
    int screenHeight = 600;
    BufferedImage bg;
    int speed = 1;
    int x = 0;
    int width;
    boolean autoMove = true;

    public Driver()
    {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setVisible(true);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run()
    {
        initialize();
        while (true)
        {
            update();
            repaint();
            try
            {
                Thread.sleep(1000 / FPS);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void initialize()
    {
        try
        {
            bg = ImageIO.read(new File("0_1.jpeg"));
            width = bg.getWidth();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void update()
    {
        x -= speed;
        if (x < -width)
        {
            x = 0;
        }
        else if (x > width)
        {
            x = 0;
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(bg, x, 0, null);
        g.drawImage(bg, x + width, 0, null);
        g.drawImage(bg, x - width, 0, null);
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_S) {
            autoMove = !autoMove;
        } else if (keyCode == KeyEvent.VK_A) {
            speed = 5;
        } else if (keyCode == KeyEvent.VK_D) {
            speed = -5;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        speed = autoMove ? 1 : 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cat Cafe Tycoon");
        Driver gamePanel = new Driver();
        frame.add(gamePanel);
        frame.addKeyListener(gamePanel);
        frame.addMouseListener(gamePanel);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        System.out.println("Harry 오빠");
    }
}
