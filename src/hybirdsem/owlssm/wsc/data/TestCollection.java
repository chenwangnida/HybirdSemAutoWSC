package hybirdsem.owlssm.wsc.data;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import hybirdsem.owlssm.wsc.data.SemServRequest;
import hybirdsem.owlssm.wsc.utils.MatchmakerInterface;
import hybirdsem.owlssm.wsc.utils.MatchMakerState;

public class TestCollection implements java.io.Serializable {

	private Map<URI, SemanticService> services = new HashMap<URI, SemanticService>();
	private Map<URI, SemServRequest> queries = new HashMap<URI, SemServRequest>();

	private Map<URI, TreeSet<?>> answerset = new HashMap<URI, TreeSet<?>>();
	private Map<URI, TreeSet<?>> matchmakerAnswerset = new HashMap<URI, TreeSet<?>>();
	private Map result = new HashMap();

	private static TestCollection instance = new TestCollection();

	private TestCollection() {
	}

	public static TestCollection getInstance() {
		return instance;
	}

	public boolean runQuery(SemServRequest SemServRequest) {
		return runQuery(SemServRequest, true);
	}

	public boolean runQuery(SemServRequest SemServRequest, boolean updateDatamodel) {
		SortedSet set = new TreeSet();
		if (MatchMakerState.getInstance().getOWLSMXP())
			MatchmakerInterface.getInstance().setOWLSMXP(true);
		else
			MatchmakerInterface.getInstance().setOWLSMXP(false);
		if (MatchMakerState.getInstance().isIntegrative())
			MatchmakerInterface.getInstance().setIntegrative(true);
		else
			MatchmakerInterface.getInstance().setIntegrative(false);
		set = MatchmakerInterface.getInstance().matchRequest(SemServRequest.getURI(),
				MatchMakerState.getInstance().getMinDegree(), MatchMakerState.getInstance().getTreshold());
		result.put(SemServRequest, set);
//		if (updateDatamodel)
//			updateData();
		return true;
	}
	
	/**
	 * returns the service of the given URI
	 * (If no services is stored with the given URI it will try to load
	 * the service from this URI and remove that one)
	 * 
	 * @param uri	URI of the service
	 * @return		ServiceItem of the service
	 */
	public SemanticService getService(URI uri) {		
		if (!services.containsKey(uri)) {
			SemanticService item = new SemanticService(uri);
			uri = item.getURI();
		}
		return (SemanticService)services.get(uri);
	}

	public Map<URI, SemanticService> getServices() {
		return services;
	}

	public void setServices(Map<URI, SemanticService> services) {
		this.services = services;
	}

	public Map<URI, SemServRequest> getQueries() {
		return queries;
	}

	public void setQueries(Map<URI, SemServRequest> queries) {
		this.queries = queries;
	}

	public Map<URI, TreeSet<?>> getAnswerset() {
		return answerset;
	}

	public void setAnswerset(Map<URI, TreeSet<?>> answerset) {
		this.answerset = answerset;
	}

	public Map<URI, TreeSet<?>> getMatchmakerAnswerset() {
		return matchmakerAnswerset;
	}

	public void setMatchmakerAnswerset(Map<URI, TreeSet<?>> matchmakerAnswerset) {
		this.matchmakerAnswerset = matchmakerAnswerset;
	}

	public Map getResult() {
		return result;
	}

	public void setResult(Map result) {
		this.result = result;
	}

}
