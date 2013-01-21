package personalization;

import Comparator.RuleBase;
import Comparator.Comparator;
import Comparator.CompareResult;
import Utilities.LOR;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Collections;
import Controller.*;

public class PersonalizationApp extends Thread {

        public static boolean stopThread = false;
        public static String TestXMLFilename = null;




        public void run(){
                PersonalizationApp.main(null); // execute as intended by Hamid.
	}

	public static String calculateSum(List<UserFeedback> l) {
		double ru = 0, ra = 0;
		int a = 20;
		for (UserFeedback f : l) {
			if (f == UserFeedback.Useful)
				ru += 1 * a;
			if (f == UserFeedback.Auxiliary)
				ra += 0.5 * a;

			--a;
			if ( a < 10 ) break;
		}
		// return "(" + ru + "," + ra + ")";
		return "" + (int) (ru + ra);
	}

	static boolean logging = false;

        /* Similar to main, but for my better understanding - JGS
         */
        public static List<CompareResult> GetPersonalizationResults(String rulesFilename, UserProfile userProfile, String testXMLFilename){

            RuleBase ruleBase = new RuleBase();
            ruleBase.parseFromFile(rulesFilename);

            Comparator comparator = new Comparator(userProfile, ruleBase);

            LOR lor = new LOR();
            lor.parseFromFile(testXMLFilename);
            lor.getAllLoms();

            List<CompareResult> list = comparator.compareAll(lor);

            /* sort the results */
            Collections.sort(list);
            Collections.reverse(list);

            return list;
        }

        /* similar to main, but for my better understanding - JGS */
        public static void AdaptProfile(String rulesFilename, UserProfile userProfile, UserProfile targetProfile, String testXMLFilename){

            List<CompareResult> list = GetPersonalizationResults(rulesFilename,userProfile,testXMLFilename);
            List<CompareResult> targetList = GetPersonalizationResults(rulesFilename,targetProfile,testXMLFilename);

            ProfileAdapter adapt = new ProfileAdapter(userProfile);

            List<UserFeedback> feedbacks = adapt.estimateUserFeedback(list,
				targetList);


            adapt.adapt(list, feedbacks);
        }

	public static void main(String[] args) {
		RuleBase ruleBase = new RuleBase();
		ruleBase.parseFromFile("res/rulesJGS.xml");
		// for (RuleBase.Rule rule : ruleBase.rules )
		// {
		// System.out.println(rule);
		// }
		// LOM lom = new LOM();
		// lom.parseFromFile("newsamplelom1.xml");
		// Set<Entry<String, String>> atts = lom.attributes.entrySet();
		// for ( Entry<String, String> entry : atts)
		// {
		// System.out.println(entry.getKey() + " : " + entry.getValue() + " : "
		// + lom.attributesWeights.get(entry.getKey()));
		// }

		UserProfile userProfile = new UserProfile();
                EE4913App tempapp = new EE4913App();
                String user_in_context = tempapp.senduserName();//fetches profile of currently logged in user
                System.out.println("Personalization of this user file: " + user_in_context);
		userProfile.parseFromFile(user_in_context);

		UserProfile targetUserProfile = new UserProfile();
		targetUserProfile.parseFromFile("res/User1JGS.xml");
		// Set<Entry<String, String>> atts = userProfile.attributes.entrySet();
		// for ( Entry<String, String> entry : atts)
		// {
		// System.out.println(entry.getKey() + " : " + entry.getValue() + " : "
		// + userProfile.attributesWeights.get(entry.getKey()));
		// }


                //added for thread control JGS
                if( stopThread ) return;


		Comparator comparator = new Comparator(userProfile, ruleBase);
		LOR lor = new LOR();

                //lor.parseFromFile("res/LOtest.xml");
		lor.parseFromFile(TestXMLFilename);

                lor.getAllLoms();


                /* JGS - conduct test on real-world LOM to find # of attributes present in each lom on average

                 lom are hashmaps, with sizes, ..

                double avg = 0.0;

                for ( LO lo : lor.los )
		{
			avg += lo.lom.attributes.size();

		}
                avg = avg / lor.los.size();

                System.out.println("LOR size: " + lor.los.size() + ", with avg # of attributes: " + avg);
                */


		List<CompareResult> list = comparator.compareAll(lor);
		RandomizeResults ranres = new RandomizeResults();
		ranres.randomizeResult(list, 10, 19,49);
		int cc = 0;

		if (logging)
		{
		System.out.println("user:");
		for (CompareResult cr : list) {
			System.out.print(cc++ + " : " + cr.lo.LOName + " : " + cr.score);
			int count = 0;
			System.out.print("\t [ ");
			for (int j = 0; j < cr.ruleResults.size(); ++j) {
				System.out.print((cr.ruleResults.get(j).score > 0.0) ? 1 : 0);
				count += (cr.ruleResults.get(j).score > 0.0) ? 1 : 0;
				System.out.print(" ");
			}
			System.out.println(" ] " + count);
			// if ( cc++ >= 20 )break;
		}
		System.out.println();

		}

		Comparator targetComparator = new Comparator(targetUserProfile,
				ruleBase);
		List<CompareResult> targetList = targetComparator.compareAll(lor);

		ProfileAdapter adapt = new ProfileAdapter(userProfile);

		List<UserFeedback> feedbacks = adapt.estimateUserFeedback(list,
				targetList);

		if (logging )
		for (UserFeedback fb : feedbacks) {
			System.out.println(fb);
		}

		cc = 0;

		if (logging )
		{
		System.out.println("target:");
		for (CompareResult cr : list) {
			System.out.print(cc++ + " : " + cr.lo.LOName + " : " + cr.score);
			int count = 0;
			System.out.print("\t [ ");
			for (int j = 0; j < cr.ruleResults.size(); ++j) {
				System.out.print((cr.ruleResults.get(j).score > 0.0) ? 1 : 0);
				count += (cr.ruleResults.get(j).score > 0.0) ? 1 : 0;
				System.out.print(" ");
			}
			System.out.print(" ] " + count);
			if ( cc - 1 < feedbacks.size() )
				System.out.print("  \t"+feedbacks.get(cc - 1));
			System.out.println();
			// if ( cc++ >= 20 )break;
		}
		}


		userProfile.print();

                //trial adaptation
                adapt.adapt(list, feedbacks);

                userProfile.print();

                //print adapted profile to xml file

		System.out.println(calculateSum(feedbacks) );
		for (int i = 0; i < 50; ++i) {
                        //50 = no. of test xmls
			adapt.adapt(list, feedbacks);

			targetList = targetComparator.compareAll(lor);
			list = comparator.compareAll(lor);
			ranres.randomizeResult(list, 10, 19,49);

			feedbacks = adapt.estimateUserFeedback(list, targetList);
//			if (logging )
//			for (UserFeedback fb : feedbacks) {
//				System.out.println(fb);
//			}


			cc = 0;
			if ( logging )
			{
			System.out.println("user:");
			for (CompareResult cr : list) {
				System.out
						.print(cc++ + " : " + cr.lo.LOName + " : " + cr.score);

				int count = 0;
				System.out.print("\t [ ");
				for (int j = 0; j < cr.ruleResults.size(); ++j) {
					System.out.print((cr.ruleResults.get(j).score > 0.0) ? 1
							: 0);
					count += (cr.ruleResults.get(j).score > 0.0) ? 1 : 0;
					System.out.print(" ");
				}
				System.out.print(" ] " + count);
				if ( cc - 1 < feedbacks.size() )
					System.out.print("  \t"+feedbacks.get(cc - 1));
				System.out.println();
				// if ( cc++ >= 20 )break;
			}
			}


			if ( logging )
				userProfile.print();
                                //userProfile.writeAdapted();
			System.out.println(calculateSum(feedbacks) );

                        //added for thread control JGS
                        if( stopThread ) return;

		}

		// userProfile.print();
	}
}
