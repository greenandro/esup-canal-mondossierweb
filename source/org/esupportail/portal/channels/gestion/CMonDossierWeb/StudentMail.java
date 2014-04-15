package org.esupportail.portal.channels.gestion.CMonDossierWeb;

/**
 * 
 * <p>StudentMail</p>
 * <p>Description : Classe implémentant la méthode de récupération des adresses mail des étudiants</p>
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.0
 *
 */
public class StudentMail implements IMail {

	public String getMail(String login) {
		if (Config.getInstance().getExtMail() != null && !Config.getInstance().getExtMail().equals(""))
			return login + Config.getInstance().getExtMail();
		return "";
	}

}
