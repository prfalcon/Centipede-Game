import javax.swing.ImageIcon;

public class Centipede extends Sprite implements Commons {
    private int lives;
    private int direction;
    public int dmg;
    private static int remaining = 12;

    private final String centImg = "data\\cent.png";
    private final String centImg_dmg1 = "data\\cent_dmg1.png";

    public Centipede(int x, int y, int dir) {
        setX(x);
        setY(y);
        direction = dir;
        ImageIcon ii = new ImageIcon(centImg);
        setImage(ii.getImage());
        dmg = 0;
        remaining = 12;
    }

    public void act() {
        setX(x + direction);
    }

    public void act2() {
        setY(y + UNIT);
        if (x < 0){
            setX(0);
        } else if (x > BOARD_WIDTH - UNIT){
            setX(BOARD_WIDTH - UNIT);
        }
    }

    public void incDmg() {
        dmg += 1;

        if (dmg == 1) {
            ImageIcon ii = new ImageIcon(centImg_dmg1);
            setImage(ii.getImage());
        } else if (dmg == 2) {
            setDying(true);
            remaining -= 1;
        }
    }

    public static boolean allDead(){
        if (remaining == 0){
            return true;
        }
        return false;
    }


    public void revDir() {
        direction = -direction;
    }

    public int getDir() {
        return direction;
    }
}
