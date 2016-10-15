package hybirdsem.owlssm.util.tasks;

import java.util.Set;
import java.util.TreeSet;

import hybirdsem.owlssm.data.SemanticService;
import hybirdsem.owlssm.io.ErrorLog;
import hybirdsem.owlssm.util.MatchmakerInterface;

public class AddServicesToMatchmakerTask {
    private Set services = new TreeSet();

	public AddServicesToMatchmakerTask(Set services) {
		MatchmakerInterface.getInstance().createMatchmaker();
		SemanticService semServ;
		ErrorLog.debug(this.getClass().toString() + "Services to add: " + services.size());
		
	
	}
    

}
