
package com.derbysoft.bookit.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Rate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Rate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BaseByGuestAmts" type="{}BaseByGuestAmts"/>
 *       &lt;/sequence>
 *       &lt;attribute name="NumberOfUnits" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="RateTimeUnit" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="UnitMultiplier" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="DecimalPlaces" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rate", propOrder = {
    "baseByGuestAmts"
})
public class Rate {

    @XmlElement(name = "BaseByGuestAmts", required = true)
    protected BaseByGuestAmts baseByGuestAmts;
    @XmlAttribute(name = "NumberOfUnits")
    protected Integer numberOfUnits;
    @XmlAttribute(name = "RateTimeUnit")
    protected String rateTimeUnit;
    @XmlAttribute(name = "UnitMultiplier")
    protected Integer unitMultiplier;
    @XmlAttribute(name = "DecimalPlaces")
    protected Integer decimalPlaces;

    /**
     * Gets the value of the baseByGuestAmts property.
     * 
     * @return
     *     possible object is
     *     {@link BaseByGuestAmts }
     *     
     */
    public BaseByGuestAmts getBaseByGuestAmts() {
        return baseByGuestAmts;
    }

    /**
     * Sets the value of the baseByGuestAmts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseByGuestAmts }
     *     
     */
    public void setBaseByGuestAmts(BaseByGuestAmts value) {
        this.baseByGuestAmts = value;
    }

    /**
     * Gets the value of the numberOfUnits property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    /**
     * Sets the value of the numberOfUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfUnits(Integer value) {
        this.numberOfUnits = value;
    }

    /**
     * Gets the value of the rateTimeUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRateTimeUnit() {
        return rateTimeUnit;
    }

    /**
     * Sets the value of the rateTimeUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRateTimeUnit(String value) {
        this.rateTimeUnit = value;
    }

    /**
     * Gets the value of the unitMultiplier property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUnitMultiplier() {
        return unitMultiplier;
    }

    /**
     * Sets the value of the unitMultiplier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUnitMultiplier(Integer value) {
        this.unitMultiplier = value;
    }

    /**
     * Gets the value of the decimalPlaces property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    /**
     * Sets the value of the decimalPlaces property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDecimalPlaces(Integer value) {
        this.decimalPlaces = value;
    }

}
