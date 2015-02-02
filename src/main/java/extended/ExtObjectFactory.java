package extended;

import javax.xml.bind.annotation.XmlRegistry;

import org.lemsml.model.ObjectFactory;

/**
 * @author borismarin
 *
 */
@XmlRegistry
public class ExtObjectFactory extends ObjectFactory
{

	@Override
	public Component createComponent()
	{
		return new Component();
	}

	@Override
	public Dimension createDimension()
	{
		return new Dimension();
	}

	@Override
	public Unit createUnit()
	{
		return new Unit();
	}

	@Override
	public Lems createLems()
	{
		return new Lems();
	}
}