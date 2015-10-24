import java.util.*;
import static org.junit.Assert.*;
public class BCNF {

  /**
   * Implement your algorithm here
   **/
  public static Set<AttributeSet> decompose(AttributeSet attributeSet,
                                            Set<FunctionalDependency> functionalDependencies) {
    System.out.println("Decomposing " + attributeSet.toString());
    //Already BCNF, since no functional dependencies
    if (functionalDependencies.isEmpty()) {
      return Collections.emptySet();
    }
    Set<AttributeSet> returnSet = new HashSet<AttributeSet>();
    //Only 1 element, already BCNF
    if (attributeSet.size() == 1) {
      return returnSet;
    }
    //Otherwise... 
    //Get attributes
    Set<Attribute> hashAtt = attributeSet.toSet();
    //Create power set from attributes
    Set<Set<Attribute>> powerSet = powerset(hashAtt);
    //Go through powerset and make an Attribute Set for each element
    for (Set<Attribute> attSubSet : powerSet) {
      AttributeSet x = new AttributeSet();
      //Create attribute set from Set of attributes
      for (Attribute att : attSubSet) {
        x.addAttribute(att);
      }
      //Get closure set
      AttributeSet closureSetInclusive = closure(x, functionalDependencies);
      
      //Get closure set with only values in the current set
      AttributeSet closureSet = new AttributeSet();
      Iterator<Attribute> closureIter = closureSetInclusive.iterator();
      while (closureIter.hasNext()) {
        Attribute nextClosureAtt = closureIter.next();
        if (attributeSet.contains(nextClosureAtt)) {
          closureSet.addAttribute(nextClosureAtt);
        }
      }
      
      //If closure is not minimal, and it is not maximal, then split into closure and (total - closure + independent)
      if (!(closureSet.size() == x.size()) && !closureSet.equals(attributeSet)) {  //neither superkey nor pointing just to itself
        if (closureSet.size() < x.size()) {
          System.out.println("Closure is less than size?!?!");
          assertTrue(false);
        }
        System.out.println("First set is " + x.toString());
        System.out.println("Closure set is " + closureSet.toString());
        System.out.println("Attribute set is " + attributeSet.toString());
        Set<AttributeSet> closureRecurse = decompose(closureSet, functionalDependencies); //Decompose the closure set
        returnSet.addAll(closureRecurse); //Add closure decomposition to response
        
        AttributeSet withoutClosure = new AttributeSet(); //Get faulty subset plus other elements not in subset
        Iterator<Attribute> iter = attributeSet.iterator();
        while (iter.hasNext()) {
          Attribute nextAtt = iter.next();
          if (x.contains(nextAtt) || !closureSet.contains(nextAtt)) {
            if (x.contains(nextAtt)) {
              System.out.println("Next Att is in X");
            }
            else 
              System.out.println("Next Att is not in closureset");
            System.out.println("Adding element " + nextAtt);
            withoutClosure.addAttribute(nextAtt);
          }
        }
        System.out.println("Without Closure set is " + withoutClosure.toString());
        Set<AttributeSet> withoutClosureRecurse = decompose(withoutClosure, functionalDependencies);
        returnSet.addAll(withoutClosureRecurse); //Add non closure decomposition to response
        break;
      }
    }
    if (returnSet.size() == 0) { //If nothing has been returned, then return the current set
      returnSet.add(attributeSet);
    }
    System.out.println("Returning " + returnSet.toString());
    return returnSet;
  }

  /**
   * Returns the closure of an attribute set given the list of functional dependencies
   **/  
  public static AttributeSet closure(AttributeSet attributeSet, Set<FunctionalDependency> functionalDependencies) {
    /*boolean changed = true;
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
    
    return returnAtt;*/
    
    //TODO: DO IMPLEMENTATION THAT THEY WANT
    //Part 1: Initialization:
    Hashtable<Attribute, ArrayList<FunctionalDependency>> attHash = new Hashtable<Attribute, ArrayList<FunctionalDependency>>();
    ArrayList<FunctionalDependency> dependencyList = new ArrayList<FunctionalDependency>(functionalDependencies);
    Hashtable<FunctionalDependency, Integer> countHash = new Hashtable<FunctionalDependency, Integer>();
    for (int i = 0; i < dependencyList.size(); i++) { 
      FunctionalDependency dependency = dependencyList.get(i);
      Iterator<Attribute> independentIter = dependency.independent().iterator();
      countHash.put(dependency, new Integer(dependency.independent().size()));
      while (independentIter.hasNext()) {
        Attribute nextAtt = independentIter.next();
        ArrayList<FunctionalDependency> funcList = attHash.get(nextAtt);
        if (funcList == null) {
          funcList = new ArrayList<FunctionalDependency>();
          attHash.put(nextAtt, funcList);
        }
        funcList.add(dependency);
      }
    } 
    AttributeSet newdep = new AttributeSet(attributeSet);
    AttributeSet update = new AttributeSet(attributeSet);
    
    //PART 2: Computation
    while (update.size() != 0) {
      Attribute nextAtt = update.getFirst();
      update.removeAttribute(nextAtt);
      ArrayList<FunctionalDependency> funcList = attHash.get(nextAtt);
      if (funcList == null) {
        continue;
      }
      for (int i = 0; i < funcList.size(); i++) {
        FunctionalDependency dependency = funcList.get(i);
        Integer count = countHash.get(dependency);
        count--; 
        countHash.put(dependency, count);
        System.out.println(count);
        if (count == 0) {
          AttributeSet add = new AttributeSet(dependency.dependent());
          Iterator<Attribute> addIter = add.iterator();
          while (addIter.hasNext()) {
            Attribute addingAtt = addIter.next();
            newdep.addAttribute(addingAtt);
            update.addAttribute(addingAtt);
          }
        }
      }
    }
    return newdep;
  }
  
  public static Set<Set<Attribute>> powerset(Set<Attribute> att) {
    if (att == null || att.size() == 0) {
      return new HashSet<Set<Attribute>>();
    }
    HashSet<Set<Attribute>> responseSet = new HashSet<Set<Attribute>>();
    responseSet.add(att);
    if (att.size() == 1) {
      return responseSet;
    }
    Iterator<Attribute> iter = att.iterator();
    while (iter.hasNext()) {
      Attribute nextAtt = iter.next();
      HashSet<Attribute> recurseAtt = new HashSet<Attribute>(att);
      recurseAtt.remove(nextAtt);
      Set<Set<Attribute>> recurseSet = powerset(recurseAtt);
      responseSet.addAll(recurseSet);
    }
    return responseSet;
    
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
