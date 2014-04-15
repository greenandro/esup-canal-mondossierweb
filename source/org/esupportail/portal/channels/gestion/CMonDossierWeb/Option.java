package org.esupportail.portal.channels.gestion.CMonDossierWeb;

/**
 * Option<br>
 * <br>
 * Cette classe décrit une option du canal. Elle est utilisée lors de la lecture
 * du fichier de configuration <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.0
 * 
 */
public class Option {

	// Nom de l'option
	private String name;
	// Valeur de l'option
	private String value;
	
	/**
	 * @return Renvoie name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name name à définir.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Renvoie value.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param value value à définir.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
