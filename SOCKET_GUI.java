package UDPSocket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
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
	public static InetAddress bc_IP_num;
	public static InetAddress dest_IP_num;
	public static DatagramPacket inPacket = null;
	public static String dest_IP_name;
	
	public static String msg;
	
	public static int port_num;
	public static String port;
	
	public static InetAddress address;
	public static String myAddress;
	
	public static String myName = "Jonathan";
	public static String src;
	public static String dest;
	
	public SOCKET_GUI(){
		
		jpMain = new JPanel();
		jpMain.setLayout(new BorderLayout());
		
		menu = new MENU();
		jpMain.add(menu);
		
		setSize(500, 500);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(jpMain);	
		
		setVisible(true);
		
	}
	 
	public static void receive(){
		do {
			inPacket = socket.receive();
			if(inPacket != null){
				System.out.println("Packet: " + inPacket);
				byte[] inBuffer = inPacket.getData();
				dest_IP_num = inPacket.getAddress();
				System.out.println("Going to: " + dest_IP_num);
				port_num = inPacket.getPort();
				msg = new String(inBuffer);
				
				try {
					address = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				myAddress = address.getHostAddress();
								
				System.out.println("BEFORE: " + gotReq(msg));
				if(gotReq(msg) && dest.equalsIgnoreCase(myName)){
					System.out.println("AFTER: " + gotReq(msg));
					msg = "##### " + myName + " ##### " + myAddress;				
					socket.send(msg, dest_IP_num, port_num);
					System.out.println("SENT TO: " + dest_IP_num);
					if(!CHAT_ARR.containsKey(dest_IP_num)){
						CHAT_GUI NEW_CHAT = new CHAT_GUI(socket, dest_IP_num, port_num);
						CHAT_ARR.put(dest_IP_num, NEW_CHAT);
						NEW_CHAT.chatbox.getText().append("\n" + dest_IP_num + ": " + msg);
					}
				}				
				else if (gotRes(msg)){
					if(!CHAT_ARR.containsKey(dest_IP_num)){	
						CHAT_GUI NEW_CHAT = new CHAT_GUI(socket, dest_IP_num, port_num);
						CHAT_ARR.put(dest_IP_num, NEW_CHAT);
						NEW_CHAT.chatbox.getText().append("\n" + dest_IP_num + ": " + msg);
					}
					else {					
						CHAT_GUI curr = CHAT_ARR.get(dest_IP_num);
						curr.chatbox.getText().append("\n" + dest_IP_num + ": " + msg);
					}
				}
				else if(!CHAT_ARR.containsKey(dest_IP_num)){
					CHAT_GUI NEW_CHAT = new CHAT_GUI(socket, dest_IP_num, port_num);
					CHAT_ARR.put(dest_IP_num, NEW_CHAT);
					NEW_CHAT.chatbox.getText().append("\n" + dest_IP_num + ": " + msg);
				}
				else if (CHAT_ARR.containsKey(dest_IP_num)){
					CHAT_GUI curr = CHAT_ARR.get(dest_IP_num);
					curr.chatbox.getText().append("\n" + dest_IP_num + ": " + msg);
				}
			}
		} while(true);
	}
	
	private static boolean gotReq(String msg) {
		boolean isReq = false;
		if(msg.startsWith("?????")){
			String[] split = msg.split(" ");
			if(split[2].equalsIgnoreCase("#####")){
				dest = split[1];
				src = split[3];
				isReq = true;
			}
		}
		return isReq;
	}
	private static boolean gotRes(String msg){
		boolean isRes = false;
		if(msg.startsWith("#####")){
			String[] split = msg.split(" ");
			if(split[1].equalsIgnoreCase(dest) && split[2].equalsIgnoreCase("#####")){
				src = split[1];
				try {
					dest_IP_num = InetAddress.getByName(split[3]);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				isRes = true;
			}
		}
		return isRes;
	}
	
	private class MENU extends JPanel implements ActionListener {
		private JTextArea dest_IP_box;
		private JTextArea port_box;
		private JButton start_btn;
		
		public MENU(){
			dest_IP_box = new JTextArea(2, 35);
			port_box = new JTextArea(2, 35);
			start_btn = new JButton("Start");
			start_btn.addActionListener(this);
			start_btn.setEnabled(true);
			
			add(dest_IP_box);
			add(port_box);
			add(start_btn);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btnClicked = (JButton)e.getSource();
			if(btnClicked.equals(start_btn)){	
				dest = dest_IP_box.getText();
				port = port_box.getText();
				port_num = Integer.parseInt(port);
				try {
					dest_IP_num = InetAddress.getByName(dest);
					CHAT_GUI chat_gui = new CHAT_GUI(socket, dest_IP_num, port_num);
					CHAT_ARR.put(dest_IP_num, chat_gui);
				} 
								
				catch (UnknownHostException e1) {//if input !IP address
					bc_IP_num = null;
					try {
						bc_IP_num = InetAddress.getByName("255.255.255.255");
					} catch (UnknownHostException e2) {
						e2.printStackTrace();
					}
					msg = "????? " + dest + " ##### " + myName;
					socket.send(msg, bc_IP_num, port_num);
					System.out.println("Now unicasting: " + msg);
				}
				
			}
		}
	}
}
