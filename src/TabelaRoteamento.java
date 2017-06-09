import java.net.DatagramPacket;
import java.time.LocalDateTime;
import java.util.HashMap;

public class TabelaRoteamento {
	/*
	 * Implemente uma estrutura de dados para manter a tabela de roteamento. A
	 * tabela deve possuir: IP Destino, Métrica e IP de Saída.
	 */

	private HashMap<String, Rota> tabelaRoteamento;
	private int tempo = 0;

	public TabelaRoteamento() {
		this.tabelaRoteamento = new HashMap<String, Rota>();
	}

	/**
	 * Atualize a tabela de rotamento a partir da string recebida.
	 * 
	 * @param tabela_s
	 * @param sender
	 */
	/*
	 * if
	 * (this.tabelaRoteamento.get(ip_destino).getIpDestino().equalsIgnoreCase(
	 * endereco)) {
	 * 
	 * } if (myRoutingTable.get(tableEntry.getKey()) == null) { RoutingInfo
	 * newDest = new RoutingInfo(tableEntry.getValue().destPort,
	 * incommingPacket.source, tableEntry.getValue().hopCount +
	 * getCost(incommingPacket.source));
	 * myRoutingTable.put(tableEntry.getValue().destPort, newDest);
	 * 
	 * if (this.tabelaRoteamento.get(ip_destino) == null) {
	 * this.tabelaRoteamento.put(sender, new Rota(sender, 1, sender));
	 * 
	 * new RoutingInfo(tableEntry.getValue().destPort, incommingPacket.source,
	 * tableEntry.getValue().hopCount + getCost(incommingPacket.source)); }
	 * 
	 * if (!this.tabelaRoteamento.containsKey(ip_destino)) {
	 * this.tabelaRoteamento.put(ip_destino, new Rota(ip_destino, 1,
	 * ip_destino)); System.out.println("Inserção auxiliar"); }
	 * 
	 * if
	 * (!this.tabelaRoteamento.get(ip_destino).getIpDestino().equalsIgnoreCase(
	 * sender)) { System.out.println("IP destino != sender"); if
	 * (tabelaRoteamento.get(sender).getIpDestino().equals(sender) &&
	 * tabelaRoteamento.containsKey(sender) == false) {
	 * this.tabelaRoteamento.put(ip_destino, new Rota(ip_destino, metrica,
	 * sender));
	 * 
	 * } else if (this.tabelaRoteamento.containsKey(ip_destino)) { if
	 * (this.tabelaRoteamento.get(ip_destino).getMetrica() > metrica) {
	 * this.tabelaRoteamento.get(ip_destino).setMetrica(metrica);
	 * this.tabelaRoteamento.get(ip_destino).setIpDestino(sender);
	 * this.tabelaRoteamento.get(ip_destino).updateValidade(); } } else {
	 * this.tabelaRoteamento.put(ip_destino, new Rota(ip_destino, metrica + 1,
	 * sender)); } } }
	 */
	public void atualizaTabela(String tabelas, DatagramPacket dp) {
		String ip_saida = dp.getAddress().getHostAddress() + ":" + dp.getPort();
		System.out.println("IP: " + ip_saida);
		System.out.println("Mensagem: " + tabelas);

		if (tabelas.equals("!")) {
			this.tabelaRoteamento.put(ip_saida, new Rota(ip_saida, 1, ip_saida));
			System.out.println("Primeira Inserção");
		} else {
			for (String linha : tabelas.split("\\*")) {

				if (!linha.isEmpty() && linha != null) {
					String linhaSplit[] = linha.split(";");
					String ip_destino = linhaSplit[0];
					Integer metrica = Integer.parseInt(linhaSplit[1]);
					Rota rotaDestino = this.tabelaRoteamento.get(ip_destino); // .equalsIgnoreCase(endereco);

					if (rotaDestino != null && rotaDestino.getIpSaida().equalsIgnoreCase(ip_destino)) {
						System.out.println("Entrou aqui");

						// else if
						// ((myRoutingTable.get(tableEntry.getKey()).hopCount) >
						// (tableEntry.getValue().hopCount)
						// + getCost(incommingPacket.source)) {
						System.out.println("Metrica: " + metrica);
						System.out.println("rotaD Metrica: " + rotaDestino.getMetrica());

						if (metrica < rotaDestino.getMetrica()) {
							System.out.println("atualiza metrica");
							rotaDestino.setIpDestino(ip_saida);
							rotaDestino.setMetrica(rotaDestino.getMetrica() + 1);
							rotaDestino.updateValidade();
						}
					} else if (this.tabelaRoteamento.get(ip_saida) == null) {
						this.tabelaRoteamento.put(ip_saida, new Rota(ip_saida, 1, ip_destino));
					}

				}
			}
		}

		this.tempo += 10;
		if (tempo > 30) {
			System.out.println("remover");
			this.tempo = 0;
			this.limparTabela();
		}

		System.out.println(get_table());
		// System.out.println(sender + ": " + tabelas);
	}

	/**
	 * 
	 * @return
	 */
	public boolean limparTabela() {
		boolean removido = false;
		String rotaID;
		Rota rotaDados;

		for (HashMap.Entry<String, Rota> rota : this.tabelaRoteamento.entrySet()) {
			rotaID = rota.getKey();
			rotaDados = rota.getValue();

			if (rotaDados.getStatus().equalsIgnoreCase("Inativo")
					|| LocalDateTime.now().withNano(0).isEqual(rotaDados.getValidade()))
				if (this.tabelaRoteamento.remove(rotaID) != null)
					removido = true;
		}

		return removido;
	}

	/**
	 * Tabela de roteamento vazia conforme especificado no protocolo Converta a
	 * tabela de rotamento para string, conforme formato definido no protocolo
	 * 
	 * @return
	 */
	public String get_tabela_string() {
		if (this.tabelaRoteamento.isEmpty()) {
			return "!";
		}

		StringBuilder sb = new StringBuilder();
		for (Rota host : this.tabelaRoteamento.values()) {
			sb.append("*" + host.getIpDestino() + ";" + host.getMetrica());
		}

		return sb.toString().trim();
	}

	public String get_table() {
		StringBuilder sb = new StringBuilder();
		String fort = "%-20s | %-7s | %-20s | %-7s | %-3s \n";

		sb.append("\n");
		sb.append(String.format(fort, "Origem", "Métrica", "Destino", "Status", "Validade"));
		sb.append("---------------------|---------|----------------------|---------|------------------ \n");

		for (Rota host : tabelaRoteamento.values())
			sb.append(String.format(fort, host.getIpDestino(), host.getMetrica(), host.getIpSaida(), host.getStatus(),
					host.getValidade()));

		sb.append("---------------------|---------|----------------------|---------|------------------ \n");

		return sb.toString();
	}
}
