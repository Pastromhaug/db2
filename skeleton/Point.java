import java.io.*; // DataInput/DataOuput
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import org.apache.hadoop.io.*; // Writable

/**
 * A Point is some ordered list of floats.
 *
 * A Point implements WritableComparable so that Hadoop can serialize
 * and send Point objects across machines.
 *
 * NOTE: This implementation is NOT complete.  As mentioned above, you need
 * to implement WritableComparable at minimum.  Modify this class as you see fit.
 */
public class Point implements WritableComparable{
		public int dimension;
		public float[] dimArray;
		/**
		 * Construct a Point with the given dimensions [dim]. The coordinates should all be 0.
		 * For example:
		 * Constructing a Point(2) should create a point (x_0 = 0, x_1 = 0)
		 */
		public Point(int dim)
		{
				dimension = dim;
				dimArray = new float[dim];
		}

		public Point() {
				dimension = -1;
				dimArray = null;
		}

		/**
		 * Construct a point from a properly formatted string (i.e. line from a test file)
		 * @param str A string with coordinates that are space-delimited.
		 * For example:
		 * Given the formatted string str="1 3 4 5"
		 * Produce a Point {x_0 = 1, x_1 = 3, x_2 = 4, x_3 = 5}
		 */
		public Point(String str)
		{
				String[] points = str.split(" ");
				dimension = points.length;
				dimArray = new float[dimension];
				for (int i = 0; i < dimension; i++) {
						dimArray[i] = Float.parseFloat(points[i]);
				}
		}

		/**
		 * Copy constructor
		 */
		public Point(Point other)
		{
				this(other.toString());
		}

		/**
		 * @return The dimension of the point.  For example, the point [x=0, y=1] has
		 * a dimension of 2.
		 */
		public int getDimension()
		{
				return dimension;
		}

		/**
		 * Converts a point to a string.  Note that this must be formatted EXACTLY
		 * for the autograder to be able to read your answer.
		 * Example:
		 * Given a point with coordinates {x=1, y=1, z=3}
		 * Return the string "1 1 3"
		 */
		public String toString()
		{
				if (dimension <= 0) {
						return "";
				}
				String rep = "" + dimArray[0];
				for (int i = 1; i < dimension; i++) {
						rep += " " + dimArray[i];
				}
				return rep;
		}

		/**
		 * One of the WritableComparable methods you need to implement.
		 * See the Hadoop documentation for more details.
		 * You should order the points "lexicographically" in the order of the coordinates.
		 * Comparing two points of different dimensions results in undefined behavior.
		 */
		public int compareTo(Object o)
		{
				if (!(o instanceof Point)) {
						return 0;
				}
				Point comparePoint =  (Point)o;
				if (comparePoint.getDimension() != dimension) {
						return 0;
				}
				for (int i = 0; i < dimension; i++) {
						if (comparePoint.dimArray[i] > dimArray[i]) {
								return -1;
						}
						else if (comparePoint.dimArray[i] < dimArray[i]) {
								return 1;
						}
				}
				return 0;
		}

		/**
		 * @return The L2 distance between two points.
		 */
		public static final float distance(Point x, Point y)
		{
				float total = 0;
				for (int i = 0; i < x.dimension; i++){
						float diff = x.dimArray[i] - y.dimArray[i];
						total += diff*diff;
				}
				return (float) Math.sqrt(total);
		}

		/**
		 * @return A new point equal to [x]+[y]
		 */
		public static final Point addPoints(Point x, Point y)
		{
				Point added = new Point(x.dimension);
				for (int i = 0; i < x.dimension; i++){
						added.dimArray[i] = x.dimArray[i] + y.dimArray[i];
				}
				return added;
		}

		public static final Point averagePoints(Iterable<Point> list) {
				if (list == null) {
						return null;
				}
				Iterator<Point> iter = list.iterator();
				
				if (!iter.hasNext()) {
						return null;
				}
				Point first_point = iter.next();
				Point added = new Point(first_point.dimension);
				int dim = added.dimension;
				float points_added = 1;
				//initialize point
				for (int j = 0; j < dim; j++) {
						added.dimArray[j] = first_point.dimArray[j];
				}
				
				//loop through the rest
				while (iter.hasNext()) {
						Point next_point = iter.next();
						if (next_point.dimension != dim) {
								continue;
						}
						points_added++;
						for (int j = 0; j < dim; j++) {
								added.dimArray[j] += next_point.dimArray[j];
						}
				}

				for (int i = 0; i < dim; i++) {
						added.dimArray[i] = added.dimArray[i] / points_added;
				}

				return added;
		}

		/**
		 * @return A new point equal to [c][x]
		 */
		public static final Point multiplyScalar(Point x, float c)
		{
				Point scalar = new Point(x.dimension);
				for (int i = 0; i < x.dimension; i++){
						scalar.dimArray[i] = x.dimArray[i] * c;
				}
				return scalar;
		}

		public void write(DataOutput out) throws IOException {
				return;
		}

		public void readFields(DataInput in) throws IOException {
				return;
		}

		public static void read(DataInput in) throws IOException {
				return;
		}

}
