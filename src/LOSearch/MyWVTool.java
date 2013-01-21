package LOSearch;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.generic.charmapper.WVTCharConverter;
import edu.udo.cs.wvtool.generic.inputfilter.WVTInputFilter;
import edu.udo.cs.wvtool.generic.loader.WVTDocumentLoader;
import edu.udo.cs.wvtool.generic.output.WVTOutputFilter;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.tokenizer.WVTTokenizer;
import edu.udo.cs.wvtool.generic.vectorcreation.WVTVectorCreator;
import edu.udo.cs.wvtool.generic.wordfilter.WVTWordFilter;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.util.TokenEnumeration;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.util.WVToolLogger;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class MyWVTool extends WVTool {

	public MyWVTool(boolean arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public WVTWordList createWordList(WVTInputList input,
			WVTConfiguration config, List initialWords, boolean addWords)
			throws WVToolException {

		// Initialize the word list

		WVTWordList wordList = new WVTWordList(initialWords, input
				.getNumClasses());

		wordList.setAppendWords(addWords);

		wordList.setUpdateOnlyCurrent(false);

		// Initialize pointers to components for the individual steps

		WVTDocumentLoader loader = null;

		WVTInputFilter infilter = null;

		WVTCharConverter charConverter = null;

		WVTTokenizer tokenizer = null;
		WVTWordFilter wordFilter = null;

		WVTStemmer stemmer = null;

		// Obtain an expanded list of all documents to consider

		Iterator inList = input.getEntries();

		// Get through the list

		while (inList.hasNext()) {

			WVTDocumentInfo d = (WVTDocumentInfo) inList.next();

			try {

				// Intialize all required components for this document

				loader = (WVTDocumentLoader) config.getComponentForStep(
						WVTConfiguration.STEP_LOADER, d);

				infilter = (WVTInputFilter) config.getComponentForStep(
						WVTConfiguration.STEP_INPUT_FILTER, d);

				charConverter = (WVTCharConverter) config.getComponentForStep(
						WVTConfiguration.STEP_CHAR_MAPPER, d);

				tokenizer = (WVTTokenizer) config.getComponentForStep(
						WVTConfiguration.STEP_TOKENIZER, d);

				wordFilter = (WVTWordFilter) config.getComponentForStep(
						WVTConfiguration.STEP_WORDFILTER, d);

				stemmer = (WVTStemmer) config.getComponentForStep(
						WVTConfiguration.STEP_STEMMER, d);

				// Process the document

				// List of tokens
				TokenEnumeration tokens = stemmer.stem(wordFilter.filter(
						tokenizer.tokenize(charConverter.convertChars(infilter
								.convertToPlainText(loader.loadDocument(d), d),
								d), d), d), d);

				while (tokens.hasMoreTokens()) {

					String[] synset_tokens = getSynsetTokens(tokens.nextToken());
					for (String s : synset_tokens)
						wordList.addWordOccurance(s);

				}

				wordList.closeDocument(d);

				loader.close(d);

			} catch (WVToolException e) {

				WVToolLogger.getGlobalLogger().logException(
						"Problems processing document " + d.getSourceName(), e);

				// close the input stream for this document

				loader.close(d);

				// If errors should not be skip throw an exception

				//				if (!skipErrors) {
				//
				//					throw new WVToolException("Problems processing document "
				//							+ d.getSourceName(), e);
				//
				//				}

				// otherwise do nothing and proceed with the next document

			}

		}

		return wordList;

	}

	public void createVectors(WVTInputList input, WVTConfiguration config,
			WVTWordList wordList) throws WVToolException {

		// Set up the word list properly

		wordList.setAppendWords(false);

		wordList.setUpdateOnlyCurrent(true);

		// Initialize pointers to components for the individual steps

		WVTDocumentLoader loader = null;

		WVTInputFilter infilter = null;

		WVTCharConverter charConverter = null;

		WVTTokenizer tokenizer = null;

		WVTWordFilter wordFilter = null;

		WVTStemmer stemmer = null;

		WVTVectorCreator vectorCreator = null;

		WVTOutputFilter outputFilter = null;

		// Obtain an expanded list of all documents to consider

		Iterator inList = input.getEntries();

		// Get through the list

		while (inList.hasNext()) {

			WVTDocumentInfo d = (WVTDocumentInfo) inList.next();

			try {

				// Intialize all required components for this document

				loader = (WVTDocumentLoader) config.getComponentForStep(
						WVTConfiguration.STEP_LOADER, d);

				infilter = (WVTInputFilter) config.getComponentForStep(
						WVTConfiguration.STEP_INPUT_FILTER, d);

				charConverter = (WVTCharConverter) config.getComponentForStep(
						WVTConfiguration.STEP_CHAR_MAPPER, d);

				tokenizer = (WVTTokenizer) config.getComponentForStep(
						WVTConfiguration.STEP_TOKENIZER, d);

				wordFilter = (WVTWordFilter) config.getComponentForStep(
						WVTConfiguration.STEP_WORDFILTER, d);

				stemmer = (WVTStemmer) config.getComponentForStep(
						WVTConfiguration.STEP_STEMMER, d);

				vectorCreator = (WVTVectorCreator) config.getComponentForStep(
						WVTConfiguration.STEP_VECTOR_CREATION, d);

				outputFilter = (WVTOutputFilter) config.getComponentForStep(
						WVTConfiguration.STEP_OUTPUT, d);

				// Process the document

				TokenEnumeration tokens = stemmer.stem(wordFilter.filter(
						tokenizer.tokenize(charConverter.convertChars(infilter
								.convertToPlainText(loader.loadDocument(d), d),
								d), d), d), d);

				while (tokens.hasMoreTokens()) {

					String[] synset_tokens = getSynsetTokens(tokens.nextToken());
					for (String s : synset_tokens)
						wordList.addWordOccurance(s);

				}

				outputFilter.write(vectorCreator.createVector(wordList
						.getFrequenciesForCurrentDocument(), wordList
						.getTermCountForCurrentDocument(), wordList, d));

				wordList.closeDocument(d);

				loader.close(d);

			} catch (WVToolException e) {

				// If an error occurs add it to the error log

				WVToolLogger.getGlobalLogger().logException(
						"Problems processing document " + d.getSourceName(), e);

				// close the input stream for this document

				loader.close(d);

				// If errors should not be skip throw an exception

//				if (!skipErrors)
//
//					throw new WVToolException("Problems processing document "
//							+ d.getSourceName(), e);

				// otherwise do nothing and proceed with the next document

			}

		}

	}

	public WVTWordVector createVector(String text, WVTDocumentInfo d,
			WVTConfiguration config, WVTWordList wordList)
			throws WVToolException {

		// Set up the word list properly

		wordList.setAppendWords(false);

		wordList.setUpdateOnlyCurrent(true);

		// Initialize pointers to components for the individual steps

		WVTCharConverter charConverter = null;

		WVTTokenizer tokenizer = null;

		WVTWordFilter wordFilter = null;

		WVTStemmer stemmer = null;

		WVTVectorCreator vectorCreator = null;

		WVTWordVector result = null;

		try {

			// Intialize all required components for this document

			charConverter = (WVTCharConverter) config.getComponentForStep(
					WVTConfiguration.STEP_CHAR_MAPPER, d);

			tokenizer = (WVTTokenizer) config.getComponentForStep(
					WVTConfiguration.STEP_TOKENIZER, d);

			wordFilter = (WVTWordFilter) config.getComponentForStep(
					WVTConfiguration.STEP_WORDFILTER, d);

			stemmer = (WVTStemmer) config.getComponentForStep(
					WVTConfiguration.STEP_STEMMER, d);

			vectorCreator = (WVTVectorCreator) config.getComponentForStep(
					WVTConfiguration.STEP_VECTOR_CREATION, d);

			// Process the document

			TokenEnumeration tokens = stemmer.stem(wordFilter.filter(tokenizer
					.tokenize(charConverter.convertChars(
							new StringReader(text), d), d), d), d);

			while (tokens.hasMoreTokens()) {

				String[] synset_tokens = getSynsetTokens(tokens.nextToken());
				for (String s : synset_tokens)
					wordList.addWordOccurance(s);

			}

			result = vectorCreator.createVector(wordList
					.getFrequenciesForCurrentDocument(), wordList
					.getTermCountForCurrentDocument(), wordList, d);

			wordList.closeDocument(d);

		} catch (WVToolException e) {

			WVToolLogger.getGlobalLogger().logException(
					"Problems processing document " + d.getSourceName(), e);

			// If errors should not be skip throw an exception

			//            if (!skipErrors)
			//
			//                throw new WVToolException("Problems processing document " + d.getSourceName(), e);

			// otherwise do nothing and proceed with the next document

		}

		return result;

	}

	public String[] getSynsetTokens(String token) {
		String[] synset_tokens = null;
		token = token.toLowerCase();
		WordNetDatabase database = WordNetDatabase.getFileInstance();

		Synset[] synsets = database.getSynsets(token);

		synset_tokens = new String[synsets.length];
		for (int j = 0; j < synsets.length; ++j) {
			String[] wordForms = synsets[j].getWordForms();
			String s = wordForms[0].replace(' ', '_');
			for (int i = 1; i < wordForms.length; ++i) {
				s += ":" + wordForms[i].replace(' ', '_');
			}
			synset_tokens[j] = s;
		}

		return synset_tokens;

	}
}
