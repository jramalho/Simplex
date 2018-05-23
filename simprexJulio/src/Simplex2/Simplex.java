/*
 CLasse que implementa os métodos do Simplex
 */
package Simplex2;

/**
 *
 * @author Jonathan
 */
public class Simplex {
Gauss g = new Gauss();
SimplexUtils su = new SimplexUtils();
    public void simplex(Problema p){
    Gauss g = new Gauss();

boolean faseI;

    //Se o vetor B for negativo irá multiplicar a linha e inverter o sinal
    su.verificaB(p);
    su.exibirProblema(p);
    
    if(su.verificaFaseI(p)){
        faseI = true;
    }
    else{
        faseI = false;
    }
    
}
}