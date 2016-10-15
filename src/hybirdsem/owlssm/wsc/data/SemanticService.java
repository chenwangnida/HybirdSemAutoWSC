package hybirdsem.owlssm.wsc.data;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import hybirdsem.owlssm.wsc.util.ServiceLoader;
import hybirdsem.owlssm.wsc.util.ServiceLoader.ServiceContent;

public class SemanticService implements java.io.Serializable, Comparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4835205656099953572L;
	protected URI uri;
	protected URI fileURI;
	protected boolean processed = false;
	protected Set inputs = new HashSet();
	protected Set outputs = new HashSet();
	protected String name;
	protected boolean hasWSDLGrounding;

	public SemanticService() {

	}

	public SemanticService(URI uri) {
		this.uri = uri;
		this.name = uri.toString();
		this.fileURI = uri;
		ServiceContent content = ServiceLoader.getInstance().loadServiceURI(uri);
		this.uri = content.uri;
		this.name = content.name;
		this.fileURI = content.fileURI;
		this.inputs = content.inputs;
		this.outputs = content.outputs;
		this.hasWSDLGrounding = content.hasWSDLGrounding;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean pro) {
		processed = pro;
	}

	public URI getURI() {
		return fileURI;
	}

	public String toString() {
		return getName();
	}

	public String getName() {
		String name = fileURI.toString();
		return name.substring(name.lastIndexOf("/") + 1);
		// return name;
	}

	public int compareTo(Object obj) {
		if (!this.getClass().equals(obj.getClass()))
			return -1;
		return this.getName().compareTo(((SemanticService) obj).getName());
	}

	public Set getOutputs() {
		return outputs;
	}

	public Set getInputs() {
		return inputs;
	}

	public boolean hasWSDLGrounding() {
		return hasWSDLGrounding;
	}
}
