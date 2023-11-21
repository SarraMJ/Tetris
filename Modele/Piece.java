package Modele;
import java.util.Random;

public class Piece implements Runnable {

    private int x = 5;
    private int y = 5;
    private int dY = 1;

    private boolean[][] tabPiece;

    private int codeCouleur = 5;
    private GrilleSimple grille;
    

    public Piece(GrilleSimple _grille) {

        grille = _grille;
        tabPiece= new boolean[4][4];
    }


    public void randPiece(){
        Random random = new Random();
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                tabPiece[i][j]= random.nextBoolean();
            }
        }
    }

    // Méthode d'exemple pour afficher le tableau
    public void afficherTableau() {
        for (int i = 0; i < tabPiece.length; i++) {
            for (int j = 0; j < tabPiece[i].length; j++) {
                System.out.print(tabPiece[i][j] + " ");
            }
            System.out.println();
        }
    }



    public void action() {
        if(dY==1) {
            dY *= 0;
        }else dY=1;
    }

    public void run() {
        int nextY = y;
        int nextX = x;

        nextY += dY;

        if (grille.validationPosition(nextX, nextY)) {
            y = nextY;
            x = nextX;
            //System.out.println("pos" + x + " "+ y);
        } else {
            dY *= 0;
        }


    }

    public int getCodeCouleur() {
        return codeCouleur;
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }



}
