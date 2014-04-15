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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.portal.channels.gestion.CMonDossierWeb.Config;
import org.esupportail.portal.utils.channels.FrameWorkException;
import org.esupportail.portal.utils.channels.MainChannel;
import org.esupportail.portal.utils.channels.SubChannel;
import org.esupportail.portal.utils.database.Query;

/**
 * Notes<br>
 * <br>
 * Gestion de l'affichage des notes d'un étudiant <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.0
 * 
 */
public class Resultat extends SubChannel {
	
		// log
	private static final Log log = LogFactory.getLog(Resultat.class);
	//Classe principale de la channel
	private CMonDossierWeb owner;
	// indique si la personne connectée est un enseignant
	private String isProf;
	private String xml;
	private int COD_IND;
	private int COD_RVN;
	private String COD_ANU;
	private int NUM_OCC_OBJ_MNP;
	private int NUM_OCC_OBJ_OBJ_MNP;
	// flux xml correspondant aux informations du diplôme
	private String xmlDiplome;
	
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public Resultat(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
	
	public Boolean setXML() throws FrameWorkException {
		isProf = "N";
		xml = START_XML;
		xml += this.getXmlDiplome();
		xml += END_XML;
		log.debug("Resultat::xml généré : "+xml);
		setXML(xml);
		return Boolean.TRUE;
	}
	
	/**
	 * Méthode qui retourne les résultats aux diplomes sous forme d'un flux xml 
	 * 
	 */
	public String getXmlDiplome() throws FrameWorkException {
		
		StringBuffer res = new StringBuffer("");
		String note_jury = "";
		Query qry = null;   
		try {
			log.debug("Resultat::getXmlDiplome::Appel de getConnexionDefault");
			qry = Config.getInstance().getConnexionDefault();
			log.debug("Resultat::getXmlDiplome::getConnexionDefault Appelee");
			qry.setSql("SELECT COD_IND, COD_RVN, COD_ANU, NUM_OCC_OBJ_MNP, NUM_OCC_OBJ_OBJ_MNP FROM TRAV_EXT_RVM_RES WHERE COD_IND=? AND NUM_OCC_OBJ_OBJ_MNP<0");
			qry.getStmt().setString(1, owner.getCodInd());				
			qry.select();
			while (qry.getRs().next()){
				COD_IND = qry.getRs().getInt("COD_IND");
				COD_RVN = qry.getRs().getInt("COD_RVN");
				COD_ANU = qry.getRs().getString("COD_ANU");
				NUM_OCC_OBJ_MNP = qry.getRs().getInt("NUM_OCC_OBJ_MNP");
				NUM_OCC_OBJ_OBJ_MNP = qry.getRs().getInt("NUM_OCC_OBJ_OBJ_MNP");
				
				Query qry1 = null;   
				try{
					qry1 = Config.getInstance().getConnexionDefault();
					qry1.setSql("SELECT COD_IND, COD_RVN, NUM_OCC_OBJ_MNP, COD_ANU, NUM_OCC_OBJ_OBJ_MNP, COD_ANU_OBJ_MNP, COD_ADM_OBJ_MNP, COD_SES_OBJ_MNP, TYP_OBJ_MNP, COD_OBJ_MNP, COD_VRS_OBJ_MNP, NOT_TRV, TRAV_EXT_RVM_RES.NOT_SUB_TRV, BAR_NOT_TRV, NOT_PNT_JUR_TRV, TRAV_EXT_RVM_RES.COD_MEN, TRAV_EXT_RVM_RES.COD_TRE, LIB_CMT_TRV,  DEC_OBJ_MNP, NUM_RNG_OBJ_MNP, ETA_LCC_OBJ_MNP, NUM_LCC_OBJ_MNP, NBR_RNG_ETU_TRV, NBR_TOT_RNG_TRV, MENTION.LIC_MEN,MENTION.LIB_MEN, TYP_RESULTAT.LIB_TRE,TYP_RESULTAT.LIC_TRE, substr(to_char(to_number(COD_ANU_OBJ_MNP)+1), 3, 2) as FULL_COD_ANU_OBJ_MNP FROM DUAL, MENTION, TYP_RESULTAT,TRAV_EXT_RVM_RES WHERE TRAV_EXT_RVM_RES.COD_MEN = MENTION.COD_MEN(+) AND TRAV_EXT_RVM_RES.COD_IND = ? AND TRAV_EXT_RVM_RES.COD_RVN = ? AND TRAV_EXT_RVM_RES.COD_ANU = ? AND TRAV_EXT_RVM_RES.NUM_OCC_OBJ_MNP = ? AND TRAV_EXT_RVM_RES.NUM_OCC_OBJ_OBJ_MNP = ? AND TRAV_EXT_RVM_RES.cod_tre=TYP_RESULTAT.cod_tre(+) AND sysdate between DAT_DEB_VAL AND DAT_FIN_VAL"
					);
					qry1.getStmt().setInt(1, COD_IND);
					qry1.getStmt().setInt(2, COD_RVN);
					qry1.getStmt().setString(3, COD_ANU);
					qry1.getStmt().setInt(4, NUM_OCC_OBJ_MNP);
					qry1.getStmt().setInt(5, NUM_OCC_OBJ_OBJ_MNP);
					qry1.select();
					while (qry1.getRs().next()){
						res.append("<diplome>");
						res.append("<COD_VRS_OBJ_MNP>"+ qry1.getRs().getInt("COD_VRS_OBJ_MNP")+"</COD_VRS_OBJ_MNP>");
						res.append("<COD_OBJ_MNP>"+ qry1.getRs().getString("COD_OBJ_MNP")+"</COD_OBJ_MNP>");
						res.append("<TYP_OBJ_MNP>"+ qry1.getRs().getString("TYP_OBJ_MNP")+"</TYP_OBJ_MNP>");
						res.append("<COD_IND>"+ qry1.getRs().getInt("COD_IND")+"</COD_IND>");
						res.append("<LIB_CMT_TRV>"+ this.string2xmlString(qry1.getRs().getString("LIB_CMT_TRV"))+"</LIB_CMT_TRV>");
						res.append("<COD_SES_OBJ_MNP>"+ qry1.getRs().getString("COD_SES_OBJ_MNP")+"</COD_SES_OBJ_MNP>");
						res.append("<COD_VRS_OBJ_MNP>"+ qry1.getRs().getInt("COD_VRS_OBJ_MNP")+"</COD_VRS_OBJ_MNP>");
						res.append("<NOT_TRV>"+ qry1.getRs().getString("NOT_TRV")+"</NOT_TRV>");
						res.append("<BAR_NOT_TRV>"+ qry1.getRs().getInt("BAR_NOT_TRV")+"</BAR_NOT_TRV>");
						res.append("<COD_TRE>"+ qry1.getRs().getString("COD_TRE")+"</COD_TRE>");
						res.append("<COD_MEN>"+ qry1.getRs().getString("COD_MEN")+"</COD_MEN>");
						note_jury = qry1.getRs().getString("NOT_PNT_JUR_TRV");
						if(note_jury != null){
						if (note_jury.startsWith(".")){
							note_jury = "0"+qry1.getRs().getString("NOT_PNT_JUR_TRV");
						}
						}
						res.append("<NOT_PNT_JUR_TRV>"+note_jury+"</NOT_PNT_JUR_TRV>");

						res.append("<NBR_RNG_ETU_TRV>"+ qry1.getRs().getString("NBR_RNG_ETU_TRV")+"</NBR_RNG_ETU_TRV>");
						res.append("<NBR_TOT_RNG_TRV>"+ qry1.getRs().getString("NBR_TOT_RNG_TRV")+"</NBR_TOT_RNG_TRV>");
						res.append("<COD_ADM_OBJ_MNP>"+ qry1.getRs().getString("COD_ADM_OBJ_MNP")+"</COD_ADM_OBJ_MNP>");
						res.append("<COD_RVN>"+ qry1.getRs().getInt("COD_RVN")+"</COD_RVN>");
						res.append("<NUM_RNG_OBJ_MNP>"+ qry1.getRs().getInt("NUM_RNG_OBJ_MNP")+"</NUM_RNG_OBJ_MNP>");
						res.append("<LIC_TRE>"+ qry1.getRs().getString("LIC_TRE")+"</LIC_TRE>");
						res.append("<LIC_MEN>"+ qry1.getRs().getString("LIC_MEN")+"</LIC_MEN>");
						res.append("<COD_ANU_OBJ_MNP>"+ qry1.getRs().getString("COD_ANU_OBJ_MNP")+"</COD_ANU_OBJ_MNP>");
						res.append("<COD_ANU>"+ qry1.getRs().getString("COD_ANU")+"</COD_ANU>");
						res.append("<FULL_COD_ANU_OBJ_MNP>"+ qry1.getRs().getString("FULL_COD_ANU_OBJ_MNP")+"</FULL_COD_ANU_OBJ_MNP>");
						res.append("<DEC_OBJ_MNP>"+ qry1.getRs().getInt("DEC_OBJ_MNP")+"</DEC_OBJ_MNP>");
						res.append("</diplome>");
						
					}
				}
				catch (SQLException e){
					log.error("Resultat::getXmlDiplome() : Erreur SQL qry1 " + e);
				}
				finally{
					qry1.close();		
				}
			}
		}
		catch (SQLException e){
			log.error("Resultat::getXmlDiplome() : Erreur SQL qry" + e);		
		}
		finally{
			qry.close();
		}
		return res.toString();
		
	}	
	
	private String string2xmlString(String string){
	    string = string.replaceAll("&","&amp;");
	    string = string.replaceAll("<","&lt;");
	    string = string.replaceAll(">","&gt;");
	    string = string.replaceAll("\"","&quot;");
	    string = string.replaceAll("'","&apos;");
	    
	return string;    
	}
}

