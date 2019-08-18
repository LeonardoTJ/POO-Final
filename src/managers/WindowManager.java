package managers;

import elements.Elemento;
import elements.Terreno;
import gamemenu.main.PauseMenu;
import gamemenu.sidebars.ActionMenu;
import gamemenu.sidebars.StatusBar;
import gamemenu.Window;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;


public class WindowManager implements Manager {
        //  Arreglo que contiene la distribucion del campo segun los indices del arreglo TERRAIN_SPRITE_FILES
                                           // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20
    private static final int[][] FIELD_GRID={{4, 0, 2, 6, 0, 0, 2, 0, 0, 0, 0, 0, 5, 0, 0, 0, 4, 0, 2, 0, 0},//0
                                             {0, 0, 0,12, 0, 4, 0, 0, 0, 3, 4, 0, 0, 4, 4, 4, 4, 0, 0, 0, 0}, //1
                                             {0, 1, 0, 6, 1, 0, 3, 4, 0, 0, 4, 4, 0, 0, 4, 4, 3, 1, 0, 0, 0}, //2
                                             {0, 7,13, 8, 0, 0, 0, 3, 0, 0, 0, 0, 4, 0, 3, 4, 0, 0, 0, 0, 0}, //3
                                             {0, 0, 1, 6, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0}, //4
                                             {0,11,10,12, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0}, //5
                                             {0, 9, 0, 6, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 5, 0, 0}, //6
                                             {0,11, 0, 6, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0}, //7
                                             {10,9, 0, 6, 0, 0, 4, 2, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, //8
                                             {0, 9, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0, 0, 1, 2, 4, 0, 0, 0, 0, 0}, //9
                                             {0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0}, //10
                                             {0, 0, 4, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 1, 0, 0}};//11
        //  Archivos de imagen para cada elemento
    private static final String[] TERRAIN_SPRITE_FILES ={"",
                                                        "/img/bosque.png",
                                                        "/img/piedra.png",
                                                        "/img/oro.png",
                                                        "/img/monte.png",
                                                        "/img/lago.png",
                                                        "/img/rio6.png",
                                                        "/img/rio7.png",
                                                        "/img/rio8.png",
                                                        "/img/camino9.png",
                                                        "/img/camino10.png",
                                                        "/img/camino11.png",
                                                        "/img/puente12.png",
                                                        "/img/puente13.png"};

    public static final String[] UNIT_SPRITE_FILES = {"/img/aldeano.png",
                                                      "/img/guardia.png",
                                                      "/img/lancero.png",
                                                      "/img/arquero.png",
                                                      "/img/caballero.png"};

    public static final String[] BUILD_SPRITE_FILES = {"/img/buildprocess.png",
                                                       "/img/casa.png",
                                                       "/img/c_urbano.png",
                                                       "/img/cuartel.png",
                                                       "/img/torre.png",
                                                       "/img/molino.png"};

    private static Image fondo;     //  El fondo del juego
    private static Point clicCampo;

    private static Point mousePos;      //  Posicion del cursor al ser desplazado
    private static Point mouseClic;     //  Posicion del cursor al dar clic
    public static Terreno[][] campo;    //  Arreglo donde se inicializan los elementos del campo de juego
    private static PauseMenu pm;

    public WindowManager(Window w){
        clicCampo = new Point();

        ImageIcon bg = new ImageIcon(getClass().getResource("/img/field.png"));
        fondo = bg.getImage();
        initCampo();
    }

    private void initCampo(){       //  Inicializar el campo de juego
        campo = new Terreno[Window.GRID_SIZE_Y][Window.GRID_SIZE_X];

        for (int y = 0; y < Window.GRID_SIZE_Y; y++) {
            for (int x = 0; x < Window.GRID_SIZE_X; x++) {
                if(FIELD_GRID[y][x] == 0)         //    Valor '0' indica que la celda esta vacia, no necesita sprite
                    campo[y][x] = new Terreno(x, y, "", FIELD_GRID[y][x],false);
                if( FIELD_GRID[y][x] >= 4 && FIELD_GRID[y][x] <=8 ){    //  4-8 indican que las celdas bloquean el paso
                    campo[y][x] = new Terreno(x, y, TERRAIN_SPRITE_FILES[FIELD_GRID[y][x]], FIELD_GRID[y][x],true);
                }
                else            //  Inicializar todas las demas con sus respectivos sprites
                    campo[y][x] = new Terreno(x, y, TERRAIN_SPRITE_FILES[FIELD_GRID[y][x]], FIELD_GRID[y][x],false);
            }
        }
    }

    public static void setPauseMenu(PauseMenu m){
        pm = m;
    }

    public static void updateComponents(){
        StatusBar.update();
        //pm.update();
    }

    public static void isClicked(int x, int y, boolean leftClick){        //  Metodo que almacena el clic del mouse y lo distribuye
        if(Window.isPaused() || Window.isOver())                          //  al manager indicado segun la posicion
            return;

        mouseClic = new Point(x, y);

        System.out.println("(PANEL) "+x+" "+y+"   (CAMPO) "+x/ Elemento.CELL_SIZE+" "+y/ Elemento.CELL_SIZE);

        if( UnitManager.waitingForInput() && !leftClick){      //  Si se esta esperando a mover una unidad o colocar un edificio
            UnitManager.setActionPoint(mouseClic);
        }
        else{                                   //  Si se selecciona una opcion del menu de acciones a la izquierda
            if(x >= 0 && x <= Elemento.CELL_SIZE){
                ActionMenu.select(y);
            }                                               //  Si se selecciona una celda del campo
            else if (x > Elemento.CELL_SIZE && x <= Window.getPWidth()-Elemento.CELL_SIZE && leftClick){
                UnitManager.selectUnit(x, y);
            }                                                       //  Si se selecciona boton de ayuda
            else if(x > Window.getPWidth()-Elemento.CELL_SIZE && y >= Window.getPHeight()-Elemento.CELL_SIZE && leftClick){
                StatusBar.showHelp();
            }
        }

        clicCampo.setLocation(x, y);
    }

    public static void setMousePos(int x, int y) {
        mousePos = new Point(x,y);
    }

    public static Point getMousePos(){
        return mousePos;
    }

    public static Point getMouseClic(){
        return mouseClic;
    }

    public static void keyAction(int keyCode){      //  Listener del teclado
        if ((keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END) ) {  //  'Q' o 'END' pausan el juego
            Window.pauseGame();
        }
        else if(keyCode == KeyEvent.VK_ESCAPE){     // 'Escape' deselecciona un elemento
            UnitManager.deselect();
            Window.pauseGame();
            pm.setVisible(Window.isPaused());
            //window.setFocusable(!Window.isPaused());
        }
        else if(keyCode == KeyEvent.VK_X){  //  Realizar accion de unidad seleccionada
            UnitManager.action(6);      //  Acciones 1-5 pertenecen al menu
        }
        else if(UnitManager.canMoveUnit()){     //  Teclas de movimiento
            if(keyCode == KeyEvent.VK_W){
                UnitManager.moveUnitKey(UP);
            }
            else if(keyCode == KeyEvent.VK_A){
                UnitManager.moveUnitKey(LEFT);
            }
            else if(keyCode == KeyEvent.VK_S){
                UnitManager.moveUnitKey(DOWN);
            }
            else if(keyCode == KeyEvent.VK_D){
                UnitManager.moveUnitKey(RIGHT);
            }
        }
    }

    private static void drawCampo(Graphics g){      //  Dibujar las celdas del campo
        for (int y = 0; y < Window.GRID_SIZE_Y; y++) {
            for (int x = 0; x < Window.GRID_SIZE_X; x++) {
                campo[y][x].draw(g);
            }
        }
    }

    public static void drawComponents(Graphics g){      //  Dibujar los componentes del juego en el orden correcto
        g.drawImage(fondo, Elemento.CELL_SIZE, 0, null);
        drawCampo(g);
        ActionMenu.draw(g);
        UnitManager.drawUnits(g);
        StatusBar.draw(g);
    }
}
