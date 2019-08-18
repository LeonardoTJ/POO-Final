package elements.buildings;

import elements.Elemento;
import elements.MakerTask;
import elements.units.Unidad;
import managers.GameManager;
import managers.UnitManager;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

public abstract class MakerBuilding extends Edificio{
    private int createProgress;
    private boolean isCreating;

    private int unitSel;

    public MakerBuilding(String nombre, int hp, int def, String spriteFile, int xCoord, int yCoord){
        super(nombre, hp, def, spriteFile, xCoord,yCoord);
        createProgress = 0;
        isCreating = false;

        MakerTask task = new MakerTask(this);       //  Timer para implementar creacion de unidades
        Timer turn = new Timer();
        turn.scheduleAtFixedRate(task, 0, 1000);
    }

    public int getCreateProgress() {
        return createProgress;
    }

    public boolean isCreating() {
        return isCreating;
    }

    public void setCreateProgress(int createProgress) {
        this.createProgress = createProgress;
    }

    public void setCreating(boolean creating) {
        isCreating = creating;
    }

    public int getUnitSel() {
        return unitSel;
    }

    public void action(int sel){
        switch(this.getClase()){
            case CUARTEL:
                switch(sel){                            //  Establecer la unidad a crear
                    case 1:
                        if(GameManager.validarRecurso(this, Unidad.GUARDIA) &&
                                UnitManager.getPoblacion() <= UnitManager.getPoblacionLimit() )
                            unitSel = Unidad.GUARDIA;
                        else
                            return;
                        break;
                    case 2:
                        if(GameManager.validarRecurso(this, Unidad.LANCERO) &&
                                UnitManager.getPoblacion() <= UnitManager.getPoblacionLimit() )
                            unitSel = Unidad.LANCERO;
                        else
                            return;
                        break;
                    case 3:
                        if(GameManager.validarRecurso(this, Unidad.ARQUERO) &&
                                UnitManager.getPoblacion() <= UnitManager.getPoblacionLimit() )
                            unitSel = Unidad.ARQUERO;
                        else
                            return;
                        break;
                    case 4:
                        if(GameManager.validarRecurso(this, Unidad.CABALLERO) &&
                                UnitManager.getPoblacion() <= UnitManager.getPoblacionLimit() )
                            unitSel = Unidad.CABALLERO;
                        else
                            return;
                        break;
                }
                makeUnit();
                break;
            case C_URBANO:
                if(GameManager.validarRecurso(this, Unidad.ALDEANO) &&
                        UnitManager.getPoblacion() <= UnitManager.getPoblacionLimit() )
                    unitSel = Unidad.ALDEANO;
                else
                    return;
                makeUnit();
        }

    }
    private void makeUnit(){    //  Inicar el proceso de creacion de unidad
        GameManager.gastarRecurso(this, unitSel);
        isCreating = true;
    }

    public void draw(Graphics g){
        super.draw(g);
        if(isCreating) {
            int width = (createProgress * Elemento.CELL_SIZE)/100;       //Calcular barra de progreso de construccion
            int y = worldY*Elemento.CELL_SIZE + Elemento.CELL_SIZE/2;
            g.setColor(Color.DARK_GRAY);
            g.fillRect(worldX*Elemento.CELL_SIZE, y, Elemento.CELL_SIZE, 15);
            g.setColor(Color.CYAN);
            g.fillRect(worldX*Elemento.CELL_SIZE, y, width, 15);
        }
    }
}
