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
    
    if(!su.verificaFaseI(p)){
        faseI = true;
    }
    else{
        faseI = false;
    }
        System.out.println(faseI);
    p = su.colocarNaFormaPadrao(p);
    System.out.println("");
    System.out.println("O problema na forma Padrão");
    su.exibirProblema(p);
    if(faseI == true){
        p = faseI(p);
        System.out.println("OPAAAAAAAA");
    }
    else{
        su.gerarVariaveis(p);
        su.exibirProblema(p);
        System.out.println("OPA222");
    }
    su.exibirProblema(p);
}
    public Problema faseI(Problema p){
        Problema art = su.criarProblemaArt(p);
        su.exibirProblema(art);
        System.out.println("COMEÇANDO RESOLUCAO DO SIMPLEX ARTIFICIAl");
        faseII(art,true);
        p.setBasicas(art.getBasicas());
        p.setNao_basicas(su.attNaoBasicas(art.getNao_basicas(),art.getCustos()));
        return p;
    }
    
    public Problema faseII(problema p){
        
    }
}