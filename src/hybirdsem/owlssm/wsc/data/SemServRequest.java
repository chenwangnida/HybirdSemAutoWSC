package hybirdsem.owlssm.wsc.data;

import java.net.URI;

public class SemServRequest extends SemanticService implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private Set answerset = new HashSet();
	
	public SemServRequest(URI uri) {
		super(uri);		
	}
	/*
	public void addServiceToAnswerset(ServiceItem item){
		answerset.add(item);
	}
	
	public Set getAnswerset() {
		return answerset;
	}
*/

}
