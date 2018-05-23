/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

/**
 *
 * @author Juliano
 */
public class Gauss {

    public static float[] gauss(float[][] matrizOriginal, float bOriginal[]) {
        
        float[][] matriz = new float[bOriginal.length][bOriginal.length];
        float[] b = new float[bOriginal.length];
        //resolução de um bug, copiar matriz para não alterar original
        for (int i = 0; i < matrizOriginal.length; i++) {
            for (int j = 0; j < matrizOriginal.length; j++ ) {
                matriz[i][j] = matrizOriginal[i][j];
            }
            b[i] = bOriginal[i];
        }

        if (matriz.length != b.length) {
            System.out.println("Não é possível resolver esse sistema porque não é um sistema quadrado!");
            return null;
        }

        float mult = 0, temp = 0;
        float[] vetorResposta = new float[b.length];
        /* Método de gauss */
        for (int k = 0; k < matriz.length; k++) {
            /* Pivoteamento parcial */
            float maxEl = Math.abs(matriz[k][k]);
            int linhaDoMax = k;

            // Procura o maximo em modulo na coluna //
            for (int i = k + 1; i < matriz.length; i++) {
                if (Math.abs(matriz[i][k]) > maxEl) {
                    maxEl = Math.abs(matriz[i][k]);
                    linhaDoMax = i;
                }
            }

            // Se o maximo for zero nao tem como calcular a resposta
            if (maxEl == 0) {
                return null;
            }

            // Troca linha atual com a linha de maior pivo //
            for (int i = 0; i < matriz.length; i++) {
                float temporaria = matriz[linhaDoMax][i];
                matriz[linhaDoMax][i] = matriz[k][i];
                matriz[k][i] = temporaria;
            }

            // Trocar também no vetor resposta
            float temporaria = b[linhaDoMax];
            b[linhaDoMax] = b[k];
            b[k] = temporaria;

            /* Triangularizacao */
            for (int i = k + 1; i < matriz.length; i++) {
                mult = matriz[i][k] / matriz[k][k];
                matriz[i][k] = 0;
                b[i] -= mult * b[k];
                for (int j = k + 1; j <= matriz.length - 1; j++) {
                    matriz[i][j] -= mult * matriz[k][j];
                }
            }
        }

        /* Substituição de cima para baixo */
        vetorResposta[b.length - 1] = b[b.length - 1] / matriz[matriz.length - 1][matriz.length - 1];
        for (int i = (matriz.length - 2); i >= 0; i--) {
            temp = b[i];
            for (int j = (i + 1); j < matriz.length; j++) {
                temp -= (matriz[i][j] * vetorResposta[j]);
            }
            vetorResposta[i] = temp / matriz[i][i];
        }

        return vetorResposta;
    }

    /**
     * @param args the command line arguments
     */

}
