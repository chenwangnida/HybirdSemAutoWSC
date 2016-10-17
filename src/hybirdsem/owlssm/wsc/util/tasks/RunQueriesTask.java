package hybirdsem.owlssm.wsc.util.tasks;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import hybirdsem.owlssm.wsc.data.SemServRequest;
import hybirdsem.owlssm.wsc.utils.hybirdSemTool;
import hybirdsem.owlssm.wsc.data.TestCollection;;


public class RunQueriesTask {
	private SortedSet<SemServRequest> queries = new TreeSet<SemServRequest>();

	public RunQueriesTask() {
		queries = new TreeSet<SemServRequest>(TestCollection.getInstance().getQueries().values());
	}

	public void go() {
		new ActualTask(queries);
	}

	class ActualTask {
		ActualTask(SortedSet<SemServRequest> queries) {
			int current = 0;
			SemServRequest query;
			for (Iterator<SemServRequest> iter = queries.iterator(); iter.hasNext(); current++) {
				query = iter.next();
				TestCollection.getInstance().runQuery(query);
			}
		}
	}

}
