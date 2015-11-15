import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.ArrayList;

import java.io.IOException;

/**
 * You can modify this class as you see fit.  You may assume that the global
 * centroids have been correctly initialized.
 */
public class PointToClusterMapper extends Mapper<Point, Text, IntWritable, Point>
{

		/**
		 * Map a point to the nearest cluster point
		 * Key: a point
		 * Value: nothing
		 */
		public void map(Point key, Text value, Context context) throws IOException, InterruptedException {

				ArrayList<Point> centroids = KMeans.centroids;
		    Point closest_cent = centroids.get(0);
				float closest_dist = Point.distance(closest_cent, key);
				int closest_spot = 0;
				for (int i = 1; i < centroids.size(); i++) {
						Point compare_point = centroids.get(i);
						float new_dist = Point.distance(compare_point, key);
						if (new_dist < closest_dist) {
								closest_cent = compare_point;
								closest_dist = new_dist;
								closest_spot = i;
						}
				}
				context.write(new IntWritable(closest_spot), key);		
		}

}

/*

	 public static class FileMapper 
	 extends Mapper<Text, Text, IntWritable, Point> {   
/**
 * Map a file to points that all have the same key.
 * @param key     The string representing a point, with coordinates separated by spaces
 * @param value   Nothing
 public void map(Text key, Text value, Context context)
 throws IOException, InterruptedException
 {
 Point p = new Point(key.toString());    
 assertTrue(p.getDimension() == dimension, "Invalid Dimension");
// Map all the points to the same key, so reducer can find centroids
context.write(KMeans.one, p);
}
}
 */
