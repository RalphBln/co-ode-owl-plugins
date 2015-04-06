package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyChangeListener;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import changeServerPackage.ApplyChangesServlet;
import changeServerPackage.ChangeCapsule;
import changeServerPackage.ReverseChangeGenerator;

/**
 * Created by IntelliJ IDEA.
 * User: candidasa
 * Date: Jan 14, 2008
 * Time: 2:09:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestClient {

    OWLOntologyManager manager = null;
    IRI ontologyURI = null;
    OWLOntology ontology = null;
    OWLDataFactory factory = null;
    ArrayList<OWLOntologyChange> currentChanges = null;

    public TestClient() {

    }



    public void sendChangeToServer(ChangeCapsule changeCapsule) throws IOException {
        if (!changeCapsule.empty()) {

            //configure connection
            URL url = new URL ("http://"+InetAddress.getLocalHost().getHostName()+":8080/ChangeServer");// URL of CGI-Bin script.
            URLConnection urlConn = url.openConnection(); // URL connection channel.
            urlConn.setDoInput(true);  // Let the run-time system (RTS) know that we want input.
            urlConn.setDoOutput (true);// Let the RTS know that we want to do output.
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// Specify the content type.

            // Send POST output
            OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());

            String content = ApplyChangesServlet.PARAMETER_COMMAND + "=" + ApplyChangesServlet.COMMIT;
            content += "&"+ApplyChangesServlet.PARAMETER_CAPSULE+ "=" + URLEncoder.encode(changeCapsule.toJSON(), "UTF-8");

            wr.write(content);
            wr.flush();

            // Get response data.
            BufferedReader input = new BufferedReader (new InputStreamReader (urlConn.getInputStream()));
            String str;
            while (null != (str = input.readLine())) {
                System.out.println(str);
            }

            //cleanup
            wr.close();
            input.close();
        }
    }




    /** deletes changes from client, so they can be downloaded from the server again and be property integrated (in the right order) */
    private void undoChanges(List<OWLOntologyChange> existingChanges) throws OWLOntologyChangeException {
        ArrayList<OWLOntologyChange> changesToUndo = new ArrayList<OWLOntologyChange>(existingChanges.size());
        ReverseChangeGenerator gen = new ReverseChangeGenerator();
        for(OWLOntologyChange changeUndo: existingChanges) {
            changesToUndo.add(changeUndo.accept(gen));
        }

        manager.applyChanges(changesToUndo);
    }




    public void createNewTestOntology() throws OWLOntologyChangeException, OWLOntologyCreationException {
        manager = OWLManager.createOWLOntologyManager();

        ontologyURI = IRI
                .create("http://www.co-ode.org/ontologies/plugin/protege/federation/test/testont.owl");

        IRI physicalURI = IRI.create(new File("tempOnto.owl"));
        SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyURI, physicalURI);
        manager.addIRIMapper(mapper);

        ontology = manager.createOntology(ontologyURI);
        factory = manager.getOWLDataFactory();

        manager.addOntologyChangeListener(new OWLOntologyChangeListener() {
            @Override
            public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
                currentChanges = new ArrayList<OWLOntologyChange>(changes.size());  //convert all changes to XML
                for(OWLOntologyChange c: changes) {
                    currentChanges.add(c);
                }
            }
        });
    }

    public ArrayList<OWLOntologyChange> applyNewChange() throws OWLOntologyChangeException {
        OWLClass clsA = factory.getOWLClass(IRI.create(ontologyURI + "#A"));
        OWLClass clsB = factory.getOWLClass(IRI.create(ontologyURI + "#B"));
        OWLClass clsC = factory.getOWLClass(IRI.create(ontologyURI + "#C"));
        OWLClass clsD = factory.getOWLClass(IRI.create(ontologyURI + "#D"));
        OWLClass clsE = factory.getOWLClass(IRI.create(ontologyURI + "#E"));
        OWLClass clsF = factory.getOWLClass(IRI.create(ontologyURI + "#F"));
        OWLClass clsG = factory.getOWLClass(IRI.create(ontologyURI + "#G"));
        OWLClass clsH = factory.getOWLClass(IRI.create(ontologyURI + "#H"));
        OWLClass clsI = factory.getOWLClass(IRI.create(ontologyURI + "#I"));
        OWLClass clsJ = factory.getOWLClass(IRI.create(ontologyURI + "#J"));
        OWLClass clsK = factory.getOWLClass(IRI.create(ontologyURI + "#K"));
        OWLClass clsL = factory.getOWLClass(IRI.create(ontologyURI + "#L"));
        OWLClass clsM = factory.getOWLClass(IRI.create(ontologyURI + "#M"));

        OWLAxiom axiom = factory.getOWLSubClassOfAxiom(clsA, clsB);
        OWLAxiom axiom2 = factory.getOWLSubClassOfAxiom(clsA, clsC);
        OWLAxiom axiom3 = factory.getOWLSubClassOfAxiom(clsA, clsD);
        OWLAxiom axiom4 = factory.getOWLSubClassOfAxiom(clsA, clsE);
        OWLAxiom axiom5 = factory.getOWLSubClassOfAxiom(clsA, clsF);
        OWLAxiom axiom6 = factory.getOWLSubClassOfAxiom(clsA, clsG);
        OWLAxiom axiom7 = factory.getOWLSubClassOfAxiom(clsA, clsH);
        OWLAxiom axiom8 = factory.getOWLSubClassOfAxiom(clsA, clsI);
        OWLAxiom axiom9 = factory.getOWLSubClassOfAxiom(clsA, clsJ);
        OWLAxiom axiom10 = factory.getOWLSubClassOfAxiom(clsA, clsK);
        OWLAxiom axiom11 = factory.getOWLSubClassOfAxiom(clsA, clsL);
        OWLAxiom axiom12 = factory.getOWLSubClassOfAxiom(clsA, clsM);

        ArrayList<AddAxiom> changes = new ArrayList<AddAxiom>();
        changes.add(new AddAxiom(ontology, axiom));
        changes.add(new AddAxiom(ontology, axiom2));
        changes.add(new AddAxiom(ontology, axiom3));
        changes.add(new AddAxiom(ontology, axiom4));
        changes.add(new AddAxiom(ontology, axiom5));
        changes.add(new AddAxiom(ontology, axiom6));
        changes.add(new AddAxiom(ontology, axiom7));
        changes.add(new AddAxiom(ontology, axiom8));
        changes.add(new AddAxiom(ontology, axiom9));
        changes.add(new AddAxiom(ontology, axiom10));
        changes.add(new AddAxiom(ontology, axiom11));
        changes.add(new AddAxiom(ontology, axiom12));

        manager.applyChanges(changes);

        return currentChanges; //changes are recorded by the visitor
    }



    /** query server the the latest change sequence number on the server */
    public long getLatestChangeSequenceNumber() {
        return 0;
    }


    /** download all changes that aren't already integrated into the current ontology */
    public void updateOntology() {

    }


    /** queries the current ontology as to it's change sequence number */
    public long getOntologySequenceNumber(OWLOntology o) {
        return -1;
    }


    public static void main(String[] args) {
        TestClient client = new TestClient();

        try {
            //create an ontology and make some teset changes
            client.createNewTestOntology();
            List<OWLOntologyChange> changeObjects = client.applyNewChange();

            //record changes in changeCapsule object
            ChangeCapsule changeSet = new ChangeCapsule(Collections.singletonList(changeObjects)); //create new object encapsulating all that changes made to the ontology
            changeSet.setUsername(InetAddress.getLocalHost().getHostName());
            changeSet.setSequence(1);
            changeSet.setSummary("This is a test summary for a change");

            //publish changes
            client.sendChangeToServer(changeSet);
            client.undoChanges(changeObjects);//delete changes from client (they will be downloaded again in the next step)


            //query for new changes


        } catch (IOException e) {
            e.printStackTrace();
        } catch (OWLOntologyChangeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}