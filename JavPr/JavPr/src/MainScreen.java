import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class MainScreen {

	private static final String OUTPUT_TEXT_FILE = "output.txt";
	private static final String TESTS_DIR = "src\\tests\\";
	JTextPane edtJava;
	ArrayList<JRadioButton> radioButtons;
	JFrame guiFrame;
	JPanel testsContainer;
	ArrayList<String> testFolders;

	public MainScreen() {
		radioButtons = new ArrayList<JRadioButton>();

		// window
		guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Problem validator");
		guiFrame.setSize(1200, 700);
		guiFrame.setLocationRelativeTo(null);

		// text field
		final JPanel javaPanel = new JPanel();
		edtJava = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(edtJava);
		editorScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editorScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editorScrollPane.setPreferredSize(new Dimension(840, 650));
		editorScrollPane.setMinimumSize(new Dimension(100, 100));

		javaPanel.add(editorScrollPane);

		// control pannel
		final JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		setControlPanel(controlPanel);

		// adding panels to window
		guiFrame.add(javaPanel, BorderLayout.WEST);
		guiFrame.add(controlPanel, BorderLayout.EAST);
		guiFrame.setVisible(true);
	}

	private void setControlPanel(final JPanel controlPanel) {
		JButton btnRun = new JButton("Run Tests");
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				runTests();
			}
		});
		controlPanel.add(btnRun);

		testFolders = getTestFolders();

		if (testFolders.size() > 0) {
			ButtonGroup bG = new ButtonGroup();
			for (String testFolderDir : testFolders) {
				JRadioButton rad = new JRadioButton("Task "
						+ testFolderDir.substring(testFolderDir
								.indexOf("tests") + 5));
				bG.add(rad);
				radioButtons.add(rad);
				controlPanel.add(rad);
			}
			radioButtons.get(0).setSelected(true);
		}
	}

	private void drawResults(boolean[] results) {
		if (testsContainer != null) {
			guiFrame.remove(testsContainer);
		}
		testsContainer = new JPanel();

		for (boolean rez : results) {
			JButton btnRun;
			if (rez) {
				btnRun = new JButton("Passed");
				btnRun.setBackground(new Color(123456));
			} else {
				btnRun = new JButton("Failed");
				btnRun.setBackground(new Color(880000));
			}
			testsContainer.add(btnRun, BorderLayout.EAST);
		}
		guiFrame.add(testsContainer);
		guiFrame.validate();
		guiFrame.repaint();
	}

	private ArrayList<String> getTestFolders() {
		ArrayList<String> testFolders = new ArrayList<>();

		String command = "cmd /c dir " + TESTS_DIR;
		try {
			System.out.println(command);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.contains("tests") && line.contains("<DIR>")) {
					System.out.println(line.substring(line.indexOf("tests"),
							line.length()));
					testFolders.add(line.substring(line.indexOf("tests"),
							line.length()));
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		return testFolders;
	}

	protected void runTests() {
		String javaCode = edtJava.getText();
		boolean[] results;

		if (javaCode != null && !javaCode.equals("")) {
			String className = makeClassFile(javaCode);
			makeJavaFile(className);

			for (int i = 0; i < radioButtons.size(); i++) {
				JRadioButton rad = radioButtons.get(i);
				if (rad.isSelected()) {
					results = runProbOneTests(className, testFolders.get(i));
					drawResults(results);
				}
			}
			// if (zad1.isSelected()) {
			// results = runProbOneTests(className);
			// drawResults(results);
			// } else {
			// runProbTwoTests();
			// }
		}

	}

	private void makeJavaFile(String className) {
		try {
			String command = "cmd /c javac " + className;
			System.out.println(command);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
	}

	private void runProbTwoTests() {
		// TODO Auto-generated method stub

	}

	private boolean[] runProbOneTests(String className, String problemName) {
		String testDir = problemName;
		System.out.println("TESTDIR:" + testDir);
		ArrayList<String> tests = getTestsArray(testDir);
		boolean[] testRez = new boolean[tests.size()];
		int br = 0;
		for (String test : tests) {
			testRez[br] = runTest(test, testDir, className);
			br++;
		}
		return testRez;
	}

	private ArrayList<String> getTestsArray(String testsDir) {
		ArrayList<String> tests = new ArrayList<>();

		String command = "cmd /c dir " + TESTS_DIR + testsDir;
		try {
			System.out.println(command);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.contains("test.") && !line.contains("out")) {
					System.out.println(line.substring(line.indexOf("test"),
							line.indexOf("in.txt") + 6));
					tests.add(line.substring(line.indexOf("test"),
							line.indexOf("in.txt") + 6));
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		return tests;
	}

	private boolean runTest(String test, String testDir, String className) {
		String command = "cmd /c java "
				+ className.substring(0, className.indexOf(".java")) + " < "
				+ TESTS_DIR + testDir + "\\" + test + " > " + OUTPUT_TEXT_FILE;
		try {
			System.out.println(command);
			Process p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		File f = new File(OUTPUT_TEXT_FILE);
		while (!f.exists()) {
			System.out.print("Waiting for file-");
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			f = new File(OUTPUT_TEXT_FILE);
		}
		return compareTestResult(test, testDir);
	}

	private boolean compareTestResult(String test, String testDir) {
		BufferedReader realResultBuffer;
		BufferedReader userResultBuffer;
		try {
			realResultBuffer = new BufferedReader(new FileReader(TESTS_DIR + testDir + "\\"
					+ test.replace("in", "out")));
			userResultBuffer = new BufferedReader(new FileReader(
					OUTPUT_TEXT_FILE));
			String line = null;

			while ((line = realResultBuffer.readLine()) != null) {
				String userLine = userResultBuffer.readLine();
				if (userLine == null || !userLine.equals(line)) {
					realResultBuffer.close();
					userResultBuffer.close();
					deleteFile(OUTPUT_TEXT_FILE);
					return false;
				}
			}

			realResultBuffer.close();
			userResultBuffer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deleteFile(OUTPUT_TEXT_FILE);
		return true;
	}

	private void deleteFile(String filePath) {
		File file = new File(filePath);
		file.delete();
	}

	private String makeClassFile(String code) {
		String name = null;
		try {
			name = getClassName(code) + ".java";
			File logFile = new File(name);
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(code);
			// Close writer
			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

	private String getClassName(String code) {
		String name = "";
		int index = code.indexOf("class");
		name = code.substring(index + 6, code.indexOf(" ", index + 7));
		return name.trim();
	}

}
