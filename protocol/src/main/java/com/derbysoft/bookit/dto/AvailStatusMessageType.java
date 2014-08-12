
package com.derbysoft.bookit.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AvailStatusMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AvailStatusMessageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StatusApplicationControl" type="{}StatusApplicationControl"/>
 *       &lt;/sequence>
 *       &lt;attribute name="BookingLimitMessageType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="BookingLimit" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AvailStatusMessageType", propOrder = {
    "statusApplicationControl"
})
public class AvailStatusMessageType {

    @XmlElement(name = "StatusApplicationControl", required = true)
    protected StatusApplicationControl statusApplicationControl;
    @XmlAttribute(name = "BookingLimitMessageType")
    protected String bookingLimitMessageType;
    @XmlAttribute(name = "BookingLimit")
    protected Integer bookingLimit;

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
     * Gets the value of the bookingLimitMessageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBookingLimitMessageType() {
        return bookingLimitMessageType;
    }

    /**
     * Sets the value of the bookingLimitMessageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBookingLimitMessageType(String value) {
        this.bookingLimitMessageType = value;
    }

    /**
     * Gets the value of the bookingLimit property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBookingLimit() {
        return bookingLimit;
    }

    /**
     * Sets the value of the bookingLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBookingLimit(Integer value) {
        this.bookingLimit = value;
    }

}
