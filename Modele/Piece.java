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

    public void randPiece(int rand){
        /*Random random = new Random();
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                tabPiece[i][j]= random.nextBoolean();
            }
        } */
        if (rand==1){ // forme I-Tetrimino ou baton
            int i=0;
            int j=0;
            for (i=0;i<4;i++){
                tabPiece[i][j]=true;
            }
            for(i=0;i<4;i++){
                for(j=1;j<4;j++){
                    tabPiece[i][j]=false;
                }
            }
        }
        if (rand ==2){ //O_tetrimino
            for (int a=0; a< 4; a++){
                for(int b= 0; b<4; b++){
                    tabPiece[a][b]= false;
                }
            }
            tabPiece[0][0] = true;
            tabPiece[1][0] = true;
            tabPiece[0][1] = true;
            tabPiece[1][1] = true;
        }
        if (rand==3){ //T-Termino
            int j=0;
            int i=0;

            for(i=0;i<4;i++){ //toutes les cases à false
                for(j=0;j<4;j++){
                    tabPiece[i][j]=false;
                }
            }
            for (i=0;i<3;i++){
                j=0;
                tabPiece[i][j]=true;
            }
            tabPiece[1][1]=true;
        }

        if (rand == 4){ // L_tetrimino
            for (int a=0; a< 4; a++){
                for(int b= 0; b<4; b++){
                    tabPiece[a][b]= false;
                }
            }
            tabPiece[0][0] = true;
            tabPiece[1][0] = true;
            tabPiece[0][1] = true;
            tabPiece[2][0] = true;

        }

        if(rand==5){ //J-Termino
            int j=0;
            int i=0;

            for(i=0;i<4;i++){ //toutes les cases à false
                for(j=0;j<4;j++){
                    tabPiece[i][j]=false;
                }
            }
            for (i=0;i<3;i++){
                j=0;
                tabPiece[i][j]=true;
            }
            tabPiece[2][1]=true;
        }

        if(rand==6) { //J-Termino
            int j = 0;
            int i = 0;

            for (i = 0; i < 4; i++) { //toutes les cases à false
                for (j = 0; j < 4; j++) {
                    tabPiece[i][j] = false;
                }
            }
            for (i = 0; i < 2; i++) {
                j = 0;
                tabPiece[i][j] = true;
            }

            tabPiece[1][1] = true;
            tabPiece[2][1] = true;
        }
        if (rand == 7){// S_tetrimino
            for (int a=0; a< 4; a++){
                for(int b= 0; b<4; b++){
                    tabPiece[a][b]= false;
                }
            }
            tabPiece[1][0] = true;
            tabPiece[2][0] = true;
            tabPiece[0][1] = true;
            tabPiece[1][1] = true;

        }

    }

    public Piece(GrilleSimple _grille) {

        grille = _grille;
        tabPiece= new boolean[4][4];
        Random random = new Random();
        int rand = random.nextInt(7) + 1;
        System.out.print(" LE RAND EST " + rand);
        randPiece(rand);
        plusbasY();
        System.out.print(" basX "+basX);
        System.out.print(" basY "+basY);
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
