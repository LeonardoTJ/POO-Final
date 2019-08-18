package gamemenu.sidebars;

import elements.Elemento;
import elements.buildings.CentroUrbano;
import managers.UnitManager;
import elements.buildings.Cuartel;
import elements.units.Aldeano;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;

public class ActionMenu extends Sidebar {
    private static Image currentUnitMenu;
    private static ImageIcon[] unitMenus;
    private static boolean selected;

    public ActionMenu() {
        super();
        unitMenus = new ImageIcon[3];
        unitMenus[0] = new ImageIcon(getClass().getResource("/img/menualdeano.png"));
        unitMenus[1] = new ImageIcon(getClass().getResource("/img/menucuartel.png"));
        unitMenus[2] = new ImageIcon(getClass().getResource("/img/aldeano.png"));
        selected = false;
    }

    public static void select(int y){      //  Acciones correspondientes al menu de acciones
        if(y < Elemento.CELL_SIZE){
            UnitManager.action(1);
        }else if(y < Elemento.CELL_SIZE*2){
            UnitManager.action(2);
        }else if(y < Elemento.CELL_SIZE*3){
            UnitManager.action(3);
        }else if(y < Elemento.CELL_SIZE*4){
            UnitManager.action(4);
        }else if(y < Elemento.CELL_SIZE*5){
            UnitManager.action(5);
        }
    }

    public static void showMenu(Elemento u) {       //  Mostrar el menu del elemento seleccionado
        if(u instanceof Aldeano){
            currentUnitMenu = unitMenus[0].getImage();
            selected = true;
        }
        else if(u instanceof CentroUrbano){
            currentUnitMenu = unitMenus[2].getImage();
            selected = true;
        }
        else if(u instanceof Cuartel){
            currentUnitMenu = unitMenus[1].getImage();
            selected = true;
        }
    }

    public static void hideMenu(){
        selected = false;
    }
    public static void draw(Graphics g){
        g.drawImage(fondo, 0, 0, null);
        if(selected){
            g.drawImage(currentUnitMenu, 0, 0, null);
        }

    }
}
