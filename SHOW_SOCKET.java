package UDPSocket;

public class SHOW_SOCKET {
	public static void main(String[]args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){//interface
			@Override
			public void run() {
				SOCKET_GUI SOCKET_GUI = new SOCKET_GUI();
			}
		});
	}
}
