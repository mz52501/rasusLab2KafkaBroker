package hr.fer.tel.rassus.stupidudp.client;

public class CvorStruktura {
	public String id;
	public String address;
	public String port;
	public CvorStruktura(String id, String address, String port) {
		super();
		this.id = id;
		this.address = address;
		this.port = port;
	}
	public String getId() {
		return id;
	}
	public String getAddress() {
		return address;
	}
	public String getPort() {
		return port;
	}
	
	
}
