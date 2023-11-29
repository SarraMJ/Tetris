package Modele;

import java.util.Observable;


public class GrilleSimple extends Observable implements Runnable {

    public final int TAILLE = 20;

    private Piece pieceCourante = new Piece(this);

    private Couleur[][] tabGrille;


    public GrilleSimple() { //constructeur

        new OrdonnanceurSimple(this).start(); // pour changer le temps de pause, garder la référence de l'ordonnanceur
        tabGrille= new Couleur[21][21];
        for (int i=0;i<21;i++){
            for(int j=0;j<21;j++){
                tabGrille[i][j]=Couleur.WHITE;
            }
        }
    }

    public void action() {  //applique la fonction action sur la pièce
        pieceCourante.action();


    }

    //si dans la grille à la prochaine position
    public boolean validationPosition(int _nextX, int _nextY) {
        return (_nextY>=0 && _nextY < TAILLE);
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

        pieceCourante.run();
        setChanged(); // setChanged() + notifyObservers() : notification de la vue pour le rafraichissement
        notifyObservers();

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
            System.err.println("Indices hors limites pour setTabGrille : i=" + i + ", j=" + j);
        }
    }

}
