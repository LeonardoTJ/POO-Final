package elements.units.enemies;

import elements.Elemento;
import elements.units.Guardia;
import elements.units.Unidad;
import gamemenu.Window;
import managers.UnitManager;
import managers.WindowManager;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

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
