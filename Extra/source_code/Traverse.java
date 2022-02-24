import java.util.HashSet;
import java.util.Set;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.pfunction.library.concat;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDF.Nodes;

public class Traverse {

	public Traverse() {

		Model events_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\events.ttl");
		System.out.println("events: " + traverse(events_model));

		Model states_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\state.ttl");
		System.out.println("states: " + traverse(states_model));

		Model rdfstar1_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar.ttl");
		System.out.println("rdfstar: " + traverse(rdfstar1_model));

		
		Model rdfstar2_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v2.ttl");
		System.out.println("rdfstar2: " + traverse(rdfstar2_model)); 

	}

	Set<Node> nodes = new HashSet<Node>();

	private int traverse(Model m) {
		nodes.clear();
		Graph g = m.getGraph();
		Node start = m.createResource("http://example.org/inst/e8859173-f8ed-4df8-9c1d-5b2a38a34064/Topic_1").asNode();
		return traverse(g, null, start, 0) ; // 
	}

	private int traverse(Graph g, Node comes, Node node, int l) {
		if (!nodes.add(node)) {
			// System.out.println("Already: "+comes+"->"+ node+" "+l);
			return l;
		}

		String comes_local="";
		if (comes != null && comes.isURI())
			comes_local = comes.getLocalName();
		String node_local = node.toString();
		if (node != null && node.isURI())
			node_local = node.getLocalName();

		//System.out.println(comes_local + "->" + node_local + " " + l);

		if (!g.find(node, null, null).hasNext())
			return l; // just object triples that have no child

		if (node.isLiteral())
			return l;
		ExtendedIterator<Triple> it = g.find();
		int ret = l;
		while (it.hasNext()) {
			Triple triple = it.next();
			Node triple_subject = triple.getSubject();
			Node triple_object = triple.getObject();
			if (triple_subject.isNodeTriple()) {
				Node triple_subject_subject = triple_subject.getTriple().getSubject();
				Node triple_subject_object = triple_subject.getTriple().getObject();
				if (triple_subject_subject.toString().equals(node.toString())) {
					int ret1 = traverse(g, node, triple_object, l + 2); // reification of RDF* is one extra step
					int ret2 = traverse(g, node, triple_subject, l + 2);
					if (ret1 > ret)
						ret = ret1;
					if (ret2 > ret)
						ret = ret2;
				} else if (triple_subject_object.toString().equals(node.toString())) {
					int ret1 = traverse(g, node, triple_object, l + 2);
					int ret2 = traverse(g, node, triple_subject, l + 2);
					if (ret1 > ret)
						ret = ret1;
					if (ret2 > ret)
						ret = ret2;
				}
			} else if (triple_object.isNodeTriple()) {
				Node triple_object_subject = triple_object.getTriple().getSubject();
				Node triple_object_object = triple_object.getTriple().getObject();
				if (triple_object_subject.toString().equals(node.toString())) {
					int ret1 = traverse(g, node, triple_object, l + 2);
					int ret2 = traverse(g, node, triple_subject, l + 2);
					if (ret1 > ret)
						ret = ret1;
					if (ret2 > ret)
						ret = ret2;
				} else if (triple_object_object.toString().equals(node.toString())) {
					int ret1 = traverse(g, node, triple_object, l + 2);
					int ret2 = traverse(g, node, triple_subject, l + 2);
					if (ret1 > ret)
						ret = ret1;
					if (ret2 > ret)
						ret = ret2;
				}
			} else if (triple.getSubject().toString().equals(node.toString())) {
				int ret1 = traverse(g, node, triple_object, l + 1);
				if (ret1 > ret)
					ret = ret1;
			} else if (triple.getObject().toString().equals(node.toString())) {
				int ret2 = traverse(g, node, triple_subject, l + 1);
				if (ret2 > ret)
					ret = ret2;
			}

		}
		//System.out.println("return "+ret);
		return ret;
	}

	public static void main(String[] args) {
		new Traverse();
	}

}
