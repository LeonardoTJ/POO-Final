package elements.buildings;

import elements.units.UnitAction;
import elements.Elemento;
import gamemenu.sidebars.ActionMenu;
import managers.UnitManager;

import java.awt.Image;

import static managers.WindowManager.campo;

public abstract class Edificio extends Elemento implements UnitAction {
    //  Constantes para identificar cada edificio
    public static final int CASA     = 1;
    public static final int C_URBANO = 2;
    public static final int CUARTEL  = 3;
    public static final int TORRE    = 4;
    public static final int MOLINO   = 5;

    protected int hp;
    protected int def;
    protected int clase;
    public static final int[] classHP = {0,
                                         500,   //  Casa
                                         750,   //  Centro Urbano
                                         850,   //  Cuartel
                                         700,   //  Torre
                                         450};  //  Molino
    protected String nombre;
    protected Image activeFrame;

    protected boolean selected;


    public Edificio(String nombre, int hp, int def, String spriteFile, int xCoord, int yCoord){
        super(xCoord, yCoord, spriteFile);
        this.nombre = nombre;
        this.hp = hp;
        this.def = def;

        selected = false;
        campo[worldY][worldX].setOcupado(false);
        System.out.println(nombre+" construido");
    }

    @Override
    public void setHP(int hp) {
        this.hp = hp;
        if(this.hp <= 0){
            UnitManager.destroy(this);
            System.out.println(nombre +" destruido!");
        }
    }

    @Override
    public int getHP() {
        return hp;
    }

    @Override
    public void setDEF(int def) {
        this.def = def;
    }

    @Override
    public int getDEF() {
        return def;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getNombre() {
        return nombre;
    }
    
    public int getClase() {
    	return this.clase;
    }
    public void setClase(int clase) {
    	this.clase = clase;
    }

    @Override
    public void selected() {
        System.out.println(nombre+" seleccionado.");
        ActionMenu.showMenu(this);
        selected = true;
    }

    @Override
    public void deselected() {
        System.out.println(nombre+" deselected.");
        ActionMenu.hideMenu();
        selected = false;
    } 
}
