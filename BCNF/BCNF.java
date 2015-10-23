import java.util.*;

public class BCNF {

  /**
   * Implement your algorithm here
   **/
  public static Set<AttributeSet> decompose(AttributeSet attributeSet,
                                            Set<FunctionalDependency> functionalDependencies) {
    //Already BCNF, since no functional dependencies
    if (functionalDependencies.isEmpty()) {
      return Collections.emptySet();
    }
    return Collections.emptySet();
  }

  /**
   * Recommended helper method
   **/  
  public static AttributeSet closure(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
    boolean changed = true;
    AttributeSet returnAtt = new AttributeSet(attributeSet);
    while(changed)
    {
      changed = false;
      for (FunctionalDependency dependency : functionalDependencies) { 
        if (containsAll(returnAtt, dependency.independent()) && !containsAll(returnAtt, dependency.dependent())) {
          Iterator<Attribute> iter = dependency.dependent().iterator();
          while (iter.hasNext()) {
            Attribute nextAtt = iter.next();
            if (!returnAtt.contains(nextAtt)) {
              returnAtt.addAttribute(nextAtt);
              changed = true;
            }
          }
        }
      }
    }
    
    return returnAtt;
    
    //TODO: DO IMPLEMENTATION THAT THEY WANT
    //Part 1: Initialization:
    //Hashtable<String, Attribute> attHash = new Hashtable<String, Attribute>();
    //for (FunctionalDependency dependency : functionalDependencies) {  
      
    }
 
  //Given two attribute sets, returns true if the second is a subset of the first, false otherwise
  public static boolean containsAll(AttributeSet att, AttributeSet sub) {
    if (att == null || sub == null) {
      return false;
    }
    Iterator<Attribute> subIter = sub.iterator();
    while (subIter.hasNext()) {
      Attribute nextAtt = subIter.next();
      if (!att.contains(nextAtt)) {
        return false;
      }
    }
    return true;
  }
  
  public static void printAttributes(AttributeSet attributeSet) {
    System.out.println(attributeSet.toString());
  }
  
}
