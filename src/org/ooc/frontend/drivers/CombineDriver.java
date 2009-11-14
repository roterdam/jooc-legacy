package org.ooc.frontend.drivers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.ooc.frontend.Target;
import org.ooc.frontend.model.Module;
import org.ooc.frontend.parser.BuildParams;

public class CombineDriver extends Driver {

	public CombineDriver(BuildParams params) {
		super(params);
	}

	@Override
	public int compile(Module module, String outName) throws Error, IOException, InterruptedException {
		
		params.compiler.reset();
		
		copyLocalHeaders(module, params, new HashSet<Module>());
		
		if(params.debug) params.compiler.setDebugEnabled();		
		params.compiler.addIncludePath(new File(params.distLocation, "libs/headers/").getPath());
		params.compiler.addIncludePath(params.outPath.getPath());
		addDeps(module, new HashSet<Module>(), new HashSet<String>());
		for(String define: params.defines) {
			params.compiler.defineSymbol(define);
		}
		for(String dynamicLib: params.dynamicLibs) {
			params.compiler.addDynamicLibrary(dynamicLib);
		}
		for(String additional: additionals) {
			params.compiler.addObjectFile(additional);
		}
		for(String compilerArg: compilerArgs) {
			params.compiler.addObjectFile(compilerArg);
		}
		
		// perhaps these should be per-compiler overrides but GCC and clang
		// both accept these flags
		if (params.fatArchitectures != null) {
			for (String arch: params.fatArchitectures) {
				params.compiler.addOption("-arch");
				params.compiler.addOption(arch);
			}
		}
		if (params.osxSDKAndDeploymentTarget != null) {
			params.compiler.addOption("-isysroot");
			params.compiler.addOption("/Developer/SDKs/MacOSX" + params.osxSDKAndDeploymentTarget + ".sdk");
			params.compiler.addOption("-mmacosx-version-min=" + params.osxSDKAndDeploymentTarget);
		}
		
		if(params.link) {
			params.compiler.setOutputPath(outName);
			Collection<String> libs = getFlagsFromUse(module);
			for(String lib: libs) params.compiler.addObjectFile(lib);
			
			if(params.enableGC) {
				params.compiler.addDynamicLibrary("pthread");
				if(params.dynGC) {
					params.compiler.addDynamicLibrary("gc");
				} else {
					params.compiler.addObjectFile(new File(params.distLocation, "libs/"
							+ Target.guessHost().toString(params.arch.equals("") ? Target.getArch() : params.arch) + "/libgc.a").getPath());
				}
			}
		} else {
			params.compiler.setCompileOnly();
		}
		
		if(params.verbose) System.out.println(params.compiler.getCommandLine());
		
		int code = params.compiler.launch();
		if(code != 0) {
			System.err.println("C compiler failed, aborting compilation process");
		}
		return code;
		
	}
	
}
