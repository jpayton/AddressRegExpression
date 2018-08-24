package baseCryptor;

import java.util.Map;

import com.sun.xml.internal.bind.v2.runtime.property.Property;

public class AddressParseResult {

	private String street = null;
	private String number = null;
	private String suffix = null;
	private String secondaryNumber = null;
	private String secondaryUnit = null;
	private String postdirectional = null;
	private String predirectional= null;
	private String city = null;
	private String state = null;
	private String zip = null;
	private String streetLine = null;
	
    // <summary>
    // Initializes a new instance of the <see cref="AddressParseResult"/> class.
    // </summary>
    // <param name="fields">The fields that were parsed.</param>
    protected  AddressParseResult(Map<String, String> fields)
    {
        if (fields == null)
        {
            throw new ArgumentNullException("fields");
        }

        var type = this.GetType();
    
        for (Map.Entry<String, String> pair : fields.entrySet()) {
        {
            var bindingFlags = 
                BindingFlags.Instance | 
                BindingFlags.Public | 
                BindingFlags.IgnoreCase;
            Property propertyInfo = type.GetProperty(pair.getKey(), bindingFlags);
            if (propertyInfo != null)
            {
                var methodInfo = propertyInfo.GetSetMethod(true);
                if (methodInfo != null)
                {
                    methodInfo.Invoke(this, pair.getValue());
                }
            }
        }
    }

	private String Join(String str1, String [ ] str2) {
		
		String newStr = str1;
		for ( String stritm : str2) {
			newStr = newStr + stritm;
		}
		
		return newStr;
	}
	
	private String streetline = null;

    // <summary>
    // Gets the full street line, such as "500 N Main St" in "500 N Main St".
    // This is typically constructed by combining other elements in the parsed result.
    // However, in some special circumstances, most notably APO/FPO/DPO addresses, the
    // street line is set directly and the other elements will be null.
    // </summary>
    public String getStreetLine()
    {	
    	String streetStack [ ]  = {
                number, 
               predirectional,
               street,
               suffix,
               postdirectional,
               secondaryUnit,
               secondaryNumber};
    	
            if (this.streetLine == null)
            {
                String streetLine = Join(" ", streetStack);
                
                streetLine = Regex
                    .Replace(streetLine, "\\ +", " ")
                    .Trim();
                return streetLine;
            }

            return this.streetLine;
    } 
    
 // <summary>
    // Gets the full street line, such as "500 N Main St" in "500 N Main St".
    // This is typically constructed by combining other elements in the parsed result.
    // However, in some special circumstances, most notably APO/FPO/DPO addresses, the
    // street line is set directly and the other elements will be null.
    // </summary>
    public void setStreetLine(String value)
    {
            this.streetLine = value;
    }

	
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the secondaryUnitNumber
	 */
	public String getSecondaryNumber() {
		return secondaryNumber;
	}

	/**
	 * @param secondaryUnitNumber the secondaryUnitNumber to set
	 */
	public void setSecondaryUnitNumber(String secondaryNumber) {
		this.secondaryNumber = secondaryNumber;
	}

	/**
	 * @return the secondaryUnitName
	 */
	public String getSecondaryUnit() {
		return secondaryUnit;
	}

	/**
	 * @param secondaryUnitName the secondaryUnitName to set
	 */
	public void setSecondaryUnit(String secondaryUnit) {
		this.secondaryUnit = secondaryUnit;
	}

	/**
	 * @return the postdirectional
	 */
	public String getPostdirectional() {
		return postdirectional;
	}

	/**
	 * @param postdirectional the postdirectional to set
	 */
	public void setPostdirectional(String postdirectional) {
		this.postdirectional = postdirectional;
	}

	/**
	 * @return the predirectional
	 */
	public String getPredirectional() {
		return predirectional;
	}

	/**
	 * @param predirectional the predirectional to set
	 */
	public void setPredirectional(String predirectional) {
		this.predirectional = predirectional;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}


    // <summary>
    // Returns a <see cref="System.String"/> that represents this instance.
    // </summary>
    // <returns>
    // A <see cref="System.String"/> that represents this instance.
    // </returns>
    protected String ToString()
    {
        return String.Format(
            CultureInfo.InvariantCulture,
            "{0}; {1}, {2}  {3}",
            this.getStreetline,
            this.getCity(),
            this.getState(),
            this.getZip());
    }

}
