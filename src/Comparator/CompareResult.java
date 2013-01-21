package Comparator;

import Utilities.LO;
import java.util.ArrayList;
import java.util.List;
import personalization.*;
import personalization.UserProfile;

/**
 * Comparison Result for one rule
 *
 */
public class CompareResult implements Comparable<CompareResult>{
	public UserProfile userProfile;
	public LO lo;
	public double score;
	
	public List<RuleCompareResult> ruleResults = new ArrayList<RuleCompareResult>();
	
	@Override
	public int compareTo(CompareResult o) {
		return Double.compare(score, o.score);
	}
}
