package Modele;

import java.util.Observable;


public class GrilleSimple extends Observable implements Runnable {

    public final int TAILLE = 20;

    private Piece pieceCourante = new Piece(this);

    private boolean[][] tabGrille;


    public GrilleSimple() { //constructeur

        new OrdonnanceurSimple(this).start(); // pour changer le temps de pause, garder la référence de l'ordonnanceur
        tabGrille= new boolean[21][21];
        for (int i=0;i<21;i++){
            for(int j=0;j<21;j++){
                tabGrille[i][j]=false;
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
    public boolean[][] getTabGrille(){
        return tabGrille;
    }

    public void setTabGrille(int i,int j,boolean b){
        tabGrille[i][j]=b;
    }

}
