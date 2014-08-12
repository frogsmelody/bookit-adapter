
package com.derbysoft.bookit.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RateAmountMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RateAmountMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StatusApplicationControl" type="{}StatusApplicationControl"/>
 *         &lt;element name="Rates" type="{}Rates"/>
 *       &lt;/sequence>
 *       &lt;attribute name="LocatorID" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RateAmountMessage", propOrder = {
    "statusApplicationControl",
    "rates"
})
public class RateAmountMessage {

    @XmlElement(name = "StatusApplicationControl", required = true)
    protected StatusApplicationControl statusApplicationControl;
    @XmlElement(name = "Rates", required = true)
    protected Rates rates;
    @XmlAttribute(name = "LocatorID")
    protected Integer locatorID;

    /**
     * Gets the value of the statusApplicationControl property.
     * 
     * @return
     *     possible object is
     *     {@link StatusApplicationControl }
     *     
     */
    public StatusApplicationControl getStatusApplicationControl() {
        return statusApplicationControl;
    }

    /**
     * Sets the value of the statusApplicationControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusApplicationControl }
     *     
     */
    public void setStatusApplicationControl(StatusApplicationControl value) {
        this.statusApplicationControl = value;
    }

    /**
     * Gets the value of the rates property.
     * 
     * @return
     *     possible object is
     *     {@link Rates }
     *     
     */
    public Rates getRates() {
        return rates;
    }

    /**
     * Sets the value of the rates property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rates }
     *     
     */
    public void setRates(Rates value) {
        this.rates = value;
    }

    /**
     * Gets the value of the locatorID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLocatorID() {
        return locatorID;
    }

    /**
     * Sets the value of the locatorID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLocatorID(Integer value) {
        this.locatorID = value;
    }

}
