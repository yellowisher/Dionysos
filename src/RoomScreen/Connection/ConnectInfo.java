package RoomScreen.Connection;

public class ConnectInfo {
	public static final int JOIN = 0;
	public static final int CREATE = 1;
	
	public String ip;
	public int port;
	private int role;
	
	public ConnectInfo(String ip, int port, int role){
		this.ip = ip;
		this.port = port;
		this.role = role;
	}
	
	public String getIp(){return ip;}
	public int getPort(){return port;}
	public int getRole(){return role;}
}
