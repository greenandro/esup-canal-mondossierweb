/*
 ESUP-portail is a french academic project developed under the GPL (General Public License) augmented according to the following :
 A binary or source file developped by ESUP-portail can be used or compiled with products under Apache license.
 The official english text of the GPL can be found here : http://www.gnu.org/licenses/gpl.html .
 A non official french translation can be found here : http://www.linux-France.org/article/these/gpl.html .
 The different kinds of licenses governing the products developed by the Apache foundation can be found here : http://www.apache.org/licenses .
 It follows that you can as well as use download contents as well modify and redistribute them provided you respect the GPL terms.
 Downloading and using such contents do not provide any guaranty.
 Be sure that you have well understood the terms of the license before using the contents it covers.
 The ESUP-portail distribution includes the following distributions :
 * UPortal :
 software developed by JA-SIG (Java Architecture - Special Interest Group)
 You can find the license page here : http://mis105.udel.edu/ja-sig/uportal/license.html
 * CAS :
 SSO solution developed by Yale University
 You can find the project page here : http://www.yale.edu/tp/auth
 * Cocoon :
 XML framework distributed by the Apache foundation under Apache license;
 Please find the full text here : http://cocoon.apache.org/2.1/license.html
 * Mod_dav:
 A DAV module for Apache web server
 You can find the project page here : http://www.webdav.org/mod_dav
 * IMP :
 webmail from Horde application framework
 You can find the project page here : http://www.horde.org
 * �. To be completed
 */

package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import java.sql.SQLException;
import org.esupportail.portal.utils.channels.FrameWorkException;
import org.esupportail.portal.utils.channels.MainChannel;
import org.esupportail.portal.utils.channels.SubChannel;
import org.esupportail.portal.utils.database.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Calendriers<br>
 * <br>
 * Gestion de l'affichage du calendrier des �preuves d'un �tudiant <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">C�dric Champmartin</a>
 * @version 1.0
 * 
 */
public class CalRent extends SubChannel {
	
	// log
	private static final Log log = LogFactory.getLog(CalRent.class);
	//Classe principale de la channel
	private CMonDossierWeb owner;
	private String xml;
	
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public CalRent(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
	
	public Boolean setXML() throws FrameWorkException {
		
		if (xml == null) {
			xml = START_XML;
			xml += getXmlEtapes();
			xml += END_XML;
			log.debug("CalRent::xml genere : "+xml);
		}
		setXML(xml);
		
		return Boolean.TRUE;
	}
	
	/**
	 * M�thode qui retourne les examens d'un �tudiant sous forme d'un flux xml 
	 * 
	 */
	public String getXmlEtapes() throws FrameWorkException {
		
		StringBuffer res = new StringBuffer("<etapes>");
		Query qry = null;
		boolean error = false;
		if (owner.getCodInd() != null) { // la personne connect�e � un cod_ind
			//on va chercher l'ann�e scolaire
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("SELECT Max(ANNEE_UNI.COD_ANU) AS COD_ANU FROM APOGEE.ANNEE_UNI ANNEE_UNI WHERE (ANNEE_UNI.ETA_ANU_IAE In ('I','O'))");
				qry.select();
				qry.getRs().next();
				//variable de r�sultat
				String annee = qry.getRs().getString("COD_ANU");
				Query qry2 = null;
				try{
					qry2 = Config.getInstance().getConnexionDefault(); 
					qry2.setSql("SELECT INDIVIDU.LIB_NOM_PAT_IND,INDIVIDU.LIB_PR1_IND, VERSION_ETAPE.COD_ETP, VERSION_ETAPE.COD_VRS_VET, LIB_WEB_VET AS LIB_ETP FROM APOGEE.INDIVIDU INDIVIDU, APOGEE.INS_ADM_ETP INS_ADM_ETP, DEVAPO.REN1_CAL_RENT REN1_CAL_RENT, APOGEE.VERSION_ETAPE VERSION_ETAPE WHERE INS_ADM_ETP.COD_ETP = VERSION_ETAPE.COD_ETP AND INS_ADM_ETP.COD_VRS_VET = VERSION_ETAPE.COD_VRS_VET AND INDIVIDU.COD_IND = INS_ADM_ETP.COD_IND AND REN1_CAL_RENT.COD_ETP = VERSION_ETAPE.COD_ETP AND REN1_CAL_RENT.COD_VRS_VET = VERSION_ETAPE.COD_VRS_VET AND ((INS_ADM_ETP.COD_ANU=?) AND (INDIVIDU.COD_IND=?) AND (REN1_CAL_RENT.TEM_AFF_RENT='O'))");
					qry2.getStmt().setString(1,annee);
					qry2.getStmt().setString(2,owner.getCodInd());
					qry2.select();
					while (qry2.getRs().next()) {
						res.append("<etape>");
						res.append("<LIB_NOM_PAT_IND>"+qry2.getRs().getString("LIB_NOM_PAT_IND")+"</LIB_NOM_PAT_IND>");
						res.append("<LIB_PR1_IND>"+qry2.getRs().getString("LIB_PR1_IND")+"</LIB_PR1_IND>");
						res.append("<etp>"+qry2.getRs().getString("COD_ETP")+"</etp>");
						res.append("<vet>"+qry2.getRs().getString("COD_VRS_VET")+"</vet>");
						res.append("<libEtp>"+qry2.getRs().getString("LIB_ETP")+"</libEtp>");
						res.append("</etape>");
						
					}
				}
				catch(SQLException e){
					log.error("CalRent::getXmlEtapes() : Erreur SQL qry2 " + e);							
				}
				finally{
					qry2.close();
				}
			}
			catch (SQLException e){
			    log.error("CalRent::getXmlEtapes() : Erreur SQL qry " + e);
			}
			finally{
				qry.close();
			}
		} 
		res.append("</etapes>");
		
		return res.toString();
	}
}
