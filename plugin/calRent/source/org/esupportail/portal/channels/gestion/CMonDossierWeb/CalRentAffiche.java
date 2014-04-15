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
 * …. To be completed
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
 * Gestion de l'affichage du calendrier des épreuves d'un étudiant <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.0
 * 
 */
public class CalRentAffiche extends SubChannel {
	
	// log
	private static final Log log = LogFactory.getLog(CalRentAffiche.class);
	//Classe principale de la channel
	private CMonDossierWeb owner;
	private String xml;
	
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public CalRentAffiche(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
	
	public Boolean setXML() throws FrameWorkException {
		
		if (xml == null) {
			xml = START_XML;
			xml += getXmlCalendrier();
			xml += END_XML;
			log.debug("CalRentAffiche::xml genere : "+xml);
		}
		setXML(xml);
		
		return Boolean.TRUE;
	}
	
	/**
	 * Méthode qui retourne les calendriers de rentrée d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public String getXmlCalendrier() throws FrameWorkException {
		
		StringBuffer res = new StringBuffer("<calendriers>");
		Query qry = null;
		String cod_etp = this.getRuntimeData().getParameter("etp");
		int cod_vet = Integer.parseInt(this.getRuntimeData().getParameter("vet"));
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("SELECT TO_CHAR(DAT_DEB) AS DAT_DEB, REN1_CAL_RENT.HRE_DEB, REN1_CAL_RENT.MIN_DEB, REN1_CAL_RENT.LIB_LIEU, REN1_CAL_RENT.COMMENTAIRE, VERSION_ETAPE.LIB_WEB_VET AS LIB_ETP, 'O' temoin FROM DEVAPO.REN1_CAL_RENT REN1_CAL_RENT, APOGEE.VERSION_ETAPE VERSION_ETAPE WHERE VERSION_ETAPE.COD_ETP = REN1_CAL_RENT.COD_ETP AND REN1_CAL_RENT.COD_VRS_VET = VERSION_ETAPE.COD_VRS_VET AND ((REN1_CAL_RENT.COD_ETP Is Null) AND (REN1_CAL_RENT.COD_VRS_VET Is Null) OR (REN1_CAL_RENT.COD_ETP=?) AND (REN1_CAL_RENT.COD_VRS_VET=?)) AND sysdate<=(REN1_CAL_RENT.DAT_DEB+450) AND REN1_CAL_RENT.TEM_AFF_RENT = 'O'");
				log.debug("CalRentAffiche::getXmlCalendrier() : 1ere valeur passee a la requete cod_etp = " + cod_etp+" 2eme valeur cod_vet = " + cod_vet);							
				qry.getStmt().setString(1, cod_etp );				
				qry.getStmt().setInt(2, cod_vet);				
				qry.select();
				while (qry.getRs().next()){
					log.debug("CalRentAffiche::getXmlCalendrier() : resultat de la requete : DAT_DEB= " + qry.getRs().getString("DAT_DEB") +" HRE_DEB = " + qry.getRs().getString("HRE_DEB") + " MIN_DEB = " + qry.getRs().getString("MIN_DEB") + " LIB_LIEU = "+qry.getRs().getString("LIB_LIEU")+" COMMENTAIRE = "+qry.getRs().getString("COMMENTAIRE"));							
					res.append("<calendrier>");
					res.append("<DAT_DEB>"+qry.getRs().getString("DAT_DEB")+"</DAT_DEB>");
					res.append("<HRE_DEB>"+qry.getRs().getString("HRE_DEB")+"</HRE_DEB>");
					res.append("<MIN_DEB>"+qry.getRs().getString("MIN_DEB")+"</MIN_DEB>");
					res.append("<LIB_LIEU>"+qry.getRs().getString("LIB_LIEU")+"</LIB_LIEU>");
					res.append("<COMMENTAIRE>"+qry.getRs().getString("COMMENTAIRE")+"</COMMENTAIRE>");
					res.append("<LIB_ETP>"+qry.getRs().getString("LIB_ETP")+"</LIB_ETP>");
					res.append("</calendrier>");
					
				}
			}
			catch(SQLException e){
			    log.error("CalRentAffiche::getXmlCalendrier() : Erreur SQL qry " + e);							
			}
			finally{
				qry.close();
			}
		} 
		res.append("</calendriers>");
		
		return res.toString();
	}
}
