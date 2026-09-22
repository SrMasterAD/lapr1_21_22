import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Projeto {
    static final int LINHAS = 1000;
    static final int COLUNAS_REGISTOS = 5;
    static final Calendar c = Calendar.getInstance();
    static final Calendar c2 = Calendar.getInstance();
    static Scanner ler = new Scanner(System.in);
    static final int LINHAS_MATRIZES = 4;
    static final int COLUNAS_MATRIZES = 4;
    static final int LINHAS_MATRIZ_TRANSICAO = 5;
    static final int COLUNAS_MATRIZ_TRANSICAO = 5;

    public static void main(String[] args) throws IOException {

        String[] matrizDatasS = new String[LINHAS];
        int[][] matrizRegistos = new int[LINHAS][COLUNAS_REGISTOS];
        Date[] matrizDatas = new Date[LINHAS];
        String[] matrizDatasSTotal = new String[LINHAS];
        int[][] matrizRegistosTotal = new int[LINHAS][COLUNAS_REGISTOS];
        Date[] matrizDatasTotal = new Date[LINHAS];
        int[] numLinhas = new int[2]; //posição 0 corresponde aos acomulados e a posição 2 aos totais
        double[][] matrizTransicao = new double[LINHAS_MATRIZ_TRANSICAO][COLUNAS_MATRIZ_TRANSICAO];
        boolean sairCase;
        boolean sairPrograma = false;

        //valores impossíveis para verificar se a leitura do ficheiro foi realizada
        matrizRegistos[0][0] = -1;
        matrizRegistosTotal[0][0] = -1;
        matrizTransicao[0][0] = -1;

        if(args.length==0) {
            do {
                System.out.println();
                System.out.println("Deseja (escreva o número da funcionalidade):");
                System.out.println("1 - Ler ficheiros.");
                System.out.println("2 - Analizar e vizualizar os dados de COVID-19 num intervalo de tempo.");
                System.out.println("3 - Comparar os dados de COVID-19 de dois intervalos de tempo.");
                System.out.println("4 - Prever os dados de COVID-19 num dia específico.");
                System.out.println("5 - Sair.");

                String funcionalidade = lerFuncinalidade();
                System.out.println();
                switch (funcionalidade) {
                    case "1":
                        do {
                            sairCase=false;
                            System.out.println("Deseja introduzir o ficheiro de (escreva o número):");
                            System.out.println("1 - Casos acumulados de COVID-19.");
                            System.out.println("2 - Casos totais de COVID-19.");
                            System.out.println("3 - Matriz Transição.");
                            System.out.println("4 - Voltar para o menu inicial.");

                            String tipoDeFicheiro = lerFicheiro();
                            switch (tipoDeFicheiro) {
                                case "1":
                                    System.out.println();
                                    System.out.println("Introduza a localização do ficheiro dos registos de dados acumulados de Covid-19.");
                                    int totalLinhas = introducaoDoLocalDoFicheiroAcumulados(matrizRegistos, matrizDatasS);
                                    transformarMatrizStringParaDates(matrizDatas, matrizDatasS, totalLinhas);
                                    numLinhas[0] = totalLinhas;
                                    // limpar buffer por causa da leitura de int do ficheiro
                                    ler.nextLine();
                                    break;
                                case "2":
                                    System.out.println();
                                    System.out.println("Insira a localização do ficheiro total de dados diários: ");
                                    int totalLinhasTotal = introducaoDoLocalDoFicheiroTotais(matrizRegistosTotal,matrizDatasSTotal);
                                    transformarMatrizStringParaDatesTotal(matrizDatasTotal, matrizDatasSTotal, totalLinhasTotal);
                                    numLinhas[1] = totalLinhasTotal;
                                    // limpar buffer por causa da leitura de int do ficheiro
                                    ler.nextLine();
                                    break;
                                case "3":
                                    System.out.println();
                                    System.out.println("Insira a localização do ficheiro da matriz de transição: ");
                                    String fileInput = ler.nextLine();
                                    matrizTransicao = lerMatrizTransicao(fileInput);
                                    break;
                                case "4":
                                    sairCase = true;
                                    break;
                            }
                            System.out.println();
                        }while(!sairCase);
                        break;

                    case "2": // analizar dados
                        do {
                            sairCase = false;
                            System.out.println("Deseja analizar e visualizar (escreva o número):");
                            System.out.println("1 - Novos casos.");
                            System.out.println("2 - Casos totais ativos.");
                            System.out.println("3 - Voltar para o menu inicial.");
                            String opcaoAnalise = lerOpcaoAnalise();
                            System.out.println();
                            switch (opcaoAnalise) {
                                case "1":
                                    if (matrizRegistos[0][0] == -1) {
                                        System.out.println("O ficheiro dos dados acumulados não foi inserido.");
                                        System.out.println();
                                    } else {
                                        System.out.println("Insira a data de inicio da análise, no formato DD-MM-AAAA");
                                        Date DataInicio = lerDatasInseridas();
                                        int linhaDataInicio = VerLinhaCorrespondente(DataInicio, matrizDatas, numLinhas[0]);
                                        linhaDataInicio = verificarDataInicio(linhaDataInicio, numLinhas[0], matrizDatas);
                                        System.out.println();
                                        System.out.println("Insira a data de fim da análise, no formato DD-MM-AAAA");
                                        Date DataFim = lerDatasInseridas();
                                        int linhaDataFim = VerLinhaCorrespondente(DataFim, matrizDatas, numLinhas[0]);
                                        linhaDataFim = verificarDataFim(linhaDataFim, numLinhas[0], matrizDatas, linhaDataInicio);
                                        System.out.println();
                                        //ler a resolução temporal e verificar
                                        System.out.println("Insira a resolução temporal que deseja. (diaria/semanal/mensal)");
                                        String resolucaoTemporal = lerResolucaoTemporal();
                                        System.out.println();
                                        switch (resolucaoTemporal) {
                                            case "diaria":
                                                int[][] matrizRegistosDiarios = criarMatrizRegistosDiarios(numLinhas[0], matrizRegistos);
                                                c.setTime(matrizDatas[linhaDataInicio]);
                                                testeNovosCasosDiarios(matrizRegistosDiarios);//02-04-2020 a 03-04-2020
                                                String opcaodiariaString = lerQuaisDadosApresentar();
                                                int opcaodiaria = Integer.parseInt(opcaodiariaString);

                                                imprimirRegistosDiarios(matrizRegistosDiarios, linhaDataInicio, linhaDataFim, opcaodiaria);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    c.setTime(matrizDatas[linhaDataInicio]);
                                                    CriarFicheiroTextoDiario(opcaodiaria, linhaDataInicio, linhaDataFim, matrizRegistosDiarios);
                                                }
                                                break;

                                            case "semanal":
                                                int[] resultadosObitdos = new int[4];
                                                int linhaDataInicioCopia = linhaDataInicio;
                                                int linhaDataFimCopia = linhaDataFim;

                                                c.setTime(matrizDatas[linhaDataInicio]);
                                                int DSemanaInicial = c.get(Calendar.DAY_OF_WEEK);
                                                linhaDataInicio = valorinicialsemana(DSemanaInicial, linhaDataInicio);

                                                c.setTime(matrizDatas[linhaDataFim]);
                                                int DSemanaFinal = c.get(Calendar.DAY_OF_WEEK);
                                                linhaDataFim = valorfinalsemana(DSemanaFinal, linhaDataFim);

                                                c.setTime(matrizDatas[linhaDataInicio]);
                                                if (linhaDataFim > linhaDataInicio) {
                                                    comparacaoSemanal(linhaDataInicio, linhaDataFim, matrizRegistos, matrizDatas, linhaDataInicioCopia, linhaDataFimCopia, resultadosObitdos);
                                                    boolean testeSemanal = testeNovosCasosSemanais(resultadosObitdos); //07-04-2020 a 20-04-2020 com a opção 5;
                                                } else {
                                                    System.out.println("Não existem dados suficientes para fazer uma análise semanal.");
                                                    System.out.println();
                                                }
                                                break;
                                            case "mensal":
                                                c.setTime(matrizDatas[linhaDataInicio]);
                                                c2.setTime(matrizDatas[linhaDataFim]);
                                                int ContAnos = (c2.get(Calendar.YEAR) - c.get(Calendar.YEAR)) + 1;
                                                int ContMeses;
                                                ContMeses = ContadorMeses(ContAnos);
                                                if (c.get(Calendar.DAY_OF_MONTH) != 1) {
                                                    ContMeses--;
                                                }
                                                if (c2.get(Calendar.DAY_OF_MONTH) != c2.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                                                    ContMeses--;
                                                }
                                                if (ContMeses > 0) {
                                                    int[][] MatrizAnaliseMensal = AnalisarDadosMes(ContMeses, linhaDataInicio, matrizRegistos, matrizDatas);
                                                    testeNovosCasosMensais(MatrizAnaliseMensal); //Mes 05/2020
                                                    String opcaoMensalString = lerQuaisDadosApresentar();
                                                    int opcaomensal = Integer.parseInt(opcaoMensalString);

                                                    if (opcaomensal == 5) {
                                                        imprimirRegistosMensais(MatrizAnaliseMensal, ContMeses);
                                                    }
                                                    if (opcaomensal < 5) {
                                                        imprimirRegistoSingularMensais(MatrizAnaliseMensal, ContMeses, opcaomensal);
                                                    }
                                                    if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                        c.setTime(matrizDatas[linhaDataInicio]);
                                                        CriarFicheiroTextoMensal(opcaomensal, MatrizAnaliseMensal);
                                                    }
                                                }
                                                if (ContMeses <= 0) {
                                                    System.out.println("Não existem dados suficientes para fazer uma análise mensal");
                                                }
                                                break;
                                        }
                                    }
                                    break;
                                case "2":
                                    if (matrizRegistosTotal[0][0] == -1) {
                                        System.out.println("O ficheiro dos dados totais não foi inserido.");
                                        System.out.println();
                                    } else {
                                        System.out.println("Insira a data de inicio da análise, no formato DD-MM-AAAA");
                                        Date DataInicio = lerDatasInseridas();
                                        int linhaDataInicio = VerLinhaCorrespondente(DataInicio, matrizDatasTotal, numLinhas[1]);
                                        linhaDataInicio = verificarDataInicioAnaliseTotal(linhaDataInicio, numLinhas[1], matrizDatasTotal);
                                        System.out.println();
                                        System.out.println("Insira a data de fim da análise, no formato DD-MM-AAAA");
                                        Date DataFim = lerDatasInseridas();
                                        int linhaDataFim = VerLinhaCorrespondente(DataFim, matrizDatasTotal, numLinhas[1]);
                                        linhaDataFim = verificarDataFim(linhaDataFim, numLinhas[1], matrizDatasTotal, linhaDataInicio);
                                        System.out.println();
                                        //ler a resolução temporal e verificar
                                        System.out.println("Insira a resolução temporal que deseja. (diaria/semanal/mensal)");
                                        String resolucaoTemporal = lerResolucaoTemporal();
                                        System.out.println();
                                        switch (resolucaoTemporal) {
                                            case "diaria":
                                                int[][] matrizRegistosTotalDiarios = new int[numLinhas[1]][COLUNAS_REGISTOS - 1];
                                                for (int i = 0; i < numLinhas[1] - 1; i++) {
                                                    for (int j = 0; j < COLUNAS_REGISTOS - 1; j++) {
                                                        matrizRegistosTotalDiarios[i][j] = matrizRegistosTotal[i][j + 1];
                                                    }
                                                }
                                                boolean testecasototaldiario = testeCasosTotaisDiarios(matrizRegistosTotalDiarios);//02-04-2020 a 03-04-2020
                                                c.setTime(matrizDatasTotal[linhaDataInicio]);
                                                //testeTotaisCasosDiarios(matrizRegistos);//02-04-2020 a 03-04-2020
                                                String opcaodiariaString = lerQuaisDadosApresentar();
                                                int opcaodiaria = Integer.parseInt(opcaodiariaString);
                                                imprimirRegistosDiariosTotais(matrizRegistosTotal, linhaDataInicio, linhaDataFim, opcaodiaria);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    c.setTime(matrizDatasTotal[linhaDataInicio]);
                                                    CriarFicheiroTextoDiario(opcaodiaria, linhaDataInicio, linhaDataFim, matrizRegistosTotalDiarios);
                                                }
                                                break;
                                            case "semanal":
                                                int[] resultadosObitdos = new int[4];
                                                int linhaDataInicioCopia = linhaDataInicio;
                                                int linhaDataFimCopia = linhaDataFim;

                                                c.setTime(matrizDatasTotal[linhaDataInicio]);
                                                int DSemanaInicial = c.get(Calendar.DAY_OF_WEEK);
                                                linhaDataInicio = valorinicialsemana(DSemanaInicial, linhaDataInicio);

                                                c.setTime(matrizDatasTotal[linhaDataFim]);
                                                int DSemanaFinal = c.get(Calendar.DAY_OF_WEEK);
                                                linhaDataFim = valorfinalsemana(DSemanaFinal, linhaDataFim);

                                                c.setTime(matrizDatasTotal[linhaDataInicio]);
                                                if (linhaDataFim > linhaDataInicio) {
                                                    comparacaoSemanalTotais(linhaDataInicio, linhaDataFim, matrizRegistosTotal, matrizDatasTotal, linhaDataInicioCopia, linhaDataFimCopia, resultadosObitdos);
                                                    boolean testecasostotaissemanais = testeCasosTotaisSemanais(resultadosObitdos); // Semana 02-11-2020
                                                } else {
                                                    System.out.println("Não existem dados suficientes para fazer uma análise semanal.");
                                                    System.out.println();
                                                }
                                                break;
                                            case "mensal":
                                                c.setTime(matrizDatasTotal[linhaDataInicio]);
                                                c2.setTime(matrizDatasTotal[linhaDataFim]);
                                                int ContAnos = (c2.get(Calendar.YEAR) - c.get(Calendar.YEAR)) + 1;
                                                int ContMeses;
                                                ContMeses = ContadorMeses(ContAnos);
                                                if (c.get(Calendar.DAY_OF_MONTH) != 1) {
                                                    ContMeses--;
                                                }
                                                if (c2.get(Calendar.DAY_OF_MONTH) != c2.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                                                    ContMeses--;
                                                }
                                                if (ContMeses > 0) {
                                                    int[][] MatrizAnaliseMensal = AnalisarDadosMesTotal(ContMeses, linhaDataInicio, matrizRegistosTotal, matrizDatasTotal);
                                                    boolean testecasototalmensal = testeCasosTotaisMensais(MatrizAnaliseMensal); //Mes 12/2020
                                                    String opcaoMensalString = lerQuaisDadosApresentar();
                                                    int opcaomensal = Integer.parseInt(opcaoMensalString);
                                                    if (opcaomensal == 5) {
                                                        imprimirRegistosMensaisTotais(MatrizAnaliseMensal, ContMeses);
                                                    }
                                                    if (opcaomensal < 5) {
                                                        imprimirRegistoSingularMensaisTotais(MatrizAnaliseMensal, ContMeses, opcaomensal);
                                                    }
                                                    if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                        c.setTime(matrizDatasTotal[linhaDataInicio]);
                                                        CriarFicheiroTextoMensal(opcaomensal, MatrizAnaliseMensal);
                                                    }
                                                }
                                                if (ContMeses <= 0) {
                                                    System.out.println("Não existem dados suficientes para fazer uma análise mensal");
                                                }
                                                break;
                                        }
                                    }
                                case "3":
                                    sairCase = true;
                                    break;
                            }
                        }while(!sairCase);
                        break;
                    case "3": // Comparar intervalos
                        do {
                            sairCase=false;
                            System.out.println("Deseja comparar:");
                            System.out.println("1 - O número total de dados diários.");
                            System.out.println("2 - O número de novo dados diários.");
                            System.out.println("3 - Voltar para o menu inicial.");
                            System.out.println();
                            int opcaoDeComparacao = lerOpcaoDeComparacao();
                            System.out.println();
                            switch (opcaoDeComparacao) {
                                case 1:
                                    if (matrizRegistosTotal[0][0] == -1) {
                                        System.out.println("O ficheiro dos acumulados não foi inserido");
                                    } else {
                                        int[] linhasDatas = lerDadosIntervalosDeTempo(numLinhas[1], matrizDatasTotal);
                                        int diasTotais = diasTotais(linhasDatas);
                                        int[][] resultados = resultadosComparacaoIntervalosTotal(linhasDatas, matrizRegistosTotal, diasTotais);
                                        boolean testesResultados = testesResultadosTotais(resultados);
                                        double[][] MediaEDesvioPadrao1Intervalo = calculoDaMedia(matrizRegistosTotal, diasTotais, linhasDatas[0], 1);
                                        boolean testesMediaEDesvio = testesMediaEDesvioPadrao(MediaEDesvioPadrao1Intervalo);
                                        double[][] MediaEDesvioPadrao2Intervalo = calculoDaMedia(matrizRegistosTotal, diasTotais, linhasDatas[2], 1);
                                        double[][] MediaEDesvioPadraoResultados = MediaEDesvioPadraoResultados(resultados, diasTotais);

                                        int LerDadosAApresentar = LerDadosApresentarComparacoes();
                                        switch (LerDadosAApresentar) {
                                            case 1:
                                                imprimirComparacoesInfetados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0);
                                                imprimirMediaEDesvioPadraoInfetados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVcomparacaonovosinfetados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 0);
                                                    criarCSVMediaEDesvioPadraoInfetados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                            case 2:
                                                imprimirComparacoesHospitalizados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0);
                                                imprimirMediaEDesvioPadraoHospitalizados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVComparacoesHospitalizados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 0);
                                                    criarCSVMediaEDesvioPadraoHospitalizados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                            case 3:
                                                imprimirComparacoesUCI(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0);
                                                imprimirMediaEDesvioPadraoUCI(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVComparacoesUCI(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 0);
                                                    criarCSVMediaEDesvioPadraoUCI(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                            case 4:
                                                imprimirComparacoesMortes(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0);
                                                imprimirMediaEDesvioPadraoMortes(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVComparacoesMortes(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 0);
                                                    criarCSVMediaEDesvioPadraoMortes(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                            case 5:
                                                imprimirComparacoesInfetados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0);
                                                imprimirMediaEDesvioPadraoInfetados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                imprimirComparacoesHospitalizados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0);
                                                imprimirMediaEDesvioPadraoHospitalizados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                imprimirComparacoesUCI(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0);
                                                imprimirMediaEDesvioPadraoUCI(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                imprimirComparacoesMortes(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0);
                                                imprimirMediaEDesvioPadraoMortes(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVcomparacaonovosinfetados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 0);
                                                    criarCSVMediaEDesvioPadraoInfetados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    criarCSVComparacoesHospitalizados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 0);
                                                    criarCSVMediaEDesvioPadraoHospitalizados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    criarCSVComparacoesUCI(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 0);
                                                    criarCSVMediaEDesvioPadraoUCI(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    criarCSVComparacoesMortes(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 0);
                                                    criarCSVMediaEDesvioPadraoMortes(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                        }
                                    }
                                    break;
                                case 2:
                                    if (matrizRegistos[0][0] == -1) {
                                        System.out.println("O ficheiro dos casos totais não foi inserido");
                                    } else {
                                        int[] linhasDatas = lerDadosIntervalosDeTempo(numLinhas[0], matrizDatas);
                                        int diasTotais = diasTotais(linhasDatas);
                                        int[][] matrizRegistosDiarios = criarMatrizRegistosDiarios(numLinhas[0], matrizRegistos);
                                        int[][] resultados = resultadosComparacaoIntervalosAcumulados(linhasDatas, matrizRegistosDiarios, diasTotais);
                                        boolean testesResulados = testesResultadosAcumulados(resultados);
                                        double[][] MediaEDesvioPadrao1Intervalo = calculoDaMedia(matrizRegistosDiarios, diasTotais, linhasDatas[0], 0);
                                        boolean testesMediaEDesvioPadrao = testesMediaEDesvioPadraoAcumulado(MediaEDesvioPadrao1Intervalo);
                                        double[][] MediaEDesvioPadrao2Intervalo = calculoDaMedia(matrizRegistosDiarios, diasTotais, linhasDatas[2], 0);
                                        double[][] MediaEDesvioPadraoResultados = MediaEDesvioPadraoResultados(resultados, diasTotais);

                                        int LerDadosAApresentar = LerDadosApresentarComparacoes();
                                        switch (LerDadosAApresentar) {
                                            case 1:
                                                imprimirComparacoesInfetados(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1);
                                                imprimirMediaEDesvioPadraoInfetados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVcomparacaonovosinfetados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 1);
                                                    criarCSVMediaEDesvioPadraoInfetados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                            case 2:
                                                imprimirComparacoesHospitalizados(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1);
                                                imprimirMediaEDesvioPadraoHospitalizados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVComparacoesHospitalizados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 1);
                                                    criarCSVMediaEDesvioPadraoHospitalizados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                            case 3:
                                                imprimirComparacoesUCI(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1);
                                                imprimirMediaEDesvioPadraoUCI(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVComparacoesUCI(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 1);
                                                    criarCSVMediaEDesvioPadraoUCI(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                            case 4:
                                                imprimirComparacoesMortes(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1);
                                                imprimirMediaEDesvioPadraoMortes(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVComparacoesMortes(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 1);
                                                    criarCSVMediaEDesvioPadraoMortes(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                            case 5:
                                                imprimirComparacoesInfetados(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1);
                                                imprimirMediaEDesvioPadraoInfetados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                imprimirComparacoesHospitalizados(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1);
                                                imprimirMediaEDesvioPadraoHospitalizados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                imprimirComparacoesUCI(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1);
                                                imprimirMediaEDesvioPadraoUCI(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                imprimirComparacoesMortes(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1);
                                                imprimirMediaEDesvioPadraoMortes(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados);
                                                if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                                    System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
                                                    String NomeFicheiro = ler.nextLine();
                                                    File file = new File(NomeFicheiro + ".csv");
                                                    FileWriter fw = new FileWriter(file, true);
                                                    PrintWriter pw = new PrintWriter(fw);
                                                    criarCSVcomparacaonovosinfetados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 1);
                                                    criarCSVMediaEDesvioPadraoInfetados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    criarCSVComparacoesHospitalizados(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 1);
                                                    criarCSVMediaEDesvioPadraoHospitalizados(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    criarCSVComparacoesUCI(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 1);
                                                    criarCSVMediaEDesvioPadraoUCI(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    criarCSVComparacoesMortes(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, pw, 1);
                                                    criarCSVMediaEDesvioPadraoMortes(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, pw);
                                                    pw.flush();
                                                    pw.close();
                                                }
                                                break;
                                        }
                                    }
                                    break;
                                case 3:
                                    sairCase=true;
                                    break;
                            }
                        }while(!sairCase);
                        break;
                    case "4": // Previsão
                        do {
                            sairCase=false;
                            System.out.println("Deseja (escolha a opção):");
                            System.out.println("1 - Prever dados para um dia específico.");
                            System.out.println("2 - Calcular o número esperado de dias que um indivíduo passa num estado antes de transitar para outro estado.");
                            System.out.println("3 - Voltar para o menu inicial.");
                            String opcaoPrevisao = lerOpcaoPrevisao();
                            System.out.println();
                            switch (opcaoPrevisao) {
                                case "1":
                                    if (matrizRegistosTotal[0][0] == -1 && matrizTransicao[0][0] == -1) {
                                        System.out.println("Os ficheiros dos dados totais e da matriz transição não foram inseridos.");
                                    } else if (matrizTransicao[0][0] == -1) {
                                        System.out.println("O ficheiro da matriz transição não foi inserido.");
                                    } else if (matrizRegistosTotal[0][0] == -1) {
                                        System.out.println("O ficheiro dos dados totais não foi inserido.");
                                    } else {
                                        System.out.println("Insira a data que deseja prever, no formato DD-MM-AAAA");
                                        Date dataprevisao = lerDatasInseridas();
                                        c2.setTime(dataprevisao);
                                        int linhaDataPrevisao = VerLinhaCorrespondente(dataprevisao, matrizDatasTotal, numLinhas[1]);
                                        System.out.println();
                                        int diasDeDiferenca = 0;
                                        while ((linhaDataPrevisao == -1 && dataprevisao.before(matrizDatasTotal[0])) || linhaDataPrevisao == 0) {
                                            if (linhaDataPrevisao == -1 && dataprevisao.before(matrizDatasTotal[0])) {
                                                System.out.println("Esta data é anterior à todas as datas do ficheiro. Por favor insira novamente.");
                                                dataprevisao = lerDatasInseridas();
                                                c2.setTime(dataprevisao);
                                                linhaDataPrevisao = VerLinhaCorrespondente(dataprevisao, matrizDatasTotal, numLinhas[1]);
                                            } else if (linhaDataPrevisao == 0) {
                                                System.out.println("Não é possivel prever os dados da primeira data do ficheiro. Por favor insira novamente.");
                                                dataprevisao = lerDatasInseridas();
                                                c2.setTime(dataprevisao);
                                                linhaDataPrevisao = VerLinhaCorrespondente(dataprevisao, matrizDatasTotal, numLinhas[1]);
                                            }
                                        }
                                        if (dataprevisao.after(matrizDatasTotal[numLinhas[1] - 1])) {
                                            c2.setTime(dataprevisao);
                                            while (dataprevisao.after(matrizDatasTotal[numLinhas[1] - 1])) {
                                                dataprevisao.setDate(dataprevisao.getDate() - 1);
                                                diasDeDiferenca++;
                                            }
                                        } else {
                                            diasDeDiferenca = 1;
                                            dataprevisao.setDate(dataprevisao.getDate() - 1);
                                        }

                                        int linhaDataMaisProx = VerLinhaCorrespondente(dataprevisao, matrizDatasTotal, numLinhas[1]);
                                        double[][] copiaMatrizTransicao = copiarMatriz(matrizTransicao);
                                        if (diasDeDiferenca > 1) {
                                            for (int i = 0; i < diasDeDiferenca - 1; i++) {
                                                matrizTransicao = multiplicarMatrizes(matrizTransicao, copiaMatrizTransicao);
                                                boolean teste = testeMultiplicacaoMatriz(matrizTransicao); // teste para dia 09-01-2022
                                            }
                                        }
                                        double[] matrizPrevisaoDeDados = mutiplicarMatrizPorVetor(matrizTransicao, converterVetorDeIntParaDouble(matrizRegistosTotal[linhaDataMaisProx]));
                                        imprimirMatrizPevisaoDeDados(matrizPrevisaoDeDados);
                                        if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                            criarCSVMatrizPevisaoDeDados(matrizPrevisaoDeDados);
                                        }
                                        matrizTransicao = copiarMatriz(copiaMatrizTransicao);
                                    }
                                    break;
                                case "2":
                                    if (matrizTransicao[0][0] == -1) {
                                        System.out.println("O ficheiro da matriz transição não foi inserido.");
                                    } else {
                                        double[][] matrizReduzida = criarMatrizSemEstadoAbsorvente(matrizTransicao);
                                        double[][] identidade = criarMatrizIdentidade();
                                        double[][] matrizOriginal = subtrairDuasMatrizes(matrizReduzida, identidade);
                                        double[][] matrizInversa = calculoDaInversa(matrizOriginal);
                                        double[] vetorLinhaComEntradasUm = {1, 1, 1, 1};
                                        double[] matrizNumeroEsperadoDeDias = mutiplicarVetorPorMatriz(vetorLinhaComEntradasUm, matrizInversa);

                                        System.out.println("Qual o estado transiente em que o indivíduo começa? (escreva o número do estado)");
                                        System.out.println("1 - Não infetado");
                                        System.out.println("2 - Infetado");
                                        System.out.println("3 - Hospitalizado");
                                        System.out.println("4 - Internado na UCI");
                                        String estadoString = lerEstado();
                                        int estado = Integer.parseInt(estadoString);
                                        System.out.println();
                                        System.out.printf("Número esperado de dias até chegar ao estado óbito: %.1f\n", matrizNumeroEsperadoDeDias[estado - 1]);
                                        if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
                                            double numeroEsperadodeDias = matrizNumeroEsperadoDeDias[estado - 1];
                                            criarCSVPrevisaoNumeroEsperadoDeDias(numeroEsperadodeDias);
                                        }
                                    }
                                    break;
                                case "3":
                                    sairCase = true;
                                    break;
                            }
                        }while(!sairCase);
                        break;
                    case "5": // Sair do programa
                        sairPrograma = true;
                }
            }while(!sairPrograma);

        } else if (args.length==16){

            String File_out = args[15];
            String File_In = args[14];
            new File(File_out);
            int meses = Integer.parseInt(args[1]);
            Date di = lerDatasInseridasNaoInterativoAcumulados(args[3]);
            Date df = lerDatasInseridasNaoInterativoAcumulados(args[5]);
            Date di1 = lerDatasInseridasNaoInterativoAcumulados(args[7]);
            Date df1 = lerDatasInseridasNaoInterativoAcumulados(args[9]);
            Date di2 = lerDatasInseridasNaoInterativoAcumulados(args[11]);
            Date df2 = lerDatasInseridasNaoInterativoAcumulados(args[13]);
            int totalLinhas = preencherMatrizRegistos(File_In, matrizRegistos, matrizDatasS);
            transformarMatrizStringParaDates(matrizDatas, matrizDatasS, totalLinhas);

            int linhaDataInicio = VerLinhaCorrespondente(di, matrizDatas, totalLinhas);
            int linhaDataFim = VerLinhaCorrespondente(df, matrizDatas, totalLinhas);
            if(linhaDataInicio!=-1 && linhaDataFim!=-1) {
                switch (meses) {
                    case 0:
                        int[][] matrizRegistosDiarios = criarMatrizRegistosDiarios(totalLinhas, matrizRegistos);
                        c.setTime(matrizDatas[linhaDataInicio]);
                        imprimirRegistosDiariosNaoInterativo(matrizRegistosDiarios, linhaDataInicio, linhaDataFim, File_out);
                        break;
                    case 1:
                        c.setTime(matrizDatas[linhaDataInicio]);
                        int DSemanaInicial = c.get(Calendar.DAY_OF_WEEK);
                        linhaDataInicio = valorinicialsemana(DSemanaInicial, linhaDataInicio);
                        c.setTime(matrizDatas[linhaDataFim]);
                        int DSemanaFinal = c.get(Calendar.DAY_OF_WEEK);
                        linhaDataFim = valorfinalsemana(DSemanaFinal, linhaDataFim);
                        c.setTime(matrizDatas[linhaDataInicio]);
                        if (linhaDataFim > linhaDataInicio) {
                            comparacaoSemanalNaoInterativo(linhaDataInicio, linhaDataFim, matrizRegistos, File_out);
                        }
                        break;
                    case 2:
                        c.setTime(matrizDatas[linhaDataInicio]);
                        c2.setTime(matrizDatas[linhaDataFim]);
                        int ContAnos = (c2.get(Calendar.YEAR) - c.get(Calendar.YEAR)) + 1;
                        int ContMeses;
                        ContMeses = ContadorMeses(ContAnos);
                        if (c.get(Calendar.DAY_OF_MONTH) != 1) {
                            ContMeses--;
                        }
                        if (c2.get(Calendar.DAY_OF_MONTH) != c2.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            ContMeses--;
                        }
                        if (ContMeses > 0) {
                            int[][] MatrizAnaliseMensal = AnalisarDadosMes(ContMeses, linhaDataInicio, matrizRegistos, matrizDatas);
                            imprimirRegistosMensaisNaoInterativo(MatrizAnaliseMensal, ContMeses, File_out);
                        }
                        break;
                }
            }

            int[] linhasDatas = lerDadosIntervalosDeTempoNaoInterativo(totalLinhas, matrizDatas, di1, df1, di2, df2);
            if (linhasDatas[0] != -2) {
                int diasTotais = diasTotais(linhasDatas);
                int[][]matrizRegistosDiarios = criarMatrizRegistosDiarios(totalLinhas, matrizRegistos);
                int[][] resultados = resultadosComparacaoIntervalosAcumulados(linhasDatas, matrizRegistosDiarios, diasTotais);
                double[][] MediaEDesvioPadrao1Intervalo = calculoDaMedia(matrizRegistosDiarios, diasTotais, linhasDatas[0], 0);
                double[][] MediaEDesvioPadrao2Intervalo = calculoDaMedia(matrizRegistosDiarios, diasTotais, linhasDatas[2], 0);
                double[][] MediaEDesvioPadraoResultados = MediaEDesvioPadraoResultados(resultados, diasTotais);
                imprimirComparacoesInfetadosNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1, File_out);
                imprimirMediaEDesvioPadraoInfetadosNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesHospitalizadosNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1, File_out);
                imprimirMediaEDesvioPadraoHospitalizadosNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesUCINaoInterativo(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1, File_out);
                imprimirMediaEDesvioPadraoUCINaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesMortesNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1, File_out);
                imprimirMediaEDesvioPadraoMortesNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
            }
        }else if(args.length==5) {
            String File_out = args[4];
            new File(File_out);
            String FileInMatrizTransicao = args[3];
            String FileInTotal = args[2];
            Date dataprevisao = lerDatasInseridasNaoInterativoAcumulados(args[1]);
            int totalLinhasTotal = preencherMatrizRegistosTotal(FileInTotal, matrizDatasSTotal, matrizRegistosTotal);
            transformarMatrizStringParaDatesTotal(matrizDatasTotal, matrizDatasSTotal, totalLinhasTotal);
            matrizTransicao = lerMatrizTransicao(FileInMatrizTransicao);

            c2.setTime(dataprevisao);
            int linhadataprevisao = VerLinhaCorrespondente(dataprevisao, matrizDatasTotal, totalLinhasTotal);
            int diasDeDiferenca = 0;
            if (dataprevisao.after(matrizDatasTotal[0])){
                if (dataprevisao.after(matrizDatasTotal[totalLinhasTotal - 1])) {
                    c.setTime(dataprevisao);
                    while (dataprevisao.after(matrizDatasTotal[totalLinhasTotal - 1])) {
                        dataprevisao.setDate(dataprevisao.getDate() - 1);
                        diasDeDiferenca++;
                    }
                } else {
                    diasDeDiferenca = 1;
                    dataprevisao.setDate(dataprevisao.getDate() - 1);
                }
                int linhaDataMaisProx = VerLinhaCorrespondente(dataprevisao, matrizDatasTotal, totalLinhasTotal);
                double[][] copiaMatrizTransicao = copiarMatriz(matrizTransicao);
                if (diasDeDiferenca > 1) {
                    for (int i = 0; i < diasDeDiferenca - 1; i++) {
                        matrizTransicao = multiplicarMatrizes(matrizTransicao, copiaMatrizTransicao);
                    }
                }
                double[] matrizPrevisaoDeDados = mutiplicarMatrizPorVetor(matrizTransicao, converterVetorDeIntParaDouble(matrizRegistosTotal[linhaDataMaisProx]));
                imprimirMatrizPevisaoDeDadosNaoInterativo(matrizPrevisaoDeDados,File_out);
                matrizTransicao=copiaMatrizTransicao;
                double[][] matrizReduzida = criarMatrizSemEstadoAbsorvente(matrizTransicao);
                double[][] identidade = criarMatrizIdentidade();
                double[][] matrizOriginal = subtrairDuasMatrizes(matrizReduzida, identidade);
                double[][] matrizInversa = calculoDaInversa(matrizOriginal);
                double[] vetorLinhaComEntradasUm = {1, 1, 1, 1};
                double[] matrizNumeroEsperadoDeDias = mutiplicarVetorPorMatriz(vetorLinhaComEntradasUm, matrizInversa);
                imprimirMatrizNumeroEsperadoDeDias(matrizNumeroEsperadoDeDias,File_out);
            }

        }else if(args.length==20){

            String File_out = args[19];
            String FileInAcumulados = args[17];
            String FileInTotal=args[16];
            String FileInMatrizTransicao=args[18];
            new File(File_out);
            int meses = Integer.parseInt(args[1]);
            Date di = lerDatasInseridasNaoInterativoAcumulados(args[3]);
            Date df = lerDatasInseridasNaoInterativoAcumulados(args[5]);
            Date di1 = lerDatasInseridasNaoInterativoAcumulados(args[7]);
            Date df1 = lerDatasInseridasNaoInterativoAcumulados(args[9]);
            Date di2 = lerDatasInseridasNaoInterativoAcumulados(args[11]);
            Date df2 = lerDatasInseridasNaoInterativoAcumulados(args[13]);
            Date dataprevisao = lerDatasInseridasNaoInterativoAcumulados(args[15]);

            int totalLinhasTotal = preencherMatrizRegistosTotal(FileInTotal, matrizDatasSTotal, matrizRegistosTotal);
            transformarMatrizStringParaDatesTotal(matrizDatasTotal, matrizDatasSTotal, totalLinhasTotal);
            matrizTransicao = lerMatrizTransicao(FileInMatrizTransicao);
            int totalLinhas = preencherMatrizRegistos(FileInAcumulados, matrizRegistos, matrizDatasS);
            transformarMatrizStringParaDates(matrizDatas, matrizDatasS, totalLinhas);

            int linhaDataInicio = VerLinhaCorrespondente(di, matrizDatas, totalLinhas);
            int linhaDataFim = VerLinhaCorrespondente(df, matrizDatas, totalLinhas);
            if(linhaDataFim!=-1 && linhaDataInicio!=-1) {
                switch (meses) {
                    case 0:
                        int[][] matrizRegistosDiarios = criarMatrizRegistosDiarios(totalLinhas, matrizRegistos);
                        c.setTime(matrizDatas[linhaDataInicio]);
                        imprimirRegistosDiariosNaoInterativo(matrizRegistosDiarios, linhaDataInicio, linhaDataFim, File_out);
                        break;
                    case 1:
                        c.setTime(matrizDatas[linhaDataInicio]);
                        int DSemanaInicial = c.get(Calendar.DAY_OF_WEEK);
                        linhaDataInicio = valorinicialsemana(DSemanaInicial, linhaDataInicio);

                        c.setTime(matrizDatas[linhaDataFim]);
                        int DSemanaFinal = c.get(Calendar.DAY_OF_WEEK);
                        linhaDataFim = valorfinalsemana(DSemanaFinal, linhaDataFim);

                        c.setTime(matrizDatas[linhaDataInicio]);
                        if (linhaDataFim > linhaDataInicio) {
                            comparacaoSemanalNaoInterativo(linhaDataInicio, linhaDataFim, matrizRegistos, File_out);
                        }
                        break;
                    case 2:
                        c.setTime(matrizDatas[linhaDataInicio]);
                        c2.setTime(matrizDatas[linhaDataFim]);
                        int ContAnos = (c2.get(Calendar.YEAR) - c.get(Calendar.YEAR)) + 1;
                        int ContMeses;
                        ContMeses = ContadorMeses(ContAnos);
                        if (c.get(Calendar.DAY_OF_MONTH) != 1) {
                            ContMeses--;
                        }
                        if (c2.get(Calendar.DAY_OF_MONTH) != c2.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            ContMeses--;
                        }
                        if (ContMeses > 0) {
                            int[][] MatrizAnaliseMensal = AnalisarDadosMes(ContMeses, linhaDataInicio, matrizRegistos, matrizDatas);
                            imprimirRegistosMensaisNaoInterativo(MatrizAnaliseMensal, ContMeses, File_out);
                        }
                        break;
                }
            }
            int[] linhasDatas = lerDadosIntervalosDeTempoNaoInterativo(totalLinhas, matrizDatas, di1, df1, di2, df2);
            if (linhasDatas[0] != -2) {
                int diasTotais = diasTotais(linhasDatas);
                int[][] matrizRegistosDiarios = criarMatrizRegistosDiarios(totalLinhas, matrizRegistos);
                int[][] resultados = resultadosComparacaoIntervalosAcumulados(linhasDatas, matrizRegistosDiarios, diasTotais);
                double[][] MediaEDesvioPadrao1Intervalo = calculoDaMedia(matrizRegistosDiarios, diasTotais, linhasDatas[0], 0);
                double[][] MediaEDesvioPadrao2Intervalo = calculoDaMedia(matrizRegistosDiarios, diasTotais, linhasDatas[2], 0);
                double[][] MediaEDesvioPadraoResultados = MediaEDesvioPadraoResultados(resultados, diasTotais);
                imprimirComparacoesInfetadosNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1, File_out);
                imprimirMediaEDesvioPadraoInfetadosNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesHospitalizadosNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1, File_out);
                imprimirMediaEDesvioPadraoHospitalizadosNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesUCINaoInterativo(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1, File_out);
                imprimirMediaEDesvioPadraoUCINaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesMortesNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatas, matrizRegistosDiarios, 1, File_out);
                imprimirMediaEDesvioPadraoMortesNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
            }

            linhaDataInicio = VerLinhaCorrespondente(di, matrizDatasTotal, totalLinhasTotal);
            linhaDataFim = VerLinhaCorrespondente(df, matrizDatasTotal, totalLinhasTotal);
            if(linhaDataFim!=-1 && linhaDataInicio!=-1){
                switch (meses) {
                    case 0:
                        c.setTime(matrizDatasTotal[linhaDataInicio]);
                        imprimirRegistosDiariosTotaisNaoInterativo(matrizRegistosTotal, linhaDataInicio, linhaDataFim, File_out);
                    case 1:
                        c.setTime(matrizDatasTotal[linhaDataInicio]);
                        int DSemanaInicial = c.get(Calendar.DAY_OF_WEEK);
                        linhaDataInicio = valorinicialsemana(DSemanaInicial, linhaDataInicio);

                        c.setTime(matrizDatasTotal[linhaDataFim]);
                        int DSemanaFinal = c.get(Calendar.DAY_OF_WEEK);
                        linhaDataFim = valorfinalsemana(DSemanaFinal, linhaDataFim);
                        c.setTime(matrizDatasTotal[linhaDataInicio]);

                        if (linhaDataFim > linhaDataInicio) {
                            comparacaoSemanalTotaisNaoInterativo(linhaDataInicio, linhaDataFim, matrizRegistosTotal, File_out);
                        }
                        break;
                    case 2:
                        c.setTime(matrizDatasTotal[linhaDataInicio]);
                        c2.setTime(matrizDatasTotal[linhaDataFim]);
                        int ContAnos = (c2.get(Calendar.YEAR) - c.get(Calendar.YEAR)) + 1;
                        int ContMeses;
                        ContMeses = ContadorMeses(ContAnos);
                        if (c.get(Calendar.DAY_OF_MONTH) != 1) {
                            ContMeses--;
                        }
                        if (c2.get(Calendar.DAY_OF_MONTH) != c2.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            ContMeses--;
                        }
                        if (ContMeses > 0) {
                            int[][] MatrizAnaliseMensal = AnalisarDadosMesTotal(ContMeses, linhaDataInicio, matrizRegistosTotal, matrizDatasTotal);
                            //testeNovosCasosMensaisTotais(MatrizAnaliseMensal); //Mes 05/2020
                            imprimirRegistosMensaisTotaisNaoInterativo(MatrizAnaliseMensal, ContMeses,File_out);
                        }
                        break;
                }
            }

            linhasDatas = lerDadosIntervalosDeTempoNaoInterativo(totalLinhasTotal, matrizDatasTotal, di1, df1, di2, df2);
            if (linhasDatas[0] != -2) {
                int diasTotais = diasTotais(linhasDatas);
                int[][] resultados = resultadosComparacaoIntervalosAcumulados(linhasDatas, matrizRegistosTotal, diasTotais);
                double[][] MediaEDesvioPadrao1Intervalo = calculoDaMedia(matrizRegistosTotal, diasTotais, linhasDatas[0], 0);
                double[][] MediaEDesvioPadrao2Intervalo = calculoDaMedia(matrizRegistosTotal, diasTotais, linhasDatas[2], 0);
                double[][] MediaEDesvioPadraoResultados = MediaEDesvioPadraoResultados(resultados, diasTotais);
                imprimirComparacoesInfetadosNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0, File_out);
                imprimirMediaEDesvioPadraoInfetadosNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesHospitalizadosNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0, File_out);
                imprimirMediaEDesvioPadraoHospitalizadosNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesUCINaoInterativo(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0, File_out);
                imprimirMediaEDesvioPadraoUCINaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
                imprimirComparacoesMortesNaoInterativo(resultados, diasTotais, linhasDatas, matrizDatasTotal, matrizRegistosTotal, 0, File_out);
                imprimirMediaEDesvioPadraoMortesNaoInterativo(MediaEDesvioPadrao1Intervalo, MediaEDesvioPadrao2Intervalo, MediaEDesvioPadraoResultados, File_out);
            }




            c2.setTime(dataprevisao);
            int linhadataprevisao = VerLinhaCorrespondente(dataprevisao, matrizDatasTotal, totalLinhasTotal);
            int diasDeDiferenca = 0;
            if (dataprevisao.after(matrizDatasTotal[0])){
                if (dataprevisao.after(matrizDatasTotal[totalLinhasTotal - 1])) {
                    c.setTime(dataprevisao);
                    while (dataprevisao.after(matrizDatasTotal[totalLinhasTotal - 1])) {
                        dataprevisao.setDate(dataprevisao.getDate() - 1);
                        diasDeDiferenca++;
                    }
                } else {
                    diasDeDiferenca = 1;
                    dataprevisao.setDate(dataprevisao.getDate() - 1);
                }
                int linhaDataMaisProx = VerLinhaCorrespondente(dataprevisao, matrizDatasTotal, totalLinhasTotal);
                double[][] copiaMatrizTransicao = copiarMatriz(matrizTransicao);
                if (diasDeDiferenca > 1) {
                    for (int i = 0; i < diasDeDiferenca - 1; i++) {
                        matrizTransicao = multiplicarMatrizes(matrizTransicao, copiaMatrizTransicao);
                    }
                }
                double[] matrizPrevisaoDeDados = mutiplicarMatrizPorVetor(matrizTransicao, converterVetorDeIntParaDouble(matrizRegistosTotal[linhaDataMaisProx]));
                imprimirMatrizPevisaoDeDadosNaoInterativo(matrizPrevisaoDeDados,File_out);
                matrizTransicao=copiaMatrizTransicao;
                double[][] matrizReduzida = criarMatrizSemEstadoAbsorvente(matrizTransicao);
                double[][] identidade = criarMatrizIdentidade();
                double[][] matrizOriginal = subtrairDuasMatrizes(matrizReduzida, identidade);
                double[][] matrizInversa = calculoDaInversa(matrizOriginal);
                double[] vetorLinhaComEntradasUm = {1, 1, 1, 1};
                double[] matrizNumeroEsperadoDeDias = mutiplicarVetorPorMatriz(vetorLinhaComEntradasUm, matrizInversa);
                imprimirMatrizNumeroEsperadoDeDias(matrizNumeroEsperadoDeDias,File_out);
            }
        }
    }



    // preencher as 2 matrizes com os dados do fich csv e da return ao numLinhas
    public static int preencherMatrizRegistos (String file,int[][] registos,String[] datas) throws FileNotFoundException{
        Scanner in = new Scanner(new File(file));
        String legenda = in.nextLine();
        int numLinha = 0;

        while (in.hasNext()){
            String[] linha = (in.nextLine()).split(",");
            datas[numLinha] = linha[0];
            for (int numColuna = 0; numColuna < COLUNAS_REGISTOS; numColuna++) {
                registos[numLinha][numColuna] = Integer.parseInt(linha[numColuna + 1]); // conversão para int
            }
            numLinha++;
        }
        in.close();
        return numLinha;
    }

    public static Date lerDatasInseridas() {
        try {
            Scanner s = new Scanner(System.in);
            String dataRecebida = s.nextLine();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date dt = df.parse(dataRecebida);
            return dt;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Date();
        }
    }

    public static void transformarMatrizStringParaDates(Date [] matrizDatas, String [] matrizDatasS, int linhas) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            for (int i=0;i<linhas;i++) {
                matrizDatas[i] = df.parse(matrizDatasS[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int VerLinhaCorrespondente(Date data,Date [] MatrizDatas, int MaxLinhas){
        int linha = 0;

        for (int i=0;i<MaxLinhas;i++){
            if (MatrizDatas[i].equals(data)){
                return i;
            }
        }
        return -1;
    }

    public static int valorinicialsemana(int DSemanaInicial, int linhaDataInicio){
        while(DSemanaInicial != 2){
            linhaDataInicio++;
            c.add(Calendar.DATE, 1);
            DSemanaInicial = c.get(Calendar.DAY_OF_WEEK);
        }
        return linhaDataInicio;
    }

    public static int valorfinalsemana(int DSemanaFinal, int linhaDataFim){
        while(DSemanaFinal != 2){
            linhaDataFim--;
            c.add(Calendar.DATE, -1);
            DSemanaFinal = c.get(Calendar.DAY_OF_WEEK);
        }
        return linhaDataFim;
    }

    public static void comparacaoSemanal(int linhainicio, int linhafim, int[][] registos, Date[]MatrizData, int linhaDataInicioCopia, int linhaDataFimCopia, int [] resultadosObtidos) throws IOException {
        int linhamedia = linhainicio + 7;
        int comparacao;
        int qtd = 0;
        String nString = lerQuaisDadosApresentar();
        int n = Integer.parseInt(nString);

        while (linhamedia <= linhafim) {
            if(n<= 4){
                comparacao = (registos[linhamedia - 1][n]) - (registos[linhainicio - 1][n]);
                impressaoComparacaoSemanalSingular(comparacao, n, qtd);
            }else {
                for (int i = 1; i <= 4; i++) {
                    comparacao = (registos[linhamedia - 1][i]) - (registos[linhainicio - 1][i]);
                    resultadosObtidos[i-1] = comparacao;
                    impressaoComparacaoSemanal(comparacao, i);
                }
            }
            c.add(Calendar.DATE, 7);
            linhamedia += 7;
            linhainicio += 7;
            qtd++;
        }
        if (VerificarSeUtilizadorQuerGuardarFicheiro()){
            c.setTime(MatrizData[linhaDataInicioCopia]);
            CriarFicheiroTextoSemanal(linhaDataInicioCopia, linhaDataFimCopia, n, registos,MatrizData);
        }
    }

    public static void impressaoComparacaoSemanalSingular(int comparacao, int i, int qtd){
        if (qtd == 0) {
            switch (i) {
                case 1:
                    System.out.println("Novos infetados: ");
                    break;
                case 2:
                    System.out.println("Novos Hospitalizados: ");
                    break;
                case 3:
                    System.out.println("Novos internados UCI: ");
                    break;
                case 4:
                    System.out.println("Novas mortes: ");
                    break;
            }
        }
        System.out.print("Na Semana de " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR)+ ": ");
        System.out.println(comparacao);
    }

    public static void impressaoComparacaoSemanal(int comparacao, int i){
        switch (i){
            case 1 :
                System.out.println("Semana de " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
                System.out.println("Novos infetados: " + comparacao);
                break;
            case 2 :
                System.out.println("Novos Hospitalizados: " + comparacao);
                break;
            case 3 :
                System.out.println("Novos internados UCI: " + comparacao);
                break;
            case 4:
                System.out.println("Novas mortes: " + comparacao);
                System.out.println();
                break;
        }
    }

    public static int [][] criarMatrizRegistosDiarios(int totalLinhas,int [][] arr) {
        int [][] matrizRegistosDiarios = new int[totalLinhas][COLUNAS_REGISTOS-1];
        for (int i = 0; i < totalLinhas-1; i++) {
            for (int j = 0; j < COLUNAS_REGISTOS-1; j++) {
                matrizRegistosDiarios[i+1][j] = (arr[i+1][j+1] - arr[i][j+1]);
            }
        }
        return matrizRegistosDiarios;
    }

    public static void imprimirRegistosDiarios (int [][] arr, int linhaDataInicial, int linhaDataFinal, int opcao){

        switch (opcao){
            case 1 :
                imprimirUmDado("Novos infetados: ",linhaDataInicial,linhaDataFinal,arr,opcao);
                break;
            case 2 :
                imprimirUmDado("Novos hospitalizados: ",linhaDataInicial,linhaDataFinal,arr,opcao);
                break;
            case 3 :
                imprimirUmDado("Novos internados na UCI: ",linhaDataInicial,linhaDataFinal,arr,opcao);
                break;
            case 4 :
                imprimirUmDado("Novas mortes: ",linhaDataInicial,linhaDataFinal,arr,opcao);
                break;
            case 5 :
                for (int i = linhaDataInicial; i <= linhaDataFinal; i++) {
                    System.out.println("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
                    System.out.println("Novos infetados: " + arr[i][0]);
                    System.out.println("Novos hospitalizados: " + arr[i][1]);
                    System.out.println("Novos internados na UCI: " + arr[i][2]);
                    System.out.println("Novas mortes: " + arr[i][3]);
                    System.out.println();
                    c.add(Calendar.DATE, 1);
                }
                break;
        }


    }

    public static void imprimirUmDado(String dado, int linhaDataInicial, int linhaDataFinal,int[][] arr,int opcao){
        for (int i = linhaDataInicial; i <= linhaDataFinal; i++) {
            System.out.println("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
            System.out.println(dado + arr[i][opcao-1]);
            System.out.println();
            c.add(Calendar.DATE, 1);
        }
    }

    public static String lerQuaisDadosApresentar(){
        System.out.println("Deseja visualizar (escreva o número da opção):");
        System.out.println("1 - Número de novos infetado");
        System.out.println("2 - Número de novos hospitalizados");
        System.out.println("3 - Número de novos internados na UCI");
        System.out.println("4 - Número de novos mortos");
        System.out.println("5 - Todos os dados");

        return lerOpcaoDados();
    }

    // verificações
    public static String lerFicheiro(){
        String ficheiroParaLer = ler.nextLine();
        while (!ficheiroParaLer.equals("1") && !ficheiroParaLer.equals("2") && !ficheiroParaLer.equals("3") && !ficheiroParaLer.equals("4")){
            System.out.println("Opção não existe, por favor escreva novamente.");
            ficheiroParaLer = ler.nextLine();
        }
        return ficheiroParaLer;
    }

    public static int verificarDataInicio (int linhaDataInicio,int totalLinhas, Date [] matrizDatas){
        while (linhaDataInicio==0 || linhaDataInicio==totalLinhas-1 || linhaDataInicio==-1){
            if (linhaDataInicio==0){
                System.out.println("Não é possível calcular dados acerca da primeira data presente no ficheiro (é necessária a data anterior). Por favor, insira a data inicial novamente.");
            }else{
                if (linhaDataInicio==totalLinhas-1){
                    System.out.println("A data inicial não pode ser a última data presente no ficheiro. Por favor, insira a data inicial novamente.");
                }else{
                    System.out.println("Esta data não existe no ficheiro. Por favor, insira a data inicial novamente.");
                }
            }
            Date DataInicio = lerDatasInseridas();
            linhaDataInicio = VerLinhaCorrespondente(DataInicio, matrizDatas,totalLinhas);
        }
        return linhaDataInicio;
    }

    public static int verificarDataInicioAnaliseTotal (int linhaDataInicio,int totalLinhas, Date [] matrizDatas){
        while (linhaDataInicio==totalLinhas-1 || linhaDataInicio==-1){
            if (linhaDataInicio==totalLinhas-1){
                System.out.println("A data inicial não pode ser a última data presente no ficheiro. Por favor, insira a data inicial novamente.");
            }else{
                System.out.println("Esta data não existe no ficheiro. Por favor, insira a data inicial novamente.");
            }
            Date DataInicio = lerDatasInseridas();
            linhaDataInicio = VerLinhaCorrespondente(DataInicio, matrizDatas,totalLinhas);
        }
        return linhaDataInicio;
    }

    public static int verificarDataFim (int linhaDataFim,int totalLinhas, Date [] matrizDatas,int linhaDataInicio){

        while (linhaDataFim<=linhaDataInicio || linhaDataFim==-1) {
            if (linhaDataFim == -1){
                System.out.println("Esta data não existe no ficheiro. Por favor, insira a data final novamente.");
            }else if (linhaDataFim<linhaDataInicio){
                System.out.println("A data final é anterior à data inicial. Por favor, insira a data final novamente.");
            }else{
                System.out.println("A data final é igual à data inicial. Por favor, insira a data final novamente.");
            }

            Date DataFim= lerDatasInseridas();
            linhaDataFim = VerLinhaCorrespondente(DataFim, matrizDatas,totalLinhas);
        }
        return linhaDataFim;
    }

    public static String lerFuncinalidade(){
        String funcionalidade = ler.nextLine();
        while (!funcionalidade.equals("1") && !funcionalidade.equals("2") && !funcionalidade.equals("3") && !funcionalidade.equals("4") && !funcionalidade.equals("5")) {
            System.out.println("Funcionalidade não existe, por favor escreva novamente.");
            funcionalidade = ler.nextLine();
        }
        return funcionalidade;
    }

    public static String lerOpcaoAnalise(){
        String opcao = ler.nextLine();
        while (!opcao.equals("1") && !opcao.equals("2")&&!opcao.equals("3")) {
            System.out.println("Opção não existe, por favor escreva novamente.");
            opcao = ler.nextLine();
        }
        return opcao;
    }

    public static String lerResolucaoTemporal(){
        String resTemporal = ler.nextLine();
        while (!resTemporal.equals("diaria") && !resTemporal.equals("semanal") && !resTemporal.equals("mensal")) {
            System.out.println("Resolução temporal desconhecida, por favor escreva novamente.");
            resTemporal = ler.nextLine();
        }
        return resTemporal;
    }

    public static String lerOpcaoDados(){
        String opcao = ler.nextLine();
        while (!opcao.equals("1") && !opcao.equals("2") && !opcao.equals("3") && !opcao.equals("4") && !opcao.equals("5")){
            System.out.println("Opção não existe, por favor escreva novamente.");
            opcao = ler.nextLine();
        }
        return opcao;
    }

    public static int lerOpcaoDeComparacao(){
        int opcaoDeComparacao = ler.nextInt();
        while (opcaoDeComparacao!=1 && opcaoDeComparacao!=2 && opcaoDeComparacao!=3){
            System.out.println("Opção não existe, por favor escreva novamente.");
            opcaoDeComparacao = ler.nextInt();
        }
        return opcaoDeComparacao;
    }

    public static String lerOpcaoPrevisao(){
        String opcao = ler.nextLine();
        while (!opcao.equals("1") && !opcao.equals("2")&&!opcao.equals("3")){
            System.out.println("Opção não existe, por favor escreva novamente.");
            opcao = ler.nextLine();
        }
        return opcao;
    }

    public static String lerEstado(){
        String estado = ler.nextLine();
        while (!estado.equals("1") && !estado.equals("2") && !estado.equals("3") && !estado.equals("4")){
            System.out.println("Opção não existe, por favor escreva novamente.");
            estado = ler.nextLine();
        }
        return estado;
    }
    //MODULOS PARA O ANALISIS MENSAL
    public static void PreencherDadosAnaliseMes(int[][]MatrizDados, int[][] MatrizOriginal, int contlinha, int L1, int L2){
        int [][] MatrizCalculo = new int[2][5];
        for (int c=0; c<5; c++){
            MatrizCalculo[0][c]= MatrizOriginal[L1][c];
            MatrizCalculo[1][c]= MatrizOriginal[L2][c];
        }
        for(int d=0; d<5; d++){
            MatrizDados[contlinha][d]=MatrizCalculo[1][d]-MatrizCalculo[0][d];
        }
    }

    public static int ContadorMeses (int ContAnos){
        int ContMes=0;
        if (ContAnos==1){
            ContMes=(c2.get(Calendar.MONTH)-c.get(Calendar.MONTH))+1;
        }
        if(ContAnos>1){
            ContMes=(13-((c.get(Calendar.MONTH))+1));
            ContMes= ContMes+((c2.get(Calendar.MONTH))+1);
            ContAnos=ContAnos-2;
            if(ContAnos>0){
                ContMes=ContMes+(12*ContAnos);
            }
        }
        return ContMes;
    }

    public static int[][] AnalisarDadosMes(int ContMeses, int linhaDataInicio, int[][]matrizRegistos, Date[]matrizDatas){
        int[][] MatrizNovosCasos = new int[ContMeses][COLUNAS_REGISTOS];
        int linhaDataInicio2 = linhaDataInicio;
        int contlinha = 0;
        linhaDataInicio2 = VerificadorLinha(linhaDataInicio2);
        for (int contMeses = 0; contMeses < ContMeses; contMeses++) {
            int linhaDataAnterior=linhaDataInicio2-1;
            linhaDataInicio2 = LeitordeLinhaUltDiaMes(linhaDataInicio2);
            PreencherDadosAnaliseMes(MatrizNovosCasos, matrizRegistos, contlinha, linhaDataAnterior, linhaDataInicio2);
            contlinha++;
            linhaDataInicio2++;
        }
        c.setTime(matrizDatas[linhaDataInicio]);
        if (c.get(Calendar.DAY_OF_MONTH)!=1){
            c.set(Calendar.DAY_OF_MONTH,1);
            c.add(Calendar.MONTH,1);
        }
        return MatrizNovosCasos;
    }

    public static int VerificadorLinha (int linhaData) {
        if (c.get(Calendar.DAY_OF_MONTH) != 1) {
            linhaData = linhaData + (c.getActualMaximum(Calendar.DAY_OF_MONTH) - c.get(Calendar.DAY_OF_MONTH)) + 1;
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.MONTH, 1);
            return linhaData;
        }
        return linhaData;
    }

    public static int LeitordeLinhaUltDiaMes(int linhaData){
        linhaData=linhaData+c.getActualMaximum(Calendar.DAY_OF_MONTH)-1;
        c.add(Calendar.MONTH,1);
        return linhaData;
    }

    public static void imprimirRegistosMensais (int [][]MatrizNovosCasos, int ContMeses){
        for (int i = 0; i < ContMeses; i++) {
            System.out.println("Mes "  + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
            System.out.println("Novos infetados: " + MatrizNovosCasos[i][1]);
            System.out.println("Novos Hospitalizados: " + MatrizNovosCasos[i][2]);
            System.out.println("Novos internados UCI: " + MatrizNovosCasos[i][3]);
            System.out.println("Novas mortes: " + MatrizNovosCasos[i][4]);
            System.out.println();
            c.add(Calendar.MONTH, 1);
        }
    }

    public static void imprimirRegistoSingularMensais (int [][] MatrizNovoCaso, int ContMeses, int opcao) {
        for (int i = 0; i < ContMeses; i++) {
            System.out.println("Mes " + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
            switch (opcao) {
                case 1:
                    System.out.println("Novos infetados: " + MatrizNovoCaso[i][1]);
                    break;
                case 2:
                    System.out.println("Novos Hospitalizados: " + MatrizNovoCaso[i][2]);
                    break;
                case 3:
                    System.out.println("Novos internados UCI: " + MatrizNovoCaso[i][3]);
                    break;
                case 4:
                    System.out.println("Novas mortes: " + MatrizNovoCaso[i][4]);
                    break;
            }
        }
        System.out.println();
        c.add(Calendar.MONTH, 1);
    }
    //MODULOS PARA A COMPARAÇÃO
    public static int [] lerDadosIntervalosDeTempo (int totalLinhas, Date[] matrizDatas){
        int linhaDataInicioIntervalo1, linhaDataFimIntervalo1, linhaDataInicioIntervalo2, linhaDataFimIntervalo2;
        int [] linhaDatas= new int[4];
        do {
            System.out.println("Insira a data de inicio do 1º intervalo de tempo da comparação no formato DD-MM-AAAA");
            Date DataInicioIntervalo1 = lerDatasInseridas();
            linhaDataInicioIntervalo1 = VerLinhaCorrespondente(DataInicioIntervalo1, matrizDatas, totalLinhas);
            linhaDataInicioIntervalo1=verificarDataInicio(linhaDataInicioIntervalo1,totalLinhas,matrizDatas);

            System.out.println("Insira a data de fim da comparação do 1º intervalo de tempo no formato DD-MM-AAAA");
            Date DataFimIntervalo1 = lerDatasInseridas();
            linhaDataFimIntervalo1 = VerLinhaCorrespondente(DataFimIntervalo1, matrizDatas, totalLinhas);
            linhaDataFimIntervalo1=verificarDataFim(linhaDataFimIntervalo1,totalLinhas,matrizDatas,linhaDataInicioIntervalo1);

            System.out.println("Insira a data de inicio do 2º intervalo de tempo da comparação no formato DD-MM-AAAA");
            Date DataInicioIntervalo2 = lerDatasInseridas();
            linhaDataInicioIntervalo2 = VerLinhaCorrespondente(DataInicioIntervalo2, matrizDatas, totalLinhas);
            linhaDataInicioIntervalo2=verificarDataInicio(linhaDataInicioIntervalo2,totalLinhas,matrizDatas);

            System.out.println("Insira a data de fim da comparação do 2º intervalo de tempo no formato DD-MM-AAAA");
            Date DataFimIntervalo2 = lerDatasInseridas();
            linhaDataFimIntervalo2 = VerLinhaCorrespondente(DataFimIntervalo2, matrizDatas, totalLinhas);
            linhaDataFimIntervalo2 = verificarDataFim(linhaDataFimIntervalo2,totalLinhas,matrizDatas,linhaDataInicioIntervalo2);

            if (((linhaDataInicioIntervalo1 >= linhaDataInicioIntervalo2 || linhaDataFimIntervalo1 >= linhaDataFimIntervalo2 || linhaDataInicioIntervalo2 <= linhaDataFimIntervalo1)) && !((linhaDataFimIntervalo1 > linhaDataFimIntervalo2 && linhaDataInicioIntervalo1 > linhaDataInicioIntervalo2))) {
                System.out.println("Não pode existir qualquer interseção entre os dois intervalos de tempo");
            } else {
                if (linhaDataInicioIntervalo1 > linhaDataInicioIntervalo2) {
                    System.out.println("O intervalo 1 tem de ser anterior ao intervalo 2");
                }
            }
        } while (linhaDataInicioIntervalo1 >= linhaDataInicioIntervalo2 || linhaDataFimIntervalo1 >= linhaDataFimIntervalo2 || linhaDataInicioIntervalo2 <= linhaDataFimIntervalo1);
        linhaDatas[0]=linhaDataInicioIntervalo1;
        linhaDatas[1]=linhaDataFimIntervalo1;
        linhaDatas[2]=linhaDataInicioIntervalo2;
        linhaDatas[3]=linhaDataFimIntervalo2;
        return linhaDatas;
    }
    public static int diasTotais(int [] linhasDatas){
        int diasIntervalo1 = linhasDatas[1] - linhasDatas[0];
        int diasIntervalo2 = linhasDatas[3] - linhasDatas[2];
        int diastotal;
        String nome;

        if (diasIntervalo1 > diasIntervalo2) {
            diastotal = diasIntervalo2;
        } else {
            diastotal = diasIntervalo1;
        }
        diastotal++;
        return diastotal;
    }
    public static int[][] resultadosComparacaoIntervalosAcumulados(int [] linhasDatas, int [][] matrizRegistosDiarios, int diasTotal) {

        int [][] resultado=new int[diasTotal+1][4];
        for (int i = 0; i < diasTotal; i++) {
            for (int j = 0; j < 4; j++) {
                resultado[i][j] = (matrizRegistosDiarios[linhasDatas[2] + i][j] - matrizRegistosDiarios[linhasDatas[0] + i][j]);
            }
        }
        return resultado;
    }
    public static  double [][] calculoDaMedia(int [][] matrizRegistosDiarios, int diastotal, int linhaDataInicioIntervalo, int verificacao){
        float media;
        double [][] mediaArr=new double[2][4];
        for(int j = 0; j < 4; j++) {
            media = 0;
            if (verificacao==0){
                for (int i = 0; i < diastotal; i++) {
                    media = media + matrizRegistosDiarios[linhaDataInicioIntervalo + i][j];
                }
            }else{
                for (int i = 0; i < diastotal; i++) {
                    media = media + matrizRegistosDiarios[linhaDataInicioIntervalo + i][j+1];
                }
            }
            media/= diastotal;
            mediaArr [0][j] = media;
            if (verificacao==0){
                mediaArr[1][j] = desvioPadrao(media, diastotal, matrizRegistosDiarios, linhaDataInicioIntervalo, j);
            }else{
                mediaArr[1][j] = desvioPadraoTotal(media, diastotal, matrizRegistosDiarios, linhaDataInicioIntervalo, j);
            }
        }
        return mediaArr;
    }
    public static double [][] MediaEDesvioPadraoResultados(int[][] resultados,int diastotal){
        double media;
        double [][] mediaArr=new double[2][4];
        for(int j=0;j<4;j++){
            media=0;
            for (int i = 0; i < diastotal; i++) {
                media = media + resultados[i][j];
            }
            media/=diastotal;
            mediaArr[0][j]=media;
            mediaArr[1][j] = desvioPadraoDosResultados(media, diastotal, resultados, 0, j);
        }
        return mediaArr;
    }
    public static double desvioPadraoDosResultados(double media, int diastotal, int [][]matrizRegistosDiarios, int linhaDataInicioIntervalo, int j) {
        float numerador = 0;
        float divisao;
        double rdesvioPadrao;
        for (int i = 0; i < diastotal; i++) {
            numerador += Math.pow(((matrizRegistosDiarios[linhaDataInicioIntervalo + i][j]) - media), 2);
        }
        divisao = numerador / diastotal;
        rdesvioPadrao = Math.sqrt(divisao);
        return rdesvioPadrao;
    }

    public static double desvioPadraoTotal(float media, int diastotal, int [][]matrizRegistosDiarios, int linhaDataInicioIntervalo, int j) {
        float numerador = 0;
        float divisao;
        double rdesvioPadrao;
        for (int i = 0; i < diastotal; i++) {
            numerador += Math.pow(((matrizRegistosDiarios[linhaDataInicioIntervalo + i][j+1]) - media), 2);
        }
        divisao = numerador / diastotal;
        rdesvioPadrao = Math.sqrt(divisao);
        return rdesvioPadrao;
    }
    public static double desvioPadrao(float media, int diastotal, int [][]matrizRegistosDiarios, int linhaDataInicioIntervalo, int j) {
        float numerador = 0;
        float divisao;
        double rdesvioPadrao;
        for (int i = 0; i < diastotal; i++) {
            numerador += Math.pow(((matrizRegistosDiarios[linhaDataInicioIntervalo + i][j]) - media), 2);
        }
        divisao = numerador / diastotal;
        rdesvioPadrao = Math.sqrt(divisao);
        return rdesvioPadrao;
    }

    public static int preencherMatrizRegistosTotal(String file,String[] matrizDatasSTotal,int[][] matrizRegistosTotal) throws FileNotFoundException{
        Scanner in = new Scanner(new File(file));
        String legenda = in.nextLine();
        int numLinha = 0;

        while (in.hasNext()){
            String[] linha = (in.nextLine()).split(",");
            matrizDatasSTotal[numLinha] = linha[0];
            for (int numColuna = 0; numColuna < COLUNAS_REGISTOS; numColuna++) {
                matrizRegistosTotal[numLinha][numColuna] = Integer.parseInt(linha[numColuna + 1]); // conversão para int
            }
            numLinha++;
        }

        in.close();
        return numLinha;
    }
    public static void transformarMatrizStringParaDatesTotal(Date [] matrizDatasTotal,String [] matrizDatasSTotal,int linhas){
        try {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            for (int i=0;i<linhas;i++) {
                matrizDatasTotal[i] = df.parse(matrizDatasSTotal[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static int[][] resultadosComparacaoIntervalosTotal(int [] linhasDatas,int [][] matrizRegistosDiarios, int diasTotal) {

        int [][] resultado=new int[diasTotal+1][4];
        for (int i = 0; i < diasTotal; i++) {
            for (int j = 0; j < 4; j++) {
                resultado[i][j] = (matrizRegistosDiarios[linhasDatas[2] + i][j+1] - matrizRegistosDiarios[linhasDatas[0] + i][j+1]);
            }
        }
        return resultado;
    }

    public static int LerDadosApresentarComparacoes(){
        System.out.println("Deseja fazer comparações de que dados?");
        System.out.println("1 - Infetados");
        System.out.println("2 - Hospitalizados");
        System.out.println("3 - Internados UCI");
        System.out.println("4 - Mortes");
        System.out.println("5 - Todos os dados");

        int resposta= ler.nextInt();
        while (resposta != 1 && resposta != 2 && resposta != 3 && resposta != 4 && resposta != 5){
            System.out.println("A opção não existe. Digite de novo");
            resposta = ler.nextInt();
        }
        return resposta;
    }

    public static void imprimirComparacoesInfetados(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos, int Verificacao){
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            System.out.println();
            System.out.println("Comparação do total de ativos infetados");
            System.out.println();
            for (int i = 0; i < dias; i++) {
                System.out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[0] + i][1]);
                System.out.print("   |   ");
                System.out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[2] + i][1]);
                System.out.print("   |   ");
                System.out.printf("Diferença %17d\n", resultados[i][0]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }else{
            System.out.println();
            System.out.println("Comparação do total de novos infetados");
            System.out.println();
            for (int i = 0; i < dias; i++) {
                System.out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[0] + i][0]);
                System.out.print("   |   ");
                System.out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[2] + i][0]);
                System.out.print("   |   ");
                System.out.printf("Diferença %17d\n", resultados[i][0]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }
    }
    public static void imprimirMediaEDesvioPadraoInfetados(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe ){
        System.out.println();
        System.out.printf("Média %21.4f",MediaEDesvioPadrao1[0][0]);
        System.out.print("   |   ");
        System.out.printf("Média %21.4f",MediaEDesvioPadrao2[0][0]);
        System.out.print("   |   ");
        System.out.printf("Média %21.4f",mEDDosRe[0][0]);


        System.out.println();
        System.out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao1[1][0]);
        System.out.print("   |   ");
        System.out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao2[1][0]);
        System.out.print("   |   ");
        System.out.printf("Desvio Padrão %13.4f\n",mEDDosRe[1][0]);

    }

    public static void imprimirComparacoesHospitalizados(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos, int Verificacao){
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            System.out.println();
            System.out.println("Comparação do total de ativos hospitalizados");
            System.out.println();
            for (int i = 0; i < dias; i++) {
                System.out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[0] + i][2]);
                System.out.print("   |   ");
                System.out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[2] + i][2]);
                System.out.print("   |   ");
                System.out.printf("Diferença %17d\n", resultados[i][1]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }else{
            System.out.println();
            System.out.println("Comparação do total de novos casos hospitalizados");
            System.out.println();
            for (int i = 0; i < dias; i++) {
                System.out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[0] + i][1]);
                System.out.print("   |   ");
                System.out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[2] + i][1]);
                System.out.print("   |   ");
                System.out.printf("Diferença %17d\n", resultados[i][1]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }
    }
    public static void imprimirMediaEDesvioPadraoHospitalizados(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe ){
        System.out.println();
        System.out.printf("Média %21.4f",MediaEDesvioPadrao1[0][1]);
        System.out.print("   |   ");
        System.out.printf("Média %21.4f",MediaEDesvioPadrao2[0][1]);
        System.out.print("   |   ");
        System.out.printf("Média %21.4f",mEDDosRe[0][1]);


        System.out.println();
        System.out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao1[1][1]);
        System.out.print("   |   ");
        System.out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao2[1][1]);
        System.out.print("   |   ");
        System.out.printf("Desvio Padrão %13.4f\n",mEDDosRe[1][1]);

    }

    public static void imprimirComparacoesUCI(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos, int Verificacao){
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            System.out.println();
            System.out.println("Comparação do total de ativos internados UCI");
            System.out.println();
            for (int i = 0; i < dias; i++) {
                System.out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[0] + i][3]);
                System.out.print("   |   ");
                System.out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[2] + i][3]);
                System.out.print("   |   ");
                System.out.printf("Diferença %17d\n", resultados[i][2]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }else{
            System.out.println();
            System.out.println("Comparação do total de novos casos de internados UCI");
            System.out.println();
            for (int i = 0; i < dias; i++) {
                System.out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[0] + i][2]);
                System.out.print("   |   ");
                System.out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[2] + i][2]);
                System.out.print("   |   ");
                System.out.printf("Diferença %17d\n", resultados[i][2]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }
    }
    public static void imprimirMediaEDesvioPadraoUCI(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe ){
        System.out.println();
        System.out.printf("Média %21.4f",MediaEDesvioPadrao1[0][2]);
        System.out.print("   |   ");
        System.out.printf("Média %21.4f",MediaEDesvioPadrao2[0][2]);
        System.out.print("   |   ");
        System.out.printf("Média %21.4f",mEDDosRe[0][2]);


        System.out.println();
        System.out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao1[1][2]);
        System.out.print("   |   ");
        System.out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao2[1][2]);
        System.out.print("   |   ");
        System.out.printf("Desvio Padrão %13.4f\n",mEDDosRe[1][2]);

    }

    public static void imprimirComparacoesMortes(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos, int Verificacao){
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            System.out.println();
            System.out.println("Comparação do total de mortes diárias");
            System.out.println();
            for (int i = 0; i < dias; i++) {
                System.out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[0] + i][4]);
                System.out.print("   |   ");
                System.out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[2] + i][4]);
                System.out.print("   |   ");
                System.out.printf("Diferença %17d\n", resultados[i][3]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }else{
            System.out.println();
            System.out.println("Comparação de novas mortes");
            System.out.println();
            for (int i = 0; i < dias; i++) {
                System.out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[0] + i][3]);
                System.out.print("   |   ");
                System.out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                System.out.printf("%14d", matrizRegistos[linhasDatas[2] + i][3]);
                System.out.print("   |   ");
                System.out.printf("Diferença %17d\n", resultados[i][3]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }
    }
    public static void imprimirMediaEDesvioPadraoMortes(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe ){
        System.out.println();
        System.out.printf("Média %21.4f",MediaEDesvioPadrao1[0][3]);
        System.out.print("   |   ");
        System.out.printf("Média %21.4f",MediaEDesvioPadrao2[0][3]);
        System.out.print("   |   ");
        System.out.printf("Média %21.4f",mEDDosRe[0][3]);


        System.out.println();
        System.out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao1[1][3]);
        System.out.print("   |   ");
        System.out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao2[1][3]);
        System.out.print("   |   ");
        System.out.printf("Desvio Padrão %13.4f\n",mEDDosRe[1][3]);

    }



    //Métodos da parte 2.3 do trabalho
    public static double[][] lerMatrizTransicao (String fileInTotal) throws FileNotFoundException{
        Scanner in = new Scanner(new File(fileInTotal));
        double[][] matrizTransicao = new double[LINHAS_MATRIZ_TRANSICAO][COLUNAS_MATRIZ_TRANSICAO];
        int numLinha = 0, numColuna = 0;

        while (in.hasNext()){
            String linha = in.nextLine();
            if (!linha.equals("")){
                String[] linhaSplit = linha.split("=");
                matrizTransicao[numLinha][numColuna] = Double.parseDouble(linhaSplit [1]);
                numColuna++;
            }else{
                numLinha++;
                numColuna = 0;
            }
        }

        in.close();
        return matrizTransicao;
    }

    public static double[][] criarMatrizSemEstadoAbsorvente(double[][] matrizTransicao) {
        double[][] matrizReduzida = new double[LINHAS_MATRIZES][COLUNAS_MATRIZES];

        for (int i = 0; i < LINHAS_MATRIZES; i++) {
            for (int j = 0; j < COLUNAS_MATRIZES; j++) {
                matrizReduzida[i][j] = matrizTransicao[i][j];
            }
        }
        return matrizReduzida;
    }

    public static double[][] subtrairDuasMatrizes(double[][] arr1, double[][] arr2) {
        double[][] matrizResultado = new  double[arr1.length][arr1[0].length];
        for (int i = 0; i < matrizResultado.length; i++) {
            for (int j = 0; j < matrizResultado[0].length; j++) {
                matrizResultado[i][j] = arr2[i][j] - arr1[i][j];
            }
        }
        return matrizResultado;
    }

    public static double[][] criarMatrizIdentidade() {
        double[][] identidade = new double[LINHAS_MATRIZES][COLUNAS_MATRIZES];
        for (int i = 0; i < LINHAS_MATRIZES; i++) {
            identidade[i][i] = 1;
        }
        return identidade;
    }

    public static double[][] calculoDaInversa(double[][] matrizOriginal){
        double[][] matrizTriangularSuperior = new double[matrizOriginal.length][matrizOriginal[0].length];
        double[][] matrizTriangularInferior = new double[matrizOriginal.length][matrizOriginal[0].length];
        double[][] inversaMatrizTriangularInferior = new double[LINHAS_MATRIZES][COLUNAS_MATRIZES];
        double[][] inversaMatrizTriangularSuperior = new double[LINHAS_MATRIZES][COLUNAS_MATRIZES];

        // preencher a matriz U com os valores conhecidos
        preencherDiagonalPrincipalCom1(matrizTriangularSuperior);

        //cáculo das matrizes L e U
        int contador = 0, numColuna=0;
        while (contador < matrizOriginal.length){
            calcularColunaMatrizTriangularInferior(contador,numColuna,matrizOriginal,matrizTriangularSuperior,matrizTriangularInferior);
            calcularLinhaMatrizTriangularSuperior(contador,numColuna,matrizOriginal,matrizTriangularSuperior,matrizTriangularInferior);
            contador++;
            numColuna++;

        }

        //cáculo das matrizes inversas de L e U
        double[][]identidade =  criarMatrizIdentidade();
        int iInferior = 0;
        int iSuperior = LINHAS_MATRIZES -1;
        for(int coluna = 0; coluna < LINHAS_MATRIZES ;coluna++) {
            inversaMatrizTriangularInferior(matrizTriangularInferior, identidade, coluna, inversaMatrizTriangularInferior, iInferior);
            iInferior++;
        }
        preencherDiagonalPrincipalCom1(inversaMatrizTriangularSuperior);
        for (int coluna = 1; coluna < LINHAS_MATRIZES ; coluna ++){
            inversaMatrizTriangularSuperior(matrizTriangularSuperior, identidade, coluna, inversaMatrizTriangularSuperior, iSuperior);
        }

        //cálculo da matriz inversa
        return multiplicarMatrizes(inversaMatrizTriangularSuperior,inversaMatrizTriangularInferior);
    }

    public static void calcularColunaMatrizTriangularInferior(int contador, int numColuna, double[][] matrizOriginal, double[][] matrizU, double[][] matrizL){
        for (int i = contador; i < matrizU.length; i++) {
            matrizL[i][numColuna] = (calcularNumerador(numColuna,i,matrizOriginal,matrizU,matrizL))/ matrizU[numColuna][numColuna];

        }

    }

    public static double calcularNumerador( int numColuna, int numLinha, double[][] matrizOriginal, double[][] matrizU, double[][] matrizL ){
        double soma = matrizOriginal[numLinha][numColuna];

        for (int i = 0; i < matrizOriginal.length; i++) {
            if (matrizU[i][numColuna] != 0 && i!=numColuna){
                soma = soma - (matrizL[numLinha][i]*matrizU[i][numColuna]);
            }
        }

        return soma;
    }

    public static void calcularLinhaMatrizTriangularSuperior(int contador,int numLinha,double[][] matrizOriginal,double[][] matrizU, double[][] matrizL){
        for (int i = contador+1; i < matrizU.length; i++) {
            matrizU[numLinha][i] = (calcularNumerador(i,numLinha,matrizOriginal,matrizU,matrizL))/matrizL[numLinha][numLinha];
        }
    }

    public static void inversaMatrizTriangularInferior(double[][]matrizTrinagularInferior, double[][]identidade, int coluna, double[][] inversaMatrizTriangularInferior, int i){
        int j;
        double soma;
        inversaMatrizTriangularInferior[i][coluna] = ((identidade[i][coluna])/matrizTrinagularInferior[i][i]);
        i++;
        while (i < LINHAS_MATRIZES) {
            soma = 0;
            j = 0;
            while (j < i) {
                soma += (inversaMatrizTriangularInferior[j][coluna] * matrizTrinagularInferior[i][j]);
                j++;
            }
            soma = identidade[i][coluna] - soma;
            soma /= matrizTrinagularInferior[i][j];
            inversaMatrizTriangularInferior[i][coluna] = soma;
            i++;
        }
    }

    public static void preencherDiagonalPrincipalCom1(double[][] inversaMatrizTriangularSuperior){
        for (int i = 0; i < LINHAS_MATRIZES; i++) {
            inversaMatrizTriangularSuperior[i][i] = 1;
        }
    }

    public static void inversaMatrizTriangularSuperior(double[][]matrizTrinagularSuperior, double[][]identidade, int coluna, double[][] inversaMatrizTriangularSuperior, int i){
        int j;
        double soma;
        i--;
        while(i >= 0){
            soma = 0;
            j = LINHAS_MATRIZES - 1;
            while (j > i) {
                soma += (inversaMatrizTriangularSuperior[j][coluna] * matrizTrinagularSuperior[i][j]);
                j--;
            }
            inversaMatrizTriangularSuperior[i][coluna] = identidade[i][coluna] - soma;
            i--;
        }
    }

    public static double[][] multiplicarMatrizes (double[][] arr1, double [][] arr2){
        double[][] matrizResultado = new double[arr1.length][arr2[0].length];

        for (int i = 0; i < matrizResultado.length; i++) {
            for (int j = 0; j < matrizResultado[0].length; j++) {
                matrizResultado[i][j] = calcularElemento(arr1[i],criarArrayColuna(arr2,j));
            }
        }
        return matrizResultado;
    }

    public static double[] mutiplicarMatrizPorVetor (double [][] arr1, double[] arr2){
        double[] matrizResultado = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            matrizResultado [i] = calcularElemento(arr1[i],arr2);
        }
        return matrizResultado;
    }

    public static double[] mutiplicarVetorPorMatriz (double [] arr1, double[][] arr2){
        double[] matrizResultado = new double[arr2[0].length];
        for (int i = 0; i < arr1.length; i++) {
            matrizResultado [i] = calcularElemento(arr1,criarArrayColuna(arr2,i));
        }
        return matrizResultado;
    }

    public static double[] criarArrayColuna (double[][] arr1, int numColuna){
        double[] coluna = new double[arr1[0].length];
        for (int i = 0; i < arr1[0].length; i++) {
            coluna [i] = arr1[i][numColuna];
        }
        return coluna;
    }

    public static double calcularElemento(double[] arr1, double [] arr2) {
        double soma = 0;
        for (int k = 0; k < arr2.length ; k++) {
            soma = soma + (arr1[k] * arr2[k] );
        }

        return soma;
    }

    //
    public static double[][] copiarMatriz (double[][] arr1) {
        double [][] arrCopia = new double[arr1.length][arr1[0].length];
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[0].length; j++) {
                arrCopia[i][j] = arr1[i][j];
            }
        }
        return arrCopia;
    }

    public static double[] converterVetorDeIntParaDouble (int[] arr1){
        double[] vetorConvertido = new double[arr1.length];
        for (int i = 0; i < vetorConvertido.length; i++) {
            vetorConvertido[i] = arr1[i];
        }
        return vetorConvertido;
    }

    public static void imprimirMatrizPevisaoDeDados(double[] arr1){
        System.out.println("Dados previstos para o dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH)+1) + "/" + (c2.get(Calendar.YEAR)));
        System.out.printf("Total de não infetados: %.1f\n",arr1[0]);
        System.out.printf("Total de infetados: %.1f\n",arr1[1]);
        System.out.printf("Total de hospitalizados: %.1f\n",arr1[2]);
        System.out.printf("Total de internados na UCI: %.1f\n",arr1[3]);
        System.out.printf("Total de mortes: %.1f\n",arr1[4]);
    }

    //Nao interativo
    public static Date lerDatasInseridasNaoInterativoAcumulados(String dataRecebida) {
        try {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date dt = df.parse(dataRecebida);
            return dt;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Date();
        }
    }
    public static void imprimirRegistosDiariosNaoInterativo (int [][] arr, int linhaDataInicial, int linhaDataFinal, String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        for (int i = linhaDataInicial; i <= linhaDataFinal; i++) {
            out.println("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
            out.println("Novos infetados: " + arr[i][0]);
            out.println("Novos hospitalizados: " + arr[i][1]);
            out.println("Novos internados na UCI: " + arr[i][2]);
            out.println("Novas mortes: " + arr[i][3]);
            out.println();
            c.add(Calendar.DATE, 1);
        }
        out.close();
    }
    public static void comparacaoSemanalNaoInterativo(int linhainicio, int linhafim, int[][] registos, String file) throws IOException {
        int [][] dadosSemanais= new int[1000][4];
        int linhamedia = linhainicio + 7;
        int comparacao;
        int linha=0;
        int coluna;
        while (linhamedia <= linhafim) {
            coluna=0;
            for (int i = 1; i <= 4; i++) {
                comparacao = (registos[linhamedia - 1][i]) - (registos[linhainicio - 1][i]);
                dadosSemanais[linha][coluna]=comparacao;
                coluna++;
            }
            linhamedia += 7;
            linhainicio += 7;
            linha++;
        }
        impressaoComparacaoSemanalNaoInterativo(dadosSemanais,file,linha);
    }
    public static void impressaoComparacaoSemanalNaoInterativo(int [][] comparacao, String file,int linha) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        for(int j=0; j<linha;j++){
            out.println("Semana de " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
            out.println("Novos infetados: " + comparacao[j][0]);
            out.println("Novos Hospitalizados: " + comparacao[j][1]);
            out.println("Novos internados UCI: " + comparacao[j][2]);
            out.println("Novas mortes: " + comparacao[j][3]);
            out.println();
            c.add(Calendar.DATE, 7);
        }
        out.close();

    }
    public static void imprimirRegistosMensaisNaoInterativo(int [][]MatrizNovosCasos, int ContMeses, String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        for (int i = 0; i < ContMeses; i++) {
            out.println("Mes "  + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
            out.println("Novos infetados: " + MatrizNovosCasos[i][1]);
            out.println("Novos Hospitalizados: " + MatrizNovosCasos[i][2]);
            out.println("Novos internados UCI: " + MatrizNovosCasos[i][3]);
            out.println("Novas mortes: " + MatrizNovosCasos[i][4]);
            out.println();
            c.add(Calendar.MONTH, 1);
        }
        out.close();
    }
    public static int [] lerDadosIntervalosDeTempoNaoInterativo (int totalLinhas, Date[] matrizDatas, Date di1,Date df1,Date di2,Date df2){
        int linhaDataInicioIntervalo1, linhaDataFimIntervalo1, linhaDataInicioIntervalo2, linhaDataFimIntervalo2;
        int [] linhaDatas= new int[4];
        linhaDataInicioIntervalo1 = VerLinhaCorrespondente(di1, matrizDatas, totalLinhas);
        linhaDataFimIntervalo1 = VerLinhaCorrespondente(df1, matrizDatas, totalLinhas);
        linhaDataInicioIntervalo2 = VerLinhaCorrespondente(di2, matrizDatas, totalLinhas);
        linhaDataFimIntervalo2 = VerLinhaCorrespondente(df2, matrizDatas, totalLinhas);

        if ((linhaDataInicioIntervalo1<linhaDataFimIntervalo1 && linhaDataInicioIntervalo2<linhaDataFimIntervalo2 && linhaDataInicioIntervalo1 < linhaDataInicioIntervalo2 && linhaDataFimIntervalo1 < linhaDataFimIntervalo2)||(linhaDataFimIntervalo1!=-1 && linhaDataFimIntervalo2!=-1 && linhaDataInicioIntervalo1!=-1 && linhaDataInicioIntervalo2!=-1)){
            linhaDatas[0]=linhaDataInicioIntervalo1;
            linhaDatas[1]=linhaDataFimIntervalo1;
            linhaDatas[2]=linhaDataInicioIntervalo2;
            linhaDatas[3]=linhaDataFimIntervalo2;
            return linhaDatas;
        }
        linhaDatas[0]=-2;
        return linhaDatas;
    }

    public static void imprimirComparacoesInfetadosNaoInterativo(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos, int Verificacao,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            out.println();
            out.println("Comparação do total de ativos infetados");
            out.println();
            for (int i = 0; i < dias; i++) {
                out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[0] + i][1]);
                out.print("   |   ");
                out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[2] + i][1]);
                out.print("   |   ");
                out.printf("Diferença %17d\n", resultados[i][0]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }else{
            out.println();
            out.println("Comparação do total de novos infetados");
            out.println();
            for (int i = 0; i < dias; i++) {
                out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[0] + i][0]);
                out.print("   |   ");
                out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[2] + i][0]);
                out.print("   |   ");
                out.printf("Diferença %17d\n", resultados[i][0]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }
        out.close();
    }
    public static void imprimirMediaEDesvioPadraoInfetadosNaoInterativo(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        out.println();
        out.printf("Média %21.4f",MediaEDesvioPadrao1[0][0]);
        out.print("   |   ");
        out.printf("Média %21.4f",MediaEDesvioPadrao2[0][0]);
        out.print("   |   ");
        out.printf("Média %21.4f",mEDDosRe[0][0]);

        out.println();
        out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao1[1][0]);
        out.print("   |   ");
        out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao2[1][0]);
        out.print("   |   ");
        out.printf("Desvio Padrão %13.4f\n",mEDDosRe[1][0]);

        out.close();
    }
    public static void imprimirComparacoesHospitalizadosNaoInterativo(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos, int Verificacao,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            out.println();
            out.println("Comparação do total de ativos hospitalizados");
            out.println();
            for (int i = 0; i < dias; i++) {
                out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[0] + i][2]);
                out.print("   |   ");
                out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[2] + i][2]);
                out.print("   |   ");
                out.printf("Diferença %17d\n", resultados[i][1]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }else{
            out.println();
            out.println("Comparação do total de novos casos hospitalizados");
            out.println();
            for (int i = 0; i < dias; i++) {
                out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[0] + i][1]);
                out.print("   |   ");
                out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[2] + i][1]);
                out.print("   |   ");
                out.printf("Diferença %17d\n", resultados[i][1]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }
        out.close();
    }
    public static void imprimirMediaEDesvioPadraoHospitalizadosNaoInterativo(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe, String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);

        out.println();
        out.printf("Média %21.4f",MediaEDesvioPadrao1[0][1]);
        out.print("   |   ");
        out.printf("Média %21.4f",MediaEDesvioPadrao2[0][1]);
        out.print("   |   ");
        out.printf("Média %21.4f",mEDDosRe[0][1]);

        out.println();
        out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao1[1][1]);
        out.print("   |   ");
        out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao2[1][1]);
        out.print("   |   ");
        out.printf("Desvio Padrão %13.4f\n",mEDDosRe[1][1]);

        out.close();
    }
    public static void imprimirComparacoesUCINaoInterativo(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos, int Verificacao,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);

        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            out.println();
            out.println("Comparação do total de ativos internados UCI");
            out.println();
            for (int i = 0; i < dias; i++) {
                out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[0] + i][3]);
                out.print("   |   ");
                out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[2] + i][3]);
                out.print("   |   ");
                out.printf("Diferença %17d\n", resultados[i][2]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }else{
            out.println();
            out.println("Comparação do total de novos casos de internados UCI");
            out.println();
            for (int i = 0; i < dias; i++) {
                out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[0] + i][2]);
                out.print("   |   ");
                out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[2] + i][2]);
                out.print("   |   ");
                out.printf("Diferença %17d\n", resultados[i][2]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }
        out.close();
    }
    public static void imprimirMediaEDesvioPadraoUCINaoInterativo(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);

        out.println();
        out.printf("Média %21.4f",MediaEDesvioPadrao1[0][2]);
        out.print("   |   ");
        out.printf("Média %21.4f",MediaEDesvioPadrao2[0][2]);
        out.print("   |   ");
        out.printf("Média %21.4f",mEDDosRe[0][2]);

        out.println();
        out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao1[1][2]);
        out.print("   |   ");
        out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao2[1][2]);
        out.print("   |   ");
        out.printf("Desvio Padrão %13.4f\n",mEDDosRe[1][2]);

        out.close();
    }
    public static void imprimirComparacoesMortesNaoInterativo(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos, int Verificacao,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);

        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            out.println();
            out.println("Comparação do total de mortes diárias");
            out.println();
            for (int i = 0; i < dias; i++) {
                out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[0] + i][4]);
                out.print("   |   ");
                out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[2] + i][4]);
                out.print("   |   ");
                out.printf("Diferença %17d\n", resultados[i][3]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }else{
            out.println();
            out.println("Comparação de novas mortes");
            out.println();
            for (int i = 0; i < dias; i++) {
                out.print("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[0] + i][3]);
                out.print("   |   ");
                out.print("Dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH) + 1) + "/" + c2.get(Calendar.YEAR));
                out.printf("%14d", matrizRegistos[linhasDatas[2] + i][3]);
                out.print("   |   ");
                out.printf("Diferença %17d\n", resultados[i][3]);
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
        }
        out.close();
    }
    public static void imprimirMediaEDesvioPadraoMortesNaoInterativo(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);

        out.println();
        out.printf("Média %21.4f",MediaEDesvioPadrao1[0][3]);
        out.print("   |   ");
        out.printf("Média %21.4f",MediaEDesvioPadrao2[0][3]);
        out.print("   |   ");
        out.printf("Média %21.4f",mEDDosRe[0][3]);

        out.println();
        out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao1[1][3]);
        out.print("   |   ");
        out.printf("Desvio Padrão %13.4f",MediaEDesvioPadrao2[1][3]);
        out.print("   |   ");
        out.printf("Desvio Padrão %13.4f\n",mEDDosRe[1][3]);
        out.println();
        out.close();
    }

    public static void imprimirMatrizPevisaoDeDadosNaoInterativo(double[] arr1,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        out.println("Dados previstos para o dia " + (c2.get(Calendar.DAY_OF_MONTH)) + "/" + (c2.get(Calendar.MONTH)+1) + "/" + (c2.get(Calendar.YEAR)));
        out.printf("Total de não infetados: %.1f\n",arr1[0]);
        out.printf("Total de infetados: %.1f\n",arr1[1]);
        out.printf("Total de hospitalizados: %.1f\n",arr1[2]);
        out.printf("Total de internados na UCI: %.1f\n",arr1[3]);
        out.printf("Total de mortes: %.1f\n",arr1[4]);
        out.println();
        out.close();
    }
    public static void imprimirMatrizNumeroEsperadoDeDias(double[] esperados,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        out.println();
        out.printf("Número esperado de dias de não infetado até chegar ao estado óbito: %.1f\n", esperados[0]);
        out.printf("Número esperado de dias de infetado até chegar ao estado óbito: %.1f\n", esperados[1]);
        out.printf("Número esperado de dias de hospitalizado até chegar ao estado óbito: %.1f\n", esperados[2]);
        out.printf("Número esperado de dias de internado na UCI até chegar ao estado óbito: %.1f\n", esperados[3]);
        out.close();
    }

    public static void imprimirRegistosDiariosTotaisNaoInterativo (int [][] arr, int linhaDataInicial, int linhaDataFinal, String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);
        for (int i = linhaDataInicial; i <= linhaDataFinal; i++) {
            out.println("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
            out.println("Número de infetados: " + arr[i][0]);
            out.println("Número de hospitalizados: " + arr[i][1]);
            out.println("Número de internados na UCI: " + arr[i][2]);
            out.println("Número de mortes: " + arr[i][3]);
            out.println();
            c.add(Calendar.DATE, 1);
        }
        out.close();
    }

    public static void comparacaoSemanalTotaisNaoInterativo(int linhainicio, int linhafim, int[][] registos, String file) throws IOException {
        int linhamedia = linhainicio + 7;
        int comparacao=0;
        while (linhamedia <= linhafim) {
            for (int i = 1; i <= 4; i++) {
                for (int j =linhainicio; j <= linhamedia; j++) {
                    comparacao = (registos[j][i]) + comparacao;
                }
                impressaoComparacaoSemanalTotal(comparacao, i);
                comparacao=0;
            }
            c.add(Calendar.DATE, 7);
            linhamedia += 7;
            linhainicio += 7;

        }
    }
    public static void impressaoComparacaoSemanalTotalNaoInterativo(int comparacao, int i, String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);

        switch (i) {
            case 1:
                out.println("Semana de " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                out.println("Número de infetados: " + comparacao);
                break;
            case 2:
                out.println("Número de hospitalizados: " + comparacao);
                break;
            case 3:
                out.println("Número de internados UCI: " + comparacao);
                break;
            case 4:
                out.println("Número de mortes: " + comparacao);
                out.println();
                break;
        }
        out.close();
    }

    public static void imprimirRegistosMensaisTotaisNaoInterativo (int[][] MatrizNovosCasos, int ContMeses,String file) throws IOException {
        FileWriter fw = new FileWriter(file, true);
        PrintWriter out = new PrintWriter(fw);

        for (int i = 0; i < ContMeses; i++) {
            out.println("Mes " + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
            out.println("Número de infetados: " + MatrizNovosCasos[i][1]);
            out.println("Número de hospitalizados: " + MatrizNovosCasos[i][2]);
            out.println("Número de internados UCI: " + MatrizNovosCasos[i][3]);
            out.println("Número de mortes: " + MatrizNovosCasos[i][4]);
            out.println();
            c.add(Calendar.MONTH, 1);
        }
        out.close();
    }


    //Testes Unitarios
    //02-04-2020 a 03-04-2020
    public static boolean testeNovosCasosDiarios(int [][] ResultadosObtidos){
        int [][] ResultadoEsperados=new int[3][4];
        ResultadoEsperados[1][0]=783;
        ResultadoEsperados[1][1]=1042;
        ResultadoEsperados[1][2]=240;
        ResultadoEsperados[1][3]=22;
        ResultadoEsperados[2][0]=852;
        ResultadoEsperados[2][1]=1058;
        ResultadoEsperados[2][2]=245;
        ResultadoEsperados[2][3]=37;
        int qtd=0;
        for(int i=1;i<3;i++){
            for (int j=0;j<4;j++){
                if (ResultadoEsperados[i][j]==ResultadosObtidos[i][j]){
                    qtd++;
                }
            }
        }
        if(qtd==8){
            return true;
        }else {
            return false;
        }
    }
    public static boolean testeNovosCasosSemanais(int [] resultadosObtidos){
        int [] resultadoEsperados=new int[4];
        resultadoEsperados[0] = 3621;
        resultadoEsperados[1] = 8696;
        resultadoEsperados[2] = 1517;
        resultadoEsperados[3] = 210;
        int qtd = 0;
        for (int i = 0; i <= 3; i++) {
            if(resultadoEsperados [i] == resultadosObtidos[i]){
                qtd++;
            }
        }
        if (qtd == 4){
            return true;
        }else return false;
    }

    //04-04-2020 <-> 31-05-2020
    public static boolean testeNovosCasosMensais(int [][] ResultadosObtidos){
        int [][] ResultadoEsperados=new int[1][4];
        ResultadoEsperados[0][0]=7455;
        ResultadoEsperados[0][1]=20984;
        ResultadoEsperados[0][2]=3226;
        ResultadoEsperados[0][3]=421;
        int qtd=0;
        int i=0;
        int k=1;
        for (int j=0;j<4;j++){
            if (ResultadoEsperados[i][j]==ResultadosObtidos[i][k]){
                qtd++;
            }
            k++;
        }
        if(qtd==4){
            return true;
        }
        else {
            return false;
        }
    }

    //02-11-2020 - 04-11-2020 a 06-11-2020 - 08-11-2020
    public static boolean testesResultadosTotais(int[][] ResultadosObtidos){
        int qtd=0;
        int [][] ResultadosEsperados={{9391,170,46,6},{12726,71,46,11},{11347,185,53,-11}};
        for(int i=0;i<3;i++){
            for(int j=0;j<4;j++){
                if (ResultadosEsperados [i][j]== ResultadosObtidos [i][j]){
                    qtd++;
                }
            }
        }
        if(qtd==12){
            return true;
        }else {
            return false;
        }
    }
    public static boolean testesMediaEDesvioPadrao(double[][] resultadosObtidos){
        int qtd=0;
        double[][] ResultadosEsperados={{62160.66796875,2313.666748046875,313.0,50.0},{2240.527393271504, 41.77186605773451, 13.589211594236426, 6.377042256268373}};
        for(int i=0;i<2;i++){
            for(int j=0;j<4;j++){
                if (ResultadosEsperados [i][j]== resultadosObtidos [i][j]){
                    qtd++;
                }
            }
        }
        if(qtd==8){
            return true;
        }else{
            return false;
        }
    }
    //02-04-2020 - 04-04-2020 a 06-11-2020 - 08-11-2020
    public static boolean testesResultadosAcumulados(int[][] ResultadosObtidos){
        int qtd=0;
        int [][] ResultadosEsperados={{-331,57,30,-6},{-140,122,26,-3},{61,136,-6,15}};
        for(int i=0;i<3;i++){
            for(int j=0;j<4;j++){
                if (ResultadosEsperados [i][j]== ResultadosObtidos [i][j]){
                    qtd++;
                }
            }
        }
        if(qtd==12){
            return true;
        }else {
            return false;
        }
    }
    public static boolean testesMediaEDesvioPadraoAcumulado(double[][] resultadosObtidos){
        int qtd=0;
        double[][] ResultadosEsperados={{757.6666870117188, 1058.3333740234375, 245.3333282470703, 26.33333396911621},{89.18270670809028, 13.474255350518504, 4.496912426822622, 7.586537644820547}};
        for(int i=0;i<2;i++){
            for(int j=0;j<4;j++){
                if (ResultadosEsperados [i][j]== resultadosObtidos [i][j]){
                    qtd++;
                }
            }
        }

        if(qtd==8){
            return true;
        }else{
            return false;
        }
    }

    public static boolean testeMultiplicacaoMatriz (double[][] resultadoObtido){
        double[][] matrizEsperada = {{0.99901525,0.058802,0.004089,0.0024395,0},{0.00097975,0.921688,0.007911,0.0287305,0},{0.0000035,0.01364,0.960628,0.038705,0},{0.0000015,0.0058,0.019312,0.902745,0},{0,0.00007,0.00806,0.02738,1}};

        for (int i = 0; i < LINHAS_MATRIZ_TRANSICAO; i++) {
            for (int j = 0; j < COLUNAS_MATRIZ_TRANSICAO; j++) {
                if (Double.compare(matrizEsperada[i][j],resultadoObtido[i][j]) == 0){
                    return false;
                }
            }
        }
        return true;
    }


    //MODULOS PARA GUARDAR OS DADOS NUM FICHEIRO
    public static boolean VerificarSeUtilizadorQuerGuardarFicheiro(){
        String opficheiro; int cont=0;
        System.out.println();
        System.out.println("Deseja guardar os dados num ficheiro? (s/n)");
        do {
            if (cont>0) {
                System.out.println("A sua resposta não é válida, por favor tente novamente");
            }
            opficheiro = ler.nextLine();
            cont++;
        }while ((!opficheiro.equals("s"))&&(!opficheiro.equals("S"))&&(!opficheiro.equals("n"))&&(!opficheiro.equals("N"))&&(cont!=3));
        if ((opficheiro.equals("s"))||(opficheiro.equals("S"))){
            return true;
        }
        return false;
    }

    public static void CriarFicheiroTextoDiario(int opcao, int LinhaInicio, int LinhaFim, int[][]MatrizDados) throws IOException {
        System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
        String NomeFicheiro= ler.nextLine();
        File file= new File(NomeFicheiro+".csv");
        FileWriter fw = new FileWriter(file,true);

        String [][] MatrizStringDados = new String [MatrizDados.length][MatrizDados[0].length+3];
        for (int i = LinhaInicio ; i <= LinhaFim ; i++) {
            MatrizStringDados[i][0] = Integer.toString(MatrizDados[i][0]);
            MatrizStringDados[i][1]= Integer.toString(MatrizDados[i][1]);
            MatrizStringDados[i][2]= Integer.toString(MatrizDados[i][2]);
            MatrizStringDados[i][3]= Integer.toString(MatrizDados[i][3]);
            MatrizStringDados[i][4]= Integer.toString(c.get(Calendar.DAY_OF_MONTH));
            MatrizStringDados[i][5]= Integer.toString(c.get(Calendar.MONTH)+1);
            MatrizStringDados[i][6]= Integer.toString(c.get(Calendar.YEAR));
            c.add(Calendar.DAY_OF_MONTH,1);
        }

        if (opcao==1){
            fw.append("Data");
            fw.append(',');
            fw.append("Infetados");
            fw.append('\n');
            CriarFicheiroDadosIndividuaisDiario(opcao, LinhaInicio, LinhaFim, MatrizStringDados, fw);
        }
        if (opcao==2){
            fw.append("Data");
            fw.append(',');
            fw.append("Hospitalizados");
            fw.append('\n');
            CriarFicheiroDadosIndividuaisDiario(opcao, LinhaInicio, LinhaFim, MatrizStringDados, fw);
        }
        if (opcao==3){
            fw.append("Data");
            fw.append(',');
            fw.append("Internados_UCI");
            fw.append('\n');
            CriarFicheiroDadosIndividuaisDiario(opcao, LinhaInicio, LinhaFim, MatrizStringDados, fw);
        }
        if (opcao==4){
            fw.append("Data");
            fw.append(',');
            fw.append("Mortes");
            fw.append('\n');
            CriarFicheiroDadosIndividuaisDiario(opcao, LinhaInicio, LinhaFim, MatrizStringDados, fw);
        }

        if (opcao==5){
            fw.append("Data");
            fw.append(',');
            fw.append("Infetados");
            fw.append(',');
            fw.append("Hospitalizados");
            fw.append(',');
            fw.append("Internados_UCI");
            fw.append(',');
            fw.append("Mortes");
            fw.append('\n');
            for(int i=LinhaInicio; i<=LinhaFim; i++) {
                fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]+"-"+MatrizStringDados[i][6]);
                fw.append(',');
                fw.append(MatrizStringDados[i][0]);
                fw.append(',');
                fw.append(MatrizStringDados[i][1]);
                fw.append(',');
                fw.append(MatrizStringDados[i][2]);
                fw.append(',');
                fw.append(MatrizStringDados[i][3]);
                fw.append('\n');
            }
            fw.flush();
            fw.close();
        }
    }
    public static void CriarFicheiroDadosIndividuaisDiario(int opcao, int LinhaInicio, int LinhaFim, String[][]MatrizStringDados,FileWriter fw) throws IOException {
        switch(opcao){
            case 1:
                for(int i=LinhaInicio; i<=LinhaFim; i++) {
                    fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]+"-"+MatrizStringDados[i][6]);
                    fw.append(',');
                    fw.append(MatrizStringDados[i][0]);
                    fw.append('\n');
                }
                fw.flush();
                fw.close();
                break;
            case 2:
                for(int i=LinhaInicio; i<=LinhaFim; i++) {
                    fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]+"-"+MatrizStringDados[i][6]);
                    fw.append(',');
                    fw.append(MatrizStringDados[i][1]);
                    fw.append('\n');
                }
                fw.flush();
                fw.close();
                break;
            case 3:
                for(int i=LinhaInicio; i<=LinhaFim; i++) {
                    fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]+"-"+MatrizStringDados[i][6]);
                    fw.append(',');
                    fw.append(MatrizStringDados[i][2]);
                    fw.append('\n');
                }
                fw.flush();
                fw.close();
                break;
            case 4:
                for(int i=LinhaInicio; i<=LinhaFim; i++) {
                    fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]+"-"+MatrizStringDados[i][6]);
                    fw.append(',');
                    fw.append(MatrizStringDados[i][3]);
                    fw.append('\n');
                }
                fw.flush();
                fw.close();
                break;
        }
    }

    public static void CriarFicheiroTextoSemanal (int LinhaInicio, int LinhaFim,int opcao, int[][] MatrizDados, Date[] MatrizData) throws IOException {
        System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
        String NomeFicheiro = ler.nextLine();
        File file = new File(NomeFicheiro+".csv");
        FileWriter fw = new FileWriter(file,true);

        c.setTime(MatrizData[LinhaInicio]);

        int DSemanaInicial = c.get(Calendar.DAY_OF_WEEK);
        LinhaInicio = valorinicialsemana(DSemanaInicial, LinhaInicio);

        c.setTime(MatrizData[LinhaFim]);

        int DSemanaFinal = c.get(Calendar.DAY_OF_WEEK);
        LinhaFim = valorfinalsemana(DSemanaFinal, LinhaFim);

        c.setTime(MatrizData[LinhaInicio]);
        int linhamedia = LinhaInicio + 7;
        int comparacao=0;

        if (opcao <= 4) {
            fw.append("Semana");
            fw.append(',');
            if (opcao==1){
                fw.append("Infetados");
            }
            if (opcao==2){
                fw.append("Hospitalizados");
            }
            if(opcao==3){
                fw.append("Internados_UCI");
            }
            if (opcao==4){
                fw.append("Mortes");
            }
            fw.append('\n');
            while (linhamedia <= LinhaFim) {
                comparacao = (MatrizDados[linhamedia - 1][opcao]) - (MatrizDados[LinhaInicio - 1][opcao]);
                ImpressaoSemanaSingularFicheiro(comparacao, fw);
                c.add(Calendar.DATE, 7);
                linhamedia += 7;
                LinhaInicio += 7;
            }
        }
        else {
            fw.append("Semana");
            fw.append(',');
            fw.append("Infetados");
            fw.append(',');
            fw.append("Hospitalizados");
            fw.append(',');
            fw.append("Internados_UCI");
            fw.append(',');
            fw.append("Mortes");
            fw.append('\n');
            while (linhamedia <= LinhaFim) {
                fw.write(Integer.toString(c.get(Calendar.DAY_OF_MONTH))+"-"+Integer.toString((c.get(Calendar.MONTH))+1)+"-"+Integer.toString(c.get(Calendar.YEAR)));
                for (int i = 1; i <= 4; i++) {
                    comparacao = (MatrizDados[linhamedia - 1][i]) - (MatrizDados[LinhaInicio - 1][i]);
                }
                ImpressaoSemanaFicheiro(comparacao, fw);
                comparacao=0;
                fw.append('\n');
                c.add(Calendar.DATE, 7);
                linhamedia += 7;
                LinhaInicio += 7;
            }
        }
        fw.flush();
        fw.close();
    }

    public static void ImpressaoSemanaSingularFicheiro(int comparacao, FileWriter fw) throws IOException {
        fw.write(c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR));
        fw.append(",");
        fw.write(Integer.toString(comparacao));
        fw.append('\n');
    }

    public static void ImpressaoSemanaFicheiro(int comparacao, FileWriter fw) throws IOException {
        fw.append(",");
        fw.write(Integer.toString(comparacao));
    }

    public static void CriarFicheiroTextoMensal(int opcao, int[][] MatrizDados) throws IOException {

        System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
        String NomeFicheiro= ler.nextLine();

        if (c.get(Calendar.DAY_OF_MONTH)!=1){
            c.set(Calendar.DAY_OF_MONTH,1);
            c.add(Calendar.MONTH,1);
        }

        File file= new File(NomeFicheiro+".csv");
        FileWriter fw = new FileWriter(file,true);

        String [][] MatrizStringDados = new String [MatrizDados.length][MatrizDados[0].length+1];
        for (int i = 0 ; i < MatrizDados.length ; i++) {
            MatrizStringDados[i][0] = Integer.toString(MatrizDados[i][1]);
            MatrizStringDados[i][1]= Integer.toString(MatrizDados[i][2]);
            MatrizStringDados[i][2]= Integer.toString(MatrizDados[i][3]);
            MatrizStringDados[i][3]= Integer.toString(MatrizDados[i][4]);;
            MatrizStringDados[i][4]= Integer.toString(c.get(Calendar.MONTH)+1);
            MatrizStringDados[i][5]= Integer.toString(c.get(Calendar.YEAR));
            c.add(Calendar.MONTH,1);
        }

        if (opcao==1){
            fw.append("Mês");
            fw.append(',');
            fw.append("Infetados");
            fw.append('\n');
            CriarFicheiroDadosIndividuaisMensal(opcao, MatrizDados,MatrizStringDados ,fw);
        }
        if (opcao==2){
            fw.append("Mês");
            fw.append(',');
            fw.append("Hospitalizados");
            fw.append('\n');
            CriarFicheiroDadosIndividuaisMensal(opcao, MatrizDados,MatrizStringDados ,fw);
        }
        if (opcao==3){
            fw.append("Mês");
            fw.append(',');
            fw.append("Internados_UCI");
            fw.append('\n');
            CriarFicheiroDadosIndividuaisMensal(opcao, MatrizDados,MatrizStringDados ,fw);
        }
        if (opcao==4){
            fw.append("Mês");
            fw.append(',');
            fw.append("Mortes");
            fw.append('\n');
            CriarFicheiroDadosIndividuaisMensal(opcao, MatrizDados,MatrizStringDados ,fw);
        }

        if (opcao==5){fw.append("Mês");
            fw.append(',');
            fw.append("Infetados");
            fw.append(',');
            fw.append("Hospitalizados");
            fw.append(',');
            fw.append("Internados_UCI");
            fw.append(',');
            fw.append("Mortes");
            fw.append('\n');
            for(int i=0; i<MatrizDados.length; i++) {
                fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]);
                fw.append(',');
                fw.append(MatrizStringDados[i][0]);
                fw.append(',');
                fw.append(MatrizStringDados[i][1]);
                fw.append(',');
                fw.append(MatrizStringDados[i][2]);
                fw.append(',');
                fw.append(MatrizStringDados[i][3]);
                fw.append('\n');
            }
            fw.flush();
            fw.close();
        }
    }

    public static void CriarFicheiroDadosIndividuaisMensal(int opcao,int[][] MatrizDados, String[][] MatrizStringDados, FileWriter fw) throws IOException {
        switch(opcao){
            case 1:
                for(int i=0; i<MatrizDados.length; i++) {
                    fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]);
                    fw.append(',');
                    fw.append(MatrizStringDados[i][0]);
                    fw.append('\n');
                }
                fw.flush();
                fw.close();
                break;
            case 2:
                for(int i=0; i<MatrizDados.length; i++) {
                    fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]);
                    fw.append(',');
                    fw.append(MatrizStringDados[i][1]);
                    fw.append('\n');
                }
                fw.flush();
                fw.close();
                break;
            case 3:
                for(int i=0; i<MatrizDados.length; i++) {
                    fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]);
                    fw.append(',');
                    fw.append(MatrizStringDados[i][2]);
                    fw.append('\n');
                }
                fw.flush();
                fw.close();
                break;
            case 4:
                for(int i=0; i<MatrizDados.length; i++) {
                    fw.write(MatrizStringDados[i][4]+"-"+MatrizStringDados[i][5]);
                    fw.append(',');
                    fw.append(MatrizStringDados[i][3]);
                    fw.append('\n');
                }
                fw.flush();
                fw.close();
                break;
        }
    }

    public static int introducaoDoLocalDoFicheiroAcumulados(int [][] matrizRegistos, String [] matrizDatasS) throws FileNotFoundException {
        String File_in = ler.next();
        int totalLinhas = preencherMatrizRegistos(File_in, matrizRegistos, matrizDatasS);
        Date[] matrizDatas = new Date[totalLinhas];
        transformarMatrizStringParaDates(matrizDatas, matrizDatasS, totalLinhas);
        return totalLinhas;
    }
    public static int introducaoDoLocalDoFicheiroTotais(int [][] matrizRegistosTotal, String[] matrizDatasSTotal) throws FileNotFoundException {
        String FileInTotal = ler.next();
        int totalLinhasTotal = preencherMatrizRegistosTotal(FileInTotal, matrizDatasSTotal, matrizRegistosTotal);
        return  totalLinhasTotal;
    }

    //MODULOS ANALISE CASOS TOTAIS ATIVOS!!
    public static void imprimirRegistosDiariosTotais(int[][] arr, int linhaDataInicial, int linhaDataFinal, int opcao) {

        switch (opcao) {
            case 1:
                imprimirUmDadoTotal("Número de infetados: ", linhaDataInicial, linhaDataFinal, arr, opcao);
                break;
            case 2:
                imprimirUmDadoTotal("Número de hospitalizados: ", linhaDataInicial, linhaDataFinal, arr, opcao);
                break;
            case 3:
                imprimirUmDadoTotal("Número de internados na UCI: ", linhaDataInicial, linhaDataFinal, arr, opcao);
                break;
            case 4:
                imprimirUmDadoTotal("Número de mortes: ", linhaDataInicial, linhaDataFinal, arr, opcao);
                break;
            case 5:
                for (int i = linhaDataInicial; i <= linhaDataFinal; i++) {
                    System.out.println("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                    System.out.println("Número de infetados: " + arr[i][1]);
                    System.out.println("Número de hospitalizados: " + arr[i][2]);
                    System.out.println("Número de internados na UCI: " + arr[i][3]);
                    System.out.println("Número de mortes: " + arr[i][4]);
                    System.out.println();
                    c.add(Calendar.DATE, 1);
                }
                break;
        }
    }

    public static void imprimirUmDadoTotal(String dado, int linhaDataInicial, int linhaDataFinal, int[][] arr, int opcao) {
        for (int i = linhaDataInicial; i <= linhaDataFinal; i++) {
            System.out.println("Dia " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
            System.out.println(dado + arr[i][opcao]);
            System.out.println();
            c.add(Calendar.DATE, 1);
        }
    }

    public static void comparacaoSemanalTotais(int linhainicio, int linhafim, int[][] registos, Date[] MatrizData, int linhaDataInicioCopia, int linhaDataFimCopia,int[]  resultadosObtidos) throws IOException {
        int linhamedia = linhainicio + 7;
        int comparacao=0;
        int qtd = 0;
        String nString = lerQuaisDadosApresentar();
        int n = Integer.parseInt(nString);

        while (linhamedia <= linhafim) {
            if (n <= 4) {
                for (int j = linhainicio; j < linhamedia; j++) {
                    comparacao = (registos[j][n]) + comparacao;
                }
                impressaoComparacaoSemanalTotalSingular(comparacao, n, qtd);
                comparacao=0;
            } else {
                for (int i = 1; i <= 4; i++) {
                    for (int j =linhainicio; j < linhamedia; j++) {
                        comparacao = (registos[j][i]) + comparacao;
                        resultadosObtidos[i-1] = comparacao;
                    }
                    impressaoComparacaoSemanalTotal(comparacao, i);
                    comparacao=0;
                }
            }
            c.add(Calendar.DATE, 7);
            linhamedia += 7;
            linhainicio += 7;
            qtd++;

        }
        if (VerificarSeUtilizadorQuerGuardarFicheiro()) {
            c.setTime(MatrizData[linhaDataInicioCopia]);
            CriarFicheiroTextoSemanalTotal(linhaDataInicioCopia, linhaDataFimCopia, n, registos, MatrizData);
        }
    }

    public static void impressaoComparacaoSemanalTotal(int comparacao, int i) {
        switch (i) {
            case 1:
                System.out.println("Semana de " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
                System.out.println("Número de infetados: " + comparacao);
                break;
            case 2:
                System.out.println("Número de Hospitalizados: " + comparacao);
                break;
            case 3:
                System.out.println("Número de internados UCI: " + comparacao);
                break;
            case 4:
                System.out.println("Número de mortes: " + comparacao);
                System.out.println();
                break;
        }
    }

    public static void impressaoComparacaoSemanalTotalSingular (int comparacao, int i, int qtd) {
        if (qtd == 0) {
            switch (i) {
                case 1:
                    System.out.println("Número de infetados: ");
                    break;
                case 2:
                    System.out.println("Número de Hospitalizados: ");
                    break;
                case 3:
                    System.out.println("Número de internados UCI: ");
                    break;
                case 4:
                    System.out.println("Número de mortes: ");
                    break;
            }
        }
        System.out.print("Na Semana de " + (c.get(Calendar.DAY_OF_MONTH)) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR) + ": ");
        System.out.println(comparacao);
    }

    public static int[][] AnalisarDadosMesTotal(int ContMeses, int linhaDataInicio, int[][] matrizRegistos, Date[] matrizDatas) {
        int[][] MatrizNovosCasos = new int[ContMeses][COLUNAS_REGISTOS];
        int linhaDataInicio2 = linhaDataInicio;
        int contlinha = 0;
        linhaDataInicio2 = VerificadorLinha(linhaDataInicio2);
        for (int contMeses = 0; contMeses < ContMeses; contMeses++) {
            int linhaDatafim = LeitordeLinhaUltDiaMes(linhaDataInicio2);
            PreencherDadosAnaliseMesTotal(MatrizNovosCasos, matrizRegistos, contlinha, linhaDataInicio2,linhaDatafim);
            contlinha++;
            linhaDataInicio2=linhaDatafim+1;
        }
        c.setTime(matrizDatas[linhaDataInicio]);
        if (c.get(Calendar.DAY_OF_MONTH) != 1) {
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.MONTH, 1);
        }
        return MatrizNovosCasos;
    }

    public static void PreencherDadosAnaliseMesTotal (int[][]MatrizDados, int[][] MatrizOriginal, int contlinha, int L1, int Lfim){
        for(int j=0; j<5; j++){
            for (int i=L1;i<=Lfim;i++)
                MatrizDados[contlinha][j]=MatrizOriginal[i][j]+MatrizDados[contlinha][j];
        }
    }

    public static void imprimirRegistosMensaisTotais (int[][] MatrizNovosCasos, int ContMeses) {
        for (int i = 0; i < ContMeses; i++) {
            System.out.println("Mes " + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
            System.out.println("Número de infetados: " + MatrizNovosCasos[i][1]);
            System.out.println("Número de Hospitalizados: " + MatrizNovosCasos[i][2]);
            System.out.println("Número de internados UCI: " + MatrizNovosCasos[i][3]);
            System.out.println("Número de mortes: " + MatrizNovosCasos[i][4]);
            System.out.println();
            c.add(Calendar.MONTH, 1);
        }
    }

    public static void imprimirRegistoSingularMensaisTotais (int[][] MatrizNovoCaso, int ContMeses, int opcao) {
        for (int i = 0; i < ContMeses; i++) {
            System.out.println("Mes " + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
            switch (opcao) {
                case 1:
                    System.out.println("Número de infetados: " + MatrizNovoCaso[i][1]);
                    break;
                case 2:
                    System.out.println("Número de Hospitalizados: " + MatrizNovoCaso[i][2]);
                    break;
                case 3:
                    System.out.println("Número de internados UCI: " + MatrizNovoCaso[i][3]);
                    break;
                case 4:
                    System.out.println("Nariação número de mortes: " + MatrizNovoCaso[i][4]);
                    break;
            }
            System.out.println();
            c.add(Calendar.MONTH, 1);
        }
    }

    //MODULO FICHEIRO TOTAL
    public static void CriarFicheiroTextoSemanalTotal (int LinhaInicio, int LinhaFim,int opcao, int[][] MatrizDados, Date[] MatrizData) throws IOException {
        System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
        String NomeFicheiro = ler.nextLine();
        File file = new File(NomeFicheiro+".csv");
        FileWriter fw = new FileWriter(file,true);

        c.setTime(MatrizData[LinhaInicio]);

        int DSemanaInicial = c.get(Calendar.DAY_OF_WEEK);
        LinhaInicio = valorinicialsemana(DSemanaInicial, LinhaInicio);

        c.setTime(MatrizData[LinhaFim]);

        int DSemanaFinal = c.get(Calendar.DAY_OF_WEEK);
        LinhaFim = valorfinalsemana(DSemanaFinal, LinhaFim);

        c.setTime(MatrizData[LinhaInicio]);
        int linhamedia = LinhaInicio + 7;
        int comparacao=0;

        if (opcao <= 4) {
            fw.append("Semana");
            fw.append(',');
            if (opcao==1){
                fw.append("Infetados");
            }
            if (opcao==2){
                fw.append("Hospitalizados");
            }
            if(opcao==3){
                fw.append("Internados_UCI");
            }
            if (opcao==4){
                fw.append("Mortes");
            }
            fw.append('\n');
            while (linhamedia <= LinhaFim) {
                for (int j = LinhaInicio; j < linhamedia; j++) {
                    comparacao = (MatrizDados[j][opcao]) + comparacao;
                }
                ImpressaoSemanaSingularFicheiro(comparacao, fw);
                comparacao=0;
                c.add(Calendar.DATE, 7);
                linhamedia += 7;
                LinhaInicio += 7;
            }
        }
        else {
            fw.append("Semana");
            fw.append(',');
            fw.append("Infetados");
            fw.append(',');
            fw.append("Hospitalizados");
            fw.append(',');
            fw.append("Internados_UCI");
            fw.append(',');
            fw.append("Mortes");
            fw.append('\n');
            while (linhamedia <= LinhaFim) {
                fw.write(Integer.toString(c.get(Calendar.DAY_OF_MONTH))+"-"+Integer.toString((c.get(Calendar.MONTH))+1)+"-"+Integer.toString(c.get(Calendar.YEAR)));
                for (int i = 1; i <= 4; i++) {
                    for (int j = LinhaInicio; j < linhamedia; j++) {
                        comparacao = (MatrizDados[j][i]) + comparacao;
                    }
                    ImpressaoSemanaFicheiro(comparacao, fw);
                    comparacao=0;
                }
                fw.append('\n');
                c.add(Calendar.DATE, 7);
                linhamedia += 7;
                LinhaInicio += 7;
            }
        }
        fw.flush();
        fw.close();
    }
    //MODULO Ficheiro CSV da comparacao e da previsão
    public static void criarCSVcomparacaonovosinfetados(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos,PrintWriter pw ,int Verificacao) throws IOException {
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_infetados_primeiro_intervalo");
            pw.append(',');
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_infetados_segundo_intervalo");
            pw.append(',');
            pw.append("Diferença");
            pw.append('\n');
            for (int i = 0; i < dias; i++) {
                pw.write( (c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[0] + i][1]);
                pw.append(',');
                pw.write( + (c2.get(Calendar.DAY_OF_MONTH)) + "-" + (c2.get(Calendar.MONTH) + 1) + "-" + c2.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[2] + i][1]);
                pw.append(',');
                pw.print(resultados[i][0]);
                pw.append('\n');
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
            pw.append('\n');
        }
        else{
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_infetados_primeiro_intervalo");
            pw.append(',');
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_infetados_segundo_intervalo");
            pw.append(',');
            pw.append("Diferença");
            pw.append('\n');
            for (int i = 0; i < dias; i++) {
                pw.write( (c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[0] + i][0]);
                pw.append(',');
                pw.write((c2.get(Calendar.DAY_OF_MONTH)) + "-" + (c2.get(Calendar.MONTH) + 1) + "-" + c2.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[2] + i][0]);
                pw.append(',');
                pw.print(resultados[i][0]);
                pw.append('\n');
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
            pw.append('\n');
        }
    }
    public static void criarCSVMediaEDesvioPadraoInfetados(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe, PrintWriter pw) throws IOException {
        pw.append("Média_Primeiro_Intervalo");
        pw.append(',');
        pw.append("Desvio_Padrão_Primeiro_Intervalo");
        pw.append('\n');
        pw.print((String.format("%.4f",MediaEDesvioPadrao1[0][0])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",MediaEDesvioPadrao1[1][0])).replace(",","."));
        pw.append('\n');
        pw.append("Média_Segundo_Intervalo");
        pw.append(',');
        pw.append("Desvio_Padrão_Segundo_Intervalo");
        pw.append('\n');
        pw.print((String.format("%.4f",MediaEDesvioPadrao2[0][0])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",MediaEDesvioPadrao2[1][0])).replace(",","."));
        pw.append('\n');
        pw.append("Média_Diferença");
        pw.append(',');
        pw.append("Desvio_Padrão_Diferença");
        pw.append('\n');
        pw.print((String.format("%.4f",mEDDosRe[0][0])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",mEDDosRe[1][0])).replace(",","."));
        pw.append('\n');
        pw.append('\n');
    }
    public static void criarCSVComparacoesHospitalizados(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos,PrintWriter pw ,int Verificacao) throws IOException {
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_hospitalizados_primeiro_intervalo");
            pw.append(',');
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_hospitalizados_segundo_intervalo");
            pw.append(',');
            pw.append("Diferença");
            pw.append('\n');
            for (int i = 0; i < dias; i++) {
                pw.write((c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[0] + i][2]);
                pw.append(',');
                pw.write((c2.get(Calendar.DAY_OF_MONTH)) + "-" + (c2.get(Calendar.MONTH) + 1) + "-" + c2.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[2] + i][2]);
                pw.append(',');
                pw.print(resultados[i][1]);
                pw.append('\n');
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
            pw.append('\n');
        }else{
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_hospitalizados_primeiro_intervalo");
            pw.append(',');
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_hospitalizados_segundo_intervalo");
            pw.append(',');
            pw.append("Diferença");
            pw.append('\n');
            for (int i = 0; i < dias; i++) {
                pw.write( (c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[0] + i][1]);
                pw.append(',');
                pw.write((c2.get(Calendar.DAY_OF_MONTH)) + "-" + (c2.get(Calendar.MONTH) + 1) + "-" + c2.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[2] + i][1]);
                pw.append(',');
                pw.print(resultados[i][1]);
                pw.append('\n');
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
            pw.append('\n');
        }
    }
    public static void criarCSVMediaEDesvioPadraoHospitalizados(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe, PrintWriter pw) throws IOException {
        pw.append("Média_Primeiro_Intervalo");
        pw.append(',');
        pw.append("Desvio_Padrão_Primeiro_Intervalo");
        pw.append('\n');
        pw.print((String.format("%.4f",MediaEDesvioPadrao1[0][1])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",MediaEDesvioPadrao1[1][1])).replace(",","."));
        pw.append('\n');
        pw.append("Média_Segundo_Intervalo");
        pw.append(',');
        pw.append("Desvio_Padrão_Segundo_Intervalo");
        pw.append('\n');
        pw.print((String.format("%.4f",MediaEDesvioPadrao2[0][1])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",MediaEDesvioPadrao2[1][1])).replace(",","."));
        pw.append('\n');
        pw.append("Média_Diferença");
        pw.append(',');
        pw.append("Desvio_Padrão_Diferença");
        pw.append('\n');
        pw.print((String.format("%.4f",mEDDosRe[0][1])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",mEDDosRe[1][1])).replace(",","."));
        pw.append('\n');
        pw.append('\n');
    }

    public static void criarCSVComparacoesUCI(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos,PrintWriter pw ,int Verificacao) throws IOException {
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_internados_UCI_primeiro_intervalo");
            pw.append(',');
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_internados_UCI_segundo_intervalo");
            pw.append(',');
            pw.append("Diferença");
            pw.append('\n');
            for (int i = 0; i < dias; i++) {
                pw.write((c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[0] + i][3]);
                pw.append(',');
                pw.write((c2.get(Calendar.DAY_OF_MONTH)) + "-" + (c2.get(Calendar.MONTH) + 1) + "-" + c2.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[2] + i][3]);
                pw.append(',');
                pw.print(resultados[i][2]);
                pw.append('\n');
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
            pw.append('\n');
        }else{
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_internados_UCI_primeiro_intervalo");
            pw.append(',');
            pw.append("Data");
            pw.append(',');
            pw.append("total_novos_internados_UCI_segundo_intervalo");
            pw.append(',');
            pw.append("Diferença");
            pw.append('\n');
            for (int i = 0; i < dias; i++) {
                pw.write((c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[0] + i][2]);
                pw.append(',');
                pw.write((c2.get(Calendar.DAY_OF_MONTH)) + "-" + (c2.get(Calendar.MONTH) + 1) + "-" + c2.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[2] + i][2]);
                pw.append(',');
                pw.print(resultados[i][2]);
                pw.append('\n');
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
            pw.append('\n');
        }
    }
    public static void criarCSVMediaEDesvioPadraoUCI(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe, PrintWriter pw) throws IOException {
        pw.append("Média_Primeiro_Intervalo");
        pw.append(',');
        pw.append("Desvio_Padrão_Primeiro_Intervalo");
        pw.append('\n');
        pw.print((String.format("%.4f",MediaEDesvioPadrao1[0][2])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",MediaEDesvioPadrao1[1][2])).replace(",","."));
        pw.append('\n');
        pw.append("Média_Segundo_Intervalo");
        pw.append(',');
        pw.append("Desvio_Padrão_Segundo_Intervalo");
        pw.append('\n');
        pw.print((String.format("%.4f",MediaEDesvioPadrao2[0][2])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",MediaEDesvioPadrao2[1][2])).replace(",","."));
        pw.append('\n');
        pw.append("Média_Diferença");
        pw.append(',');
        pw.append("Desvio_Padrão_Diferença");
        pw.append('\n');
        pw.print((String.format("%.4f",mEDDosRe[0][2])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",mEDDosRe[1][2])).replace(",","."));
        pw.append('\n');
        pw.append('\n');
    }

    public static void criarCSVComparacoesMortes(int [][] resultados, int dias, int [] linhasDatas, Date[] matrizDatas, int [][] matrizRegistos,PrintWriter pw ,int Verificacao) throws IOException {
        c.setTime(matrizDatas[linhasDatas[0]]);
        c2.setTime(matrizDatas[linhasDatas[2]]);
        if(Verificacao==0) {
            pw.append("Data");
            pw.append(',');
            pw.append("total_novas_mortes_primeiro_intervalo");
            pw.append(',');
            pw.append("Data");
            pw.append(',');
            pw.append("total_novas_mortes_segundo_intervalo");
            pw.append(',');
            pw.append("Diferença");
            pw.append('\n');
            for (int i = 0; i < dias; i++) {
                pw.write((c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[0] + i][4]);
                pw.append(',');
                pw.write((c2.get(Calendar.DAY_OF_MONTH)) + "-" + (c2.get(Calendar.MONTH) + 1) + "-" + c2.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[2] + i][4]);
                pw.append(',');
                pw.print(resultados[i][3]);
                pw.append('\n');
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
            pw.append('\n');
        }else{
            pw.append("Data");
            pw.append(',');
            pw.append("total_novas_mortes_primeiro_intervalo");
            pw.append(',');
            pw.append("Data");
            pw.append(',');
            pw.append("total_novas_mortes_segundo_intervalo");
            pw.append(',');
            pw.append("Diferença");
            pw.append('\n');
            for (int i = 0; i < dias; i++) {
                pw.write((c.get(Calendar.DAY_OF_MONTH)) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[0] + i][3]);
                pw.append(',');
                pw.write((c2.get(Calendar.DAY_OF_MONTH)) + "-" + (c2.get(Calendar.MONTH) + 1) + "-" + c2.get(Calendar.YEAR));
                pw.append(',');
                pw.print(matrizRegistos[linhasDatas[2] + i][3]);
                pw.append(',');
                pw.print(resultados[i][3]);
                pw.append('\n');
                c.add(Calendar.DATE, 1);
                c2.add(Calendar.DATE, 1);
            }
            pw.append('\n');
        }
    }
    public static void criarCSVMediaEDesvioPadraoMortes(double [][] MediaEDesvioPadrao1, double [][] MediaEDesvioPadrao2, double [][] mEDDosRe, PrintWriter pw) throws IOException {
        pw.append("Média_Primeiro_Intervalo");
        pw.append(',');
        pw.append("Desvio_Padrão_Primeiro_Intervalo");
        pw.append('\n');
        pw.print((String.format("%.4f",MediaEDesvioPadrao1[0][3])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",MediaEDesvioPadrao1[1][3])).replace(",","."));
        pw.append('\n');
        pw.append("Média_Segundo_Intervalo");
        pw.append(',');
        pw.append("Desvio_Padrão_Segundo_Intervalo");
        pw.append('\n');
        pw.print((String.format("%.4f",MediaEDesvioPadrao2[0][3])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",MediaEDesvioPadrao2[1][3])).replace(",","."));
        pw.append('\n');
        pw.append("Média_Diferença");
        pw.append(',');
        pw.append("Desvio_Padrão_Diferença");
        pw.append('\n');
        pw.print((String.format("%.4f",mEDDosRe[0][3])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.4f",mEDDosRe[1][3])).replace(",","."));
        pw.append('\n');
        pw.append('\n');
    }

    public static void criarCSVMatrizPevisaoDeDados(double[] arr1) throws IOException {
        System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
        String NomeFicheiro= ler.nextLine();
        File file= new File(NomeFicheiro+".csv");
        FileWriter fw = new FileWriter(file,true);
        PrintWriter pw = new PrintWriter(fw);
        pw.append("Data");
        pw.append(',');
        pw.append("total_não_infetados" );
        pw.append(',');
        pw.append("total_infetados");
        pw.append(',');
        pw.append("total_hospitalizados");
        pw.append(',');
        pw.append("total_internados_UCI");
        pw.append(',');
        pw.append("total_mortes");
        pw.append('\n');
        pw.write(c2.get(Calendar.DAY_OF_MONTH)+ "-" + (c2.get(Calendar.MONTH)+1) + "-" + (c2.get(Calendar.YEAR)));
        pw.append(',');
        pw.print((String.format("%.1f",arr1[0])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.1f",arr1[1])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.1f",arr1[2])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.1f",arr1[3])).replace(",","."));
        pw.append(',');
        pw.print((String.format("%.1f",arr1[4])).replace(",","."));
        pw.append('\n');
        pw.append('\n');
        pw.flush();
        pw.close();
    }
    public static void criarCSVPrevisaoNumeroEsperadoDeDias(double numeroEsperadodeDias) throws IOException {
        System.out.println("Qual o nome que deseja colocar ao ficheiro CSV?");
        String NomeFicheiro= ler.nextLine();
        File file= new File(NomeFicheiro+".csv");
        FileWriter fw = new FileWriter(file,true);
        PrintWriter pw = new PrintWriter(fw);
        pw.append("Número esperado de dias até chegar ao estado óbito");
        pw.append('\n');
        pw.print((String.format("%.1f",numeroEsperadodeDias)).replace(",","."));
        pw.append('\n');
        pw.flush();
        pw.close();
    }
    // DATA TESTE 02-11-2020 || 03-11-2020
    public static boolean testeCasosTotaisDiarios(int [][] ResultadosObtidos){
        int [][] ResultadoEsperados=new int[3][4];
        ResultadoEsperados[1][0]=60963;
        ResultadoEsperados[1][1]=2255;
        ResultadoEsperados[1][2]=294;
        ResultadoEsperados[1][3]=46;
        ResultadoEsperados[2][0]=60219;
        ResultadoEsperados[2][1]=2349;
        ResultadoEsperados[2][2]=320;
        ResultadoEsperados[2][3]=45;
        int qtd=0;
        for(int i=1;i<3;i++){
            for (int j=0;j<4;j++){
                if (ResultadoEsperados[i][j]==ResultadosObtidos[i][j]){
                    qtd++;
                }
            }
        }
        if(qtd==8){
            return true;
        }else {
            return false;
        }
    }
    // DATA TESTE 02-11-2020  || 09-11-2020
    public static boolean testeCasosTotaisSemanais(int [] resultadosObtidos){
        int [] resultadoEsperados=new int[4];
        resultadoEsperados[0] = 473585;
        resultadoEsperados[1] = 16670;
        resultadoEsperados[2] = 2343;
        resultadoEsperados[3] = 352;
        int qtd = 0;
        for (int i = 0; i <= 3; i++) {
            if(resultadoEsperados [i] == resultadosObtidos[i]){
                qtd++;
            }
        }
        if (qtd == 4){
            return true;
        }else return false;
    }
    //DATA TESTE 02-11-2020  ||  31-12-2020
    public static boolean testeCasosTotaisMensais(int [][] ResultadosObtidos){
        int [][] ResultadoEsperados=new int[1][4];
        ResultadoEsperados[0][0]=2193454;
        ResultadoEsperados[0][1]=96468;
        ResultadoEsperados[0][2]=15632;
        ResultadoEsperados[0][3]=2401;
        int qtd=0;
        int i=0;
        int k=1;
        for (int j=0;j<4;j++){
            if (ResultadoEsperados[i][j]==ResultadosObtidos[i][k]){
                qtd++;
            }
            k++;
        }
        if(qtd==4){
            return true;
        }
        else {
            return false;
        }
    }
}