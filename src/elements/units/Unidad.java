package elements.units;

import gamemenu.sidebars.ActionMenu;
import elements.Elemento;
import gamemenu.Window;
import managers.UnitManager;
import managers.WindowManager;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.Timer;


public abstract class Unidad extends Elemento implements UnitAction{
        //  Constantes para identificar cada unidad
    public static final int ALDEANO  = 0;
    public static final int GUARDIA  = 1;
    public static final int LANCERO  = 2;
    public static final int ARQUERO  = 3;
    public static final int CABALLERO= 4;
        //  Caracteristicas de la unidad
    protected String nombre;
    protected int hp;

    protected int atk;
    protected int def;
    protected int speed;
    protected int clase;
    public static final int[] classHP = {250,   //  Aldeano
                                         300,   //  Guardia
                                         290,   //  Lancero
                                         270,   //  Arquero
                                         340};  //  Caballero

    protected boolean combatState;
    private Image attackFrame;
    private String attacker;

        //  Atributos de movimiento y posicion
    protected Point dest;
    protected boolean movingKey;
    protected boolean movingMouse;
    protected int dx = 0;
    protected int dy = 0;
    protected int targetX;
    protected int targetY;
    protected Timer timer;

    public Unidad(String nombre, int hp, int atk, int def, int speed, String spriteFile, int xCoord, int yCoord){
        super(xCoord, yCoord, spriteFile);
        this.nombre = nombre;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.speed = speed;

        ImageIcon img = new ImageIcon(getClass().getResource("/img/attacking.png"));
        attackFrame = img.getImage().getScaledInstance(Elemento.CELL_SIZE, Elemento.CELL_SIZE, Image.SCALE_SMOOTH);

        movingKey = false;
        movingMouse = false;
        dest = new Point(getLocation());
        selected = false;
        combatState = false;
        attacker = "";
        WindowManager.campo[worldY][worldX].setOcupado(true);
        System.out.println(nombre+" creado");
    }

    public void setHP(int hp){
        this.hp = hp;
        if(this.hp <= 0){
            combatState = false;
            UnitManager.kill(this);
            WindowManager.campo[worldY][worldX].setOcupado(false);
            System.out.println(nombre+" ha muerto!");
        }
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getNombre(){
        return this.nombre;
    }

    @Override
    public int getHP() {
        return hp;
    }

    public int getATK() {
        return atk;
    }

    public void setATK(int atk){
        this.atk = atk;
    }

    @Override
    public void setDEF(int def) {
        this.def=def;
    }

    @Override
    public int getDEF() {
        return def;
    }

    public int getClase() {
        return clase;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isMoving() {
        return movingKey || movingMouse;
    }

    public boolean isEnemy(){
        return nombre.contains("Enemy");
    }

    public void setCombatState(boolean status){
        combatState = status;
        if(!combatState)
            attacker = "";
    }

    public boolean getCombatState(){
        return combatState;
    }

    public String getAttacker() {
        return attacker;
    }

    public void setAttacker(String attacker) {
        this.attacker = attacker;
    }

    public void selected(){
        System.out.println(nombre+" seleccionado.");
        ActionMenu.showMenu(this);
        selected = true;
    }

    public void deselected(){
        System.out.println(nombre+" deselected.");
        ActionMenu.hideMenu();
        selected = false;
        movingKey = false;
        movingMouse = false;

        if( (x % Elemento.CELL_SIZE != 0) || (y % Elemento.CELL_SIZE != 0) ){   //  Reacomodar unidad si se estaba moviendo
            System.out.println("Offset");
            if( targetX > worldX ){
                x = (worldX+1) * Elemento.CELL_SIZE;
                worldX++;
            }
            else{
                x = worldX * Elemento.CELL_SIZE;
            }


            if( targetY > worldY ){
                y = (worldY+1) * Elemento.CELL_SIZE;
                worldY++;
            }
            else
                y = worldY * Elemento.CELL_SIZE;
        }
    }

    public abstract void action(int sel);

    public void moveToKey(int targetX, int targetY){
        if(worldX+targetX < 1 || worldX+targetX >= Window.GRID_SIZE_X ||
                worldY+targetY >= Window.GRID_SIZE_Y || worldY+targetY < 0){
            System.out.println("Limite");
            return;
        }

        System.out.println((worldX+targetX)+ " y "+(worldY+targetY)+
                ((WindowManager.campo[worldY+targetY][worldX+targetX].ocupado())? (" ocupado"): (" libre")));

        if( !WindowManager.campo[worldY+targetY][worldX+targetX].ocupado() ){
            //System.out.println("Current: "+worldX +" "+worldY +"---"+getPanelCoord(worldX,worldY));

            WindowManager.campo[worldY][worldX].setOcupado(false);
            movingKey = true;
            dest = new Point(getPanelCoord(worldX+targetX, worldY+targetY));
            //System.out.println("Delta: "+targetX+" "+targetY+"---"+getPanelCoord(targetX, targetY));

            System.out.println("Target: "+(worldX+targetX)+ " "+ (worldY+targetY)+"---"+dest);
            dx = targetX*speed;
            dy = targetY*speed;
        }
    }

    private boolean moveX(){
        if( targetX > worldX && !WindowManager.campo[worldY][worldX+1].ocupado()){
            //System.out.println("Moviendo "+nombre+" en x " +worldX+" "+worldY);
            x += dx;
            if(x % Elemento.CELL_SIZE == 0){
                this.worldX = x/Elemento.CELL_SIZE;
            }
            return false;
        }
        else if(targetX < worldX && !WindowManager.campo[worldY][worldX-1].ocupado()){
            //System.out.println("Moviendo "+nombre+" en x "+worldX+" "+worldY);
            x -= dy;
            if(x % Elemento.CELL_SIZE == 0){
                this.worldX = x/Elemento.CELL_SIZE;
            }
            return false;
        }
        else{
            this.worldX = x/Elemento.CELL_SIZE;
            return true;
        }
    }

    private boolean moveY(){
        if( targetY > worldY && !WindowManager.campo[worldY+1][worldX].ocupado()){
            //System.out.println("Moviendo "+nombre+" en y "+worldX+" "+worldY);
            y += dy;
            if(y % Elemento.CELL_SIZE == 0){
                this.worldY = y/Elemento.CELL_SIZE;
            }
            return false;
        }
        else if(targetY < worldY && !WindowManager.campo[worldY-1][worldX].ocupado()){
            //System.out.println("Moviendo "+nombre+" en y "+worldX+" "+worldY);
            y -= dy;
            if(y % Elemento.CELL_SIZE == 0){
                this.worldY = y/Elemento.CELL_SIZE;
            }
            return false;
        }
        else{
            this.worldY = y/Elemento.CELL_SIZE;
            return true;
        }
    }

    public void moveToMouse(Point target){
        //dest = new Point(target);
        targetX = (int) target.getX()/Elemento.CELL_SIZE;
        targetY = (int) target.getY()/Elemento.CELL_SIZE;

        if(!WindowManager.campo[targetY][targetX].ocupado() && targetX >= 1 && targetX < Window.GRID_SIZE_X){
            WindowManager.campo[worldY][worldX].setOcupado(false);
            movingMouse = true;
            dx = speed;
            dy = speed;
        }
    }

    public void update() {
        if(movingKey) {
            x +=dx;
            y +=dy;
            if( (dx > 0 && x >= dest.getX() || dx < 0 && x <= dest.getX() ) ||
                    ( (dy > 0 && y >= dest.getY()) || (dy < 0 && y <= dest.getY()) ) ){

                x = (int)dest.getX();
                y = (int)dest.getY();
                System.out.println("Arrived " + dest.getX() + " " + dest.getY());

                worldX += dx/speed;     // +- 1
                worldY += dy/speed;
                dx = 0;
                dy = 0;
                WindowManager.campo[worldY][worldX].setOcupado(true);
                movingKey = false;
            }
        }
        else if(movingMouse){   //  Recibe orden de moverse desde el mouse
            if(moveX()){        //  Mover en eje X
                if(moveY()){    //  Mover en eje Y
                    movingMouse = false;    //  Al llegar al destino detenerse
                    WindowManager.campo[worldY][worldX].setOcupado(true);
                }
            }
        }
    }

    public void draw(Graphics g){
        super.draw(g);

        if(combatState){
            g.drawImage(attackFrame, x, y, null);
            UnitManager.showBar(this, g);
        }
    }
}