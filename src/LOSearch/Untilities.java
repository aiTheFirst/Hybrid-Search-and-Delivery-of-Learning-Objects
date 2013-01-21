package LOSearch;


import edu.udo.cs.wvtool.main.WVTWordVector;


public class Untilities {
	public static double dotproduct(WVTWordVector v1, WVTWordVector v2)
	{
		double result = 0;
		double[] v1v = v1.getValues();
		double[] v2v = v2.getValues();
		for ( int i = 0 ; i < v1v.length ; ++i )
		{
//			System.out.println(v1v[i]);
			result += v2v[i]*v1v[i];
		}
		
		return result;
	}

}

