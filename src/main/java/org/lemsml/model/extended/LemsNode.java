package org.lemsml.model.extended;

import java.io.File;

import org.lemsml.model.exceptions.LEMSCompilerError;

public class LemsNode {
	private LEMSCompilerError error;
	//TODO: ugly.
	private File definedIn = new File("");

	public LEMSCompilerError getError() {
		return error;
	}
	public void setError(LEMSCompilerError error) {
		this.error = error;
	}
	public File getDefinedIn() {
		return definedIn;
	}
	public void setDefinedIn(File includedFile) {
		this.definedIn = includedFile;
	}

}
