
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
 *         &lt;element name="RateAmountMessages" type="{}RateAmountMessages"/>
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
    "rateAmountMessages"
})
@XmlRootElement(name = "OTA_HotelRateAmountNotifRQ")
public class OTAHotelRateAmountNotifRQ
    extends OTAMessage
{

    @XmlElement(name = "RateAmountMessages", required = true)
    protected RateAmountMessages rateAmountMessages;

    /**
     * Gets the value of the rateAmountMessages property.
     * 
     * @return
     *     possible object is
     *     {@link RateAmountMessages }
     *     
     */
    public RateAmountMessages getRateAmountMessages() {
        return rateAmountMessages;
    }

    /**
     * Sets the value of the rateAmountMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link RateAmountMessages }
     *     
     */
    public void setRateAmountMessages(RateAmountMessages value) {
        this.rateAmountMessages = value;
    }

}
