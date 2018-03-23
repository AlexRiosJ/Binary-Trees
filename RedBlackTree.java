package trees;

public class RedBlackTree<T extends Comparable<? super T>> {

	public final Node NIL = new Node(null, false);
	public Node root = NIL;
	private int size = 0;
	
	public class Node {
		public T key;
		public boolean isRed = true;
		public Node left = NIL, right = NIL, parent = NIL;
		
		public Node(T key) {
			this.key = key;
			this.isRed = true;
		}
		
		// Constructor para el nodo NIL
		public Node(T key, boolean nilColor) {
			this.key = key;
			this.isRed = nilColor;
			this.parent = NIL;
			this.right = NIL;
			this.left = NIL;
		}
		
		public void setBlack() {
			this.isRed = false;
		}
		
		public void setRed() {
			this.isRed = true;
		}
		
		public boolean isRed() {
			return this.isRed;
		}
		
		public boolean isBlack() {
			return !this.isRed;
		}
		
		public String toString() {
			if(this.isBlack()) return this.key.toString() + ", B";
			return this.key.toString() + ", R";
		}
	}
	
	public Node getRoot() {
		return this.root;
	}
	
	public Node search(T key, Node current) {
		if(current.equals(NIL)) return NIL;
		int result = key.compareTo(current.key);
		if(result == 0) return current;
		else if(result < 0) return search(key, current.left);
		else return search(key, current.right);
	}
	
	public boolean contains(T key) {
		return !search(key, this.root).equals(NIL);
	}
	
	private void leftRotate(Node x) {
		Node y = x.right;
		x.right = y.left;
		if(!y.left.equals(NIL))
			y.left.parent = x;
		y.parent = x.parent;
		if(x.parent.equals(NIL))
			this.root = y;
		else if(x.equals(x.parent.left))
			x.parent.left = y;
		else
			x.parent.right = y;
		y.left = x;
		x.parent = y;
	}
	
	private void rightRotate(Node x) {
		Node y = x.left;
		x.left = y.right;
		if(!y.right.equals(NIL))
			y.right.parent = x;
		y.parent = x.parent;
		if(x.parent.equals(NIL))
			this.root = y;
		else if(x.equals(x.parent.right))
			x.parent.right = y;
		else
			x.parent.left = y;
		y.right = x;
		x.parent = y;
	}
	
	public boolean add(T key) {
		Node z = new Node(key);
		Node y = NIL;
		Node x = this.root;
		boolean added = false;
		while(!x.equals(NIL)) {
			y = x;
			if(z.key.compareTo(x.key) == 0)
				return added;
			else if(z.key.compareTo(x.key) < 0)
				x = x.left;
			else 
				x = x.right;
		}
		z.parent = y;
		if(y.equals(NIL)) {
			this.root = z;
			added = true;
		}
		else if(z.key.compareTo(y.key) < 0) {
			y.left = z;
			added = true;
		}
		else {
			y.right = z;
			added = true;
		}
		z.left = NIL;
		z.right = NIL;
		z.setRed();
		addFixup(z);
		this.size ++;
		return added;
	}
	
	private void addFixup(Node z) {
		while(z.parent.isRed()) {
			if(z.parent.equals(z.parent.parent.left)) {
				Node y = z.parent.parent.right;
				if(y.isRed()) {
					z.parent.setBlack();
					y.setBlack();
					z.parent.parent.setRed();
					z = z.parent.parent;
				}
				else {
					if(z.equals(z.parent.right)) {
						z = z.parent;
						leftRotate(z);
					}
					z.parent.setBlack();
					z.parent.parent.setRed();
					rightRotate(z.parent.parent);
				}
			}
			else {
				Node y = z.parent.parent.left;
				if(y.isRed()) {
					z.parent.setBlack();
					y.setBlack();
					z.parent.parent.setRed();
					z = z.parent.parent;
				}
				else {
					if(z.equals(z.parent.left)) {
						z = z.parent;
						rightRotate(z);
					}
					z.parent.setBlack();
					z.parent.parent.setRed();
					leftRotate(z.parent.parent);
				}
			}
		}
		this.root.setBlack();
	}
	
	private void transplant(Node u, Node v) {
		if(u.parent.equals(NIL))
			this.root = v;
		else if(u.equals(u.parent.left))
			u.parent.left = v;
		else
			u.parent.right = v;
		v.parent = u.parent;
	}
	
	public boolean delete(T key) {
		if(!contains(key))
			return false;
		Node z = search(key, this.root);
		Node y = z;
		Node x;
		boolean yOriginalColor = y.isRed;
		if(z.left.equals(NIL)) {
			x = z.right;
			transplant(z, z.right);
		}
		else if(z.right.equals(NIL)) {
			x = z.left;
			transplant(z, z.left);
		}
		else {
			y = treeMaximum(z.left);
			yOriginalColor = y.isRed;
			x = y.left;
			if(y.parent.equals(z))
				x.parent = y;
			else {
				transplant(y, y.left);
				y.left = z.left;
				y.left.parent = y;
			}
			transplant(z, y);
			y.right = z.right;
			y.right.parent = y;
			y.isRed = z.isRed();
		}
		if(yOriginalColor == false) // yOriginalColor == BLACK
			deleteFixup(x);
		this.size --;
		return true;
	}
	
	private void deleteFixup(Node x) {
		while(!x.equals(this.root) && x.isBlack()) {
			if(x.equals(x.parent.left)) {
				Node w = x.parent.right;
				if(w.isRed()) {
					w.setBlack();	
					x.parent.setRed();
					leftRotate(x.parent);
					w = x.parent.right;
				}
				if(w.left.isBlack() && w.right.isBlack()) {
					w.setRed();
					x = x.parent;
				}
				else {
					if(w.right.isBlack()) {
						w.left.setBlack();
						w.setRed();
						rightRotate(w);
						w = x.parent.right;
					}
					w.isRed = x.parent.isRed();
					x.parent.setBlack();
					w.right.setBlack();
					leftRotate(x.parent);
					x = this.root;
				}
			}
			else {
				Node w = x.parent.left;
				if(w.isRed()) {
					w.setBlack();
					x.parent.setRed();
					rightRotate(x.parent);
					w = x.parent.left;
				}
				if(w.right.isBlack() && w.left.isBlack()) {
					w.setRed();
					x = x.parent;
				}
				else {
					if(w.left.isBlack()) {
						w.right.setBlack();
						w.setRed();
						leftRotate(w);
						w = x.parent.left;
					}
					w.isRed = x.parent.isRed();
					x.parent.setBlack();
					w.left.setBlack();
					rightRotate(x.parent);
					x = this.root;
				}
			}
		}
		x.setBlack();
	}
	
	private Node treeMaximum(Node x) {
		while(!x.right.equals(NIL))
			x = x.right;
		return x;
	}
	
	private void print(Node node, int spaces) {
		if (!node.equals(NIL)) {
			String str = "";
			for(int i = 0; i < spaces; i ++) str += " ";
			System.out.println(str + node);
			print(node.left, spaces + 2);
			print(node.right, spaces + 2);
		}
	}
	
	public void print() {
		print(this.root, 0);
		System.out.println("-----------");
	}
	
	public int size() {
		return this.size;
	}

}
