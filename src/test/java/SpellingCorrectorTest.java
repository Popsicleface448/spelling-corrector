import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.*;

public class SpellingCorrectorTest {
	SpellingCorrector spellingCorrector = new SpellingCorrector();

	@Test
	public void shouldReturnDeletions() throws Exception {
		Set<String> words = new HashSet<String>();
		words.add("he");
		words.add("te");
		words.add("th");
	
		assertEquals(words, spellingCorrector.deletions("the"));
	}

	@Test
	public void shouldReturnTranspositions() throws Exception {
		Set<String> words = new HashSet<String>();
		words.add("etst");
		words.add("tset");
		words.add("tets");

		assertEquals(words, spellingCorrector.transpositions("test"));
	}
	
	@Test
	public void shouldReturnAlterations() throws Exception {
		assertEquals(101, spellingCorrector.alterations("test").size());
	}
	
	
	@Test
	public void shouldReturnInserts() throws Exception {
		assertEquals(126, spellingCorrector.inserts("test").size());
	}
	
	@Test
	public void shouldReturnTheCorrectWord() throws Exception {
		assertEquals("access", spellingCorrector.correct("acess"));
		assertEquals("access", spellingCorrector.correct("access"));
		assertEquals("causing", spellingCorrector.correct("acesing"));
		assertEquals("heard", spellingCorrector.correct("heare"));
		assertEquals("carry", spellingCorrector.correct("carr"));
		assertEquals("forbidden", spellingCorrector.correct("forbiden"));
		assertEquals("house", spellingCorrector.correct("hoyse"));
	}
	

	

	

}
