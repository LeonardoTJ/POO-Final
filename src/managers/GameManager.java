package managers;

import elements.Elemento;
import elements.buildings.Edificio;
import elements.buildings.MakerBuilding;
import elements.units.Unidad;
import gamemenu.Window;
import gamemenu.sidebars.StatusBar;

import java.awt.Graphics;

public class GameManager implements Manager {
        //  Arreglo de recursos utilizados para crear unidades y construir edificios
    public static int[] recursos;
    public static final int COMIDA = 0;
    public static final int MADERA = 1;
    public static final int PIEDRA = 2;
    public static final int ORO    = 3;

    private static final int INITIAL_RESOURCE = 100;

                                        //    C, M, P, O
    public static final int[][] unitCost = {{50, 0, 0,30},   //  Aldeano
                                            {30, 0, 0,80},   //  Guardia
                                            {20,80, 0, 0},   //  Lancero
                                            {40,40, 0,20},   //  Arquero
                                            {80,30, 0,40}};  //  Caballero

                                         //   C,  M,  P,  O
    public static final int[][] buildCost = {{0,  0,  0,  0},
                                             {0, 50, 20,  0},   //  Casa
                                             {0,150, 40,  0},   //  Centro urbano
                                             {0,130, 70, 50},   //  Cuartel
                                             {0, 70,120, 20},   //  Torre
                                             {0,100,  0, 30}};  //  Molino

    public static final int WIN_CONDITION = 5; //  Enemigos a vencer para ganar
    public static final String[] gameOverMessage = {"Game Over.",
                                                    "You win!"};


    public GameManager(){           //  Inicializado al tamaño correcto para
        recursos = new int[4];      //  empatar los indices con el arreglo FIELD_GRID
        recursos[COMIDA] = INITIAL_RESOURCE + 100;
        recursos[MADERA] = INITIAL_RESOURCE;
        recursos[PIEDRA] = INITIAL_RESOURCE;
        recursos[ORO]    = INITIAL_RESOURCE;
    }

    public static void almacenarRecurso(int x, int y){
        int res = WindowManager.campo[y][x].getResource();

        switch(res){
            case MADERA:
                System.out.print("Recogiendo madera ");
                recursos[MADERA] += 10;
                System.out.println(recursos[MADERA]);
                StatusBar.setHighlight(MADERA);
                break;
            case ORO:
                System.out.print("Recogiendo oro ");
                recursos[ORO] += 10;
                System.out.println(recursos[ORO]);
                StatusBar.setHighlight(ORO);
                break;
            case PIEDRA:
                System.out.print("Recogiendo piedra ");
                recursos[PIEDRA] += 10;
                System.out.println(recursos[PIEDRA]);
                StatusBar.setHighlight(PIEDRA);
                break;
        }
    }

    public static void almacenarRecurso(){
        System.out.print("Recogiendo comida ");
        recursos[COMIDA] += 5;
        System.out.println(recursos[COMIDA]);
        StatusBar.setHighlight(COMIDA);
    }

    public static boolean validarRecurso(Elemento e, int type){
        if(e instanceof Unidad){
            if(recursos[COMIDA] < buildCost[type][COMIDA]
            || recursos[MADERA] < buildCost[type][MADERA]
            || recursos[PIEDRA] < buildCost[type][PIEDRA]
            || recursos[ORO]    < buildCost[type][ORO]){
                System.out.println("No es posible construir");
                return false;
            }
            else
                return true;
        }
        else{
            if(recursos[COMIDA] < unitCost[type][COMIDA]
            || recursos[MADERA] < unitCost[type][MADERA]
            || recursos[PIEDRA] < unitCost[type][PIEDRA]
            || recursos[ORO]    < unitCost[type][ORO]){
                System.out.println("No es posible crear");
                return false;
            }
            else
                return true;
        }
    }

    public static void gastarRecurso(Elemento e, int type){
        if(!validarRecurso(e, type))
            return;

        if(e instanceof Unidad){
            recursos[COMIDA] -= buildCost[type][COMIDA];
            recursos[MADERA] -= buildCost[type][MADERA];
            recursos[PIEDRA] -= buildCost[type][PIEDRA];
            recursos[ORO]    -= buildCost[type][ORO];
        }
        else{
            recursos[COMIDA] -= unitCost[type][COMIDA];
            recursos[MADERA] -= unitCost[type][MADERA];
            recursos[PIEDRA] -= unitCost[type][PIEDRA];
            recursos[ORO]    -= unitCost[type][ORO];
        }
    }

    public static void gameOver(boolean playerWin){
            Window.stopGame(playerWin);
    }

}
