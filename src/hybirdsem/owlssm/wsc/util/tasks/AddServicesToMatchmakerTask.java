package hybirdsem.owlssm.wsc.util.tasks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import hybirdsem.owlssm.wsc.data.SemanticService;
import hybirdsem.owlssm.io.ErrorLog;
import hybirdsem.owlssm.wsc.utils.MatchmakerInterface;
import hybirdsem.owlssm.wsc.data.TestCollection;;

public class AddServicesToMatchmakerTask {
	private Set<SemanticService> services = new TreeSet<SemanticService>();

	public AddServicesToMatchmakerTask() {
		services = new HashSet<SemanticService>(TestCollection.getInstance().getServices().values());
		// lengthOfTask = services.size();

	}

	public void go() {
		new ActualTask(services);

	}

	class ActualTask {
		ActualTask(Set<SemanticService> services) {
			MatchmakerInterface.getInstance().createMatchmaker();
			SemanticService service;
			ErrorLog.debug(this.getClass().toString() + "Services to add: " + services.size());

			int current = 0;
			for (Iterator<SemanticService> iter = services.iterator(); iter.hasNext(); current++) {
				service = iter.next();
				ErrorLog.debug(this.getClass().toString() + ": Adding service: " + service.getURI().toString());
				MatchmakerInterface.getInstance().addService(service.getURI());
			}

		}

	}

}
