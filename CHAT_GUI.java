package UDPSocket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CHAT_GUI extends JFrame {
	
	private JPanel jpMain;
	private CHATBOX chatbox;
	public static String dest_IP;
	public static InetAddress dest_IP_num;
	public static String port;
	public static int portNum;
	public static Socket socket;
	public static InetAddress myAddress;
	public static DatagramPacket packet;
	public static String msg;
	
	public void config(){
		portNum = Integer.parseInt(port);
		socket = new Socket(portNum);
		myAddress = socket.getMyAddress();
		try {
			dest_IP_num = InetAddress.getByName(dest_IP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public CHAT_GUI(String dest_IP, String port){
		this.dest_IP = dest_IP;
		this.port = port;
		config();
		jpMain = new JPanel();
		jpMain.setLayout(new BorderLayout());
		chatbox = new CHATBOX();
		jpMain.add(chatbox);
		
		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(jpMain);
	}
	
	public class CHATBOX extends JPanel implements KeyListener {
		private JLabel prompt;
		private JTextArea outputTF;
		private JTextArea inputTF;
		
		public CHATBOX(){
			//receive();
			
			setLayout(new BorderLayout());
			prompt = new JLabel("Now connected to "+ "IP: " + dest_IP + " on Port: " + port);
			add(prompt, BorderLayout.NORTH);
			
			outputTF = new JTextArea();
			outputTF.setEditable(false);
			add(outputTF, BorderLayout.CENTER);
			
			inputTF = new JTextArea("Enter text here");
			inputTF.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e){
					inputTF.setText("");
				}
			});
			inputTF.addKeyListener(this);
			add(inputTF, BorderLayout.SOUTH);
		}
		
		public void receive(){
			packet = socket.receive();
			if(packet != null){
				msg = new String(packet.getData());
				outputTF.append("\n" + packet.getAddress() + ": " + msg);
			}
		}
		
		@Override
		public void keyPressed(KeyEvent e) {//send()
			packet = socket.receive();
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				msg = "\n" + myAddress + ": " + inputTF.getText();
				outputTF.append(msg);
				socket.send(msg, dest_IP_num, portNum);
				//System.out.println("Message: "+ inputTF.getText() + " sent to " + "IP: " + dest_IP + " on Port: " + port);
				inputTF.setText("");//clear input box
				inputTF.setCaretPosition(-1);//put cursor back where it was
			}
			if(packet != null){
				msg = new String(packet.getData());
				outputTF.append("\n" + packet.getAddress() + ": " + msg);
			}
		}
		@Override public void keyTyped(KeyEvent e) {} 
		@Override public void keyReleased(KeyEvent e) {}
	}
}
