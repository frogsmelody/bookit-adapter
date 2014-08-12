
package com.derbysoft.bookit.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{}OTAMessage">
 *       &lt;sequence>
 *         &lt;element name="AvailStatusMessages" type="{}AvailStatusMessagesType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "availStatusMessages"
})
@XmlRootElement(name = "OTA_HotelAvailNotifRQ")
public class OTAHotelAvailNotifRQ
    extends OTAMessage
{

    @XmlElement(name = "AvailStatusMessages", required = true)
    protected AvailStatusMessagesType availStatusMessages;

    /**
     * Gets the value of the availStatusMessages property.
     * 
     * @return
     *     possible object is
     *     {@link AvailStatusMessagesType }
     *     
     */
    public AvailStatusMessagesType getAvailStatusMessages() {
        return availStatusMessages;
    }

    /**
     * Sets the value of the availStatusMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link AvailStatusMessagesType }
     *     
     */
    public void setAvailStatusMessages(AvailStatusMessagesType value) {
        this.availStatusMessages = value;
    }

}
