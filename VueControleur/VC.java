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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.io.File;



public class VC extends JFrame implements Observer {


    JTextField jt = new JTextField("");
    JButton jb = new JButton("pause");

    JButton js= new JButton("start");
    JButton jq=new JButton("quit");
    GrilleSimple modele;

    boolean gameO=true;


    Observer vueGrille;

    VueProchainesPieces vueProchainesPieces;
    private Executor ex =  Executors.newSingleThreadExecutor();

    JLabel scoreLabel = new JLabel("  Score: 0   ");

    private final Clip Soundjeu;
    private final Clip soundGameOver;
    public VC(GrilleSimple _modele) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        modele = _modele;

        setSize(650, 500);
        Soundjeu = loadSound("./ressources/music_tetris.wav");
        soundGameOver = loadSound("./ressources/game_over_music.wav");

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
                ex.execute(new Runnable() {
                    @Override
                    public void run() {
                        modele.getPieceCourante().togglePause();
                        boolean paused=modele.getPieceCourante().getPaused();
                        toggleSoundPause(paused);

                    }
                });
                requestFocusInWindow();
            }
        });

        js.addActionListener(new ActionListener() { //quand on rappuie sur start quand on est à gameover
            @Override
            public void actionPerformed(ActionEvent e) {
                // Si le jeu est terminé, réinitialiser le jeu
                System.out.println(modele.getGameOver());
                if (modele.getGameOver()) {
                    modele.resetGame();
                    modele.setGameOver(false);
                    modele.startGame();
                    startGameSound(modele.getGameOver());
                    gameO = true; // Réinitialiser le drapeau gameO
                }
                setFocusable(true);
                requestFocusInWindow();
            }
        });


        js.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //modele.setGameOver(false);
                modele.startGame();
                startGameSound(modele.getGameOver());
                requestFocusInWindow();
            }
        });

        jq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add logic to quit the game
                stopSounds();
                System.exit(0);
            }
        });




        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { //évènement clavier : object contrôleur qui réceptionne
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE: modele.action();
                    //System.out.println(modele.getPieceCourante().getOrientation());

                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { //translation a droite
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        int x=modele.getPieceCourante().getx();
                        int y=modele.getPieceCourante().gety();
                        boolean[][] tabP=modele.getPieceCourante().getTabPiece();
                        if (!modele.validationCollisionCoteDroit(x,y,tabP)) {
                            modele.getPieceCourante().translation(Direction.DROITE);
                         }
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
                        int x=modele.getPieceCourante().getx();
                        int y=modele.getPieceCourante().gety();
                        boolean[][] tabP=modele.getPieceCourante().getTabPiece();
                        if (!modele.validationCollisionCoteGauche(x,y,tabP)) {
                            modele.getPieceCourante().translation(Direction.GAUCHE);
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
                        boolean paused=modele.getPieceCourante().getPaused();
                        toggleSoundPause(paused);
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

                //gameover sound
                boolean gameover= modele.getGameOver();

                if(gameover && gameO){
                    startGameSound(gameover);
                    gameO=false;
                }

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
                                           // Arrêter la musique au début du jeu
                                           vc.stopSounds();

                                           vc.setVisible(true);

                                       }
                                   }
        );
    }

    private Clip loadSound(String fileName) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource(fileName);

            if (url == null) {
                throw new IllegalArgumentException("Le fichier son " + fileName + " n'a pas pu être chargé. URL nulle.");
            }

            System.out.println("Chargement du fichier son depuis l'URL : " + url);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du fichier son : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void stopSounds() {
        if (Soundjeu != null && Soundjeu.isRunning()) {
            Soundjeu.stop();
            Soundjeu.setFramePosition(0);
        }
        if (soundGameOver != null && soundGameOver.isRunning()) {
            soundGameOver.stop();
            soundGameOver.setFramePosition(0);
        }
    }
    private void startGameSound(boolean gameOver) {
        stopSounds(); // Arrêter les sons en cours
        if (!gameOver && Soundjeu != null) {
            Soundjeu.loop(Clip.LOOP_CONTINUOUSLY);
        } else if (soundGameOver != null) {
            soundGameOver.start();
        }
    }
    private void playGameOverSound() {
        if (soundGameOver != null) {
            System.out.println("Game over, playing new sound");
            soundGameOver.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private void toggleSoundPause(boolean paused) {
        if (paused) {
            Soundjeu.stop();
            Soundjeu.setFramePosition(0);  // Réinitialiser la position du son principal
        } else {
            Soundjeu.start();
        }

    }


}
