package elements.units.enemies;

import elements.units.Unidad;
import managers.UnitManager;

public class GuardiaEnemy extends Enemy {

    public GuardiaEnemy(int xCoord, int yCoord){
        super("Guardia (Enemy) " + (UnitManager.getContEnemigos(GUARDIA)+1), Unidad.classHP[GUARDIA]+20, 60,
                205, ENEMY_SPRITE_FILES[GUARDIA], xCoord, yCoord);
        clase = GUARDIA;
    }

    @Override
    public void action(int sel) {

    }

}
