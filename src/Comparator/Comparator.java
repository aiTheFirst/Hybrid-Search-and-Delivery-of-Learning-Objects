package Comparator;

import Utilities.LO;
import Utilities.LOR;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import personalization.UserProfile;

public class Comparator {
	UserProfile userProfile;
	RuleBase ruleBase;
	double RuleCount = 17.0;

	public Comparator(UserProfile userProfile, RuleBase ruleBase) {
		this.userProfile = userProfile;
		this.ruleBase = ruleBase;
	}

	public CompareResult compareLOM(LO lo) {
		CompareResult compareResult = new CompareResult();
		compareResult.lo = lo;
		compareResult.score = 0.0;
		compareResult.userProfile = userProfile;
		
		double score = 0.0;
		double weight = 1.0;
		for (RuleBase.Rule rule : ruleBase.rules) {
			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByName("JavaScript");
			for (int i = 0; i < rule.lomParams.size(); ++i) {
				String vv = lo.lom.attributes.get(rule.lomParams.get(i));
//				System.out.println(vv);
//				System.out.flush();
				Object val = null;
				if (rule.lomParamType.get(i).equals("int"))
					val = Integer.parseInt(vv);
				else if (rule.lomParamType.get(i).equals("double"))
					val = Double.parseDouble(vv);
				else
					val = vv;
				engine.put(rule.lomParams.get(i), val);
			}
			{
				String vv = userProfile.attributes.get(rule.userProfileParam);
				Object val = null;
//				System.out.print(rule.userProfileParam);
//				System.out.println(" : " + vv);
				if (rule.userProfileParamType.equals("int"))
					val = Integer.parseInt(vv);
				else if (rule.userProfileParamType.equals("double"))
					val = Double.parseDouble(vv);
				else
					val = vv;
				engine.put(rule.userProfileParam, val);
			}

			weight = userProfile.attributesWeights.get(rule.userProfileParam);
			try {
				engine.eval(rule.ruleScript);
				Invocable invocableEngine = (Invocable) engine;
				double ruleResult = weight * (Double) invocableEngine.invokeFunction("test");
				score = score + ruleResult;
				
				
				RuleCompareResult ruleCompareResult = new RuleCompareResult();
				ruleCompareResult.rule = rule;
				ruleCompareResult.score = ruleResult;
				
				compareResult.ruleResults.add(ruleCompareResult);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		compareResult.score = score / RuleCount;
		return compareResult;
	}
	
	public List<CompareResult> compareAll(LOR lor)
	{
		List<CompareResult> compareResults = new ArrayList<CompareResult>(); 
		for ( LO lo : lor.los )
		{
			CompareResult result = compareLOM(lo);
			compareResults.add(result);			
		}
		
		Collections.sort(compareResults , Collections.reverseOrder());
		
		return compareResults;
	}
}
