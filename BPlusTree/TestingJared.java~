import static org.junit.Assert.assertEquals;

/**
 * Auto Generated Java Class.
 */
public class TestingJared {
  
  
  public static void main(String[] args) { 
 /*System.out.println("HEllo!");
    BPlusTree<Integer, String> tree = new BPlusTree<Integer, String>();
    tree.insert(3,"3");
    System.out.println(tree.search(3));
    tree.insert(1,"1");
    System.out.println(tree.search(1));

    tree.insert(2,"2");
    System.out.println(tree.search(2));


    tree.insert(4, "4");
    System.out.println(tree.search(4));

    tree.insert(5, "5");
    System.out.println(tree.search(5));


    tree.insert(6, "6");
    System.out.println(tree.search(6));

    tree.insert(7, "7");
    System.out.println(tree.search(7));

    tree.insert(8, "8");
    System.out.println(tree.search(8));

   tree.insert(9, "9");
   System.out.println(tree.search(9));

    tree.insert(10, "10");
    tree.insert(11, "11");
    tree.insert(12, "12");
    tree.insert(13, "13");
    tree.insert(14, "14");
    tree.insert(15, "15");
    tree.insert(16, "16");
    tree.insert(17, "17");
    tree.insert(18, "18");
    tree.insert(19, "19");
    tree.insert(20, "20");
    System.out.println(tree.search(20));

    tree.delete(5);
    //System.out.println(tree.search(5));
    //System.out.println(tree.search(5));


    tree.delete(3);
     tree.delete(4);
    tree.delete(2);
    assert(tree.root != null);
    
    //tree.delete(3);


    // tree.insert('g', "G");
    Utils.printTree(tree);
*/
	  Integer testNumbers[] = new Integer[] { 2, 4, 7, 8, 5, 6, 3 };
	  String testNumberStrings[] = new String[testNumbers.length];
	  for (int i = 0; i < testNumbers.length; i++) {
	   testNumberStrings[i] = (testNumbers[i]).toString();
	  }
	  BPlusTree<Integer, String> tree = new BPlusTree<Integer, String>();
	  Utils.bulkInsert(tree, testNumbers, testNumberStrings);
	  
	  System.out.println("printing this");
	  Utils.printTree(tree);

	  tree.delete(6);
	  Utils.printTree(tree);
	  tree.delete(7);
	  Utils.printTree(tree);
	  tree.delete(8);

	  String test = Utils.outputTree(tree);
	  Utils.printTree(tree);

	  String result = "@4/@%%[(2,2);(3,3);]#[(4,4);(5,5);]$%%";
	  System.out.println(result);
	  assertEquals(result, test);
  }
  
  
  
  /* ADD YOUR CODE HERE */
  
}
