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

    JButton js= new JButton("start");
    JButton jq=new JButton("quit");
    GrilleSimple modele;

    Observer vueGrille;

    VueProchainesPieces vueProchainesPieces;
    private Executor ex =  Executors.newSingleThreadExecutor();

    JLabel scoreLabel = new JLabel("  Score: 0   ");

    public VC(GrilleSimple _modele) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        modele = _modele;

        setSize(650, 500);

        // Panneau principal avec BorderLayout
        // Panneau principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(jt, BorderLayout.NORTH);
        // Panneau pour la grille
        vueGrille = new VueGrilleV2(modele);
        vueProchainesPieces = new VueProchainesPieces(modele);
        JPanel grillePanel = (JPanel) vueGrille;
        JPanel ProchainesPiecesPanel=(JPanel) vueProchainesPieces;


        //grillePanel.add(scoreLabel,BorderLayout.EAST);

        // Panneau pour le score et le bouton pause avec BoxLayout
        JPanel scorePausePanel = new JPanel();

        scorePausePanel.setLayout(new BoxLayout(scorePausePanel, BoxLayout.X_AXIS));

        // Ajout du score au panneau

        scorePausePanel.add(js);
        scorePausePanel.add(jq);

        // Ajout d'un espace vertical entre le score et le bouton pause
        scorePausePanel.add(Box.createVerticalStrut(10));

        // Ajout du bouton pause au panneau
        scorePausePanel.add(jb);

        // Ajout de la grille au centre
        mainPanel.add(grillePanel, BorderLayout.WEST);

        // Ajout du label de score à droite de la grille
        mainPanel.add(scoreLabel, BorderLayout.EAST);

        mainPanel.add(ProchainesPiecesPanel, BorderLayout.CENTER);

        // Ajout du panneau scorePause au sud
        mainPanel.add(scorePausePanel, BorderLayout.SOUTH);

        // Ajout du panneau principal
        setContentPane(mainPanel);


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

        js.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modele.setGameOver(false);
                modele.startGame();
                requestFocusInWindow();
            }
        });

        jq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add logic to quit the game
                System.exit(0);
            }
        });




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
                        int x=modele.getPieceCourante().getx();
                        int y=modele.getPieceCourante().gety();
                        int suivant_y=modele.getPieceCourante().gety()+1;
                        boolean[][] tabP=modele.getPieceCourante().getTabPiece();
                        if(!modele.validationCollision_2(x,y,tabP) && !modele.validationCollision_2(x,suivant_y,tabP)) {
                            modele.getPieceCourante().translation(Direction.BAS);

                        }
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
                scoreLabel.setText("  Score: " + modele.getScore()+"   ");
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
