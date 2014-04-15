package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.portal.utils.channels.FrameWorkException;
import org.esupportail.portal.utils.channels.MainChannel;
import org.esupportail.portal.utils.channels.SubChannel;
import org.esupportail.portal.utils.database.Query;
import org.jasig.portal.utils.XMLEscaper;

/**
 * Adresses<br>
 * <br>
 * Gestion de l'affichage des adresses d'un étudiant <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.2
 * 
 */
public class Adresses extends SubChannel {
	
	// Classe principale de la channel
	private CMonDossierWeb owner;
	// log
	private static final Log log = LogFactory.getLog(Adresses.class);
	private StringBuffer xml;
	
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public Adresses(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}

	public Boolean setXML() throws FrameWorkException {
		if (xml == null) {
			xml = new StringBuffer(START_XML);
			if (Config.getInstance().getRappelIdentite())
				xml.append(owner.getXmlEtudiant());
			xml.append(getXmlAdrAnnu());
			xml.append(getXmlAdrFixe());
			xml.append(END_XML);
		}
		setXML(xml.toString());
		return Boolean.TRUE;
	}
		
	/**
	 * Méthode qui retourne l'adresse annuelle d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlAdrAnnu() throws FrameWorkException {
		StringBuffer res = new StringBuffer("<adrAnnuelle>");
		Query qry = null;
		boolean error = false;
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("select a.lib_ad1,a.lib_ad2,a.lib_ad3,a.num_tel, " +
							"decode(a.lib_ade,NULL,a.cod_bdi || ' - ' ||cb.lib_ach,a.lib_ade) \"VILLE\", " + 
							"p.lib_pay \"PAYS\", au.lic_anu " +
							"from apogee.adresse a,apogee.com_bdi cb,apogee.pays p, apogee.annee_uni au " + 
							"where cod_ind_ina = ? and cod_anu_ina = ? " +  
							"and   cb.cod_com (+) = a.cod_com and   cb.cod_bdi (+) = a.cod_bdi " +  
							"and   p.cod_pay = a.cod_pay " + 
							"and   cod_anu_ina = au.cod_anu");
				qry.getStmt().setString(1, owner.getCodInd());
				qry.getStmt().setString(2, getLastAnuInsc(owner.getCodInd()));
				qry.select();			  
				String lib_ad2 = null; 
				String lib_ad3 = null; 
				String num_tel = null;
				while (qry.getRs().next()){
					lib_ad2 = (qry.getRs().getString("lib_ad2") == null) ? "" : qry.getRs().getString("lib_ad2");
					lib_ad3 = (qry.getRs().getString("lib_ad3") == null) ? "" : qry.getRs().getString("lib_ad3");
					num_tel = (qry.getRs().getString("num_tel") == null) ? "" : qry.getRs().getString("num_tel");
					res.append("<adresse1>").append(XMLEscaper.escape(qry.getRs().getString("lib_ad1"))).append("</adresse1>").
						append("<adresse2>").append(XMLEscaper.escape(lib_ad2)).append("</adresse2>");
					res.append("<adresse3>").append(XMLEscaper.escape(lib_ad3)).append("</adresse3>");
					res.append("<ville>").append(XMLEscaper.escape(qry.getRs().getString("VILLE"))).append("</ville>").
						append("<pays>").append(XMLEscaper.escape(qry.getRs().getString("PAYS"))).append("</pays>"). 
						append("<tel>").append(XMLEscaper.escape(num_tel)).append("</tel>").
						append("<anu>").append(qry.getRs().getString("lic_anu")).append("</anu>");   
				}
			}
			catch (SQLException e) {
				log.error("Adresses::getXmlAdrAnnu() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Adresses::getXmlAdrAnnu() : Erreur " + e);
				error = true;
			}
			finally {
				qry.close();
				if (error)
					throw new FrameWorkException();
			}
		} 
		res.append("</adrAnnuelle>");
		return res;
	}
	
	/**
	 * Retourne le cod_ind de la dernière année d'inscription administratives d'une personne
	 * @param cod_ind
	 * @return
	 */
	public String getLastAnuInsc(String cod_ind) throws FrameWorkException {
		String res = "";
		Query qry = null;
		boolean error = false;
		if (cod_ind != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("select max(cod_anu) as COD_ANU from apogee.ins_adm_anu " + 
							"where cod_ind = ? ");
				qry.getStmt().setString(1, cod_ind);
				qry.select();			  
				while (qry.getRs().next()){
					res = qry.getRs().getString("COD_ANU");
				}
			}
			catch (SQLException e) {
				log.error("Adresses::getLastAnuInsc() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Adresses::getLastAnuInsc() : Erreur " + e);
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
	 * Méthode qui retourne l'adresse fixe d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlAdrFixe() throws FrameWorkException {
		StringBuffer res = new StringBuffer("<adrFixe>");
		Query qry = null;
		boolean error = false;
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("select   a.lib_ad1,a.lib_ad2,a.lib_ad3,a.num_tel," +
   						   "decode(a.lib_ade,NULL,a.cod_bdi || ' - ' ||cb.lib_ach,a.lib_ade) \"VILLE\"," +
   						   "p.lib_pay \"PAYS\" " +
   						   "from apogee.adresse a,apogee.com_bdi cb,apogee.pays p " +
   						   "where cod_ind = ? and cb.cod_com (+) = a.cod_com " +
   						   "and   cb.cod_bdi (+) = a.cod_bdi and   p.cod_pay = a.cod_pay");
				qry.getStmt().setString(1, owner.getCodInd());
				qry.select();			   
				String lib_ad2 = null;
				String num_tel = null;
				while (qry.getRs().next()){
					lib_ad2 = (qry.getRs().getString("lib_ad2") == null) ? "" : qry.getRs().getString("lib_ad2");
					num_tel = (qry.getRs().getString("num_tel") == null) ? "" : qry.getRs().getString("num_tel");
					res.append("<adresse1>").append(XMLEscaper.escape(qry.getRs().getString("lib_ad1"))).append("</adresse1>").
						append("<adresse2>").append(XMLEscaper.escape(lib_ad2)).append("</adresse2>").
						append("<ville>").append(XMLEscaper.escape(qry.getRs().getString("VILLE"))).append("</ville>").
						append("<pays>").append(XMLEscaper.escape(qry.getRs().getString("PAYS"))).append("</pays>"). 
						append("<tel>").append(XMLEscaper.escape(num_tel)).append("</tel>");   
				}
			}
			catch (SQLException e) {
				log.error("Adresses::getXmlAdrFixe() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Adresses::getXmlAdrFixe() : Erreur " + e);
				error = true;
			}
			finally {
				qry.close();
				if (error)
					throw new FrameWorkException();
			}
		} 
		res.append("</adrFixe>");
		return res;
	}
	
}