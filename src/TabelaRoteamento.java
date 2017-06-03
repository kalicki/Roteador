import java.util.HashMap;

public class TabelaRoteamento {
	/*
	 * Implemente uma estrutura de dados para manter a tabela de roteamento. A
	 * tabela deve possuir: IP Destino, Métrica e IP de Saída.
	 */

	private HashMap<String, Tabela> tabela;

	public TabelaRoteamento() {
		this.tabela = new HashMap<String, Tabela>();
	}

	/**
	 * Atualize a tabela de rotamento a partir da string recebida.
	 * 
	 * @param tabela_s
	 * @param sender
	 */
	public void atualizaTabela(String tabelas, String sender) {
		if (tabelas.equals("!") && tabela.containsKey(sender)) {
			System.out.println("valido");
			this.tabela.put(sender, new Tabela(sender, 1, sender));
		} else {
			String[] linhas = tabelas.split("\\*");

			for (String linha : linhas) {
				String ip_destino = linha.split(";")[0];
				Integer metrica = Integer.parseInt(linha.split(";")[1]);

				if (this.tabela.containsKey(ip_destino)) {
					if (metrica < this.tabela.get(ip_destino).getMetrica()) {
						this.tabela.get(ip_destino).setMetrica(metrica);
						this.tabela.get(ip_destino).setIpDestino(sender);
					}
				} else {
					this.tabela.put(ip_destino, new Tabela(ip_destino, metrica + 1, sender));
				}
			}

		}

		System.out.println(sender + ": " + tabelas);
	}

	/**
	 * Tabela de roteamento vazia conforme especificado no protocolo Converta a
	 * tabela de rotamento para string, conforme formato definido no protocolo
	 * 
	 * @return
	 */
	public String get_tabela_string() {
		if (tabela.size() < 1) {
			return "!";
		}
		String resposta = "";
		for (Tabela host : tabela.values()) {
			resposta += "*";
			resposta += host.getIpDestino();
			resposta += ";";
			resposta += host.getMetrica();
		}

		return resposta;
	}
}
