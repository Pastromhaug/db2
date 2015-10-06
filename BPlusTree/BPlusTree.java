//import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.ArrayList;

/**
 * BPlusTree Class Assumptions: 1. No duplicate keys inserted 2. Order D:
 * D<=number of keys in a node <=2*D 3. All keys are non-negative
 * TODO: Rename to BPlusTree
 */
public class BPlusTree<K extends Comparable<K>, T> {

 public Node<K,T> root;
 public static final int D = 2;

 /**
  * TODO Search the value for a specific key
  * 
  * @param key
  * @return value
  */
 //JARED HERE
 public T search(K key) {
  LeafNode<K,T> leaf;
  leaf = getLeaf(key);
  return (T) leaf.getValue(key);
 }

 /**
  * TODO Insert a key/value pair into the BPlusTree
  * 
  * @param key
  * @param value
  */
 public void insert(K key, T value) {
   if (root == null) {
     LeafNode<K,T> leaf = new LeafNode<K,T>(key, value);
     ArrayList<Node<K,T>> leafHolder = new ArrayList<Node<K,T>>();
     ArrayList<K> keyHolder = new ArrayList<K>();
     leafHolder.add(leaf);
     keyHolder.add(key);
     root = new IndexNode<K,T>(keyHolder, leafHolder);
     return;
   }
   LeafNode<K,T> searchNode = getLeaf(key);
   searchNode.insertSorted(key, value);
 }

 /**
  * TODO Split a leaf node and return the new right node and the splitting
  * key as an Entry<slitingKey, RightNode>
  * 
  * @param leaf, any other relevant data
  * @return the key/node pair as an Entry
  */
 public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf/*, ...*/) {

  return null;
 }

 /**
  * TODO split an indexNode and return the new right node and the splitting
  * key as an Entry<slitingKey, RightNode>
  * 
  * @param index, any other relevant data
  * @return new key/node pair as an Entry
  */
 public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index/*, ..*/) {

  return null;
 }

 /**
  * TODO Delete a key/value pair from this B+Tree
  * 
  * @param key
  */
 public void delete(K key) {

 }

 /**
  * TODO Handle LeafNode Underflow (merge or redistribution)
  * 
  * @param left
  *            : the smaller node
  * @param right
  *            : the bigger node
  * @param parent
  *            : their parent index node
  * @return the splitkey position in parent if merged so that parent can
  *         delete the splitkey later on. -1 otherwise
  */
 public int handleLeafNodeUnderflow(LeafNode<K,T> left, LeafNode<K,T> right,
   IndexNode<K,T> parent) {
  return -1;

 }

 /**
  * TODO Handle IndexNode Underflow (merge or redistribution)
  * 
  * @param left
  *            : the smaller node
  * @param right
  *            : the bigger node
  * @param parent
  *            : their parent index node
  * @return the splitkey position in parent if merged so that parent can
  *         delete the splitkey later on. -1 otherwise
  */
 public int handleIndexNodeUnderflow(IndexNode<K,T> leftIndex,
   IndexNode<K,T> rightIndex, IndexNode<K,T> parent) {
  return -1;
 }
 
 //Pre: Receives a key
 //Post: Returns the leafnode where that key should be stored.
 public LeafNode<K,T> getLeaf(K key){
	 Node<K,T> searchNode = root;
	 IndexNode<K,T> index;
	 ArrayList<Node<K,T>> children;
	 ArrayList<K> nodeKeys;
	 //traverse through the tree until it finds a leafNode
	 while(!searchNode.isLeaf()) {
		 index = (IndexNode<K,T>) searchNode;
		 nodeKeys = (ArrayList<K>) searchNode.getKeys();
		 children = index.getChildren();
		 for (int i = 0; i < nodeKeys.size() + 1; i++) {
			 if (nodeKeys.get(i).compareTo(key) > 0) {
				 searchNode = children.get(i);
				 break;
			 }
		}
	}
	return (LeafNode<K,T>) searchNode;
 }

}
