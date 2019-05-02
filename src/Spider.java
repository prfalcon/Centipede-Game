import javax.swing.ImageIcon;
import java.util.Random;

public class Spider extends Sprite implements Commons {
    private final String spiderImg = "data\\spider.png";
    private final String spiderImg_dmg1 = "data\\spider_dmg1.png";
    private int target_x;
    private int target_y;
    public boolean target_reached = true;
    public int dmg;
    private int speed;
    private int steps;

    public Spider(int x, int y) {
        setX(x);
        setY(y);

        ImageIcon ii = new ImageIcon(spiderImg);
        setImage(ii.getImage());

        speed = 5;
        dmg = 0;
        steps = 0;
    }

    public void incSpeed() {
        speed += 3;
    }

    public void act(){
        Random ran = new Random();
        if (steps == 0) {
            steps = ran.nextInt(100);
            steps += 30;
            dx = ran.nextInt(5) - 2;
            dy = ran.nextInt(5) - 2;
            if (dx == 0 && dy == 0) {
                steps = 1;
            }
        }

        int cur_speed = ran.nextInt(speed /2);
        cur_speed += speed/2;

        setX(x + dx * cur_speed);
        setY(y + dy * cur_speed);
        steps -= 1;

        if (x < 0) {
            x = 0;
            steps = 0;
        } else if (x >= BOARD_WIDTH - SPIDER_WIDTH - UNIT) {
            x = BOARD_WIDTH - SPIDER_WIDTH - UNIT;
            steps = 0;
        }
        if (y < 0) {
            y = 0;
            steps = 0;
        } else if (y >= BOARD_HEIGHT - SPIDER_HEIGHT) {
            y = BOARD_HEIGHT - SPIDER_HEIGHT;
            steps = 0;
        }
    }

    public void incDmg() {
        dmg += 1;
        if (dmg == 1) {
            ImageIcon ii = new ImageIcon(spiderImg_dmg1);
            setImage(ii.getImage());
        } else if (dmg == 2) {
            this.setDying(true);
        }
    }

    public void reset() {
        dmg = 0;
        setX(100);
        setY(100);
        setVisible(true);
        setDying(false);

        target_reached = true;
        ImageIcon ii = new ImageIcon(spiderImg);
        setImage(ii.getImage());
    }
}
