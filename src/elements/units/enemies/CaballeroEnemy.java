package elements.units.enemies;

import elements.units.Unidad;
import managers.UnitManager;

public class CaballeroEnemy extends Enemy {

    public CaballeroEnemy(int xCoord, int yCoord){
        super("Caballero (Enemy) " + (UnitManager.getContEnemigos(CABALLERO)+1), Unidad.classHP[CABALLERO]+20,
                70,240, ENEMY_SPRITE_FILES[CABALLERO], xCoord, yCoord);
        clase = CABALLERO;
    }

    @Override
    public void action(int sel) {

    }
}
