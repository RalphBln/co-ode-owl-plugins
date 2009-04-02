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
package org.coode.patterns.syntax;

/**
 * @author Luigi Iannone
 * 
 */
public class Utils {
	public static String readString(PatternParser patternParser,
			int... delimiterTokenKinds) {
		String toReturn = "";
		while (true) {
			Token token = patternParser.getToken(1);
			boolean found = false;
			for (int i = 0; !found && i < delimiterTokenKinds.length; i++) {
				found = token.kind == PatternParser.EOF
						|| delimiterTokenKinds[i] == token.kind;
			}
			if (found) {
				break;
			} else {
				toReturn += token.image;
				patternParser.getNextToken();
			}
		}
		return toReturn;
	}
}
