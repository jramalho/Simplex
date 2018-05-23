package simplex;

/**
 *
 * @author juliano
 */
public class Problema {
    /* Matriz de Restrições do problema */
    private float[][] restricoes;
    
    /* Vetor de Custos e Bezinho */
    private float[] custos_funcao, b;
    
    /* Vetor de Sinais das restrições e Indicador da função (Max ou Min). */
    private String[] sinais;
    
    /* Vetores de variáveis Básicas e não Básicas */
    private int[] basicas, nao_basicas;

    public Problema(float[][] restricoes, float[] C, float[] b, String[] sinais) {
        this.restricoes = restricoes;
        this.custos_funcao = C;
        this.b = b;
        this.sinais = sinais;
        this.basicas = null;
        this.nao_basicas = null;
    }
    
    public Problema(float[][] restricoes, float[] C, float[] b, String[] sinais, int[] basicas, int[] nao_basicas) {
        this.restricoes = restricoes;
        this.custos_funcao = C;
        this.b = b;
        this.sinais = sinais;
        this.basicas = basicas;
        this.nao_basicas = nao_basicas;
    }

    public void setRestricoes(float[][] restricoes) {
        this.restricoes = restricoes;
    }

    public void setC(float[] C) {
        this.custos_funcao = C;
    }

    public void setB(float[] b) {
        this.b = b;
    }

    public void setSinais(String[] sinais) {
        this.sinais = sinais;
    }

    public void setBasicas(int[] basicas) {
        this.basicas = basicas;
    }

    public void setNao_basicas(int[] nao_basicas) {
        this.nao_basicas = nao_basicas;
    }
    
    public float[][] getRestricoes() {
        return restricoes;
    }

    public float[] getC() {
        return custos_funcao;
    }

    public float[] getB() {
        return b;
    }

    public String[] getSinais() {
        return sinais;
    }

    public int[] getBasicas() {
        return basicas;
    }

    public int[] getNao_basicas() {
        return nao_basicas;
    }
    
    public void exibirProblema(Problema p) {
        System.out.print("f(x):");
        for (int i = 0; i < p.getC().length; i++) {
            
            if (i != 0) {
                if (p.getC()[i] >= 0) {
                    System.out.print("+");
                }
            }
            System.out.print(p.getC()[i] + "*x" + i);
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
                System.out.print(p.getRestricoes()[i][j] + "*x" + j);
            }
            System.out.print(p.getSinais()[i + 1] + p.getB()[i] + "\n");
        }
        System.out.println("");
    }

}
