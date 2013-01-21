package LOSearch;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.udo.cs.wvtool.generic.stemmer.AbstractStemmer;
import edu.udo.cs.wvtool.util.WVToolException;


public class SynsetStemmer extends AbstractStemmer{


	public String getBase(String term) throws WVToolException {
		String base = "---";
//		term = term.toLowerCase();
//		WordNetDatabase database = WordNetDatabase.getFileInstance();
//		
//		Synset[] synsets = database.getSynsets(term);
//		
//		if (synsets.length > 0)
//		{
//			String[] wordForms = synsets[0].getWordForms();
//			base = wordForms[0].replace(' ', '_');
//			for ( int i = 1 ; i < wordForms.length ; ++i )
//			{
//				base += ":"+wordForms[i].replace(' ', '_');
//			}
//		}
//		
//		return base;
		
		
		
		term = term.toLowerCase();
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		String[] bases = database.getBaseFormCandidates(term, SynsetType.NOUN);

		if ( bases.length == 0 )
			bases = database.getBaseFormCandidates(term, SynsetType.VERB);
		
		if ( bases.length == 0 )
			bases = database.getBaseFormCandidates(term, SynsetType.ADJECTIVE);

		if ( bases.length == 0 )
			bases = database.getBaseFormCandidates(term, SynsetType.ADVERB);
		
		if ( bases.length == 0 )
			bases = database.getBaseFormCandidates(term, SynsetType.ADJECTIVE_SATELLITE);

		if ( bases.length > 0)
		{
			base = bases[0];
//			for ( int i = 1 ; i < bases.length ; ++i )
//				base += ":"+bases[i];
//			return base+"_"+term;
			return base;
		}
		else
			return term;
	}

}
