package kwic;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Represent a phrase.
 *
 */
public class Phrase implements Comparable{

	final protected String phrase;

	public Phrase(String s){
		phrase = s;
	}

	/** 
	 * Provide the words of a phrase.  
	 * Each word may have to be cleaned up:  
	 * punctuation removed, put into lower case
	 */

	public Set<Word> getWords() {
		Set<Word> words = new HashSet<Word>();
		StringTokenizer st = new StringTokenizer(phrase);
		while (st.hasMoreTokens()) {
			Word next = new Word(cleanUp(st.nextToken()));
			words.add(next);
		}

		return words;
	}

	/** 
	 Compares whether two Phrase objects are equal by comparing their strings
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phrase other = (Phrase) obj;
		if (phrase == null) {
			if (other.phrase != null)
				return false;
		} else if (!phrase.equals(other.phrase))
			return false;
		return true;
	}
	/** 
	 Returns the hashcode associated with a given phrase.
	 */

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((phrase == null) ? 0 : phrase.hashCode());
		return result;
	}



	/** Filter the supplied {@link String} (which is the String of
      a {@link Phrase} presumably) into a canonical form
      for subsequent matching.
      The actual filtering depends on what you consider to be
      insignificant in terms of matching.  
      <UL> <LI> If punctuation is
      irrelevant, remove punctuation.
           <LI> If case does not matter, than convert to lower (or upper)
	        case.
      </UL>
	 */
	protected static String cleanUp(String s){
		String newString = s.replace("[^\\w]",""); //Regular expression to remove extra characters
		return newString.toLowerCase();
	}
	/** 
	 Used to sort phrases.
	 */	
	@Override
	public int compareTo(Object obj) {
		if (!(obj instanceof Phrase)){
			return 0;
		}
		Phrase comparePhrase = (Phrase) obj;
		return phrase.compareTo(comparePhrase.phrase);
	}
	/** 
	 Returns the Phrase as a string.
	 */
	public String toString(){
		return phrase;
	}
}
