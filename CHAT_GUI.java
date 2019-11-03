package UDPSocket;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CHAT_GUI extends JFrame {
	
	private JPanel jpMain;
	private CHATBOX chatbox;
	
	public CHAT_GUI(){
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
		private JTextArea outputLBL;
		private JTextArea inputTF;
		private JButton btn;
		
		public CHATBOX(){
			setLayout(new BorderLayout());
			
			outputLBL = new JTextArea();
			outputLBL.setEditable(false);
			add(outputLBL, BorderLayout.NORTH);
			
			inputTF = new JTextArea();
			inputTF.addKeyListener(this);
			add(inputTF, BorderLayout.SOUTH);
		}
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				outputLBL.append("\nIPADDRESSHERE: " + inputTF.getText());
				inputTF.setText(null);
				inputTF.setCaretPosition(-1);
			}
		}
		@Override public void keyTyped(KeyEvent e) {} 
		@Override public void keyReleased(KeyEvent e) {}
	}
	
}
