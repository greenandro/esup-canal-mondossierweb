package org.esupportail.portal.channels.gestion.CMonDossierWeb;

/**
 * 
 * <p>IMail</p>
 * <p>Description : Interface d�crivant la m�thode de r�cup�ration d'une adresse mail d'un �tudiant</p>
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">C�dric Champmartin</a>
 * @version 1.0
 *
 */
public interface IMail {

	/**
	 * R�cup�ration de l'adresse mail � partir du login
	 * 
	 * @param login
	 * @return l'adresse mail
	 */
	public String getMail(String login);
	
}
