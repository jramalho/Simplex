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
   
    
    
    public Problema faseI(Problema p){
        Problema art = criarProblemaArt(p); 
        
        
        
        
        return p;
    }
    
    
}