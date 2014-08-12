
package com.derbysoft.bookit.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BaseByGuestAmts complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BaseByGuestAmts">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BaseByGuestAmt" type="{}BaseByGuestAmt" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseByGuestAmts", propOrder = {
    "baseByGuestAmt"
})
public class BaseByGuestAmts {

    @XmlElement(name = "BaseByGuestAmt")
    protected List<BaseByGuestAmt> baseByGuestAmt;

    /**
     * Gets the value of the baseByGuestAmt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the baseByGuestAmt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBaseByGuestAmt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BaseByGuestAmt }
     * 
     * 
     */
    public List<BaseByGuestAmt> getBaseByGuestAmt() {
        if (baseByGuestAmt == null) {
            baseByGuestAmt = new ArrayList<BaseByGuestAmt>();
        }
        return this.baseByGuestAmt;
    }

}
