package elements.units;

import managers.UnitManager;
import managers.WindowManager;

public class Lancero extends Unidad {

    public Lancero(int xCoord, int yCoord) {
        super("Lancero "+ (UnitManager.getContUnidades(LANCERO)+1), Unidad.classHP[LANCERO],
                60, 180,4, WindowManager.UNIT_SPRITE_FILES[LANCERO], xCoord, yCoord);

        clase = LANCERO;
        UnitManager.setPoblacion(LANCERO);
    }

    @Override
    public void action(int sel) {

    }
}
