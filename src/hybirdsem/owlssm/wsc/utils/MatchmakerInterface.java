package hybirdsem.owlssm.wsc.utils;

import java.net.URI;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import hybirdsem.owlssm.data.MatchedService;
import hybirdsem.owlssm.exceptions.MatchingException;
import hybirdsem.owlssm.wsc.data.HybirdSemanticService;
import hybirdsem.owlssm.wsc.data.SemanticService;
import hybirdsem.owlssm.wsc.data.TestCollection;
import hybirdsem.owlssm.similaritymeasures.SimilarityMeasure;
import hybirdsem.owlssm.io.ErrorLog;
import hybirdsem.owlssm.SimilarityMatchingEngine;
import hybirdsem.owlssm.similaritymeasures.CosineSimilarity;
import hybirdsem.owlssm.similaritymeasures.ConstraintSimilarity;
import hybirdsem.owlssm.similaritymeasures.ExtendedJaccardMeasure;
import hybirdsem.owlssm.similaritymeasures.JensenShannonMeasure;
import hybirdsem.owlssm.wsc.utils.MatchMakerState;

/**
 * @author Chen Wang
 *
 */
public class MatchmakerInterface {	
	
	private boolean owlsmxp = false;
	private boolean integrative = false;
	
	private String getSimType(int type) {
		switch(type){		
		case SimilarityMeasure.SIMILARITY_COSINE:
			return "OWLS M3: Cosine";
		case SimilarityMeasure.SIMILARITY_EXTENDED_JACCARD:
			return "OWLS M3: exJ";
		case SimilarityMeasure.SIMILARITY_JENSEN_SHANNON:
			return "OWLS M3: Jensen Shannon";
		case SimilarityMeasure.SIMILARITY_LOI:
			return "OWLS M3: LOI";
		default:
			return "OWLS M0: Semantic";				
	}	
		
	}
	
	private SimilarityMatchingEngine matcher=null;
	private static MatchmakerInterface _instance = new MatchmakerInterface();
	private boolean ranMatchmaker = false;
	
	public static MatchmakerInterface getInstance() {
		return _instance;
	}
	
	public void createMatchmaker() {
		short type = (short)MatchMakerState.getInstance().getSimilarityMeasure();
		ErrorLog.debug("Similarity measure: " + getSimType(type));
		switch(type){		
			case SimilarityMeasure.SIMILARITY_COSINE:
				matcher = new SimilarityMatchingEngine(new CosineSimilarity());
				break;
			case SimilarityMeasure.SIMILARITY_EXTENDED_JACCARD:
				matcher = new SimilarityMatchingEngine(new ExtendedJaccardMeasure());
				break;
			case SimilarityMeasure.SIMILARITY_JENSEN_SHANNON:
				matcher = new SimilarityMatchingEngine(new JensenShannonMeasure());
				break;
			case SimilarityMeasure.SIMILARITY_LOI:
				matcher = new SimilarityMatchingEngine(new ConstraintSimilarity());
				break;
			default:
				matcher = new SimilarityMatchingEngine(null);				
		}		
		matcher.clear();
	}
	
	public SortedSet matchRequest(URI profileURI, int minimumDegreeOfMatch, double treshold) {
		try {				
			ErrorLog.debug("Matching request: " + profileURI);
			ErrorLog.debug("Minimum DOM: " + minimumDegreeOfMatch);
			ErrorLog.debug("Similarity treshold: " + treshold);
			matcher.setIntegrative(integrative);
			ranMatchmaker = true;
			//matcher.setSimilarityMeasure(MatchMakerState.getInstance().getSimilarityMeasure());
			SortedSet tmpResult = matcher.matchRequest(profileURI, MatchMakerState.getInstance().getMinDegree(), MatchMakerState.getInstance().getTreshold());
			SortedSet result;
			if(owlsmxp) {
				try {
					org.mindswap.owl.OWLKnowledgeBase kb = org.mindswap.owl.OWLFactory.createKB();
					org.mindswap.owl.OWLOntology request = kb.read(profileURI);		
					SortedSet filteredResult = matcher.useOWLSMXPFilter(tmpResult, request);
					for(Iterator iter = filteredResult.iterator(); iter.hasNext();) {
						MatchedService service = (MatchedService) iter.next();
					}
					result = MatchmakerToGUISet(tmpResult, filteredResult);
				}
				catch(Exception e) {
					e.printStackTrace();
					matcher = null;
					return new TreeSet();
				}
			}
			else {
				result = MatchmakerToGUISet(tmpResult);
			}
			ErrorLog.debug("Resultat:\n   " + tmpResult);	
			return result;
		} catch (MatchingException e) {
			e.printStackTrace();
			matcher = null;
			return new TreeSet();
		}
	}
	
	public void addService(URI profileURI) {
		try {
		if (matcher==null) {
			ErrorLog.debug(this.getClass().toString()+": Reset machmaker");
			createMatchmaker();
		}

		ErrorLog.debug(this.getClass().toString()+": Adding service: " + profileURI.toString());
		matcher.addService(profileURI);
		}
		catch(Exception e) {
			MatchMakerState.displayWarning("Matchmaker", "Couldn't add service " + profileURI + " either file not found or not an valid OWL-S 1.1 file."); 
		}
		
	}
	
	private SortedSet MatchmakerToGUISet(SortedSet result) {		
		SortedSet hybrid = new TreeSet();
		if (result == null) {
			MatchMakerState.displayWarning("Matchmaker", "Result set is empty");
//			ErrorLog.report("Matchmaker didn't return any result");
//			owlsmx.io.ErrorLog.debug("Matchmaker didn't return any result");
			return hybrid;
		}
//		owlsmx.io.ErrorLog.debug("Matchmaker result: " +  result);
		MatchedService m_result;
		HybirdSemanticService h_result;
		SemanticService s_item;
		for(Iterator iter = result.iterator();iter.hasNext();){
			m_result = (MatchedService) iter.next();
			s_item = TestCollection.getInstance().getService(m_result.serviceURI);
			h_result = new HybirdSemanticService(s_item);
			h_result.setDegreeOfMatch(m_result.degreeOfMatch);
			h_result.setSyntacticSimilarity(m_result.similarity);
//			owlsmx.io.ErrorLog.debug(this.getClass().toString() + "|M2GUI: MResult " + m_result.toString());
			hybrid.add(h_result);
		}
		
		return hybrid;
	}
	
	private SortedSet MatchmakerToGUISet(SortedSet results, SortedSet compatibleResults) {
		SortedSet hybrid = new TreeSet();
		if(results == null) {
			MatchMakerState.displayWarning("Matchmaker", "Result set is empty");
			return hybrid;
		}
		MatchedService m_result;
		HybirdSemanticService h_result;
		SemanticService s_item;
		for(Iterator iter = results.iterator(); iter.hasNext(); ) {
			m_result = (MatchedService) iter.next();
			s_item = TestCollection.getInstance().getService(m_result.serviceURI);
			h_result = new HybirdSemanticService(s_item);
			h_result.setDegreeOfMatch(m_result.degreeOfMatch);
			h_result.setSyntacticSimilarity(m_result.similarity);
			h_result.setDataTypeCompatible(compatibleResults.contains(m_result));
			hybrid.add(h_result);
		}
		
		return hybrid;
	}
	
	public void clear() {
		matcher.clear();
		matcher = null;
		ErrorLog.debug(this.getClass().toString()+": Reset machmaker");
	}
	
	public boolean didRun() {
		return ranMatchmaker;
	}
	
	public void setOWLSMXP(boolean value) {
		owlsmxp = value;
	}
	
	public void setIntegrative(boolean value) {
		integrative = value;
	}
}
