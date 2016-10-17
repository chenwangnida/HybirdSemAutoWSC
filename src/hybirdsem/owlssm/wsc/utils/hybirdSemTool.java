package hybirdsem.owlssm.wsc.utils;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import hybirdsem.owlssm.wsc.data.TestCollection;
import hybirdsem.owlssm.wsc.utils.tasks.AddServicesToMatchmakerTask;
import hybirdsem.owlssm.wsc.utils.tasks.RunQueriesTask;
import hybirdsem.owlssm.wsc.data.SemServRequest;
import hybirdsem.owlssm.wsc.data.SemanticService;

public class hybirdSemTool {
	private static String FILE_NOT_FOUND_ERROR = "Error: The specified file does not exist.";
	private static String NO_TC_ERROR = "Error: A test collection XML file must be provided for the -x option.";

	private static String X = "-x";

	public static void main(String[] args) {
		Vector<String> arguments = new Vector<String>();
		Collections.addAll(arguments, args);

		if (arguments.contains(X)) {
			Set<URI> requests = null;
			Set<URI> offers = null;
			try {
				String tcString = arguments.get(arguments.indexOf(X) + 1);
				de.dfki.sme2.TestCollection tc = de.dfki.sme2.TestCollection.parse(tcString);
				if (tc == null) {
					System.err.println(FILE_NOT_FOUND_ERROR + " (" + tcString + ")");
					System.exit(1);
				}
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
			
			// register all the services
			serviceRegister(offers);

			// load all the service requests
			serviceRequests(requests);

			// add Service to matchmaker
			runAllQueries(TestCollection.getInstance().getServices());

		}
	}

	private static void serviceRegister(Set<URI> offers) {
		for (URI uri : offers) {
			SemanticService semService = new SemanticService(uri);
			TestCollection.getInstance().getServices().put(uri, semService);
		}
	}

	private static void serviceRequests(Set<URI> requests) {
		for (URI uri : requests) {
			SemServRequest semServReq = new SemServRequest(uri);
			TestCollection.getInstance().getQueries().put(uri, semServReq);
			TestCollection.getInstance().getAnswerset().put(uri, new TreeSet());
			TestCollection.getInstance().getMatchmakerAnswerset().put(uri, new TreeSet());
		}
	}

	private static void runAllQueries(Map<URI, SemanticService> relev) {
		AddServicesToMatchmakerTask addServicesTask = new AddServicesToMatchmakerTask();
		RunQueriesTask runQueriesTask = new RunQueriesTask();
		addServicesTask.go();
		runQueriesTask.go();
	
	}

	private static void printTCInfo(de.dfki.sme2.TestCollection tc) {
		System.out.println("Test collection information:");
		System.out.println("Name:\t\t" + tc.getName());
		System.out.println("Type:\t\t" + tc.getType());
		System.out.println("Desc:\t\t" + tc.getDescription());
		System.out.println("SerQuery:\t\t" + tc.getQueries().size());
		System.out.println("SerRegis:\t\t" + tc.getServiceOffers().size());
		System.out.println();
	}

}
