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
public class CalRentPageInfo extends SubChannel {
	
	// log
	private static final Log log = LogFactory.getLog(CalRentPageInfo.class);
	//Classe principale de la channel
	private CMonDossierWeb owner;
	private String xml;
	
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public CalRentPageInfo(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
	
	public Boolean setXML() throws FrameWorkException {
		
		if (xml == null) {
			xml = START_XML;
			xml += getXmlInfo();
			xml += END_XML;
			log.debug("CalRentPageInfo::xml genere : "+xml);
		}
		setXML(xml);
		
		return Boolean.TRUE;
	}
	
	/**
	 * Méthode qui retourne les examens d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public String getXmlInfo() throws FrameWorkException {
		
		StringBuffer res = new StringBuffer("<infos>");
		Query qry = null;
		boolean error = false;
		String cod_etp = this.getRuntimeData().getParameter("etp");
		log.debug("CalRentPageInfo::getXmlInfo valeur de cod_etp : "+cod_etp);
		int cod_vet = Integer.parseInt(this.getRuntimeData().getParameter("vet"));
		log.debug("CalRentPageInfo::getXmlInfo valeur de vet : "+cod_vet);
		String nb_jours_affiche = mainChannel.getActionParam("calRentPageInfo", "nb_jours_affichage").getValue();
		log.debug("CalRentPageInfo::getXmlInfo valeur de nb_jours_affiche : "+nb_jours_affiche);
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("SELECT REN1_AFF_RENT_INFO_MINI.LIB_INF_MINI, 'O' temoin FROM REN1_AFF_RENT_INFO_MINI REN1_AFF_RENT_INFO_MINI, REN1_CAL_RENT REN1_CAL_RENT WHERE REN1_CAL_RENT.COD_CGE = REN1_AFF_RENT_INFO_MINI.COD_CGE AND ((REN1_CAL_RENT.COD_ETP=?) AND (REN1_CAL_RENT.COD_VRS_VET=?) AND sysdate<=(REN1_CAL_RENT.DAT_DEB+?)) AND REN1_CAL_RENT.TEM_AFF_RENT = 'O'");
				qry.getStmt().setString(1, cod_etp );				
				qry.getStmt().setInt(2, cod_vet);				
				qry.getStmt().setString(3, "450");				
				qry.select();
				while (qry.getRs().next()){
					if ((qry.getRs().getString("LIB_INF_MINI") != null) && !(qry.getRs().getString("LIB_INF_MINI").equals(""))){
						res.append("<LIB_INF_MINI>"+qry.getRs().getString("LIB_INF_MINI")+"</LIB_INF_MINI>");
					}else{
						//redirection vers autre action
					}
						res.append("<etp>"+cod_etp+"</etp>");
						res.append("<vet>"+cod_vet+"</vet>");
				}
			}
			catch (SQLException e){
			    log.error("CalRentPageInfo::getXmlInfo() : Erreur SQL qry " + e);
			}
			finally{
				qry.close();
			}
		} 
		res.append("</infos>");
		
		return res.toString();
	}
}
