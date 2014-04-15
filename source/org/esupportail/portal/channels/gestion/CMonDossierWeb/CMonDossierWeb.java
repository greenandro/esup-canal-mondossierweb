package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.portal.utils.channels.FrameWorkException;
import org.esupportail.portal.utils.channels.MainChannel;
import org.esupportail.portal.utils.channels.SubChannel;
import org.esupportail.portal.utils.connectors.ApogeeConnector;
import org.esupportail.portal.utils.connectors.ConnectorException;
import org.esupportail.portal.utils.database.Query;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.IServant;
import org.jasig.portal.PortalException;
import org.jasig.portal.security.IPerson;
import org.jasig.portal.utils.XMLEscaper;
import org.xml.sax.ContentHandler;
import org.esupportail.portal.utils.channels.plugins.FatalError;

/**
 * 
 * <p>CMonDossierWeb</p>
 * <p>Description : Mon dossier Web : consultation des résultats des étudiants</p>
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.2
 *
 */
public class CMonDossierWeb extends MainChannel {
	
	// log
	private static final Log log = LogFactory.getLog(CMonDossierWeb.class);
	// id de l'utilisateur
	private String userId;
	// cod_ind de l'utilisateur
	protected String cod_ind;
	// flux xml correspondant à l'en-tête des différentes pages
	private String xmlEtudiant;
	// login de la personne traitée
	protected String loginEtu;
	// objet permettant de construire les adresses mail des étudiants
	protected IMail mailObj;
	
	// servant mailTo
	private final static String toField ="to";//Mail.TO : pour éviter la dépendence on n'utilise pas la constante
	private final static String ccField ="cc";//Mail.CC : pour éviter la dépendence on n'utilise pas la constante
	private final static String bccField ="bcc";//Mail.BCC : pour éviter la dépendence on n'utilise pas la constante
	private final static String contentField ="message";//Mail.CONTENT : pour éviter la dépendence on n'utilise pas la constante
	
	public CMonDossierWeb() throws PortalException {
		setConfigActions(Config.getInstance());	
	}
	
	/** 
	 *  Receive static channel data from the portal.
	 *  Satisfies implementation of IChannel Interface.
	 *
	 *  @param <b>ChannelStaticData</b> sd static channel data
	 * @throws PortalException
	 */
	public void setStaticData(ChannelStaticData sd) throws PortalException {
		super.setStaticData(sd);
		// Gestion BD indisponible
		Query qry = null;
		boolean error = false;
		try {
			qry = Config.getInstance().getConnexionDefault();
			qry.setSql("select * from dual");
		}
		catch (Exception e) {
			error = true;
			log.error("CMonDossierWeb::setStaticData : " + e);
		}
		finally {
			qry.close();
			if (error)
				FatalError.fatalError(this, Config.getInstance().getMsgErreurBD());
		}
		userId = (String) staticData.getPerson().getAttribute(IPerson.USERNAME);
		cod_ind = getCodInd();
		loginEtu = getLoginEtu();
		// instanciation de la classe gérant les mails étudiants
		try {
			Class MailClass = Class.forName(SubChannel.getPackageName(this.getClass()) + "." + Config.getInstance().getClassMail());
			mailObj = (IMail) MailClass.newInstance();
		}
		catch (Exception e) {
			log.error("CMonDossierWeb::setStaticData() : Erreur " + e);
		}
	}
	
	public void setRuntimeData(ChannelRuntimeData rd) throws PortalException  {
		// gestion du back navigateur pour le servant CMailToServantFinished
		if (rd.getParameter("action") != null && !rd.getParameter("action").equals("sendMail")
			&& currentAction != null && currentAction.getName().equals("sendMail")) {
			rd.setParameter("CMailToServantFinished","1");
			currentAction.setServantfinish(rd.getParameter("action"));
		}
		if (rd.getParameter("action") != null && rd.getParameter("action").equals("sendMail")) {	
			// Destinataire du canal MailTo
			String[] att = new String[3];
			// la scolarité dans le cas du lien "Nous contacter"
			if (runtimeData.getParameter("dest") != null && runtimeData.getParameter("dest").equals("scolarite")) 
				att[0] = Config.getInstance().getAdrContact();
	        else // le titulaire du dossier consulté dans le cas du lien sur le mail
				att[0] = mailObj.getMail(loginEtu);
	        att[1] = "false";
	        att[2] = "true";
	        runtimeData.setParameterValues(toField,att);
		}
		super.setRuntimeData(rd);
	}
  
	/**
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Récupération du cod_ind 
	 * 
	 */
	public String getCodInd() {
		if (cod_ind == null) {
			// on récupère le cod_ind via le connecteur Apogee
			try {
				cod_ind = ApogeeConnector.getCOD_IND(getStaticData().getPerson());
				// pour test 
				// cod_ind = "29701020"; // V. Jeannot pour VAC
			} 
			catch (ConnectorException e1) {
				// enregistrement plugin fatalError
				FatalError.fatalError(this, Config.getInstance().getMsgInterdit());
				return null;
			}
		}
		return cod_ind;
	}
	
	
	/**
	 * Méthode qui retourne les infos principales d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlEtudiant() throws FrameWorkException {
		Query qry = null;
		boolean error = false;
		StringBuffer res = null;
		if (xmlEtudiant == null) {
			res = new StringBuffer("<etudiant>");
			//	Connexion à la base et exécution d'une requête
			try {
				// on récupère le cod_ind via le connecteur Apogee
				cod_ind = getCodInd(); 
				if (cod_ind != null) { // la personne connectée à un cod_ind
					qry = Config.getInstance().getConnexionDefault();
					qry.setSql("select i.cod_etu,i.cod_nne_ind,i.cod_cle_nne_ind,i.lib_nom_pat_ind,i.lib_nom_usu_ind," +
								"i.lib_pr1_ind,i.lib_pr2_ind,i.lib_pr3_ind," + 
								"i.daa_ent_etb, i.cod_etb " +
								" from apogee.individu i " +
								" where i.cod_ind = ? ");
					qry.getStmt().setString(1, cod_ind);				
					qry.select();			   
					while (qry.getRs().next()){
						res.append("<dossier>").append(XMLEscaper.escape(qry.getRs().getString("cod_etu"))).append("</dossier>").
						append("<NNE>");
						if (qry.getRs().getString("cod_nne_ind") != null) {
							res.append(qry.getRs().getString("cod_nne_ind"));
                                        		if (qry.getRs().getString("cod_cle_nne_ind") != null) {
								res.append(qry.getRs().getString("cod_cle_nne_ind"));
							}
						}
						res.append("</NNE>").
							append("<nom>").append(XMLEscaper.escape(qry.getRs().getString("lib_nom_pat_ind"))).append("</nom>").
							append("<prenom>").append(XMLEscaper.escape(qry.getRs().getString("lib_pr1_ind"))).append("</prenom>");
						addOptionEtaCivilToXml(qry,res);	
						res.append("<email>");
						if (getMailObj() != null) {
							res.append(XMLEscaper.escape(mailObj.getMail(loginEtu)));
						}	
						res.append("</email>");
					}
				}
			} 
			catch (SQLException e) {
				log.error("CMonDossierWeb::getXmlEtudiant() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("CMonDossierWeb::getXmlEtudiant() : Erreur " + e);
				error = true;
			}
			finally {
				qry.close();
				if (error)
					throw new FrameWorkException();
			}
			res.append("</etudiant>");
		}
		return res;
	}
	
	/** 
	 *  Initialise les static et les runtime data pour le servant
	 *  @param <b>IServant</b> servant servant à instancier
	 */
	public void initServant(IServant servant) throws PortalException {
		// Servant Envoi de mails
		// Destinataire
		String[] att = new String[3];
		// la scolarité dans le cas du lien "Nous contacter"
		if (runtimeData.getParameter("dest") != null && runtimeData.getParameter("dest").equals("scolarite")) 
			att[0] = Config.getInstance().getAdrContact();
        else // le titulaire du dossier consulté dans le cas du lien sur le mail
			att[0] = mailObj.getMail(loginEtu);
        att[1] = "false";
        att[2] = "true";
        runtimeData.setParameterValues(toField,att);
        // Les champs copies sont désactivés
        att = new String[3];
        att[0] = "";
        att[1] = "false";
        att[2] = "false";
        runtimeData.setParameterValues(ccField,att);
        att = new String[3];
        att[0] = "";
        att[1] = "false";
        att[2] = "false";
        runtimeData.setParameterValues(bccField,att);
        // Initialisation du contenu du mail
        if (getActionParam("sendMail", "content") !=  null 
        		&& runtimeData.getParameter("dest") == null) {   
	        att = new String[3];
	        att[0] = getActionParam("sendMail", "content").getValue();
	        att[1] = "true";
	        att[2] = "true";
	        runtimeData.setParameterValues(contentField,att);
        }
        // on fixe dynamiquement l'action de retour en fonction de l'action appelante
        if (getPreviousAction() != null) {
        	if (getPreviousAction().getName().equals("detailNotesPDF"))
        		getCurrentAction().setServantfinish("detailNotes");
        	else if (getPreviousAction().getName().equals("notesPDF"))
        		getCurrentAction().setServantfinish("notes");
        	else
            	getCurrentAction().setServantfinish(getPreviousAction().getName());
        }
		super.initServant(servant);
	}
		
	public void renderXML(ContentHandler out) throws PortalException {
		// on passe à toutes les subchannel non servant le fait qu'on utilise ou pas le lien "Nous contacter"
		if (!currentAction.isServant() && Config.getInstance().getAdrContact() != null) 
			currentSubChannel.getXSLParameter().put("contact",Config.getInstance().getAdrContact());
		if (this.getAction("sendMail") != null)
			currentSubChannel.getXSLParameter().put("servantMailTo","1");
		super.renderXML(out);
	}	
	
	/**
	 * @return Returns the loginEtu.
	 */
	public String getLoginEtu() {
		if (loginEtu == null)
			loginEtu = userId;
		return loginEtu;
	}
	
	/**
	 * @return Returns the mailObj.
	 */
	public IMail getMailObj() {
		return mailObj;
	}
	/**
	 * @param mailObj The mailObj to set.
	 */
	public void setMailObj(IMail mailObj) {
		this.mailObj = mailObj;
	}

	/**
	 * Ajoute les résultats des options de l'action EtatCivil au flux xml 
	 * @param qry
	 * @param xml
	 * @throws SQLException
	 */
	public void addOptionEtaCivilToXml(Query qry,StringBuffer xml) throws SQLException {
		if (getActionParam("etat_civil","option") != null) {
			List listeOption = getActionParam("etat_civil","option").getValues();
			// Prénom 2
			String lib_pr2_ind = qry.getRs().getString("lib_pr2_ind");
			if (listeOption.contains("prenom2") && lib_pr2_ind != null) {
				xml.append("<prenom2>").
				append(XMLEscaper.escape(lib_pr2_ind)).
				append("</prenom2>");
			}
			// Prénom 3
			String lib_pr3_ind = qry.getRs().getString("lib_pr3_ind");
			if (listeOption.contains("prenom3") && lib_pr3_ind != null) {
				xml.append("<prenom3>").
				append(XMLEscaper.escape(lib_pr3_ind)).
				append("</prenom3>");
			}	
			// Nom d'usage
			String lib_nom_usu_ind = qry.getRs().getString("lib_nom_usu_ind");
			if (listeOption.contains("nom_usage") && lib_nom_usu_ind != null) {
				xml.append("<nom_usage>").
				append(XMLEscaper.escape(lib_nom_usu_ind)).
				append("</nom_usage>");
			}
		}
	}
}
