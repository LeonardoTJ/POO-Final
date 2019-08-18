package elements.buildings;

import managers.UnitManager;
import managers.WindowManager;

public class CentroUrbano extends MakerBuilding {

    public CentroUrbano(int xCoord, int yCoord){
        super("Centro urbano "+ UnitManager.getContEdificios(C_URBANO)+1, classHP[C_URBANO], 140,
                        WindowManager.BUILD_SPRITE_FILES[C_URBANO], xCoord, yCoord);
        setClase(C_URBANO);
        UnitManager.setContEdificios(C_URBANO);
        UnitManager.setPoblacionLimit( UnitManager.getPoblacionLimit() + 5);
    }

    @Override
    public void update() {

    }
}
