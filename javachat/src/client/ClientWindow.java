package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientWindow {

	private JFrame frmChatProgram;
	private JTextField messageField;

	private static JTextArea textArea = new JTextArea();
	
	private Client client;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ClientWindow window = new ClientWindow();
					window.frmChatProgram.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientWindow() {
		initialize();
		String name = JOptionPane.showInputDialog("Enter name");
		client=new Client(name,"192.168.50.37",8657);
	}

	private void initialize() {
		frmChatProgram = new JFrame();
		frmChatProgram.setTitle("Chat Program");
		frmChatProgram.setBounds(100, 100, 738, 527);
		frmChatProgram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatProgram.getContentPane().setLayout(new BorderLayout(0, 0));

		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		frmChatProgram.getContentPane().add(scrollPane);
		
		JPanel panel = new JPanel();
		frmChatProgram.getContentPane().add(panel, BorderLayout.SOUTH);
		
		messageField = new JTextField();
		panel.add(messageField);
		messageField.setColumns(50);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(e->{
			if(!messageField.getText().equals("")){
				client.send(messageField.getText());
				messageField.setText("");
			}
			
				
		});
		panel.add(btnSend);
		
		frmChatProgram.setLocationRelativeTo(null);
	}
	public static void printToconsole(String message) {
		textArea.setText(textArea.getText()+message+"\n");
	}
}
