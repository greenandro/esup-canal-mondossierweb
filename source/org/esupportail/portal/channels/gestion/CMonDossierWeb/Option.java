package org.esupportail.portal.channels.gestion.CMonDossierWeb;

/**
 * Option<br>
 * <br>
 * Cette classe d�crit une option du canal. Elle est utilis�e lors de la lecture
 * du fichier de configuration <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">C�dric Champmartin</a>
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
	 * @param name name � d�finir.
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
	 * @param value value � d�finir.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
