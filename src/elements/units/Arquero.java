package elements.units;

import managers.UnitManager;
import managers.WindowManager;

public class Arquero extends Unidad {

    public Arquero(int xCoord, int yCoord) {
        super("Arquero "+ (UnitManager.getContUnidades(ARQUERO)+1), Unidad.classHP[ARQUERO], 70,
                230, 4, WindowManager.UNIT_SPRITE_FILES[ARQUERO], xCoord, yCoord);

        clase = ARQUERO;
        UnitManager.setPoblacion(ARQUERO);
    }

    @Override
    public void action(int sel) {

    }
}
