// VueProchainesPieces.java
package VueControleur;

import Modele.GrilleSimple;
import Modele.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.LinkedList;
import java.util.Queue;

public class VueProchainesPieces extends JPanel implements Observer {

    private GrilleSimple modele;

    private final static int TAILLE = 16;

    private Queue<Piece> prochainesPieces = new LinkedList<>();
    public VueProchainesPieces(GrilleSimple _modele) {
        modele = _modele;
        modele.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        prochainesPieces = new LinkedList<>(modele.getProchainesPieces());
        repaint(); // Mettez à jour l'affichage des prochaines pièces lorsque le modèle change
    }

    @Override
    protected void paintComponent(Graphics g) {
        Piece p=modele.getPieceCourante();
        if (p!=null && !modele.getGameOver()) {
            super.paintComponent(g);

            int startX = 0; // Position de départ pour afficher les prochaines pièces

            Queue<Piece> prochainesPieces = new LinkedList<>(modele.getProchainesPieces());

            for (int k = 0; k < 3 && !prochainesPieces.isEmpty(); k++) {
                Piece piece = prochainesPieces.poll();
                Color couleurPiece = VueGrilleV2.switchCouleurToColor(piece.getCouleurfromCode(piece.getCodeCouleur()));
                drawPiece(g, piece, startX, 0, couleurPiece);
                startX += 5 * TAILLE; // Ajuster l'espacement entre les pièces (5 * TAILLE est un exemple)
            }
        }
    }
    private void drawPiece(Graphics g, Piece piece, int startX, int startY, Color couleur) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (piece.getTabPiece(i, j)) {
                    g.setColor(couleur);
                    g.fillRect(startX + i * TAILLE, startY + j * TAILLE, TAILLE, TAILLE);
                    g.setColor(Color.BLACK);
                    g.drawRoundRect(startX + i * TAILLE, startY + j * TAILLE, TAILLE, TAILLE, 1, 1);
                }
            }
        }
    }
}
