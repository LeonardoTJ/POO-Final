package elements;

import managers.UnitManager;
import managers.WindowManager;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Terreno extends Elemento {
        //  Comprobar si se esta construyendo algo en esta casilla
    private boolean building;
        //  Almacenar la imagen del terreno original si se llega a construir un edificio
    private String origSprite;
        //  Tipo de edificio que se esta construyendo
    private int buildType;
        //  Progreso de la construccion
    private int buildProgress;
    //  De haber un recurso en esta casilla
    private int resource;

    private boolean ocupado;

    public Terreno(int x, int y, String spriteFile, int resource, boolean blocksPath){
        super(x,y, spriteFile);
        origSprite = spriteFile;

        if(resource >= 1 && resource <= 3)      //  Validar si la casilla contiene recursos
            this.resource = resource;
        else
            this.resource = 0;

        ocupado = blocksPath;       //  Definir si se puede caminar por esta casilla
        buildProgress = 0;
    }

    public int getBuildProgress() {
        return buildProgress;
    }

    public void setBuildProgress(int buildProgress) {
        this.buildProgress = buildProgress;
    }

    @Override
    public void selected() {
        selected = true;
    }

    @Override
    public void deselected() {
        selected = false;

    }

    public boolean ocupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getBuildType() {
        return buildType;
    }

    public void setBuildType(int type) { buildType = type;
    }

    public boolean isBuilding(){
        return building;
    }

    public void startBuilding(int sel){
        setSprite(WindowManager.BUILD_SPRITE_FILES[0]);
        building = true;
        buildType = sel;
    }

    public void finishBuilding(){
        building = false;
        buildProgress = 0;
        setSprite(origSprite);
    }

    @Override
    public void update() {

    }

    public void draw(Graphics g){
        g.drawImage(sprite, x, y, null);

        if(building) {
            int width = (buildProgress * Elemento.CELL_SIZE)/100;       //Calcular barra de progreso de construccion
            int y = worldY*Elemento.CELL_SIZE +Elemento.CELL_SIZE;
            g.setColor(Color.GRAY);
        	g.fillRect(worldX*Elemento.CELL_SIZE, y, Elemento.CELL_SIZE, 15);
        	g.setColor(Color.WHITE);
        	g.fillRect(worldX*Elemento.CELL_SIZE, y, width, 15);
        }
    }
}
