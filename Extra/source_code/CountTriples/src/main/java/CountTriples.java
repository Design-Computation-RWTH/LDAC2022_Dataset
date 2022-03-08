import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;

public class CountTriples {

	public CountTriples()
	{
		/*Model eventsA_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\event_A_initial.ttl") ;
		System.out.println("events: "+countStatements(eventsA_model));
		Model eventsB_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\event_B_update.ttl") ;
		System.out.println("events: "+countStatements(eventsB_model));
		/*		
		Model eventsC_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\event_C_final.ttl") ;
		System.out.println("events: "+countStatements(eventsC_model));*/

		//Model eventsC_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\event_C_final_combined.ttl") ;
		//System.out.println("events combined: "+countStatements(eventsC_model));

		
		/*Model states_A_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\state_A_initial.ttl") ;
		System.out.println("events: "+countStatements(states_A_model));
		Model states_B_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\state_B_update.ttl") ;
		System.out.println("events: "+countStatements(states_B_model));
		states_B_model.remove(states_A_model); 
		System.out.println("State diff 1: "+countStatements(states_B_model));*/
		
		//Model states_C_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\state_C_final.ttl") ;
		//System.out.println("events: "+countStatements(states_C_model));
		//states_C_model.remove(states_B_model); 
		//System.out.println("State diff 2: "+countStatements(states_C_model));
		
		Model states_C_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\state_C_final_combined.ttl") ;
		System.out.println("events: "+countStatements(states_C_model));
		
		/*Model rdfstar1_A_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v1_A_initial.ttl") ;
		System.out.println("rdfstar_v1.ttl: "+countStatements(rdfstar1_A_model));
		Model rdfstar1_B_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v1_B_update.ttl") ;
		System.out.println("rdfstar_v1.ttl: "+countStatements(rdfstar1_B_model));
		rdfstar1_B_model.remove(rdfstar1_A_model); 
		System.out.println("rdfstar_v1.ttl diff: "+countStatements(rdfstar1_B_model));
		rdfstar1_B_model.listStatements().forEachRemaining(s->System.out.println(s));
		Model rdfstar1_C_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v1_C_final.ttl") ;
		System.out.println("rdfstar_v1.ttl: "+countStatements(rdfstar1_C_model));*/

		
		/*Model rdfstar2_A_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v2_A_initial.ttl") ;
		System.out.println("rdfstar_v2.ttl: "+countStatements(rdfstar2_A_model));
		Model rdfstar2_B_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v2_B_update.ttl") ;
		System.out.println("rdfstar_v2.ttl: "+countStatements(rdfstar2_B_model));
		rdfstar2_B_model.remove(rdfstar2_A_model); 
		System.out.println("rdfstar_v2.ttl diff: "+countStatements(rdfstar2_B_model));
		rdfstar2_B_model.listStatements().forEachRemaining(s->System.out.println(s));
		Model rdfstar2_C_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v2_C_final.ttl") ;
		System.out.println("rdfstar_v2.ttl: "+countStatements(rdfstar2_C_model));*/
		
		
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
