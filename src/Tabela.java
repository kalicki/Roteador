
public class Tabela {
	private String ipDestino;
	private String ipSaida;
	private int metrica;

	public Tabela(String ipDestino, int metrica, String ipSaida) {
		this.ipDestino = ipDestino;
		this.ipSaida = ipSaida;
		this.metrica = metrica;
	}

	public String getIpDestino() {
		return ipDestino;
	}

	public void setIpDestino(String ipDestino) {
		this.ipDestino = ipDestino;
	}

	public String getIpSaida() {
		return ipSaida;
	}

	public void setIpSaida(String ipSaida) {
		this.ipSaida = ipSaida;
	}

	public int getMetrica() {
		return metrica;
	}

	public void setMetrica(int metrica) {
		this.metrica = metrica;
	}
}
