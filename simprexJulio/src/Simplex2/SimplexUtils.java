/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simplex2;

import java.util.ArrayList;

/**
 *
 * @author Jonathan
 */
public class SimplexUtils {
    Gauss g = new Gauss();
public SimplexUtils(){}


public float[][] calcTrans(float[][] A){
    int m = A.length;
    int n = A[0].length;
    float[][] trans = new float[m][n];
    
    
    for (int i =0;i<m;i++){
        for(int j=0;j<n;j++){
            trans[i][j] = A[i][j];
        }
    }
    return trans;
}
public void pontilhado(){
    System.out.println("-----------------------------------------------");
}
public float[][] copiaMatriz(float[][] A){
    int m = A.length;
    int n = A[0].length;
    float[][] cp = new float[m][n];
    
    for(int i=0;i<m;i++){
        for(int j=0;j<n;j++){
        cp[i][j] = A[i][j];
        }
    }
    return cp;
}
public void exibeMatriz(float[][] A){
    for(int i=0;i<A.length;i++){
        for(int j=0;j<A.length;j++){
            System.out.print(A[i][j] + "\t");
        }
        System.out.println("");
    }
}

public void exibeVetor(float[] v){
    System.out.print("( ");
    for (int i =0;i<v.length;i++){
        System.out.print(v[i] + " ");
    }
    System.out.println(")");
}
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
      boolean aux = false;
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
        System.out.print("f(x):\t");
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
   
    public Problema colocarNaFormaPadrao(Problema p){
    p.setRestricoes(padronizarRestricoes(p));
    p.setCustos(padronizarCustos(p));
    return p;    
    }
    
    public int[] attNaoBasicas(int[] nao_basicas, float[] custos) {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < nao_basicas.length; i++) {
            if (custos[nao_basicas[i]] == 0.0) {
                temp.add(nao_basicas[i]);
            }
        }
        int[] nb = new int[temp.size()];
        for (int i = 0; i < nb.length; i++) {
            nb[i] = temp.get(i);
        }
        return nb;
    }  
    
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
    public float[] padronizarCustos(Problema p) {
        float[] custos_novos = new float[p.getRestricoes()[0].length];
        for (int i = 0; i < custos_novos.length; i++) {
            if (i < p.getCustos().length) {
                if (p.getSinais()[0].equals("max")) {
                    custos_novos[i] = p.getCustos()[i] * -1;
                } else {
                    custos_novos[i] = p.getCustos()[i];
                }
            } else {
                custos_novos[i] = 0;
            }
        }
        return custos_novos;
    }
    public Problema gerarVariaveis(Problema p){
        int cont = p.getRestricoes().length;
        int custosL = p.getCustos().length;
        int[] basicas = new int[cont];
        int[] nao_basicas = new int[p.getCustos().length - basicas.length];
        
        for (int i = p.getCustos().length -1; i > 0;i--){
            if (cont > 0){
                basicas[cont-1] = i;
                cont--;
            }
        }
        
        boolean contains = false;
        for(int i=0;i<custosL - basicas.length;i++){
            for(int j=0;j<basicas.length;j++){
                if(basicas[j] == i){
                    contains = true;
                }
            }
            if(!contains){
                nao_basicas[i]=i;
            }
            contains = false;
        }
        p.setBasicas(basicas);
        p.setNao_basicas(nao_basicas);
        return p;
    }
    public float[][] gerarRestricoesArt(Problema p){
    float novas_restricoes[][] = new float[p.getRestricoes().length][p.getRestricoes()[0].length + p.getRestricoes().length];    
    for(int i=0;i<novas_restricoes.length;i++){
        for(int j=0;j<novas_restricoes[0].length;j++){
            if (j < p.getRestricoes()[0].length){
                novas_restricoes[i][j] = p.getRestricoes()[i][j];
            }
            else{
                novas_restricoes[i][j] = 0;
            }
        }
    }
    int t = p.getRestricoes()[0].length;
    for (int i=0;i<novas_restricoes.length;i++){
        novas_restricoes[i][t] = 1;
        t++;
    }
    return novas_restricoes;
    }
    public float[] gerarCustosArt(Problema p){
        float[] newCustos = new float[p.getCustos().length + p.getRestricoes().length];
        for (int i=0;i<newCustos.length;i++){
            if (i < p.getCustos().length){
                newCustos[i] = 0;
            }
            else{
                newCustos[i] = 1;
            }
        }
        return newCustos;
    }
    
    public Problema criarProblemaArt(Problema p){
        Problema art = new Problema(p.getRestricoes(),p.getCustos(),p.getB(),p.getSinais());
        art.setRestricoes(gerarRestricoesArt(p));
        art.setCustos(gerarCustosArt(p));
        gerarVariaveis(art);
        return art;
    }
   
    public float[][] retiraB(float[][] restricoes, int[] basicas){
        float[][] B = new float[basicas.length][basicas.length];
        for (int i=0;i<basicas.length;i++){
            for (int j=0;j<basicas.length;j++){
                B[i][j] = restricoes[i][basicas[j]];
            }
            System.out.println("");
        }
        return B;
    }
    
   public float[] calcXB(float B[][], float[] b){
       return g.gauss(B,b);
   }
   
   public float[] calcCB(float[] custos, float[] b, int[] basicas){
       float[] CB = new float[b.length];
       for (int i=0;i<b.length;i++){
           CB[i] = custos[basicas[i]];
       }
       return CB;
   }
   
   public float[] calcLambda(float[][] B, float[] b, float[] custos, int[] basicas){
       float[] CB = calcCB(custos,b,basicas);
       float[][] mTrans = calcTrans(B);
       return g.gauss(mTrans,CB);
   }
   
   public float multVet(float[] a, float[] b){
       float sum = 0;
       for (int i=0;i<a.length;i++){
           sum += a[i]*b[i];
       }
       return sum;
   }
   public float[] obtCol(float[][] A, int index){
       float ani[] = new float[A.length];
       for (int i =0;i<A.length;i++){
           ani[i] = A[i][index];
       }
       return ani;
   }
   
   public float[] calcCustos(float[] lambda, float[][] restricoes, float[] custos, int[] nao_basicas){
       float[] CNi = new float[nao_basicas.length];
       for (int i=0;i<CNi.length;i++){
           CNi[i] = custos[nao_basicas[i]] - multVet(lambda,obtCol(restricoes,nao_basicas[i]));
       }
       return CNi;
   }
   public int retCNk(float[] CNi){
       int cnk = 0;
       float menorValor = CNi[0];
       for (int i=0;i<CNi.length;i++){
           if(CNi[i] < menorValor){
           menorValor = CNi[i];
           cnk = i;
       }
       }
       return cnk;
   }
   
   public boolean isOtima(float[] CNi, int cnk){
       return CNi[cnk] >= 0.0;
   }
   
   public boolean varArtForaDaBase(int[] basicas, float[] custos){
       for (int i=0;i<basicas.length;i++){
           if(custos[basicas[i]] != 0.0){
               return false;
           }
       }
   return true;
   }
   public float[] calcY(float[][] B, float[] vet){
       return g.gauss(B,vet);
   }
   public boolean veriInfinitaSolucoes(float[] Y){
       int m= Y.length;
       for (int i=0;i<m;i++){
           if(Y[i] > 0){
           return false;
           }
       }
       return true;
   }
   public float calcE(float[] xb, float[] y){
       double result[] = new double[y.length];
       System.out.print("E = min{");
       for(int i=0;i<y.length;i++){
           if (y[i]>0){
               System.out.print("("+ xb[i] + "/" + y[i] + ")");
               result[i] = xb[i]/y[i];
           } else{
               result[i] = -1.0;
           }
       }
       System.out.print("}");
       int k = 0;
       double minimo = 0;
       for (int i =0;i<y.length;i++){
           if(result[i] >= 0.0){
               minimo = result[i];
               k=i;
               break;
           }
       }
       for (int i=0;i<y.length;i++){
           if(result[i]<0){
               continue;
           }
           if(result[i] < minimo){
               k=i;
               minimo = result[i];
           }
       }
       return k;
   }
   public float[][] attBase(float[][] B, float[] XB, float[][] restricoes, int[] basicas, int[] nao_basicas, float[] Y, float E, int cnk){
       int aux = basicas[Math.round(E)];
       basicas[Math.round(E)] = nao_basicas[cnk];
       nao_basicas[cnk] = aux;
       return retiraB(restricoes,basicas);
   }
   
}