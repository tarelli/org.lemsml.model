package org.lemsml.model.test;

import java.io.File;
import java.math.BigInteger;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.Parameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Dimension;
import org.lemsml.model.extended.Lems;
import org.lemsml.model.extended.Unit;


public class DimensionalAnalysisTest extends BaseTest {
	@Rule public ExpectedException exception = ExpectedException.none();

	private File schema;
	private File lemsDoc;
	private LEMSCompilerFrontend compiler;

	@Before
	public void setUp() throws Throwable {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
		lemsDoc = getLocalFile("/examples/dimensional-analysis-test/dimensional.xml");
		compiler = new LEMSCompilerFrontend(lemsDoc, schema);
	}

	@Test
	public void testPlainFile() throws Throwable{
		compiler.generateLEMSDocument();
	}

	@Test
	public void testWrongParameter() throws Throwable {
		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.DimensionalAnalysis.toString());

		//Lems fakeLems = compiler.generateLEMSDocument();
		Lems fakeLems = LEMSCompilerFrontend.parseLemsFile(lemsDoc, schema);

		Component fakeHO = (Component) new Component()
							.withType("HarmonicOscillator")
							.withId("hoho");
		//intentionally causing an unit mismatch error to be catched (mass in metres...)
		fakeHO.withParameterValue("m", "1m");
		fakeHO.withParameterValue("k", "1N_per_m");
		fakeLems.withComponents(fakeHO);
		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(null);
		compiler.semanticAnalysis(fakeLems);
	}

	@Test
	public void testDerivedParDimension() throws Throwable {
		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.DimensionalAnalysis.toString());
		Lems fakeLems = new Lems();

		Dimension lenDim = new Dimension();
		lenDim.setL( BigInteger.valueOf(1));
		lenDim.setName("length");
		Dimension timeDim = new Dimension();
		timeDim.setT( BigInteger.valueOf(1));
		timeDim.setName("time");

		Unit metre = new Unit();
		metre.setDimension("length");
		metre.setSymbol("m");

		ComponentType type = new ComponentType();
		type.setName("Foo");

		Parameter par = new Parameter();
		par.setName("p");
		par.setDimension("length");
		type.getParameters().add(par);

		DerivedParameter derPar = new DerivedParameter();
		derPar.setName("twoP");
		derPar.setValueDefinition("2*p");
		derPar.setDimension("time"); //ooops!
		type.getDerivedParameters().add(derPar);

		Component comp = new Component();
		comp.setName("foo");
		comp.setType("Foo");
		comp.getOtherAttributes().put(new QName("p"), "1m");

		fakeLems.getDimensions().add(lenDim);
		fakeLems.getDimensions().add(timeDim);
		fakeLems.getUnits().add(metre);
		fakeLems.getComponents().add(comp);
		fakeLems.getComponentTypes().add(type);

		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(null);
		compiler.semanticAnalysis(fakeLems);
	}



}
