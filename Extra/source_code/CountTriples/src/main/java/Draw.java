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


public class Draw {
	SingleGraph graph;
	Random rand = new Random(System.currentTimeMillis());

	Map<String,Integer> property_counts = new HashMap<>();
	Set<String>  drawn_property = new HashSet<>();
	long lit_inx=100; // literals are not shared
	
	public Draw() {
		graph = new SingleGraph("1");
		graph.setAttribute("ui.stylesheet", "url('file://c:\\jo\\2022_02_LDAC2022\\style.txt')");
		graph.setStrict(false);
		graph.setAutoCreate(true);

		 //Model events_model =
		 //RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\event_C_final.ttl");
		 //traverse(events_model);

		 //Model state_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\state_C_final.ttl");
		 //traverse(state_model);
		
		 //Model rs1_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v1_C_final.ttl");		
		 //traverse(rs1_model);

		 Model rs2_model = RDFDataMgr.loadModel("c:\\jo\\2022_02_LDAC2022\\rdfstar_v2_A_initial.ttl");
	     traverse(rs2_model);

		
		
		
		System.out.println("Diameter of the graph: " + diameter(graph));
		System.out.println("Average clustering coefficient: " + averageClusteringCoefficient(graph));
		
		LinLog l = new LinLog(); // The layout algorithm
		l.setQuality(4);
		l.setStabilizationLimit(0.99);

		Viewer viewer = graph.display();
		viewer.enableAutoLayout(l);
	}
	Set<Node> nodes = new HashSet<Node>();
	private void traverse(Model m) {
		property_counts.clear();
		nodes.clear();
		drawn_property.clear();
		Graph g = m.getGraph();
		ExtendedIterator<Triple> it = g.find();
		while (it.hasNext()) {
			Triple triple = it.next();
			String sp=triple.getPredicate().getLocalName();
			int count=property_counts.getOrDefault(sp, 0);
			count++;
			property_counts.put(sp, count);
		}
		property_counts.put("hasSubject", 5);
		Node start = m.createResource("http://example.org/inst/e8859173-f8ed-4df8-9c1d-5b2a38a34064/Topic_1").asNode();
		this.graph.addNode(start.getURI()).setAttribute("ui.label", start.getLocalName());
		
		org.graphstream.graph.Node start_node = this.graph.getNode(start.getURI());
		start_node.setAttribute("layout.weight", 100);

		traverse(g, null, start); //
		System.out.println("unweightedEccentricity for topic: " + unweightedEccentricity(start_node, false));
		
		ConnectedComponents cc = new ConnectedComponents();
		cc.init(graph);

		System.out.printf("%d connected component(s) in this graph, so far.%n",
				cc.getConnectedComponentsCount());
		
		System.out.println("averageDegree: "+org.graphstream.algorithm.Toolkit.averageDegree(graph));
		System.out.println("degreeAverageDeviation: "+org.graphstream.algorithm.Toolkit.degreeAverageDeviation(graph));
		System.out.println("density: "+org.graphstream.algorithm.Toolkit.density(graph));
	}

	
	private void traverse(Graph g, Node comes, Node node) {
		if (!nodes.add(node))
			return ;
	    //org.graphstream.graph.Node n = this.graph.addNode(node.toString());
	    //n.setAttribute("layout.weight", 10);

		if (node.isURI()||node.isNodeTriple())
		{
			if(node.isURI()&&node.getLocalName().length()>0)
			{
		       //n.setAttribute("ui.label", node.getLocalName());
			}
			else
			{
				 //n.setAttribute("ui.label", node.getURI());
			}
		}
		else
		{
			 //n.setAttribute("ui.label", node.getLiteralLexicalForm());
		}
		
		
		ExtendedIterator<Triple> it = g.find();
		while (it.hasNext()) {
			Triple triple = it.next();
			Node triple_subject = triple.getSubject();
			Node triple_object = triple.getObject();
			if (triple.getPredicate().getLocalName().toLowerCase().equals("type"))
				continue;
			if (triple_subject.toString().equals(node.toString())) {
				if (triple_object != null && triple_object.isURI())
				{
					Edge e=graph.addEdge(node.toString() + "-" + triple_object.toString(), node.toString(), triple_object.toString(), true);
					if(graph.getEdge(triple_object.toString() + "-" + node.toString())==null)
						addEdgeAttribute(e, triple.getPredicate());
					System.out.println("e11: "+e.getNode0()+"\n "+e.getNode1());
					traverse(g, node, triple_object);
				}
				else
				{
					Edge e12=graph.addEdge(node.toString() + "-" + triple_object.getLiteralLexicalForm()+lit_inx, node.toString(), triple_object.getLiteralLexicalForm()+lit_inx++, false);					
					addEdgeAttribute(e12, triple.getPredicate());
					e12.setAttribute("ui.style", "size: 1px; fill-color: gray; shape: line; ");
					e12.setAttribute("weigh", "0.5");
				    System.out.println("e12: "+e12.getNode0()+"\n "+e12.getNode1());
				}
			} else if (triple_object.toString().equals(node.toString())) {
				
				Edge e=graph.addEdge(triple_subject.toString() + "-" + node.toString(), triple_subject.toString(), node.toString(),
						true);
				System.out.println("e2: "+e.getNode0()+"\n "+e.getNode1());
				if(graph.getEdge(node.toString() + "-" + triple_subject.toString())==null)
					addEdgeAttribute(e, triple.getPredicate());

				traverse(g, node, triple_subject);
			}
			else if(triple_subject.isNodeTriple())
			{
				Triple tripe_subject=triple_subject.getTriple();
				if (tripe_subject.getSubject().toString().equals(node.toString())) {
					
					org.graphstream.graph.Node re_n = this.graph.addNode((new Node_Triple(tripe_subject.getSubject(),tripe_subject.getPredicate(),tripe_subject.getObject())).toString());
					
					Edge e_reif1_subj=graph.addEdge(re_n.toString()+ "-" +node.toString()  ,re_n.toString(), node.toString(), true);  // hasSubject					
					addEdgeAttribute(e_reif1_subj, "hasSubject");
					System.out.println("e reif1_s: "+e_reif1_subj.getNode0()+"\n "+e_reif1_subj.getNode1());
					//TODO predicate and object not handled 
					Node_Triple re_nn=new Node_Triple(triple);
					if (g.find(re_nn, null, null).hasNext())
					{							
					  Edge e_reif1_reif1=graph.addEdge(re_nn.toString() + " - " + re_n.toString(), re_nn.toString(), re_n.toString(), true);  // hasSubject					  
					  addEdgeAttribute(e_reif1_reif1, "hasSubject");
						//TODO predicate and object not handled
					  System.out.println("e reif2_reif1: "+e_reif1_subj.getNode0()+"\n "+e_reif1_reif1.getNode1());
					  traverse(g, node, new Node_Triple(triple));  // reif2 -level connections
					}
					if (triple_object != null && triple_object.isURI())
					{
						Edge e_reif2object=graph.addEdge(re_n.toString() + "-" + triple_object.getURI(), re_n.toString(), triple_object.getURI(), true);												   
						   addEdgeAttribute(e_reif2object, triple.getPredicate());
						   System.out.println("e reif2o: "+e_reif2object.getNode0()+"\n "+e_reif2object.getNode1());
						traverse(g, node, triple_object);
					}
					else
					{
						Edge e_reif2lit=graph.addEdge(re_n.toString() + "-" +  triple_object.getLiteralLexicalForm()+lit_inx, re_n.toString(), triple_object.getLiteralLexicalForm()+lit_inx++, true);
						addEdgeAttribute(e_reif2lit, triple.getPredicate());
						System.out.println("e reif2lit: "+e_reif2lit.getNode0()+"\n "+e_reif2lit.getNode1());
						traverse(g, node, new Node_Triple(triple));
					}
				} else if (tripe_subject.getObject().toString().equals(node.toString())) {
					
				  // Object equals
				  //TODO add handling:  not needed urgently, as not in the dataset
				}
				
				
				
				
			}

		}
	}

	private void addEdgeAttribute(Edge e, Node predicate)
	{
	   /*String local_name=predicate.getLocalName();
	   int prob_random = rand.nextInt((this.property_counts.getOrDefault(local_name, 2)+1)/1); 
	   if(drawn_property.add(local_name))
		   e.setAttribute("ui.label", local_name);
	   else
	   {
	     if(prob_random==0)
	      e.setAttribute("ui.label", local_name);
	   }*/
	}
	private void addEdgeAttribute(Edge e, String label)
	{
	   /*int prob_random = rand.nextInt((this.property_counts.getOrDefault(label, 2)+1)/1); 
	   if(drawn_property.add(label))
		   e.setAttribute("ui.label", label);
	   else
	   {
	     if(prob_random==0)
	      e.setAttribute("ui.label", label);
	   }*/
	}
	
	public static void main(String[] args) {
		System.setProperty("org.graphstream.ui", "swing");
		new Draw();
	}

}
