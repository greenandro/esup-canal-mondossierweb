package org.esupportail.portal.channels.gestion.CMonDossierWeb;

/**
 * 
 * <p>StudentMail</p>
 * <p>Description : Classe impl�mentant la m�thode de r�cup�ration des adresses mail des �tudiants</p>
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">C�dric Champmartin</a>
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
