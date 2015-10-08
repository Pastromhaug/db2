import java.util.AbstractMap;
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
  * Search the value for a specific key
  * 
  * @param key
  * @return value
  */
 public T search(K key) {
  LeafNode<K,T> leaf;
  leaf = getLeaf(key); //gets the leaf where the key should be located
  return (T) leaf.getValue(key); //return value
 }

 /**
  * Insert a key/value pair into the BPlusTree
  * 
  * @param key
  * @param value
  */
 public void insert(K key, T value) {
   //If empty tree, create a leaf
   if (root == null) {
	 root = new LeafNode<K,T>(key, value);
     return;
   }
   //call recursive insert
   insertbody(key, value, root);
 }

 //bulk work of insert function
 public Entry<K, Node<K,T>> insertbody(K key, T value, Node<K,T> n) {
	 //get list of keys
	 ArrayList<K> nodeKeys = n.keys;
	 Entry<K, Node<K,T>> newEntry;
	 
	 if (n.isLeafNode) { // n is leaf node
		LeafNode<K,T> leaf = (LeafNode<K,T>) n;
		//edge case, no values in leaf, shouldn't happen
		if (leaf.keys.size() == 0) {
			leaf.keys.add(key);
			leaf.values.add(value);
			assert(false);
		}
		else {
			leaf.insertSorted(key, value);
			if (!n.isOverflowed()) {
				return null;
			}
			else {
				newEntry = splitLeafNode(leaf);
				if (n == root) {
 					Node<K, T> newEntryChild = newEntry.getValue();
 					K newEntryKey = newEntry.getKey();
 					IndexNode<K,T> newroot = new IndexNode<K,T>(newEntryKey, n, newEntryChild);
 					root = newroot;
 					return null;
 				}
				return newEntry;
			}
		}
	 }
	 //n is an index node
	 else {
 		IndexNode<K,T> index = (IndexNode<K,T>) n;
 		int i;
 		for (i = 0; i < index.keys.size(); i++){
 			if (nodeKeys.get(i).compareTo(key) >= 0) {
 				break;
 			}
 		}
 		newEntry = insertbody(key, value, index.children.get(i));
 		if (newEntry == null) {
 			return null;
 		}
 		else {
 			index.insertSorted(newEntry);
 			if (!index.isOverflowed()) {
 				return null;
 			}
 			else {
 				newEntry = splitIndexNode(index);
 				if (n == root) {
 					Node<K, T> newEntryChild = newEntry.getValue();
 					K newEntryKey = newEntry.getKey();
 					IndexNode<K,T> newroot = new IndexNode<K,T>(newEntryKey, n, newEntryChild);
 					root = newroot;
 					return null;
 				}
 				return newEntry;
 			}
 		}

 	}
	return null;
 }

 /**
  * Split a leaf node and return the new right node and the splitting
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
	 ArrayList<T> rightVals = new ArrayList<T>(leaf.values.subList(keySize/2, keySize));
	 ArrayList<K> rightKeys = new ArrayList<K>(leaf.keys.subList(keySize/2, keySize));
	 LeafNode<K,T> rightLeaf = new LeafNode<K,T>(rightKeys, rightVals);
	 
	 //remove values from left leaf node
	 leaf.values = new ArrayList<T>(leaf.values.subList(0, keySize/2));
	 leaf.keys = new ArrayList<K>(leaf.keys.subList(0, keySize/2));
	 
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
	 ArrayList<K> leftKeys = new ArrayList<K>(index.keys.subList(0, swapNum));
	 ArrayList<K> rightKeys = new ArrayList<K>(index.keys.subList(swapNum+1, index.keys.size()));
	 ArrayList<Node<K,T>> leftChild = new ArrayList<Node<K,T>>(index.children.subList(0, swapNum+1));
	 ArrayList<Node<K,T>> rightChild = new ArrayList<Node<K,T>>(index.children.subList(swapNum+1, index.children.size()));
	 
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
  *  Delete a key/value pair from this B+Tree
  * 
  * @param key
  */
 public void delete(K key) {
	 //root should never be null
	 if (root == null) {
		 return;
	 }
	 //if root is a leaf node
	 else if (root.isLeafNode) {
		 LeafNode<K,T> leaf = (LeafNode<K,T>) root;
		 ArrayList<K> keys = leaf.keys;
		 ArrayList<T> values = leaf.values;
		 //root leaf node shouldn't be empty
		 if (keys.size() == 0) {
			 root = null;
			 return;
		 }
		 //if only one key left and it is this key
		 if (keys.size() == 1 && keys.get(0).equals(key)) {
			 root = null;
			 return;
		 }
		 //otherwise, search and delete
		 for (int i = 0; i < keys.size(); i++) {
			 if (keys.get(i).equals(key)) {
				 keys.remove(i);
				 values.remove(i);
				 return;
			 }
		 }
	 }
	 //root is index node
	 else {
		 Node<K,T> nextNode = null;
		 boolean foundNext = false;
		 IndexNode<K,T> index = (IndexNode<K,T>) root;
		 ArrayList<K> nodeKeys = (ArrayList<K>) index.keys;
		 ArrayList<Node<K,T>> children = index.children;
		 for (int i = 0; i < nodeKeys.size(); i++) {
			 if (nodeKeys.get(i).compareTo(key) > 0) {
				 nextNode = children.get(i);
				 foundNext = true;
				 break;
			 }
		}
		if (!foundNext) {
			nextNode = children.get(nodeKeys.size());
		}
		int response = delete_help(index, nextNode, key);
		if (index.children.size() == 1) {
			root = index.children.get(0);
		}
		if (response == 1) {
			System.out.println("Should i be doing something here?");
		}
	 }

 }
 
 //returns 0 if no problem, 1 if there was a reorganization
 //bulk of delete work done here
 public int delete_help(IndexNode<K,T> parent, Node<K,T> node, K key) {
	 //if leaf node
	 if (node.isLeafNode) {
		 LeafNode<K,T> leaf = (LeafNode<K,T>) node;
		 leaf.remove(key);
		 //if underflowed
		 if (leaf.isUnderflowed()) {
			 int splitkey = -1;
			 int index = -1;
			 //find child index
			 for (int i = 0; i < parent.children.size(); i++) {
				 if (parent.children.get(i).equals(node)) {
					 index = i;
					 break;
				 }
			 }
			 //should always find index
			 if (index == -1) {
				 assert(false);
			 }
			 //if no left leaf, pass in right
			 else if (index == 0) {
				 splitkey = handleLeafNodeUnderflow(leaf, leaf.nextLeaf, parent);
			 }
			 //base case
			 else {
				 splitkey = handleLeafNodeUnderflow(leaf.previousLeaf, leaf, parent);
			 }
			 if (splitkey != -1){
				 parent.keys.remove(splitkey);
				 parent.children.remove(splitkey+1);
				 return 1;
			 }
			 return 0;
		 }
		 return 0;
	 }
	 //if index
	 else {
		 IndexNode<K,T> index = (IndexNode<K,T>) node;
		 Node<K,T> nextNode = null;
		 boolean foundNext = false;
		 ArrayList<K> nodeKeys = (ArrayList<K>) index.keys;
		 ArrayList<Node<K,T>> children = index.children;
		 for (int i = 0; i < nodeKeys.size(); i++) {
			 if (nodeKeys.get(i).compareTo(key) > 0) {
				 nextNode = children.get(i);
				 foundNext = true;
				 break;
			 }
		}
		if (!foundNext) {
			nextNode = children.get(nodeKeys.size());
		}
		//recurse
		int response = delete_help(index, nextNode, key);
		
		//if something was moved, and underflowed, fix
		if (response == 1 && index.isUnderflowed()) {
			int splitkey = -1;
			int nodeLoc = -1;
			//find child_index
			for (int i = 0; i < parent.children.size(); i++) {
				 if (parent.children.get(i).equals(node)) {
					 nodeLoc = i;
					 break;
				 }
			 }
			if (nodeLoc == -1) {
				assert(false);
			}
			//handle underflow
			else if (nodeLoc == 0) {
				splitkey = handleIndexNodeUnderflow(index, (IndexNode<K,T>) parent.children.get(1), parent);
			}
			else {
				splitkey = handleIndexNodeUnderflow((IndexNode<K,T>) parent.children.get(nodeLoc-1), index, parent);
			}
			//remove splitkey if merged
			 if (splitkey != -1){
				 parent.keys.remove(splitkey);
				 parent.children.remove(splitkey);
				 return 1;
			 }
		}
		 
	 }
	 return 0;
 }

 /**
  *  Handle LeafNode Underflow (merge or redistribution)
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
	 int splitkey = -1;
	 int parentpivotindex = 0;
	 // loop through all parent keys to find the pivotkey for the merge
	 for (int j = 0; j < parent.keys.size(); j++){
		 //true if last key on left is less than key
	 	 boolean leftLessParentKey = left.keys.get(left.keys.size()-1).compareTo(parent.keys.get(j)) == -1;
	 	 //true if first key on right is greater than or equal to key
	 	 boolean rightGreaterEqualParentKey = 
	 			 right.keys.get(0).compareTo(parent.keys.get(j)) >= 1;
	 	 
	 	// if j is the index of parent key that separates left and right before merge
	 	 if (leftLessParentKey && rightGreaterEqualParentKey){ 
	 		 parentpivotindex= j;
	 		 break;
	 	 }
	 }
	 // if we should merge left and right
	 if (left.keys.size() + right.keys.size() < 2*D){ 
		 // loop through right entries and insert them into left Leaf
		 for (int i = 0; i < right.keys.size(); i++){
			 left.insertSorted(right.keys.get(i), right.values.get(i));
		 }
		 //preserve leaf order
		 left.nextLeaf = right.nextLeaf;
		 if (right.nextLeaf != null) {
			 right.nextLeaf.previousLeaf = left;
		 }
		 return parentpivotindex;
	 }
	 // we shouldn't merge left and right, will redistribute
	 else {
		 if (left.keys.size() < D){ // move entries from right to left
			 while (left.keys.size() < D){
				 left.insertSorted(right.keys.get(0), right.values.get(0));
				 right.keys.remove(0);
				 right.values.remove(0);
				 
			 }
		 }
		 else if (right.keys.size() < D){ // move entries from left to right
			 while(right.keys.size() < D){
				 right.insertSorted(left.keys.get(left.keys.size()-1), left.values.get(left.values.size()-1));
				 left.keys.remove(left.keys.size()-1);
				 left.values.remove(left.values.size()-1);
			 }
		 }
		 parent.keys.set(parentpivotindex,right.keys.get(0));
		 return splitkey; 
	 }
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
	 int splitkey = -1;
	 int parentpivotindex = 0;
	 // loop through all parent keys to find the pivotkey for the merge
	 for (int j = 0; j < parent.keys.size(); j++) {
	 	 boolean leftLessParentKey = leftIndex.keys.get(leftIndex.keys.size()-1).compareTo(parent.keys.get(j)) == -1;
	 	 boolean rightGreaterEqualParentKey = 
	 			 rightIndex.keys.get(0).compareTo(parent.keys.get(j)) >= 0;
	 	 
	 	// if j is the index of parent key that separates left and right before merge
	 	 if (leftLessParentKey && rightGreaterEqualParentKey){ 
	 		 parentpivotindex = j;
	 		 break;
	 	 }
	 }
	 // if we should merge left and right
	 if (leftIndex.keys.size() + rightIndex.keys.size() < 2D){ 
		 // loop through right entries and insert them into left Leaf
		 leftIndex.insertSorted(parent.keys.get(parentpivotindex), rightIndex.children.get(0));
		 for (int i = 0; i < rightIndex.keys.size(); i++){
			 leftIndex.insertSorted(rightIndex.keys.get(i), rightIndex.children.get(i+1));
		 }
		 return parentpivotindex;
	 }
	 // we shouldn't merge left and right, will to redistribute
	 else {
		 if (leftIndex.keys.size() < D){ // move entries from right to left
			 while (leftIndex.keys.size() < D){
				 leftIndex.insertSorted(parent.keys.get(parentpivotindex), rightIndex.children.get(0));
				 parent.keys.set(parentpivotindex, rightIndex.keys.get(0));
				 rightIndex.keys.remove(0);
				 rightIndex.children.remove(0);
			 }
		 }
		 else if (rightIndex.keys.size() < D){ // move entries from left to right
			 while(rightIndex.keys.size() < D){
				 rightIndex.insertSorted(parent.keys.get(parentpivotindex), leftIndex.children.get(leftIndex.children.size()-1));
				 parent.keys.set(parentpivotindex, leftIndex.keys.get(leftIndex.keys.size()-1));
				 leftIndex.keys.remove(leftIndex.keys.size()-1);
				 leftIndex.children.remove(leftIndex.children.size()-1);
			 }
		 }
		 return splitkey; 
	 }
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
	 while(!searchNode.isLeafNode) {
		 foundNext = false;
		 index = (IndexNode<K,T>) searchNode;
		 nodeKeys = (ArrayList<K>) searchNode.keys;
		 children = index.children;
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
