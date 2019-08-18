package elements.units;

import elements.Elemento;
import gamemenu.Window;
import managers.UnitManager;
import managers.WindowManager;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

public class Guardia extends Unidad{

    public Guardia(int xCoord, int yCoord){
        super("Guardia "+(UnitManager.getContUnidades(GUARDIA)+1), Unidad.classHP[GUARDIA], 75, 200,3,
                        WindowManager.UNIT_SPRITE_FILES[GUARDIA], xCoord, yCoord);
        clase = GUARDIA;
        UnitManager.setPoblacion(GUARDIA);

    }

    @Override
    public void action(int sel) {

    }
}
