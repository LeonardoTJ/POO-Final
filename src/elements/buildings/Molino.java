package elements.buildings;

import managers.UnitManager;
import managers.WindowManager;

public class Molino extends Edificio {
    public Molino(int xCoord, int yCoord) {
        super("Molino", classHP[MOLINO], 85, WindowManager.BUILD_SPRITE_FILES[MOLINO], xCoord, yCoord);
        setClase(MOLINO);
        UnitManager.setContEdificios(MOLINO);
    }

    @Override
    public void update() {

    }
}
