/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simplex2;

/**
 *
 * @author Jonathan
 */
public class Main {
    public static void main(String[] args) {
       
        Simplex simp = new Simplex();
       //5.4
       /* 
       float[] C = {-1, -2};
        float[][] A = {{1, 1}, {1, -1}, {-1, 1}};
        float[] b = {6,4,4};
        String[] s = {"min", "<=", "<=", "<="};
        Problema p = new Problema(A, C, b, s); 
        */
       float[] C = {-1,-1};
       float[][] A = {{1,-1}, {-1,1}};
       float[] b = {4,4};
       String[] s = {"min", "<=", "<="};
       Problema p = new Problema(A,C,b,s);
       
    simp.simplex(p);
    
    }
    
}
