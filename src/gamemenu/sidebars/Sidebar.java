package gamemenu.sidebars;

import elements.Elemento;
import elements.Terreno;
import gamemenu.Window;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;

public abstract class Sidebar extends JPanel {
    private static final int GRID_SIZE_X = 1;
    private static final int GRID_SIZE_Y = 12;
    private static final int PWIDTH = 48;
    private static final int PHEIGHT = 576;

    protected static Image fondo;

    public Sidebar(){
        setLayout(new GridLayout(GRID_SIZE_Y, GRID_SIZE_X));
        setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        setIgnoreRepaint(true);

        initComponents();
    }
    private void initComponents(){
        ImageIcon img = new ImageIcon(getClass().getResource("/img/optionmenu.png"));
        fondo = img.getImage().getScaledInstance(Elemento.CELL_SIZE, Window.getPHeight(), Image.SCALE_SMOOTH);
    }
}
