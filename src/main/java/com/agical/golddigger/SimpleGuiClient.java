package com.agical.golddigger;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class SimpleGuiClient extends JFrame {
	public static JTextField user;
	public static JTextField server;
	public static JTextField port;
	public static JTextArea result;
    private static WebConversation wc = new WebConversation();
	JPanel config = new JPanel(new FlowLayout());
	JPanel general = new JPanel(new FlowLayout());
	JPanel move4 = new JPanel(new FlowLayout());
	JPanel move6 = new JPanel(new FlowLayout());
	JTabbedPane commands = new JTabbedPane();
	JPanel logs = new JPanel(new FlowLayout());
	
	public SimpleGuiClient(){
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		user = new JTextField(12);
		config.add(new JLabel("User:"));
		config.add(user);
		server = new JTextField("localhost",20);
		config.add(new JLabel("IP Address:"));
		config.add(server);
		port = new JTextField("8066",5);
		config.add(new JLabel("Port:"));
		config.add(port);
		
		result = new JTextArea(10,50);
		logs.add(new JScrollPane(result));
		
		add(config);
		add(commands);
		add(logs);
		initCommands();
		pack();
	}
	
	public void initCommands(){
		JPanel general = new JPanel();
		general.add(button("View", "/view"));
		general.add(button("Grab", "/grab"));
		general.add(button("Drop", "/drop"));
		general.add(button("Next Map","/next"));
		general.add(button("Carrying","/carrying"));
		general.add(button("Score","/score"));
		commands.add(general, "General");
		
		JPanel move4 = new JPanel();
		move4.add(timed_button("North", "/move/north"));
		move4.add(timed_button("South", "/move/south"));
		move4.add(timed_button("East", "/move/east"));
		move4.add(timed_button("West", "/move/west"));
		commands.add(move4, "Square Tile Movement");
		
		JPanel move6 = new JPanel();
		move6.add(timed_button("North", "/move/north"));
		move6.add(timed_button("South", "/move/south"));
		move6.add(timed_button("North East", "/move/north-east"));
		move6.add(timed_button("South East", "/move/south-east"));
		move6.add(timed_button("North West", "/move/north-east"));
		move6.add(timed_button("South West", "/move/south-west"));
		commands.add(move6, "Hex Tile Movement");
	}
	
	public static void main(String[] args) {
		new SimpleGuiClient();
	}
	
	public static String call(String url){
		WebResponse r;
		String result;
		try {
			r = wc.getResponse(url);
			result = "Status:"+r.getResponseCode()+" - "+r.getResponseMessage() + "\n";
			result += r.getText();
		} catch (Exception e){
			result = "Error:\n"+e.getMessage();
		}
		return result;
	}

	static private JButton button(String name, final String cmd){
		JButton button = new JButton(name);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String url = "http://"+server.getText()+":"+port.getText();
				url += "/golddigger/digger/"+user.getText() + cmd;
				result.setText(call(url));
			}
		});
		return button;
	}
	
	static private JButton timed_button(String name, final String cmd){
		JButton button = new JButton(name);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String output, url = "http://"+server.getText()+":"+port.getText();
				url += "/golddigger/digger/"+user.getText();
				url += cmd;
				long start = System.currentTimeMillis();
				output = call(url);
				long stop = System.currentTimeMillis();
				output += "Reply Time: "+(stop-start)+"ms";
				result.setText(output);
			}
		});
		return button;
	}
}
