package elements.buildings;

import elements.units.CombatAction;
import elements.units.Unidad;
import elements.units.enemies.Enemy;
import gamemenu.Window;
import managers.UnitManager;
import managers.WindowManager;

public class Torre extends Edificio implements CombatAction {
    private Unidad target;

    public Torre(int xCoord, int yCoord) {
        super("Torre", classHP[TORRE], 185, WindowManager.BUILD_SPRITE_FILES[TORRE], xCoord, yCoord);
        setClase(TORRE);
        UnitManager.setContEdificios(TORRE);
    }

    @Override
    public void update(){
        if( target == null && foundEnemy() ){
            target.setCombatState(true);
            //target.setAttacker(nombre);
            ((Enemy)target).setTarget(this);
        }
        else if(target != null && (target.getHP() <= (75*Unidad.classHP[target.getClase()])/100) ){
            System.out.println( target.getNombre() + Unidad.classHP[target.getClase()] + (75*Unidad.classHP[target.getClase()])/100 );
            //target.setAttacker("");
            ((Enemy)target).setTarget(null);
            target = null;
        }
    }

    @Override
    public boolean foundEnemy() {
        boolean enemy = false;

        if( worldX > 1 && WindowManager.campo[worldY][worldX-1].ocupado() ){
            target = UnitManager.isEnemy(WindowManager.campo[worldY][worldX-1]);
            enemy = (target != null);
        }
        if( worldY < Window.GRID_SIZE_Y-1 && WindowManager.campo[worldY+1][worldX].ocupado() ){
            target = UnitManager.isEnemy(WindowManager.campo[worldY+1][worldX]);
            enemy = (target != null);
        }
        if( worldY > 0 && WindowManager.campo[worldY-1][worldX].ocupado() ){
            target = UnitManager.isEnemy(WindowManager.campo[worldY-1][worldX]);
            enemy = (target != null);
        }
        if( worldX < Window.GRID_SIZE_X-1 && WindowManager.campo[worldY][worldX+1].ocupado() ){
            target = UnitManager.isEnemy(WindowManager.campo[worldY][worldX+1]);
            enemy = (target != null);
        }

        return enemy;
    }
}
