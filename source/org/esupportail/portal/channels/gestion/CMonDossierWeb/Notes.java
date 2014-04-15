package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.HashMap;

import org.jasig.portal.utils.XMLEscaper;
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
 * @version 1.2
 * 
 */
public class Notes extends SubChannel {
	
	//Classe principale de la channel
	protected CMonDossierWeb owner;
	// log
	private static final Log log = LogFactory.getLog(Notes.class);
	// indique si la personne connectée est un enseignant
	protected String isProf;
	protected StringBuffer xml;
	// hashMap des flux xml pour chaque étape (clé = code/Version::année::vue) 
	protected HashMap xmlRES_ELP_EPR;
	// hasMap des flux xml pour chaque diplôme (clé = vue)
	protected HashMap xmlDiplome;
	// hasMap des flux xml pour chaque étape (clé = vue)
	protected HashMap xmlEtape;
	// Code de l'élément sélectionné
	protected String code_etp;
	// Code de l'année sélectionnée
	protected String code_anu;
	
	/**
	 * Contructeur de la classe
	 * @param main
	 */
	public Notes(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}

	public Boolean setXML() throws FrameWorkException {			
		// Gestion du témoin "vue Etudiant" 
		if (this.runtimeData.getParameter("isProf") != null)
			isProf = this.runtimeData.getParameter("isProf");
		if (isProf != null && isProf.equals("N")) 
			this.getXSLParameter().put("vueEtudiant","checked");
		else {
			this.getXSLParameter().put("vueEtudiant","none");
			isProf= "O";
		}	
		if (this.runtimeData.getParameter("code") != null)
			this.getXSLParameter().put("code",this.runtimeData.getParameter("code"));
		if (this.runtimeData.getParameter("codeAnnu") != null)
			this.getXSLParameter().put("codeAnnu",this.runtimeData.getParameter("codeAnnu"));
		// En mode standalone, on consulte forcément en tant qu'étudiant (test systématique : CC - 04/06/08)
		if (owner.getStaticData().getParameter("MonDossierWebServantMode") == null) {
			isProf = "N";
		}				
		xml = new StringBuffer(START_XML);
		if (Config.getInstance().getRappelIdentite())
			xml.append(owner.getXmlEtudiant());
		// cas affichage général
		if (owner.getCurrentAction().getName().equals("notes"))
			xml.append(getXmlGeneral());
		else {
			// cas détails des épreuves
			xml.append(getXmlEpreuve());
		}	
		// Ajout de la liste des codes résultats
		xml.append(getXmlTypesRes());
		xml.append(END_XML);
		setXML(xml.toString());
		// Pour permettre le bon fonctionnement du lien de téléchargement
		addDownloadXslParameter();
		// Activation des éditions PDF ?
		this.getXSLParameter().put("notesPDF", Config.getInstance().getOptions().get("notesPDF"));
		return Boolean.TRUE;
	}
	
	/**
	 * Méthode qui retourne les résultats aux épreuves en gérant le cache 
	 * @throws FrameWorkException
	 * 
	 */
	protected StringBuffer getXmlEpreuve() throws FrameWorkException {
		StringBuffer res = new StringBuffer();
		// création de la hashMap des flux xml pour chaque étape
		if (xmlRES_ELP_EPR == null) 
			xmlRES_ELP_EPR = new HashMap();
		// Initialisation des variables de l'objet
		if (this.getRuntimeData().getParameter("code") != null)
			this.code_etp = this.getRuntimeData().getParameter("code");
		if (this.getRuntimeData().getParameter("codeAnnu") != null)
			this.code_anu = this.getRuntimeData().getParameter("codeAnnu");
		// calcul de la clé locale 
		String key = this.code_etp+"::"+this.code_anu+"::"+isProf;
		// création du flux xml
		if (xmlRES_ELP_EPR.get(key) == null) {
			// calcul du flux
			res.append(getXmlRES_ELP_EPR());
			// pas de résultat
			if (getXmlRES_ELP_EPR() == null) 
				return null;
			xmlRES_ELP_EPR.put(key,getXmlRES_ELP_EPR());
		}	
		else { // récupération du xml déjà calculé
			res.append(xmlRES_ELP_EPR.get(key));
		}
		return res;
	}
	
	/**
	 * Méthode qui retourne les résultats aux diplomes et étapes en gérant le cache 
	 * 
	 */
	protected StringBuffer getXmlGeneral() throws FrameWorkException {
		StringBuffer res = new StringBuffer();
		String key = isProf;
		// diplômes 
		if (xmlDiplome == null)
			xmlDiplome = new HashMap();
		if (xmlDiplome.get(key) == null) {
			res.append(getXmlDiplome());
			xmlDiplome.put(key,getXmlDiplome());
		}	
		else {
			res.append(xmlDiplome.get(key));
		}
		// étapes
		if (xmlEtape == null) 
			xmlEtape = new HashMap();
		if (xmlEtape.get(key) == null) {
			res.append(getXmlEtape());
			xmlEtape.put(key,getXmlEtape());
		}	
		else {
			res.append(xmlEtape.get(key));
		}
		return res;
	}
	
	/**
	 * Méthode qui retourne les résultats aux diplomes sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlDiplome() throws FrameWorkException {
		StringBuffer res = new StringBuffer();
		Query qry = null;
		boolean error= false;
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				// récupération des diplômes auxquels la personne est inscrite
				getDiplomes(qry,owner.getCodInd());
				qry.select();
				Query qry1 = null;   
				while (qry.getRs().next()){
					res.append("<diplome annee=\"").append(XMLEscaper.escape(qry.getRs().getString("ANU"))).append("\" libelle=\"");
					res.append(XMLEscaper.escape(qry.getRs().getString("LIC_VDI"))).append("\" code=\"").
						append(XMLEscaper.escape(qry.getRs().getString("VDI"))).append("\">");
					try {
						qry1 = Config.getInstance().getConnexionDefault();
						// récupération des notes et résultats
						getNotesResDiplomes(qry1, isProf, qry.getRs().getString("COD_DIP"), qry.getRs().getString("COD_ANU"), owner.getCodInd());
						qry1.select();
						while (qry1.getRs().next()) {
							if (qry1.getRs().getString("COD_SES").equals("0") || qry1.getRs().getString("COD_SES").equals("1"))
								res.append("<session type=\"Session 1\" ");
							else
								res.append("<session type=\"Session 2\" ");	
							res.append("note=\"").append(XMLEscaper.escape(qry1.getRs().getString("NOTE")));	
							res.append("\" resultat=\"").append(XMLEscaper.escape(qry1.getRs().getString("RES")));
							//cas admission / admissibilité pas géré pour l'instant
							if (qry1.getRs().getString("COD_ADM").equals("0"))
								res.append("\" etape=\"Admissibilité\"/>");
							else
								res.append("\" etape=\"\"/>");	
						}	
					}
					finally {
						qry1.close();
					}
					res.append("</diplome>");
				}
			}
			catch (SQLException e) {
				log.error("Notes::getXmlDiplome() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Notes::getXmlDiplome() : Erreur " + e);
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
	 * Méthode qui retourne les résultats aux étapes sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlEtape() throws FrameWorkException {
		StringBuffer res = new StringBuffer();
		Query qry = null;
		boolean error = false;
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				// récupération des étapes 
				getEtapes(qry,owner.getCodInd());
				qry.select();
				Query qry1 = null;   
				while (qry.getRs().next()){
					res.append("<etape annee=\"").append(XMLEscaper.escape(qry.getRs().getString("ANU"))).append("\" libelle=\"");
					res.append(XMLEscaper.escape(qry.getRs().getString("LIB_ETP"))).append("\" code=\"").
						append(XMLEscaper.escape(qry.getRs().getString("VET"))).append("\" codeAnnu=\"").
						append(XMLEscaper.escape(qry.getRs().getString("cod_anu"))).append("\">");
					try {
						qry1 = Config.getInstance().getConnexionDefault();
						// récupération des notes et résultats
						getNotesResEtapes(qry1, isProf, qry.getRs().getString("COD_ETP"), qry.getRs().getString("COD_ANU"), owner.getCodInd());
						qry1.select();
						while (qry1.getRs().next()) {
							if (qry1.getRs().getString("COD_SES").equals("0") || qry1.getRs().getString("COD_SES").equals("1"))
								res.append("<session type=\"Session 1\" ");
							else
								res.append("<session type=\"Session 2\" ");	
							res.append("note=\"").append(XMLEscaper.escape(qry1.getRs().getString("NOTE")));
							res.append("\" resultat=\"").append(XMLEscaper.escape(qry1.getRs().getString("RES")));
							// cas admission / admissibilité pas géré pour l'instant
							if (qry1.getRs().getString("COD_ADM").equals("0"))
								res.append("\" etape=\"Admissibilité\"/>");
							else	
								res.append("\" etape=\"\"/>");
						}	
					}
					finally {
						qry1.close();
					}
					res.append("</etape>");
				}
			}
			catch (SQLException e) {
				log.error("Notes::getXmlEtape() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Notes::getXmlEtape() : Erreur " + e);
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
	 * Méthode qui retourne les résultats aux éléments et épreuves sous forme d'un flux xml 
	 * 
	 */
	public StringBuffer getXmlRES_ELP_EPR() throws FrameWorkException {
		StringBuffer res = new StringBuffer();
		Query qry = null;
		InscrpNote iNote = null;
		String lib = "";
		boolean error = false;
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				// récupération du cod_etp sélectionné
				int pos = this.code_etp.indexOf("/");
				if (pos == -1)
					return null;
				String cod_etp = this.code_etp.substring(0,pos);
				String cod_vrs_vet = this.code_etp.substring(pos+1,this.code_etp.length());
				// récupération de l'étape pour laquelle il y a des inscriptions pédagogiques
				getEtapeIP(qry, owner.getCodInd(), cod_etp, cod_vrs_vet, this.code_anu);
				qry.select();
				// Etat d'avancement de la VET
				String eta_avc_vet_session0 = "";
				String eta_avc_vet_session2 = "";
				while (qry.getRs().next()){
					iNote = new InscrpNote(qry.getRs().getString("COD_ANU"),
								qry.getRs().getString("COD_ETP"),qry.getRs().getString("COD_VRS_VET"),
								qry.getRs().getString("LIB_ETP"),0,"VET");
					Get_Note_Res(iNote,"1",null);
					eta_avc_vet_session2 = Get_Note_Res(iNote,"2",null);	
					if (iNote.getNote1().equals("")) {
						eta_avc_vet_session0 = Get_Note_Res(iNote,"0",null);
					}		
				}
				// pas de résultat
				if (iNote == null) 
					return null;
				// génération du xml pour l'étape
			 	res.append("<etape annee=\"").append(iNote.getAnnee()).append("\" libelle=\"").append(XMLEscaper.escape(iNote.getLib())).append("\" ");
			 	res.append("code=\"").append(XMLEscaper.escape(iNote.getCode())).append("\" version=\"").append(XMLEscaper.escape(iNote.getVersion())).append("\" noteJuin=\"").append(XMLEscaper.escape(iNote.getNote1())).append("\" ");
			 	res.append("resJuin=\"").append(XMLEscaper.escape(iNote.getRes1())).append("\" noteSep=\"").append(XMLEscaper.escape(iNote.getNote2())).append("\" resSept=\"").append(XMLEscaper.escape(iNote.getRes2())).append("\" ");
			 	if (eta_avc_vet_session0.equals("A") || eta_avc_vet_session2.equals("A")
			 			|| (iNote.getNote1().equals("") && iNote.getRes1().equals("")
			 		       && iNote.getNote2().equals("") && iNote.getRes2().equals(""))) 	
			 		res.append("eta_avc_vet=\"A\">");
			 	else
			 		res.append(">");
				// détail des IP pour une VET donnée
			 	getIPs(qry, iNote.getAnnee(), owner.getCodInd(), iNote.getCode(), iNote.getVersion());
			 	qry.select();
				Query qry1 = null;
				try {
					qry1 = Config.getInstance().getConnexionDefault();
					while (qry.getRs().next()){
						InscrpNote iNote1 = new InscrpNote();
						getELP(qry1, qry.getRs().getString("COD_ELP"));
						qry1.select();
						while (qry1.getRs().next()){
							lib = qry1.getRs().getString("LIB");	
						}
						if (qry.getRs().getString("TEM_PRC_ICE").equals("N")) {
							iNote1.setAnnee(qry.getRs().getString("COD_ANU"));
						}
						else {	
							// Témoin PRC coché
							// si lien de correspondance
							if (qry.getRs().getString("COD_LCC_ICE") != null) {
								iNote1.setRes1("ADM");
								iNote1.setNote1("COR");
							} 
							else {	
								// Recherche note antérieure
								getCOD_ANU_MAX(qry1, owner.getCodInd(), qry.getRs().getString("COD_ELP"), qry.getRs().getString("COD_ANU"));
								qry1.select();
								while (qry1.getRs().next()){
									if (qry1.getRs().getString("COD_ANU") != null)
										iNote1.setAnnee(qry1.getRs().getString("COD_ANU"));
									else {
										Query qry2 = Config.getInstance().getConnexionDefault();
										try {
											getCOD_ANU_DISPENSE_ELP(qry2, owner.getCodInd(), qry.getRs().getString("COD_ELP"), qry.getRs().getString("COD_ANU"));
											qry2.select();
											while (qry2.getRs().next()){
												if (qry2.getRs().getString("COD_ANU") != null) {
													iNote1.setAnnee(qry2.getRs().getString("COD_ANU"));
													iNote1.setNote1("VAC");
												}
											}	
										}
										finally {
											qry2.close();	
										}					
									}
								}
							}
						}
						iNote1.setCode(qry.getRs().getString("COD_ELP"));
						iNote1.setLevel(qry.getRs().getInt("LEVEL"));
						iNote1.setType("ELP");
						iNote1.setLib(lib);
						Get_Note_Res(iNote1,"1",iNote);
						Get_Note_Res(iNote1,"2",iNote);
						if (iNote1.getNote1().equals("")) {
							Get_Note_Res(iNote1,"0",iNote);
						}
						// on affiche pas les éléments FICM
						if (!lib.equals("FICM"))
							res.append("<element libelle=\"").append(XMLEscaper.escape(lib)).append("\" code=\"").
								append(XMLEscaper.escape(qry.getRs().getString("COD_ELP"))).append("\" noteJuin=\"").append(XMLEscaper.escape(iNote1.getNote1())).
								append("\" resJuin=\"").append(XMLEscaper.escape(iNote1.getRes1())).append("\" noteSep=\"").append(XMLEscaper.escape(iNote1.getNote2())).
								append("\" resSept=\"").append(XMLEscaper.escape(iNote1.getRes2())).append("\" level=\"").append(qry.getRs().getString("LEVEL")).
								append("\">");
						// Détail des épreuves pour un élément
						getResultatsEPR(qry1, isProf, qry.getRs().getString("COD_ELP"), owner.getCodInd(), qry.getRs().getString("COD_ANU"));
						qry1.select();
						String old_COD_EPR = "";
						while (qry1.getRs().next()){
							InscrpNote iNote2 = new InscrpNote();
							// cas où la ligne récupérée correspond à une nouvelle épreuve
							// car 2 lignes retournées si résultat de juin et résultat de septembre
							if (!old_COD_EPR.equals(qry1.getRs().getString("COD_EPR"))) {
								// on ferme la balise epreuve s'il y a lieu en mettant l'attribut noteSep pour les tests en XSL
								if (!old_COD_EPR.equals("") && !res.toString().endsWith("/>")) {
									res.append(" noteSep=\"\"/>");
								}	
								old_COD_EPR = qry1.getRs().getString("COD_EPR");
								iNote2.setCode(qry1.getRs().getString("COD_EPR"));
								iNote2.setLevel(qry.getRs().getInt("LEVEL")+1);
								iNote2.setType("EPR");
								iNote2.setAnnee(qry1.getRs().getString("COD_ANU"));
								Query qry2 = Config.getInstance().getConnexionDefault();
								try {
									// récupération du libellé de l'épreuve
									getLibEPR(qry2, qry1.getRs().getString("COD_EPR"));
									qry2.select();
									while (qry2.getRs().next()){
										iNote2.setLib(qry2.getRs().getString("LIB_EPR"));
									}				
								}
								finally {
									qry2.close();	
								}
								res.append("<epreuve libelle=\"").append(XMLEscaper.escape(iNote2.getLib())).
									append("\" code=\"").append(XMLEscaper.escape(iNote2.getCode())).append("\" ");
							}
							if (qry1.getRs().getInt("COD_SES") < 2) {
								iNote2.setNote1(qry1.getRs().getString("NOTE"));
								res.append(" noteJuin=\"").append(XMLEscaper.escape(iNote2.getNote1())).append("\" level=\"").append(iNote2.getLevel()).append("\"");
							} else {
								iNote2.setNote2(qry1.getRs().getString("NOTE"));
								res.append(" noteSep=\"").append(XMLEscaper.escape(iNote2.getNote2())).append("\"/>");
							}
						}
						// on ferme s'il y a lieu la dernière épreuve récupérée
						if (!old_COD_EPR.equals("") && !res.toString().endsWith("/>"))
							res.append(" noteSep=\"\" />");
						// on affiche pas les éléments FICM
						if (!lib.equals("FICM"))
							res.append("</element>");
					}		
				}	
				finally {
					qry1.close();
				}
				res.append("</etape>");
			}
			catch (SQLException e) {
				log.error("Notes::getXmlRES_ELP_EPR() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Notes::getXmlRES_ELP_EPR() : Erreur " + e);
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
	 * Récupération d'une note/résultat pour un objet, une année et une session donnés
	 * @return l'état d'avancement de la VET, "" sinon
	 * 
	 */
	private String Get_Note_Res(InscrpNote iNote,String session, InscrpNote iNotePere) throws FrameWorkException {
		char type = ' ';
		Query qry = null;
		boolean error = false;
		String res = "";
		if (!iNote.getNote1().equals("") && session.equals("1")) 
			return res; 	
		if (iNote.getType().equals("VET"))
			type = 'V';
		else if (iNote.getType().equals("ELP"))
			type = 'E';	
		switch (type) {
			case 'V' :
				try {
					qry = Config.getInstance().getConnexionDefault();
					getResultatsVET(qry, isProf, owner.getCodInd(), iNote.getAnnee(), iNote.getCode(), iNote.getVersion(), session);
					qry.select();
					if (qry.getRs() != null) {
						while (qry.getRs().next()){	
							if (!session.equals("2")) {
								if (qry.getRs().getString("NOTE") != null)
									iNote.setNote1(qry.getRs().getString("NOTE"));
								if (qry.getRs().getString("RES") != null)
									iNote.setRes1(qry.getRs().getString("RES"));
							}
							else {
								if (qry.getRs().getString("NOTE") != null)
									iNote.setNote2(qry.getRs().getString("NOTE"));
								if (qry.getRs().getString("RES") != null)
									iNote.setRes2(qry.getRs().getString("RES"));
							}
							res = qry.getRs().getString("eta_avc_vet");
						}
					}
				}
				catch (SQLException e) {
					log.error("Notes::Get_Note_Res() : Erreur SQL " + e);
					error = true;
				}
				catch (Exception e) {
					log.error("Notes::Get_Note_Res() : Erreur " + e);
					error = true;
				}
				finally {
					qry.close();
					if (error)
						throw new FrameWorkException();
				}	
				break;
			case 'E' :
				int vac = 0;
				try {
					qry = Config.getInstance().getConnexionDefault();
					if (iNote.getAnnee() != null && !iNote.getAnnee().equals("")) {
						getResultatsELP(qry, isProf, owner.getCodInd(), iNote.getAnnee(), iNote.getCode(), session);
					}		
					else { // Recherche VAC
						if (iNotePere == null) {
							getResultatsDispenseELP(qry, owner.getCodInd(), iNote.getCode());
						}		   	   
						else {
							getResultatsDispenseELP(qry, owner.getCodInd(), iNote.getCode(), iNotePere.getCode(), iNotePere.getVersion());
						}
						vac = 1;
					}
					qry.select();
					while (qry.getRs().next()){
						if (vac == 1 && qry.getRs().getString("COD_ANU") != null)
							iNote.setAnnee(qry.getRs().getString("COD_ANU"));
						if (!session.equals("2")) {
							if (qry.getRs().getString("NOTE") != null)
								iNote.setNote1(qry.getRs().getString("NOTE"));
							if (qry.getRs().getString("RES") != null)
								iNote.setRes1(qry.getRs().getString("RES"));
						} 
						else {
							if (qry.getRs().getString("NOTE") != null)
								iNote.setNote2(qry.getRs().getString("NOTE"));
							if (qry.getRs().getString("RES") != null)
								iNote.setRes2(qry.getRs().getString("RES"));
						}		
					}	
				}
				catch (SQLException e) {
					log.error("Notes::Get_Note_Res() : Erreur SQL " + e);
					error = true;
				}
				catch (Exception e) {
					log.error("Notes::Get_Note_Res() : Erreur " + e);
					error = true;
				}
				finally {
					qry.close();
					if (error)
						throw new FrameWorkException();	
				}
				break;					
		}
		return res;
	}
	
	/**
	 * Récupération des diplômes auxquels l'étudiant est inscrit
	 * 
	 * @param qry objet Query
	 * @param COD_IND code Individu
	 * @throws SQLException
	 */
	public void getDiplomes(Query qry, String COD_IND) throws SQLException {
		qry.setSql("select distinct r.cod_anu || '/' || to_char(to_number(r.cod_anu)+1) \"ANU\", r.cod_dip || '/' || to_char(r.cod_vrs_vdi) \"VDI\",vdi.lic_vdi,r.cod_anu,r.cod_dip " +
					"from apogee.resultat_vdi r,apogee.version_diplome vdi " +
					"where r.cod_ind = ? " +
					"and (r.not_vdi is not null or r.not_sub_vdi is not null or r.cod_tre is not null) " +
					"and vdi.cod_dip = r.cod_dip " +
					"and vdi.cod_vrs_vdi = r.cod_vrs_vdi " + 
					"order by r.cod_anu desc,r.cod_dip");
		qry.getStmt().setString(1, COD_IND);
	}
	
	/**
	 * Récupération des notes et résultats aux diplômes auxquels l'étudiant est inscrit
	 * @param qry objet Query
	 * @param isProf indique si la personne connectée est un enseignant ("N" par défaut) 
	 * @param COD_DIP code diplôme
	 * @param COD_ANU code année
	 * @param COD_IND code individu
	 * @throws SQLException
	 */
	public void getNotesResDiplomes(Query qry, String isProf, String COD_DIP, String COD_ANU, String COD_IND) throws SQLException {
		qry.setSql("select r.cod_ses,decode(decode(?,'O','E',g.eta_avc_vdi),'A',' ',nvl(to_char(r.not_vdi) || " + 
					"decode(NOT_PNT_JUR_VDI,null,null,' (+' || to_char(NOT_PNT_JUR_VDI,'990D999')|| ')'), " +
					"r.not_sub_vdi)) \"NOTE\", g.cod_adm, decode(decode(?,'O','E',g.eta_avc_vdi),'A',' ',r.cod_tre) \"RES\" " + 
					"from apogee.grp_resultat_vdi g, apogee.resultat_vdi r " +
					"where g.cod_dip = ? and g.cod_anu = ? " +
					"and r.cod_ind = ? " +
					"and   (r.not_vdi is not null or r.not_sub_vdi is not null or r.cod_tre is not null) " +
					"and   g.cod_dip = r.cod_dip " +
					"and   g.cod_vrs_vdi = r.cod_vrs_vdi " + 
					"and   g.cod_ses = r.cod_ses " +
					"and   g.cod_adm = r.cod_adm " +
					"and   g.cod_anu = r.cod_anu");
		qry.getStmt().setString(1, isProf);			
		qry.getStmt().setString(2, isProf);										
		qry.getStmt().setString(3, COD_DIP);
		qry.getStmt().setString(4, COD_ANU);
		qry.getStmt().setString(5, COD_IND);
	}
	
	/**
	 * Récupération des étapes auxquelles l'étudiant est inscrit
	 * 
	 * @param qry objet Query
	 * @param COD_IND code Individu
	 * @throws SQLException
	 */
	public void getEtapes(Query qry, String COD_IND) throws SQLException {
		qry.setSql("select distinct ANU, VET,lib_etp,cod_anu,e.cod_etp " +
					" from (select cod_anu || '/' || to_char(to_number(cod_anu)+1) ANU, cod_etp || '/' || " +
					"to_char(cod_vrs_vet) VET,cod_anu,cod_etp " +
					" from ins_adm_etp " +
					" where cod_ind = ? " +
					" and   eta_iae = 'E' " +
					// Ajout CC - 04/06/08
					" and   eta_pmt_iae = 'P' " + 
					" union " +
					" select cod_anu || '/' || to_char(to_number(cod_anu)+1) ANU, cod_etp || '/' || " +
					"to_char(cod_vrs_vet) VET, cod_anu,cod_etp " +
					" from ins_pedagogi_etp " +
					" where cod_ind = ? and cod_typ_ipe != 'N') ins, etape e " +
					" where e.cod_etp = ins.cod_etp " + 
					" order by cod_anu desc, cod_etp ");
		qry.getStmt().setString(1, COD_IND);				
		qry.getStmt().setString(2, COD_IND);				
	}
	
	/**
	 * Récupération des notes et résultats aux étapes auxquelles l'étudiant est inscrit
	 * @param qry objet Query
	 * @param isProf indique si la personne connectée est un enseignant ("N" par défaut) 
	 * @param COD_ETP code étape
	 * @param COD_ANU code année
	 * @param COD_IND code individu
	 * @throws SQLException
	 */
	public void getNotesResEtapes(Query qry, String isProf, String COD_ETP, String COD_ANU, String COD_IND) throws SQLException {
		qry.setSql("select r.cod_ses,decode(decode(?,'O','E',g.eta_avc_vet),'A',' ',nvl(to_char(r.not_vet) || "+ 
					"decode(NOT_PNT_JUR_VET,null,null,' (+' || to_char(NOT_PNT_JUR_VET,'990D999')|| ')'), "+ 
					"r.not_sub_vet)) \"NOTE\", "+
					"decode(decode(?,'O','E',g.eta_avc_vet),'A',' ',r.cod_tre) \"RES\",g.cod_adm "+
					"from apogee.resultat_vet r,apogee.grp_resultat_vet g "+ 
					"where r.cod_ind = ? "+
					"and g.cod_etp = ? and g.cod_anu = ? "+
					"and   g.cod_etp = r.cod_etp "+ 
					"and   g.cod_vrs_vet = r.cod_vrs_vet "+ 
					"and   g.cod_ses = r.cod_ses "+
					"and   g.cod_adm = r.cod_adm "+
					"and   g.cod_anu = r.cod_anu");
		qry.getStmt().setString(1, isProf);			
		qry.getStmt().setString(2, isProf);
		qry.getStmt().setString(3, COD_IND);										
		qry.getStmt().setString(4, COD_ETP);
		qry.getStmt().setString(5, COD_ANU);
	}
	
	/**
	 * Récupération de l'étape pour laquelle il y a des inscriptions pédagogiques (IP)
	 * @param qry objet Query
	 * @param COD_IND code individu 
	 * @param COD_ETP code étape
	 * @param COD_VRS_VET code version
	 * @param COD_ANU code année
	 * @throws SQLException
	 */
	public void getEtapeIP(Query qry, String COD_IND, String COD_ETP, String COD_VRS_VET, String COD_ANU) throws SQLException {
		qry.setSql("select i.cod_anu,i.cod_etp,i.cod_vrs_vet,e.lib_etp " + 
					"from apogee.ins_pedagogi_etp i,apogee.etape e where i.cod_ind = ? " +
					"and e.cod_etp = ? " +
					"and i.cod_vrs_vet = ? " +
					"and i.cod_anu = ? " +
					"and e.cod_etp = i.cod_etp  " +
					"order by i.cod_anu desc,i.cod_etp,i.cod_vrs_vet");
		qry.getStmt().setString(1, COD_IND);
		qry.getStmt().setString(2, COD_ETP);
		qry.getStmt().setString(3, COD_VRS_VET);
		qry.getStmt().setString(4, COD_ANU);
	}
	
	/**
	 * Récupération des IP pour une VET donnée
	 * @param qry objet Query
	 * @param COD_ANU code année
	 * @param COD_IND code individu 
	 * @param COD_ETP code étape
	 * @param COD_VRS_VET code version
	 * @throws SQLException
	 */
	public void getIPs(Query qry, String COD_ANU, String COD_IND, String COD_ETP, String COD_VRS_VET) throws SQLException {
		qry.setSql("select level,cod_elp,cod_anu,tem_prc_ice,COD_LCC_ICE " +
				   "FROM APOGEE.IND_CONTRAT_ELP CONNECT BY prior COD_ELP = COD_ELP_SUP " +
				   " AND COD_ANU= ? " + 
			   	   " AND COD_IND= ? " + 
			   	   " AND COD_ETP= ? " +
			   	   " AND COD_VRS_VET= ? " +
			   	   " START WITH COD_ELP_SUP IS NULL " +
			   	   " AND COD_ANU= ? " +
			   	   " AND COD_IND= ?" + 
			   	   " AND COD_ETP= ? " + 
				   " order siblings by COD_ELP");
		qry.getStmt().setString(1, COD_ANU);
		qry.getStmt().setString(2, COD_IND);
		qry.getStmt().setString(3, COD_ETP);
		qry.getStmt().setString(4, COD_VRS_VET);
		qry.getStmt().setString(5, COD_ANU);
		qry.getStmt().setString(6, COD_IND);
		qry.getStmt().setString(7, COD_ETP);
	}
	
	/**
	 * Récupération d'un élément pédagogique
	 * @param qry objet Query
	 * @param COD_ELP code élément pédagogique
	 * @throws SQLException
	 */
	public void getELP(Query qry, String COD_ELP) throws SQLException {
		qry.setSql("select decode(cod_nel,'FICM','FICM',lib_elp) \"LIB\"" +
					" from apogee.element_pedagogi" +
					" where cod_elp = ? ");
		qry.getStmt().setString(1, COD_ELP);
	}
	
	/**
	 * Récupération du COD_ANU max
	 * @param qry objet Query
	 * @param COD_IND code individu
	 * @param COD_ELP code élément pédagogique
	 * @param COD_ANU code année
	 * @throws SQLException
	 */
	public void getCOD_ANU_MAX(Query qry, String COD_IND, String COD_ELP, String COD_ANU) throws SQLException {
		qry.setSql("select max(cod_anu) \"COD_ANU\"" +
					" from apogee.resultat_elp" +
		 			" where cod_ind = ? " +
		 			" and   cod_elp = ? " +
		 			" and   cod_anu < ? " +
		 			" and   (not_elp is not null or not_sub_elp is not null " +
		 			" or cod_tre is not null)");
		qry.getStmt().setString(1, COD_IND); 			
		qry.getStmt().setString(2, COD_ELP);
		qry.getStmt().setString(3, COD_ANU);
	}
	
	/**
	 * Récupération des COD_ANU des éléments dont l'étudiant est dispensé
	 * @param qry objet Query
	 * @param COD_IND code individu
	 * @param COD_ELP code élément pédagogique
	 * @param COD_ANU code année
	 * @throws SQLException
	 */
	public void getCOD_ANU_DISPENSE_ELP(Query qry, String COD_IND, String COD_ELP, String COD_ANU) throws SQLException {
		qry.setSql("select cod_anu from apogee.ind_dispense_elp " +
					" where cod_ind = ? " +
					" and cod_elp = ? " +
					" and cod_anu <= ? ");
		qry.getStmt().setString(1, COD_IND); 			
		qry.getStmt().setString(2, COD_ELP);
		qry.getStmt().setString(3, COD_ANU);
	}
	
	/**
	 * Récupération des résultats d'un étudiant aux épreuves
	 * @param qry objet Query
	 * @param isProf indique si la personne connectée est un enseignant ("N" par défaut)
	 * @param COD_ELP code élément pédagogique
	 * @param COD_IND code individu
	 * @param COD_ANU code année
	 * @throws SQLException
	 */
	public void getResultatsEPR(Query qry, String isProf, String COD_ELP, String COD_IND, String COD_ANU) throws SQLException {
		/*qry.setSql("select r.cod_epr, cod_anu,e.lib_epr,r.cod_ses, " +
					"decode(decode(?,'O','N',e.tem_ctl_val_cad_epr),'N'," +
					"nvl(to_char(r.not_epr),r.not_sub_epr),' ') \"NOTE\" "+
					" from apogee.resultat_epr r,apogee.epr_sanctionne_elp ese,apogee.epreuve e " +
					" where ese.cod_elp = ? " +
					" and   ese.cod_ses = r.cod_ses " +
					" and   ese.tem_sus_epr_ses = 'N' " +
					" and   r.cod_ind = ? " +
					" and   r.cod_epr = ese.cod_epr " +
					" and   r.cod_anu = ? " +
					" and   (r.not_epr is not null or r.not_sub_epr is not null)" +
					" and   TEM_IND_CRN_EPR = 'CS' and e.cod_epr = r.cod_epr" +
					" order by r.cod_epr,r.cod_ses");*/
		// Modification suite demande PEJ : notes visibles dès que l'ELP père est passé à l'état E ou T
		qry.setSql("select r.cod_epr, r.cod_anu,e.lib_epr,r.cod_ses, " +
					"	   case " +
					"	   	   when " + 
					"	   		  case " +
					"			  	  when g.eta_avc_elp = 'A' then " + 
					"					   case " + 
					"					   	   when ? = 'O' then 'N' " + 
					"						   else e.tem_ctl_val_cad_epr " + 
					"					   end " +
					"				  else 'N' " + 
					"			  end " +
					"			  = 'N' then nvl(to_char(r.not_epr),r.not_sub_epr) " +
					"			else ' ' " +
					"		end \"NOTE\" " +
				   //"decode(decode(ETA_AVC_ELP,'E','N','T','N',decode(?,'O','N',e.tem_ctl_val_cad_epr)),'N', " + 
				   //"nvl(to_char(r.not_epr),r.not_sub_epr),' ') \"NOTE\" " +  
				   "from apogee.grp_resultat_elp g,apogee.resultat_epr r,apogee.epr_sanctionne_elp ese,apogee.epreuve e " + 
				   "where ese.cod_elp = ? " + 
				   "and   ese.cod_ses = r.cod_ses " + 
				   "and   ese.tem_sus_epr_ses = 'N' " +  
				   "and   r.cod_ind = ? " + 
				   "and   r.cod_epr = ese.cod_epr " + 
				   "and   r.cod_anu = ? " +
				   "and   (r.not_epr is not null or r.not_sub_epr is not null) " + 
				   "and   TEM_IND_CRN_EPR = 'CS' and e.cod_epr = r.cod_epr " +
				   "and   g.cod_elp = ese.cod_elp and g.cod_ses = r.cod_ses " + 
				   "and   g.cod_adm = r.cod_adm and g.cod_anu = r.cod_anu " +
				   "order by r.cod_epr,r.cod_ses");
		qry.getStmt().setString(1, isProf); 			
		qry.getStmt().setString(2, COD_ELP);
		qry.getStmt().setString(3, COD_IND);
		qry.getStmt().setString(4, COD_ANU);
	}
	
	/**
	 * Récupération du libellé d'une épreuve
	 * @param qry objet Query
	 * @param COD_EPR code épreuve
	 * @throws SQLException
	 */
	public void getLibEPR(Query qry, String COD_EPR) throws SQLException {
		qry.setSql("select lib_epr from apogee.epreuve where cod_epr = ? ") ;
		qry.getStmt().setString(1,COD_EPR);
	}
	
	/**
	 * Récupération des résultats d'un étudiant à une version d'étape
	 * @param qry objet Query
	 * @param isProf indique si la personne connectée est un enseignant ("N" par défaut)
	 * @param COD_IND code individu
	 * @param COD_ANU code année
	 * @param COD_ETP code étape
	 * @param COD_VRS_VET version étape
	 * @param COD_SES code session
	 * 
	 * @throws SQLException
	 */
	public void getResultatsVET(Query qry, String isProf, String COD_IND, String COD_ANU, String COD_ETP, String COD_VRS_VET, String COD_SES) throws SQLException {
		qry.setSql("select decode(decode(?,'O','E',g.eta_avc_vet),'A',' ',nvl(to_char(r.not_vet) ||" +
					"decode(NOT_PNT_JUR_Vet,null,null,' (+' || to_char(NOT_PNT_JUR_VET,'990D999') || ')')," +
				 	"r.not_sub_vet)) \"NOTE\"," +
				 	"decode(decode(?,'O','E',g.eta_avc_vet),'A',' ',r.cod_tre) \"RES\", g.eta_avc_vet "+
					" from apogee.resultat_vet r,apogee.grp_resultat_vet g " +
					" where r.cod_ind = ? " +
				 	" and   r.cod_anu = ? " +
				 	" and   r.cod_etp = ? " +
				 	" and   r.cod_vrs_vet = ? " +
				 	" and   r.cod_ses =  ? " +
				 	" and   (r.not_vet is not null or r.not_sub_vet is not null or r.cod_tre is not null) " +
				 	" and   g.cod_etp = r.cod_etp and   g.cod_vrs_vet = r.cod_vrs_vet" +
				 	" and   g.cod_ses = r.cod_ses and   g.cod_adm = r.cod_adm" +
				 	" and   g.cod_anu = r.cod_anu");
		qry.getStmt().setString(1, isProf);
		qry.getStmt().setString(2, isProf);
		qry.getStmt().setString(3, COD_IND);
		qry.getStmt().setString(4, COD_ANU);
		qry.getStmt().setString(5, COD_ETP);
		qry.getStmt().setString(6, COD_VRS_VET);
		qry.getStmt().setString(7, COD_SES);
	}
	
	
	/**
	 * Récupération des résultats d'un étudiant à un élément pédagogique
	 * @param qry objet Query
	 * @param isProf indique si la personne connectée est un enseignant ("N" par défaut)
	 * @param COD_IND code individu
	 * @param COD_ANU code année
	 * @param COD_ELP code élément pédagogique
	 * @param COD_SES code session
	 * 
	 * @throws SQLException
	 */
	public void getResultatsELP(Query qry, String isProf, String COD_IND, String COD_ANU, String COD_ELP, String COD_SES) throws SQLException {
		qry.setSql("select decode(decode(?,'O','E',g.eta_avc_elp),'A',' ',nvl(to_char(r.not_elp) ||" +
		 			"decode(NOT_PNT_JUR_ELP,null,null,' (+' || to_char(NOT_PNT_JUR_ELP,'990D999') || ')')," +
					"nvl(r.not_sub_elp,' '))) \"NOTE\",r.cod_anu," +
					"decode(decode(?,'O','E',g.eta_avc_elp),'A',' ',nvl(r.cod_tre,' ')) \"RES\"" +
					" from apogee.resultat_elp r,apogee.grp_resultat_elp g "+
					" where r.cod_ind = ? " + 
		 			" and   r.cod_anu = ? " +
					" and   r.tem_ind_crn_elp = 'CS' " +
					" and   r.tem_not_rpt_elp = 'N' " +
					" and   r.cod_elp = ? " +
					" and   r.cod_ses = ? " + 
					" and   (r.not_elp is not null or r.not_sub_elp is not null or r.cod_tre is not null) " +
					" and   g.cod_elp = r.cod_elp and   g.cod_ses = r.cod_ses " +
					" and   g.cod_adm = r.cod_adm and   g.cod_anu = r.cod_anu");
		qry.getStmt().setString(1, isProf);
		qry.getStmt().setString(2, isProf);
		qry.getStmt().setString(3, COD_IND);
		qry.getStmt().setString(4, COD_ANU);
		qry.getStmt().setString(5, COD_ELP);
		qry.getStmt().setString(6, COD_SES);
	}
	
	/**
	 * Récupération des résultats d'un étudiant à un élément pédagogique dont il est dispensé
	 * @param qry objet Query
	 * @param COD_IND code individu
	 * @param COD_ELP code élément pédagogique
	 * 
	 * @throws SQLException
	 */
	public void getResultatsDispenseELP(Query qry, String COD_IND, String COD_ELP) throws SQLException {
		qry.setSql("select 'VAC' \"NOTE\",' ' \"RES\",cod_anu" +
				   " from apogee.ind_dispense_elp" +
				   " where cod_ind = ? " +
			   	   " and   cod_elp = ? ");
		qry.getStmt().setString(1, COD_IND);
		qry.getStmt().setString(2, COD_ELP);
	}
	
	/**
	 * Récupération des résultats d'un étudiant à un élément pédagogique dont il est dispensé
	 * @param qry objet Query
	 * @param COD_IND code individu
	 * @param COD_ELP code élément pédagogique
	 * @param COD_ETP code étape
	 * @param COD_VRS_VET version étape
	 * 
	 * @throws SQLException
	 */
	public void getResultatsDispenseELP(Query qry, String COD_IND, String COD_ELP, String COD_ETP, String COD_VRS_VET) throws SQLException {
		qry.setSql("select 'VAC' \"NOTE\",' ' \"RES\",cod_anu" +
				   " from apogee.ind_dispense_elp" +
				   " where cod_ind = ? " +
				   " and   cod_elp = ? " +	   	   
			   	   " and   cod_etp = ? " +
			       " and   cod_vrs_vet = ? ");
		qry.getStmt().setString(1, COD_IND);
		qry.getStmt().setString(2, COD_ELP);
		qry.getStmt().setString(3, COD_ETP);	        
		qry.getStmt().setString(4, COD_VRS_VET);
	}
	
	/**
	 * Méthode qui retourne les codes résultats d'Apogée sous forme d'un flux xml 
	 * @return
	 * @throws FrameWorkException
	 */
	public StringBuffer getXmlTypesRes() throws FrameWorkException {
		StringBuffer res = new StringBuffer("");
		Query qry = null;
		boolean error = false;
		if (owner.getCodInd() != null) { // la personne connectée à un cod_ind
			try {
				qry = Config.getInstance().getConnexionDefault();
				getTypesRes(qry);
				qry.select();
				while (qry.getRs().next()){
					res.append("<type_resultat cod_tre=\"").
					append(XMLEscaper.escape(qry.getRs().getString("cod_tre"))).
					append("\" lib_tre=\"").
					append(XMLEscaper.escape(qry.getRs().getString("lib_tre"))).
					append("\" />");
				}
			}
			catch (SQLException e) {
				log.error("Notes::getXmlTypesRes() : Erreur SQL " + e);
				error = true;
			}
			catch (Exception e) {
				log.error("Notes::getXmlTypesRes() : Erreur " + e);
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
	 * Récupération de la signification des codes résultat
	 * @param qry objet Query
	 */
	public void getTypesRes(Query qry) throws SQLException {
		qry.setSql("select cod_tre, lib_tre from typ_resultat order by 1");
	}
	
}

class InscrpNote {
	private String annee;
	private String code;
	private String version;
	private String lib;
	private int level;
	private	String type;
	private String note1;
	private String res1;
	private String note2;
	private String res2;
	
	public InscrpNote() {
	}	
	
	public InscrpNote(String annee, String code, String version, String lib, int level, String type) {
		this.annee = annee;
		this.code = code;
		this.version = version;
		this.lib = lib;
		this.level = level;
		this.type = type;
	}	
	
	/**
	 * @return
	 */
	public String getAnnee() {
		return annee;
	}

	/**
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return
	 */
	public String getLib() {
		return lib;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param string
	 */
	public void setAnnee(String string) {
		annee = string;
	}

	/**
	 * @param string
	 */
	public void setCode(String string) {
		code = string;
	}

	/**
	 * @param string
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @param string
	 */
	public void setLib(String string) {
		lib = string;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}

	/**
	 * @return
	 */
	public String getNote1() {
		if (note1 == null)
			return "";
		return note1;
	}

	/**
	 * @return
	 */
	public String getNote2() {
		if (note2 == null)
			return "";
		return note2;
	}

	/**
	 * @return
	 */
	public String getRes1() {
		if (res1 == null)
			return "";
		return res1;
	}

	/**
	 * @return
	 */
	public String getRes2() {
		if (res2 == null)
			return "";
		return res2;
	}

	/**
	 * @param string
	 */
	public void setNote1(String string) {
		note1 = string;
		if (note1.startsWith(","))
			note1 = "0" + note1;
	}

	/**
	 * @param string
	 */
	public void setNote2(String string) {
		note2 = string;
		if (note2.startsWith(","))
			note2 = "0" + note2;
	}

	/**
	 * @param string
	 */
	public void setRes1(String string) {
		res1 = string;
	}

	/**
	 * @param string
	 */
	public void setRes2(String string) {
		res2 = string;
	}
	
}	

