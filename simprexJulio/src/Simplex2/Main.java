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
        Simplex s = new Simplex();
        float[] C2 = {-1, -2};
        float[][] A2 = {
            {1, 1},
            {1, -1},
            {-1, 1}
        };
        float[] b2 = {6, 4, 4};
        String[] s2 = {"min", ">=", "<=", "<="};
        Problema p3 = new Problema(A2, C2, b2, s2); 
        
    s.simplex(p3);
    
    }
    
}
