package assignment;

public class TreapNode<K extends Comparable<K>, V>{
	
	private K key;
	private V value;
	private int priority;
	private TreapNode<K, V> left;
	private TreapNode<K, V> right;
	
	public TreapNode(){
		
	}

	public TreapNode(K key, V value, int priority){
		this.key = key;
		this.value = value;
		this.priority = priority;
	}
	
	public K getKey() {
		return key;
	}
	
	public V getValue() {
		return value;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public TreapNode<K, V> getLeft() {
		return left;
	}
	
	public TreapNode<K, V> getRight(){
		return right;
	}
	
	public void setKey(K key) {
		this.key = key;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void setLeft(TreapNode<K,V> node) {
		left = node;
	}
	
	public void setRight(TreapNode<K,V> node) {
		right = node;
	}
	
}
