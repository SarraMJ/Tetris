package VueControleur;

import Modele.Direction;
import Modele.GrilleSimple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class VC extends JFrame implements Observer {


    JTextField jt = new JTextField("");
    JButton jb = new JButton("pause");
    GrilleSimple modele;

    Observer vueGrille;
    private Executor ex =  Executors.newSingleThreadExecutor();

    public VC(GrilleSimple _modele) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        modele = _modele;

        setSize(350, 400);
        JPanel jp = new JPanel(new BorderLayout());
        jp.add(jt, BorderLayout.NORTH);
        jp.add(jb, BorderLayout.SOUTH);

       // vueGrille = new VueGrilleV1(modele); // composants swing, saccades
        vueGrille = new VueGrilleV2(modele); // composant AWT dédié

        jp.add((JPanel)vueGrille, BorderLayout.CENTER);
        setContentPane(jp);


        jb.addActionListener(new ActionListener() { //évènement bouton : object contrôleur qui réceptionne
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(" Bouton DOOOOOO ");
                ex.execute(new Runnable() {
                    @Override
                    public void run() {
                        modele.getPieceCourante().togglePause();

                    }
                });
                requestFocusInWindow();
            }
        });
/*
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        modele.action();
                        break;
                    case KeyEvent.VK_RIGHT:
                        ex.execute(new Runnable() {
                            @Override
                            public void run() {
                                modele.getPieceCourante().translation(Direction.DROITE);
                            }
                        });
                        break;
                    case KeyEvent.VK_LEFT:
                        ex.execute(new Runnable() {
                            @Override
                            public void run() {
                                modele.getPieceCourante().translation(Direction.GAUCHE);
                            }
                        });
                        break;
                    case KeyEvent.VK_DOWN:
                        ex.execute(new Runnable() {
                            @Override
                            public void run() {
                                modele.getPieceCourante().translation(Direction.BAS);
                            }
                        });
                        break;
                }
            }
        }); */



        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { //évènement clavier : object contrôleur qui réceptionne
                super.keyPressed(e);
                System.out.print(" Bouton2 ");
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE: modele.action();

                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { //translation a droite
                super.keyPressed(e);
                System.out.print(" VK_RIGHT ");
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        modele.getPieceCourante().translation(Direction.DROITE);
                        break;
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { //translation a gauche
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        modele.getPieceCourante().translation(Direction.GAUCHE);
                        break;
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        modele.getPieceCourante().translation(Direction.BAS);
                        break;
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_P:
                        modele.getPieceCourante().togglePause();
                        break;
                }
            }
        });





        setFocusable(true);
        requestFocusInWindow();


    }

    static long lastTime = System.currentTimeMillis();

    @Override
    public void update(Observable o, Object arg) { // rafraichissement de la vue

        SwingUtilities.invokeLater(new Runnable() {
            //@Override
            public void run() {
                vueGrille.update(o, arg);

                jt.setText("Elapsed time : " + (System.currentTimeMillis() - lastTime) + "ms - x = " + modele.getPieceCourante().getx() + " y = " + modele.getPieceCourante().gety());
                lastTime = System.currentTimeMillis();

            }
        });

    }





    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    GrilleSimple m = new GrilleSimple();
                    VC vc = new VC(m);
                    vc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    m.addObserver(vc);
                    vc.setVisible(true);

                }
            }
        );
    }







}
