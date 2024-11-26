package freezemonsters;

import java.awt.*;

import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;

import javax.swing.ImageIcon;


import freezemonsters.Commons;
import spriteframework.AbstractBoard;
import spriteframework.sprite.BadSprite;
import spriteframework.sprite.Player;

import spaceinvaders.sprite.*;

public class FreezeMonstersBoard extends AbstractBoard{

    //define sprites
    //private List<BadSprite> aliens;
    private Ray ray;
    Color customColor = new Color(51,212,106);
    private int ultimaKey;

    // define global control vars
    private int directionX = -1;

    private int directionY = -1;
    private int frozen = 0;
    private String explImg = "imagesSpaceInvaders/explosion.png";

    public void doDrawing(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setColor(customColor);
        g.fillRect(0, 0, d.width, d.height);


        if (inGame) {
            drawBadSprites(g);
            drawPlayers(g);
            drawOtherSprites(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    protected void createBadSprites() {  // create sprites
        for (int i = 0; i < 8; i++) {
            Monster monster = new Monster(Commons.MONSTER_INIT_X,Commons.MONSTER_INIT_Y);
                badSprites.add(monster);
        }
    }

    protected Woody createPlayer(){
        return new Woody();
    }

    protected void createOtherSprites() {
        ray = new Ray();
        ultimaKey = KeyEvent.VK_UP;
    }

    private void drawRay(Graphics g) {

        if (ray.isVisible()) {

            g.drawImage(ray.getImage(), ray.getX(), ray.getY(), this);
        }
    }

    // Override
    protected void drawOtherSprites(Graphics g) {
        drawRay(g);
        g.setColor(Color.green);
    }

    protected void processOtherSprites(Player player, KeyEvent e) {
        int x = player.getX();
        int y = player.getY();

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {

            if (inGame) {

                if (!ray.isVisible()) {
                    ultimaKey = ((Woody)player).getUltimoMov();
                    ray = new Ray(x, y);
                }
            }
        }
    }

//    private void gameOver(Graphics g) {
//
//        g.setColor(Color.black);
//        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
//
//        g.setColor(new Color(0, 32, 48));
//        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
//        g.setColor(Color.white);
//        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
//
//        Font small = new Font("Helvetica", Font.BOLD, 14);
//        FontMetrics fontMetrics = this.getFontMetrics(small);
//
//        g.setColor(Color.white);
//        g.setFont(small);
//        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
//                Commons.BOARD_WIDTH / 2);
//    }

    protected void update() {

        if (frozen == Commons.NUMBER_OF_MONSTERS_TO_FREEZE) {
            inGame = false;
            timer.stop();
            message = "Game won!";
        }

        // player
        for (Player player: players)
            player.act();

        // shot
        if (ray.isVisible()) {

            int rayX = ray.getX();
            int rayY = ray.getY();

            for (BadSprite monster : badSprites) {

                int monsterX = monster.getX();
                int monsterY = monster.getY();

                if (monster.isVisible() && ray.isVisible() && !((Monster)monster).isCongelado()) {
                    if (rayX >= (monsterX)
                            && rayX <= (monsterX + Commons.MONSTER_WIDTH)
                            && rayY >= (monsterY)
                            && rayY <= (monsterY + Commons.MONSTER_HEIGHT)) {

                        monster.die();
                        frozen++;
                        ray.die();
                    }
                }
            }
            if(ultimaKey == KeyEvent.VK_LEFT){
                rayX -= 4;
            }
            if(ultimaKey == KeyEvent.VK_RIGHT){
                rayX += 4;
            }
            if(ultimaKey == KeyEvent.VK_UP){
                rayY -= 4;
            }
            if(ultimaKey == KeyEvent.VK_DOWN){
                rayY += 4;
            }
            if(rayY < 0 || rayX < 0 || rayY > Commons.BOARD_HEIGHT || rayX > Commons.BOARD_WIDTH - Commons.BORDER_RIGHT){
                ray.die();
            }
            else {
                ray.setX(rayX);
                ray.setY(rayY);
            }
        }

        // aliens

        for (BadSprite monster : badSprites) {
            if (!((Monster) monster).isCongelado()) {
                    Random rand = new Random();
                    if(((Monster) monster).getTimer() == 40) {
                        monster.setDx(rand.nextInt(-1, 2));
                        monster.setDy(rand.nextInt(-1, 2));
                        ((Monster) monster).setTimer(0);
                    }
                    ((Monster) monster).act();
                    ((Monster) monster).setTimer(((Monster) monster).getTimer()+1);
            }
        }


        // bombs

        updateOtherSprites();
    }


    protected void updateOtherSprites() {
        Random generator = new Random();

        for (BadSprite monster : badSprites) {
            //int ray1 = generator.nextInt(15);
            Slime slime = ((Monster) monster).getSlime();


            if (!((Monster) monster).isCongelado() && slime.isDestroyed()) {

                int directionX = generator.nextInt(-4,5);
                int directionY = generator.nextInt(-4,5);

                while(directionY == 0 && directionX == 0){
                    directionX = generator.nextInt(-4,5);
                    directionY = generator.nextInt(-4,5);
                }

                slime.setDx(directionX);
                slime.setDy(directionY);

                slime.setDestroyed(false);
                slime.setX(monster.getX());
                slime.setY(monster.getY());
            }

            if (players.get(0).isVisible() && !slime.isDestroyed()) {

                if (slime.getX() >= (players.getFirst().getX())
                        && slime.getX() <= (players.getFirst().getX() + Commons.WOODY_WIDTH)
                        && slime.getY() >= (players.getFirst().getY())
                        && slime.getY() <= (players.getFirst().getY() + Commons.WOODY_HEIGHT)) {

                    ImageIcon ii = new ImageIcon(explImg);
                    players.get(0).setImage(ii.getImage());
                    players.get(0).setDying(true);
                    slime.setDestroyed(true);
                }
            }

            if (!slime.isDestroyed()) {
                slime.setX(slime.getX() + slime.getDx());
                slime.setY(slime.getY() + slime.getDy());

                if (slime.getX() >= Commons.BOARD_WIDTH || slime.getX() <= 0 || slime.getY() <= 0) {
                    slime.setDestroyed(true);
                }
                if(slime.getX() == ray.getX() && slime.getY() == ray.getY()){
                    slime.setDestroyed(true);
                    ray.die();
                }
            }
        }
    }
}
