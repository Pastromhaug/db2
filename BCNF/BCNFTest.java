import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class BCNFTest {
  /**
   * Performs a basic test on a simple table.
   * gives input attributes (a,b,c) and functional dependency a->c
   * and expects output (a,c),(b,c) or any reordering
   **/
  @Test
  public void testSimpleBCNF() {
    //construct table
    AttributeSet attrs = new AttributeSet();
    attrs.addAttribute(new Attribute("a"));
    attrs.addAttribute(new Attribute("b"));
    attrs.addAttribute(new Attribute("c"));

    //create functional dependencies
    Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
    AttributeSet ind = new AttributeSet();
    AttributeSet dep = new AttributeSet();
    ind.addAttribute(new Attribute("a"));
    dep.addAttribute(new Attribute("c"));
    FunctionalDependency fd = new FunctionalDependency(ind, dep);
    fds.add(fd);

    //run client code
    Set<AttributeSet> bcnf = BCNF.decompose(attrs, fds);

    //verify output
    assertEquals("Incorrect number of tables", 2, bcnf.size());

    for(AttributeSet as : bcnf) {
      assertEquals("Incorrect number of attributes", 2, as.size());
      assertTrue("Incorrect table", as.contains(new Attribute("a")));
    }

  }
  
  //Tests closure
  @Test
  public void closure() {
    AttributeSet attrs = new AttributeSet();
    Attribute a = new Attribute("a");
    Attribute b = new Attribute("b");
    Attribute c = new Attribute("c");
    Attribute d = new Attribute("d");
    Attribute e = new Attribute("e");
    Attribute f = new Attribute("f");
    Attribute g = new Attribute("g");
    Attribute h = new Attribute("h");
    attrs.addAttribute(a);
    Set <FunctionalDependency> fds = new HashSet<FunctionalDependency>();
    AttributeSet ind1 = new AttributeSet();
    AttributeSet dep1 = new AttributeSet();
    ind1.addAttribute(a);
    dep1.addAttribute(b);
    FunctionalDependency fd = new FunctionalDependency(ind1, dep1);
    fds.add(fd);
    AttributeSet responseAttrs = new AttributeSet();
    responseAttrs.addAttribute(a);
    responseAttrs.addAttribute(b);
    AttributeSet response = BCNF.closure(attrs, fds);
    assertTrue("Incorrect response", response.equals(responseAttrs)); //TESTS SIMPLE CLOSURE
    AttributeSet ind2 = new AttributeSet();
    AttributeSet dep2 = new AttributeSet();
    ind2.addAttribute(b);
    dep2.addAttribute(c);
    fd = new FunctionalDependency(ind2, dep2);
    fds.add(fd);
    responseAttrs.addAttribute(c);
    response = BCNF.closure(attrs, fds);
    assertTrue("Transitivity does not work", response.equals(responseAttrs)); //TESTS CLOSURE TRANSITIVE
    AttributeSet ind3 = new AttributeSet();
    AttributeSet dep3 = new AttributeSet();
    ind3.addAttribute(a);
    ind3.addAttribute(b);
    dep3.addAttribute(d);
    fd = new FunctionalDependency(ind3, dep3);
    fds.add(fd);
    responseAttrs.addAttribute(d);
    response = BCNF.closure(attrs, fds);
    assertTrue("2 to 1 does not work", response.equals(responseAttrs)); //TESTS 2 to 1
    AttributeSet ind4 = new AttributeSet();
    AttributeSet dep4 = new AttributeSet();
    ind4.addAttribute(a);
    dep4.addAttribute(e);
    dep4.addAttribute(f);
    fd = new FunctionalDependency(ind4, dep4);
    fds.add(fd);
    responseAttrs.addAttribute(e);
    responseAttrs.addAttribute(f);
    response = BCNF.closure(attrs, fds);
    assertTrue("1 to 2 does not work", response.equals(responseAttrs)); //TESTS 1 to 2
    AttributeSet ind5 = new AttributeSet();
    AttributeSet dep5 = new AttributeSet();
    ind5.addAttribute(a);
    ind5.addAttribute(g);
    dep5.addAttribute(h);
    fd = new FunctionalDependency(ind5, dep5);
    fds.add(fd);
    response = BCNF.closure(attrs, fds);
    assertTrue("Should not have included that fd", response.equals(responseAttrs)); //TESTS bad fd
  }
}