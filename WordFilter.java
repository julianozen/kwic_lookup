package kwic;

/** A <I>singleton</I> class for filtering words into their
 * canonical form.
 */

public class WordFilter {

	/** Filter the supplied {@link String} (which is the String of
      a {@link Word} presumably) into a canonical form
      for subsequent matching.
      For this default filter, do the following:
      <UL> <LI> Remove punctuation and spaces.
           <LI> Convert to lower case.
	   </UL>
	 */

	public String makeCanonical(String temp) {
		temp = temp.toLowerCase();
		temp = temp.replaceAll("[^\\w ]", "");
		//System.out.println(temp);
		//if(temp.lastIndexOf(".") == temp.length()-1)
//			temp = temp.substring(0,temp.length()-1);
//		if(temp.length() > 2 && temp.lastIndexOf("\'") == temp.length()-2){
//			System.out.println(""+temp);
//			temp = temp.substring(0,temp.length()-2);
//		}
		return temp;
	}
	
	/*
	 * Do not allow outside calls to the constructor.
	 */
	private WordFilter() {
		
	}

	private static WordFilter staticDefaultFilter = new WordFilter();
	/** Singleton pattern: return one instance, always the same, of this
	 * class.
	 */
	
	public static WordFilter instance() {
		return staticDefaultFilter;
	}
}
