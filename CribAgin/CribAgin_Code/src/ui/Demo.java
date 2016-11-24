package ui;

import java.io.*;
import java.net.URISyntaxException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import static ui.SwingConsole.*;
import ui.Display;

public class Demo {

	public static void main(String args[]) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, URISyntaxException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		run(new Display() ,500,275);
	}
}
