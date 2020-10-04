package ink.codflow.sync.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ErrorDialog {

	public static void showQuickErrorDialog(Throwable e) {
		showQuickErrorDialog(null, e);
	}

	public static void showQuickErrorDialog(JFrame parent, Throwable e) {

		final JTextArea textArea = new JTextArea();

		textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));

		textArea.setEditable(false);

		StringWriter writer = new StringWriter();

		e.printStackTrace(new PrintWriter(writer));

		textArea.setText(writer.toString());

		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(350, 150));

		JOptionPane.showMessageDialog(parent, scrollPane, "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);

	}

	public static void showQuickErrorDialog(String message) {
		showQuickErrorDialog(null, message);
	}

	public static void showQuickErrorDialog(JFrame parent, String message) {

		final JTextArea textArea = new JTextArea();

		textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 10));

		textArea.setEditable(false);

		textArea.setText(message);

		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setPreferredSize(new Dimension(350, 150));

		JOptionPane.showMessageDialog(parent, scrollPane, "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);

	}

}
