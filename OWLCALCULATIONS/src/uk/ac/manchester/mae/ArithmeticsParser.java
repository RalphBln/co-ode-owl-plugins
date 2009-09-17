/* Generated By:JJTree&JavaCC: Do not edit this line. ArithmeticsParser.java */
package uk.ac.manchester.mae;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.expression.OWLEntityChecker;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.coode.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.expression.ParserException;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLObject;
import org.protege.editor.owl.ui.clsdescriptioneditor.AutoCompleterMatcher;

public class ArithmeticsParser/*@bgen(jjtree)*/implements ArithmeticsParserTreeConstants, ArithmeticsParserConstants {/*@bgen(jjtree)*/
  protected static JJTArithmeticsParserState jjtree = new JJTArithmeticsParserState();
        static private OWLEntityChecker owlEntityChecker = null;

        static private AutoCompleterMatcher matcher;

        static private OWLDataFactory dataFactory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

        static public void setOWLDataFactory(OWLDataFactory dataFactory){
                ArithmeticsParser.dataFactory = dataFactory;
        }

        static{

                initCompletions();
        }
        static public void setOWLEntityChecker(OWLEntityChecker owlEntityChecker){
                ArithmeticsParser.owlEntityChecker = owlEntityChecker;
        }

        static public void setAutoCompleterMatcher(AutoCompleterMatcher matcher){
                ArithmeticsParser.matcher = matcher;
        }

        // Unchecked on purpose as it must contain both OWLObject instances and String
        private static List completions;

        public static List getCompletions(){
                return completions;
        }

        private static void initCompletions(){
                if(completions ==null){
                                completions = new ArrayList();
        }
                completions.clear();
                completions.add("$");
                completions.add("APPLIESTO <");
                completions.add("STORETO <");
                completions.add("{");
        }

        private static void initCompletions(boolean conflictExpressed,
                                                                 boolean appliesToExpressed,
                                                                 boolean storeToExpressed,
                                                                 boolean bindingExpressed){
                initCompletions();
                if(conflictExpressed){
                                completions.remove("$");
                }
                if(appliesToExpressed){
                        completions.remove("$");
                        completions.remove("APPLIESTO <");
                }
                if(storeToExpressed){
                        completions.remove("$");
                        completions.remove("APPLIESTO <");
                        completions.remove("STORETO <");
                }
                if(bindingExpressed){
                        completions.remove("$");
                        completions.remove("APPLIESTO <");
                        completions.remove("STORETO <");
                        completions.remove("{");
                }

        }

  public static void main(String args[]) {
    System.out.println("Reading from standard input...");
    System.out.print("Enter an expression like \u005c"1+(2+3)*var;\u005c" :");
    new ArithmeticsParser(System.in);
    try {
      SimpleNode n = ArithmeticsParser.Start();
      n.dump("");
      System.out.println("Thank you.");
    } catch (Exception e) {
      System.out.println("Oops.");
      System.out.println(e.getMessage());
    }
  }

  static final public SimpleNode Start() throws ParseException {
                      /*@bgen(jjtree) Start */
                      MAEStart jjtn000 = new MAEStart(JJTSTART);
                      boolean jjtc000 = true;
                      jjtree.openNodeScope(jjtn000);Set<String> variables = new HashSet<String>();
                                                                 boolean conflictExpressed = false;
                                                                 boolean appliesToExpressed = false;
                                                                 boolean storeToExpressed = false;
                                                                 boolean bindingExpressed = false;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 25:
        ConflictStrategy();
        conflictExpressed = true;
        break;
      default:
        jj_la1[0] = jj_gen;
        ;
      }
        initCompletions(conflictExpressed, appliesToExpressed, storeToExpressed, bindingExpressed);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 28:
        AppliesTo();
        appliesToExpressed = true;
        break;
      default:
        jj_la1[1] = jj_gen;
        ;
      }
        initCompletions(conflictExpressed, appliesToExpressed, storeToExpressed, bindingExpressed);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 26:
        StoreTo();
         storeToExpressed = true;
        break;
      default:
        jj_la1[2] = jj_gen;
        ;
      }
        initCompletions(conflictExpressed, appliesToExpressed, storeToExpressed, bindingExpressed);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OPENCURLYBRACKET:
        jj_consume_token(OPENCURLYBRACKET);
        initCompletions(conflictExpressed, appliesToExpressed, storeToExpressed, bindingExpressed);
        Binding(variables);
        bindingExpressed = true;
        completions.addAll(variables);
        label_1:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 23:
            ;
            break;
          default:
            jj_la1[3] = jj_gen;
            break label_1;
          }
          jj_consume_token(23);
          Binding(variables);
                completions.addAll(variables);
        }
        jj_consume_token(BINDINGEND);
        break;
      default:
        jj_la1[4] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INTEGER_LITERAL:
      case NUMBER:
      case IDENTIFIER:
      case 32:
        Expression(variables);
        break;
      case 35:
      case 36:
        Function(variables);
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(24);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
        {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
    if (jjtc000) {
      jjtree.clearNodeScope(jjtn000);
      jjtc000 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte000 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte000;}
    }
    if (jjte000 instanceof ParseException) {
      {if (true) throw (ParseException)jjte000;}
    }
    {if (true) throw (Error)jjte000;}
    } finally {
    if (jjtc000) {
      jjtree.closeNodeScope(jjtn000, true);
    }
    }
    throw new Error("Missing return statement in function");
  }

  static final public void ConflictStrategy() throws ParseException {
                         /*@bgen(jjtree) ConflictStrategy */
                         MAEConflictStrategy jjtn000 = new MAEConflictStrategy(JJTCONFLICTSTRATEGY);
                         boolean jjtc000 = true;
                         jjtree.openNodeScope(jjtn000);Token t;
    try {
      jj_consume_token(25);
                        completions.clear();
            completions.add("$OVERRIDING$");
            completions.add("$OVERRIDDEN$");
            completions.add("$EXCEPTION$");
      t = jj_consume_token(STRATEGY);
                jjtn000.setStrategyName(t.image);
      jj_consume_token(25);
    } finally {
           if (jjtc000) {
             jjtree.closeNodeScope(jjtn000, true);
           }
    }
  }

  static final public void StoreTo() throws ParseException {
                /*@bgen(jjtree) StoreTo */
  MAEStoreTo jjtn000 = new MAEStoreTo(JJTSTORETO);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      jj_consume_token(26);
                completions.clear();
      PropertyChain();
      jj_consume_token(27);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static final public void AppliesTo() throws ParseException {
    jj_consume_token(28);
                completions.clear();
    manSyntaxClassExpression();
    jj_consume_token(27);
  }

  static void manSyntaxClassExpression() throws ParseException {
                                         /*@bgen(jjtree) manSyntaxClassExpression */
             MAEmanSyntaxClassExpression jjtn000 = new MAEmanSyntaxClassExpression(JJTMANSYNTAXCLASSEXPRESSION);
             boolean jjtc000 = true;
             jjtree.openNodeScope(jjtn000);
             try {Token t;
                while(true){
                        t = getToken(1);
                        if (t.image.equals(">") || t.image.equals("]") || t.kind == EOF){
                                break;
                        }else{
                                jjtn000.setContent(jjtn000.getContent()+(jjtn000.getContent().equals("")?"":" ")+t.image);
                                t = getNextToken();
                        }
                }
                String content = jjtn000.getContent();
                ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(dataFactory,content);
        parser.setOWLEntityChecker(owlEntityChecker);

        try {
                parser.parseDescription();

        }catch(ParserException e){
                if(matcher!=null){

                                int lastIndexOfWhiteSpace = content.lastIndexOf(" ");
                                String word = lastIndexOfWhiteSpace == -1 ? content
                                                : content.substring(lastIndexOfWhiteSpace).trim();
                                Set<OWLObject> matches = matcher
                                                .getMatches(word, e.isClassNameExpected(), e
                                                                .isObjectPropertyNameExpected(), e
                                                                .isDataPropertyNameExpected(), e
                                                                .isIndividualNameExpected(), e
                                                                .isDatatypeNameExpected());
                                List kwMatches = new ArrayList(matches.size() + 10);
                                for (String s : e.getExpectedKeywords()) {
                                        if (word.matches("(\u005c\u005cs)*") || s.toLowerCase().startsWith(word.toLowerCase())) {
                                                kwMatches.add(s);
                                        }
                                }
                                completions.addAll(kwMatches);
                                completions.addAll(matches);
                                }
                throw new ParseException(e.getMessage());

        }
        String expression = content+"**";
        ManchesterOWLSyntaxEditorParser completerParser = new ManchesterOWLSyntaxEditorParser(dataFactory,expression);
        completerParser.setOWLEntityChecker(owlEntityChecker);
        try{

                completerParser.parseDescription();
                } catch (ParserException e) {
                                if(matcher!=null){

                                int lastIndexOfWhiteSpace = expression.lastIndexOf(" ");
                                String word = lastIndexOfWhiteSpace == -1 ? ""
                                                : content.substring(lastIndexOfWhiteSpace).trim();

                                List kwMatches = new ArrayList(10);
                                for (String s : e.getExpectedKeywords()) {
                                        if (word.matches("(\u005c\u005cs)*") || s.toLowerCase().startsWith(word.toLowerCase())) {
                                                kwMatches.add(s);
                                        }
                                }
                                completions.addAll(kwMatches);

                                }

                }/*@bgen(jjtree)*/
             } finally {
               if (jjtc000) {
                 jjtree.closeNodeScope(jjtn000, true);
               }
             }
  }

  static final public void Binding(Set<String> variables) throws ParseException {
                                     /*@bgen(jjtree) Binding */
                                     MAEBinding jjtn000 = new MAEBinding(JJTBINDING);
                                     boolean jjtc000 = true;
                                     jjtree.openNodeScope(jjtn000);Token identifier; Token propertyName;
    try {
      identifier = jj_consume_token(IDENTIFIER);
                variables.add(identifier.image);
                jjtn000.setIdentifier(identifier.image);
      jj_consume_token(29);
      PropertyChain();
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static final public void PropertyChain() throws ParseException {
                       /*@bgen(jjtree) PropertyChain */
                       MAEPropertyChain jjtn000 = new MAEPropertyChain(JJTPROPERTYCHAIN);
                       boolean jjtc000 = true;
                       jjtree.openNodeScope(jjtn000);String propertyName; Token index;
    try {
      propertyName = PropertyURI();
                jjtn000.setPropertyName(propertyName);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 30:
        jj_consume_token(30);
        PropertyFacet();
        jj_consume_token(31);
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
      }
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case EXCLAMATION:
          ;
          break;
        default:
          jj_la1[7] = jj_gen;
          break label_2;
        }
        jj_consume_token(EXCLAMATION);
                jjtn000.setEnd(false);
        PropertyChain();
      }
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static final public void PropertyFacet() throws ParseException {
                      /*@bgen(jjtree) PropertyFacet */
  MAEPropertyFacet jjtn000 = new MAEPropertyFacet(JJTPROPERTYFACET);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      manSyntaxClassExpression();
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static String PropertyURI() throws ParseException {
                String toReturn = null;
                String uriString = "";
                Token t;
                while (true) {
                        t = getToken(1);
                        if (t.kind == EXCLAMATION || t.image.startsWith("[")
                                        || t.kind == CLOSEDCURLYBRACKET || t.kind == BINDINGEND || t.image.startsWith("{") || t.image.startsWith(">") || t.kind==EOF) {
                                break;
                        }
                        uriString = (uriString + t.image).trim();
                        t = getNextToken();
                }
                if (owlEntityChecker != null) {
                        OWLDataProperty dataProperty = owlEntityChecker
                                        .getOWLDataProperty(uriString);
                        if (dataProperty != null) {
                                toReturn = dataProperty.getURI().toString();
                        }
                        if(toReturn ==null){
                                OWLObjectProperty objectProperty = owlEntityChecker
                                                .getOWLObjectProperty(uriString);
                                if (objectProperty != null) {
                                        toReturn = objectProperty.getURI().toString();
                                }
                        }
                }
                if (toReturn == null) {
                        if(matcher !=null){
                completions.addAll(matcher.getMatches(uriString, false, true, true, false, false));
            }
                        throw new ParseException(uriString + " invalid property URI or name");

                }
                if(matcher !=null){
                        completions.addAll(matcher.getMatches(uriString, false, true, true, false, false));
                }
                return toReturn;
  }

  static final public void Expression(Set<String> variables) throws ParseException {
    AdditiveExpression(variables);
  }

  static final public void AdditiveExpression(Set<String> variables) throws ParseException {
                                                        Token op =null;
    MAEAdd jjtn001 = new MAEAdd(JJTADD);
    boolean jjtc001 = true;
    jjtree.openNodeScope(jjtn001);
    try {
      MultiplicativeExpression(variables);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SUM:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_3;
        }
        op = jj_consume_token(SUM);
        jjtn001.setSum(op == null || op.image.compareTo("+")==0);
        AdditiveExpression(variables);
      }
    } catch (Throwable jjte001) {
    if (jjtc001) {
      jjtree.clearNodeScope(jjtn001);
      jjtc001 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte001 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte001;}
    }
    if (jjte001 instanceof ParseException) {
      {if (true) throw (ParseException)jjte001;}
    }
    {if (true) throw (Error)jjte001;}
    } finally {
    if (jjtc001) {
      jjtree.closeNodeScope(jjtn001, jjtree.nodeArity() > 1);
    }
    }
  }

  static final public void MultiplicativeExpression(Set<String> variables) throws ParseException {
                                                              Token op =null;
    MAEMult jjtn001 = new MAEMult(JJTMULT);
    boolean jjtc001 = true;
    jjtree.openNodeScope(jjtn001);
    try {
      UnaryExpression(variables);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case MULTIPLY:
          ;
          break;
        default:
          jj_la1[9] = jj_gen;
          break label_4;
        }
        op = jj_consume_token(MULTIPLY);
     jjtn001.setMultiplication(op == null || op.image.compareTo("*")==0);
     jjtn001.setPercentage(op != null && op.image.compareTo("%")==0);
        MultiplicativeExpression(variables);
      }
    } catch (Throwable jjte001) {
    if (jjtc001) {
      jjtree.clearNodeScope(jjtn001);
      jjtc001 = false;
    } else {
      jjtree.popNode();
    }
    if (jjte001 instanceof RuntimeException) {
      {if (true) throw (RuntimeException)jjte001;}
    }
    if (jjte001 instanceof ParseException) {
      {if (true) throw (ParseException)jjte001;}
    }
    {if (true) throw (Error)jjte001;}
    } finally {
    if (jjtc001) {
      jjtree.closeNodeScope(jjtn001, jjtree.nodeArity() > 1);
    }
    }
  }

  static final public void UnaryExpression(Set<String> variables) throws ParseException {
    if (jj_2_1(2)) {
      Power(variables);
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 32:
        jj_consume_token(32);
        Expression(variables);
        jj_consume_token(33);
        break;
      case INTEGER_LITERAL:
      case NUMBER:
      case IDENTIFIER:
        groundTerm(variables);
        break;
      default:
        jj_la1[10] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  static final public void Power(Set<String> variables) throws ParseException {
                                    /*@bgen(jjtree) Power */
                                    MAEPower jjtn000 = new MAEPower(JJTPOWER);
                                    boolean jjtc000 = true;
                                    jjtree.openNodeScope(jjtn000);Double base=null; Double exp; Token baseId=null;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INTEGER_LITERAL:
      case NUMBER:
        base = Integer();
        break;
      case IDENTIFIER:
        baseId = jj_consume_token(IDENTIFIER);
                        if(!variables.contains(baseId.image)){
                                {if (true) throw new ParseException("Unbound symbol: "+baseId.image);}
                        }
        break;
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(34);
      exp = Integer();
                  jjtree.closeNodeScope(jjtn000, true);
                  jjtc000 = false;
                        if(base!=null){
                                jjtn000.setBase(base);
                        }else{
                                jjtn000.setSymbolic(true);
                                jjtn000.setBaseIdentifier(baseId.image);
                        }
                        jjtn000.setExp(exp);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static final public double Integer() throws ParseException {
                            /*@bgen(jjtree) IntNode */
                            MAEIntNode jjtn000 = new MAEIntNode(JJTINTNODE);
                            boolean jjtc000 = true;
                            jjtree.openNodeScope(jjtn000);Token t;
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NUMBER:
        t = jj_consume_token(NUMBER);
  jjtree.closeNodeScope(jjtn000, true);
  jjtc000 = false;
        jjtn000.setValue(Double.parseDouble(t.image));
        jjtn000.setSymbolic(false);
        {if (true) return jjtn000.getValue();}
        break;
      case INTEGER_LITERAL:
        t = jj_consume_token(INTEGER_LITERAL);
    jjtree.closeNodeScope(jjtn000, true);
    jjtc000 = false;
        jjtn000.setValue(Double.parseDouble(t.image));
        jjtn000.setSymbolic(false);
        {if (true) return jjtn000.getValue();}
        break;
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } finally {
  if (jjtc000) {
    jjtree.closeNodeScope(jjtn000, true);
  }
    }
    throw new Error("Missing return statement in function");
  }

  static final public void Identifier(Set<String> variables) throws ParseException {
                                                     /*@bgen(jjtree) Identifier */
                                                     MAEIdentifier jjtn000 = new MAEIdentifier(JJTIDENTIFIER);
                                                     boolean jjtc000 = true;
                                                     jjtree.openNodeScope(jjtn000);Token t;
    try {
      t = jj_consume_token(IDENTIFIER);
          jjtree.closeNodeScope(jjtn000, true);
          jjtc000 = false;
                if(!variables.contains(t.image)){
                        {if (true) throw new ParseException("Unbound symbol: "+t.image);}
                }
                jjtn000.setIdentifierName(t.image);
                jjtn000.setSymbolic(true);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static final public void groundTerm(Set<String> variables) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
    case NUMBER:
      Integer();
      break;
    case IDENTIFIER:
      Identifier(variables);
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Function(Set<String> variables) throws ParseException {
    BigSum(variables);
  }

  static final public void BigSum(Set<String> variables) throws ParseException {
                                     /*@bgen(jjtree) BigSum */
  MAEBigSum jjtn000 = new MAEBigSum(JJTBIGSUM);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 35:
        jj_consume_token(35);
        break;
      case 36:
        jj_consume_token(36);
        break;
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      Expression(variables);
      jj_consume_token(33);
    } catch (Throwable jjte000) {
          if (jjtc000) {
            jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
          } else {
            jjtree.popNode();
          }
          if (jjte000 instanceof RuntimeException) {
            {if (true) throw (RuntimeException)jjte000;}
          }
          if (jjte000 instanceof ParseException) {
            {if (true) throw (ParseException)jjte000;}
          }
          {if (true) throw (Error)jjte000;}
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_3R_8() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_9()) {
    jj_scanpos = xsp;
    if (jj_3R_10()) return true;
    }
    return false;
  }

  static private boolean jj_3_1() {
    if (jj_3R_5()) return true;
    return false;
  }

  static private boolean jj_3R_10() {
    if (jj_scan_token(INTEGER_LITERAL)) return true;
    return false;
  }

  static private boolean jj_3R_5() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_6()) {
    jj_scanpos = xsp;
    if (jj_3R_7()) return true;
    }
    if (jj_scan_token(34)) return true;
    return false;
  }

  static private boolean jj_3R_6() {
    if (jj_3R_8()) return true;
    return false;
  }

  static private boolean jj_3R_7() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  static private boolean jj_3R_9() {
    if (jj_scan_token(NUMBER)) return true;
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public ArithmeticsParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[15];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x2000000,0x10000000,0x4000000,0x800000,0x100,0x30800,0x40000000,0x400,0x200000,0x400000,0x30800,0x30800,0x10800,0x30800,0x0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x19,0x0,0x0,0x0,0x0,0x1,0x0,0x0,0x0,0x18,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[1];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public ArithmeticsParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public ArithmeticsParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ArithmeticsParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public ArithmeticsParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ArithmeticsParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public ArithmeticsParser(ArithmeticsParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(ArithmeticsParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 15; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[37];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 15; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 37; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 1; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
