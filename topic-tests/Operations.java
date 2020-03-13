import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Operations {

	public static void main(String[] args) {
		JFrame frame = new JFrame("'fixes madness");
		JPanel main = new JPanel(new BorderLayout());
		JPanel subMain = new JPanel(new BorderLayout());
		JTextField in = new JTextField();
		in.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		JPanel footer = new JPanel(new GridLayout(1, 2));
		footer.add(new JLabel("The solution is: "));
		JTextField result = new JTextField("");
		result.setEditable(false);
		footer.add(result);
		footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		JPanel buttons = new JPanel(new GridLayout(1, 6));
		JButton preinf = new JButton("Prefix to Infix");
		preinf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.setText(prefix_infix(in.getText()));
			}
		});
		buttons.add(preinf);
		JButton prepost = new JButton("Prefix to Postfix");
		prepost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.setText(prefix_postfix(in.getText()));
			}
		});
		buttons.add(prepost);

		JButton infpre = new JButton("Infix to Prefix");
		infpre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.setText(infix_prefix(in.getText()));
			}
		});
		buttons.add(infpre);

		JButton inpost = new JButton("Infix to Postfix");
		inpost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.setText(infix_postfix(in.getText()));
			}
		});
		buttons.add(inpost);

		JButton postpre = new JButton("Postfix to Prefix");
		postpre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.setText(postfix_prefix(in.getText()));
			}
		});
		buttons.add(postpre);

		JButton postin = new JButton("Postfix to Infix");
		postin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result.setText(postfix_infix(in.getText()));
			}
		});
		buttons.add(postin);

		subMain.add(in, BorderLayout.NORTH);
		subMain.add(buttons, BorderLayout.CENTER);
		subMain.add(footer, BorderLayout.SOUTH);

		JPanel close = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		close.add(closeButton);
		subMain.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		main.add(subMain, BorderLayout.CENTER);
		main.add(close, BorderLayout.SOUTH);

		frame.add(main);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(850, 200));
		frame.setVisible(true);
	}

	private static String prefix_infix(String exp) {
		Stack<String> stack = new Stack<>();
		for (int i = exp.length() - 1; i >= 0; i--) {
			char c = exp.charAt(i);

			if (isOperator(c)) {
				String a = stack.pop();
				String b = stack.pop();
				String temp = "(" + a + c + b + ")";
				stack.push(temp);
			} else {
				stack.push(c + "");
			}
		}

		String result = stack.pop();

		return result;
	}

	private static String postfix_infix(String exp) {
		Stack<String> stack = new Stack<>();
		for (int i = 0; i < exp.length(); i++) {
			char c = exp.charAt(i);

			if (c == '*' || c == '/' || c == '^' || c == '+' || c == '-') {
				String a = stack.pop();
				String b = stack.pop();
				String temp = "(" + b + c + a + ")";
				stack.push(temp);
			} else {
				stack.push(c + "");
			}
		}

		String result = stack.pop();
		return result;
	}

	private static String prefix_postfix(String exp) {
		Stack<String> stack = new Stack<>();
		for (int i = exp.length() - 1; i >= 0; i--) {
			if (isOperator(exp.charAt(i))) {
				String a = stack.pop();
				String b = stack.pop();
				String temp = a + b + exp.charAt(i);
				stack.push(temp);
			} else {
				stack.push(exp.charAt(i) + "");
			}
		}

		String result = stack.pop();
		return result;
	}

	private static String postfix_prefix(String exp) {
		Stack<String> stack = new Stack<>();
		for (int i = 0; i < exp.length(); i++) {
			if (isOperator(exp.charAt(i))) {
				String a = stack.pop();
				String b = stack.pop();
				String temp = exp.charAt(i) + b + a;
				stack.push(temp);
			} else {
				stack.push(exp.charAt(i) + "");
			}
		}
		String result = stack.pop();
		return result;
	}

	private static String infix_prefix(String exp) {
		StringBuilder result = new StringBuilder();
		StringBuilder input = new StringBuilder(exp);
		input.reverse();
		Stack<Character> stack = new Stack<Character>();

		char[] charsExp = new String(input).toCharArray();
		for (int i = 0; i < charsExp.length; i++) {

			if (charsExp[i] == '(') {
				charsExp[i] = ')';
				i++;
			} else if (charsExp[i] == ')') {
				charsExp[i] = '(';
				i++;
			}
		}
		for (char c : charsExp) {
			if (precedence(c) > 0) {
				while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
					result.append(stack.pop());
				}
				stack.push(c);
			} else if (c == ')') {
				char x = stack.pop();
				while (x != '(') {
					result.append(x);
					x = stack.pop();
				}
			} else if (c == '(') {
				stack.push(c);
			} else {
				result.append(c);
			}
		}

		for (int i = 0; i <= stack.size(); i++) {
			result.append(stack.pop());
		}
		StringBuilder reverse = result.reverse();
		return reverse.toString();
	}

	private static String infix_postfix(String exp) {
		String result = "";
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < exp.length(); i++) {
			if (precedence(exp.charAt(i)) > 0) {
				while (stack.isEmpty() == false && precedence(stack.peek()) >= precedence(exp.charAt(i))) {
					result += stack.pop();
				}
				stack.push(exp.charAt(i));
			} else if (exp.charAt(i) == ')') {
				char x = stack.pop();
				while (x != '(') {
					result += x;
					x = stack.pop();
				}
			} else if (exp.charAt(i) == '(') {
				stack.push(exp.charAt(i));
			} else {
				result += exp.charAt(i);
			}
		}
		for (int i = 0; i <= stack.size(); i++) {
			result += stack.pop();
		}
		return result;
	}

	private static int precedence(char c) {
		if (c == '+' || c == '-') {
			return 1;
		} else if (c == '*' || c == '/') {
			return 2;
		} else if (c == '^') {
			return 3;
		} else {
			return -1;
		}
	}

	private static boolean isOperator(char x) {
		if (x == '+' || x == '-' || x == '/' || x == '*' || x == '^') {
			return true;
		} else {
			return false;
		}
	}
}