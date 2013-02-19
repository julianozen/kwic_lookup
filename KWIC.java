package kwic;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.beans.PropertyChangeSupport;
import java.io.*;
/*TA
 * graded by: BRS
 * grade: 88
 * -10 please use class and function headers, delete commented out code
 * -2 make sure to use the specified capitalization for variables (see style guide)
 */
/** 
 * Key Word in Context
 */


public class KWIC {

	final protected Map<Word,Set<Phrase>> m = new TreeMap<Word,Set<Phrase>>();
	protected PropertyChangeSupport pcs;

	public KWIC() { 
		pcs = (new PropertyChangeSupport(this)); 
	}

	/** 
	 * Required for part (b) of this lab.
	 * Accessor for the {@link PropertyChangeSuppport} 
	 */

	public PropertyChangeSupport getPCS() { return pcs; }

	/** 
	 * Convenient interface, accepts a standrd Java {@link String}
	 * @param s String to be added
	 */
	public void addPhrase(String s) {
		addPhrase(new Phrase(s));
	}

	/**
	 * Add each line in the file as a phrase.
	 * For each line in the file, call {@link addPhrase(String)} to
	 *   add the line as a phrase.
	 * @param file the file whose lines should be loaded as phrases
	 */
	public void addPhrases(File file) {
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (br.ready()) {
				addPhrase(br.readLine());
			}
			br.close();
		}
		catch (IOException e){
		}
	}
	


	/** 
	 * For each {@link Word} in the {@link Phrase}, 
	 * add the {@link Word}
	 * to the association.
	 * Use reduction to {@link #forceAssoc(Word, Phrase)}.
	 * @param p Phrase to be added
	 */
	public void addPhrase(Phrase p) {
		for (Word w : p.getWords()) {
			forceAssoc(w, p);			
		}
		pcs.firePropertyChange("Phrase Added",false, true);
	}


	/** For each word in the {@link Phrase}, delete the association between
      the word and the phrase.
      Use reduction to {@link #dropAssoc(Word, Phrase)}.
	 */
	public void deletePhrase(Phrase p) {
		for (Word w : p.getWords()) {
			dropAssoc(w,p);
		}
		pcs.firePropertyChange("Phrase Deleted",false,true);
	}

	/** Force a mapping between the speicified {@link Word} and {@link Phrase} */
	public void forceAssoc(Word w, Phrase p) {
		Word newWord = new Word(w.getMatchWord());
		Set<Phrase> newSet = new HashSet<Phrase>();
		if (m.containsKey(newWord)){ //Check if the map has the word w
			m.get(newWord).add(p); //add the phrase to the word
		}
		else{
			newSet.add(p);
			m.put(newWord, newSet); // create a new set, and put it in the word list
		}
		pcs.firePropertyChange("Phrase Added",false,true);
	}


	/** 
	 * Drop the association between the 
	 * specified {@link Word} and {@link Phrase}, if any
	 */
	public void dropAssoc(Word w, Phrase p) {
		m.get(w).remove(p);
		pcs.firePropertyChange("Phrase Deleted",false,true);
	}


	/** Return a Set that provides each {@link Phrase} associated with
    the specified {@link Word}.
	 */
	public Set<Phrase> getPhrases(Word w) {
		if (m.get(w) == null){ //check if word is in map
			Set<Phrase> newset = new HashSet<Phrase>(); //if not return empty set
			return newset;
		}
		else{
			return m.get(w);
		}
	}


	/** 
	 * Drop a word completely from the KWIC 
	 * 
	 * @param w Word to be dropped
	 */
	public void deleteWord(Word w) {
		m.remove(w);
		pcs.firePropertyChange("Word Deleted",false,true);
	}

	/** Return a Set of all words */
	public Set<Word> getWords() {
		if (m.keySet().isEmpty()){ // check if set is empty
			return new HashSet<Word>();// if it is return empty
		}
	
		return m.keySet();	
	}
}
