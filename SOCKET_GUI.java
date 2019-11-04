package UDPSocket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SOCKET_GUI extends JFrame {
	private JPanel jpMain;
	private MENU menu;
	
	public SOCKET_GUI(){
		jpMain = new JPanel();
		jpMain.setLayout(new BorderLayout());
		
		menu = new MENU();
		//menu.setBackground(Color.GREEN);
		jpMain.add(menu);
		
		setSize(500, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(jpMain);
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
			CHAT_GUI chat_gui = new CHAT_GUI(dest_IP.getText(), port.getText());
			if(btnClicked.equals(start)){	
				chat_gui.setVisible(true);
			}
		}
	}
}
