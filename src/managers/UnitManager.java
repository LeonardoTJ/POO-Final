package managers;

import elements.Elemento;
import elements.Terreno;
import elements.buildings.Casa;
import elements.buildings.CentroUrbano;
import elements.buildings.Cuartel;
import elements.buildings.Edificio;
import elements.buildings.Molino;
import elements.buildings.Torre;
import elements.units.Aldeano;
import elements.units.Arquero;
import elements.units.Caballero;
import elements.units.Guardia;
import elements.units.Lancero;
import elements.units.Unidad;
import elements.units.UnitAction;
import elements.units.enemies.ArqueroEnemy;
import elements.units.enemies.CaballeroEnemy;
import elements.units.enemies.GuardiaEnemy;
import elements.units.enemies.LanceroEnemy;
import gamemenu.Window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class UnitManager implements Manager {
        //  Arreglo de unidades y edificios (de una clase que permita modificar la lista mientras se realizan iteraciones)
    private static CopyOnWriteArrayList<Elemento> elementos;
        //  Arreglo de unidades enemigas
    private static CopyOnWriteArrayList<Unidad> enemigos;
        //  El elemento seleccionado
    private static int indexActive;
        //  Cuando se esta colocando un edificio, se establece en 'true' para que no se realicen otras acciones con el mouse
    private static boolean waiting;

    private static Point actionPoint;

    //Arreglos para poblacion
    private static int poblacion;

    private static int poblacionLimit;
    private static int[] contUnidades;

    private static int[] contEdificios;
    private static int[] contEnemigos;
    private static int enemyKillCount;



    public UnitManager(){
        indexActive = -1;
        waiting = false;

        contUnidades = new int[5];
        contEdificios = new int[6];
        contEnemigos = new int[5];

        initUnidades();
    }

    private void initUnidades(){            //  Inicializar la lista de elementos y agregar algunos objetos
        elementos = new CopyOnWriteArrayList<Elemento>();

        Casa canton = new Casa(2, 7);
        elementos.add(canton);
        contEdificios[Edificio.CASA]++;
        poblacionLimit += 5;

        Molino m = new Molino(6, 6);
        elementos.add(m);

        CentroUrbano cu = new CentroUrbano(6,7);
        elementos.add(cu);

        Aldeano pancho = new Aldeano(2, 6);
        elementos.add(pancho);

        Guardia g = new Guardia(13, 5);
        elementos.add(g);

        enemigos = new CopyOnWriteArrayList<Unidad>();

        TimerTask enemyTask = new CrearEnemigo();       //  Timer para crear enemigos cada 30 segundos
        Timer enemyWait = new Timer(true);
        enemyWait.scheduleAtFixedRate(enemyTask, 30000, 40000);
    }

    public static int getActiveElement(){
        if(indexActive >= 0)
            if(elementos.get(indexActive) instanceof Unidad)
                return ((Unidad)elementos.get(indexActive)).getClase();
            else
                return ((Edificio)elementos.get(indexActive)).getClase();
        else
            return -1;
    }

    public static int getActiveBuilding(){
        if(indexActive >= 0 && elementos.get(indexActive) instanceof Edificio)
            return ((Edificio)elementos.get(indexActive)).getClase();
        else
            return -1;
    }

    public static int getPoblacion() {
        return poblacion;
    }

    public static void setPoblacion(int type){
        if(poblacion <= poblacionLimit){
            UnitManager.contUnidades[type]++;
            poblacion++;
        }
    }

    public static int getPoblacionLimit() {
        return poblacionLimit;
    }

    public static void setPoblacionLimit(int poblacionLimit) {
        UnitManager.poblacionLimit = poblacionLimit;
    }

    public static int getContUnidades(int clase) {
        return contUnidades[clase];
    }

    public static int getContEdificios(int clase) {
        return contEdificios[clase];
    }

    public static void setContEdificios(int type) {
        UnitManager.contEdificios[type]++;
    }

    public static int getContEnemigos(int clase) {
        return contEnemigos[clase];
    }

    public static void createBuilding(int type, int x, int y){      //  Metodo para agregar un nuevo edificio
//        if(WindowManager.campo[y][x].ocupado())
//            return;

        switch(type){
            case 0:
                break;
            case Edificio.CASA:
                elementos.add(0, new Casa(x, y));   //  Agregar edificios al principio del arreglo
                System.out.println("Casa construida");
                poblacionLimit += 2;
                break;
            case Edificio.C_URBANO:
                elementos.add(0, new CentroUrbano(x, y));
                System.out.println("Centro Urbano construido");
                poblacionLimit += 5;
                break;
            case Edificio.CUARTEL:
                elementos.add(0, new Cuartel(x, y));
                System.out.println("Cuartel construido");
                break;
            case Edificio.TORRE:
                elementos.add(0, new Torre(x, y));
                System.out.println("Torre construido");
                break;
            case Edificio.MOLINO:
                elementos.add(0, new Molino(x, y));
                System.out.println("Molino construido");
                break;
        }
        deselect();
        waiting = false;
    }

    public static void createUnit(int type, int x, int y){      //  Metodo para agregar una nueva unidad
        if(WindowManager.campo[y][x].ocupado())
            return;

        switch(type){
            case Unidad.ALDEANO:
                elementos.add(new Aldeano(x,y));
                break;
            case Unidad.GUARDIA:
                elementos.add(new Guardia(x,y));
                break;
            case Unidad.LANCERO:
                elementos.add(new Lancero(x,y));
                break;
            case Unidad.ARQUERO:
                elementos.add(new Arquero(x,y));
                break;
            case Unidad.CABALLERO:
                elementos.add(new Caballero(x,y));
                break;
        }
        deselect();
        waiting = false;
    }

    public static boolean isAtMolino(int xPos, int yPos){
        for(Elemento e : elementos){
            if(e instanceof Molino && ((e.getWorldX() == xPos) && (e.getWorldY() == yPos)) )
                return true;
        }
        return false;
    }

    public static void showBar(Elemento e, Graphics g){
               //  Mostrar barra de salud de unidad activa
            int x = (int)(e.getX());
            int y = (int)(e.getY());
            int hp;
            if(e instanceof Unidad)
            	hp = ( ((Unidad)e).getHP()*100 )/Unidad.classHP[((Unidad)e).getClase()];   //  Obtener porcentaje de salud
            else
            	hp = ( ((Edificio)e).getHP()*100 )/Edificio.classHP[((Edificio)e).getClase()];
            
            double width = 0;

            if(hp == 100)
                width = Elemento.CELL_SIZE*2;
            else if(hp >=75)
                width = Elemento.CELL_SIZE*1.5;
            else if(hp >=50)
                width = Elemento.CELL_SIZE;
            else if(hp >=25)
                width = Elemento.CELL_SIZE*0.5;
            else if(hp > 0)
                width = Elemento.CELL_SIZE*0.25;

            if( e instanceof Unidad && ((Unidad) e).getNombre().contains("Enemy") ){   //  Posicionar la barra de salud
                x += Elemento.CELL_SIZE;
                y += Elemento.CELL_SIZE;
            }
            else{
                x -= 25;
                y-= 15;
            }

            g.setColor(Color.RED);
            g.fillRect(x, y, Elemento.CELL_SIZE*2, 15);
            g.setColor(Color.green);
            g.fillRect(x, y, (int) width, 15);
            g.setColor(Color.black);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
            if(e instanceof Unidad)
                g.drawString(((Unidad) e).getNombre(), x+20, y+10);
            else
                g.drawString(((Edificio) e).getNombre(), x+20, y+10);
        
    }

    public static void updateUnits(){       //  Iterar la lista actualizando cada elemento
        for(Elemento el : elementos){
            el.update();
        }
        for(Elemento en : enemigos){
            en.update();
        }
    }

    public static void drawUnits(Graphics g){   //  Iterar la lista dibujando cada elemento en pantalla
        /*for(int i=0; i<elementos.size(); i++){
            if(i == indexActive)
                continue;
            elementos.get(i).draw(g);
        }*/
        for(Elemento el : elementos){
            el.draw(g);
        }
        for(Unidad unit : enemigos){
            unit.draw(g);
        }
        if(indexActive >= 0)
            showBar(elementos.get(indexActive), g);
    }

    public static boolean canMoveUnit(){        //  Comprobar si hay alguna unidad en movimiento, solo se puede mover una a la vez
        return (indexActive >= 0 && (elementos.get(indexActive) instanceof Unidad) &&
                 !((Unidad)elementos.get(indexActive)).isMoving() );
    }

    public static Unidad isEnemy(Terreno cell){
        Point cellPos = new Point(cell.getWorldPos());
        for(Unidad enemy : enemigos){
            if(enemy.getWorldPos().equals(cellPos))
                return enemy;
        }
        return null;
    }

    public static Unidad isAlly(Terreno cell){
        Point cellPos = new Point(cell.getWorldPos());
        for(Elemento ally : elementos){
            if(ally.getWorldPos().equals(cellPos) && (ally instanceof Unidad) )
                return (Unidad) ally;
        }
        return null;
    }

    private static void removeAlly(Unidad u){
        elementos.remove(u);

        if(u instanceof Guardia)
            contUnidades[Unidad.ALDEANO]--;
        else if(u instanceof Aldeano)
            contUnidades[Unidad.GUARDIA]--;

        poblacion--;
        if(poblacion == 0)
            GameManager.gameOver(false);
    }

    private static void removeEnemy(Unidad u){
        enemigos.remove(u);

        if(u instanceof GuardiaEnemy){
            contEnemigos[Unidad.GUARDIA]--;
        }
    }

    public static void kill(Unidad u){
        if(u.getNombre().contains("Enemy")){
            removeEnemy(u);
            if(++enemyKillCount == GameManager.WIN_CONDITION){
                GameManager.gameOver(true);
            }
        }
        else{
            removeAlly(u);
        }
        deselect();
    }

    public static void destroy(Edificio e){
        elementos.remove(e);
    }

    public static void moveUnitKey(int dir){       //  Dar la orden a la unidad activa de que se mueva en la direccion indicada por el teclado
        Unidad unit = (Unidad) elementos.get(indexActive);
        if(dir == UP)
            unit.moveToKey(0,-1);
        else if(dir == LEFT)
            unit.moveToKey(-1,0);
        else if(dir == RIGHT)
            unit.moveToKey(1,0);
        else if(dir == DOWN)
            unit.moveToKey(0,1);
    }

    public static void selectUnit(int x, int y){    //  Seleccionar un elemento con el mouse
        deselect();
        Point p = new Point(x, y);

        //   Si dos elementos se encuentran en la misma posicion, queremos dar prioridad a las unidades
        //    Las unidades se encuentran al ultimo de la lista, asi que iteramos desde el final

        for(int i = elementos.size()-1; i >= 0; i--){
            if(elementos.get(i).contains(p)){
                elementos.get(i).selected();
                indexActive = i;
                Window.repaintFlag = true;
                if(elementos.get(i) instanceof Unidad)
                    waiting = true;
                break;
            }
        }
    }

    public static void action(int sel){
        if(indexActive >= 0){
            ((UnitAction)elementos.get(indexActive)).action(sel);
            if(elementos.get(indexActive) instanceof Aldeano && sel < 6){
                waiting = true;
            }
        }
    }

    public static boolean waitingForInput(){    //  Comprobar si se esta esperando a colocar un edificio
        return waiting;
    }

    public static void setActionPoint(Point p){     //  Metodo de construccion de edificios
        actionPoint = new Point(p);

        if(indexActive < 0 || !waiting )
            return;

        if( (elementos.get(indexActive) instanceof Aldeano) ){
            if( !( (Aldeano)elementos.get(indexActive) ).isPlacingBuild() ){
                ( (Unidad)elementos.get(indexActive) ).moveToMouse(actionPoint);
                waiting = false;
            }
            else{
                actionPoint = Elemento.getWorldCoord(actionPoint);
                if(actionPoint.getX() >= 1 && actionPoint.getX() < Window.GRID_SIZE_X){
                    Terreno target = (WindowManager.campo[ (int)actionPoint.getY() ][ (int)actionPoint.getX() ]);
                    int b = ( (Aldeano)elementos.get(indexActive) ).getBuildSelection();

                    if( !target.ocupado() && target.getResource() == 0 ){
                        GameManager.gastarRecurso(elementos.get(indexActive), b);
                        (WindowManager.campo[ (int)actionPoint.getY() ][ (int)actionPoint.getX() ]).startBuilding(b);
                        ( (Aldeano)elementos.get(indexActive) ).setBuilding(false);
                        waiting = false;
                    }
                    else
                        System.out.println("Ocupado");  //  Comprobar si la casilla esta ocupada
                }
                else                                    //  Evitar colocar el edificio fuera del campo
                    System.out.println("No puedes construir en "+actionPoint.getX() + "  "+actionPoint.getY());
            }
        }
        else{
            ( (Unidad)elementos.get(indexActive) ).moveToMouse(actionPoint);
            waiting = false;
        }
    }

    public static Point getActionPoint(){
        return actionPoint;
    }

    public static int getKillCount(){
        return enemyKillCount;
    }

    public static void deselect(){      //  Deseleccionar el elemento activo
        if(indexActive >= 0){
            if(indexActive >= elementos.size()){    //  Comprobar si la unidad activa fue destruida
                indexActive = -1;
                return;
            }
            elementos.get(indexActive).deselected();
            indexActive = -1;
            Window.repaintFlag = true;
            waiting = false;
        }
    }

    class CrearEnemigo extends TimerTask {

        private void createEnemy(int y){      //  Metodo para agregar un nuevo enemigo al campo
            if(WindowManager.campo[y][20].ocupado()){
                System.out.println("Enemigo en lugar ocupado");
                return;
            }
            int sel = 1+ (int)(Math.random()*5);

            switch(sel){
                case Unidad.GUARDIA:
                    enemigos.add(new GuardiaEnemy(20,y));
                    contEnemigos[Unidad.GUARDIA]++;
                    break;
                case Unidad.LANCERO:
                    enemigos.add(new LanceroEnemy(20,y));
                    contEnemigos[Unidad.LANCERO]++;
                    break;
                case Unidad.ARQUERO:
                    enemigos.add(new ArqueroEnemy(20,y));
                    contEnemigos[Unidad.ARQUERO]++;
                    break;
                case Unidad.CABALLERO:
                    enemigos.add(new CaballeroEnemy(20,y));
                    contEnemigos[Unidad.CABALLERO]++;
                    break;
                default:
            }
        }

        @Override
        public void run() {
            if( (!Window.isPaused() || !Window.isOver()) && enemigos.size() <= 4)
                createEnemy( (int)(Math.random()*11) );

        }
    }
}
