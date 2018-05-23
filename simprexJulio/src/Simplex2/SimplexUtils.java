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
public class SimplexUtils {
public SimplexUtils(){}

    public void verificaB(Problema p){
        for (int i=0;i<p.getB().length;i++){
            if(p.getB()[i] < 0){
                p.setBNegativo(i);
                for (int j=0;j<p.getRestricoes()[i].length;j++){
                    p.getRestricoes()[i][j] *= -1;
                }
                switch (p.getSinais()[i+1]){
                    case "<":
                        p.setSinal(i+1,">");
                        break;
                    case "<=":
                        p.setSinal(i+1, ">=");
                        break;
                    case ">":
                        p.setSinal(i+1, "<");
                        break;
                    case ">=":
                        p.setSinal(i+1, "<=");
                        break;
                }
            }
        }
    }

  public boolean verificaFaseI(Problema p){
      for (int i=1;i<p.getSinais().length;i++){
          if(p.getSinais()[i].equals(">=") || (p.getSinais()[i].equals(">"))){
              System.out.println("Necessitará de Fase I");   
          return false;
          }
      }
      System.out.println("Nao necessitará de Fase I");
      return true;
  }
  
    public void exibirProblema(Problema p) {
        System.out.println("-----------------------------------------------");
        System.out.print("f(x):");
        for (int i = 0; i < p.getCustos().length; i++) {
            
            if (i != 0) {
                if (p.getCustos()[i] >= 0) {
                    System.out.print("+");
                }
            }
            System.out.print(p.getCustos()[i] + "*x" + (i+1));
        }
        System.out.println("");
        for (int i = 0; i < p.getRestricoes().length; i++) {
            System.out.print("s.a:\t");
            for (int j = 0; j < p.getRestricoes()[0].length; j++) {
                
                if (j != 0) {
                    if (p.getRestricoes()[i][j] >= 0) {
                        System.out.print("+");
                    }
                }
                System.out.print(p.getRestricoes()[i][j] + "*x" + (j+1));
            }
            System.out.print(p.getSinais()[i + 1] + p.getB()[i] + "\n");
        }
        System.out.println("-----------------------------------------------");
    }
/*
public void padronizarProblema(Problema p) {
        p.setRestricoes(padronizarRestricoes(p));
        p.setC(padronizarCustos(p));
        System.out.println("Forma Normal");
        exibirProblema(p);
    }
*/
    public float[][] padronizarRestricoes(Problema p) {
        int contVariaveis = 0;

        for (String sinais : p.getSinais()) {
            if (!sinais.equals("=")) {
                contVariaveis++;
            }
        }

        float restricoes_novas[][] = new float[p.getRestricoes().length][p.getRestricoes()[0].length + contVariaveis - 1];
        for (int i = 0; i < restricoes_novas.length; i++) {
            for (int j = 0; j < restricoes_novas[0].length; j++) {
                if (j < p.getRestricoes()[0].length) {
                    restricoes_novas[i][j] = p.getRestricoes()[i][j];
                } else {
                    restricoes_novas[i][j] = 0;
                }
            }
        }

        int t = p.getRestricoes()[0].length;
        for (int i = 0; i < restricoes_novas.length; i++) {
            if (p.getSinais()[i + 1].equals("<") || p.getSinais()[i + 1].equals("<=")) {
                restricoes_novas[i][t] = 1;
                p.getSinais()[i + 1] = "=";
                t++;
            } else if (p.getSinais()[i + 1].equals(">") || p.getSinais()[i + 1].equals(">=")) {
                restricoes_novas[i][t] = -1;
                p.getSinais()[i + 1] = "=";
                t++;
            }
        }
        return restricoes_novas;
    }
}