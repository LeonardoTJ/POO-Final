package elements.units.enemies;

import elements.units.Unidad;
import managers.UnitManager;

public class LanceroEnemy extends Enemy {

    public LanceroEnemy(int xCoord, int yCoord){
        super("Lancero (Enemy) " + (UnitManager.getContEnemigos(LANCERO)+1), Unidad.classHP[LANCERO]+20, 40,
                187, ENEMY_SPRITE_FILES[LANCERO], xCoord, yCoord);
        clase = LANCERO;
    }

    @Override
    public void action(int sel) {

    }
}
