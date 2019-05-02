import java.awt.EventQueue;
import javax.swing.JFrame;

public class CentipedeMain extends JFrame implements Commons {

    public CentipedeMain(int mushChance) {
        initUI(mushChance);
    }

    private void initUI(int mushChance) {
        add(new Board(mushChance));
        setTitle("Centipede");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String [] args) {

        EventQueue.invokeLater(() -> {
            int mushChance = Integer.valueOf(args[0]);
           CentipedeMain ex = new CentipedeMain(mushChance);
           ex.setVisible(true);
        });
    }
}

