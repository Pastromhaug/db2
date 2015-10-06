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
     LeafNode<K,T> rightLeaf = new LeafNode<K,T>(key, value);
     LeafNode<K,T> leftLeaf = new LeafNode<K,T> (new ArrayList<K>(), new ArrayList<T>());
     ArrayList<Node<K,T>> leafHolder = new ArrayList<Node<K,T>>();
     ArrayList<K> keyHolder = new ArrayList<K>();
     leafHolder.add(leftLeaf);
     leafHolder.add(rightLeaf);
     keyHolder.add(key);
     root = new IndexNode<K,T>(keyHolder, leafHolder);
     return;
   }
   insertbody(key, value, root, null);
 }

 public void insertbody(K key, T value, Node n, Entry<K, Node<K,T>> newchildentry) {
 	if (n.isLeafNode == false) {
 		int i;
 		for (i = 0; i < n.keys.size(); i++){
 			if (nodeKeys.get(i).compareTo(key) > 0) break;
 		}
 		insertbody(key, value, n.children(i));
 		if (newchildentry == null) {
 			return;
 		}
 		else{
 			if (n.isOverFlowed == false){
 				n.insertSorted(newchildentry, i);
 			}
 			else {
 				newchildentry = splitIndexNode(n);
 				if (n == root){
 					IndexNode<K,T> newroot = new IndexNode(
 							newchildentry.keys, newchildentry.children);
 					Entry<K,Node<K,T>> leftentry =
			    		new AbstractMap.SimpleEntry<K, Node<K,T>>(n.keys, n.children);
			    	insertSorted(leftentry, 0);
 					root = newroot;
 				}
 				return;
 			}
 		}

 	}
 	else{ // n is leaf node
 		if (n.isOverFlowed == false){
 			n.insertSorted(K, T);
 			newchildentry = null
 			return;
 		}
 		else{
 			newchildentry = splitLeafNode(n);
 			LeafNode<K,T> newright = newchildentry.getValue();
 			n.nextLeaf = newright;
 			newright.previousLeaf = n;
 			return;
 		}
 	}
 }

 /**
  * TODO Split a leaf node and return the new right node and the splitting
  * key as an Entry<slitingKey, RightNode>
  * 
  * @param leaf, any other relevant data
  * @return the key/node pair as an Entry
  */
 public Entry<K, Node<K,T>> splitLeafNode(LeafNode<K,T> leaf) {
	 //null check
	 if (leaf == null) {
		return null; 
	 }
	 //Get split key
	 int keySize = leaf.keys.size();
	 K splitKey = leaf.keys.get(keySize/2);
	 
	 //Get values to go into right leaf node and create it 
	 ArrayList<T> rightVals = (ArrayList<T>) leaf.values.subList(keySize/2, keySize);
	 ArrayList<K> rightKeys = (ArrayList<K>) leaf.keys.subList(keySize/2, keySize);
	 LeafNode<K,T> rightLeaf = new LeafNode<K,T>(rightKeys, rightVals);
	 
	 //remove values from left leaf node
	 leaf.values = (ArrayList<T>) leaf.values.subList(0, keySize/2);
	 leaf.keys = (ArrayList<K>) leaf.keys.subList(0, keySize/2);
	 
	 //Bring back the order of the leaves
	 rightLeaf.nextLeaf = leaf.nextLeaf;
	 rightLeaf.previousLeaf = leaf;
	 leaf.nextLeaf = rightLeaf;
	 
	 //return the new leaf node and the split key
	 Entry<K,Node<K,T>> entry =
			    new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rightLeaf);
	 return entry;
 }

 /**
  * TODO split an indexNode and return the new right node and the splitting
  * key as an Entry<slitingKey, RightNode>
  * 
  * @param index, any other relevant data
  * @return new key/node pair as an Entry
  */
 public Entry<K, Node<K,T>> splitIndexNode(IndexNode<K,T> index) {
	 //check null case
	 if (index == null) {
		 return null;
	 }
	 int swapNum = index.keys.size()/2;
	 
	 //Get split key
	 K splitKey = index.keys.get(swapNum);
	 
	 //generate keys and children for each side
	 ArrayList<K> leftKeys = (ArrayList<K>) index.keys.subList(0, swapNum);
	 ArrayList<K> rightKeys = (ArrayList<K>) index.keys.subList(swapNum+1, index.keys.size());
	 ArrayList<Node<K,T>> leftChild = (ArrayList<Node<K,T>>) index.children.subList(0, swapNum+1);
	 ArrayList<Node<K,T>> rightChild = (ArrayList<Node<K,T>>) index.children.subList(swapNum+1, index.children.size());
	 
	 //create new index node
	 IndexNode<K,T> rightIndex = new IndexNode<K,T>(rightKeys, rightChild);
	 
	 //switch out original index's keys
	 index.keys = leftKeys;
	 index.children = leftChild;
	 
	 //Return 
	 Entry<K,Node<K,T>> entry =
			    new AbstractMap.SimpleEntry<K, Node<K,T>>(splitKey, rightIndex);
	 return entry; 
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
	 boolean foundNext = false;
	 Node<K,T> searchNode = root;
	 IndexNode<K,T> index;
	 ArrayList<Node<K,T>> children;
	 ArrayList<K> nodeKeys;
	 //traverse through the tree until it finds a leafNode
	 while(!searchNode.isLeaf()) {
		 foundNext = false;
		 index = (IndexNode<K,T>) searchNode;
		 nodeKeys = (ArrayList<K>) searchNode.getKeys();
		 children = index.getChildren();
		 for (int i = 0; i < nodeKeys.size(); i++) {
			 if (nodeKeys.get(i).compareTo(key) > 0) {
				 searchNode = children.get(i);
				 foundNext = true;
				 break;
			 }
		}
		if (!foundNext) {
			searchNode = children.get(nodeKeys.size());
		}
		 
	}
	return (LeafNode<K,T>) searchNode;
 }

}
