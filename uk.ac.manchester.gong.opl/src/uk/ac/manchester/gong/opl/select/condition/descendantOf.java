package uk.ac.manchester.gong.opl.select.condition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
* Copyright (C) 2007, University of Manchester
*
* Modifications to the initial code base are copyright of their
* respective authors, or their employers as appropriate.  Authorship
* of the modifications may be determined from the ChangeLog placed at
* the end of this file.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import uk.ac.manchester.gong.opl.ReasonerFactory;
import uk.ac.manchester.gong.opl.javacc.select.OPLSelectParser;
import uk.ac.manchester.gong.opl.javacc.select.ParseException;
import uk.ac.manchester.gong.opl.select.SelectStatementResult;
import uk.ac.manchester.gong.opl.select.SelectStatementResultSet;

public class descendantOf implements MatchingCondition {

    private OWLOntologyManager manager;
    private Map ns2uri;

    public descendantOf(OWLOntologyManager manager, Map ns2uri) {
        this.manager=manager;
        this.ns2uri=ns2uri;
    }

    @Override
    public String getConditionName (){
        return "descendantOf";
    }

    /* (non-Javadoc)
      * @see uk.ac.manchester.gong.opl.select.condition.MatchingCondition#match(java.lang.String, org.semanticweb.owl.model.OWLOntology)
      */
    @Override
    public SelectStatementResultSet match(String SelectExpression, OWLOntology ontology) {

        // List to store the results
        List<SelectStatementResult> results = new ArrayList<SelectStatementResult>();

        // Parse the expression and obtain an OWLExpression to query the reasoner
        OWLClassExpression owldescription = null;
        try {
            owldescription = OPLSelectParser.parse(SelectExpression.split(getConditionName())[1], ns2uri, manager);
        }
        catch (ParseException e1) {e1.printStackTrace();}

        // Create a reasoner and query it
        OWLReasoner reasoner = ReasonerFactory.createReasoner(manager);

        Set<OWLClass> descendants = reasoner.getSubClasses(owldescription,
                false).getFlattened();
            for(OWLClass cls : descendants) {
                SelectStatementResult result = new SelectStatementResult (cls);
                results.add(result);
            }

        // Create the result set and pass it
        SelectStatementResultSet resultSet = new SelectStatementResultSet(results, SelectExpression);
        return resultSet;
    }
}
