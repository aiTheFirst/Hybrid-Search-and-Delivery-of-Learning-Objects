package personalization;

import Comparator.CompareResult;
import java.util.List;
import java.util.Random;


public class RandomizeResults {
	Random rand = new Random(329);
	public void randomizeResult(List<CompareResult> list , int from ,int mid , int to)
	{
		for ( int i = 0; i < 30 ;  ++i)
		{
			int ix1 = rand.nextInt(to - mid)+from;
			int ix2 = rand.nextInt(to - mid)+mid;
//			System.out.println("randing "+ix1 + " with "+ix2);
			if ( ix1 != ix2 )
			{
				CompareResult temp =  list.get(ix1);
				list.set(ix1 , list.get(ix2) );
				list.set(ix2 , temp);
			}
		}
			
	}
}
