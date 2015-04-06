/**
 * Copyright (C) 2008, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.manchester.mae.evaluation;

/**
 * @author Luigi Iannone The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Apr 10, 2008
 */
public class BindingModel {

    String identifier;
    PropertyChainModel propertyChainModel;

    /**
     * @param identifier
     * @param propertyChainModel
     */
    public BindingModel(String identifier,
            PropertyChainModel propertyChainModel) {
        this.identifier = identifier;
        this.propertyChainModel = propertyChainModel;
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier
     *        the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the propertyChainModel
     */
    public PropertyChainModel getPropertyChainModel() {
        return propertyChainModel;
    }

    /**
     * @param propertyChainModel
     *        the propertyChainModel to set
     */
    public void setPropertyChainModel(PropertyChainModel propertyChainModel) {
        this.propertyChainModel = propertyChainModel;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return obj.getClass().equals(this.getClass())
                && ((BindingModel) obj).identifier.equals(getIdentifier());
    }

    @Override
    public String toString() {
        return identifier + "->" + propertyChainModel.toString();
    }
}
