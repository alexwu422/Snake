import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;


class SnakeModel extends Observable implements Runnable {
    boolean[][] matrix;
    LinkedList nodeArray = new LinkedList();
    Node food;
    int maxX;
    int maxY;
    int direction = 2;
    boolean running = false;
    boolean newGame = true;

    int timeInterval = 80;
//    int timeInterval = 200;
    double speedChangeRate = 1.2;
//    double speedChangeRate = 0.75;
    boolean paused = false;

    int score = 0;
    int countMove = 0;

    // UP and DOWN should be even
    // RIGHT and LEFT should be odd
    public static final int UP = 2;
    public static final int DOWN = 4;
    public static final int LEFT = 1;
    public static final int RIGHT = 3;

    public SnakeModel( int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;

        reset();
    }

    public void reset(){
        direction = SnakeModel.UP;
        timeInterval = 80;
        paused = false;
        score = 0;
        countMove = 0;


        matrix = new boolean[maxX][];
        for (int i = 0; i < maxX; ++i) {
            matrix[i] = new boolean[maxY];
            Arrays.fill(matrix[i], false);
        }


        int initArrayLength = maxX > 20 ? 10 : maxX / 2;
        nodeArray.clear();
        for (int i = 0; i < initArrayLength; ++i) {
            int x = maxX / 2 + i;
            int y = maxY / 2;

            nodeArray.addLast(new Node(x, y));
            matrix[x][y] = true;
        }


        food = createFood();
        matrix[food.x][food.y] = true;
    }

    public void changeDirection(int newDirection) {

        if (direction % 2 != newDirection % 2) {
            direction = newDirection;
        }
    }


    public boolean moveOn() {
        Node n = (Node) nodeArray.getFirst();
        int x = n.x;
        int y = n.y;


        switch (direction) {
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }


        if ((0 <= x && x < maxX) && (0 <= y && y < maxY)) {

            if (matrix[x][y]) {
                if (x == food.x && y == food.y) {
                    nodeArray.addFirst(food);


                    int scoreGet = (10000 - 200 * countMove) / timeInterval;
                    score += scoreGet > 0 ? scoreGet : 10;
                    countMove = 0;

                    food = createFood();
                    matrix[food.x][food.y] = true;
                    return true;
                } else
                    return false;

            } else {
                nodeArray.addFirst(new Node(x, y));
                matrix[x][y] = true;
                n = (Node) nodeArray.removeLast();
                matrix[n.x][n.y] = false;
                countMove++;
                return true;
            }
        }
        return false;
    }

    public void run() {
        running = true;
        while (running) {
            try {
                Thread.sleep(timeInterval);
            } catch (Exception e) {
                break;
            }

            if (!paused) {
                if (moveOn()) {
                    setChanged();
                    notifyObservers();
                } else {
                    int option=JOptionPane.showConfirmDialog(null, "<html>Score: " + score + "<br>" + "New Game?</html>", "", JOptionPane.YES_NO_OPTION);



                    if(option==JOptionPane.OK_OPTION){
                        this.reset();
                    }else{

                        this.newGame = false;
                        System.exit(0);
                        break;
                    }
                }
            }
        }
        running = false;
    }

    private Node createFood() {
        int x = 0;
        int y = 0;

        do {
            Random r = new Random();
            x = r.nextInt(maxX);
            y = r.nextInt(maxY);
        } while (matrix[x][y]);

        return new Node(x, y);
    }

    public void speedUp() {
        timeInterval /= speedChangeRate;
    }

    public void speedDown() {
        timeInterval *= speedChangeRate;
    }

    public void changePauseState() {
        paused = !paused;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < nodeArray.size(); ++i) {
            Node n = (Node) nodeArray.get(i);
            result += "[" + n.x + "," + n.y + "]";
        }
        return result;
    }
}

class Node {
    int x;
    int y;

    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}