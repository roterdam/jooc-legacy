package org.ooc.frontend.parser;

import java.io.IOException;

import org.ooc.frontend.model.CoverDecl;
import org.ooc.frontend.model.FunctionDecl;
import org.ooc.frontend.model.Import;
import org.ooc.frontend.model.Module;
import org.ooc.frontend.model.OocDocComment;
import org.ooc.frontend.model.Type;
import org.ooc.frontend.model.TypeDecl;
import org.ooc.frontend.model.VariableDecl;
import org.ooc.frontend.model.tokens.Token;
import org.ooc.frontend.model.tokens.TokenReader;
import org.ooc.frontend.model.tokens.Token.TokenType;
import org.ubi.CompilationFailedError;
import org.ubi.SourceReader;

public class CoverDeclParser {

	public static CoverDecl parse(Module module, SourceReader sReader, TokenReader reader) throws IOException {
		int mark = reader.mark();
		
		OocDocComment comment = null;
		Token commentToken = reader.peek();
		if(commentToken.type == TokenType.OOCDOC) {
			comment = new OocDocComment(reader.read().get(sReader), commentToken);
		}
		
		Token startToken = reader.peek();
		String name = "";
		Type tName = TypeParser.parse(module, sReader, reader);
		if(tName != null) {
			name = tName.toString();
			if(reader.read().type != TokenType.COLON) {
				reader.reset(mark);
				return null;
			}
		}
		
		String externName = ExternParser.parse(sReader, reader);
		
		if(reader.read().type == TokenType.COVER_KW) {

			Type overType = null;
			String superName = "";
			while(true) {
				Token token = reader.peek();
				if(token.type == TokenType.FROM_KW) {
					reader.skip();
					overType = TypeParser.parse(module, sReader, reader);
					if(overType == null) {
						throw new CompilationFailedError(sReader.getLocation(reader.peek()),
						"Expected cover's base type name after the from keyword.");
					}
					continue;
				} else if(token.type == TokenType.EXTENDS_KW) {
					reader.skip();
					superName = reader.read().get(sReader);
					continue;
				}
				break;
			}
			
			CoverDecl coverDecl = new CoverDecl(name, superName, overType, startToken);
			module.parseStack.push(coverDecl);
			coverDecl.setExternName(externName);
			if(comment != null) coverDecl.setComment(comment);
			
			boolean absorbed = false;
			for(Import imp: module.getImports()) {
				Module depMod = imp.getModule();
				if(depMod != null) {
					TypeDecl base = depMod.getType(name);
					if(base != null) {
						coverDecl.absorb((CoverDecl) base);
						absorbed = true;
						break;
					}
				}
			}
			if(!absorbed) {
				outer: for(Module other: ModuleParser.cache.values()) {
					for(Import imp: other.getImports()) {
						if(imp.getName().equals(module.getFullName())) {
							TypeDecl addon = other.getType(name);
							if(addon != null) {
								((CoverDecl) addon).absorb(coverDecl);
								break outer;
							}
						}
					}
				}
			}
			
			Token t2 = reader.read();
			if(t2.type != TokenType.OPEN_BRACK) {
				if(t2.type == TokenType.LINESEP) {
					module.parseStack.pop(coverDecl);
					return coverDecl; // empty cover, acts like a typedef
				}
				throw new CompilationFailedError(sReader.getLocation(t2),
						"Expected opening bracket to begin cover declaration, got "+t2);
			}
			
			while(reader.hasNext() && reader.peek().type != TokenType.CLOS_BRACK) {

				if(reader.peek().type == TokenType.LINESEP) {
					reader.skip(); continue;
				}
				
				VariableDecl varDecl = VariableDeclParser.parse(module, sReader, reader);
				if(varDecl != null) {
					if(reader.read().type != TokenType.LINESEP) {
						throw new CompilationFailedError(sReader.getLocation(reader.prev()),
							"Expected newline after variable declaration in cover declaration, but got "+reader.prev());
					}
					if(overType != null && !varDecl.isExtern()) {
						throw new CompilationFailedError(sReader.getLocation(reader.prev()),
							"You can't add non-extern member variables to a Cover which already has a base type (in this case, "
								+overType.getName()+")");
					}
					coverDecl.addVariable(varDecl);
					continue;
				}
				
				FunctionDecl funcDecl = FunctionDeclParser.parse(module, sReader, reader, false);
				if(funcDecl != null) {
					coverDecl.addFunction(funcDecl);
					continue;
				}
				
				if(reader.peek().type == TokenType.OOCDOC) {
					reader.skip();
					continue;
				}
				
				throw new CompilationFailedError(sReader.getLocation(reader.peek()),
						"Expected variable declaration or function declaration in a cover declaration, but got "
						+reader.peek());
			
			}
			reader.skip();
			
			module.parseStack.pop(coverDecl);
			return coverDecl;
			
		}
		
		reader.reset(mark);
		return null;
	}
	
}
