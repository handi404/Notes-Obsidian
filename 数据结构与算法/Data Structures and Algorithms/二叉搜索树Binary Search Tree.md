	// 二叉搜索树 = 一种树形数据结构，其中每个节点都大于它左边的子节点、
    	// 但小于右边的节点。
    	
    	// 优点：当节点按此顺序排列时，很容易找到节点						
    	
    	// 时间复杂性：最佳情况为 O(log n)
    	// 最差情况为 O(n)
    	
    	// 空间复杂度 O(n)

---

```java
public class Main {

    public static void main(String[] args) {

    	BinarySearchTree tree = new BinarySearchTree();
    	
    	tree.insert(new Node(5));
    	tree.insert(new Node(1));
    	tree.insert(new Node(9));
    	tree.insert(new Node(2));
    	tree.insert(new Node(7));
    	tree.insert(new Node(3));
    	tree.insert(new Node(6));
    	tree.insert(new Node(4));
    	tree.insert(new Node(8));
    	
    	tree.display();
    }
}
```

```java
public class BinarySearchTree {

	Node root;
	
	public void insert(Node node) {
		
		root = insertHelper(root, node);
	}
	private Node insertHelper(Node root, Node node) {
		
		int data = node.data;
		
		if(root == null) {
			root = node;
			return root;
		}
		else if(data < root.data) {
			root.left = insertHelper(root.left, node);
		}
		else {
			root.right = insertHelper(root.right, node);
		}
		
		return root;
	}
	public void display() {
		displayHelper(root);
	}
	private void displayHelper(Node root) {
		
		if(root != null) {
			displayHelper(root.left);
			System.out.println(root.data);
			displayHelper(root.right);
		}
	}
	public boolean search(int data) {
		return searchHelper(root, data);
	}
	private boolean searchHelper(Node root, int data) {
		
		if(root == null) {
			return false;
		}
		else if(root.data == data) {
			return true;
		}
		else if(root.data > data) {
			return searchHelper(root.left, data);
		}
		else {
			return searchHelper(root.right, data);
		}
	}
	public void remove(int data) {
		
		if(search(data)) {
			removeHelper(root, data);
		}
		else {
			System.out.println(data + " could not be found");
		}
	}
	private Node removeHelper(Node root, int data) {
		
		if(root == null) {
			return root;
		}
		else if(data < root.data) {
			root.left = removeHelper(root.left, data);
		}
		else if(data > root.data) {
			root.right = removeHelper(root.right, data);
		}
		else { // 找到节点
			if(root.left == null && root.right == null) {
				root = null;
			}
			else if(root.right != null) { //找到替代该节点的后继节点
				root.data = successor(root);
				root.right = removeHelper(root.right, root.data);
			}
			else { //查找替代此节点的前置节点
				root.data = predecessor(root);
				root.left = removeHelper(root.left, root.data);
			}
		}
		return root;
	}
	private int successor(Node root) { //在此根节点的右子节点下方找到最小值
		root = root.right;
		while(root.left != null){
			root = root.left;
		}
		return root.data;
	}
	private int predecessor(Node root) {//在此根节点的左子节点下方找到最大值
		root = root.left;
		while(root.right != null){
			root = root.right;
		}
		return root.data;
	}
}
```

```java
public class Node {

	int data;
	Node left;
	Node right;
	
	public Node(int data) {
		this.data = data;
	}
}
```

