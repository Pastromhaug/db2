/**
 * Auto Generated Java Class.
 */
public class TestingJared {
  
  
  public static void main(String[] args) { 
	System.out.println("HEllo!");
    BPlusTree<Character, String> tree = new BPlusTree<Character, String>();
    tree.insert('b',"B");
    tree.insert('a',"A");
    tree.insert('c',"C");
    System.out.println(Utils.outputTree(tree));
  }
  
  /* ADD YOUR CODE HERE */
  
}
