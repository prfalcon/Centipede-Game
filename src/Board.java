import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import javax.sound.sampled.*;
import java.io.*;

public class Board extends JPanel implements Runnable, Commons {

    private Dimension d;
    private Player player;
    private ArrayList<Centipede> centipede;
    private Spider spider;
    private ArrayList<Mushroom> mushrooms;
    private ArrayList<Shot> shots;

    public int mushChance;
    private int centSpeed = 4;
    private int score = 0;
    private boolean ingame = true;
    private String message = "GAME OVER";

    private Thread animator;

    public Board(int mushC){
        mushChance = mushC;
        TAdapter ta1 = new TAdapter();
        addMouseListener(ta1);
        addMouseMotionListener(ta1);
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.BLACK);

        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {
        player = new Player(PLAYER_SX, PLAYER_SY);
        spider = new Spider(100, 300);
        shots = new ArrayList<>();

        initCentipede();
        initMushrooms();

        if (animator == null || !ingame){
            animator = new Thread(this);
            animator.start();
        }
    }

    public void initCentipede() {
        centipede = new ArrayList<>();
        for (int i = (BOARD_WIDTH - UNIT); i > (BOARD_WIDTH - (UNIT * (CENT_LENGTH + 1))); i -= UNIT) {
            centipede.add(new Centipede(i, MUSH_TOP - UNIT, -centSpeed));
        }
    }

    public void initMushrooms() {
        mushrooms = new ArrayList<>();

        Random ran = new Random();
        int temp;
        int cur = 0;
        int cur_row = -1;
        int prev_row;
        for (int j = MUSH_TOP; j < MUSH_BOT; j += UNIT) {
            prev_row = cur_row;
            cur_row = cur;
            for (int i = UNIT; i < (BOARD_WIDTH - (UNIT * 2)); i += UNIT) {
                temp = ran.nextInt((int)(1 / (mushChance / 100.0)));
                if (temp == 0 && canPlace(i, j, prev_row)) {
                    mushrooms.add(new Mushroom(i, j));
                    cur += 1;
                }
            }
        }
    }

    public boolean canPlace(int x, int y, int prev_r) {
        if (prev_r == -1) {
            //System.out.printf("%d,%d,%d%n", x, y, prev_r);
            return true;
        }

        int i = prev_r;
        Mushroom mush;

        while (i < mushrooms.size() && mushrooms.get(i).getY() + UNIT == y) {
            mush = mushrooms.get(i);
            if (mush.getX() + UNIT == x || mush.getX() - UNIT == x) {
                return false;
            }
            i += 1;
        }
        //System.out.printf("%d,%d,%d%n", x, y, prev_r);
        return true;
    }


    public void drawPlayer(Graphics g) {
        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        if (player.isDying()) {
            player.die();
        }
    }

    public void drawMushrooms(Graphics g) {
        for (Mushroom mushroom: mushrooms) {
            if (mushroom.isVisible()) {
                g.drawImage(mushroom.getImage(), mushroom.getX(), mushroom.getY(), this);
            }
            if (mushroom.isDying()) {
                mushroom.die();
            }
        }
    }

    public void drawSpider(Graphics g) {
        if (spider.isVisible()) {
            g.drawImage(spider.getImage(), spider.getX(), spider.getY(), this);
        }
        if (spider.isDying()) {
            spider.die();
        }
    }

    public void drawcentipede(Graphics g) {
        for (Centipede centPart: centipede) {
            if (centPart.isVisible()) {
                g.drawImage(centPart.getImage(), centPart.getX(), centPart.getY(), this);
            }
            if (centPart.isDying()) {
                centPart.die();
            }
        }
    }

    public void drawShot(Graphics g) {
        for (Shot shot: shots){
            if (shot.isVisible()){
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

    public void drawScore(Graphics g) {
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString("Score: " + score + "           Lives: " + player.getLives(), 5, 20);

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {
            drawPlayer(g);
            drawcentipede(g);
            drawSpider(g);
            drawMushrooms(g);
            drawShot(g);
            drawScore(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {
        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(32, 0, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 100);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 100);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, 350, 410);
        g.drawString("Score: " + score, 350, 435);
    }


    public void gameReset() {
        initCentipede();

        player.decLives();
        player.reset();
        spider.reset();
        shots = new ArrayList<>();

        for (Mushroom mushroom: mushrooms) {
            if (mushroom.isVisible() && (mushroom.dmg != 0)) {
                score += 10;
            }
            mushroom.reset();
        }
    }

    public void nxtLevel() {
        initCentipede();
        if (centSpeed == 4) {
            centSpeed = 5;
        } else if (centSpeed == 5) {
            centSpeed = 10;
        } else {
            centSpeed = 20;
        }
        spider.reset();
        spider.incSpeed();
        player.reset();
        shots = new ArrayList<>();
        score += 600;
    }

    public boolean leftMushColl(Centipede centPart) {
        for (Mushroom mush : mushrooms) {
            if (centPart.getY() == mush.getY() && centPart.getX() - UNIT == mush.getX()) {
                return true;
            }
        }
        return false;
    }

    public boolean rightMushColl(Centipede centPart) {
        for (Mushroom mush : mushrooms) {
            if (centPart.getY() == mush.getY() && centPart.getX() + UNIT == mush.getX()) {
                return true;
            }
        }
        return false;
    }

    public boolean centColl(Centipede centPart) {
        if ( (centPart.getX() > player.getX() + PLAYER_WIDTH) || (player.getX() > centPart.getX() + UNIT)) {
            return false;
        }
        if ( (centPart.getY() > player.getY() + PLAYER_HEIGHT) || (player.getY() > centPart.getY() + UNIT)) {
            return false;
        }
        return true;
    }

    public boolean spiderColl() {
        if ((player.getX() > spider.getX() + SPIDER_WIDTH) || (spider.getX() > player.getX() + PLAYER_WIDTH)) {
            return false;
        }
        if ((spider.getY() > player.getY() + PLAYER_HEIGHT) || (player.getY() > spider.getY() + SPIDER_HEIGHT)) {
            return false;
        }
        return true;
    }

    public void animationCycle() {
        if (player.getLives() == 0) {
            ingame = false;
        }
        if (!player.isVisible()) {
            gameReset();
        }
        if (Centipede.allDead()) {
            nxtLevel();
        }


        player.act();

        for (Centipede centPart : centipede) {
            if (!centPart.isVisible()) {
                continue;
            }

            int c_x = centPart.getX();
            int c_y = centPart.getY();

            if (centPart.getDir() < 0) {
                if (c_x <= 0) {
                    if (c_y <= MUSH_BOT) {
                        centPart.act2();
                    }
                    centPart.revDir();
                } else if (leftMushColl(centPart)) {
                    centPart.act2();
                } else {
                    centPart.act();
                }
            } else {
                if (c_x >= BOARD_WIDTH - UNIT) {
                    if (c_y <= MUSH_BOT) {
                        centPart.act2();
                    }
                    centPart.revDir();
                } else if (rightMushColl(centPart)) {
                    centPart.act2();
                } else {
                    centPart.act();
                }
            }
        }

        spider.act();

        if (spider.isVisible() && player.isVisible()) {
            if (spiderColl()) {
                player.setDying(true);
            }
        }

        for (Centipede centPart : centipede) {
            if (centPart.isVisible() && player.isVisible()) {
                if (centColl(centPart)) {
                    player.setDying(true);
                }
            }
        }

        for (Shot shot : shots) {
            if (!shot.isVisible()) {
                continue;
            }

            int s_x = shot.getX();
            int s_y = shot.getY();

            for (Centipede centPart : centipede) {
                if (!centPart.isVisible()) {
                    continue;
                }
                int c_x = centPart.getX();
                int c_y = centPart.getY();

                if (s_x >= c_x && s_x <= (c_x + UNIT) && s_y >= c_y && s_y <= (c_y + UNIT)) {
                    shot.die();
                    centPart.incDmg();
                    if (centPart.isDying()) {
                        score += 5;
                    } else {
                        score += 2;
                    }
                }
            }

            if (!shot.isVisible()) {
                continue;
            }

            for (Mushroom mushroom : mushrooms) {
                if (!mushroom.isVisible()){
                    continue;
                }
                int m_x = mushroom.getX();
                int m_y = mushroom.getY();

                if (s_x >= m_x && s_x <= (m_x + UNIT) && s_y >= m_y && s_y <= (m_y + UNIT)) {
                    mushroom.incDmg();
                    shot.die();
                    if (mushroom.isDying()) {
                        score += 5;
                    } else {
                        score += 1;
                    }
                }
            }

            if (!shot.isVisible()) {
                continue;
            }

            if (spider.isVisible()) {
                int sp_x = spider.getX();
                int sp_y = spider.getY();
                if (s_x >= sp_x && s_x <= (sp_x + SPIDER_WIDTH) && s_y >= sp_y && s_y <= (sp_y + SPIDER_HEIGHT)) {
                    shot.die();
                    spider.incDmg();
                    if (!spider.isDying()) {
                        score += 100;
                    } else {
                        score += 600;
                    }
                }
            }

            if (shot.isVisible()) {
                shot.act();
                if (shot.getY() < 0) {
                    shot.die();
                }
            }

        }
    }

    public void shotSound() {

        File soundFile = new File("data\\laser.wav");

        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(sound);
            clip.start();
        } catch (Exception e) {
            System.out.println("Audio Exception.");
        }
    }


    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {
            repaint();
            animationCycle();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();

        }
        gameOver();
    }

    private class TAdapter extends MouseAdapter implements MouseMotionListener {
        @Override
        public void mousePressed(MouseEvent e) {
            if (ingame) {
                shots.add(new Shot (player.getX() + 15, player.getY()));
                shotSound();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            player.mouseMoved(e);
        }
    }
}
