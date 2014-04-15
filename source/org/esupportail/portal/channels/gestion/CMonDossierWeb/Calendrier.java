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
 * Calendrier<br>
 * <br>
 * Gestion de l'affichage du calendrier des épreuves d'un étudiant <br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.2
 * 
 */
public class Calendrier extends SubChannel {
	
	//Classe principale de la channel
	private CMonDossierWeb owner;
	// log
	private static final Log log = LogFactory.getLog(Calendrier.class);
	private StringBuffer xml;

	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public Calendrier(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}

	public Boolean setXML() throws FrameWorkException {
		if (xml == null) {
			xml = new StringBuffer(START_XML);
			if (Config.getInstance().getRappelIdentite())
				xml.append(owner.getXmlEtudiant());
			xml.append(getXmlExamen());
			xml.append(END_XML);
		}
		setXML(xml.toString());
		return Boolean.TRUE;
	}
		
	/**
	 * Méthode qui retourne les examens d'un étudiant sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlExamen() throws FrameWorkException {
		StringBuffer res = new StringBuffer("<examens>");
		Query qry = null;
		boolean error = false;
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				qry.setSql("SELECT DISTINCT to_char(PESA.DAT_DEB_PES,'DD/MM/YYYY') \"DATE\"," +
   							"PESA.DAT_DEB_PES,DECODE(SUBSTR(TO_CHAR(PESA.DHH_DEB_PES),1,1),'1'," +
   							"TO_CHAR(PESA.DHH_DEB_PES),'0'||TO_CHAR(PESA.DHH_DEB_PES)) ||':'||" +
   							"DECODE(TO_CHAR(PESA.DMM_DEB_PES),'0','00',TO_CHAR(PESA.DMM_DEB_PES)) \"HEURE\"," +
   							"PESA.DUR_EXA_EPR_PES || ' min.' \"DUREE\"," +
   							"PESA.COD_SAL \"SALLE\"," +
   							"NVL(TO_CHAR(PI.NUM_PLC_AFF_PSI),' ') \"PLACE\"," +
   							"BAT.LIB_BAT \"BATIMENT\", E.LIB_EPR \"EPREUVE\" " +
   							"FROM APOGEE.PRD_EPR_SAL_ANU PESA,APOGEE.EPREUVE E,APOGEE.PES_IND PI,APOGEE.BATIMENT BAT," +
   							"APOGEE.SALLE SAL,APOGEE.PERIODE_EXA PEX " +
   							"WHERE   ( PI.COD_IND=?) AND ( PI.COD_PES=PESA.COD_PES  ) " +
   							"AND  ( PESA.COD_EPR=E.COD_EPR  ) AND  ( PESA.COD_PXA = PEX.COD_PXA) " +
   							"AND  (PEX.LIB_PXA LIKE '@%') AND  (SAL.COD_SAL = PESA.COD_SAL) " +
   							"AND  (BAT.COD_BAT = SAL.COD_BAT) ORDER BY PESA.DAT_DEB_PES,2");
				qry.getStmt().setString(1, owner.getCodInd());				
				qry.select();			   
				while (qry.getRs().next()){
					res.append("<examen>").
						append("<date>").append(XMLEscaper.escape(qry.getRs().getString("DATE"))).append("</date>").
						append("<heure>").append(XMLEscaper.escape(qry.getRs().getString("HEURE"))).append("</heure>").
						append("<duree>").append(XMLEscaper.escape(qry.getRs().getString("DUREE"))).append("</duree>").
						append("<batiment>").append(XMLEscaper.escape(qry.getRs().getString("BATIMENT"))).append("</batiment>").
						append("<salle>").append(XMLEscaper.escape(qry.getRs().getString("SALLE"))).append("</salle>"). 
						append("<epreuve>").append(XMLEscaper.escape(qry.getRs().getString("EPREUVE"))).append("</epreuve>");
					if (Config.getInstance().getOptions().get("affNumPlaceExamen") == null || Config.getInstance().getOptions().get("affNumPlaceExamen").equals("true"))
						res.append("<place>").append(XMLEscaper.escape(qry.getRs().getString("PLACE"))).append("</place>"); 
					res.append("</examen>");   
				}
				if (Config.getInstance().getOptions().get("cmtCalExamen") != null) {
					res.append("<cmtCalExamen>").
					    append(Config.getInstance().getOptions().get("cmtCalExamen")).
					    append("</cmtCalExamen>");
				}	
			}
			catch (SQLException e) {
				log.error("Calendrier::getXmlExamen() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Calendrier::getXmlExamen() : Erreur " + e);
				error = true;
			}
			finally {
				qry.close();
				if (error)
					throw new FrameWorkException();
			}
		} 
		res.append("</examens>");
		return res;
	}
	
}
