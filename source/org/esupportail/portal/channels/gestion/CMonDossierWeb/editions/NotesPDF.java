package org.esupportail.portal.channels.gestion.CMonDossierWeb.editions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;

import org.apache.avalon.framework.logger.Log4JLogger;
import org.apache.avalon.framework.logger.NullLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;
import org.apache.log4j.Logger;
import org.esupportail.portal.channels.gestion.CMonDossierWeb.CMonDossierWeb;
import org.esupportail.portal.channels.gestion.CMonDossierWeb.Config;
import org.esupportail.portal.channels.gestion.CMonDossierWeb.Notes;
import org.esupportail.portal.utils.channels.MainChannel;
import org.jasig.portal.IMimeResponse;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Notes<br>
 * <br>
 * Affichage des notes d'un étudiant en PDF<br>
 * <br>
 * (c)Copyright <a href="http://www.esup-portail.org">ESup-Portail 2004</a><br>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.2
 * 
 */
public class NotesPDF extends Notes implements IMimeResponse {

	// log
	private static final Log log = LogFactory.getLog(NotesPDF.class);

	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public NotesPDF(MainChannel main) {
		super(main);
	}

	/* (non-Javadoc)
	 * @see org.jasig.portal.IMimeResponse#getContentType()
	 */
	public String getContentType() {
		return "application/pdf"; 
	}

	/* (non-Javadoc)
	 * @see org.jasig.portal.IMimeResponse#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		// TODO Raccord de méthode auto-généré
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jasig.portal.IMimeResponse#downloadData(java.io.OutputStream)
	 */
	public void downloadData(OutputStream out) throws IOException {
		try {
			/* Correctif CC - 04/06/08 */
			/*isProf = runtimeData.getParameter("isProf");*/
			xml = new StringBuffer(START_XML);
			// Alimentation du pdf si la génération est autorisée
			if (Config.getInstance().getOptions().get("notesPDF").equals("true")) {
				xml.append(owner.getXmlEtudiant());
				// cas affichage général
				if (owner.getCurrentAction().getName().equals("notesPDF"))
					xml.append(getXmlGeneral());
				else // cas détails des épreuves
					xml.append(getXmlEpreuve());
			}
			// Ajout de la liste des codes résultats
			xml.append(getXmlTypesRes());
			xml.append(END_XML);
			// Xsl de transformation
			URL resourceURL = CMonDossierWeb.class.getResource(getXslFile());
			if(resourceURL != null) 
			    	render(xml.toString(),resourceURL.toString(), out);
			else
				log.error("NotesPDF::downloadData() : Le fichier de transformation est introuvable");
		} catch (Exception e) {
			log.error("NotesPDF::downloadData() : " + e);
		}
	}

	/* (non-Javadoc)
	 * @see org.jasig.portal.IMimeResponse#getName()
	 */
	public String getName() {
		String name = owner.getLoginEtu() + "_";
		name += (owner.getCurrentAction().getName().equals("notesPDF")) ? "notes" : "detailNotes" + "_" + code_anu;
		return name + ".pdf"; // nom du fichier à télécharger
	}

	/* (non-Javadoc)
	 * @see org.jasig.portal.IMimeResponse#getHeaders()
	 */
	public Map getHeaders() {
		 HashMap map = new HashMap(); 
         map.put("Content-disposition", "attachment; filename=" + getName()); 
         return map;
	}

	/* (non-Javadoc)
	 * @see org.jasig.portal.IMimeResponse#reportDownloadError(java.lang.Exception)
	 */
	public void reportDownloadError(Exception e) {
		log.error("NotesPDF::reportingDownloadError() : " + e);		
	}
	
	/**
	 * Réalise la transformation XSL-FO
	 * @param xmlInput l'input XML
	 * @param stylesheetPath le chemin de la stylsheet
	 * @param os l'output stream à écrire
	 */
	public static void render(String xmlInput, String stylesheetPath, OutputStream os)  {
		Driver driver = new Driver();
		try {
			Log4JLogger log4JLogger = new Log4JLogger((Logger)log);
			driver.setLogger(log4JLogger);
			MessageHandler.setScreenLogger(log4JLogger);
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("NotesPDF::render() : Could not get a log4j logger, FOP logs will be lost.");
			driver.setLogger(new NullLogger());
			MessageHandler.setScreenLogger(new NullLogger());
		}
		driver.setRenderer(Driver.RENDER_PDF);
		driver.setOutputStream(os);
		Result result = new SAXResult(driver.getContentHandler());		
		Source source = new StreamSource(new StringReader(xmlInput));
		Source style = new StreamSource(stylesheetPath);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer(style);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			log.error("NotesPDF::render() : FOP transformation failed! " + e);
		}
	}
	
	/**
	 * Retourne le chemin du fichier de transformation
	 * @return le chemin du fichier de transformation
	 */
	private String getXslFile() {
		String path = "/"+getPackageName(this.getClass())+"/";
		path = path.replaceAll("[.]","/");
		return path + getXSL();
	}
	
}	
