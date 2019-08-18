package elements;

import elements.buildings.Edificio;
import elements.buildings.MakerBuilding;
import elements.units.Aldeano;
import elements.units.Unidad;
import gamemenu.Window;
import managers.GameManager;
import managers.UnitManager;
import managers.WindowManager;

import java.util.TimerTask;

public class MakerTask extends TimerTask{
    private boolean isAldeano;

    private int worldX;
    private int worldY;
    private Elemento unit;

    public MakerTask(Elemento e){
        super();
        unit = e;
        isAldeano = unit instanceof Unidad;
    }

    private void construir(){
        Terreno t = WindowManager.campo[worldY][worldX];    //  Almacenar la posicion del edificio a construir
        int progreso = t.getBuildProgress();
        int type = t.getBuildType();

        if(progreso < 100){                 //Imprimir progreso
            System.out.printf("Progreso: %d\r", progreso);

            switch(type){               //  Dependiendo del tipo de edificio, sumar al progreso una
                case Edificio.CASA:     //  cantidad determinada
                case Edificio.MOLINO:
                    progreso += 10;
                    break;
                case Edificio.C_URBANO:
                case Edificio.TORRE:
                    progreso +=8;
                    break;
                case Edificio.CUARTEL:
                    progreso += 7;
                    break;
            }

            t.setBuildProgress( progreso );
        }
        else{
            UnitManager.createBuilding( t.getBuildType(), worldX, worldY );    // Al completar, crear el objeto
            t.finishBuilding();
        }
    }

    @Override
    public void run() {
        if(Window.isPaused() || Window.isOver())
            return;

        worldX = unit.getWorldX();
        worldY = unit.getWorldY();

        if(isAldeano){
            if (WindowManager.campo[worldY][worldX].isBuilding()) {
                construir();
            } else if ( ((Aldeano)unit).isCollecting() )
                GameManager.almacenarRecurso(worldX, worldY);
            else if(UnitManager.isAtMolino(worldX,worldY)){
                GameManager.almacenarRecurso();
            }
        }
        else{
            if ( ((MakerBuilding)unit).isCreating() ) {       //  Comprobar si se esta creando una unidad actualmente
                int createProgress = ((MakerBuilding)unit).getCreateProgress(); //  De ser asi, aumentar el progreso en cada actualizacion

                if (createProgress < 100){
                    System.out.printf("Progreso: %d\r",createProgress);
                    ((MakerBuilding)unit).setCreateProgress(createProgress + 10);
                } else{                        //  Al finalizar, crear el objeto de la unidad y agregarlo a la lista
                    UnitManager.createUnit( ((MakerBuilding)unit).getUnitSel(), worldX, worldY);
                    ((MakerBuilding)unit).setCreating(false);
                    ((MakerBuilding)unit).setCreateProgress(0);
                }
            }
        }
    }
}
