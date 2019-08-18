package gamemenu.main;

import elements.Elemento;
import gamemenu.Window;
import managers.WindowManager;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseMenu extends JFrame {

    private boolean status;

    public PauseMenu(){
        super("Pause Menu");
        setUndecorated(true);
        add(new PausePanel(this));

        Point pos = Elemento.getPanelCoord(7,3 );
        setLocation((int)pos.getX(), (int)pos.getY());
        setAlwaysOnTop(true);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        requestFocus();

        setVisible(false);
    }

    public void update(){
        setVisible(Window.isPaused());
        requestFocus();
    }

    class PausePanel extends JPanel{
        private Image fondo;
        private JFrame frame;
        PausePanel(JFrame frame){
            this.frame = frame;
            setLayout( new BoxLayout(this, BoxLayout.Y_AXIS));
            initBotones();
            fondo = (new ImageIcon(getClass().getResource("/img/pauseScreen.png")).getImage());
            setPreferredSize(new Dimension(Elemento.CELL_SIZE*6, Elemento.CELL_SIZE*3));
        }

        private void initBotones(){
            add( Box.createRigidArea(new Dimension(Elemento.CELL_SIZE*6, Elemento.CELL_SIZE+18)) );
            JButton botonContinuar = new JButton("continuar");
            botonContinuar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Window.pauseGame();
                    frame.dispose();
                }
            });
            botonContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
            //botonContinuar.setLocation(90,150);
            botonContinuar.setOpaque(false);
            botonContinuar.setContentAreaFilled(false);
            botonContinuar.setBorderPainted(false);
            botonContinuar.setFont(new Font("Consolas", Font.BOLD, 24));
            add(botonContinuar);

            JButton botonSalir = new JButton("salir");
            botonSalir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            botonSalir.setAlignmentX(Component.CENTER_ALIGNMENT);
            //botonSalir.setLocation(90,150);
            botonSalir.setOpaque(false);
            botonSalir.setContentAreaFilled(false);
            botonSalir.setBorderPainted(false);
            botonSalir.setFont(new Font("Consolas", Font.BOLD, 24));
            add(botonSalir);
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(fondo, 0,0, this);
            g.setFont(new Font("Poor Richard", Font.PLAIN, 67));
            g.setColor(Color.WHITE);
            g.drawString("Pausa", 75, 55);
        }
    }
}