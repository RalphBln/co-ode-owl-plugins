/**
 * 
 */
package org.coode.oppl.lint;

/**
 * @author Luigi Iannone
 * 
 */
public interface AbstractOPPLLintParser {
	/**
	 * Parses a valid OPPL Lint string into an OPPL Lint Script
	 * 
	 * @param input
	 *            The OPPL string. Cannot be {@code null}.
	 * @return The resulting OPPLLintScript if the input String is valid.
	 *         {@code null} otherwise.
	 * @throws NullPointerException
	 *             if the input is {@code null}.
	 */
	OPPLLintScript parse(String input);
}
