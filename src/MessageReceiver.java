import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MessageReceiver implements Runnable {
	private static final String TabelaRoteamento = null;
	private TabelaRoteamento tabela;
	private DatagramSocket serverSocket;

	public MessageReceiver(TabelaRoteamento t) throws SocketException {
		this.tabela = t;
		// Inicializa o servidor para aguardar datagramas na porta 5000
		serverSocket = new DatagramSocket(5000);
	}

	@Override
	public void run() {
		/*
		 * try { procuraVizinhoOff } catch (SocketException ex) {
		 * Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE,
		 * null, ex); return; }
		 */

		byte[] recebeDados = new byte[1024];

		// Loop para verificar se foi recebido
		while (true) {
			try {
				// Cria um DatagramPacket
				DatagramPacket recebePacote = new DatagramPacket(recebeDados, recebeDados.length);

				// Aguarda o recebimento de uma mensagem
				serverSocket.receive(recebePacote);

				DatagramPacket sender = recebePacote;

				/* Transforma a mensagem em string */
				// String tabela_string = new String(recebePacote.getData());

				/* Obtem o IP de origem da mensagem */
				// InetAddress IPAddress = recebePacote;

				System.out.println(
						"Recebendo: " + recebePacote.getAddress().getHostAddress() + ":" + recebePacote.getPort());

				// Atualiza dados na tabela
				// synchronized (TabelaRoteamento) {
				tabela.atualizaTabela(new String(recebePacote.getData()).trim(), sender);
				// }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
