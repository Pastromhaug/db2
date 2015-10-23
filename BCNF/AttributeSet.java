import java.util.*;

/**
 * An unordered set of Attributes. This could very easily be a Java collection,
 * but an important operation (namely examining the powerset) is not easily done
 * with the Java collection.
 **/
public class AttributeSet {

 //a list of the backing attributes
 private final List<Attribute> _attributes;

 //construct an empty AttributeSet
 public AttributeSet() {
  _attributes = new ArrayList<>();
 }

 //copy constructor
 public AttributeSet(AttributeSet other) {
  _attributes = new ArrayList<>(other._attributes);
 }

 public void addAttribute(Attribute a) {
  if(!_attributes.contains(a))
   _attributes.add(a);
 }
 
 public void removeAttribute(Attribute a) {
   if(_attributes.contains(a))
   _attributes.add(a);
 }

 public boolean contains(Attribute a) {
  return _attributes.contains(a);
 }

 public int size() {
  return _attributes.size();
 }

 public boolean equals(Object other) {
  if(other == null || !(other instanceof AttributeSet)){
   System.out.println("HERE");
   return false;
  }
  AttributeSet otherAtt = (AttributeSet) other;
  if (otherAtt.size() != _attributes.size()) {
    
    System.out.println("Other size is " + otherAtt.size());
    System.out.println("This size is " + _attributes.size());
    return false;
  }
  for (int i = 0; i < _attributes.size(); i++) {
    if (!otherAtt.contains(_attributes.get(i))) {
      return false;
    }
  }
  return true;
 }

 public Iterator<Attribute> iterator() {
  return _attributes.iterator();
 }

 public String toString() {
  String out = "";
  Iterator<Attribute> iter = iterator();
  while(iter.hasNext())
   out += iter.next() + "\t";

  return out;
 }
}
