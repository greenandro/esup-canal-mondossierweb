package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.portal.utils.channels.FrameWorkException;
import org.esupportail.portal.utils.channels.MainChannel;
import org.esupportail.portal.utils.channels.SubChannel;
import org.esupportail.portal.utils.database.Query;
import org.jasig.portal.utils.XMLEscaper;


/**
 * Inscription<br>
 * <br>
 * Gestion de l'affichage des inscriptions d'un étudiant <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.2
 * 
 */
public class Inscription extends SubChannel  {

	//Rmq : Les méthodes non documentées sont héritées de la classe SubChannel, et surchargées ici
	
	// Classe principale de la channel
	private CMonDossierWeb owner;
	// log
	private static final Log log = LogFactory.getLog(Inscription.class);
 	// libellé de l'année consultée	
	private String lib_annee = new String();
	private String codETB;
	private String datDebInscr;
	private String etablissementDebut;
	private StringBuffer xml;
	
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public Inscription(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
	
	public Boolean setXML() throws FrameWorkException {
				
		if (xml == null) {
			xml = new StringBuffer(START_XML);
			if (Config.getInstance().getRappelIdentite())
				xml.append(owner.getXmlEtudiant());
			// Etablissement par défaut
			HashMap etablissementDef = getEtablissementDef();
			xml.append("<etablissementDef cod_etb=\"");
			xml.append(XMLEscaper.escape((String)etablissementDef.get("cod_etb")));
			xml.append("\" lib_etb=\"");
			xml.append(XMLEscaper.escape(((String)etablissementDef.get("lib_etb")).toUpperCase()));
			xml.append("\"/>");
			// on récupère les infos sur la première inscription
			premiereInscription();
			xml.append("<inscriptions anneeDebut=\"");
			if (this.datDebInscr == null)
				xml.append("");
			else
				xml.append(XMLEscaper.escape(this.datDebInscr));	 
			xml.append("\" etablissementDebut=\"");
			if (this.etablissementDebut == null)
				xml.append("");
			else
				xml.append(XMLEscaper.escape(this.etablissementDebut));
			xml.append("\">");
			xml.append(getXmlInscriptionIAE());
			xml.append(getXmlInscriptionDAC());
			xml.append("</inscriptions>");
			xml.append(END_XML);
		}
		setXML(xml.toString());
		// passage de paramètre à la feuille de style xsl
		this.getXSLParameter().put("libAnnee", lib_annee.toLowerCase());
				
		return Boolean.TRUE;
	}
	
	/**
	 * Méthode qui fixe l'établissement 1ère inscription
	 * 
	 */
	public void premiereInscription() throws FrameWorkException {
		Query qry = null;
		boolean error = false;
		
		try {
			qry = Config.getInstance().getConnexionDefault();
			qry.setSql("select i.daa_ent_etb, i.cod_etb " +
					   "from apogee.individu i " +
					   "where i.cod_ind= ? ");
			qry.getStmt().setString(1, owner.getCodInd());
			qry.select();
			while (qry.getRs().next()){
				this.datDebInscr = qry.getRs().getString("daa_ent_etb");
				this.codETB = qry.getRs().getString("cod_etb");	
			}
			qry.setSql("select LIB_ETB from apogee.etablissement where cod_etb = ?");
			qry.getStmt().setString(1, this.codETB);				
			qry.select();
			while (qry.getRs().next()){
				this.etablissementDebut = qry.getRs().getString("LIB_ETB");	
			}
		}
		catch (SQLException e) {
			log.error("Inscription::premiereInscription() : Erreur SQL " + e);
			error = true;
		}
		catch (Exception e) {
			log.error("Inscription::premiereInscription() : Erreur " + e);
			error = true;
		}
		finally {
			qry.close();
			if (error)
				throw new FrameWorkException();
		}		
	}
	
	/**
	 * Méthode qui retourne les inscriptions d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlInscriptionIAE() throws FrameWorkException {
		StringBuffer res = new StringBuffer();
		Query qry = null;
		boolean error = false;
		
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("select iae.cod_anu||'/'||to_char(to_number(iae.cod_anu)+1) \"COD_ANU\"," +
   						   "iae.cod_etp, iae.cod_vrs_vet, e.lib_etp, iae.cod_ind " +
   						   "from apogee.ins_adm_etp iae, apogee.etape e " +
   						   // Ajout CC eta_pmt_iae - 04/06/08
   						   "where iae.cod_ind = ? and iae.eta_iae = 'E'and iae.eta_pmt_iae = 'P' and e.cod_etp = iae.cod_etp " +
   						   "order by cod_anu desc,iae.cod_etp");
				qry.getStmt().setString(1, owner.getCodInd());				
				qry.select();			   
				while (qry.getRs().next()){
					res.append("<inscription id =\"IAE\">").
						append("<annee>").append(XMLEscaper.escape(qry.getRs().getString("COD_ANU"))).append("</annee>").
						append("<code>").append(XMLEscaper.escape(qry.getRs().getString("cod_etp"))).append("</code>").
						append("<vers>").append(XMLEscaper.escape(qry.getRs().getString("cod_vrs_vet"))).append("</vers>").
						append("<etape>").append(XMLEscaper.escape(qry.getRs().getString("lib_etp"))).append("</etape>").
						append("</inscription>");
				}
			}
			catch (SQLException e) {
				log.error("Inscription::getXmlInscriptionIAE() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Inscription::getXmlInscriptionIAE() : Erreur " + e);
				error = true;
			}
			finally {
				qry.close();
				if (error)
					throw new FrameWorkException();
			}
		} 
		
		return res;
	
	}
	
	/**
	 * Méthode qui retourne les inscriptions d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlInscriptionDAC() throws FrameWorkException {
		StringBuffer res = new StringBuffer();
		Query qry = null;
		boolean error = false;
		
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("select d.daa_uni_dac||'/'||to_char(to_number(d.daa_uni_dac)+1) \"COD_ANU\"," +
   							"d.cod_dac,d.lib_cmt_dac,e.lib_etb,decode(d.tem_obt_dac,'N','AJOURNE','OBTENU') \"RES\" " +
   							"from apogee.ind_dac d, apogee.etablissement e " +
   							"where d.cod_ind = ? and e.cod_etb = d.cod_etb and e.cod_tpe = d.cod_tpe " +
   							"order by d.daa_uni_dac desc");
				qry.getStmt().setString(1, owner.getCodInd());				
				qry.select();
				String specialite = "";
				while (qry.getRs().next()){
					specialite = (qry.getRs().getString("lib_cmt_dac") == null) ? "" : qry.getRs().getString("lib_cmt_dac");
					res.append("<inscription id =\"DAC\">").
						append("<annee>").append(XMLEscaper.escape(qry.getRs().getString("COD_ANU"))).append("</annee>").
						append("<type>").append(XMLEscaper.escape(qry.getRs().getString("cod_dac"))).append("</type>").
						append("<specialite>").append(XMLEscaper.escape(specialite)).append("</specialite>").
						append("<etablissement>").append(XMLEscaper.escape(qry.getRs().getString("lib_etb"))).append("</etablissement>").
						append("<resultat>").append(XMLEscaper.escape(qry.getRs().getString("RES"))).append("</resultat>").
						append("</inscription>");
				}
			}
			catch (SQLException e) {
				log.error("Inscription::getXmlInscriptionDAC() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Inscription::getXmlInscriptionDAC() : Erreur " + e);
				error = true;
			}
			finally {
				qry.close();
				if (error)
					throw new FrameWorkException();	
			}
		} 
		return res;
	}

	/**
	 * Méthode qui retourne l'établissement par défaut
	 * @return l'établissement par défaut
	 * @throws FrameWorkException
	 */
	public HashMap getEtablissementDef() throws FrameWorkException {
		HashMap res = new HashMap(); 
		Query qry = null;
		boolean error = false;
		try {
			qry = Config.getInstance().getConnexionDefault();
			qry.setSql("select va.par_vap cod_etb, e.lib_etb " + 
					   "from apogee.variable_appli va, etablissement e " +
					   "where COD_VAP = 'ETB_COD' " + 
					   "and va.PAR_VAP = e.COD_ETB");
			qry.select();
			if (qry.getRs().next()){
				res.put("cod_etb",qry.getRs().getString("cod_etb"));
				res.put("lib_etb",qry.getRs().getString("lib_etb"));
			}
		}
		catch (SQLException e) {
			log.error("Inscription::getEtablissementDef() : Erreur SQL " + e);
			error = true;
		}
		finally {
			qry.close();
			if (error)
				throw new FrameWorkException();	
		}
		return res;
	}
	
}
