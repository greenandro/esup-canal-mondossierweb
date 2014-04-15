package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import org.esupportail.portal.channels.gestion.CMonDossierWeb.Config;
import org.esupportail.portal.utils.channels.FrameWorkException;
import org.esupportail.portal.utils.channels.MainChannel;
import org.esupportail.portal.utils.channels.SubChannel;
import org.esupportail.portal.utils.database.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


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
public class DetailResultat extends SubChannel {
	
	// log
	private static final Log log = LogFactory.getLog(DetailResultat.class);
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
	private String LIBELLE;

	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public DetailResultat(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
	
	public Boolean setXML() throws FrameWorkException {
		isProf = "N";
		xml = START_XML;
		xml += this.getXmlDetail();
		xml += END_XML;
		log.debug("DetailResultat::xml généré : "+xml);
		setXML(xml);
		return Boolean.TRUE;
	}
	
	/**
	 * Méthode qui retourne le detail d'un diplomes sous forme d'un flux xml 
	 * 
	 */
	public String getXmlDetail() throws FrameWorkException {
		
		StringBuffer res = new StringBuffer("");
		String note_jury = "";
		Query qry2 = null;   
		Query qry = null;   
		COD_RVN = Integer.parseInt(this.getRuntimeData().getParameter("rvn"));
		COD_IND = Integer.parseInt(this.getRuntimeData(). getParameter("ind"));
		try {
			LIBELLE = URLDecoder.decode(this.getRuntimeData().getParameter("libelle"),"UTF-8");
			res.append("<LIBELLE_ETAPE>"+ LIBELLE +"</LIBELLE_ETAPE>");
			qry2 = Config.getInstance().getConnexionDefault();
			qry2.setSql("SELECT COD_IND, COD_RVN, COD_ANU, NUM_OCC_OBJ_MNP, NUM_OCC_OBJ_OBJ_MNP FROM TRAV_EXT_RVM_RES WHERE COD_RVN = ? AND COD_IND = ? AND NUM_OCC_OBJ_OBJ_MNP>0 ORDER BY NUM_RNG_OBJ_MNP");
			qry2.getStmt().setInt(1, COD_RVN );				
			qry2.getStmt().setString(2, owner.getCodInd());				
			qry2.select();
			Query qry3 = null;   
			while (qry2.getRs().next()){
				COD_IND = qry2.getRs().getInt("COD_IND");
				COD_RVN = qry2.getRs().getInt("COD_RVN");
				COD_ANU = qry2.getRs().getString("COD_ANU");
				NUM_OCC_OBJ_MNP = qry2.getRs().getInt("NUM_OCC_OBJ_MNP");
				NUM_OCC_OBJ_OBJ_MNP = qry2.getRs().getInt("NUM_OCC_OBJ_OBJ_MNP");
				
				try{
					qry3 = Config.getInstance().getConnexionDefault();
					qry3.setSql("SELECT COD_IND, COD_RVN, NUM_OCC_OBJ_MNP, COD_ANU, NUM_OCC_OBJ_OBJ_MNP, COD_ANU_OBJ_MNP, COD_ADM_OBJ_MNP, COD_SES_OBJ_MNP, TYP_OBJ_MNP, COD_OBJ_MNP, COD_VRS_OBJ_MNP, NOT_TRV, TRAV_EXT_RVM_RES.NOT_SUB_TRV, BAR_NOT_TRV, NOT_PNT_JUR_TRV, TRAV_EXT_RVM_RES.COD_MEN, TRAV_EXT_RVM_RES.COD_TRE, LIB_CMT_TRV, DAT_DEB_VAL, DAT_FIN_VAL, DEC_OBJ_MNP, NUM_RNG_OBJ_MNP, ETA_LCC_OBJ_MNP, NUM_LCC_OBJ_MNP, NBR_RNG_ETU_TRV, NBR_TOT_RNG_TRV, MENTION.LIC_MEN,MENTION.LIB_MEN, TYP_RESULTAT.LIB_TRE,TYP_RESULTAT.LIC_TRE, substr(to_char(to_number(COD_ANU_OBJ_MNP)+1), 3, 2) as FULL_COD_ANU_OBJ_MNP FROM MENTION, TYP_RESULTAT,TRAV_EXT_RVM_RES WHERE TRAV_EXT_RVM_RES.COD_MEN = MENTION.COD_MEN(+) AND TRAV_EXT_RVM_RES.COD_TRE = TYP_RESULTAT.COD_TRE(+) AND TRAV_EXT_RVM_RES.COD_IND = ? AND TRAV_EXT_RVM_RES.COD_RVN = ? AND TRAV_EXT_RVM_RES.COD_ANU = ? AND TRAV_EXT_RVM_RES.NUM_OCC_OBJ_MNP = ? AND TRAV_EXT_RVM_RES.NUM_OCC_OBJ_OBJ_MNP = ?");
					qry3.getStmt().setInt(1, COD_IND);
					qry3.getStmt().setInt(2, COD_RVN);
					qry3.getStmt().setString(3, COD_ANU);
					qry3.getStmt().setInt(4, NUM_OCC_OBJ_MNP);
					qry3.getStmt().setInt(5, NUM_OCC_OBJ_OBJ_MNP);
					qry3.select();
					while (qry3.getRs().next()){
						res.append("<detail>");
						res.append("<COD_VRS_OBJ_MNP>"+ qry3.getRs().getInt("COD_VRS_OBJ_MNP")+"</COD_VRS_OBJ_MNP>");
						res.append("<COD_OBJ_MNP>"+ qry3.getRs().getString("COD_OBJ_MNP")+"</COD_OBJ_MNP>");
						res.append("<TYP_OBJ_MNP>"+ qry3.getRs().getString("TYP_OBJ_MNP")+"</TYP_OBJ_MNP>");
						res.append("<COD_IND>"+ qry3.getRs().getInt("COD_IND")+"</COD_IND>");
						res.append("<LIB_CMT_TRV>"+ this.string2xmlString(qry3.getRs().getString("LIB_CMT_TRV"))+"</LIB_CMT_TRV>");
						res.append("<COD_SES_OBJ_MNP>"+ qry3.getRs().getString("COD_SES_OBJ_MNP")+"</COD_SES_OBJ_MNP>");
						res.append("<COD_VRS_OBJ_MNP>"+ qry3.getRs().getInt("COD_VRS_OBJ_MNP")+"</COD_VRS_OBJ_MNP>");
						res.append("<NOT_TRV>"+ qry3.getRs().getString("NOT_TRV")+"</NOT_TRV>");
						res.append("<BAR_NOT_TRV>"+ qry3.getRs().getInt("BAR_NOT_TRV")+"</BAR_NOT_TRV>");
						res.append("<COD_TRE>"+ qry3.getRs().getString("COD_TRE")+"</COD_TRE>");
						res.append("<COD_MEN>"+ qry3.getRs().getString("COD_MEN")+"</COD_MEN>");
						note_jury = qry3.getRs().getString("NOT_PNT_JUR_TRV");
						if (note_jury!=null){
						if (note_jury.startsWith(".")){
							note_jury = "0"+qry3.getRs().getString("NOT_PNT_JUR_TRV");
						}
						}
						res.append("<NOT_PNT_JUR_TRV>"+note_jury+"</NOT_PNT_JUR_TRV>");
						res.append("<NBR_RNG_ETU_TRV>"+ qry3.getRs().getString("NBR_RNG_ETU_TRV")+"</NBR_RNG_ETU_TRV>");
						res.append("<NBR_TOT_RNG_TRV>"+ qry3.getRs().getString("NBR_TOT_RNG_TRV")+"</NBR_TOT_RNG_TRV>");
						res.append("<COD_ADM_OBJ_MNP>"+ qry3.getRs().getString("COD_ADM_OBJ_MNP")+"</COD_ADM_OBJ_MNP>");
						res.append("<COD_RVN>"+ qry3.getRs().getInt("COD_RVN")+"</COD_RVN>");
						res.append("<NUM_RNG_OBJ_MNP>"+ qry3.getRs().getInt("NUM_RNG_OBJ_MNP")+"</NUM_RNG_OBJ_MNP>");
						res.append("<LIC_TRE>"+ qry3.getRs().getString("LIC_TRE")+"</LIC_TRE>");
						res.append("<LIC_MEN>"+ qry3.getRs().getString("LIC_MEN")+"</LIC_MEN>");
						res.append("<COD_ANU_OBJ_MNP>"+ qry3.getRs().getString("COD_ANU_OBJ_MNP")+"</COD_ANU_OBJ_MNP>");
						res.append("<COD_ANU>"+ qry3.getRs().getString("COD_ANU")+"</COD_ANU>");
						res.append("<FULL_COD_ANU_OBJ_MNP>"+ qry3.getRs().getString("FULL_COD_ANU_OBJ_MNP")+"</FULL_COD_ANU_OBJ_MNP>");
						res.append("<DEC_OBJ_MNP>"+ qry3.getRs().getInt("DEC_OBJ_MNP")+"</DEC_OBJ_MNP>");
						res.append("</detail>");
					}
				}
				catch (SQLException e){
					log.error("detailResultat::getXmlDetail() : Erreur SQL qry3" + e);			
				}
				finally {
					qry3.close();		
				}
			}
		}
		catch (SQLException e){
			log.error("detailResultat::getXmlDetail() : Erreur SQL qry2" + e);			
		}
		catch (UnsupportedEncodingException e){
			log.error("detailResultat::getXmlDetail() : " + e);			
		}
		finally {
			qry2.close();		
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
