package gamemenu.sidebars;

import elements.Elemento;
import elements.buildings.Edificio;
import elements.units.Unidad;
import gamemenu.Window;
import managers.GameManager;
import managers.UnitManager;
import managers.WindowManager;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class StatusBar extends Sidebar {
    private static boolean mouseOver;
    private static String[] cost;

    private static Image foodIcon;
    private static Image woodIcon;
    private static Image stoneIcon;
    private static Image goldIcon;

    private static int highlightRes;   //  Indicar el recurso que se esta almacenando actualmente
    private static Image highlight;
    private static boolean showHelp;
    private static Image help;
    private static Image helpIcon;
    private static Image killIndicator;

    public StatusBar(){
        cost = new String[4];
        cost[0] = "";
        cost[1] = "";
        cost[2] = "";
        cost[3] = "";
        highlightRes = -1;
        highlight = (new ImageIcon(getClass().getResource("/img/highlightRes.png")).getImage());
        help = (new ImageIcon(getClass().getResource("/img/help.png")).getImage());
        helpIcon = (new ImageIcon(getClass().getResource("/img/helpIcon.png")).getImage());
        killIndicator = (new ImageIcon(getClass().getResource("/img/swords_red.png")).getImage());

        foodIcon = (new ImageIcon(getClass().getResource("/img/comida.png")).getImage().getScaledInstance(Elemento.CELL_SIZE, Elemento.CELL_SIZE/2, Image.SCALE_SMOOTH));
        woodIcon = (new ImageIcon(getClass().getResource("/img/madera.png")).getImage().getScaledInstance(Elemento.CELL_SIZE, Elemento.CELL_SIZE/2, Image.SCALE_SMOOTH));
        stoneIcon = (new ImageIcon(getClass().getResource("/img/piedramenu.png")).getImage().getScaledInstance(Elemento.CELL_SIZE, Elemento.CELL_SIZE, Image.SCALE_SMOOTH));
        goldIcon = (new ImageIcon(getClass().getResource("/img/oromenu.png")).getImage().getScaledInstance(Elemento.CELL_SIZE, Elemento.CELL_SIZE, Image.SCALE_SMOOTH));
    }

    public static void setHighlight(int resource){
        highlightRes = resource;
    }

    public static void showHelp(){
        showHelp = !showHelp;
    }

    public static void update(){
        if(UnitManager.getActiveElement() < 0)
            return;
        Point mousePos = new Point(WindowManager.getMousePos());
        String comida = "";
        String madera = "";
        String piedra = "";
        String oro    = "";

        if( (mousePos.getX() <= Elemento.CELL_SIZE) && (mousePos.getY() <= Elemento.CELL_SIZE*5) ){
            int pos = 0;
            if(mousePos.getY() <= Elemento.CELL_SIZE)
                pos = 1;
            else if(mousePos.getY() <= Elemento.CELL_SIZE*2)
                pos = 2;
            else if(mousePos.getY() <= Elemento.CELL_SIZE*3)
                pos = 3;
            else if(mousePos.getY() <= Elemento.CELL_SIZE*4)
                pos = 4;
            else if(mousePos.getY() <= Elemento.CELL_SIZE*5)
                pos = 5;

            int active = UnitManager.getActiveElement();

            switch(active){
                case Unidad.ALDEANO:
                    if(pos > 0){
                        comida = Integer.toString(GameManager.buildCost[pos][GameManager.COMIDA]);
                        madera = Integer.toString(GameManager.buildCost[pos][GameManager.MADERA]);
                        piedra = Integer.toString(GameManager.buildCost[pos][GameManager.PIEDRA]);
                        oro = Integer.toString(GameManager.buildCost[pos][GameManager.ORO]);
                        mouseOver = true;
                    }
                    else
                        mouseOver = false;
                case Edificio.C_URBANO:
                    if(pos == 1){
                        comida = Integer.toString(GameManager.unitCost[Unidad.ALDEANO][GameManager.COMIDA]);
                        madera = Integer.toString(GameManager.unitCost[Unidad.ALDEANO][GameManager.MADERA]);
                        piedra = Integer.toString(GameManager.unitCost[Unidad.ALDEANO][GameManager.PIEDRA]);
                        oro = Integer.toString(GameManager.unitCost[Unidad.ALDEANO][GameManager.ORO]);
                        mouseOver = true;
                    }
                    break;
                case Edificio.CUARTEL:
                    if(pos > 0 && pos < 5){
                        comida = Integer.toString(GameManager.unitCost[pos][GameManager.COMIDA]);
                        madera = Integer.toString(GameManager.unitCost[pos][GameManager.MADERA]);
                        piedra = Integer.toString(GameManager.unitCost[pos][GameManager.PIEDRA]);
                        oro = Integer.toString(GameManager.unitCost[pos][GameManager.ORO]);
                        mouseOver = true;
                    }
                    else
                        mouseOver = false;
            }
            if ((comida.length() > 0) && Integer.parseInt(comida) > 0)
                cost[GameManager.COMIDA] = "-"+comida;
            else
                cost[GameManager.COMIDA] = "";
            if ( (madera.length() > 0) && Integer.parseInt(madera) > 0)
                cost[GameManager.MADERA] = "-"+madera;
            else
                cost[GameManager.MADERA] = "";
            if ((piedra.length() > 0) && Integer.parseInt(piedra)> 0)
                cost[GameManager.PIEDRA] = "-"+piedra;
            else
                cost[GameManager.PIEDRA] = "";
            if ((oro.length() > 0) && Integer.parseInt(oro) > 0)
                cost[GameManager.ORO] = "-"+oro;
            else
                cost[GameManager.ORO] = "";
        }
        else
            mouseOver = false;
    }

    private static void showResource(Graphics g){
        g.setFont(new Font("Consolas", Font.BOLD, 24));
        g.setColor(Color.BLACK);

        g.drawImage(woodIcon, Window.getPWidth() - Elemento.CELL_SIZE, 0, null);
        g.drawString(Integer.toString(GameManager.recursos[1]),Window.getPWidth() - Elemento.CELL_SIZE, Elemento.CELL_SIZE);

        g.drawImage(stoneIcon, Window.getPWidth() - Elemento.CELL_SIZE, Elemento.CELL_SIZE, null);
        g.drawString(Integer.toString(GameManager.recursos[2]),Window.getPWidth() - Elemento.CELL_SIZE, Elemento.CELL_SIZE*2+24);

        g.drawImage(goldIcon, Window.getPWidth() - Elemento.CELL_SIZE, Elemento.CELL_SIZE*3-24, null);
        g.drawString(Integer.toString(GameManager.recursos[3]),Window.getPWidth() - Elemento.CELL_SIZE, Elemento.CELL_SIZE*4);

        g.drawImage(foodIcon, Window.getPWidth() - Elemento.CELL_SIZE, Elemento.CELL_SIZE*4, null);
        g.drawString(Integer.toString(GameManager.recursos[0]),Window.getPWidth() - Elemento.CELL_SIZE, Elemento.CELL_SIZE*5);
    }

    private static void showCost(Graphics g){
        g.setFont(new Font("Consolas", Font.BOLD, 15));
        g.setColor(Color.WHITE);
        g.drawString(cost[GameManager.MADERA],Window.getPWidth() - Elemento.CELL_SIZE+10, Elemento.CELL_SIZE/2);
        g.drawString(cost[GameManager.PIEDRA],Window.getPWidth() - Elemento.CELL_SIZE+10, Elemento.CELL_SIZE*2);
        g.drawString(cost[GameManager.ORO],Window.getPWidth() - Elemento.CELL_SIZE+10, Elemento.CELL_SIZE*3+24);
        g.drawString(cost[GameManager.COMIDA],Window.getPWidth() - Elemento.CELL_SIZE+10, Elemento.CELL_SIZE*4+24);
    }

    public static void draw(Graphics g){        //  Dibujar menu en pantalla con informacion de recursos
        g.drawImage(fondo, Window.getPWidth()- Elemento.CELL_SIZE, 0, null);

        showResource(g);
        if(highlightRes >= 0){
            switch(highlightRes){
                case GameManager.COMIDA:
                    g.drawImage(highlight, Window.getPWidth()-Elemento.CELL_SIZE, Elemento.CELL_SIZE*4-25, null);
                    break;
                case GameManager.MADERA:
                    g.drawImage(highlight, Window.getPWidth()-Elemento.CELL_SIZE, -25, null);
                    break;
                case GameManager.PIEDRA:
                    g.drawImage(highlight, Window.getPWidth()-Elemento.CELL_SIZE, Elemento.CELL_SIZE, null);
                    break;
                case GameManager.ORO:
                    g.drawImage(highlight, Window.getPWidth()-Elemento.CELL_SIZE, Elemento.CELL_SIZE*3-24, null);
                    break;
            }
            showResource(g);
        }

        if(mouseOver)
            showCost(g);
        g.setFont(new Font("Consolas", Font.BOLD, 15));
        g.setColor(Color.RED);
        g.drawImage(killIndicator, Window.getPWidth()-Elemento.CELL_SIZE, Window.getPHeight()-Elemento.CELL_SIZE*3, null);
        g.drawString(Integer.toString(UnitManager.getKillCount()), Window.getPWidth()-Elemento.CELL_SIZE+30,
                Window.getPHeight()-Elemento.CELL_SIZE*2-12);

        g.drawImage(helpIcon, Window.getPWidth()-Elemento.CELL_SIZE, Window.getPHeight()-Elemento.CELL_SIZE, null);
        if(showHelp)
            g.drawImage(help, 135, 40, null);
    }
}