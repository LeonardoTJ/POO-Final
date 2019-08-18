package elements;

import elements.units.UnitAction;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Elemento extends Rectangle implements ElementAction {
    public static final int CELL_SIZE = 48;

    protected int worldX;
    protected int worldY;

    protected Image sprite;
    protected Image activeFrame;
    protected Image ocupadoFrame;


    protected boolean selected;

    public Elemento(int x, int y, String spriteFile) {
        super(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        worldX = x;
        worldY = y;

        setLocation(getPanelCoord(x, y));
        setSprite(spriteFile);

        ImageIcon img = new ImageIcon(getClass().getResource("/img/selected.png"));
        activeFrame = img.getImage().getScaledInstance(Elemento.CELL_SIZE, Elemento.CELL_SIZE, Image.SCALE_SMOOTH);

        img = new ImageIcon(getClass().getResource("/img/ocupado.png"));
        ocupadoFrame = img.getImage().getScaledInstance(Elemento.CELL_SIZE, Elemento.CELL_SIZE, Image.SCALE_SMOOTH);
    }

    public Elemento() {

    }

    public static Point getWorldCoord(int x, int y){
        return new Point(x/CELL_SIZE, y/CELL_SIZE);
    }

    public static Point getWorldCoord(double x, double y){
        int xCoord = (int) x;
        int yCoord = (int) y;
        return new Point(xCoord/CELL_SIZE, yCoord/CELL_SIZE);
    }
    public static Point getWorldCoord(Point p){
        return new Point((int)p.getX()/CELL_SIZE, (int)p.getY()/CELL_SIZE);
    }

    public static Point getPanelCoord(int x, int y){

        return new Point(x*CELL_SIZE, y*CELL_SIZE);
    }
    public static Point getPanelCoord(double x, double y){
        int xCoord = (int) x;
        int yCoord = (int) y;
        return new Point(xCoord*CELL_SIZE, yCoord*CELL_SIZE);
    }
    public static Point getPanelCoord(Point p){
        return new Point((int)p.getX()*CELL_SIZE, (int)p.getY()*CELL_SIZE);
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public Point getWorldPos(){
        return new Point(worldX,worldY);
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    protected void setWorldCoord(int x, int y){
        worldX = (int) Elemento.getWorldCoord(x, y).getX();
        worldY = (int) Elemento.getWorldCoord(x, y).getY();
    }
    protected void setPanelCoord(int x, int y){
        this.setLocation(Elemento.getPanelCoord(x, y));
    }



    public void action(int sel){

    }

    public boolean contains(Point p){
        return ( (p.getX() >= getMinX() && p.getX() <= getMaxX())  && ( p.getY() >= getMinY() && p.getY() <= getMaxY()) );
    }

    public void setSprite(String spriteFile){
        ImageIcon img = new ImageIcon(getClass().getResource(spriteFile));
        sprite = img.getImage().getScaledInstance(Elemento.CELL_SIZE, Elemento.CELL_SIZE, Image.SCALE_SMOOTH);
    }

    public abstract void update();

    public void draw(Graphics g){
        if(selected){
            g.drawImage(activeFrame, x, y, null);
        }
        g.drawImage(sprite, x, y, null);
    }
}
