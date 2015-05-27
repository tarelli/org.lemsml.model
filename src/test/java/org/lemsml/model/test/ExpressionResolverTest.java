package org.lemsml.model.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.lemsml.model.DerivedParameter;
import org.lemsml.model.compiler.LEMSCompilerFrontend;
import org.lemsml.model.exceptions.LEMSCompilerError;
import org.lemsml.model.exceptions.LEMSCompilerException;
import org.lemsml.model.extended.Component;
import org.lemsml.model.extended.ComponentType;
import org.lemsml.model.extended.Lems;

public class ExpressionResolverTest extends BaseTest {
	@Rule public ExpectedException exception = ExpectedException.none();
	private File schema;

	@Before
	public void setUp() {
		schema = getLocalFile("/Schemas/LEMS_v0.9.0.xsd");
	}

	@Test
	public void testNested() throws Throwable {

		File lemsDoc = getLocalFile("/examples/expression-resolver-test/nested_expressions.xml");

		LEMSCompilerFrontend compiler = new LEMSCompilerFrontend(lemsDoc,
				schema);
		Lems compiledLems = compiler.generateLEMSDocument();

//		assertEquals("-0.1", compiledLems.getConstantByName("const0").getValue());
		Double p0 = new Double(2.0);
		Double dp0 = compiledLems.getComponentById("comp0").resolve("dp0").evaluate();
		Double dp1 = compiledLems.getComponentById("comp0").resolve("dp1").evaluate();
		assertEquals(p0 * p0, dp0, 1e-12);
		assertEquals(p0 * p0 * dp0, dp1, 1e-12);
	}
	
	@Test
	public void testUndefinedSymbol() throws Throwable {
		exception.expect(LEMSCompilerException.class);
		exception.expectMessage(LEMSCompilerError.UndefinedSymbol.toString());

		// Unfortunately, the fluent API jaxb plugin seems to be broken, so we
		// have to live with verbosity...
		Lems fakeLems = new Lems(); 
		ComponentType compType = new ComponentType();
		compType.setName("Foo");
		Component fakeComp = new Component();
		fakeComp.setType("Foo");
		fakeLems.getComponents().add(fakeComp);

		DerivedParameter fakeDp = new DerivedParameter();
		fakeDp.setName("fake");
		fakeDp.setValue("2 * undefined");
		compType.getDerivedParameters().add(fakeDp);
		fakeLems.getComponentTypes().add(compType);

		LEMSCompilerFrontend.semanticAnalysis(fakeLems);
	}

}
