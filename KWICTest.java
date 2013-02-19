package kwic;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;




public class KWICTest {

	@Test
	public void testWord() {
		Word w = new Word("Dog");
		assertEquals("Dog", w.getOriginalWord());
		assertEquals("dog", w.getMatchWord());
		assertEquals(new Word("dog"), w);
		assertEquals(new Word("DOG"), w);
		assertEquals(new Word("DOG").hashCode(), new Word("dog").hashCode());
	}

	@Test
	public void testPhrase() {
		Phrase s = new Phrase("Owa tagu siam");
		assertEquals(3, s.getWords().size());
	}

	private KWIC genKWICFromArray() {
		KWIC kwic = new KWIC();
		for (String s : testFortunes) {
			kwic.addPhrase(s);
		}
		return kwic;
	}
	
	private KWIC genKWICFromFile() {
		KWIC kwic = new KWIC();
		kwic.addPhrases(new File("fortunes.txt"));
		return kwic;
	}

	@Test
	public void test() {
		testKWIC(genKWICFromArray(), 9);
		testKWIC(genKWICFromFile(), 68);
	}
	
	private void testKWIC(KWIC kwic, int expectYous) {
		
		//System.out.println(kwic.getPhrases(new Word("will")).size());
		
		assertEquals(1, kwic.getPhrases(new Word("Hug")).size());
		assertEquals(2, kwic.getPhrases(new Word("surrounded")).size());
		kwic.deletePhrase(new Phrase("You will soon be surrounded by good friends and laughter."));
		assertEquals(1, kwic.getPhrases(new Word("surrounded")).size());
		System.out.println(kwic.getPhrases(new Word("you")));
		assertEquals(expectYous, kwic.getPhrases(new Word("you")).size());
		
		kwic.forceAssoc(new Word("you"), new Phrase("It is not about me"));
		assertEquals(expectYous+1, kwic.getPhrases(new Word("you")).size());
		
		kwic.deleteWord(new Word("YOU"));
		assertEquals(0, kwic.getPhrases(new Word("you")).size());
	}
	
	// Uncomment if you want to see the words and phrases containing the word
	// @Test
	public void dump() {
		KWIC kwic = genKWICFromArray();
		for (Word w : kwic.getWords()) {
			System.out.println(w);
			for (Phrase p : kwic.getPhrases(w)) {
				System.out.println("   " + p);
			}
		}
	}

	private final static String[] testFortunes = {
		"You will enjoy good health, you will be surrounded by luxury.",
		"Society prepares the crime; the criminal commits it.",
		"Your talents will be recognized and suitably rewarded.",
		"Never trouble trouble till trouble troubles you.",
		"A new chapter in your life is being written.",
		"You will soon be surrounded by good friends and laughter.",
		"The first step to better times is to imagine them.",
		"An unexpected event will bring you riches.",
		"A modest man never talks of himself.",
		"You are courteous, diplomatic and affable and may find happiness in politics and public service.",
		"You have the ability to analyse and solve any problem.",
		"Give a hug to someone who needs one more than you.",
		"You will take a chance in something in the near future.",
		"The best profit of the future is the past.",
		"You are faithful in the execution of any public trust.",
		"You are heading for a land of sunshine.",
		"A close friend reveals a hidden talent.",
	};
}
