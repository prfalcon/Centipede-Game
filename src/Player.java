import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class Player extends Sprite implements Commons {

    private int lives = 3;
    private final String playerImg = "data\\player.png";

    public Player(int x, int y) {
        lives = 3;
        ImageIcon ii = new ImageIcon(playerImg);

        setImage(ii.getImage());
        setX(x);
        setY(y);
    }

    public void reset() {
        setVisible(true);
        setDying(false);
        setX(PLAYER_SX);
        setY(PLAYER_SY);
    }

    public void act() {
        if (dx < 15) {
            x += dx;
            dx = 0;
        } else {
            x += 15;
            dx -= 15;
        }

        if (dy < 15) {
            y += dy;
            dy = 0;
        } else {
            y += 15;
            dy -= 15;
        }

        if (x <= 0) {
            x = 0;
        } else if (x >= BOARD_WIDTH - PLAYER_WIDTH){
            x = BOARD_WIDTH - PLAYER_WIDTH;
        }

        if (y <= 0){
            y = 0;
        } else if (y >= BOARD_HEIGHT - PLAYER_HEIGHT){
            y = BOARD_HEIGHT - PLAYER_HEIGHT;
        }

    }

    public void mouseMoved(MouseEvent e) {
        dx = (e.getX() - 20) - x;
        dy = (e.getY() - 20) - y;
    }

    public int getLives(){
        return lives;
    }

    public void decLives(){
        lives -= 1;
    }

}
