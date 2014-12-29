/**
 * 
 */
package org.lemsml.model.test;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.Test;
import org.lemsml.model.ComponentType;
import org.lemsml.model.Lems;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import extended.ExtObjectFactory;
import extended.ExtParameter;

/**
 * @author boris
 *
 */
public class ValidationUnmarshallingTest {

	@Test
	public void test() {
		SchemaFactory sf = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL lems_xsd = getClass().getResource("/Schemas/LEMS_v0.8.0.xsd");
		URL hr_lems = getClass().getResource("/examples/HindmarshRose3d.xml");

		try {
			Schema schema = sf.newSchema(new File(lems_xsd.getFile()));

			JAXBContext jc = JAXBContext.newInstance(Lems.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(schema);
			unmarshaller.setProperty("com.sun.xml.internal.bind.ObjectFactory",new ExtObjectFactory());
			Lems lems = (Lems) unmarshaller.unmarshal(new File(hr_lems.getFile()));
			ComponentType hrct = lems.getComponentType().get(0);
			String desc = hrct.getDescription();
			assertEquals(desc, "     The Hindmarsh Rose model is a simplified point cell model which     captures complex firing patterns of single neurons, such as     periodic and chaotic bursting. It in a fast spiking subsystem,     which is a generalization of the Fitzhugh-Nagumo system, coupled     to a slower subsystem which allows the model to fire bursts. The     dynamical variables x,y,z correspond to the membrane potential, a     recovery variable, and a slower adaptation current, respectively.     ");
			List<ExtParameter> extParameterList = hrct.getParameter();
			assertEquals(extParameterList.get(0).getDescription(), "cubic term in x         nullcline");
		}
		catch(SAXParseException ex){
			fail("Test file " + hr_lems.getPath() + "not valid according to schema " + lems_xsd.getPath());
		} catch (SAXException e) {
			fail("Problems with schema file" + lems_xsd.getPath());
		} catch (JAXBException e) {
			e.printStackTrace();
			fail("Problems unmarshalling file" + hr_lems.getPath());
		}
	}

}
