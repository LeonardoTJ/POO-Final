package gamemenu;

import gamemenu.main.PauseMenu;
import gamemenu.sidebars.ActionMenu;
import gamemenu.sidebars.StatusBar;
import managers.GameManager;
import managers.UnitManager;
import managers.WindowManager;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class Window extends JPanel implements Runnable{
        //  Variables de actualizacion de pantalla
    public static int oldFrameCount;
    public static int oldTickCount;
    public static int tickCount;

    private volatile static boolean isPaused;
    private volatile static boolean running;

        //  Tamaño de la ventana
    private static final int PWIDTH = 1056;
    private static final int PHEIGHT = 576;
    private static volatile boolean gameOver = false;
        //  Hilo para llevar a cabo la animacion
    private Thread animator;

        //  Contexto grafico
    private Graphics dbg; 
    private Image dbImage = null;

        //  Tamaño del 'tablero' de juego
    public static final int GRID_SIZE_X = 21;
    public static final int GRID_SIZE_Y = 12;

        //  Bandera para redibujar la pantalla
    public static boolean repaintFlag;

    private static String message;

        //  MouseListener
    public static EscuchaMouse mouseL;
    
    public Window(JFrame frame){
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        setIgnoreRepaint(true);
//        setFocusable(true);
//        requestFocus();
        initComponents(frame);
    }

    private void initComponents(JFrame frame){      //  Inicializar listeners y managers
        running = true;

        mouseL = new EscuchaMouse();
        addMouseMotionListener(mouseL);
        addMouseListener(mouseL);

        new WindowManager(this);
        new UnitManager();
        new GameManager();

        PauseMenu m = new PauseMenu();
        m.setIconImage(frame.getIconImage());
        WindowManager.setPauseMenu(m);

        add(new ActionMenu());
        add(new StatusBar());


        setLayout( new GridLayout(GRID_SIZE_Y, GRID_SIZE_X) );    //  Distribucion de tabla
    }

    public void addNotify(){    // Esperar a que se agregue el JPanel al JFrame antes de iniciar
         super.addNotify();
          startGame();
    }

    public void run(){         //  Metodo de actualizacion de pantalla
        final double GAME_HERTZ = 40;
        final double TBU = 1000000000 / GAME_HERTZ; // Time Before Update

        final int MUBR = 3; // Must Update before render

        double lastUpdateTime = System.nanoTime();
        double lastRenderTime;

        final double TARGET_FPS = 25;
        final double TTBR = 1000000000 / TARGET_FPS; // Total time before render

        int frameCount = 0;
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);
        oldFrameCount = 0;

        tickCount = 0;
        oldTickCount = 0;

        while(running) {

            double now = System.nanoTime();
            int updateCount = 0;
            while (((now - lastUpdateTime) > TBU) && (updateCount < MUBR)) {
                //update(now);

                lastUpdateTime += TBU;
                updateCount++;
                tickCount++;
                // (^^^^) We use this variable for the soul purpose of displaying it
            }

            if (now - lastUpdateTime > TBU) {
                lastUpdateTime = now - TBU;
            }
            gameUpdate();
            gameRender();
            paintScreen();

            lastRenderTime = now;
            frameCount++;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime) {
                if (frameCount != oldFrameCount) {
                    System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                    oldFrameCount = frameCount;
                }

                if (tickCount != oldTickCount) {
                    System.out.println("NEW SECOND (T) " + thisSecond + " " + tickCount);
                    oldTickCount = tickCount;
                }
                tickCount = 0;
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TTBR && now - lastUpdateTime < TBU) {
                Thread.yield();

                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    System.out.println("ERROR: yielding thread");
                }

                now = System.nanoTime();
            }

        }
    }

    private void gameUpdate(){      //  Actualizar las unidades del juego mediante el manager
        if(!isPaused && !gameOver){
            UnitManager.updateUnits();
            WindowManager.updateComponents();
        }
    }

    private void gameOverMessage(Graphics g){
        // center the game-over message in the panel
              int x = PWIDTH/2; 
              int y = PHEIGHT/2;
              g.setColor(Color.red);
              g.setFont(new Font("Poor Richard", Font.BOLD, 36 ));
              g.drawString(message, x, y);
    }

    private void gameRender(){      //  Creacion del contexto grafico
        if(dbImage == null){
            dbImage = createImage(PWIDTH, PHEIGHT);
            if(dbImage == null){
                System.out.println("dbImage is null");
                return;
            }
            else
                dbg = dbImage.getGraphics();
        }
        WindowManager.drawComponents(dbg);
        repaint();

        if(gameOver)
            gameOverMessage(dbg);
    }
    private void paintScreen(){
        Graphics g;
        try {
            g = this.getGraphics();

            if ((g != null) && (dbImage != null))
                g.drawImage(dbImage, 0, 0, null);

            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch (Exception e)
        { System.out.println("Graphics context error: " + e);  }
    }

    public void repaint(){      //  Metodo sobreescrito para dibujar en pantalla dependiendo de la bandera
        if(repaintFlag){
            paintScreen();
            repaintFlag = false;
        }
    }

    public static boolean isPaused() {
        return isPaused;
    }

    public static boolean isOver(){
        return gameOver;
    }

    private void startGame(){        //  Inicializar el juego
          if (animator == null || !running) {
            animator = new Thread((Runnable) this);
                animator.start();
          }
    }

    public static synchronized void pauseGame() {   //  Pausar
        isPaused = !isPaused;
    }

    public static void stopGame(boolean playerWin) {
        gameOver = true;
        if(!playerWin)
            message = GameManager.gameOverMessage[0];
        else
            message = GameManager.gameOverMessage[1];
  }

    public static int getPWidth() {
        return PWIDTH;
    }

    public static int getPHeight() {
        return PHEIGHT;
    }

    class EscuchaMouse extends MouseInputAdapter {  //  Clase interna para implementar eventos de mouse

        public void mouseClicked(MouseEvent e) {
            WindowManager.isClicked(e.getX(), e.getY(), SwingUtilities.isLeftMouseButton(e));
        }
        public void mouseMoved(MouseEvent e){
            WindowManager.setMousePos(e.getX(), e.getY());
        }
    }
}
