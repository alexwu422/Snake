import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;


public class SnakeView implements Observer {
    SnakeControl control = null;
    SnakeModel model = null;

    JFrame mainFrame;
    Canvas paintCanvas;
    JLabel labelScore;

    public static final int canvasWidth = 1282; //canvas size
    public static final int canvasHeight = 735;

    public static final int nodeWidth = 11; //15
    public static final int nodeHeight = 11;

    public SnakeView(SnakeModel model, SnakeControl control) {
        this.model = model;
        this.control = control;

        mainFrame = new JFrame("SNAKE");

        Container cp = mainFrame.getContentPane();

        labelScore = new JLabel("Score:");
        labelScore.setFont(new Font("Serif", Font.PLAIN, 80));
        cp.add(labelScore, BorderLayout.NORTH);

        paintCanvas = new Canvas();
        paintCanvas.setSize(canvasWidth, canvasHeight);
        paintCanvas.addKeyListener(control);
        cp.add(paintCanvas, BorderLayout.CENTER);




        JPanel panelButtom = new JPanel();
//        panelButtom.setLayout(new BorderLayout());
        JLabel labelHelp;
        labelHelp = new JLabel("[Directions]: ←, ↑, ↓, → ", JLabel.CENTER);
        panelButtom.add(labelHelp, BorderLayout.NORTH);
        labelHelp = new JLabel("[Speed]: Q, W ", JLabel.CENTER);
        panelButtom.add(labelHelp, BorderLayout.CENTER);
        labelHelp = new JLabel("[Pause]: Space Bar", JLabel.CENTER);

        panelButtom.add(labelHelp, BorderLayout.SOUTH);

        panelButtom.setFont(new Font("Serif", Font.PLAIN, 60));

        cp.add(panelButtom, BorderLayout.SOUTH);

        mainFrame.addKeyListener(control);
        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    void repaint() {
        Graphics g = paintCanvas.getGraphics();

        //draw background
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, canvasWidth+20, canvasHeight+20);

        // draw the snake
        g.setColor(new Color(150, 150, 150));

        LinkedList na = model.nodeArray;
        Iterator it = na.iterator();
        while (it.hasNext()) {
            Node n = (Node) it.next();
            drawNode(g, n);
        }

        // draw the food
        g.setColor(new Color(235, 82 ,44));
        Node n = model.food;
        drawNode(g, n);

        updateScore();
    }

    private void drawNode(Graphics g, Node n) {
        g.fillRect(n.x * nodeWidth,n.y * nodeHeight, nodeWidth, nodeHeight );
    }

    public void updateScore() {
        String s = "Score:                                             " + model.score;
        labelScore.setText(s);
    }

    public void update(Observable o, Object arg) {
        repaint();
    }
}