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
    }
    else{
        su.gerarVariaveis(p);
    }
    faseII(p,false);
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
    
    public void faseII(Problema p, boolean probArt){
    boolean otima = false;
    boolean infinitas = false;
    boolean infactivel = false;
    boolean stop = false;
    int it = 1;
    float[][] B;
    float [] XB,CNi,lambda;
    B = su.retiraB(p.getRestricoes(),p.getBasicas());
    
    XB = su.calcXB(B,p.getB());
    while (!stop){
        System.out.println("IT " + it);
        System.out.println("Passo 1: Calculo da solucao basica");
        XB = su.calcXB(B,p.getB());
        System.out.println("");
        System.out.println("Matriz B");
        su.exibeMatriz(B);
        
        System.out.print("\n XB =");
        su.exibeVetor(XB);
        su.pontilhado();
        
        System.out.println("Passo 2: Calculo dos custos relativos");
        System.out.println("Passo 2.1. : vetor multiplicador simplex");
        lambda = su.calcLambda(B,p.getB(),p.getCustos(),p.getBasicas());
        System.out.print("λ = ");
        su.exibeVetor(lambda);
        su.pontilhado();
        
        System.out.println("Passo 2.2: custos relativos");
        CNi = su.calcCustos(lambda,p.getRestricoes(),p.getCustos(),p.getNao_basicas());
        System.out.print("Ĉni:");
        su.exibeVetor(CNi);
        su.pontilhado();
        
        System.out.println("Passo 2.3. : Determinação da variável a entrar na base");
        int cnk = su.retCNk(CNi);
        System.out.print("K = ");
        System.out.println(cnk + " Ĉnk = " + CNi[cnk]);
        System.out.println("");
        su.pontilhado();
        
        System.out.println("Passo 3: Teste de otimalidade");
        
        if(su.isOtima(CNi,cnk)){
            otima = true;
            infinitas = false;
            infactivel = false;
            if(probArt){
                if(su.varArtForaDaBase(p.getBasicas(),p.getCustos())){
                    System.out.println("Otimo e sem variaveis artificiais");
                otima = true;
                infinitas = false;
                infactivel = false;
                } else{
                    System.out.println("Otimo mas infáctivel");
                    otima = true;
                    infinitas = false;
                    infactivel = true;
                }
            }
            stop = true;
            break;
        }
        System.out.println("Ĉnk não é maior ou igual a zero");
        su.pontilhado();
        float[] Y = su.calcY(B,su.obtCol(p.getRestricoes(), cnk));
        System.out.println("Passo 4: calculo da direção Simplex");
        System.out.print("Y =");
        su.exibeVetor(Y);
        
        su.pontilhado();
        System.out.println("Passo 5 : determinação do passo e var a sair da base");
        if(su.veriInfinitaSolucoes(Y)){
            otima = false;
            infinitas = true;
            infactivel = false;
            stop = true;
            break;
        }
        
        float E = su.calcE(XB,Y);
        float calcE = su.calcEAux(XB,Y);
        System.out.println("\nE = " + calcE);
        
        B = su.attBase(B,XB,p.getRestricoes(),p.getBasicas(),p.getNao_basicas(),Y,E,cnk);
        System.out.println("Atualizacao de Vars");
        for(int i=0;i<p.getBasicas().length;i++){
            System.out.print("B" + (i+1) + " = " + p.getBasicas()[i] + " ");
        }
        for(int i=0;i<p.getNao_basicas().length;i++){
            System.out.print("N" + (i+1) + " = " + p.getNao_basicas()[i] + " ");
        }
        System.out.println("");
        it++;    
    }
    if(otima){
        su.pontilhado();
        System.out.println("Matriz Solucao");
        su.exibeMatriz(B);
        System.out.println("");
        for(int i=0;i<p.getBasicas().length;i++){
            System.out.print("B" + (i+1) + " = " + p.getBasicas()[i] + " ");
        }
        for(int i=0;i<p.getNao_basicas().length;i++){
            System.out.print("N" + (i+1) + " = " + p.getNao_basicas()[i] + " ");
        }
        if(!probArt){
            float s = 0;
            for(int i=0;i<p.getBasicas().length;i++){
                s += p.getCustos()[p.getBasicas()[i]] * XB[i];
            }
            
            if(p.getSinais()[0] == "max"){
                s = -s;
            }
            su.pontilhado();
            System.out.println("SOLUCAO FINAL");
            System.out.print("XB =");
            su.exibeVetor(XB);
            System.out.println("\n F(x): " + s);
        }
    }
    if (infinitas){
        System.out.println("Problema não possui solucao otima infinita para f(x) → −∞");
    }
    if(infactivel){
        System.out.println("Problema infactivel");
    }
    
    }
}