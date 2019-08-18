/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamemenu.main;

import gamemenu.sidebars.ActionMenu;
import gamemenu.sidebars.StatusBar;
import gamemenu.Window;
import managers.WindowManager;

import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Visual extends JFrame {

    public Visual(){
        setTitle("Imperio");
        setIconImage((new ImageIcon(getClass().getResource("/img/icon.png")).getImage()));
        makeGUI();
        pack();
        addKeyListener( new EscuchaTeclado() );
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                String[] ObjButtons = {"Si", "No"};
                int PromptResult = JOptionPane.showOptionDialog(null,"Salir de partida?","Salir",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
                if(PromptResult==JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
        setFocusable(true);
        //requestFocus();
        
        setVisible(true);
    }

    private void makeGUI(){
         //Container c = getContentPane();

         add(new Window(this));
    }

    class EscuchaTeclado extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            Thread hilo = new Thread( new Runnable(){
                public void run(){
                    if(Window.isOver())
                        return;
                    int keyCode = e.getKeyCode();
                    WindowManager.keyAction(keyCode);
                }
            });
            hilo.start();
        }
    }
}
