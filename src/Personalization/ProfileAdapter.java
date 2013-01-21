package personalization;

import Comparator.CompareResult;
import Comparator.RuleCompareResult;
import Utilities.*;
import java.util.ArrayList;
import java.util.List;


public class ProfileAdapter {
	UserProfile userProfile;
	int limit = 20;
	final double step = 0.001;
	public ProfileAdapter(UserProfile profile)
	{
		userProfile = profile;
	}
	public void adapt(List<CompareResult> compareResults , List<UserFeedback> feedbacks)
	{
		for ( int i = 0 ; i < compareResults.size() && i < limit ; ++i )
		{
			CompareResult cr = compareResults.get(i);
			
			if ( feedbacks.get(i) == UserFeedback.Useful  )
			{
					for ( RuleCompareResult rcr : cr.ruleResults)
					{
						double w = userProfile.attributesWeights.get(rcr.rule.userProfileParam);
						if ( rcr.score > 0 )
						{
//							System.err.println(""+i+" Increasing "+ rcr.rule.userProfileParam );
							userProfile.attributesWeights.put(rcr.rule.userProfileParam, ((w + step <= 0.9)? w + step : 0.9));
						}
						else
						{
//							System.err.println(""+i+" Decreasing "+ rcr.rule.userProfileParam );
							userProfile.attributesWeights.put(rcr.rule.userProfileParam, ((w - step >= 0.1)? w - step : 0.1));
						}
					}
			}
			else if ( feedbacks.get(i) == UserFeedback.Auxiliary  )
			{
					for ( RuleCompareResult rcr : cr.ruleResults)
					{
						double w = userProfile.attributesWeights.get(rcr.rule.userProfileParam);
						if ( rcr.score > 0 )
						{
//							System.err.println(""+i+" Increasing "+ rcr.rule.userProfileParam + " AUX");
							userProfile.attributesWeights.put(rcr.rule.userProfileParam, ((w + step/2 <= 0.9)? w + step/2 : 0.9));
						}
						else
						{
//							System.err.println(""+i+" Decreasing "+ rcr.rule.userProfileParam + " AUX");
							userProfile.attributesWeights.put(rcr.rule.userProfileParam, ((w - step/2 >= 0.1)? w - step/2 : 0.1));
						}

					}
			}
//			else if ( feedbacks.get(i) == UserFeedback.NotUseful  )
//			{
//				for ( CompareResult cr : compareResults )
//				{
//					for ( RuleCompareResult rcr : cr.ruleResults)
//					{
//						double w = userProfile.attributesWeights.get(rcr.rule.userProfileParam);
//						if ( rcr.score > 0 )
//							userProfile.attributesWeights.put(rcr.rule.userProfileParam, ((w - step/2 >= 0.1)? w - step/2 : 0.1));
//						else
//							userProfile.attributesWeights.put(rcr.rule.userProfileParam, ((w + step/2 <= 0.9)? w + step/2 : 0.9));
//					}
//				}
//			}
			
		}
	}
	final double THRESHOLD_USEFUL = 0.30;
	final double THRESHOLD_AUX = 0.26;
	public List<UserFeedback> estimateUserFeedback(List<CompareResult> realCompareResult , List<CompareResult> perfectCompareResult)
	{
		List<UserFeedback> feedbacks = new ArrayList<UserFeedback>();
		int count = 0;
		for ( int i = 0 ; i < realCompareResult.size() && i < limit ; ++i)
		{
			UserFeedback fb = UserFeedback.NotUseful;
			for ( int j = 0 ; j < perfectCompareResult.size(); ++j )
			{
//				System.out.println(realCompareResult.get(i).lo.LOName + "  -  "+ perfectCompareResult.get(j).lo.LOName);
				if ( realCompareResult.get(i).lo.LOName.equals(  perfectCompareResult.get(j).lo.LOName  ))
				{
//					System.out.println("---"+realCompareResult.get(i).lo.LOName + "  "+j);
//					if (perfectCompareResult.get(j).score >= THRESHOLD_USEFUL || j == 0)
//						fb = UserFeedback.Useful;
//					else if (perfectCompareResult.get(j).score >= THRESHOLD_AUX || j == 1 )
//						fb = UserFeedback.Auxiliary;
					if ( perfectCompareResult.get(j).score >= perfectCompareResult.get(7).score )
						fb = UserFeedback.Useful;
					else if ( perfectCompareResult.get(j).score >= perfectCompareResult.get(20).score )
						fb = UserFeedback.Auxiliary;
					
					count ++;
				}
			}
			feedbacks.add(fb);
		}
//		System.out.println("Count ------- : "+count);
		return feedbacks;
	}
}
