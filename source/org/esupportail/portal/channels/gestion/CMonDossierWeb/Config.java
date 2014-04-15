package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import java.io.IOException;
import java.util.HashMap;

import org.esupportail.portal.utils.channels.ConfigChannel;

/**
 * 
 * <p>ConfigChannel</p>
 * <p>Classe permettant d'accéder à la configuration de la channel CMonDossierWeb</p>
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:olivier.ziller@univ-nancy2.fr">Olivier Ziller</a>
 * @version 1.1
 *
 */
public class Config extends ConfigChannel {
	
	// Déclaration des variables de la classe
	private static Config singleton = null;
	// Classe implémentant la récupération de l'adresse mail d'un étudiant
	private String classMail;
	// Extension des adresses mail étudiantes
	private String extMail;
	// Adresse du contact
	private String adrContact;
	// Rappel identite
	private boolean rappelIdentite;
	// Message d'interdiction
	private String msgInterdit;
	// Message d'erreur base de données
	private String msgErreurBD;
	// Liste des options du canal
	private HashMap options;
	
	/**
	* 
	* @return ConfigChannel Instance
	* @throws IOException
	* @throws JspException
	*/
	public static Config getInstance() {
		if(singleton == null) {
			 singleton = new Config();
			 }
		return singleton;
	}

	/**
	 * Méthode qui permet d'ajouter une personnalisation sur les règles que doit prendre en compte
	 * le digester
	 */
	protected void customDigester() {
		dig.addCallMethod("config","addRoot",2);
		dig.addCallParam("config/msgInterdit",0);
		dig.addCallParam("config/msgErreurBD",1);
		// Mail 
		dig.addCallMethod("config/studentMail","addMail",2);
		dig.addCallParam("config/studentMail/classMail",0);
		dig.addCallParam("config/studentMail/extMail",1);
		// Contact
		dig.addCallMethod("config/lienContact","addLienContact",1);
	    dig.addCallParam("config/lienContact",0,"value");
	    // Rappel Identite
	    dig.addCallMethod("config/rappelIdentite","setRappelIdentite",1);
	    dig.addCallParam("config/rappelIdentite",0,"value");
		// Options
		dig.addObjectCreate("config/options/option",Option.class);
		dig.addSetProperties("config/options/option");
		dig.addSetNext("config/options/option","addOption");
	}

	/* (non-Javadoc)
	 * @see org.esupportail.portal.utils.channels.ConfigChannel#getConfigFile()
	 */
	protected String getConfigFile() {
		return "/properties/channels/org_esup/CMonDossierWeb/CMonDossierWeb.xml";
	}
	
	/**
	* 
	* @param msgInterdit Parameter of file CMonDossierWeb.xml
	* 
	*/
	public void addRoot(String msgInterdit, String msgErreurBD) { 
		this.msgInterdit = msgInterdit;
		this.msgErreurBD = msgErreurBD;
	}

	/**
	 * 
	 * @param classMail Parameter of file CMonDossierWeb.xml
	 * @param extMail Parameter of file CMonDossierWeb.xml
	 */
	public void addMail(String classMail, String extMail) {
		this.classMail = classMail;
		this.extMail = extMail;
	}
	
	public void addLienContact(String value){
		this.adrContact = value;
	}
	
	/**
	 * Méthode appelée par le digester lorsqu'une option a été lue dans le fichier de config
	 * @param o
	 */
	public void addOption(Option o) {
		if (options == null) {
			options = new HashMap();
		}
		options.put(o.getName(),o.getValue());
	}

	/**
	 * @return Returns the extMail.
	 */
	public String getExtMail() {
		return extMail;
	}
	/**
	 * @param extMail The extMail to set.
	 */
	public void setExtMail(String extMail) {
		this.extMail = extMail;
	}
	
	/**
	 * @return Returns the msgInterdit.
	 */
	public String getMsgInterdit() {
		return msgInterdit;
	}
	/**
	 * @param msgInterdit The msgInterdit to set.
	 */
	public void setMsgInterdit(String msgInterdit) {
		this.msgInterdit = msgInterdit;
	}
	
	/**
	 * @return Returns the classMail.
	 */
	public String getClassMail() {
		return classMail;
	}
	
	/**
	 * @param classMail The classMail to set.
	 */
	public void setClassMail(String classMail) {
		this.classMail = classMail;
	}
	/**
	 * @return Returns the adrContact.
	 */
	public String getAdrContact() {
		return adrContact;
	}

	/**
	 * @return Renvoie rappelIdentite.
	 */
	public boolean getRappelIdentite() {
		return rappelIdentite;
	}

	/**
	 * @param rappelIdentite rappelIdentite à définir.
	 */
	public void setRappelIdentite(String rappelIdentite) {
		if (rappelIdentite != null && rappelIdentite.equals("true"))
			this.rappelIdentite = true;
		else
			this.rappelIdentite = false;
	}

	/**
	 * @return Renvoie msgErreurBD.
	 */
	public String getMsgErreurBD() {
		return msgErreurBD;
	}

	/**
	 * @param msgErreurBD msgErreurBD à définir.
	 */
	public void setMsgErreurBD(String msgErreurBD) {
		this.msgErreurBD = msgErreurBD;
	}

	/**
	 * @return Renvoie options.
	 */
	public HashMap getOptions() {
		return options;
	}

	/**
	 * @param options options à définir.
	 */
	public void setOptions(HashMap options) {
		this.options = options;
	}

}

