package ui;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.CodeSource;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import logic.RabinKarp;

public class Display extends JFrame{
	
	private static final long serialVersionUID = -7470395919945318538L;
	private JButton compare = new JButton("Compare");
	private JButton reset = new JButton("Reset"); 
	private JButton proc = new JButton("Processing");
	private JButton help = new JButton("About");
	private JButton browse1;
	private JButton browse2;
	private JButton reverse;
	private JTextField text_field_1 = new JTextField(10);
	private JTextField text_field_2 = new JTextField(10);
	private JTextField text_field_3 = new JTextField(2);
	private JTextArea answer = new JTextArea(2, 10);
	private JTextArea txtar = new JTextArea(2, 10);
	JFileChooser chooser1 = new JFileChooser();
	JFileChooser chooser2 = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("text files", "txt");
	Font font = new Font("Sans", Font.BOLD, 12);
	Font font1 = new Font("Serif", Font.PLAIN, 12);
	Font font2 = new Font("Serif", Font.PLAIN, 14);
	JScrollPane scrollpane1 = new JScrollPane(txtar);
	JScrollPane scrollpane2 = new JScrollPane(answer);
	private String action[] = {"N-Grams", "Lines"};
	private JComboBox<String> cbox = new JComboBox<String>(action);
	private JLabel lbl1 = new JLabel();
	private JLabel lbl2 = new JLabel();
	private JLabel lbl3 = new JLabel();
	private JLabel lbl4 = new JLabel(); //for n grams
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel panel3 = new JPanel();
	private JPanel panel4 = new JPanel();
	private JPanel board = new JPanel();
	private JFrame procframe = new JFrame();
	private JPanel procpanel = new JPanel();
	
	private static double total_length_1 = 0, total_length_2 = 0, match_length = 0, result_1 = 0, result_2 = 0;
	private static int opt = 0, n_grams;
	private static String grams[] = null;
	//private static RabinKarp rk = new RabinKarp();
	private static String text_1 = "", text_2 = "", s = "";
	
	class CBoxActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			int n = cbox.getSelectedIndex();
			if(n==0) {
				text_field_3.setEditable(true);
			} else {
				text_field_3.setText("");
				text_field_3.setEditable(false);
			}
		}
	}
	
	class ButtonListener1 implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			long start_time = System.currentTimeMillis();
			if(txtar.getText().length()!=0)
			{
				txtar.setText(null);
			}
			if(answer.getText().length()!=0)
			{
				answer.setText(null);
			}
			
			if(text_field_1.getText().length()==0||text_field_2.getText().length()==0)
			{
				txtar.setText("Error!\n--one or more fields are empty--");
				answer.setText("Error!\n--one or more fields are empty--");
			}else {
				total_length_1 = text_1.trim().split(" ").length;
				total_length_2 = text_2.trim().split(" ").length;
				match_length = 0;
				opt = cbox.getSelectedIndex();
				int get = 0;
				RabinKarp rk = new RabinKarp();
				
				if(opt==0) {
					try {
						n_grams = Integer.parseInt(text_field_3.getText());
						if(n_grams==0) {
							n_grams = 4;
						}
					} catch(NumberFormatException ex) {
						text_field_3.setText("4");
						n_grams = 4;
					}
					
					grams = rk.nGrams(text_1, n_grams);
					
					for(int i=0;i<grams.length;i++){
						get = rk.match(text_2,grams[i]);
						if(get>0) {
							txtar.append(grams[i] + "\t[x" + get + "]\n");
							match_length+= (grams[i].trim().split(" ").length);
						}
					}
				}else if(opt==1) {
					grams = rk.lines(text_1);
					
					for(int i=0;i<grams.length;i++){
						get = rk.match(text_2, grams[i]);
						if(get>0) {
							txtar.append(grams[i] + "\t[x" + get + "]\n");
							match_length+= (grams[i].trim().split(" ").length);
						}
					}
				}
				result_1=Math.round(((match_length/total_length_1)*100)*100); // calculating relative percentage
				result_1 = result_1/100;
				result_2=Math.round(((match_length/total_length_2)*100)*100); // calculating relative percentage
				result_2 = result_2/100;
				long end_time = System.currentTimeMillis();
				answer.setText(result_1 + "% [" + text_field_1.getText() +  "]\t<=====>\t" + result_2 + "% [" + text_field_2.getText()
						+ "]\n\nSize of files:\n" + text_field_1.getText() + ": " + (int)total_length_1 
						+ " (words)\t" + text_field_2.getText() + ": " + (int)total_length_2 + " (words)\n\nTotal no. of matches found: " + (int)match_length
						+ " (words)\n\nTotal execution time: " + (end_time-start_time) + " (ms)");
			}
		}
	}
	
	class ButtonListener2 implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			cbox.setSelectedIndex(0);
			text_field_1.setText(null);
			text_field_2.setText(null);
			text_field_3.setText(null);
			txtar.setText(null);
			answer.setText(null);
		}
	}
	
	class ButtonListener3 implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			GroupLayout proclayout = new GroupLayout(procpanel);
			procpanel.setLayout(proclayout);
			
			procframe.setTitle("Processing");
			procframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			procframe.setResizable(false);
			procframe.setSize(600, 400);
			
			procpanel.setBackground(Color.WHITE);
			txtar.setEditable(false);
			txtar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			txtar.setCaretPosition(txtar.getDocument().getLength());
			//txtar.setBackground(Color.lightGray);
			//txtar.setForeground(Color.WHITE);
			txtar.setFont(font2);
			scrollpane1.setPreferredSize(new Dimension(400, 200));
			proclayout.setAutoCreateGaps(true);
			proclayout.setAutoCreateContainerGaps(true);
			proclayout.setHorizontalGroup(
					proclayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(proclayout.createSequentialGroup()
								.addComponent(scrollpane1))
					);
			proclayout.setVerticalGroup(
					proclayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(proclayout.createSequentialGroup()
							.addComponent(scrollpane1))
					);
			procframe.add(procpanel);
			procframe.setVisible(true);
		}
	}
	
	class ButtonListener4 implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			final File f = new File(Display.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			String path = ""+f;
			path = path.replace("CribAgin.jar", "");
			try {
				Runtime.getRuntime().exec("cmd.exe /C start AcroRD32 " + path + "About.pdf");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				try {
					Runtime.getRuntime().exec("evince " + path + "About.pdf");
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		}
	}
	
	class ButtonListener5 implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			chooser1.setFileFilter(filter);
			chooser1.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			 int returnVal = chooser1.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION)
			    {
			    	text_field_1.setText(chooser1.getSelectedFile().getName());
			    	BufferedReader reader1 = null;
			    	text_1 = "";
			    	s = "";
			    	
					try {
						reader1 = new BufferedReader(new FileReader(chooser1.getSelectedFile().getCanonicalPath()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						while((s = reader1.readLine())!=null)
						{
							if(s.length()>0) {
								text_1 = text_1 + s + "\n";
							}
						}
						text_1 = text_1.trim().replaceAll(" +", " ");
						text_1 = text_1.replaceAll("[.!?\\-,;']", "");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
		}
	}
	
	class ButtonListener6 implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			chooser2.setFileFilter(filter);
			chooser2.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			 int returnVal = chooser2.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) 
			    {
			    	text_field_2.setText(chooser2.getSelectedFile().getName());
			    	BufferedReader reader2 = null;
			    	text_2 = "";
			    	s = "";
					try {
						reader2 = new BufferedReader(new FileReader(chooser2.getSelectedFile().getCanonicalPath()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						while((s = reader2.readLine())!=null)
						{
							if(s.length()>0) {
								text_2 = text_2 + s + "\n";
							}
						}
						text_2 = text_2.trim().replaceAll(" +", " ");
						text_2 = text_2.replaceAll("[.!?\\-,;']", "");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    }
		}
	}
	
	class ButtonListener7 implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String temp=null;
			temp = text_field_1.getText();
			text_field_1.setText(text_field_2.getText());	
			text_field_2.setText(temp);
			temp = text_1;
			text_1 = text_2;
			text_2 = temp;
		}
	}
	
	private CBoxActionListener cbl = new CBoxActionListener();
	private ButtonListener1 bl1 = new ButtonListener1(); 
	private ButtonListener2 bl2 = new ButtonListener2(); 
	private ButtonListener3 bl3 = new ButtonListener3(); 
	private ButtonListener4 bl4 = new ButtonListener4(); 
	private ButtonListener5 bl5 = new ButtonListener5(); 
	private ButtonListener6 bl6 = new ButtonListener6(); 
	private ButtonListener7 bl7 = new ButtonListener7();
	
	public Display() throws UnsupportedEncodingException, URISyntaxException
	{
		cbox.addActionListener(cbl);
		compare.addActionListener(bl1);
		reset.addActionListener(bl2);
		proc.addActionListener(bl3);
		help.addActionListener(bl4);
		CodeSource codeSource = Display.class.getProtectionDomain().getCodeSource();
		File jarFile = new File(codeSource.getLocation().toURI().getPath());
		String jarDir = jarFile.getParentFile().getPath();
		/*final File f = new File(Display.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String path = ""+f;
		path = URLDecoder.decode(path, "UTF-8");
		path = path.replace("CribAgin.jar", "");*/
		browse1 = new JButton(new ImageIcon(((new ImageIcon(jarDir + "/images/open.png").getImage().getScaledInstance(20, 20,java.awt.Image.SCALE_SMOOTH)))));
		browse2 = new JButton(new ImageIcon(((new ImageIcon(jarDir + "/images/open.png").getImage().getScaledInstance(20, 20,java.awt.Image.SCALE_SMOOTH)))));
		reverse = new JButton(new ImageIcon(((new ImageIcon(jarDir + "/images/switchicon.png").getImage().getScaledInstance(20, 20,java.awt.Image.SCALE_SMOOTH)))));
		browse1.addActionListener(bl5);
		browse2.addActionListener(bl6);
		reverse.addActionListener(bl7);
		this.setIconImage(new ImageIcon(((new ImageIcon(jarDir + "images/favicon.png").getImage().getScaledInstance(20, 20,java.awt.Image.SCALE_SMOOTH)))).getImage());
		cbox.setBackground(Color.WHITE);
		cbox.setSelectedIndex(0);
		cbox.setToolTipText("select the text comparison technique");
		lbl1.setText("Select Split Criteria: ");
		lbl2.setText("Test Document 1: ");
		//lbl2.setForeground(Color.WHITE);
		lbl3.setText("Test Document 2: ");
		//lbl3.setForeground(Color.WHITE);
		lbl4.setText("N-Grams: ");
		text_field_1.setToolTipText("first test document here");
		text_field_1.setFont(font);
		text_field_2.setToolTipText("second test document here");
		text_field_2.setFont(font);
		text_field_3.setToolTipText("size of grams here");
		text_field_3.setFont(font);
		answer.setEditable(false);
		answer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		answer.setCaretPosition(txtar.getDocument().getLength());
		answer.setForeground(Color.BLUE);
		answer.setFont(font1);
		scrollpane2.setPreferredSize(new Dimension(400, 150));
		GroupLayout layoutmain = new GroupLayout(board);
		board.setLayout(layoutmain);
		//board.setBackground(Color.BLUE);
		GroupLayout layout1 = new GroupLayout(panel1);
		panel1.setLayout(layout1);
		//panel1.setBackground(Color.YELLOW);
		GroupLayout layout2 = new GroupLayout(panel2);
		panel2.setLayout(layout2);
		//panel2.setBackground(Color.RED);
		GroupLayout layout3 = new GroupLayout(panel3);
		panel3.setLayout(layout3);
		//panel3.setBackground(Color.RED);
		GroupLayout layout4 = new GroupLayout(panel4);
		panel4.setLayout(layout4);
		//panel4.setBackground(Color.GREEN);
		
		layoutmain.setAutoCreateGaps(true);
		layoutmain.setAutoCreateContainerGaps(true);
		layout1.setAutoCreateGaps(true);
		layout1.setAutoCreateContainerGaps(true);
		layout2.setAutoCreateGaps(true);
		layout2.setAutoCreateContainerGaps(true);
		layout3.setAutoCreateGaps(true);
		layout3.setAutoCreateContainerGaps(true);
		layout4.setAutoCreateGaps(true);
		layout4.setAutoCreateContainerGaps(true);
		
		layoutmain.setHorizontalGroup(
				   layoutmain.createSequentialGroup()
				      .addGroup(layoutmain.createParallelGroup(GroupLayout.Alignment.CENTER)
				           .addComponent(panel1)
				           .addComponent(panel2)
				           .addComponent(panel3)
				           .addComponent(panel4))
				           );
		layoutmain.setVerticalGroup(
				   layoutmain.createSequentialGroup()
				      .addGroup(layoutmain.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(panel1))
			          .addGroup(layoutmain.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(panel2))
			          .addGroup(layoutmain.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(panel3))
			          .addGroup(layoutmain.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(panel4))
				           );
		
		layout1.setHorizontalGroup(
				layout1.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layout1.createSequentialGroup()
						.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(lbl1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(30, 30, 30)
						.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(cbox, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
						.addGap(30, 30, 30)
						.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(lbl4, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(30, 30, 30)
						.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(text_field_3, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
				);
		layout1.setVerticalGroup(
				layout1.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addGroup(layout1.createSequentialGroup()
						.addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lbl1)
								.addComponent(cbox)
								.addComponent(lbl4)
								.addComponent(text_field_3)))
				);
		
		layout2.setHorizontalGroup(
				layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout2.createSequentialGroup()
						.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(lbl2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(30, 30, 30)
						.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(text_field_1, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
						.addGap(30 , 30, 30)
						.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(browse1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
				);
		layout2.setVerticalGroup(
				layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout2.createSequentialGroup()
						.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lbl2)
								.addComponent(text_field_1)
								.addComponent(browse1, GroupLayout.Alignment.CENTER)))
				);
		
		layout3.setHorizontalGroup(
				layout3.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout3.createSequentialGroup()
						.addGroup(layout3.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(lbl3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(30, 30, 30)
						.addGroup(layout3.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(text_field_2, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
						.addGap(30 , 30, 30)
						.addGroup(layout3.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(browse2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
				);
		layout3.setVerticalGroup(
				layout3.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout3.createSequentialGroup()
						.addGroup(layout3.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(lbl3)
								.addComponent(text_field_2)
								.addComponent(browse2, GroupLayout.Alignment.CENTER)))
				);
		
		layout4.setHorizontalGroup(
				layout4.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout4.createSequentialGroup()
						.addGroup(layout4.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(reset, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
						.addGap(20, 20, 20)
						.addGroup(layout4.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(compare, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout4.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(reverse))
						.addGroup(layout4.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(proc, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
						.addGap(20 , 20, 20)
						.addGroup(layout4.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(help, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)))
				.addGroup(layout4.createSequentialGroup()
						.addComponent(scrollpane2))
				);
		layout4.setVerticalGroup(
				layout4.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout4.createSequentialGroup()
						.addGroup(layout4.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(reset)
								.addComponent(compare)
								.addComponent(reverse)
								.addComponent(proc)
								.addComponent(help))
						.addGroup(layout4.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(scrollpane2)))
				);
		add(board);
	}
}