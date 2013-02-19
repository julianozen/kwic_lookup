package kwic;


/** Represents the original and matching forms of a word.  
 * You must implement 
 * {@link Object#hashCode()} correctly as well as
 * {@link Object#equals(Object)} 
 * for this to work.
 */

public class Word implements Comparable{

	public String string;

	public Word(String w){
		string = w;

	}

	/**
	 * The word used for matching is the original word run through
	 * the WordCanonical filter.
	 * @return the form of the word used for matching.
	 * 
	 */
	public String getMatchWord() {	
		if (string.isEmpty()){
			return "";
		}
		WordFilter matchWord = WordFilter.instance();
		String lowercase = matchWord.makeCanonical(string);
		return lowercase;
	}

	/**
	 * 
	 * @return the original word
	 */
	public String getOriginalWord() {
		return string;
	}

	/**
	 * 
	 * @return the hashcode value associated with the Matched Word
	 */
	public int hashCode() {
		String lowercase = getMatchWord();
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((lowercase == null) ? 0 : lowercase.hashCode());
		return result;
	}




	/**
	 * You must implement this so that two words equal each
	 * other if their matched forms equal each other.
	 * You can let eclipse generate this method automatically,
	 * but you have to modify the resulting code to get the
	 * desired effect.
	 * 
	 * This method is commented out so you can have eclipse generate
	 * a skeleton of it for you.
	 */

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass()) //If this or other == null, respond true or false respectively. If obj is not the same class as this, return false. 
			return false;
		Word other = (Word) obj; //Define Object obj as a Word
		String thisLowercase = getMatchWord();	

		if (thisLowercase == null) {
			if (other.getMatchWord() != null)
				return false;
		} else if (!thisLowercase.equals(other.getMatchWord()))
			return false;
		return true;
	}


	@Override
	public int compareTo(Object otherWord) {
		if (!(otherWord instanceof Word)){
			return -1;

		}
		Word compareWord = (Word) otherWord;
		if (compareWord.getMatchWord() == ""){
			return -1;
		}
		if (this.getMatchWord() == ""){
			return 1;
		}
		return this.getMatchWord().compareTo(compareWord.getMatchWord());
	}

	/**
	 * @return the word and its matching form, if different
	 */
	public String toString(){
		if (getOriginalWord().equals(getMatchWord()))
			return getOriginalWord();
		else
			return getOriginalWord() + " --> " + getMatchWord();
	}	
	
}
