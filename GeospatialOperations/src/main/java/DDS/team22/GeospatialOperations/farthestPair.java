package DDS.team22.GeospatialOperations;

/**
 * Find the Geometry Farthest Pair of Points
 * Input: Points like x,y
 * Output: Pair of points like x1,y1 and x2,y2 in new lines
 */
import java.util.Arrays;
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class farthestPair 
{
    public static void main( String[] args )
    {
    	/*
    	 * Input File path and output directory for the result. Call Farthest Point Function
    	 */
        String inputFile = "/home/user22/Downloads/points.csv";
        String outputDir = "/home/user22/Downloads/output";
        farthestPoints(inputFile,outputDir);
        
    }
    
    public static void farthestPoints(String inputFile, String outputDir)
    {
    	/*
    	 * Create the java SparkContext and call the function getFarthestPoints
    	 * getFarthestPoints function computes the pair of points with farthest distances
    	 */
    	SparkConf conf = new SparkConf().setAppName("FarthestPointinSetofPoints");
    	JavaSparkContext sc = new JavaSparkContext(conf);
    	getFarthestPoints(inputFile,outputDir,sc);
    }
    
    @SuppressWarnings("serial")
	public static void getFarthestPoints(String inputFile, String outputDir, JavaSparkContext sc)
    {
    	/*
    	 * Read the input file into RDD and transform the values into Points
    	 * Take cartesian product of rdd with itself to get Pair of points Rdd
    	 * Create a Pair Rdd with distance between pair of points and the pair of points
    	 */
    	
    	//Read the input file and converts it into Rdd
    	
    	JavaRDD<String> inputRdd = sc.textFile(inputFile);
    	
    	//Get the String points into objects of point class
    	
    	JavaRDD<Point> pointRdd = inputRdd.map(new Function<String, Point>() {
        	public Point call(String s) {
        		String[] splitstring =s.split(",");
        		Float[] point = new Float[splitstring.length];
        		for (int i = 0 ; i<splitstring.length ; i++ )
        			point[i] = Float.parseFloat(splitstring[i]);
        		return new Point(point[0], point[1]);
        		}
        	});
    
    	
    	 //Create the cartesian product of the rdd to find all pair of points
    	 
    	JavaPairRDD<Point,Point> cartesian = pointRdd.cartesian(pointRdd);
    	
    	/*
    	 * Create the Pair Rdds with distance between pair of points with same pair of points
    	 */

    	JavaPairRDD<Double, PairOfPoints> resultingRdd = cartesian.mapToPair(new PairFunction<Tuple2<Point,Point>,Double, PairOfPoints>() {
    		public Tuple2<Double, PairOfPoints> call(Tuple2<Point,Point> v1) {
				double distance;
    			Point p1 = v1._1();
				Point p2 = v1._2();
				distance = Point.getDistPairOfPoints(p1, p2);
    			return new Tuple2<Double,PairOfPoints>(distance,new PairOfPoints(p1,p2));
    			
    		}
    	});
    	
    	//Sort the rdd by key in descending order to get maximum distance
    	
    	JavaPairRDD<Double, PairOfPoints> farthestPair = resultingRdd.sortByKey(false);
    	
    	// Get the first line of the rdd with is the farthest pair of points
    	
    	Tuple2<Double, PairOfPoints> PointFarthest = farthestPair.first();
    	
    	//Parse Objects and Create a rdd with the farthest pair of points and saves it into output directory
    	
    	sc.parallelize(Arrays.asList(Float.toString(PointFarthest._2().getPoint()[0].xcord())
    								+ "," + Float.toString(PointFarthest._2().getPoint()[0].ycord())
    								, Float.toString(PointFarthest._2().getPoint()[1].xcord()) 
    								+ "," + Float.toString(PointFarthest._2().getPoint()[1].ycord()))
    								).saveAsTextFile(outputDir);
   
    	 }    
}
