package org.coode.search.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.parameters.Imports;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Mar 18, 2008<br><br>
 */
public class AnnotationFinder {

    private Set<OWLAnnotationAssertionAxiom> annotAxioms;

    public Set<OWLAnnotationAssertionAxiom> getAnnotationAxioms(OWLAnnotationProperty property, String str, Set<OWLOntology> onts) {
        return getAnnotationAxioms(Collections.singleton(property), str, onts);
    }

    public Set<OWLAnnotationAssertionAxiom> getAnnotationAxioms(Set<OWLAnnotationProperty> properties, String str, Set<OWLOntology> onts) {
        annotAxioms = new HashSet<>();

        Pattern pattern = null;
        try{
            if (str != null){
                pattern = Pattern.compile(str);
            }
        }
        catch (PatternSyntaxException e){
            e.printStackTrace();
        }

        for (OWLOntology ont : onts){
            for (OWLAnnotationProperty p : properties){
                for (OWLAxiom refAx : ont.getReferencingAxioms(p,
                        Imports.EXCLUDED)) {
                    if (refAx.isOfType(AxiomType.ANNOTATION_ASSERTION)){
                        OWLAnnotationAssertionAxiom ax = (OWLAnnotationAssertionAxiom)refAx;
                        if (pattern != null){
                            if (ax.getAnnotation().getValue() instanceof OWLLiteral){
                                String strValue = ((OWLLiteral)ax.getAnnotation().getValue()).getLiteral();
                                Matcher matcher = pattern.matcher(strValue);
                                if (matcher.matches()){
                                    annotAxioms.add(ax);
                                }
                            }
                        }
                        else{
                            annotAxioms.add(ax);
                        }
                    }
                }
            }
        }
        return annotAxioms;
    }
}
