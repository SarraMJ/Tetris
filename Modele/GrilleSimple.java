package Modele;

import java.util.Observable;
import java.util.LinkedList;
import java.util.Queue;

public class GrilleSimple extends Observable implements Runnable {

    public final int TAILLE = 20;

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

    private void genererProchainesPieces(int n) {
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

    //si dans la grille à la prochaine position
    public boolean validationPosition(int _nextX, int _nextY) {
        return (_nextY>=0 && _nextY < TAILLE);
    }

    public boolean validationCollision(int x, int y, boolean[][] tabPiece) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tabPiece[i][j]) {
                    int coordX = x + i;
                    int coordY = y + j;

                    // Vérifier si la case est déjà occupée par une autre pièce dans la grille
                    if (coordX >= 0 && coordX < TAILLE && coordY >= 0 && coordY < TAILLE) {
                        if (tabGrille[coordX][coordY] != Couleur.WHITE) {
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

            setChanged(); // setChanged() + notifyObservers() : notification de la vue pour le rafraichissement
            notifyObservers();
                //on check et enleve les lignes remplies
            checkAndRemoveLines();

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



    public void checkAndRemoveLines() {
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
                removeLineAndShiftDown(i);
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

    public void removeLineAndShiftDown(int ligne) {
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
}
