package Modele;

import java.util.Observable;
import java.util.LinkedList;
import java.util.Queue;

public class GrilleSimple extends Observable implements Runnable {

    public final int TAILLE = 20; //taille de la grille

    private Piece pieceCourante=null;

    private Couleur[][] tabGrille;

    private int score = 0;

    private boolean gameStarted = false;

    private boolean gameOver = false;

    private Queue<Piece> prochainesPieces;

    public GrilleSimple() { //constructeur

        new OrdonnanceurSimple(this).start(); // pour changer le temps de pause, garder la référence de l'ordonnanceur
        tabGrille= new Couleur[21][21];
        for (int i=0;i<21;i++){
            for(int j=0;j<21;j++){
                tabGrille[i][j]=Couleur.WHITE;
            }
        }
        prochainesPieces = new LinkedList<>();
        // Générez les premières pièces dans la liste des prochaines pièces
        genererProchainesPieces(3);
    }

    private void genererProchainesPieces(int n) { //genere 3 pieces a mettre dans notre queue
        for (int i = 0; i < n; i++) {
            prochainesPieces.add(new Piece(this));
        }
    }

    public void startGame() {
        if (!gameStarted) {
            gameStarted = true;
            pieceCourante=getProchainePiece();
            // Ajoutez une nouvelle pièce à la fin de la file d'attente
            Piece nouvellePiece = new Piece(this); // Remplacez ceci par la logique de génération de votre nouvelle pièce
            prochainesPieces.offer(nouvellePiece);

        }
    }

    public Piece getProchainePiece() {
        return prochainesPieces.poll();
    }

    public Queue<Piece> getProchainesPieces() {
        return prochainesPieces;
    }

    public void action() {  //applique la fonction action sur la pièce
        pieceCourante.action();


    }

    //si la piece est dans la grille et verifie seuleemnt par rapport a l'axe des ordonnees
    public boolean validationPosition(int _nextX, int _nextY) {
        return (_nextY>=0 && _nextY < TAILLE);
    }

    public boolean validationCollision(int x, int y, boolean[][] tabPiece) { //verifie s'il y a collision
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tabPiece[i][j]) { //position absolu du tableau de la piece
                    int coordX = x + i;
                    int coordY = y + j;

                    // Vérifier si la case est déjà occupée par une autre pièce dans la grille
                    if (coordX >= 0 && coordX < TAILLE && coordY >= 0 && coordY < TAILLE) {
                        if (tabGrille[coordX][coordY] != Couleur.WHITE) { //si le tableau de grille est vite a cette position
                            if (y<=0) {
                                // La pièce courante a atteint la ligne y = 0, arrêter d'avoir de nouvelles pièces
                                gameStarted = false;
                                gameOver=true;
                            }

                            return true; // Collision détectée

                        }
                    }
                }
            }
        }
        return false; // Aucune collision détectée
    }

    public boolean validationCollision_2(int x, int y, boolean[][] tabPiece) { // sans le gameover
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tabPiece[i][j]) {
                    int coordX = x + i;
                    int coordY = y + j;

                    // Vérifier si la case est déjà occupée par une autre pièce dans la grille
                    if (coordX >= 0 && coordX < TAILLE && coordY >= 0 && coordY < TAILLE) {
                        if (tabGrille[coordX][coordY] != Couleur.WHITE) {

                            return true; // Collision détectée

                        }
                    }
                }
            }
        }
        return false; // Aucune collision détectée
    }

    public boolean validationCollisionCoteDroit(int x, int y, boolean[][] tabPiece) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tabPiece[i][j]) {
                    int coordX = x + i + 1; // Vérifiez le côté droit, donc ajoutez 1 à la coordonnée x
                    int coordY = y + j;

                    // Vérifier si la case est déjà occupée par une autre pièce dans la grille
                    if (coordX >= 0 && coordX < TAILLE && coordY >= 0 && coordY < TAILLE) {
                        if (tabGrille[coordX][coordY] != Couleur.WHITE) {
                            return true; // Collision détectée
                        }
                    }
                }
            }
        }
        return false; // Aucune collision détectée
    }

    public boolean validationCollisionCoteGauche(int x, int y, boolean[][] tabPiece) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tabPiece[i][j]) {
                    int coordX = x + i - 1; // Vérifiez le côté gauche, donc soustrayez 1 à la coordonnée x
                    int coordY = y + j;

                    // Vérifier si la case est déjà occupée par une autre pièce dans la grille
                    if (coordX >= 0 && coordX < TAILLE && coordY >= 0 && coordY < TAILLE) {
                        if (tabGrille[coordX][coordY] != Couleur.WHITE) {
                            return true; // Collision détectée
                        }
                    }
                }
            }
        }
        return false; // Aucune collision détectée
    }


    // par rapport aux x et aux y on verifie si la piece est dans la grille
    public boolean validationTab(int x,int y,boolean[][] tab) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tab[i][j]) {
                    int coordX = x + i;
                    int coordY = y + j;

                    if ( coordX >= TAILLE ||coordY >= TAILLE|| coordX < 0|| coordY < 0 ) {
                        return false;
                    }
                }
            }
        }
        return true;
    }



    public void run() {
        if (gameStarted) { // Vérifier si le jeu a démarré
            pieceCourante.run();

            setChanged(); // notification de la vue pour le rafraichissement
            notifyObservers();

            verifie_et_supprime_lignes_remplies();//on check et enleve les lignes remplies

        }
    }


    public Piece getPieceCourante() {
        return pieceCourante;
    }

    public void setPieceCourante(Piece p){
        pieceCourante=p;
    }
    public Couleur[][] getTabGrille(){
        return tabGrille;
    }

    public void setTabGrille(int i,int j,boolean b){
        if (i >= 0 && i < 21 && j >= 0 && j < 21) {
            if(b && tabGrille[i][j]==Couleur.WHITE) {

                tabGrille[i][j] = pieceCourante.getCouleurfromCode(pieceCourante.getCodeCouleur());
            }

        } else {
            // Gérer l'erreur ou afficher un message approprié si les indices sont hors limites.
            //System.err.println("Indices hors limites pour setTabGrille : i=" + i + ", j=" + j);
        }
    }



    public void verifie_et_supprime_lignes_remplies() { // pour verifier les lignes remplies et les supprimer
        int nbLignesremplies=0;
        for (int i = TAILLE - 1; i >= 0; i--) {
            boolean ligneRemplie = true;
            for (int j = 0; j < TAILLE; j++) {
                if (tabGrille[j][i] == Couleur.WHITE) {
                    ligneRemplie = false;
                    break;
                }
            }

            if (ligneRemplie) {
                nbLignesremplies++;
                enleveligne_et_decale(i);
                setChanged(); // Indiquer que la grille a été modifiée
                notifyObservers(); // Notifier les observateurs pour le rafraîchissement
                i++; // Révérifier la même ligne, car tout a été décalé vers le bas
            }
        }
        if (nbLignesremplies==1){
            score+=40;
        }
        if (nbLignesremplies==2){
            score+=100;
        }
        if(nbLignesremplies==3){
            score+=300;
        }
        if(nbLignesremplies==4){
            score+=1200;
        }
    }

    public void enleveligne_et_decale(int ligne) {
        for (int i = ligne; i > 0; i--) {
            for (int j = 0; j < TAILLE; j++) {
                tabGrille[j][i] = tabGrille[j][i-1];
            }
        }

        // Remettre la première ligne à vide
        for (int j = 0; j < TAILLE; j++) {
            tabGrille[j][0] = Couleur.WHITE;
        }

    }

    public int getScore() {
        return score;
    }

    public boolean getGameStarted() {
        return gameStarted;
    }

    public boolean getGameOver() {
        return gameOver;
    }
    public void setGameOver(boolean b){
        gameOver=b;
    }

    public void resetGame() { //appele quand j'appuie sur start et que le jeu est en mode gamover
        tabGrille = new Couleur[21][21];
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                tabGrille[i][j] = Couleur.WHITE;
            }
        }
        score = 0;
        gameStarted = false;
        gameOver = false;
        genererProchainesPieces(3); // Générez les premières pièces dans la liste des prochaines pièces
    }

}
