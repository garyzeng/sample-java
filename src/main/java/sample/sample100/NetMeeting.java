package sample.sample100;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;

public class NetMeeting extends JFrame{
	
	public NetMeeting(){
		super("NetMeeting II");
		Container c=getContentPane();
		this.setLayout(new BorderLayout());
		Box box=new Box(BoxLayout.Y_AXIS);
		JLabel label=new JLabel("Java Simple Is Beauty!!!");
		label.setFont(new Font("11",Font.BOLD,12));
		label.setForeground(Color.red);
		JTextField jtf=new JTextField();
		jtf.setFont(new Font("111",Font.BOLD,15));
		jtf.setText("127.0.0.1");
		JButton jbtn1=new JButton("<<<       呼叫      >>>");
		JButton jbtn2=new JButton("<<<       挂断      >>>");
		JButton jbtn3=new JButton("<<<       目录      >>>");
		JLabel label1=new JLabel("----------用户列表------------");
		JTextArea jta=new JTextArea(20,5);
		box.add(label);
		box.add(jtf);
		box.add(jbtn1);
		box.add(jbtn2);
		box.add(jbtn3);
		box.add(label1);
		box.add(new JScrollPane(jta));
		c.add(box);
		this.setSize(170,400);
		this.setVisible(true);
		this.setResizable(false);
		this.setLocation(700,100);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	public static void main(String[] args){
		new NetMeeting();
	}
}
