/**
 *
 */
package uk.ac.manchester.cs.owl.lint.commons;

import org.semanticweb.owlapi.lint.ActingLint;
import org.semanticweb.owlapi.lint.Lint;
import org.semanticweb.owlapi.lint.LintVisitor;
import org.semanticweb.owlapi.lint.PatternBasedLint;

/**
 * @author Luigi Iannone
 */
public abstract class DefaultLintVisitorAdapter implements LintVisitor {

    protected abstract void doDefault(Lint<?> lint);

    /**
     * @see org.semanticweb.owlapi.lint.LintVisitor#visitPatternPasedLint(org.semanticweb.owlapi.lint.PatternBasedLint)
     */
    @Override
    public void visitPatternPasedLint(PatternBasedLint<?> lint) {
        doDefault(lint);
    }

    /**
     * @see org.semanticweb.owlapi.lint.LintVisitor#visitActingLint(org.semanticweb.owlapi.lint.ActingLint)
     */
    @Override
    public void visitActingLint(ActingLint<?> actingLint) {
        doDefault(actingLint);
    }

    /**
     * @see org.semanticweb.owlapi.lint.LintVisitor#visitGenericLint(org.semanticweb.owlapi.lint.Lint)
     */
    @Override
    public void visitGenericLint(Lint<?> lint) {
        doDefault(lint);
    }
}
