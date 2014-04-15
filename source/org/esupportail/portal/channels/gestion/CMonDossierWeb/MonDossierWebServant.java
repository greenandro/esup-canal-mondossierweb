package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.IServant;
import org.jasig.portal.PortalException;
import org.xml.sax.ContentHandler;

/**
 * 
 * <p>MonDossierWebServant</p>
 * <p>Description : Extension en servant de MonDossierWeb</p>
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:cedric.champmartin@univ-nancy2.fr">Cédric Champmartin</a>
 * @version 1.1
 *
 */
public class MonDossierWebServant extends CMonDossierWeb implements IServant {
	
	/**
	 * Constructeur
	 * @throws PortalException
	 */
	public MonDossierWebServant() throws PortalException  {
		super();
	}

	/**
	 * retourne true ou false si le servant a fini de travailler
	 * pour se terminer, le canal doit recevoir : servantFinished
	 * qui est en fait le nom d'un bouton sur la feuille xsl 
	 * @return True si le servant est fini, False sinon
	 */
	public boolean isFinished() {
		if (runtimeData.getParameter("monDossierWebFinished") != null)
			return true;
		return false;
	}

	/**
	 * Retourne les Personnes trouvées en résultat de la recherche
	 * @return Object[] (à caster en Personne), Null sinon
	 */
	public Object[] getResults() {
		return null;
	}

	/**
	 * Ajoute un paramètre au staticData du servant
	 */
	public void setStaticData(ChannelStaticData sd) throws PortalException {
		cod_ind = sd.getParameter("cod_ind");
		loginEtu = sd.getParameter("loginEtu");
		sd.setParameter("MonDossierWebServantMode", "true");
		super.setStaticData(sd);
	}
	
	public void renderXML(ContentHandler out) throws PortalException {
		// on passe à toutes les subchannel non servant le fait qu'elles travaillent en mode servant (feuille XSL)
		if (!currentAction.isServant())
			currentSubChannel.getXSLParameter().put("modeServant","1");
		super.renderXML(out);
	}	
	
}
