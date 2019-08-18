package elements.units;

import elements.MakerTask;
import managers.GameManager;
import managers.UnitManager;
import elements.buildings.Edificio;
import gamemenu.Window;
import managers.WindowManager;

import javax.swing.ImageIcon;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Timer;

public class Aldeano extends Unidad {
      //  Posicion del cursor donde dibujar la imagen del edificio a construir
    private int cursorX;
    private int cursorY;

    private Image buildSprite;      //  Imagen del edificio en construccion

    private int buildSelection;      //  Seleccion del arreglo BUILD_SPRITE_FILES

    private boolean placingBuild;   //  Colocando edificio

    private boolean collecting;     //  Recolectando recurso

    private int recurso;            //  El recurso que se está almacenando

    public Aldeano(int xCoord, int yCoord){
        super("Aldeano "+(UnitManager.getContUnidades(ALDEANO)+1), classHP[ALDEANO], 20, 100,2,
                        WindowManager.UNIT_SPRITE_FILES[ALDEANO], xCoord, yCoord);
        clase = ALDEANO;

        buildSelection = 0;
        placingBuild = false;
        collecting = false;
        recurso = -1;

        UnitManager.setPoblacion(ALDEANO);

        MakerTask task = new MakerTask(this);       //  Timer para implementar construccion y recoleccion de recursos
        Timer turn = new Timer();
        turn.scheduleAtFixedRate(task, 0, 1000);
    }

    public int getRecurso() {
        return recurso;
    }

    public boolean isPlacingBuild() {
        return placingBuild;
    }

    public int getBuildSelection() {
        return buildSelection;
    }

    public boolean isCollecting() {
        return collecting;
    }

    public void deselected(){
        placingBuild = false;
        collecting = false;
        super.deselected();
    }

    @Override
    public void action(int sel){   //  Recibir accion seleccionada del menu
        switch(sel){
            case 1:
                if(GameManager.validarRecurso(this, Edificio.CASA)){
                    buildSelection = Edificio.CASA;
                    startBuild();
                    System.out.println("Build casa ");
                }
                else
                    return;
                break;
            case 2:
                if(GameManager.validarRecurso(this, Edificio.C_URBANO)){
                    buildSelection = Edificio.C_URBANO;
                    startBuild();
                    System.out.println("Build casa ");
                }
                else
                    return;
                break;
            case 3:
                if(GameManager.validarRecurso(this, Edificio.CUARTEL)){
                    buildSelection = Edificio.CUARTEL;
                    startBuild();
                    System.out.println("Build cuartel ");
                }
                else{
                    System.out.println("No se puede construir "+WindowManager.BUILD_SPRITE_FILES[buildSelection]);
                    return;
                }
                break;
            case 4:
                if(GameManager.validarRecurso(this, Edificio.TORRE)){
                    buildSelection = Edificio.TORRE;
                    startBuild();
                    System.out.println("Build torre ");
                }
                else
                    return;
                break;
            case 5:
                if(GameManager.validarRecurso(this, Edificio.MOLINO)){
                    buildSelection = Edificio.MOLINO;
                    startBuild();
                    System.out.println("Build Molino");
                }
                else
                    return;
                break;
            case 6:
                recoger();
                break;
        }
    }

    public void moveToMouse(Point target){
        super.moveToMouse(target);
        collecting = false;
    }

    private void recoger(){      //  Recoger recurso de la casilla actual
        if(collecting){
            collecting = false;
            recurso = -1;
        }
        else{
            System.out.println("Buscando recursos...");
            recurso = WindowManager.campo[worldY][worldX].getResource();
            if(recurso > 0){
                collecting = true;

            }
            else{
                System.out.println("No hay recursos");
            }
        }
    }

    private void startBuild(){      //  Iniciar construccion
        ImageIcon img = new ImageIcon(getClass().getResource(WindowManager.BUILD_SPRITE_FILES[buildSelection]));
        buildSprite = img.getImage();
        placingBuild = true;
    }

    public void setBuilding(boolean building) {
        placingBuild = building;
        buildSelection = 0;
    }

    private void buildPointer(Graphics g){          //  Mostrar imagen del edificio a construir siguiendo el cursor
        Point p = new Point(getWorldCoord(WindowManager.getMousePos()));   //  colocar en coordenadas segun el tablero de juego

        //  Si el cursor esta en un area valida
        if( (p.getX() >= 1 && p.getX() < Window.GRID_SIZE_X) && (p.getY() >= 0 && p.getY() < Window.GRID_SIZE_Y)){
            cursorX = (int)WindowManager.campo[(int)p.getY()][(int)p.getX()].getX();
            cursorY = (int)WindowManager.campo[(int)p.getY()][(int)p.getX()].getY();
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite( AlphaComposite.SrcOver.derive(0.5f) );        //  Establecer la opacidad de la imagen
            g2d.drawImage(buildSprite, cursorX, cursorY, null);
        }
    }

    public Point getBuildCoords(){
        return new Point(cursorX, cursorY);
    }

    public void update(){
        super.update();
    }

    public void draw(Graphics g){
        g.drawImage(sprite, x, y, null);
        if(selected){
            g.drawImage(activeFrame, x, y, null);
        }
        if(placingBuild){       //  Si se esta colocando un edificio mostrar la imagen
            buildPointer(g);
        }
    }

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public void setDEF(int def) {
        this.def = def;
    }

    @Override
    public int getDEF() {
        return def;
    }

}
