import com.sun.jna.Library;
import com.sun.jna.Native;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.util.SequencedSet;

public class Main {

    public interface ImpressoraDLL extends Library {
        ImpressoraDLL INSTANCE = (ImpressoraDLL) Native.load(
                "C:\\Users\\geovanna_costa\\Downloads\\Java-Aluno EM\\Java-Aluno EM\\Java-Aluno EM\\E1_Impressora01.dll",
                ImpressoraDLL.class
        );

        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param);
        int FechaConexaoImpressora();
        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho);
        int Corte(int avanco);
        int ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao);
        int ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI);
        int AvancaPapel(int linhas);
        int StatusImpressora(int param);
        int AbreGavetaElgin();
        int AbreGaveta(int pino, int ti, int tf);
        int SinalSonoro(int qtd, int tempoInicio, int tempoFim);
        int ModoPagina();
        int LimpaBufferModoPagina();
        int ImprimeModoPagina();
        int ModoPadrao();
        int PosicaoImpressaoHorizontal(int posicao);
        int PosicaoImpressaoVertical(int posicao);
        int ImprimeXMLSAT(String dados, int param);
        int ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param);
    }

    private static boolean conexaoAberta = false;
    private static int tipo;
    private static String modelo;
    private static String conexao;
    private static int parametro;
    private static final Scanner scanner = new Scanner(System.in);

    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    public static void configurarConexao() {
        if (!conexaoAberta) {
            tipo = Integer.parseInt(capturarEntrada("Digite o tipo de conexão (1-USB, 2-Serial, etc.): "));
            modelo = capturarEntrada("Digite o modelo da impressora (ex: I9, I8, etc.): ");
            conexao = capturarEntrada("Digite o nome da conexão (ex: USB, COM1, etc.): ");
            parametro = Integer.parseInt(capturarEntrada("Digite o parâmetro adicional (0 para padrão): "));
            System.out.println("Configuração salva com sucesso!");
        } else {
            System.out.println("Feche a conexão antes de reconfigurar.");
        }
    }

    public static void abrirConexao() {
        if (!conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);
            if (retorno == 0) {
                conexaoAberta = true;
                System.out.println(" Conexão aberta com sucesso!");
            } else {
                System.out.println(" Erro ao abrir conexão. Código: " + retorno);
            }
        } else {
            System.out.println("A conexão já está aberta.");
        }
    }

    public static void fecharConexao() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();
            if (retorno == 0) {
                conexaoAberta = false;
                System.out.println(" Conexão fechada com sucesso!");
            } else {
                System.out.println(" Erro ao fechar conexão. Código: " + retorno);
            }
        } else {
            System.out.println("Nenhuma conexão aberta.");
        }
    }

    public static void impressaoTexto() {
        if (conexaoAberta) {
            String texto = capturarEntrada("Digite o texto para impressão: ");
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(texto, 1, 4, 0);
            ImpressoraDLL.INSTANCE.AvancaPapel(2);
            System.out.println(retorno == 0 ? " Impressão de texto realizada com sucesso!" : "Erro na impressão de texto. Código: " + retorno);
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    public static void impressaoQRCode() {
        if (conexaoAberta) {
            String dados = capturarEntrada("Digite o conteúdo do QRCode: ");
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, 6, 4);

            System.out.println(retorno == 0 ? " QRCode impresso!" : "Erro ao imprimir QRCode. Código: " + retorno);
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    // --------------------------------------------------------
    //  IMPRESSÃO DE CÓDIGO DE BARRAS AUTOMÁTICA (SEM USUÁRIO)
    // --------------------------------------------------------
    public static void impressaoCodigoBarras() {
        if (conexaoAberta) {

            // Código de barras gerado automaticamente
            String dados = "{A012345678912";  // você pode trocar para outro

            // Tipo 8 = CODE128
            int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(8, dados, 100, 2, 3);

            System.out.println(retorno == 0 ?
                    " Código de barras impresso automaticamente!" :
                    "Erro ao imprimir código de barras. Código: " + retorno);
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }
    // --------------------------------------------------------

    public static void abreGavetaElgin() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreGavetaElgin();
            System.out.println(retorno == 0 ? " Gaveta aberta!" : "Erro ao abrir gaveta. Código: " + retorno);
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    public static void abreGaveta() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.AbreGaveta(1, 5, 10);
            System.out.println(retorno == 0 ? " Gaveta aberta (pino)!" : "Erro ao abrir gaveta. Código: " + retorno);
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    public static void sinalSonoro() {
        if (conexaoAberta) {
            int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(4, 5, 5);
            System.out.println(retorno == 0 ? " Sinal sonoro emitido!" : "Erro ao emitir sinal. Código: " + retorno);
        } else {
            System.out.println("Erro: Conexão não está aberta.");
        }
    }

    public  static void impressaoXMLSAT() {
        if (conexaoAberta) {
            int ret = ImpressoraDLL.INSTANCE.ImprimeXMLSAT("path=C:\\Users\\geovanna_costa\\Downloads\\Java-Aluno EM\\Java-Aluno EM\\Java-Aluno EM\\XMLSAT.xml", 0);

            if(ret == 0){
                System.out.println("Impressao OK");
            }
            else{
                System.out.println("Impressao ERRO. Retorno: " + ret);
            }
        }
    }

    public  static void impressaoCancelamentoSAT() {
        if (conexaoAberta) {

            String assQRCode = "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";
            int ret = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT("path=C:\\Users\\geovanna_costa\\Downloads\\Java-Aluno EM\\Java-Aluno EM\\Java-Aluno EM\\CANC_SAT.xml", assQRCode, 0);

            if(ret == 0){
                System.out.println("Impressao OK");
            }
            else{
                System.out.println("Impressao ERRO. Retorno: " + ret);
            }
        }
    }

    private static String lerArquivoComoString(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return new String(data, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n*************************************************");
            System.out.println("**************** MENU IMPRESSORA ****************");
            System.out.println("*************************************************\n");

            System.out.println("1  - Configurar Conexão");
            System.out.println("2  - Abrir Conexão");
            System.out.println("3  - Imprimir Texto");
            System.out.println("4  - Imprimir QRCode");
            System.out.println("5  - Imprimir Código de Barras (automático)");
            System.out.println("6  - Imprimir XML SAT");
            System.out.println("7  - Imprimir XML Cancelamento SAT");
            System.out.println("8  - Abrir Gaveta Elgin");
            System.out.println("9  - Abrir Gaveta (Pino)");
            System.out.println("10 - Emitir Sinal Sonoro");
            System.out.println("0  - Fechar Conexão e Sair");

            String escolha = capturarEntrada("\nDigite a opção desejada: ");

            switch (escolha) {
                case "1":
                    configurarConexao();
                    break;
                case "2":
                    abrirConexao();
                    break;
                case "3":
                    impressaoTexto();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;
                case "4":
                    impressaoQRCode();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;
                case "5":
                    impressaoCodigoBarras();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;
                case "6":
                    impressaoXMLSAT();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;
                case "7":
                    impressaoCancelamentoSAT();
                    ImpressoraDLL.INSTANCE.Corte(3);
                    break;
                case "8":
                    abreGavetaElgin();
                    break;
                case "9":
                    abreGaveta();
                    break;
                case "10":
                    sinalSonoro();
                    break;
                case "0":
                    fecharConexao();
                    System.out.println(" Programa encerrado.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}