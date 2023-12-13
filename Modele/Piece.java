package Modele;
import java.awt.*;
import java.util.Random;

public class Piece implements Runnable {

    private int x = 7;
    private int y = -2;

    //coordonnees de la case la plus basse qui est true
    private int basX;
    private int basY;
    private int dY = 1; //quand à 1 la piece descend, si à 0 la piece se fige

    private boolean[][] tabPiece;

    private boolean paused = false;

    //private Couleur[][] tabPieceCouleur;

    private int codeCouleur = 5;
    private GrilleSimple grille;


    public void randPiece(int rand){ //donne la forme de la piece selon un int en parametre

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
        randPiece(2);
        plusbasY();

    }


    public void togglePause() {
        paused = !paused;
    } //change l'etat du jeu :pause et play




    public void action() { //appelle la rotation de la piece si les conditions de collisions sont verifiees
        if(!paused) {

            // Vérifie si dans la grille
            if (grille.validationTab(x, y, tabPiece)) {
                // Vérifie la collision avec d'autres pièces
                if (grille.validationCollision(x, y, tabPiece)) {
                    // Collision=rotation, on arrête la pièce et d'autres actions nécessaires
                    // Génère une nouvelle pièce pour la mettre à la fin de la queue
                    Piece p = grille.getProchainePiece();
                    Piece nouvellePiece = new Piece(grille);
                    grille.getProchainesPieces().offer(nouvellePiece);
                    grille.setPieceCourante(p);
                } else {
                    // Aucune collision avec d'autres pièces, on fait la rotation
                    rotation();
                }
            }
        }
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
        if(!paused ) {
            int nextY = y;
            int nextX = x;

            int nextBasY = basY;
            int nextBasX = basX;

            nextY += dY;
            nextBasY += dY;

            //pour le moment on rajoute pas de x pour la bouger
            //apres on rajoutera dX
            //quand il n'y a pas de collisions, on fait descendre la piece
            if (grille.validationPosition(nextBasX, nextBasY) && !grille.validationCollision(nextX, nextY, tabPiece)) {
                y = nextY;
                x = nextX;
                basY = nextBasY;
                basX = nextBasX;

            } else { //quand il y a collision
                dY *= 0;

                //on stocke les cases true dans le tableau de la grille
                int w = x;
                int z = y;
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        //System.out.println(" LE Y "+y);
                        w = x + i; //positions absolues des cases du tableau
                        z = y + j;
                        grille.setTabGrille(w, z, tabPiece[i][j]); //on stocke la couleur de la piece dans la grille
                    }
                }
                Piece p = grille.getProchainePiece();
                Piece nouvellePiece = new Piece(grille);
                grille.getProchainesPieces().offer(nouvellePiece);
                grille.setPieceCourante(p);

            }
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

    public boolean getPaused() {
        return paused;
    }



    public boolean getTabPiece(int i,int j){
        return tabPiece[i][j];
    }

    public boolean[][] getTabPiece() {
        return tabPiece;
    }

    public void rotation() {
        boolean[][] nouveauTabPiece = new boolean[4][4];



        // Fais la rotation à droite
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                nouveauTabPiece[i][j] = tabPiece[3-j][i];
            }
        }

//si pas de collision on effectue la rotation
        if(grille.validationTab(x,y,nouveauTabPiece) && !grille.validationCollision(x, y, nouveauTabPiece)) {
            tabPiece = nouveauTabPiece;
            plusbasY();
        }
    }



    public void translation(Direction direction) {
        if (!paused) {
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
                if (grille.validationCollision(nextX, nextY, tabPiece)) { //s'il y a collision
                    // Collision! arrêter la pièce
                    // mettre à jour la pièce courante
                    // Générer une nouvelle pièce
                    dY=0;
                    Piece p = grille.getProchainePiece();
                    Piece nouvellePiece = new Piece(grille);
                    grille.getProchainesPieces().offer(nouvellePiece);
                    grille.setPieceCourante(p);
                } else {
                    y = nextY;
                    x = nextX;
                    plusbasY(); // Mettre à jour les coordonnées de la case la plus basse
                }
            }
        }
    }



}
