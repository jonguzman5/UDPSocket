package UDPSocket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SOCKET_GUI extends JFrame {
	private JPanel jpMain;
	private MENU menu;
	private static Map<InetAddress, CHAT_GUI> CHAT_ARR = new HashMap<InetAddress, CHAT_GUI>();
	public static Socket socket = new Socket(64000);
	public static InetAddress dest_IP_num;
	public static int port_num;
	public static String rec_msg;
	public static String dest_IP_name;
	public MulticastSocket mult_socket;
	
	
	public SOCKET_GUI(){
		
		jpMain = new JPanel();
		jpMain.setLayout(new BorderLayout());
		
		menu = new MENU();
		jpMain.add(menu);
		
		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(jpMain);	
	}
	 
	public static void receive(){
		DatagramPacket inPacket = null;
		do {
			inPacket = socket.receive();
			//System.out.println(inPacket);
			if(inPacket != null){
				System.out.println(inPacket);
				byte[] inBuffer = inPacket.getData();
				dest_IP_num =  inPacket.getAddress();
				port_num = inPacket.getPort();
				rec_msg = new String(inBuffer);
				if(!CHAT_ARR.containsKey(dest_IP_num)){
					CHAT_GUI NEW_CHAT = new CHAT_GUI(socket, dest_IP_num, port_num);
					CHAT_ARR.put(dest_IP_num, NEW_CHAT);
					System.out.println("MADE IT HERE 1");
					NEW_CHAT.chatbox.getText().append("\n" + dest_IP_num + ": " + rec_msg);
					NEW_CHAT.setVisible(true);
				}
				else {
					CHAT_GUI curr = CHAT_ARR.get(dest_IP_num);
					System.out.println("MADE IT HERE 2");
					curr.chatbox.getText().append("\n" + dest_IP_num + ": " + rec_msg);
					curr.setVisible(true);	
				}
			}
		} while(inPacket == null);
	}
	public InetAddress broadcast(String name) {
		try {
			mult_socket = new MulticastSocket(port_num);
			//mult_socket.joinGroup(InetAddress.getLocalHost());
			System.out.println("Now broadcasting: " + name + "...");			
			//if connect { leaveGroup()
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private class MENU extends JPanel implements ActionListener {
		private JTextArea dest_IP;
		private JTextArea port;
		private JButton start;
		
		public MENU(){
			dest_IP = new JTextArea(2, 35);
			port = new JTextArea(2, 35);
			start = new JButton("Start");
			start.addActionListener(this);
			start.setEnabled(true);
			
			add(dest_IP);
			add(port);
			add(start);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btnClicked = (JButton)e.getSource();
			if(btnClicked.equals(start)){	
				String d = dest_IP.getText();
				String p = port.getText();
				try {
					dest_IP_num = InetAddress.getByName(d);
				} catch (UnknownHostException e1) {
					//e1.printStackTrace();
					//dest_IP_name = d;
					//dest_IP_num = broadcast(dest_IP_name);
				}
				port_num = Integer.parseInt(p);
				
				CHAT_GUI chat_gui = new CHAT_GUI(socket, dest_IP_num, port_num);
				CHAT_ARR.put(dest_IP_num, chat_gui);
			}
		}
	}
}
