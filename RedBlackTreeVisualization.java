package trees;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RedBlackTreeVisualization extends JFrame {
	private static final long serialVersionUID = 1L;
	private RedBlackTree<Integer> tree;
	private JTextField jtfKey = new JTextField(5);
	private PaintTree paintTree = new PaintTree();
	private JButton jbtInsert = new JButton("Insertar");
	private JButton jbtRemove = new JButton("Remover");
	private JButton jbtSearch = new JButton("Buscar");
	private JButton jbtReset = new JButton("Reset");

	public RedBlackTreeVisualization(RedBlackTree<Integer> tree) {
		this.tree = tree;
		setUI();
	}

	private void setUI() {
		this.setLayout(new BorderLayout());
		add(paintTree, BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.add(new JLabel("Ingresa un valor entero: "));
		panel.add(jtfKey);
		panel.add(jbtInsert);
		panel.add(jbtRemove);
		panel.add(jbtSearch);
		panel.add(jbtReset);
		add(panel, BorderLayout.SOUTH);

		jbtInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					int key = Integer.parseInt(jtfKey.getText());
					if (tree.contains(key)) { // key is in the tree already
						JOptionPane.showMessageDialog(null, key + " ya está en el árbol.");
					} else {
						tree.add(key); // Insert a new key
						paintTree.founded = tree.NIL;
						paintTree.repaint(); // Redisplay the tree
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Ingresa un valor entero.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		jbtRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int key = Integer.parseInt(jtfKey.getText());
					if (!tree.contains(key)) { // key is not in the tree
						JOptionPane.showMessageDialog(null, key + " no está en el árbol.");
					} else {
						tree.delete(key); // Delete a key
						paintTree.founded = tree.NIL;
						paintTree.repaint(); // Redisplay the tree
					}
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Ingresa un valor entero.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		jbtReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tree.root.equals(tree.NIL))
					JOptionPane.showMessageDialog(null, "El árbol ya está vacío.");
				while(!tree.root.equals(tree.NIL)) { // Delete root until it is NIL
					tree.delete(tree.root.key);
					paintTree.repaint();
				}
				paintTree.founded = tree.NIL;
			}
		});
		
		jbtSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int key = Integer.parseInt(jtfKey.getText());
					if(!tree.contains(key))
						JOptionPane.showMessageDialog(null, key + " no está en el árbol.");
					else{
						paintTree.founded = tree.search(key, tree.root);
						paintTree.contains = true;
						paintTree.repaint();
					}
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Ingresa un valor entero.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		setTitle("Árbol Rojo Negro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		setVisible(true);
	}

	class PaintTree extends JPanel {
		public boolean contains = false;
		public RedBlackTree<Integer>.Node founded;
		private static final long serialVersionUID = 1L;
		private int radius = 20; // Tree node radius
		private int vGap = 50; // Gap between two levels in a tree

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (!tree.getRoot().equals(tree.NIL)) {
				// Display tree recursively
				displayTree(g, tree.getRoot(), getWidth() / 2, 30, getWidth() / 4);
			}
		}

		private void displayTree(Graphics g, RedBlackTree<Integer>.Node root, int x, int y, int hGap) {
			Color red = new Color(214, 91, 91);
			Color lightRed = new Color(255, 170, 170);
			Color black = new Color(0, 0, 0);
			Color gray = new Color(170, 170, 170);
			Color green = new Color(0, 220, 0);
			if(contains && root.equals(founded)) {
				g.setColor(black);
				g.fillOval(x - radius - 4, y - radius - 4, 2 * radius + 8, 2 * radius + 8);
				g.setColor(green);
				g.fillOval(x - radius - 3, y - radius - 3, 2 * radius + 6, 2 * radius + 6);
			}
			if(root.isBlack()) g.setColor(black);
			else g.setColor(red);
			g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
			if(root.isBlack()) g.setColor(gray);
			else g.setColor(lightRed);
			g.fillOval(x - radius + 2, y - radius + 2, 2 * radius - 4, 2 * radius - 4);
			if(root.isBlack()) g.setColor(black);
			else g.setColor(red);
			g.drawString(root.key + "", x - 6, y + 4);
			g.setColor(black);
			g.drawString("Tamaño: " + tree.size(), 20, 20);

			if (!root.left.equals(tree.NIL)) {
				connectLeftChild(g, x - hGap, y + vGap, x, y);
				displayTree(g, root.left, x - hGap, y + vGap, hGap / 2);
			}

			if (!root.right.equals(tree.NIL)) {
				connectRightChild(g, x + hGap, y + vGap, x, y);
				displayTree(g, root.right, x + hGap, y + vGap, hGap / 2);
			}
		}

		private void connectLeftChild(Graphics g, int x1, int y1, int x2, int y2) {
			double d = Math.sqrt(vGap * vGap + (x2 - x1) * (x2 - x1));
			int x11 = (int) (x1 + radius * (x2 - x1) / d);
			int y11 = (int) (y1 - radius * vGap / d);
			int x21 = (int) (x2 - radius * (x2 - x1) / d);
			int y21 = (int) (y2 + radius * vGap / d);
			g.drawLine(x11, y11, x21, y21);
		}

		private void connectRightChild(Graphics g, int x1, int y1, int x2, int y2) {
			double d = Math.sqrt(vGap * vGap + (x2 - x1) * (x2 - x1));
			int x11 = (int) (x1 - radius * (x1 - x2) / d);
			int y11 = (int) (y1 - radius * vGap / d);
			int x21 = (int) (x2 + radius * (x1 - x2) / d);
			int y21 = (int) (y2 + radius * vGap / d);
			g.drawLine(x11, y11, x21, y21);
		}
	}
}