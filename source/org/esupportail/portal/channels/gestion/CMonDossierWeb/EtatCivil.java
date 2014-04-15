package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portal.utils.XMLEscaper;
import org.esupportail.portal.utils.channels.FrameWorkException;
import org.esupportail.portal.utils.channels.MainChannel;
import org.esupportail.portal.utils.channels.SubChannel;
import org.esupportail.portal.utils.database.Query;

/**
 * Etat civil<br>
 * <br>
 * Gestion de l'affichage de l'état civil d'un étudiant <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.2
 * 
 */
public class EtatCivil extends SubChannel  {

	//Rmq : Les méthodes non documentées sont héritées de la classe SubChannel, et surchargées ici
	
	// Classe principale de la channel
	protected CMonDossierWeb owner;
	// log
	private static final Log log = LogFactory.getLog(EtatCivil.class);
	
	// nationalité
	//private String nationalite;
	// date de naissance
	private String datNaiss;
	// lieu de naissance
	private String lieu;
	// département
	private String depPays;
	// code du pays
	private String codPays;
	// code type dep/pays
	private String codTypDepPays;
	// code ETB
	private String codETB;
	// date de première inscription
	private String datDebInscr;
	// flux xml : calculé qu'une seul fois, réaffiché ensuite
	private StringBuffer xml; 
		
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public EtatCivil(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
		
	public Boolean setXML() throws FrameWorkException {
		if (xml == null) {
			xml = new StringBuffer(START_XML);
			xml.append(getXmlGeneralites());
			xml.append(getXmlNaissance());
			xml.append(getXmlInscrUniv());
			xml.append(getXmlBac());
			xml.append(END_XML);		
		}
		setXML(xml.toString());
		return Boolean.TRUE;
	}
	
	/**
	 * Méthode qui retourne les inscriptions d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlGeneralites() throws FrameWorkException {
		StringBuffer res = new StringBuffer("<generalites>");
		Query qry = null;
		boolean error = false;
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("select i.cod_etu,i.cod_nne_ind,i.cod_cle_nne_ind,i.lib_nom_pat_ind,i.lib_nom_usu_ind, "+
						   "i.lib_pr1_ind,i.lib_pr2_ind,i.lib_pr3_ind, " + 	
						   "to_char(i.date_nai_ind,'DD/MM/YYYY') \"DATE_NAI_IND\", i.lib_vil_nai_etu, "+
							"i.cod_dep_pay_nai, i.daa_ent_etb, i.cod_typ_dep_pay_nai, i.cod_pay_nat, i.cod_etb "+ 
				 			"from apogee.individu i " + 
				 			"where i.cod_ind = ? "); //  23001316
				qry.getStmt().setString(1, owner.getCodInd());				
				qry.select();			   
				while (qry.getRs().next()){
					res.append("<dossier>").
					append(XMLEscaper.escape(qry.getRs().getString("cod_etu"))).
					append("</dossier>").
					append("<NNE>");
					if (qry.getRs().getString("cod_nne_ind") != null) {
						res.append(qry.getRs().getString("cod_nne_ind"));
						if (qry.getRs().getString("cod_cle_nne_ind") != null) {
							res.append(qry.getRs().getString("cod_cle_nne_ind"));
						}
					}
					res.append("</NNE>").
					append("<nom>").
					append(XMLEscaper.escape(qry.getRs().getString("lib_nom_pat_ind"))).
					append("</nom>").
					append("<prenom>").
					append(XMLEscaper.escape(qry.getRs().getString("lib_pr1_ind"))).
					append("</prenom>");
					owner.addOptionEtaCivilToXml(qry,res);
					res.append("<email>");
					if (owner.getMailObj() != null) 
						res.append(XMLEscaper.escape(owner.getMailObj().getMail(owner.getLoginEtu())));
					res.append("</email>"); 
					this.codPays = qry.getRs().getString("cod_pay_nat");
					this.datNaiss = qry.getRs().getString("date_nai_ind");
					this.lieu = qry.getRs().getString("lib_vil_nai_etu");
					this.depPays = qry.getRs().getString("cod_dep_pay_nai"); 
					this.codTypDepPays = qry.getRs().getString("cod_typ_dep_pay_nai");
					this.datDebInscr = qry.getRs().getString("daa_ent_etb");
					this.codETB = qry.getRs().getString("cod_etb");
				}
			}
			catch (SQLException e) {
				log.error("EtatCivil::getXmlGeneralites() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("EtatCivil::getXmlGeneralites() : Erreur " + e);
				error = true;
			}
			finally {
				qry.close();
				if (error)
					throw new FrameWorkException();
			}
		} 
		res.append("</generalites>");
		return res;
	}
	
	/**
	 * Méthode qui retourne les informations de naissance d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlNaissance() throws FrameWorkException {
		StringBuffer res = new StringBuffer("<naissance>");
		Query qry = null;
		boolean error = false;
		try {
			qry = Config.getInstance().getConnexionDefault();
			//nationalité
			qry.setSql("select LIB_NAT from apogee.pays where cod_pay = ?");
			qry.getStmt().setString(1, this.codPays);				
			qry.select();
			while (qry.getRs().next()){
				res.append("<nationalite>").append(qry.getRs().getString("lib_nat")).append("</nationalite>");				
			}
			// Recherche Pays ou Département de Naissance
	  	    if (this.codTypDepPays.equals("D"))  
				qry.setSql("select LIB_DEP \"LIB_DEP_PAY\" from apogee.departement where cod_dep = ?");
	  	    else 
				qry.setSql("select LIB_PAY \"LIB_DEP_PAY\" from apogee.pays where cod_pay = ?");
	  	    qry.getStmt().setString(1, this.depPays);
			qry.select();
			while (qry.getRs().next()){
				res.append("<depPays>").append(qry.getRs().getString("lib_dep_pay")).append("</depPays>");				
			} 
		} 
		catch (SQLException e) {
			log.error("EtatCivil::getXmlNaissance() : Erreur SQL " + e);
			error = true;
		}
		catch (Exception e) {
			log.error("EtatCivil::getXmlNaissance() : Erreur " + e);
			error = true;
		}
		finally {
			qry.close();
			if (error)
				throw new FrameWorkException();
		}					
		res.append("<datNaiss>").append(XMLEscaper.escape(this.datNaiss)).append("</datNaiss>");
		res.append("<lieu>").append(XMLEscaper.escape(this.lieu)).append("</lieu>");
		res.append("</naissance>");
		return res;
	}	
	
	/**
	 * Méthode qui retourne la première inscription d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlInscrUniv() throws FrameWorkException {
		StringBuffer res = new StringBuffer("<inscrUniv>");
		Query qry = null;
		boolean error = false;
		try {
			qry = Config.getInstance().getConnexionDefault();
			qry.setSql("select LIB_ETB from apogee.etablissement where cod_etb = ?");
			qry.getStmt().setString(1, this.codETB);				
			qry.select();
			while (qry.getRs().next()){
				res.append("<etablissement>").append(XMLEscaper.escape(qry.getRs().getString("lib_etb"))).append("</etablissement>");				
			}
		} 
		catch (SQLException e) {
			log.error("EtatCivil::getXmlInscrUniv() : Erreur SQL " + e);
			error = true;
		}
		catch (Exception e) {
			log.error("EtatCivil::getXmlInscrUniv() : Erreur " + e);
			error = true;
		}
		finally {
			qry.close();
			if (error)
				throw new FrameWorkException();
		}					
		res.append("<annee>").append(XMLEscaper.escape(this.datDebInscr)).append("</annee>");
		res.append("</inscrUniv>");
		return res;
	}	
		
	/**
	 * Méthode qui retourne la liste des bacs sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlBac() throws FrameWorkException {
		String cod_mnb;
		String cod_tpe;
		String cod_etb;
		StringBuffer res = new StringBuffer("<bacs>");
		Query qry = null;
		boolean error = false;
		try {
			qry = Config.getInstance().getConnexionDefault();
			qry.setSql("select b.lib_bac,ib.daa_obt_bac_iba,ib.cod_mnb,ib.cod_tpe,ib.cod_etb ,ib.cod_dep "+ 
					   "from apogee.bac_oux_equ b, apogee.ind_bac ib "+
					   "where ib.cod_ind = ? and b.cod_bac = ib.cod_bac ");
			qry.getStmt().setString(1,owner.getCodInd());				
			qry.select();
			Query qry1 = null;
			while (qry.getRs().next()){
				// initialisation
				cod_mnb = "";
				cod_tpe = "";
				cod_etb = "";
				cod_mnb = qry.getRs().getString("cod_mnb");
				cod_tpe = qry.getRs().getString("cod_tpe");
				cod_etb = qry.getRs().getString("cod_etb");
				res.append("<bac>");
				// type
				res.append("<type>").append(XMLEscaper.escape(qry.getRs().getString("lib_bac"))).append("</type>");
				// date obtention
				res.append("<datObtention>").append(XMLEscaper.escape(qry.getRs().getString("daa_obt_bac_iba"))).append("</datObtention>");
				// département
				res.append("<departement>").append(XMLEscaper.escape(qry.getRs().getString("cod_dep"))).append("</departement>");
				try {
					qry1 = Config.getInstance().getConnexionDefault();
					// mention
					qry1.setSql("select lib_men from apogee.mention where cod_men = ?");
					qry1.getStmt().setString(1, cod_mnb);
					qry1.select();
					while (qry1.getRs().next()) {
						res.append("<mention>").append(XMLEscaper.escape(qry1.getRs().getString("lib_men"))).append("</mention>");
					}
					// type etablissement
					qry1.setSql("select lib_tpe from apogee.typ_etb where cod_tpe = ?");
					qry1.getStmt().setString(1, cod_tpe);
					qry1.select();
					while (qry1.getRs().next()) {
						res.append("<typEtabliss>").append(XMLEscaper.escape(qry1.getRs().getString("lib_tpe"))).append("</typEtabliss>");
					}
					// Type Etablissement Bac
					qry1.setSql("select lib_etb from apogee.etablissement "+
		   						"where cod_tpe = ? and cod_etb = ? ");
					qry1.getStmt().setString(1, cod_tpe);
					qry1.getStmt().setString(2, cod_etb);
					qry1.select();
					while (qry1.getRs().next()) {
						res.append("<etablissement>").append(XMLEscaper.escape(qry1.getRs().getString("lib_etb"))).append("</etablissement>");
					}	
				}
				finally {
					qry1.close();
				}
				res.append("</bac>");				
			}
		} 
		catch (SQLException e) {
			log.error("EtatCivil::getXmlBac() : Erreur SQL " + e);
			error = true;
		}
		catch (Exception e) {
			log.error("EtatCivil::getXmlBac() : Erreur " + e);
			error = true;
		}
		finally {
			qry.close();
			if (error)
				throw new FrameWorkException();
		}				
		res.append("</bacs>");
		return res;
	}

	/**
	 * @return Renvoie owner.
	 */
	public CMonDossierWeb getOwner() {
		return owner;
	}

	/**
	 * @param owner owner à définir.
	 */
	public void setOwner(CMonDossierWeb owner) {
		this.owner = owner;
	}		
	
}	
