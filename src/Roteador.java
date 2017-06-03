import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Roteador {
	private final static boolean DEV = true;
	private final static String lerArquivo = "IPVizinhos.txt";

	public static void main(String[] args) throws IOException {
		String leitura = lerArquivo;
		String linhaArquivo;

		// Lista de endereço IPs dos vizinhos
		ArrayList<String> listaVizinhos = new ArrayList<String>();

		// Le arquivo de entrada com lista de IPs dos roteadores vizinhos
		try (BufferedReader inputFile = new BufferedReader(new FileReader(leitura))) {
			System.out.println("Lendo arquivo: " + leitura);

			while ((linhaArquivo = inputFile.readLine()) != null) {
				// Adiciona no Arraylist o IP:PORTA
				listaVizinhos.add(linhaArquivo);
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
			return;
		}

		/*
		 * Cria instâncias da tabela de roteamento e das threads de envio e
		 * recebimento de mensagens.
		 */
		TabelaRoteamento tabela = new TabelaRoteamento();
		new Thread(new MessageReceiver(tabela)).start();
		new Thread(new MessageSender(tabela, listaVizinhos)).start();
	}
}
