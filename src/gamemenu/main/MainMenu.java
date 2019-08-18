package gamemenu.main;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu(){
        setTitle("Imperio");
        setLayout(new FlowLayout());
        setUndecorated(true);
        add(new MainScreen(this));

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        requestFocus();

        setVisible(true);
    }


    private class MainScreen extends JPanel{
        private JFrame frame;
        private Image fondo;
        private Image help;
        private boolean showHelp;

        private MainScreen(JFrame frame){
            this.frame = frame;
            fondo = (new ImageIcon(getClass().getResource("/img/mainbg2.png")).getImage());
            help = (new ImageIcon(getClass().getResource("/img/help.png")).getImage());
            setPreferredSize( new Dimension(fondo.getWidth(this), fondo.getHeight(this)) );

            initBotones();
        }

        private void initBotones(){
            JButton botonInicio = new JButton();
            botonInicio.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    new Visual();
                }
            });
            //botonInicio.setLocation(625,388);
            botonInicio.setOpaque(false);
            botonInicio.setContentAreaFilled(false);
            botonInicio.setBorderPainted(false);
            botonInicio.setIcon(new ImageIcon(getClass().getResource("/img/iniciar.png")));
            add(botonInicio);

            JButton botonAyuda = new JButton();
            botonAyuda.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showHelp = !showHelp;
                    repaint();
                }
            });
            //botonAyuda.setLocation(625+123,388);
            botonAyuda.setOpaque(false);
            botonAyuda.setContentAreaFilled(false);
            botonAyuda.setBorderPainted(false);
            botonAyuda.setIcon(new ImageIcon(getClass().getResource("/img/ayudaBoton.png")));
            add(botonAyuda);

            JButton botonSalir = new JButton();
            botonSalir.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            //botonSalir.setLocation(625+123,388);
            botonSalir.setOpaque(false);
            botonSalir.setContentAreaFilled(false);
            botonSalir.setBorderPainted(false);
            botonSalir.setIcon(new ImageIcon(getClass().getResource("/img/salir.png")));
            add(botonSalir);
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, this);
            g.setFont(new Font("Poor Richard", Font.PLAIN, 67));
            g.setColor(Color.WHITE);
            g.drawString("Imperio", 466, 125);
            if(showHelp)
                g.drawImage(help, 175, 60, this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                new MainMenu();
            }
        });
    }
}
