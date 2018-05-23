package simplex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author juliano
 */
public class Simplex {

    //Utilidades u = new Utilidades();
    Gauss g = new Gauss();

    public void simplex(Problema p) {
        
        //Variável booleana para indicar se precisa de fase I ou não
        boolean faseI;
        
        //Verifica se o vetor b é negativo. E multiplica por -1 caso necessario.
        verificarB(p);

        //Verifica se precisa de fase I
        System.out.println("-----------------------Problema Original------------------------");
        exibirProblema(p);

        if (!verificaFaseI(p)) {
            faseI = true;
        } else {
            faseI = false;
        }

        //Coloca o problema na Forma Padrão
        padronizarProblema(p);


        /* Executa a Fase I para o Problema se necessário */
        if (faseI) {
            p = faseI(p);
        } //normalizar problema

        // Gera os vetores das variáveis básicas e não basicas para o Problema 
        // Caso tenha fase I, passa da fase I para a II
        if (!faseI) {
            gerarBeNB(p);
        }

        /* Executa a Fase II do Simples para o Problema */
        faseII(p, false); //executar passos simplex para o problema original
    }

    /**
     * Fase I do Algoritmo Simplex, usado para casos onde o problema não tem
     * solução factível inicial.
     *
     * @param p objeto Problema do qual será gerado uma solução inicial
     * factível.
     * @return retorna objeto Problema com
     * 
     * 
     * 
     * o vetor de Básicas e Não Básicas já
     * iniciados com uma solução básica factível.
     */
    public Problema faseI(Problema p) {
        Problema artificial = criarProblemaArtificial(p);
        System.out.println("--------------------Problema Artificial:------------------------");
        exibirProblema(artificial);
        System.out.println("Simplex artificial");
        faseII(artificial, true);
        p.setBasicas(artificial.getBasicas());
        p.setNao_basicas(atualizarNaoBasicas(artificial.getNao_basicas(), artificial.getC()));
        return p;
    }

    /**
     * Fase II do Algoritmo Simplex, executado após a Fase I, ou quando o
     * problema já possui uma solução básica factível.
     *
     * @param p objeto Problema a ser resolvido.
     * @param problemaArtificial booleano que representa se o problema sendo
     * resolvido é artificial por causa da Fase I ou não.
     */
    public void faseII(Problema p, boolean problemaArtificial) {
        boolean otima = false;
        boolean infinitas = false;
        boolean infactivel = false;
        int iteração = 1;

        float B[][];
        float[] XB, CNi, lambda;

        boolean parar = false;

        //Gera B a partir das variáveis básicas
        B = construirB(p.getRestricoes(), p.getBasicas());

        //Calcular B * XB = b
        XB = calcularXB(B, p.getB());

        while (!parar) {
            System.out.println("Iteração " + iteração);
            System.out.println("\nPasso 1: {cálculo da soluçãao básica}");
            XB = calcularXB(B, p.getB());
            System.out.println("");
            System.out.println("Matriz B");
            exibirMatriz(B);

            System.out.print("\nXB=(");
            exibirVetor(XB);
            System.out.print(")\n");

            //Calcular Bt * lambda = CB
            System.out.println("\nPasso 2: {cálculo dos custos relativos}");
            System.out.println("\nPasso 2.1: {vetor multiplicador simplex}");
            lambda = calcularLambda(B, p.getB(), p.getC(), p.getBasicas());
            System.out.print("Lambda=(");
            exibirVetor(lambda);
            System.out.print(")\n");

            System.out.println("\nPasso 2.2: {custos relativos}");
            CNi = calcularCustos(lambda, p.getRestricoes(), p.getC(), p.getNao_basicas());
            System.out.print("Ĉni:(");
            exibirVetor(CNi);
            System.out.print(")\n");

            //Obter a variável que tem custo de menor valor
            
             System.out.println("\nPasso 2.3: {determinação da variável a entrar na base}");
            int cnk = obterCNk(CNi);
            System.out.print("K = ");
            System.out.println(cnk + " Ĉnk = " + CNi[cnk]);
            System.out.println("");
            //Verifica se é solução ótima
            System.out.println("Passo 3: {teste de otimalidade}");
            if (ehSolucaoOtima(CNi, cnk)) {
                otima = true;
                infinitas = false;
                infactivel = false;
                if (problemaArtificial) {
                    if (varArtificaisForaDaBase(p.getBasicas(), p.getC())) {
                        System.out.println("Otimo, sem variáveis artificiais.");
                        otima = true;
                        infinitas = false;
                        infactivel = false;
                    } else {
                        System.out.println("Otimo porém infactível.");
                        otima = true;
                        infinitas = false;
                        infactivel = true;
                    }
                }
                parar = true;
                break;
            }

//            if (problemaArtificial) {
//                if (varArtificaisForaDaBase(p.getBasicas(), p.getC())) {
//                    System.out.println("Nao ótimo, mas, sem variáveis artificiais.");
//                    otima = false;
//                    infinitas = false;
//                    infactivel = false;
//                    parar = true;
//                    break;
//                }
//            }

            System.out.println("Ĉnk >= 0? Não");

            float[] Y = calcularY(B, obterColuna(p.getRestricoes(), cnk));
            System.out.println("\nPasso 4: {cálculo da direção simplex}");
            System.out.print("Y=(");
            exibirVetor(Y);
            System.out.print(")\n");
            
            System.out.println("\nPasso 5: {determinação do passo e variável a sair da base}");
            if (saoInfinitasSolucoes(Y)) {
                otima = false;
                infinitas = true;
                infactivel = false;
                parar = true;
                break;
            }

            //Calculo do E
            float E = calcularE(XB, Y);
            System.out.println("\nE = " + E);

            //Atualizar a base
            
            B = atualizarBase(B, XB, p.getRestricoes(), p.getBasicas(), p.getNao_basicas(), Y, E, cnk);
            System.out.println("Atualização das variáveis");
            for (int i = 0; i < p.getBasicas().length; i++) {
                System.out.print("B" + i + " = " + p.getBasicas()[i] + " ");
            }

            for (int i = 0; i < p.getNao_basicas().length; i++) {
                System.out.print("N" + i + " = " + p.getNao_basicas()[i] + " ");
            }
            System.out.println("\n");
            iteração++;
        }

        if (otima) {
            

            System.out.println("Matriz solução");
            exibirMatriz(B);
            System.out.println("");
            for (int i = 0; i < p.getBasicas().length; i++) {
                System.out.print("B" + i + " = " + p.getBasicas()[i] + " ");
            }

            for (int i = 0; i < p.getNao_basicas().length; i++) {
                System.out.print("N" + i + " = " + p.getNao_basicas()[i] + " ");
            }
            if (!problemaArtificial) {
                float s = 0;
                for (int it = 0; it < p.getBasicas().length; it++) {
                    s += p.getC()[p.getBasicas()[it]] * XB[it];
                }

                if (p.getSinais()[0] == "max") {
                    s = -s;
                }
                System.out.print("\n\nSOLUÇÃO FINAL");
                System.out.print("\nXB=(");
                exibirVetor(XB);
                System.out.print(")");
                System.out.println("\nF(x): " + s);
            }

        }
        if (infinitas) {
            System.out.println("problema não tem soluçãao ótima finita f(x) → −∞");
        }
        if (infactivel) {
            System.out.println("Problema Infactível.");
        }
    }

    /**
     * Verifica se o vetor B do problema possui valores negativos e os corrige.
     *
     * @param p objeto Problema a ser verificado.
     */
    public void verificarB(Problema p) {
        for (int i = 0; i < p.getB().length; i++) {
            if (p.getB()[i] < 0) {
                p.getB()[i] *= -1;
                for (int j = 0; j < p.getRestricoes()[i].length; j++) {
                    p.getRestricoes()[i][j] *= -1;
                }
                switch (p.getSinais()[i + 1]) {
                    case "<":
                        p.getSinais()[i + 1] = ">";
                        break;
                    case "<=":
                        p.getSinais()[i + 1] = ">=";
                        break;
                    case ">":
                        p.getSinais()[i + 1] = "<";
                        break;
                    case ">=":
                        p.getSinais()[i + 1] = "<=";
                        break;
                }
            }
        }
    }

    /**
     * Verifica se o Problema passado está na Forma Padrão para resolução.
     *
     * @param p objeto Problema a ser verificado
     * @return true se o problema está em Forma Padrão, caso contrário false.
     */
    public boolean verificaFaseI(Problema p) {

        for (int i = 1; i < p.getSinais().length; i++) {
            if (!p.getSinais()[i].equals("<=")) {
                System.out.println("\nPrecisa FASE I\n");
                return false;
            }
        }
        System.out.println("Não precisa FASE I\n");
        return true;
    }

    /**
     * Coloca um problema em Forma Padrão.
     *
     * @param p objeto Problema a ser padronizado.
     */
    public void padronizarProblema(Problema p) {
        p.setRestricoes(padronizarRestricoes(p));
        p.setC(padronizarCustos(p));
        System.out.println("Forma Normal");
        exibirProblema(p);
    }

    /**
     * Padroniza as restrições de um Problema, adicionando as variáveis de folga
     * ou excesso.
     *
     * @param p objeto Problema a ser padronizado.
     * @return matriz de Restrições padronizadas.
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

    /**
     * Padroniza o vetor de Custos de um Problema.
     *
     * @param p ojeto Problema a ser padronizado.
     * @return vetor de Custos padronizado.
     */
    public float[] padronizarCustos(Problema p) {
        float[] custos_novos = new float[p.getRestricoes()[0].length];
        for (int i = 0; i < custos_novos.length; i++) {
            if (i < p.getC().length) {
                if (p.getSinais()[0].equals("max")) {
                    custos_novos[i] = p.getC()[i] * -1;
                } else {
                    custos_novos[i] = p.getC()[i];
                }
            } else {
                custos_novos[i] = 0;
            }
        }
        return custos_novos;
    }

    /**
     * Cria um problema artificial com base em um Poblema já existente.
     *
     * @param p objeto Problema base para o Artificial.
     * @return objeto Problema artificial.
     */
    public Problema criarProblemaArtificial(Problema p) {
        Problema art = new Problema(p.getRestricoes(), p.getC(), p.getB(), p.getSinais());
        art.setRestricoes(gerarRestricoesArtificiais(p));
        art.setC(gerarFuncaoArtificial(p));
        gerarBeNB(art);
        return art;
    }

    /**
     * Gera o vetor de Custos artificial.
     *
     * @param p objeto Problema base para gerar o Artificial.
     * @return vetor de Custos Artificiais.
     */
    public float[] gerarFuncaoArtificial(Problema p) {
        //Cria vetor para nova função objetivo do tamanho original
        //somado a quantidade de restrições.
        float[] custos_novos = new float[p.getC().length + p.getRestricoes().length];
        for (int i = 0; i < custos_novos.length; i++) {
            if (i < p.getC().length) {
                custos_novos[i] = 0;    //variaveis NÃO artificiais recebem 0.
            } else {
                custos_novos[i] = 1;    //variáveis artificiais recebem 1.
            }
        }
        return custos_novos;
    }

    /**
     * Gera a matriz de Restrições Artificiais.
     *
     * @param p objeto Problema base para gerar o Artificial.
     * @return matriz de Restrições artificiais.
     */
    public float[][] gerarRestricoesArtificiais(Problema p) {
        //Cria um vetor para as variáveis artificiais com tamanho das retrições
        //somado a quantidade de variáveis novas.
        float restricoes_novas[][] = new float[p.getRestricoes().length][p.getRestricoes()[0].length + p.getRestricoes().length];

        for (int i = 0; i < restricoes_novas.length; i++) {
            for (int j = 0; j < restricoes_novas[0].length; j++) {
                if (j < p.getRestricoes()[0].length) {
                    //Preenche a matriz original com os valores do problema
                    restricoes_novas[i][j] = p.getRestricoes()[i][j];
                } else {
                    //Preenche a parte das novas variáveis com 0.
                    restricoes_novas[i][j] = 0;
                }
            }
        }

        int t = p.getRestricoes()[0].length;
        for (int i = 0; i < restricoes_novas.length; i++) {
            restricoes_novas[i][t] = 1; //Percorre a diagonal das variáveis
            t++;                        //novas e atribui 1;
        }

        return restricoes_novas;
    }

    /**
     * Gera os vetores das variáveis Básicas e Não Básicas para um Problema.
     *
     * @param p objeto Problema base.
     */
    public void gerarBeNB(Problema p) {

        int basicas[] = new int[p.getRestricoes().length];

        //Não basicas são o tamanho dos custsos - o tamanho das básicas
        int nao_basicas[] = new int[p.getC().length - basicas.length];

        int cont = p.getRestricoes().length;

        for (int i = p.getC().length - 1; i > 0; i--) {
            if (cont > 0) {          //Corre pela função objetivo
                basicas[cont - 1] = i;//e adiciona nas variáveis básicas as
                cont--;             //variáveis de folga para formar a base.
            }
        }

        boolean contains = false;
        for (int i = 0; i < p.getC().length - basicas.length; i++) {
            for (int j = 0; j < basicas.length; j++) {
                if (basicas[j] == i) {
                    contains = true;
                }
            }
            if (!contains) {
                nao_basicas[i] = i; //Atribui as não básicas todas as outras
            }
            contains = false;       //que não foram para a base.
        }
        p.setBasicas(basicas);
        p.setNao_basicas(nao_basicas);
    }

    /**
     * Constroi uma Matriz de dados com base nas Restrições e variáveis Básicas
     * de um Problema.
     *
     * @param restricoes matriz de dados com as Restrições do Problema.
     * @param basicas vetor de variáveis Básicas do Problema.
     * @return matriz de dados B.
     */
    public float[][] construirB(float[][] restricoes, int[] basicas) {
        float[][] B = new float[basicas.length][basicas.length];
        for (int i = 0; i < basicas.length; i++) {
            for (int j = 0; j < basicas.length; j++) {  //Constroi a matriz B
                B[i][j] = restricoes[i][basicas[j]];    //utilizando as variáveis
                //System.out.print(B[i][j] + " ");          //básicas.
            }
            System.out.println("");
        }
        return B;
    }

    public float[] calcularXB(float B[][], float[] b) {
        return g.gauss(B, b);   //Aplica eliminação de Gauss para calcular os 
        //valores de XB.
    }

    /**
     * Gera o vetor de Custos das variáveis que estão na Base no Problema.
     *
     * @param custos vetor de Custos do Problema.
     * @param b vetor Bezinho.
     * @param basicas vetor de variáveis que estão na Base.
     * @return vetor com os Custos das variáveis básicas.
     */
    public float[] calcularCB(float[] custos, float[] b, int[] basicas) {
        float[] CB = new float[b.length];
        for (int i = 0; i < b.length; i++) {    //Busca e armazena os custos das
            CB[i] = custos[basicas[i]]; //variáveis não básicas.
        }
        return CB;
    }

    /**
     * Calcula o vetor Lambda para o Problema.
     *
     * @param B matriz B de dados.
     * @param b vetor Bezinho do Problema.
     * @param custos vetor de Custos do Problema.
     * @param basicas vetor de variáveis Básicsa do Problema.
     * @return vetor contendo os valores de Lambda.
     */
    public float[] calcularLambda(float B[][], float[] b, float[] custos, int[] basicas) {
        float[] CB = calcularCB(custos, b, basicas);
        float mT[][] = calcularTransposta(B); //Calcula a matriz Transposta de B
        return g.gauss(mT, CB);                //Aplica eliminação de Gauss
        //para encontrar os valores de lambda.
    }

    /**
     * Calcula o vetor de Custos Relativos para o Problema.
     *
     * @param lambda vetor Lambda.
     * @param restricoes matriz de Restrições do Problema.
     * @param custos vetor de Custos do Problema.
     * @param nao_basicas vetor de Não Básicas.
     * @return vetor com os Custos Relativos das Não Básicas.
     */
    public float[] calcularCustos(float[] lambda, float[][] restricoes, float[] custos, int[] nao_basicas) {
        float[] CNi = new float[nao_basicas.length];
        for (int i = 0; i < CNi.length; i++) {  //Realiza o calculo dos custos relativos
            //para as variáveis não basicas.
            //Cni = Cni - LambdaT * Ani
            CNi[i] = custos[nao_basicas[i]] - multiplicarVetores(lambda, obterColuna(restricoes, nao_basicas[i]));
        }
        return CNi;
    }

    /**
     * Obtem o indice da variável que contém o menor Custo Relativo.
     *
     * @param CNi vetor de Custos Relativos.
     * @return indice do menor Custo.
     */
    public int obterCNk(float[] CNi) {
        int cnk = 0;
        float valorMenor = CNi[0];
        for (int i = 0; i < CNi.length; i++) {
            if (CNi[i] < valorMenor) {   //Busca e armazena o menor dos custos
                valorMenor = CNi[i];    //relativos que foram calculados.
                cnk = i;
            }
        }
        return cnk;
    }

    /**
     * Verifica se variáeis Artificiais estão fora da Base do Problema.
     *
     * @param basicas vetor das variáveis Básicas.
     * @param custos vetor de Custos do Problema.
     * @return true se não haver mais variáveis Básicas na Base, false caso
     * contrário.
     */
    public boolean varArtificaisForaDaBase(int[] basicas, float[] custos) {
        for (int i = 0; i < basicas.length; i++) {
            if (custos[basicas[i]] != 0.0) //Verifica se todas as variáveis
            {
                return false;               //artificiais estão fora da Base
            }
        }
        return true;
    }

    /**
     * Verifica se a solução encontrada até o momento é Ótima.
     *
     * @param CNi vetor de Custos Relativos.
     * @param cnk indice do menor Custo Relativo.
     * @return true se for a solução ótima, false caso contrário.
     */
    public boolean ehSolucaoOtima(float[] CNi, int cnk) {
        return CNi[cnk] >= 0.0; //Verifica se existe custo relativo negativo
        //para afirmar existencia de solução ótima.
    }

    /**
     * Calcula o vetor Y para o Problema.
     *
     * @param B matriz B do Problema.
     * @param vet vetor coluna da variável de menor custo Relativo.
     * @return vetor com valores de Y.
     */
    public float[] calcularY(float[][] B, float[] vet) {
        return g.gauss(B, vet); //Executa a eliminação de Gauss sobre B para
        //encontrar os valores de Yi.
    }

    /**
     * Verifica se o Problema possui infinitas Soluções.
     *
     * @param Y vetor Y do Problema.
     * @return true se o Problema tiver infinitas soluções, false caso
     * contrário.
     */
    public boolean saoInfinitasSolucoes(float[] Y) {
        int m = Y.length;
        for (int i = 0; i < m; i++) {
            if (Y[i] > 0) //Verifica para todos os Yi se existe
            {
                return false;   //algum que é maior que 0 para não ser infinito
            }
        }                       //o número de soluções.
        return true;
    }

    /**
     * Calcula o valor de E do Problema.
     *
     * @param custosBasicas vetor dos Custos das variáveis Básicas.
     * @param Y vetor com valores de Y do Problema.
     * @return o valor de E.
     */
//    public float calcularE(float[] custosBasicas, float[] Y) {
//        ArrayList<Float> array = new ArrayList<>();
//        int m = custosBasicas.length;
//        System.out.print("\nE = min{ ");
//        for (int i = 0; i < m; i++) {
//            if (Y[i] > 0) {
//                array.add(custosBasicas[i] / Y[i]); //Faz a divisão de CBi / Yi
//                System.out.print(custosBasicas[i] + "/" + Y[i]);
//            }
//            System.out.print(" ");
//        }
//        System.out.println("}");
//        Collections.sort(array);
//        return array.get(0);    //Pega a de menor valor que tenha Yi maior que 0.
//    }
    public float calcularE(float[] Xb, float[] Y) {
        double result[] = new double[Y.length];
        System.out.print("E = min{");
        for (int i = 0; i < Y.length; i++) {
            if (Y[i] > 0) {
                System.out.print("("+Xb[i] + "/" + Y[i]+")");
                result[i] = Xb[i] / Y[i];
            } else {
                result[i] = -1.0;
            }
        }
        System.out.print("}");
        int k = 0;
        double minimo = 0;
        for (int i = 0; i < Y.length; i++) {
            if (result[i] >= 0.0) {
                minimo = result[i];
                k = i;
                break;
            }
        }

        for (int i = 0; i < Y.length; i++) {
            if (result[i] < 0) {
                continue;
            }
            if (result[i] < minimo) {
                k = i;
                minimo = result[i];
            }
        }
        return k;    //Pega a de menor valor que tenha Yi maior que 0.
    }

    /**
     * Realiza a atualiação da Base do Problema alterando as variáveis que estão
     * na Base e Fora dela.
     *
     * @param B matriz B dos dados do Problema.
     * @param XB vetor XB do Problema.
     * @param restricoes matriz de Restrições do Problema.
     * @param basicas vetor de variáveis Básicas do Problema.
     * @param nao_basicas vetor de variáveis Não Básicas do Problema.
     * @param Y vetor de valores de Y do Problema.
     * @param E valor de E do Problema.
     * @param cnk indice da variável de menor custo Relativo.
     * @return matriz de dados B atualizada pela nova Base.
     */
    public float[][] atualizarBase(float[][] B, float[] XB, float[][] restricoes, int[] basicas, int[] nao_basicas, float[] Y, float E, int cnk) {
        int aux = basicas[Math.round(E)];
        basicas[Math.round(E)] = nao_basicas[cnk];
        nao_basicas[cnk] = aux;
        return construirB(restricoes, basicas);
    }

    /**
     * Atualiza o vetor das não Básicas do Problema original após a execução da
     * Fase I com o Problema Artificial.
     *
     * @param nao_basicas vetor de Não Básicas do Problema.
     * @param custos vetor de Custos do Problema.
     * @return vetor de Não Básicas atualizado sem as variáveis artificiais.
     */
    public int[] atualizarNaoBasicas(int[] nao_basicas, float[] custos) {
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

    public static void main(String args[]) {
        Simplex simplex = new Simplex();

        //2 2 min -5 -3 3 5 <= 15 5 2 <= 10
        //2 3 min 1 1 -1 1 >= 3 1 1 <= 27 2 -1  <= -3

        float[] C2 = {3, -1};
        float[][] A2 = {
            {2, 1},
            {-1, 1},
            {-1, 1}
        };
        float[] b2 = {2,1,4};
        String[] s2 = {"max", ">=", ">=", "<="};
        Problema p3 = new Problema(A2, C2, b2, s2);

        simplex.simplex(p3); // Tem Fase I Tem Ótima OK

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

    public void exibirVetor(float[] vetor) {
        System.out.print(" ");
        for (int i = 0; i < vetor.length; i++) {
            System.out.print(vetor[i] + " ");
        }
    }

    public void exibirMatriz(float[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    public float[] obterColuna(float mat[][], int indice) {
        float ani[] = new float[mat.length];
        for (int i = 0; i < mat.length; i++) {
            ani[i] = mat[i][indice];
        }
        return ani;
    }

    public float[][] calcularTransposta(float[][] matriz) {
        int m = matriz.length;
        int n = matriz[0].length;
        float[][] transposta = new float[m][n];

        copiarMatriz(transposta, matriz);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                transposta[i][j] = matriz[j][i];
            }
        }
        return transposta;
    }

    public float multiplicarVetores(float a[], float b[]) {
        float sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

    public void copiarMatriz(float[][] destino, float[][] origem) {
        int m = origem.length;
        int n = origem[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                destino[i][j] = origem[i][j];
            }
        }
    }
}
