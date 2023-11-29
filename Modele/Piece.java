package Modele;
import java.awt.*;
import java.util.Random;

public class Piece implements Runnable {

    private int x = 7;
    private int y = 0;

    //coordonnees de la case la plus basse qui est true
    private int basX;
    private int basY;
    private int dY = 1;

    private boolean[][] tabPiece;

    //private Couleur[][] tabPieceCouleur;

    private int codeCouleur = 5;
    private GrilleSimple grille;

    private Orientation orientation = Orientation.ORIGINALE;



    public void randPiece(int rand){
        /*Random random = new Random();
        for (int i=0;i<4;i++){
            for (int j=0;j<4;j++){
                tabPiece[i][j]= random.nextBoolean();
            }
        } */
        if (rand==1){ // forme I-Tetrimino ou baton
            codeCouleur=1;
            int i=0;
            int j=0;

            for(i=0;i<4;i++){
                for(j=0;j<4;j++){
                    tabPiece[i][j]=false;
                }
            }
            for (i=0;i<4;i++){
                j=1;
                tabPiece[i][j]=true;
            }
        }
        if (rand ==2){ //O_tetrimino
            codeCouleur=2;
            for (int a=0; a< 4; a++){
                for(int b= 0; b<4; b++){
                    tabPiece[a][b]= false;
                }
            }
            tabPiece[1][1] = true;
            tabPiece[2][1] = true;
            tabPiece[1][2] = true;
            tabPiece[2][2] = true;
        }
        if (rand==3){ //T-Termino
            codeCouleur=3;
            int j=0;
            int i=0;

            for(i=0;i<4;i++){ //toutes les cases à false
                for(j=0;j<4;j++){
                    tabPiece[i][j]=false;
                }
            }
            for (i=1;i<4;i++){
                j=1;
                tabPiece[i][j]=true;
            }
            tabPiece[2][2]=true;
        }

        if (rand == 4){ // L_tetrimino
            codeCouleur=4;
            for (int a=0; a< 4; a++){
                for(int b= 0; b<4; b++){
                    tabPiece[a][b]= false;
                }
            }
            tabPiece[3][1] = true;
            tabPiece[1][1] = true;
            tabPiece[2][1] = true;
            tabPiece[1][2] = true;

        }

        if(rand==5){ //J-Termino
            codeCouleur=5;
            int j=0;
            int i=0;

            for(i=0;i<4;i++){ //toutes les cases à false
                for(j=0;j<4;j++){
                    tabPiece[i][j]=false;
                }
            }
            tabPiece[0][1]=true;
            tabPiece[1][1]=true;
            tabPiece[2][2]=true;
            tabPiece[2][1]=true;
        }

        if(rand==6) { //Z-Termino
            codeCouleur=6;
            int j = 0;
            int i = 0;

            for (i = 0; i < 4; i++) { //toutes les cases à false
                for (j = 0; j < 4; j++) {
                    tabPiece[i][j] = false;
                }
            }

            tabPiece[1][1] = true;
            tabPiece[2][1] = true;
            tabPiece[2][2] = true;
            tabPiece[3][2] = true;
        }

        if (rand == 7){// S_tetrimino
            codeCouleur=7;
            for (int a=0; a< 4; a++){
                for(int b= 0; b<4; b++){
                    tabPiece[a][b]= false;
                }
            }
            tabPiece[2][1] = true;
            tabPiece[3][1] = true;
            tabPiece[1][2] = true;
            tabPiece[2][2] = true;

        }

    }

    public Piece(GrilleSimple _grille) {

        grille = _grille;
        tabPiece= new boolean[4][4];
        Random random = new Random();
        int rand = random.nextInt(7) + 1;
        randPiece(rand);
        plusbasY();

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

    public void nextOrientation(){


        switch (orientation) {
            case ORIGINALE:
                orientation=Orientation.DROITE;
                break;
            case DROITE:
                orientation=Orientation.ENBAS;
                break;
            case ENBAS:
                orientation=Orientation.GAUCHE;
                break;
            case GAUCHE:
                orientation=Orientation.ORIGINALE;
                break;

        }

    }

    public void action() {
        nextOrientation();
        System.out.print(" "+orientation+" ");
        rotation(orientation);



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

        if (grille.validationPosition(nextBasX, nextBasY) && !grille.validationCollision(nextX,nextY,tabPiece)) {
            y = nextY;
            x = nextX;
            basY=nextBasY;
            basX=nextBasX;
            //System.out.println("pos" + x + " "+ y);
        } else {
            dY *= 0;

            //on stocke les cases true dans le tableau de la grille
            int w=x;
            int z=y;
            for (int i=0;i<4;i++) {
                for (int j = 0; j < 4; j++) {
                    System.out.println(" LE Y "+y);
                    w=x+i;
                    z=y+j;
                    grille.setTabGrille(w,z,tabPiece[i][j]);
                }
            }

            Piece p=new Piece(grille);
            grille.setPieceCourante(p);
        }


    }

    public int getCodeCouleur() {
        return codeCouleur;
    }

    public Couleur getCouleurfromCode(int codeColor){
        switch (codeColor) {
            case 1:
                return Couleur.CYAN;
            case 2:
                return Couleur.YELLOW;
            case 3:
                return Couleur.PURPLE;
            case 4:
                return Couleur.ORANGE;
            case 5:
                return Couleur.BLUE;
            case 6:
                return Couleur.RED;
            case 7:
                return Couleur.GREEN;

        }
        return Couleur.BLACK;

    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public Orientation getOrientation() {
        return orientation;
    }



    public boolean getTabPiece(int i,int j){
        return tabPiece[i][j];
    }

    public void rotation(Orientation nouvelleOrientation) {
        boolean[][] nouveauTabPiece = new boolean[4][4];

        // Effectue la rotation en fonction de la nouvelle orientation
        switch (nouvelleOrientation) {
            case ORIGINALE:
                // Aucune rotation, c'est le tableau d'origine
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        nouveauTabPiece[i][j] = tabPiece[i][j];
                    }
                }
                break;
            case DROITE:
                // Fais la rotation à droite
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        nouveauTabPiece[i][j] = tabPiece[3-j][i];
                    }
                }
                break;
            case ENBAS:
                // Fais la rotation vers le bas
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        nouveauTabPiece[i][j] = tabPiece[3-i][3-j];
                    }
                }
                break;
            case GAUCHE:
                // Fais la rotation à gauche
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        nouveauTabPiece[i][j] = tabPiece[j][3-i];
                    }
                }
                break;
        }

            if(grille.validationTab(x,y,nouveauTabPiece)) {
                tabPiece = nouveauTabPiece;
                orientation = nouvelleOrientation;
                plusbasY();
            }
    }

    public void translation(Direction direction) {
        int nextY = y;
        int nextX = x;

        switch (direction) {

            case DROITE:
                nextX++;
                break;
            case BAS:
                nextY++;
                break;
            case GAUCHE:
                nextX--;
                break;
        }

        // Vérifier si la nouvelle position est valide dans la grille


            if (grille.validationTab(nextX, nextY, tabPiece)) {
                y = nextY;
                x = nextX;
                plusbasY(); // Mettre à jour les coordonnées de la case la plus basse
            }
    }


}
