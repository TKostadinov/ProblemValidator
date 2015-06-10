import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Starter {

	public static void main(String[] args) {
		new MainScreen();


	}

	private static String removeScanner(String line) {
		if(line.contains("Scanner")) return "";
		return line;
	}

	private static String replaceWriteConsole(String line) {
		if(line.contains("println")) {
			int skobaIndex = line.indexOf("println(");
			return "Console.WriteLine(" + line.substring(skobaIndex + 8, line.length());
		}
		if(line.contains("print")) {
			int skobaIndex = line.indexOf("print(");
			return "Console.Write(" + line.substring(skobaIndex + 6, line.length());
		}
		return line;
	}

	private static String replaceReadConsole(String line) {
		if(line.contains("nextLine")) {
			int equalsIndex = line.indexOf("=");
			return line.substring(0, equalsIndex + 1) + "Console.ReadLine();";
		}
		
		if(line.contains("nextByte")) {
			line = line.replace("byte", "int").replace("nextByte", "nextInt");
		}
		
		if(line.contains("nextShort")) {
			line = line.replace("short", "int").replace("nextShort", "nextInt");
		}
		
		if(line.contains("nextInt")) {
			int equalsIndex = line.indexOf("=");
			return line.substring(0, equalsIndex + 1) + "Convert.ToInt32(Console.ReadLine());";
		}
		return line;
	}
}
