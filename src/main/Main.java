package main;

import modelo.TGrafo;

public class Main {
    public static void main(String[] args) {
        TGrafo g = new TGrafo(5);

        System.out.println("Agregar aristas:");
        System.out.println("1-2: " + g.agregaArista(1, 2));
        System.out.println("1-3: " + g.agregaArista(1, 3));
        System.out.println("2-4: " + g.agregaArista(2, 4));
        System.out.println("4-5: " + g.agregaArista(4, 5));

        System.out.println("\nIntentos invalidos o repetidos:");
        System.out.println("1-2 repetida: " + g.agregaArista(1, 2));
        System.out.println("6-1 fuera de rango: " + g.agregaArista(6, 1));

        System.out.println(g.obtieneListaAdy());

        System.out.println("DFS desde 1:");
        System.out.println(g.busquedaProfundidad(1));

        System.out.println("DFS desde 3:");
        System.out.println(g.busquedaProfundidad(3));

        System.out.println("DFS invalido:");
        System.out.println(g.busquedaProfundidad(8));
    }
}