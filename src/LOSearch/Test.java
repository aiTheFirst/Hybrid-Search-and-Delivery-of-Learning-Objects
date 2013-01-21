package LOSearch;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("wordnet.database.dir", "./data/dict");
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] something = database.getSynsets("function", SynsetType.NOUN);
		
				
		for ( Synset s : something )
		{
			for ( String f : s.getWordForms())
				System.out.print(f+" , ");
//			for ( String f : ((NounSynset)s))
//				System.out.print(f+" , ");
			
			System.out.println();
		}
	
	}
}
