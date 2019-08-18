package elements.buildings;

import managers.UnitManager;
import managers.WindowManager;

public class Cuartel extends MakerBuilding {

    public Cuartel(int xCoord, int yCoord){
        super("Cuartel", classHP[CUARTEL], 200,  WindowManager.BUILD_SPRITE_FILES[CUARTEL], xCoord, yCoord);
        setClase(CUARTEL);
        UnitManager.setContEdificios(CUARTEL);
    }

    @Override
    public void update() {

    }


}
