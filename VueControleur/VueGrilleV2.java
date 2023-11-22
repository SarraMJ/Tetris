package VueControleur;

import Modele.GrilleSimple;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;
import java.awt.Color;
import java.util.Random;

class VueGrilleV2 extends JPanel implements Observer {

    private final static int TAILLE = 16;
    private GrilleSimple modele;
    Canvas c;

    public VueGrilleV2(GrilleSimple _modele) {

        modele = _modele;
        setLayout(new BorderLayout());
        Dimension dim = new Dimension(TAILLE*modele.TAILLE,TAILLE*modele.TAILLE);
        //this.setPreferredSize(dim);



        //setBackground(Color.black);

        c = new Canvas() {


            public void paint(Graphics g) {


                for (int i = 0; i < modele.TAILLE; i++) {
                    for (int j = 0; j < modele.TAILLE; j++) {
                        //if (!(i == modele.getPieceCourante().getx() && j == modele.getPieceCourante().gety())) {
                        boolean[][] tabG;
                        tabG= modele.getTabGrille();
                        Color couleurCase;
                        couleurCase=obtenirCouleur();

                        if(!tabG[i][j]) {
                            g.setColor(Color.WHITE);
                        }
                        if(tabG[i][j]) {
                            g.setColor(couleurCase);
                        }
                        g.fillRect(i * TAILLE, j * TAILLE, TAILLE, TAILLE);
                        g.setColor(Color.BLACK);
                        g.drawRoundRect(i * TAILLE, j * TAILLE, TAILLE, TAILLE, 1, 1);

                    }

                }


                Color ColorP = obtenirCouleur();
                g.setColor(ColorP);

                int PieceX=modele.getPieceCourante().getx();
                int PieceY=modele.getPieceCourante().gety();

                for(int i=0;i<4;i++){
                    for(int j=0;j<4;j++){
                        if (modele.getPieceCourante().getTabPiece(i,j)){

                            g.setColor(ColorP);
                            g.fillRect((PieceX * TAILLE)+(TAILLE*i), (PieceY * TAILLE)+(TAILLE*j), TAILLE, TAILLE);
                        }
                    }
                }


            }
        };

        c.setPreferredSize(dim);
        add(c, BorderLayout.CENTER);
    }


    @Override
    public void update(Observable o, Object arg) {

        BufferStrategy bs = c.getBufferStrategy(); // bs + dispose + show : double buffering pour éviter les scintillements
        if(bs == null) {
            c.createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        c.paint(g); // appel de la fonction pour dessiner
        g.dispose();
        //Toolkit.getDefaultToolkit().sync(); // forcer la synchronisation
        bs.show();
    }

    public Color obtenirCouleur() {

        if(modele.getPieceCourante().getCodeCouleur()==1) {
            return Color.CYAN;
        }
        if(modele.getPieceCourante().getCodeCouleur()==2) {
            return Color.YELLOW;
        }
        if(modele.getPieceCourante().getCodeCouleur()==3) {
            return new Color(128, 0, 128); // Violet
        }
        if(modele.getPieceCourante().getCodeCouleur()==4) {
            return Color.ORANGE;
        }
        if(modele.getPieceCourante().getCodeCouleur()==5) {
            return Color.BLUE;
        }
        if(modele.getPieceCourante().getCodeCouleur()==6) {
            return Color.RED;
        }
        if(modele.getPieceCourante().getCodeCouleur()==7) {
            return Color.GREEN;
        }
        else return Color.BLACK;
    }
}
