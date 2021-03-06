package org.lemsml.model.extended;

import java.text.MessageFormat;

import javax.xml.bind.annotation.XmlTransient;

import org.lemsml.visitors.Visitor;

/**
 * @author borismarin
 *
 */
@XmlTransient
public class ComponentType extends org.lemsml.model.ComponentType {

	private ComponentType superType;

	public ComponentType getSuperType() {
		return superType;
	}

	public void setParent(ComponentType parent) {
		this.superType = parent;
	}

	@Override
	public <R, E extends Throwable> R accept(Visitor<R, E> aVisitor) throws E {
		return aVisitor.visit(this);
	}

	public String toString(){
		return MessageFormat.format("[{0}]", this.getName());
	}
}
