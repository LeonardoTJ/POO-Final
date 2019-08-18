package elements.buildings;

import managers.UnitManager;
import managers.WindowManager;

public class Casa extends Edificio {

    public Casa(int xCoord, int yCoord) {
        super("Casa", classHP[CASA], 100, WindowManager.BUILD_SPRITE_FILES[CASA], xCoord, yCoord);
        setClase(CASA);
        UnitManager.setContEdificios(CASA);
        UnitManager.setPoblacionLimit( UnitManager.getPoblacionLimit() + 5);
    }

    public void selected(){
        super.selected();
    }

    public void deselected(){
        super.deselected();
    }

    @Override
    public void update() {

    }
}
