package VueControleur;

import Modele.Couleur;
import Modele.GrilleSimple;
import Modele.Piece;

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


    VueProchainesPieces vueProchainesPieces;

    public VueGrilleV2(GrilleSimple _modele) {

        modele = _modele;
        setLayout(new BorderLayout());
        Dimension dim = new Dimension(TAILLE*modele.TAILLE,TAILLE*modele.TAILLE);
        //this.setPreferredSize(dim);



        //setBackground(Color.black);

        c = new Canvas() {


            public void paint(Graphics g) {
                Piece p=modele.getPieceCourante();
                if (p!=null && !modele.getGameOver()) {

                    for (int i = 0; i < modele.TAILLE; i++) {
                        for (int j = 0; j < modele.TAILLE; j++) {

                            Couleur[][] tabG;
                            tabG = modele.getTabGrille();
                            Couleur couleurCase = tabG[i][j];
                            Color ColorCase = switchCouleurToColor(couleurCase);

                            couleurCase = p.getCouleurfromCode(p.getCodeCouleur());


                            g.setColor(ColorCase);

                            g.fillRect(i * TAILLE, j * TAILLE, TAILLE, TAILLE);
                            g.setColor(Color.BLACK);
                            g.drawRoundRect(i * TAILLE, j * TAILLE, TAILLE, TAILLE, 1, 1);

                        }

                    }


                    Color ColorP = obtenirCouleur();
                    g.setColor(ColorP);

                    int PieceX = modele.getPieceCourante().getx();
                    int PieceY = modele.getPieceCourante().gety();

                    for (int i = 0; i < 4; i++) { //on dessine la piece
                        for (int j = 0; j < 4; j++) {
                            if (modele.getPieceCourante().getTabPiece(i, j)) {

                                g.setColor(ColorP);
                                g.fillRect((PieceX * TAILLE) + (TAILLE * i), (PieceY * TAILLE) + (TAILLE * j), TAILLE, TAILLE);
                            }
                        }
                    }

                }else if(!modele.getGameOver()){
                    //si p est null et que je jeu n'est pas à gameover, on affiche tetris coloriée au milieu de la grille
                    String tetrisText = "Tetris";
                    int x = (getWidth() - tetrisText.length() * 36) / 2;

                    for (int i = 0; i < tetrisText.length(); i++) {
                        char letter = tetrisText.charAt(i);
                        Color letterColor = getLetterColor(letter);  // Function to get a different color for each letter
                        g.setColor(letterColor);
                        g.setFont(new Font("Arial", Font.BOLD, 36));  // You can adjust the font and size

                        g.drawString(String.valueOf(letter), x, (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent());
                        x += 36;  // Adjust the spacing between letters as needed
                    }
                }else { //gameover
                    g.setColor(Color.RED);
                    g.setFont(new Font("Arial", Font.BOLD, 50));  // You can adjust the font and size
                    String gameOverText = "Game Over";

                    // Calculate the x-coordinate to center the text
                    int x = (getWidth() - g.getFontMetrics().stringWidth(gameOverText)) / 2;

                    // Calculate the y-coordinate to center the text vertically
                    int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();

                    g.drawString(gameOverText, x, y);

                }


            }
        };

        c.setPreferredSize(dim);
        add(c, BorderLayout.CENTER);

    }

    private Color getLetterColor(char letter) {
        //donne la couleur de chaque lettre du mot tetris
        switch (Character.toUpperCase(letter)) {
            case 'T':
                return Color.RED;
            case 'E':
                return Color.GREEN;
            case 'R':
                return Color.BLUE;
            case 'I':
                return Color.YELLOW;
            case 'S':
                return Color.ORANGE;
            default:
                return Color.WHITE;  // Default color for unknown letters
        }
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

    public static Color switchCouleurToColor(Couleur couleur) {
        switch (couleur) {
            case CYAN:
                return Color.CYAN;
            case YELLOW:
                return Color.YELLOW;
            case PURPLE:
                return new Color(128, 0, 128);
            case ORANGE:
                return Color.ORANGE;
            case BLUE:
                return Color.BLUE;
            case RED:
                return Color.RED;
            case GREEN:
                return Color.GREEN;
            case WHITE:
                return Color.WHITE;
            case BLACK:
                return Color.BLACK;

        }
        return Color.BLACK;
    }
}
