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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CHAT_GUI extends JFrame {
	
	private JPanel jpMain;
	CHATBOX chatbox;
	public static Socket socket;
	public static String dest_IP;
	public static InetAddress dest_IP_num;
	
	public static String port;
	public static int port_num;
	
	public static InetAddress myAddress;
	public static DatagramPacket packet;
	public static String msg;
	
	public CHAT_GUI(Socket socket, InetAddress dest_IP_num, int port_num){
		this.socket = socket;
		this.dest_IP_num = dest_IP_num;
		this.port_num = port_num;
		try {
			this.myAddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		jpMain = new JPanel();
		jpMain.setLayout(new BorderLayout());
		chatbox = new CHATBOX();
		jpMain.add(chatbox);
		
		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(CHAT_GUI.DISPOSE_ON_CLOSE);
		add(jpMain);
	}
	
	public class CHATBOX extends JPanel implements KeyListener {
		private JLabel prompt;
		private JTextArea outputTF;
		private JTextArea inputTF;

		public CHATBOX(){
			setLayout(new BorderLayout());
			prompt = new JLabel("Now connected to "+ "IP: " + dest_IP_num + " on Port: " + port_num);
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
		public JTextArea getText(){
			return this.outputTF;
		}
		@Override
		public void keyPressed(KeyEvent e) {//send()
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				msg = "\n" + myAddress + ": " + inputTF.getText();
				outputTF.append(msg);
				socket.send(msg, dest_IP_num, port_num);
				//System.out.println("Message: "+ inputTF.getText() + " sent to " + "IP: " + dest_IP_num + " on Port: " + port_num);
				inputTF.setText("");//clear input box
				inputTF.setCaretPosition(-1);//put cursor back where it was
			}
		}
		@Override public void keyTyped(KeyEvent e) {} 
		@Override public void keyReleased(KeyEvent e) {}
	}
}
