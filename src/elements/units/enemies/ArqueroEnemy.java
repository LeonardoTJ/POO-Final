package elements.units.enemies;

import elements.units.Unidad;
import managers.UnitManager;
import managers.WindowManager;

public class ArqueroEnemy extends Enemy {

    public ArqueroEnemy(int xCoord, int yCoord){
        super("Arquero (Enemy) " + (UnitManager.getContEnemigos(ARQUERO)+1), Unidad.classHP[ARQUERO]+20, 60,
                225, ENEMY_SPRITE_FILES[ARQUERO], xCoord, yCoord);
        clase = ARQUERO;
    }

    @Override
    public void action(int sel) {

    }
}
