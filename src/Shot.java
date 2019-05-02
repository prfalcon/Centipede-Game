import javax.swing.ImageIcon;

public class Shot extends Sprite implements Commons{
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;

    private final String shotImg = "data\\shot.png";

    public Shot() {
    }

    public Shot(int x, int y) {
        ImageIcon ii = new ImageIcon(shotImg);
        setImage(ii.getImage());

        setX(x + H_SPACE);
        setY(y + V_SPACE);
    }

    public void act() {
        setY(y - SHOT_SPEED);
    }
}
