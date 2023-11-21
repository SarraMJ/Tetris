package Modele;
import java.util.Random;

public class Piece implements Runnable {

    private int x = 5;
    private int y = 5;

    //coordonnees de la case la plus basse qui est true
    private int basX;
    private int basY;
    private int dY = 1;

    private boolean[][] tabPiece;

    private int codeCouleur = 5;
    private GrilleSimple grille;

    public void randPiece(){
        Random random = new Random();
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                tabPiece[i][j]= random.nextBoolean();
            }
        }
    }

    public Piece(GrilleSimple _grille) {

        grille = _grille;
        tabPiece= new boolean[4][4];
        randPiece();
        plusbasY();
        System.out.print("basX "+basX);
        System.out.print("basY "+basY);
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

    public void plusbasY(){ //les coordonnees de la case la plus basse de la piece qui est true
        basY=y;
        basX=x;
        for (int i=0;i<4;i++) {
            for (int j = 0; j < 4; j++) {
                if(tabPiece[i][j]){
                    if (y+j >= basY){
                        basY=y+j;
                        basX=x+i;
                    }
                }
            }
        }
    }

    public void run() {  //on update les x,y,basX,basY

        int nextY = y;
        int nextX = x;

        int nextBasY=basY;
        int nextBasX=basX;

        nextY += dY;
        nextBasY += dY;

        //pour le moment on rajoute pas de x pour la bouger
        //apres on rajoutera dX

        if (grille.validationPosition(nextBasX, nextBasY)) {
            y = nextY;
            x = nextX;
            basY=nextBasY;
            basX=nextBasX;
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

    public boolean getTabPiece(int i,int j){
        return tabPiece[i][j];
    }

}
