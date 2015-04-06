/**
 * Copyright (C) 2008, University of Manchester
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
package org.coode.oae.utils;

import java.io.StringReader;
import java.util.Set;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.clsdescriptioneditor.AutoCompleterMatcherImpl;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.mae.parser.ArithmeticsParser;

/**
 * @author Luigi Iannone The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Apr 9, 2008
 */
public class ParserFactory {

    static ArithmeticsParser parser = null;

    public static ArithmeticsParser initParser(String formulaBody,
            OWLOntologyManager ontologyManager) {
        return initParser(formulaBody, ontologyManager.getOntologies());
        // if (parser == null) {
        // parser = new ArithmeticsParser(new StringReader(formulaBody));
        // } else {
        // ArithmeticsParser.ReInit(new StringReader(formulaBody));
        // }
        // BidirectionalShortFormProviderAdapter
        // bidirectionalShortFormProviderAdapter = new
        // BidirectionalShortFormProviderAdapter(
        // ontologyManager.getOntologies(), new SimpleShortFormProvider());
        // ShortFormEntityChecker shortFormEntityChecker = new
        // ShortFormEntityChecker(
        // bidirectionalShortFormProviderAdapter);
        // ArithmeticsParser
        // .setOWLDataFactory(ontologyManager.getOWLDataFactory());
        // ArithmeticsParser.setOWLEntityChecker(shortFormEntityChecker);
        // return parser;
    }

    public static ArithmeticsParser initParser(String formulaBody,
            Set<OWLOntology> ontologies) {
        if (parser == null) {
            parser = new ArithmeticsParser(new StringReader(formulaBody));
        } else {
            ArithmeticsParser.ReInit(new StringReader(formulaBody));
        }
        // BidirectionalShortFormProviderAdapter
        // bidirectionalShortFormProviderAdapter = new
        // BidirectionalShortFormProviderAdapter(
        // ontologies, new SimpleShortFormProvider());
        // ShortFormEntityChecker shortFormEntityChecker = new
        // ShortFormEntityChecker(
        // bidirectionalShortFormProviderAdapter);
        // ArithmeticsParser.setOWLEntityChecker(shortFormEntityChecker);
        ArithmeticsParser.setOWLEntityChecker(
                new RenderingOWLEntityCheckerNoModelManager(ontologies));
        return parser;
    }

    public static ArithmeticsParser initParser(String formulaBody,
            OWLModelManager modelManager) {
        if (parser == null) {
            parser = new ArithmeticsParser(new StringReader(formulaBody));
        } else {
            ArithmeticsParser.ReInit(new StringReader(formulaBody));
        }
        ArithmeticsParser.setOWLEntityChecker(
                new RenderingOWLEntityChecker(modelManager));
        ArithmeticsParser.setAutoCompleterMatcher(
                new AutoCompleterMatcherImpl(modelManager));
        return parser;
    }
}
