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
public class Problema {
 
    float[][] restricoes;
    float[] custos,b;
    String[] sinais;
    int[] basicas, nao_basicas;
    
    public Problema(float[][] restricoes, float[] custos, float[] b, String [] sinais){
        this.restricoes = restricoes;
        this.custos = custos;
        this.b = b;
        this.sinais = sinais;
        this.basicas = null;
        this.nao_basicas = null;
    }

    public float[][] getRestricoes() {
        return restricoes;
    }

    public void setRestricoes(float[][] restricoes) {
        this.restricoes = restricoes;
    }

    public float[] getCustos() {
        return custos;
    }

    public void setCustos(float[] custos) {
        this.custos = custos;
    }

    public float[] getB() {
        return b;
    }

    public void setB(float[] b) {
        this.b = b;
    }

    public String[] getSinais() {
        return sinais;
    }

    public void setSinais(String[] sinais) {
        this.sinais = sinais;
    }

    public int[] getBasicas() {
        return basicas;
    }

    public void setBasicas(int[] basicas) {
        this.basicas = basicas;
    }

    public int[] getNao_basicas() {
        return nao_basicas;
    }
    public void setSinal(int i,String sinal){
        sinais[i] = sinal;
    }
    
    public void setNao_basicas(int[] nao_basicas) {
        this.nao_basicas = nao_basicas;
    }
    
    public void setBNegativo(int i){
        b[i] = b[i]*-1;
    }
    
}
