package elements.units;

import managers.UnitManager;
import managers.WindowManager;

public class Caballero extends Unidad {

    public Caballero(int xCoord, int yCoord) {
        super("Caballero "+(UnitManager.getContUnidades(CABALLERO)+1), Unidad.classHP[CABALLERO], 80,
                230, 4, WindowManager.UNIT_SPRITE_FILES[CABALLERO], xCoord, yCoord);

        clase = CABALLERO;
        UnitManager.setPoblacion(CABALLERO);
    }

    @Override
    public void action(int sel) {

    }
}
