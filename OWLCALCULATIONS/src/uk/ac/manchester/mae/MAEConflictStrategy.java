/* Generated By:JJTree: Do not edit this line. MAEConflictStrategy.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=MAE,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package uk.ac.manchester.mae;

public
class MAEConflictStrategy extends SimpleNode {
  public MAEConflictStrategy(int id) {
    super(id);
  }

  public MAEConflictStrategy(ArithmeticsParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ArithmeticsParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e3d84fc4833ee211542f6f1d4b3e015c (do not edit this line) */
