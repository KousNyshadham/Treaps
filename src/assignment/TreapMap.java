package assignment;

import java.util.Iterator;
import java.util.ArrayList;

public class TreapMap<K extends Comparable<K>, V>  implements Treap<K, V>{
	
	TreapNode<K, V> root;
	TreapNode<K, V> sameK;
	V val;
	ArrayList<K> keys = new ArrayList<K>();
	String toString = "";
	
	public TreapMap(){
			
	}
	
	public TreapMap(TreapNode<K, V> root){
		this.root = root;
	}
	
	//Retrieves the value associated with a key in this dictionary.
    //If the key is null or the key is not present in this
    //dictionary, this method returns null.
	public V lookup(K key) {
		if(key == null) {
			return null;
		}
		return lookup(root, key);
	}
	
	private V lookup(TreapNode<K, V> root, K key) {
		if(root == null || root.getKey().compareTo(key)==0) {
			//key not contained within tree
			if(root == null) {
				return null;
			}
			return root.getValue();
		}
		else {
			//key is less than current node's key so go to left tree
			if(root.getKey().compareTo(key) > 0) {
				return lookup(root.getLeft(), key);
			}
			//key is greater than current node's key so go to right tree
			return lookup(root.getRight(), key);
		}
	}
	
	// Adds a key-value pair to this dictionary.  Any old value
    // associated with the key is lost.  If the key or the value is
    // null, the pair is not added to the dictionary.
	public void insert(K key, V value) {
		//if key or value is null return
		if(key == null || value == null) {
			return;
		}
		int priority = (int)(Math.random() * Treap.MAX_PRIORITY)+1;
		root = insert(root, key, value, priority);	
	}
	
	private TreapNode<K,V> insert(TreapNode<K, V> root, K key, V value, int priority) {
		if(root == null || root.getKey().compareTo(key) == 0){
			//if root is null, insert here
			if(root == null) {
				return new TreapNode<K, V>(key, value, priority);
			}
			//else, key of current node is inputted key and therefore only change value
			sameK = new TreapNode<K, V>(root.getKey(), root.getValue(), root.getPriority());
			root.setValue(value);
			if(value == null) {
				root.setPriority(priority);
			}
			return root;
		}
		else{
			//key is less than current node's key so go to left tree
			if(root.getKey().compareTo(key) > 0) {
				root.setLeft(insert(root.getLeft(), key, value, priority));
				//backtracking
				//left child priority greater than root so rotate right
				if(root.getLeft().getPriority() > root.getPriority()) {
					root = rotateRight(root);
				}
			}
			//key is greater than current node's key so go to right tree
			else{
				root.setRight(insert(root.getRight(), key, value, priority));
				//backtracking
				//right child priority greater than root so rotate left
				if(root.getRight().getPriority() > root.getPriority()) {
					root = rotateLeft(root);
				}
			}
			return root;
		}
	}	

	//rotate roots left
	private TreapNode<K, V> rotateLeft(TreapNode<K,V> parent){
		TreapNode<K, V> child = parent.getRight();
		TreapNode<K, V> t2 = child.getLeft();
		child.setLeft(parent);
		parent.setRight(t2);
		return child;
	}
	
	//rotate roots right
	private TreapNode<K, V> rotateRight(TreapNode<K,V> parent){
		TreapNode<K, V> child = parent.getLeft();
		TreapNode<K, V> t2 = child.getRight();
		child.setRight(parent);
		parent.setLeft(t2);
		return child;
	}

	//Removes a key from this dictionary.  If the key is not present
    // in this dictionary, this method does nothing.  Returns the
    // value associated with the removed key, or null if the key
    // is not present.
	public V remove(K key) {
		if(key == null) {
			return null;
		}
		root = remove(root, key);
		return val;
	}
	
	private TreapNode<K, V> remove(TreapNode<K, V> root, K key) {
		//not contained within Treap so don't modify
		if(root == null) {
			val = null;
			return root;
		}
		//key is less than root's key so traverse to left tree
		if(root.getKey().compareTo(key) > 0) {
			root.setLeft(remove(root.getLeft(), key));
		}
		//key is greater than root's key so traverse to right tree
		else if(root.getKey().compareTo(key) < 0) {
			root.setRight(remove(root.getRight(), key));
		}
		//node being looked for has no left children so set it to be equal to the right subtree and retrieve the value
		else if(root.getLeft() == null){
			val = root.getValue();
			root = root.getRight();
		}
		//node being locked for has no right children so set it be equal to the left subtree and retrieve the value
		else if(root.getRight() == null) {
			val = root.getValue();
			root = root.getLeft();
		}
		//node being looked for has two children and if left child has higher priority then the right one, ...
		//... rotateRight and then call the remove function on the subtree with the key as the root
		else if(root.getLeft().getPriority() > root.getRight().getPriority()) {
			root = rotateRight(root);
			root.setRight(remove(root.getRight(), key));
		}
		//wlog ^
		else {
			root = rotateLeft(root);
			root.setLeft(remove(root.getLeft(), key));
		}
		return root;
	}

	// Splits this treap into two treaps.  The left treap should contain
    // values less than the key, while the right treap should contain
    // values greater than or equal to the key.
	public Treap<K, V>[] split(K key) {
		//set sameK to be null in case 2 insertions were made with the same key before this method was called
		sameK = null;
		//insert node with given key and maximum priority so left treap will be less than k and right treap will be greater than k
		this.root = this.insert(root, key, null, Treap.MAX_PRIORITY);
		Treap<K, V> t1 = new TreapMap<K, V>(root.getLeft());
		Treap<K, V> t2 = new TreapMap<K, V>(root.getRight());
		//if sameK is not null, treap contained node with key k and add it to right treap
		if(sameK !=null) {
			((TreapMap<K,V>)t2).root = ((TreapMap<K,V>)t2).insert(((TreapMap<K,V>)t2).root, sameK.getKey(), sameK.getValue(), sameK.getPriority());
		}
		return new Treap[]{t1, t2};
	}
	
	// Joins this treap with another treap.  If the other treap is not
    // an instance of the implementing class, both treaps are unmodified.
    // At the end of the join, this treap will contain the result.
    // This method may destructively modify both treaps.
	public void join(Treap<K, V> t) {
		if(!(t instanceof TreapMap)) {
			return;
		}
		TreapMap<K,V> t1 = ((TreapMap<K, V>)t);
		//if root is null, then root becomes other root 
		if(this.root == null) {
			this.root = t1.root;
			return;
		}
		//if other root is null, root stays same
		if(t1.root == null) {
			return;
		}
		//dummy node of new treap map
		TreapNode<K, V> dummy = new TreapNode<K, V>();
		//if every element in this treap is less than every element in other treap
		if(this.root.getKey().compareTo(t1.root.getKey()) < 0) {
			TreapNode<K, V> head = this.root;
			//head becomes largest element in this treap
			while(head.getRight() != null) {
				head = head.getRight();
			}
			//dummy set to head and left child of dummy set to smaller elements and right child set to greater elements
			dummy.setKey(head.getKey());
			dummy.setLeft(this.root);
			dummy.setRight(t1.root);
			//root of map set to dummy and remove it to have combined treap
			TreapMap<K, V> map = new TreapMap<K, V>(dummy);
			map.remove(dummy.getKey());
			this.root = map.root;
		}
		//wlog
		else {
			TreapNode<K, V> head = t1.root;
			while(head.getRight() != null) {
				head = head.getRight();
			}
			dummy.setKey(head.getKey());
			dummy.setLeft(t1.root);
			dummy.setRight(this.root);
			TreapMap<K, V> map = new TreapMap<K, V>(dummy);
			map.remove(dummy.getKey());
			this.root = map.root;
		}
	}
	
	@Override
	public void meld(Treap<K, V> t) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void difference(Treap<K, V> t) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	// @return a fresh iterator that points to the first element in
	// this Treap and iterates in sorted order.
	public Iterator<K> iterator() {
		keys.clear();
		inOrderTraversal(root);
		return keys.iterator();
	}
	
	private void inOrderTraversal(TreapNode<K, V> root) {
		if(root == null) {
			return;
		}
		inOrderTraversal(root.getLeft());
		keys.add(root.getKey());
		inOrderTraversal(root.getRight());
	}
	
	// Returns the balance factor of the treap.  The balance factor
    // is the height of the treap divided by the minimum possible
    // height of a treap of this size.  A perfectly balanced treap
    // has a balance factor of 1.0.  If this treap does not support
    // balance statistics, throw an exception.
	public double balanceFactor() throws UnsupportedOperationException {
		int height = getHeight(root);
		int minimumHeight = (int) (Math.log10(size(root))/Math.log10(2));
		if(minimumHeight == 0) {
			return 0;
		}
		return height/minimumHeight;
	}
	
	private int getHeight(TreapNode<K, V> root) {
		if(root == null) {
			return -1;
		}
		return Math.max(getHeight(root.getLeft())+1, getHeight(root.getRight())+1);
	}
	
	private int size(TreapNode<K, V> root) {
		if(root == null) {
			return 0;
		}
		int leftSize = size(root.getLeft());
		int rightSize = size(root.getRight());
		return leftSize + rightSize + 1;
	}
	
    // Build a human-readable version of the treap.
    // Each node in the treap will be represented as
    //
    //     [priority] <key, value>\n
    //
    // Subtreaps are indented one tab over from their parent for
    // printing.  This method prints out the string representations
    // of key and value using the object's toString(). Treaps should
    // be printed in pre-order traversal fashion.
	public String toString() {
		toString = "";
		preOrderTraversal(root, "");
		return toString;
	}
	
	private void preOrderTraversal(TreapNode<K, V> root, String space) {
		if(root == null) {
			return;
		}
		toString+= space + "[" + root.getPriority() + "]" + " <" + root.getKey() + ", " + root.getValue() + ">" + "\n";
		preOrderTraversal(root.getLeft(), space + "\t");
		preOrderTraversal(root.getRight(), space + "\t");
	}
	
}
