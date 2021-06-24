package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import Map.map;

public class Lifegame extends JFrame implements MouseMotionListener{
	private  map world;
	static JMenu location=new JMenu();
	static JTextField jt1=new JTextField("40",8),jt2=new JTextField("50",8),jt3=new JTextField(4);
	static JPanel jp1=new JPanel();	
	static	JLabel jb1=new JLabel("行:"),jb2=new JLabel("列:"),jb3=new JLabel("速度:"),jb4=new JLabel("代数：");
	static	JComboBox cbox=new JComboBox();
	static	JButton jbot=new JButton("确认");
	public Lifegame(int rows,int columns)
	{
		world=new map(rows, columns);
		world.setBackground(Color.LIGHT_GRAY);
		new Thread(world).start();
		add(world);
	}
	public static void main(String[]args)
	{
		Lifegame frame=new Lifegame(40, 50);
		
		frame.addMouseMotionListener(frame);
		JMenuBar menu=new JMenuBar();
		frame.setJMenuBar(menu);
		
		
		JMenu options =new JMenu("Options");
		menu.add(options);
		JMenu changeSpeed=new JMenu("ChangeSpeed");
		menu.add(changeSpeed);
		JMenu other = new JMenu("Other");
		menu.add(other);
		
		JMenuItem start=options.add("Start");
		start.addActionListener(frame.new StartActionListener());
		JMenuItem random=options.add("Random");
		random.addActionListener(frame.new RandomActionListener());
		
		JMenuItem stop=options.add("Stop");
		stop.addActionListener(frame.new StopActionListener());
		JMenuItem pause=options.add("Pause");
		pause.addActionListener(frame.new PauseActionListener());
		JMenuItem doityourself=options.add("Add");
		doityourself.addActionListener(frame.new DIYActionListener());
		JMenuItem clean=options.add("Kill");
		clean.addActionListener(frame.new CleanActionListener());
		
		JMenuItem slow=changeSpeed.add("Slow");
		slow.addActionListener(frame.new SlowActionListener());
		JMenuItem fast=changeSpeed.add("Fast");
		fast.addActionListener(frame.new FastActionListener());
		JMenuItem hyper=changeSpeed.add("Hyper");
		hyper.addActionListener(frame.new HyperActionListener());
		
		JMenuItem help=other.add("Help");
		help.addActionListener(frame.new HelpActionListener());
		
		JMenuItem about=other.add("About");
		about.addActionListener(frame.new AboutActionListener());
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1007,830);
		frame.setTitle("Game of Life");
		frame.setVisible(true);
		frame.setResizable(false);
			
		int[] a=new int[8];
		for(int i=0;i<8;i++) {
			a[i]=i+1;
			cbox.addItem(a[i]);		
		}
			

		jbot.addActionListener(frame.new OkActionListener());
		
		
		jp1.add(jb1);
		jp1.add(jt1);
		jp1.add(jb2);
		jp1.add(jt2);
		jp1.add(jb3);
		jp1.add(cbox);
		jp1.add(jb4);
		jp1.add(jt3);
		jp1.add(jbot);
		frame.add(jp1,BorderLayout.SOUTH);
		
	}
class OkActionListener implements  ActionListener{
	public void actionPerformed(ActionEvent e) {
		int h,l,s,limit;
		h=Integer.parseInt(jt1.getText());
		l=Integer.parseInt(jt2.getText());
		s=(int)cbox.getSelectedItem();
		limit=Integer.parseInt(jt3.getText());
		remove(world);
		world=new map(h, l);
		world.setBackground(Color.LIGHT_GRAY);
		new Thread(world).start();
		add(world);
		world.setspeed(s);
		world.setlimit(limit);
	}
}
	class RandomActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.diy=false;
			world.clean=false;
			world.setBackground(Color.LIGHT_GRAY);
			world.setRandom();
		}
	}
	class StartActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.setBackground(Color.LIGHT_GRAY);
			world.diy=false;
			world.clean=false;
			world.setShape();
		}
	}
	class StopActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.setBackground(Color.LIGHT_GRAY);
			world.diy=false;
			world.clean=false;
			world.setStop();
		}
	}
	class PauseActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.setBackground(Color.LIGHT_GRAY);
			world.diy=false;
			world.clean=false;
			world.setPause();
		}
	}
	class SlowActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.changeSpeedSlow();
		}
	}
	class FastActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.changeSpeedFast();
		}
	}
	class HyperActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.changeSpeedHyper();
		}
	}
	class HelpActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			JOptionPane.showMessageDialog(null, "这是生命游戏!!!\n生命游戏是英国数学家约翰·何顿·康威在1970年发明的细胞自动机\n "+"1．如果一个细胞周围有3个细胞为生，则该细胞为生;\n"
												+"2． 如果一个细胞周围有2个细胞为生，则该细胞的生死状态保持不变;\n"
												+"3． 在其它情况下，该细胞为死。");
		}
	}
	class AboutActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			JOptionPane.showMessageDialog(null, "游戏作者：季雨轩，火兴涛");
		}
	}
	class CleanActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.setPause();
			world.clean=true;
			world.diy=false;
			world.setBackground(Color.orange);
		}
	}
	class DIYActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.setPause();
			world.diy=true;
			world.clean=false;
			world.setBackground(Color.cyan);
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(world.diy){
		int x=e.getX();
		int y=e.getY();
		map.pauseshape[(y-50)/20][x/20]=1;
		world.setDiy();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(world.clean){
		int x=e.getX();
		int y=e.getY();
		map.pauseshape[(y-50)/20][x/20]=0;
		world.setDiy();
		}
	}
}
