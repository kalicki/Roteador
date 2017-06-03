import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSender implements Runnable {
	TabelaRoteamento tabela; // Tabela de roteamento
	ArrayList<String> vizinhos; // Lista de IPs dos roteadores vizinhos

	/**
	 * Construtor
	 * 
	 * @param tabela
	 * @param vizinhos
	 */
	public MessageSender(TabelaRoteamento tabela, ArrayList<String> vizinhos) {
		this.tabela = tabela;
		this.vizinhos = vizinhos;
	}

	@Override
	public void run() {
		DatagramSocket clientSocket = null;
		InetAddress IPAddress;
		byte[] sendData;

		// Cria socket para envio de mensagem
		try {
			clientSocket = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			/*
			 * Pega a tabela de roteamento em array de BYTES, conforme
			 * especificado pelo protocolo.
			 */
			sendData = tabela.get_tabela_string().getBytes();

			// Anuncia a tabela de roteamento para cada um dos vizinhos
			try {
				for (String vizinho : vizinhos) {
					// registra o IP
					String ip = vizinho.split(":")[0];
					// registra a porta se existir, senão usa 5000 como default
					Integer porta = vizinho.split(":").length > 1 ? Integer.parseInt(vizinho.split(":")[1]) : 5000;

					// Converte string com o IP do vizinho para formato
					// InetAddress
					IPAddress = InetAddress.getByName(ip);

					// Configura pacote para envio da menssagem para o roteador
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, porta);

					// Realiza envio da mensagem.
					clientSocket.send(sendPacket);

					System.out.println("Enviando mensagem: " + tabela.get_tabela_string() + " de "
							+ sendPacket.getAddress() + ":" + sendPacket.getPort());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * Espera 10 segundos antes de realizar o próximo envio. CONTUDO,
			 * caso a tabela de roteamento sofra uma alteração, ela deve ser
			 * reenvida aos vizinho imediatamente.
			 */
			try {
				Thread.sleep(10000);
			} catch (InterruptedException ex) {
				Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
