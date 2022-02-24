import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;

public class CountTriples {

	public CountTriples()
	{
		Model events_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\events.ttl") ;
		System.out.println("events: "+countStatements(events_model));
		Model states_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\state.ttl") ;
		System.out.println("events: "+countStatements(states_model));
		Model rdfstar1_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar.ttl") ;
		System.out.println("rdfstar_v1.ttl: "+countStatements(rdfstar1_model));
		Model rdfstar2_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v2.ttl") ;
		System.out.println("rdfstar_v2.ttl: "+countStatements(rdfstar2_model));
	}
	
	private int countStatements(Model m)
	{
		System.out.println("----------------");
		int i=0;
		StmtIterator iterator = m.listStatements();
		while(iterator.hasNext())
		{
			Statement statement = iterator.next();
			System.out.println("- "+statement);
			i++;
		}
		return i;
	}
	
	public static void main(String[] args) {
		new CountTriples();
	}

}
