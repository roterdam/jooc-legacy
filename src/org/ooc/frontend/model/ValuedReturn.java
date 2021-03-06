package org.ooc.frontend.model;

import java.io.IOException;

import org.ooc.frontend.Visitor;
import org.ooc.frontend.model.interfaces.MustBeResolved;
import org.ooc.frontend.model.tokens.Token;
import org.ooc.middle.OocCompilationError;
import org.ooc.middle.hobgoblins.Resolver;

public class ValuedReturn extends Return implements MustBeResolved {

	protected Expression expression;

	public ValuedReturn(Expression expression, Token startToken) {
		super(startToken);
		this.expression = expression;
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	@Override
	public void accept(Visitor visitor) throws IOException {
		visitor.visit(this);
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}
	
	@Override
	public void acceptChildren(Visitor visitor) throws IOException {
		expression.accept(visitor);
	}
	
	@Override
	public boolean replace(Node oldie, Node kiddo) {
		
		if(super.replace(oldie, kiddo)) return true;
		
		if(oldie == expression) {
			expression = (Expression) kiddo;
			return true;
		}
		
		return super.replace(oldie, kiddo);
		
	}

	public boolean isResolved() {
		return false;
	}

	public Response resolve(NodeList<Node> stack, Resolver res, boolean fatal) {
		
		int funcIndex = stack.find(FunctionDecl.class);
		if(funcIndex == -1) {
			throw new OocCompilationError(this, stack, "'return' outside a function: wtf?");
		}
		
		if(expression.getType() == null || !expression.getType().isResolved()) {
            // expr type is unresolved
        	if(res.params.veryVerbose) {
        		System.out.println("Expr type ("+expression.getType()+") of "+expression+" is unresolved, looping");
        	}
            return Response.LOOP;
        }
		
		FunctionDecl fDecl = (FunctionDecl) stack.get(funcIndex);
		if(fDecl.getReturnType().isGeneric()) {
			VariableAccess returnAcc = new VariableAccess(fDecl.getReturnArg(), startToken);
            
            If if1 = new If(returnAcc, startToken);
            
            VariableDecl vdfe = new VariableDecl(null, generateTempName("returnVal", stack), expression, startToken, stack.getModule());
            
            Assignment ass = new Assignment(returnAcc, new VariableAccess(vdfe, startToken), startToken);
            if1.getBody().add(new Line(ass));
            
            stack.peek().addBeforeLine(stack, vdfe);
            stack.peek().addBeforeLine(stack, if1);
            stack.peek().replace(this, new Return(startToken));
            
            // just got replaced
            return Response.LOOP;
        }

		if(fDecl.getReturnType().isSuperOf(expression.getType()) || (expression.getType().isGeneric())) {
			expression = new Cast(expression, fDecl.getReturnType(), expression.startToken);
		}
		
		return Response.OK;
		
	}
	
	@Override
	public String toString() {
		return "return "+expression;
	}
	
}
