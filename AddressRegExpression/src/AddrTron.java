package baseCryptor;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.Regexp;

public class AddrTron {
	
	private static Map<String,  String> directionals; 
	private static Map<String,  String> suffixes; 
	private static Map<String,  String> states; 

	private static String [ ] fields =
		{ "NUMBER", "PREDIRECTIONAL", "STREET", "STREETLINE",
				"SUFFIX", "POSTDIRECTIONAL", "CITY",
				"STATE", "ZIP", "SECONDARYUNIT,"
						+ "SECONDARYNUMBER"
		};
	
	static {
	    // Maps directional names (north, northeast, etc.) to abbreviations (N, NE, etc.).
        // </summary>
    
		directionals = new Hashtable<String, String> ();
		
		String [ ] [ ] items = {
				{"NORTH", "N" },
				{"NORTHEAST", "NE"},
				{"EAST", "E"},
				{"SOUTHEAST","SE"},
				{"SOUTH", "S"},
				{"SOUTHWEST" ,"SW"},
				{"WEST", "W"},
				{"NORTHWEST", "NW"}
		};
		
		directionals = buildTable(items,directionals);
		
        // Maps lowercased US state and territory names to their canonical two-letter
        // postal abbreviations.

		states = new Hashtable<String, String> ();
		
		String [ ] [ ] stateList = {
                { "ALABAMA", "AL" },
                { "ALASKA", "AK" },
                { "AMERICAN SAMOA", "AS" },
                { "ARIZONA", "AZ" },
                { "ARKANSAS", "AR" },
                { "CALIFORNIA", "CA" },
                { "COLORADO", "CO" },
                { "CONNECTICUT", "CT" },
                { "DELAWARE", "DE" },
                { "DISTRICT OF COLUMBIA", "DC" },
                { "FEDERATED STATES OF MICRONESIA", "FM" },
                { "FLORIDA", "FL" },
                { "GEORGIA", "GA" },
                { "GUAM", "GU" },
                { "HAWAII", "HI" },
                { "IDAHO", "ID" },
                { "ILLINOIS", "IL" },
                { "INDIANA", "IN" },
                { "IOWA", "IA" },
                { "KANSAS", "KS" },
                { "KENTUCKY", "KY" },
                { "LOUISIANA", "LA" },
                { "MAINE", "ME" },
                { "MARSHALL ISLANDS", "MH" },
                { "MARYLAND", "MD" },
                { "MASSACHUSETTS", "MA" },
                { "MICHIGAN", "MI" },
                { "MINNESOTA", "MN" },
                { "MISSISSIPPI", "MS" },
                { "MISSOURI", "MO" },
                { "MONTANA", "MT" },
                { "NEBRASKA", "NE" },
                { "NEVADA", "NV" },
                { "NEW HAMPSHIRE", "NH" },
                { "NEW JERSEY", "NJ" },
                { "NEW MEXICO", "NM" },
                { "NEW YORK", "NY" },
                { "NORTH CAROLINA", "NC" },
                { "NORTH DAKOTA", "ND" },
                { "NORTHERN MARIANA ISLANDS", "MP" },
                { "OHIO", "OH" },
                { "OKLAHOMA", "OK" },
                { "OREGON", "OR" },
                { "PALAU", "PW" },
                { "PENNSYLVANIA", "PA" },
                { "PUERTO RICO", "PR" },
                { "RHODE ISLAND", "RI" },
                { "SOUTH CAROLINA", "SC" },
                { "SOUTH DAKOTA", "SD" },
                { "TENNESSEE", "TN" },
                { "TEXAS", "TX" },
                { "UTAH", "UT" },
                { "VERMONT", "VT" },
                { "VIRGIN ISLANDS", "VI" },
                { "VIRGINIA", "VA" },
                { "WASHINGTON", "WA" },
                { "WEST VIRGINIA", "WV" },
                { "WISCONSIN", "WI" },
                { "WYOMING", "WY" }
		};
		
		states = buildTable(stateList,states);

		/// Maps lowerecased USPS standard street suffixes to their canonical postal
        /// abbreviations as found in TIGER/Line.
		suffixes = new Hashtable<String, String> ();
		
		String [ ] [ ] suffixItems = {
                { "ALLEE", "ALY" },
                { "ALLEY", "ALY" },
                { "ALLY", "ALY" },
                { "ANEX", "ANX" },
                { "ANNEX", "ANX" },
                { "ANNX", "ANX" },
                { "ARCADE", "ARC" },
                { "AV", "AVE" },
                { "AVEN", "AVE" },
                { "AVENU", "AVE" },
                { "AVENUE", "AVE" },
                { "AVN", "AVE" },
                { "AVNUE", "AVE" },
                { "BAYOO", "BYU" },
                { "BAYOU", "BYU" },
                { "BEACH", "BCH" },
                { "BEND", "BND" },
                { "BLUF", "BLF" },
                { "BLUFF", "BLF" },
                { "BLUFFS", "BLFS" },
                { "BOT", "BTM" },
                { "BOTTM", "BTM" },
                { "BOTTOM", "BTM" },
                { "BOUL", "BLVD" },
                { "BOULEVARD", "BLVD" },
                { "BOULV", "BLVD" },
                { "BRANCH", "BR" },
                { "BRDGE", "BRG" },
                { "BRIDGE", "BRG" },
                { "BRNCH", "BR" },
                { "BROOK", "BRK" },
                { "BROOKS", "BRKS" },
                { "BURG", "BG" },
                { "BURGS", "BGS" },
                { "BYPA", "BYP" },
                { "BYPAS", "BYP" },
                { "BYPASS", "BYP" },
                { "BYPS", "BYP" },
                { "CAMP", "CP" },
                { "CANYN", "CYN" },
                { "CANYON", "CYN" },
                { "CAPE", "CPE" },
                { "CAUSEWAY", "CSWY" },
                { "CAUSWAY", "CSWY" },
                { "CEN", "CTR" },
                { "CENT", "CTR" },
                { "CENTER", "CTR" },
                { "CENTERS", "CTRS" },
                { "CENTR", "CTR" },
                { "CENTRE", "CTR" },
                { "CIRC", "CIR" },
                { "CIRCL", "CIR" },
                { "CIRCLE", "CIR" },
                { "CIRCLES", "CIRS" },
                { "CK", "CRK" },
                { "CLIFF", "CLF" },
                { "CLIFFS", "CLFS" },
                { "CLUB", "CLB" },
                { "CMP", "CP" },
                { "CNTER", "CTR" },
                { "CNTR", "CTR" },
                { "CNYN", "CYN" },
                { "COMMON", "CMN" },
                { "CORNER", "COR" },
                { "CORNERS", "CORS" },
                { "COURSE", "CRSE" },
                { "COURT", "CT" },
                { "COURTS", "CTS" },
                { "COVE", "CV" },
                { "COVES", "CVS" },
                { "CR", "CRK" },
                { "CRCL", "CIR" },
                { "CRCLE", "CIR" },
                { "CRECENT", "CRES" },
                { "CREEK", "CRK" },
                { "CRESCENT", "CRES" },
                { "CRESENT", "CRES" },
                { "CREST", "CRST" },
                { "CROSSING", "XING" },
                { "CROSSROAD", "XRD" },
                { "CRSCNT", "CRES" },
                { "CRSENT", "CRES" },
                { "CRSNT", "CRES" },
                { "CRSSING", "XING" },
                { "CRSSNG", "XING" },
                { "CRT", "CT" },
                { "CURVE", "CURV" },
                { "DALE", "DL" },
                { "DAM", "DM" },
                { "DIV", "DV" },
                { "DIVIDE", "DV" },
                { "DRIV", "DR" },
                { "DRIVE", "DR" },
                { "DRIVES", "DRS" },
                { "DRV", "DR" },
                { "DVD", "DV" },
                { "ESTATE", "EST" },
                { "ESTATES", "ESTS" },
                { "EXP", "EXPY" },
                { "EXPR", "EXPY" },
                { "EXPRESS", "EXPY" },
                { "EXPRESSWAY", "EXPY" },
                { "EXPW", "EXPY" },
                { "EXTENSION", "EXT" },
                { "EXTENSIONS", "EXTS" },
                { "EXTN", "EXT" },
                { "EXTNSN", "EXT" },
                { "FALLS", "FLS" },
                { "FERRY", "FRY" },
                { "FIELD", "FLD" },
                { "FIELDS", "FLDS" },
                { "FLAT", "FLT" },
                { "FLATS", "FLTS" },
                { "FORD", "FRD" },
                { "FORDS", "FRDS" },
                { "FOREST", "FRST" },
                { "FORESTS", "FRST" },
                { "FORG", "FRG" },
                { "FORGE", "FRG" },
                { "FORGES", "FRGS" },
                { "FORK", "FRK" },
                { "FORKS", "FRKS" },
                { "FORT", "FT" },
                { "FREEWAY", "FWY" },
                { "FREEWY", "FWY" },
                { "FRRY", "FRY" },
                { "FRT", "FT" },
                { "FRWAY", "FWY" },
                { "FRWY", "FWY" },
                { "GARDEN", "GDN" },
                { "GARDENS", "GDNS" },
                { "GARDN", "GDN" },
                { "GATEWAY", "GTWY" },
                { "GATEWY", "GTWY" },
                { "GATWAY", "GTWY" },
                { "GLEN", "GLN" },
                { "GLENS", "GLNS" },
                { "GRDEN", "GDN" },
                { "GRDN", "GDN" },
                { "GRDNS", "GDNS" },
                { "GREEN", "GRN" },
                { "GREENS", "GRNS" },
                { "GROV", "GRV" },
                { "GROVE", "GRV" },
                { "GROVES", "GRVS" },
                { "GTWAY", "GTWY" },
                { "HARB", "HBR" },
                { "HARBOR", "HBR" },
                { "HARBORS", "HBRS" },
                { "HARBR", "HBR" },
                { "HAVEN", "HVN" },
                { "HAVN", "HVN" },
                { "HEIGHT", "HTS" },
                { "HEIGHTS", "HTS" },
                { "HGTS", "HTS" },
                { "HIGHWAY", "HWY" },
                { "HIGHWY", "HWY" },
                { "HILL", "HL" },
                { "HILLS", "HLS" },
                { "HIWAY", "HWY" },
                { "HIWY", "HWY" },
                { "HLLW", "HOLW" },
                { "HOLLOW", "HOLW" },
                { "HOLLOWS", "HOLW" },
                { "HOLWS", "HOLW" },
                { "HRBOR", "HBR" },
                { "HT", "HTS" },
                { "HWAY", "HWY" },
                { "INLET", "INLT" },
                { "ISLAND", "IS" },
                { "ISLANDS", "ISS" },
                { "ISLES", "ISLE" },
                { "ISLND", "IS" },
                { "ISLNDS", "ISS" },
                { "JCTION", "JCT" },
                { "JCTN", "JCT" },
                { "JCTNS", "JCTS" },
                { "JUNCTION", "JCT" },
                { "JUNCTIONS", "JCTS" },
                { "JUNCTN", "JCT" },
                { "JUNCTON", "JCT" },
                { "KEY", "KY" },
                { "KEYS", "KYS" },
                { "KNOL", "KNL" },
                { "KNOLL", "KNL" },
                { "KNOLLS", "KNLS" },
                { "LA", "LN" },
                { "LAKE", "LK" },
                { "LAKES", "LKS" },
                { "LANDING", "LNDG" },
                { "LANE", "LN" },
                { "LANES", "LN" },
                { "LDGE", "LDG" },
                { "LIGHT", "LGT" },
                { "LIGHTS", "LGTS" },
                { "LNDNG", "LNDG" },
                { "LOAF", "LF" },
                { "LOCK", "LCK" },
                { "LOCKS", "LCKS" },
                { "LODG", "LDG" },
                { "LODGE", "LDG" },
                { "LOOPS", "LOOP" },
                { "MANOR", "MNR" },
                { "MANORS", "MNRS" },
                { "MEADOW", "MDW" },
                { "MEADOWS", "MDWS" },
                { "MEDOWS", "MDWS" },
                { "MILL", "ML" },
                { "MILLS", "MLS" },
                { "MISSION", "MSN" },
                { "MISSN", "MSN" },
                { "MNT", "MT" },
                { "MNTAIN", "MTN" },
                { "MNTN", "MTN" },
                { "MNTNS", "MTNS" },
                { "MOTORWAY", "MTWY" },
                { "MOUNT", "MT" },
                { "MOUNTAIN", "MTN" },
                { "MOUNTAINS", "MTNS" },
                { "MOUNTIN", "MTN" },
                { "MSSN", "MSN" },
                { "MTIN", "MTN" },
                { "NECK", "NCK" },
                { "ORCHARD", "ORCH" },
                { "ORCHRD", "ORCH" },
                { "OVERPASS", "OPAS" },
                { "OVL", "OVAL" },
                { "PARKS", "PARK" },
                { "PARKWAY", "PKWY" },
                { "PARKWAYS", "PKWY" },
                { "PARKWY", "PKWY" },
                { "PASSAGE", "PSGE" },
                { "PATHS", "PATH" },
                { "PIKES", "PIKE" },
                { "PINE", "PNE" },
                { "PINES", "PNES" },
                { "PK", "PARK" },
                { "PKWAY", "PKWY" },
                { "PKWYS", "PKWY" },
                { "PKY", "PKWY" },
                { "PLACE", "PL" },
                { "PLAIN", "PLN" },
                { "PLAINES", "PLNS" },
                { "PLAINS", "PLNS" },
                { "PLAZA", "PLZ" },
                { "PLZA", "PLZ" },
                { "POINT", "PT" },
                { "POINTS", "PTS" },
                { "PORT", "PRT" },
                { "PORTS", "PRTS" },
                { "PRAIRIE", "PR" },
                { "PRARIE", "PR" },
                { "PRK", "PARK" },
                { "PRR", "PR" },
                { "RAD", "RADL" },
                { "RADIAL", "RADL" },
                { "RADIEL", "RADL" },
                { "RANCH", "RNCH" },
                { "RANCHES", "RNCH" },
                { "RAPID", "RPD" },
                { "RAPIDS", "RPDS" },
                { "RDGE", "RDG" },
                { "REST", "RST" },
                { "RIDGE", "RDG" },
                { "RIDGES", "RDGS" },
                { "RIVER", "RIV" },
                { "RIVR", "RIV" },
                { "RNCHS", "RNCH" },
                { "ROAD", "RD" },
                { "ROADS", "RDS" },
                { "ROUTE", "RTE" },
                { "RVR", "RIV" },
                { "SHOAL", "SHL" },
                { "SHOALS", "SHLS" },
                { "SHOAR", "SHR" },
                { "SHOARS", "SHRS" },
                { "SHORE", "SHR" },
                { "SHORES", "SHRS" },
                { "SKYWAY", "SKWY" },
                { "SPNG", "SPG" },
                { "SPNGS", "SPGS" },
                { "SPRING", "SPG" },
                { "SPRINGS", "SPGS" },
                { "SPRNG", "SPG" },
                { "SPRNGS", "SPGS" },
                { "SPURS", "SPUR" },
                { "SQR", "SQ" },
                { "SQRE", "SQ" },
                { "SQRS", "SQS" },
                { "SQU", "SQ" },
                { "SQUARE", "SQ" },
                { "SQUARES", "SQS" },
                { "STATION", "STA" },
                { "STATN", "STA" },
                { "STN", "STA" },
                { "STR", "ST" },
                { "STRAV", "STRA" },
                { "STRAVE", "STRA" },
                { "STRAVEN", "STRA" },
                { "STRAVENUE", "STRA" },
                { "STRAVN", "STRA" },
                { "STREAM", "STRM" },
                { "STREET", "ST" },
                { "STREETS", "STS" },
                { "STREME", "STRM" },
                { "STRT", "ST" },
                { "STRVN", "STRA" },
                { "STRVNUE", "STRA" },
                { "SUMIT", "SMT" },
                { "SUMITT", "SMT" },
                { "SUMMIT", "SMT" },
                { "TERR", "TER" },
                { "TERRACE", "TER" },
                { "THROUGHWAY", "TRWY" },
                { "TPK", "TPKE" },
                { "TR", "TRL" },
                { "TRACE", "TRCE" },
                { "TRACES", "TRCE" },
                { "TRACK", "TRAK" },
                { "TRACKS", "TRAK" },
                { "TRAFFICWAY", "TRFY" },
                { "TRAIL", "TRL" },
                { "TRAILS", "TRL" },
                { "TRK", "TRAK" },
                { "TRKS", "TRAK" },
                { "TRLS", "TRL" },
                { "TRNPK", "TPKE" },
                { "TRPK", "TPKE" },
                { "TUNEL", "TUNL" },
                { "TUNLS", "TUNL" },
                { "TUNNEL", "TUNL" },
                { "TUNNELS", "TUNL" },
                { "TUNNL", "TUNL" },
                { "TURNPIKE", "TPKE" },
                { "TURNPK", "TPKE" },
                { "UNDERPASS", "UPAS" },
                { "UNION", "UN" },
                { "UNIONS", "UNS" },
                { "VALLEY", "VLY" },
                { "VALLEYS", "VLYS" },
                { "VALLY", "VLY" },
                { "VDCT", "VIA" },
                { "VIADCT", "VIA" },
                { "VIADUCT", "VIA" },
                { "VIEW", "VW" },
                { "VIEWS", "VWS" },
                { "VILL", "VLG" },
                { "VILLAG", "VLG" },
                { "VILLAGE", "VLG" },
                { "VILLAGES", "VLGS" },
                { "VILLE", "VL" },
                { "VILLG", "VLG" },
                { "VILLIAGE", "VLG" },
                { "VIST", "VIS" },
                { "VISTA", "VIS" },
                { "VLLY", "VLY" },
                { "VST", "VIS" },
                { "VSTA", "VIS" },
                { "WALKS", "WALK" },
                { "WELL", "WL" },
                { "WELLS", "WLS" },
                { "WY", "WAY" }
		};
		
		suffixes = buildTable(suffixItems,suffixes);
		
	}
	private static void InitializeRegex() {
	
		Regex suffixPattern = new Regex (		//todo: Need to figure this out
				"|" 
			+	"|" + suffixes.keys() 
			+	"|" + suffixes.elements().Distinct())
				)),
		 	RegexOptions.Compiled);
		 	
		 	String statePattern = "\b(?:" +
		 			"|" + 
		 			"|" + states.keys().Select(x => Regex.Escape(x))) + 
					"|" + states.values() +
			")\b";
		 	
 			String directionalPattern =
 	               "|" +
 	                "|" + directionals.keySet() + 
 	                "|" + directionals.values() +
 	                "|" + directionals.values().Select(x => Regex.Replace(x, "(\\w)", "$1\\."));
 			 String zipPattern = "\\d{5}(?:-?\\d{4})?";

             String numberPattern =
                  "("
                 +  " ((?<NUMBER>\\d+)(?<SECONDARYNUMBER>(-[0-9])|(\\-?[A-Z]))(?=\\b))"    
                 +  "    |(?<NUMBER>\\d+[\\-\\ ]?\\d+\\/\\d+)"                                   
                 +  "   |(?<NUMBER>\\d+-?\\d*)"                                             
                 +  "   |(?<NUMBER>[NSWE]\\ ?\\d+\\ ?[NSWE]\\ ?\\d+)"                          
                 +  ")";

	}
	
	private static String Join(String oldstr, String[ ] newStr ) {
			
			String input = oldStr;
			for (String item : newStr) {
				input = input + item;
			}
			return input;
	}

	private static String Join(String [ ] newStr, String oldStr ) {
		
		String input = oldStr;
		for (String item : newStr) {
			input = input + item;
		}
		return input;
	}

	private static Map<String, String> buildTable(String items [ ] [ ], Map<String, String> theTable) {

		String item [ ] = null;
		for (int x = 0; x < items.length; x++) {
			item = items[x];
			theTable.put(item[0], item[1]);
		}
	
		return theTable;
	}
	
    // Given a set of fields pulled from a successful match, this normalizes each value
    // by stripping off some punctuation and, if applicable, converting it to a standard
    // USPS abbreviation.
    // </summary>
    // <param name="extracted">The dictionary of extracted fields.</param>
    // <returns>A dictionary of the extracted fields with normalized values.</returns>
	private static Map<String, String> Normalize (Map<String, String> extracted) {
		
		Map<String,String> normalized = new Hashtable<String, String>();
		
		for (Map.Entry<String, String> entry : extracted.entrySet()) {
			
			String key = entry.getKey(); 
			String value = entry.getValue(); 
					
			String = Regex.Replace (value,	"^\\s+|\\s+$|[^\\/\\w\\s\\-\\#\\&]",  String.Empty);

                // Normalize to official abbreviations where appropriate
                value = GetNormalizedValueForField(key, value);

                normalized.replace(key,value);
		}
		
		// special case for an attahced unit
		if (extracted.containsKey("SECONDARYNUMBER") &&
				(!extracted.containsKey("SECONDARYNUMBER") ||
				String.IsNullOrWhiteSpace(extracted.get("SECONDARYUNIT")))
			normalized.replace("SECONDARYUNIT", "APT");
			
		return normalized;
	}
	
	
}
