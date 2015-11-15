import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.ArrayList;

import java.io.IOException;

/** 
 * You can modify this class as you see fit, as long as you correctly update the
 * global centroids.
 */
public class ClusterToPointReducer extends Reducer<Point, Point, IntWritable, Point>
{

	public void reduce(IntWritable key, Iterable<Point> values, Context context) throws IOException, InterruptedException {
		int keyVal = key.get();
		Point new_cent = Point.averagePoints(values);
		Point old_cent = KMeans.centroids.get(keyVal);
		if (new_cent.compareTo(old_cent) != 0) {
			context.write(key, new_cent);
		}
	}
	

}



/*

    public static class FileReducer
        extends Reducer<IntWritable, Point, IntWritable, Point>
    {
        /**
         * Picks k points and makes them to the initial centroids.
         * Emits all the points unchanged with some arbitrary unique key.
         * @param set        Some arbitrary int.
         * @param values     A bunch of points    
        public void reduce(IntWritable key, Iterable<Point> values, Context context)
            throws IOException, InterruptedException
        {
            ArrayList<Point> pts = new ArrayList<Point>();
            int counter = 0;
            for (Point p : values)
            {
                pts.add(new Point(p));
                context.write(new IntWritable(counter), p);
                ++counter;
            }
            assertTrue(k <= pts.size(), "k too high");

            // To choose k elements to be the starting centroids, we will shuffle the list and choose
            // the first k elements.
            Collections.shuffle(pts, rng);
            for (int i = 0; i < k; ++i)
            {
                centroids.set(i, pts.get(i));
            }
        }
    }
*/
