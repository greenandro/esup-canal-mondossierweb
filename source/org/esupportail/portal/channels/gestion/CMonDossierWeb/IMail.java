package org.esupportail.portal.channels.gestion.CMonDossierWeb;

/**
 * 
 * <p>IMail</p>
 * <p>Description : Interface décrivant la méthode de récupération d'une adresse mail d'un étudiant</p>
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.0
 *
 */
public interface IMail {

	/**
	 * Récupération de l'adresse mail à partir du login
	 * 
	 * @param login
	 * @return l'adresse mail
	 */
	public String getMail(String login);
	
}
