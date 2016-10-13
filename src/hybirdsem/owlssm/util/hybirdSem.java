package hybirdsem.owlssm.util;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import de.dfki.sme2.TestCollection;
import hybirdsem.owlsm.data.SemanticService;

public class hybirdSem {
	private static String FILE_NOT_FOUND_ERROR = "Error: The specified file does not exist.";
	private static String NO_TC_ERROR = "Error: A test collection XML file must be provided for the -x option.";

	private static String X = "-x";

	public static void main(String[] args) {
		Vector<String> arguments = new Vector<String>();
		Collections.addAll(arguments, args);

		if (arguments.contains(X)) {
			Set<URI> requests = null;
			Set<URI> offers = null;
			String tcName = null;
			try {
				String tcString = arguments.get(arguments.indexOf(X) + 1);
				TestCollection tc = TestCollection.parse(tcString);
				if (tc == null) {
					System.err.println(FILE_NOT_FOUND_ERROR + " (" + tcString + ")");
					System.exit(1);
				}
				tcName = tc.getName();
				printTCInfo(tc);
				requests = new HashSet<URI>();
				for (URI request : tc.getQueries()) {
					requests.add(request);
				}
				offers = new HashSet<URI>();
				for (URI offer : tc.getServiceOffers()) {
					offers.add(offer);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.err.println(NO_TC_ERROR);
				System.exit(1);
			}

			// match
			serviceRegister(requests);

		}
	}

	private static void serviceRegister(Set<URI> requests) {
		for (URI uri : requests) {		
			SemanticService semService = new SemanticService(uri);

		}

	}

	private static void printTCInfo(TestCollection tc) {
		System.out.println("Test collection information:");
		System.out.println("Name:\t\t" + tc.getName());
		System.out.println("Type:\t\t" + tc.getType());
		System.out.println("Desc:\t\t" + tc.getDescription());
		System.out.println("SerQuery:\t\t" + tc.getQueries().size());
		System.out.println("SerRegis:\t\t" + tc.getServiceOffers().size());
		System.out.println();
	}

}
