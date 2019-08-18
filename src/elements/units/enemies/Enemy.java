package elements.units.enemies;

import elements.Elemento;
import elements.buildings.Edificio;
import elements.units.Arquero;
import elements.units.CombatAction;
import elements.units.Unidad;
import gamemenu.Window;
import managers.UnitManager;
import managers.WindowManager;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Enemy extends Unidad implements CombatAction {
    public static final String[] ENEMY_SPRITE_FILES = {"",
                                                       "/img/guardiaEnemy.png",
                                                       "/img/lanceroEnemy.png",
                                                       "/img/arqueroEnemy.png",
                                                       "/img/caballeroEnemy.png"};
    //  El estado por defecto de los enemigos es moverse aleatoriamente por el mapa

    //  Si el enemigo llega a un extremo del campo, activar 'flag' para cambiar la direccion de movimiento en x
    //  TRUE  == Movimiento hacia la derecha
    //  FALSE == Movimiento hacia la izquierda
    private boolean flag;
    private boolean moveWait;
    private Unidad target;

    public Enemy(String nombre, int hp, int atk, int def, String spriteFile, int xCoord, int yCoord) {
        super(nombre, hp, atk, def, 1, spriteFile, xCoord, yCoord);

        TimerTask combat = new AttackTask();       //  Timer para implementar el combate
        Timer turn = new Timer();
        turn.scheduleAtFixedRate(combat, 0, 1000);
    }

    public void setCombatState(boolean status){
        super.setCombatState(status);
        dx = 0;
        dy = 0;
    }

    public void setTarget(Elemento e){
        if(e != null){
            combatState = true;
            target = new Arquero(e.getWorldX(), e.getWorldY());
            target.setHP(((Edificio)e).getHP());
            target.setDEF(((Edificio)e).getDEF());
            target.setSpeed(1);
            target.setAttacker(nombre);
        }
        else{
            combatState = false;
            target = null;
        }
    }

    public boolean foundEnemy(){
        boolean enemy = false;

        if( worldX > 1 && WindowManager.campo[worldY][worldX-1].ocupado() ){
            target = UnitManager.isAlly(WindowManager.campo[worldY][worldX-1]);
            enemy = (target != null);
        }
        if( worldY < Window.GRID_SIZE_Y-1 && WindowManager.campo[worldY+1][worldX].ocupado() ){
            target = UnitManager.isAlly(WindowManager.campo[worldY+1][worldX]);
            enemy = (target != null);
        }
        if( worldY > 0 && WindowManager.campo[worldY-1][worldX].ocupado() ){
            target = UnitManager.isAlly(WindowManager.campo[worldY-1][worldX]);
            enemy = (target != null);
        }
        if( worldX < Window.GRID_SIZE_X-1 && WindowManager.campo[worldY][worldX+1].ocupado() ){
            target = UnitManager.isAlly(WindowManager.campo[worldY][worldX+1]);
            enemy = (target != null);
        }

        return enemy;
    }

    private int criticalDMG(){
        int crit = 1 + (int) (Math.random()*8);

        switch(crit){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                crit = 0;
                break;
            case 8:
            case 9:
                crit = 10;
                //crit += Math.abs(atk-target.getATK())/2;
        }
        return crit;
    }

    private boolean attackOrder(){
        int enemyTurn  = speed + (int) (Math.random()*20);
        int targetTurn = target.getSpeed() + (int) (Math.random()*20);

        return (enemyTurn > targetTurn);
    }

    private void selectMoveDir(){
        int dir;
        if(worldX == 1 || worldY == 0)     //  Comprobar si el enemigo ha llegado a un extremo del mapa
            flag = true;
        else if(worldX == Window.GRID_SIZE_X-1 || worldY == Window.GRID_SIZE_Y-1)
            flag = false;

        if(!moveWait) {
            moveWait = true;
            dir = (int) (Math.random()*9);  //  Seleccionar la direccion en la que se va a mover el enemigo
        }

        else {
            moveWait = false;
            dir = (int) (10+Math.random()*10);
        }

        switch(dir){
            case 0:
            case 1:
            case 2:
            case 3: dx = (flag ? speed : -1*speed);   //  Elegir cuatro casos para el movimiento 'x' de manera que sea el mas comun
                dy = 0;
                break;
            case 4:
            case 5:
            case 6: dx = 0;
                dy = (flag ? speed : -1*speed);   //  Los movimientos en 'y' tienen tres casos
                break;
            case 7:
            case 8:
            case 9: dx = 0;
                dy = (flag ? -1*speed : speed);
                break;
            default: dx = 0;
                dy = 0;
        }
    }

    public void update(){
        if(Window.isPaused())
            return;

        if( ((x % Elemento.CELL_SIZE == 0) && (y % Elemento.CELL_SIZE == 0)) ){     //  Enemigo encontrado
            worldX = x / Elemento.CELL_SIZE;
            worldY = y / Elemento.CELL_SIZE;
            if( target == null && foundEnemy() ){
                setCombatState(true);
                target.setCombatState(true);
                target.setAttacker(nombre);
            }
            else if(!combatState){
                selectMoveDir();

                targetX = worldX + dx;
                targetY = worldY + dy;
                if( (targetX < 1 || targetX >= Window.GRID_SIZE_X) || (targetY < 0 || targetY >= Window.GRID_SIZE_Y) ){
                    //  Validar limites del campo y casilla destino
                    dx = 0;
                    dy = 0;
                }
                else if(WindowManager.campo[targetY][targetX].ocupado()){
                    //  Validar que la casilla destino no se encuentre ocupada
                    dx = 0;
                    dy = 0;
                }
                else{
                    WindowManager.campo[targetY][targetX].setOcupado(true);
                    WindowManager.campo[worldY][worldX].setOcupado(false);
                }
            }
        }
        x += dx;
        y += dy;
    }

    public void draw(Graphics g){
        super.draw(g);
    }

    class AttackTask extends TimerTask{

        private void attack(){
            if(target == null)
                return;
            System.out.print( "\n"+nombre+" está atacando a "+target.getNombre()+"!" );
            int damage =  (int) Math.floor( atk * Math.pow(2, (double)((atk - target.getDEF())/50) )) + criticalDMG();
            System.out.print("  "+damage+" puntos de daño!");
            target.setHP( target.getHP()-damage );

            //  Comprobar salud del enemigo
            if(target.getHP() <= 0){
                combatState = false;
                target = null;
            }
            else
                System.out.print("  Salud de "+target.getNombre()+": "+target.getHP()+"\n");
        }

        private void defend(){
            if(target == null)
                return;

            if(target.getClase() > 0){      //  Si target es una unidad de combate, contraataque
                System.out.print( target.getNombre()+" está atacando a "+nombre+"!" );
                int damage =  (int) Math.floor( target.getATK() * Math.pow(2, (double)((target.getATK() - def)/50) )) + criticalDMG();
                System.out.print("  "+damage+" puntos de daño!");
                setHP( hp-damage );
                if(hp <= 0){
                    target.setCombatState(false);
                    target.setAttacker("");
                }
                else
                    System.out.print("  Salud de "+nombre+": "+hp+"\n");
            }
        }

        private void combat(){
            if(target == null){
                combatState = false;
                return;
            }

            //     Comprobar si el objetivo se salio de rango
            if( (getX() - target.getX() > Elemento.CELL_SIZE) || (getY()-target.getY() > Elemento.CELL_SIZE) ){
                combatState = false;
                target.setCombatState(false);
                target = null;
                return;
            }
            if(target.getHP() > 0){
                if(attackOrder()){
                    attack();
                    if( target != null && target.getAttacker().equals(nombre) )
                        defend();
                }
                else{
                    if( target.getAttacker().equals(nombre) )
                        defend();
                    if(hp > 0)
                        attack();
                }
            }
            else{
                combatState = false;
                target = null;
            }
        }

        @Override
        public void run(){
            if( (!Window.isPaused() || !Window.isOver()) && combatState){
                combat();
            }
        }
    }
}
