import javax.swing.ImageIcon;

public class Mushroom extends Sprite {
    public int dmg;

    private final String mushImg = "data\\mush.png";
    private final String mushImg_dmg1 = "data\\mush_dmg1.png";
    private final String mushImg_dmg2 = "data\\mush_dmg2.png";

    public Mushroom(int x, int y) {
        dmg = 0;

        setX(x);
        setY(y);

        ImageIcon ii = new ImageIcon(mushImg);
        setImage(ii.getImage());
    }

    public void incDmg() {
        dmg += 1;

        if (dmg == 1) {
            ImageIcon ii = new ImageIcon(mushImg_dmg1);
            setImage(ii.getImage());
        } else if (dmg == 2) {
            ImageIcon ii = new ImageIcon(mushImg_dmg2);
            setImage(ii.getImage());
        } else if (dmg == 3) {
            setDying(true);
        }
    }

    public void reset() {
        dmg = 0;
        ImageIcon ii = new ImageIcon(mushImg);
        setImage(ii.getImage());
        setVisible(true);
        setDying(false);
    }
}
