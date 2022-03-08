import static org.graphstream.algorithm.Toolkit.averageClusteringCoefficient;
import static org.graphstream.algorithm.Toolkit.diameter;
import static org.graphstream.algorithm.Toolkit.unweightedEccentricity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.jena.graph.Graph;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Node_Triple;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.view.Viewer;

public class DirectDraw {
	SingleGraph graph;
	Random rand = new Random(System.currentTimeMillis());

	Set<String> drawn_property = new HashSet<>();
	long lit_inx = 100; // literals are not shared

	public DirectDraw() {
		graph = new SingleGraph("1");
		graph.setAttribute("ui.stylesheet", "url('file://c:\\jo\\2022_02_LDAC2022\\style.txt')");
		graph.setStrict(false);
		graph.setAutoCreate(true);

		 //Model events_model =
		 //RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\event_C_final.ttl");
		 //traverse(events_model);

		 //Model state_model =
		 //RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\state_C_final.ttl");
		 //traverse(state_model);

		 //Model rs1_model =
		 //RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v1_C_final.ttl");
		 //traverse(rs1_model);

		 Model rs2_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v2_C_final.ttl");
		 traverse(rs2_model);

		LinLog l = new LinLog(); // The layout algorithm
		l.setQuality(4);
		l.setStabilizationLimit(0.99);

		Viewer viewer = graph.display();
		viewer.enableAutoLayout(l);
	}

	Set<Node> nodes = new HashSet<Node>();

	private void traverse(Model m) {
		nodes.clear();
		drawn_property.clear();
		Graph g = m.getGraph();

		ExtendedIterator<Triple> it = g.find();
		while (it.hasNext()) {
			Triple triple = it.next();
			Node triple_subject = triple.getSubject();
			Node triple_object = triple.getObject();

			if (triple.getPredicate().getLocalName().toLowerCase().equals("type"))
				continue;

			if (triple_object != null && triple_object.isURI()) {
				if(g.find(triple_object, null, null).hasNext())
				    graph.addEdge(triple_subject.toString() + "-" + triple_object.toString(), triple_subject.toString(),
						triple_object.toString(), true);
				else
					graph.addEdge(triple_subject.toString() + "-" + triple_object.toString()+ lit_inx, triple_subject.toString(),
							triple_object.toString()+ lit_inx++, true);
			} else {
				graph.addEdge(triple_subject.toString() + "-" + triple_object.getLiteralLexicalForm() + lit_inx,
						triple_subject.toString(), triple_object.getLiteralLexicalForm() + lit_inx++, true);
			}

		}

		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);

		System.out.printf("%d connected component(s) in this graph, so far.%n", cc.getConnectedComponentsCount());

	}

	public static void main(String[] args) {
		System.setProperty("org.graphstream.ui", "swing");
		new DirectDraw();
	}

}
