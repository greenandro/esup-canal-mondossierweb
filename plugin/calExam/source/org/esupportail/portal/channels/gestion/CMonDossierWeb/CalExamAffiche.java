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
public class CalExamAffiche extends SubChannel {
	
	// log
	private static final Log log = LogFactory.getLog(CalExamAffiche.class);
	//Classe principale de la channel
	private CMonDossierWeb owner;
	private String xml;
	
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public CalExamAffiche(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
	
	public Boolean setXML() throws FrameWorkException {
		
		if (xml == null) {
			xml = START_XML;
			xml += getXmlCalendrier();
			xml += END_XML;
			log.debug("CalExamAffiche::xml genere : "+xml);
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
		Query qry2 = null;
		String avant = "";
		String cod_etp = this.getRuntimeData().getParameter("etp");
		int cod_vet = Integer.parseInt(this.getRuntimeData(). getParameter("vet"));
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("SELECT distinct to_char(trunc(R.DAT_DEB_PES) + R.DHH_DEB_PES/24 + R.DMM_DEB_PES/1440, 'HH24:MI') AS HEURE_DEB,decode(E.COD_NEP, 'ORA',null,to_char(trunc(R.DAT_DEB_PES)+ R.DHH_DEB_PES/24 + (R.DMM_DEB_PES+R.DUR_EXA_EPR_PES)/1440, 'HH24:MI')) AS HEURE_FIN,to_char(R.DAT_DEB_PES,'DD/MM/YYYY') AS DAT_DEB,V.LIB_WEB_VET AS LIB_ETP,'O' temoin,R.LIB_EPR,R.COD_ANU,R.LIB_PXA,to_char(R.DAT_MAJ,'DD/MM/YYYY HH24:MI') AS MAJ,R.COD_PES,decode(E.COD_NEP,'ORA','(Oral) ','ECR','(Ecrit) ','IND','(Indétermin&#233;) ','TP','(TP) ','SOU','(Soutenance) ',' ') || E.LIB_TYP_EXE_EPR AS TYPE_EPREUVE FROM VERSION_ETAPE V, REN1_AFF_EPR R, EPREUVE E WHERE (SYSDATE BETWEEN R.DAT_DEB_AFF AND R.DAT_FIN_AFF) AND R.COD_ETP = V.COD_ETP and R.COD_VRS_VET = V.COD_VRS_VET AND (R.COD_ETP=?) AND (R.COD_VRS_VET=?) AND E.COD_EPR=R.COD_EPR ORDER BY TO_DATE(DAT_DEB,'DD/MM/YYYY'),HEURE_DEB");
				qry.getStmt().setString(1, cod_etp );				
				qry.getStmt().setInt(2, cod_vet);				
				qry.select();
				while (qry.getRs().next()){
					String apres = qry.getRs().getString("DAT_DEB")+qry.getRs().getString("LIB_ETP")+qry.getRs().getString("LIB_EPR")+qry.getRs().getString("COD_ANU")+qry.getRs().getString("HEURE_DEB")+qry.getRs().getString("HEURE_FIN");
					//test pour eviter les doublons ayant un cod_pes different
					if (apres.compareTo(avant)!=0){
						res.append("<calendrier>");
						res.append("<DAT_DEB>"+qry.getRs().getString("DAT_DEB")+"</DAT_DEB>");
						res.append("<HEURE_DEB>"+qry.getRs().getString("HEURE_DEB")+"</HEURE_DEB>");
						res.append("<HEURE_FIN>"+qry.getRs().getString("HEURE_FIN")+"</HEURE_FIN>");
						res.append("<LIB_ETP>"+qry.getRs().getString("LIB_ETP")+"</LIB_ETP>");
						res.append("<LIB_EPR>"+qry.getRs().getString("LIB_EPR")+" "+qry.getRs().getString("TYPE_EPREUVE")+"</LIB_EPR>");
						res.append("<COD_ANU>"+qry.getRs().getString("COD_ANU")+"</COD_ANU>");
						res.append("<LIB_PXA>"+qry.getRs().getString("LIB_PXA")+"</LIB_PXA>");
						res.append("<MAJ>"+qry.getRs().getString("MAJ")+"</MAJ>");
						res.append("<COD_PES>"+qry.getRs().getString("COD_PES")+"</COD_PES>");
						res.append("<TYPE_EPREUVE>"+qry.getRs().getString("TYPE_EPREUVE")+"</TYPE_EPREUVE>");
						try{
							qry2 = Config.getInstance().getConnexionDefault();
							qry2.setSql("SELECT DISTINCT REN1_AFF_EPR.COD_ETP,REN1_AFF_EPR_1.LIB_BATIMENT, REN1_AFF_EPR.COD_VRS_VET, REN1_AFF_EPR.LIB_EPR, REN1_AFF_EPR.COD_EPR, REN1_AFF_EPR_1.LIB_SALLE FROM REN1_AFF_EPR REN1_AFF_EPR, REN1_AFF_EPR REN1_AFF_EPR_1 WHERE REN1_AFF_EPR.LIB_EPR = REN1_AFF_EPR_1.LIB_EPR AND REN1_AFF_EPR.DAT_DEB_PES = REN1_AFF_EPR_1.DAT_DEB_PES AND REN1_AFF_EPR_1.DHH_DEB_PES = REN1_AFF_EPR.DHH_DEB_PES AND REN1_AFF_EPR_1.DMM_DEB_PES = REN1_AFF_EPR.DMM_DEB_PES AND REN1_AFF_EPR.COD_VRS_VET = REN1_AFF_EPR_1.COD_VRS_VET AND ((REN1_AFF_EPR.COD_ETP=?) AND (REN1_AFF_EPR.COD_PES=?) AND (REN1_AFF_EPR.COD_VRS_VET=?))");
							qry2.getStmt().setString(1, cod_etp );				
							qry2.getStmt().setString(2, qry.getRs().getString("COD_PES"));				
							qry2.getStmt().setInt(3, cod_vet);				
							qry2.select();
							while (qry2.getRs().next()){
								res.append("<localisation>");
								res.append("<COD_ETP>"+qry2.getRs().getString("COD_ETP")+"</COD_ETP>");
								res.append("<LIB_BATIMENT>"+qry2.getRs().getString("LIB_BATIMENT")+"</LIB_BATIMENT>");
								res.append("<COD_VRS_VET>"+qry2.getRs().getString("COD_VRS_VET")+"</COD_VRS_VET>");
								res.append("<COD_EPR>"+qry2.getRs().getString("COD_EPR")+"</COD_EPR>");
								res.append("<LIB_SALLE>"+qry2.getRs().getString("LIB_SALLE")+"</LIB_SALLE>");
								res.append("</localisation>");
							}
						}catch (SQLException e){
							log.error("CalExamAffiche::getXmlCalendrier() : Erreur SQL qry2 " + e);												
						}finally{
							qry2.close();
						}
						res.append("</calendrier>");
					}//fin du if 
					avant = apres ;
				}//fin du while
			}//fin du try
			catch(SQLException e){
			    log.error("CalExamAffiche::getXmlCalendrier() : Erreur SQL qry " + e);							
			}
			finally{
				qry.close();
			}
		} 
		res.append("</calendriers>");
		
		return res.toString();
	}
}
