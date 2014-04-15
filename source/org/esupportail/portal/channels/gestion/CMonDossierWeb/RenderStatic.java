/*
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:olivier.ziller@univ-nancy2.fr">Olivier Ziller</a>
 * @version 1.0
 */ 
package org.esupportail.portal.channels.gestion.CMonDossierWeb;

import org.esupportail.portal.utils.channels.MainChannel;
import org.esupportail.portal.utils.channels.SubChannel;

/**
 * <p>RenderStatic</p>
 * <p>Sub-channel pour les pages statiques de la channel CMonDossierWeb</p>
 * (c)Copyright <a href="www.esup-portail.org">ESup-Portail 2004</a>
 * @author <a href="mailto:olivier.ziller@univ-nancy2.fr">Olivier Ziller</a>
 * @version 1.1
 *
 */
public class RenderStatic extends SubChannel {

	// Classe principale de la channel
	private CMonDossierWeb owner;
	
	/**
	 * Constructeur
	 * @param main
	 */
	public RenderStatic(MainChannel main) {
		super(main);
		owner = (CMonDossierWeb)main;
	}
		
}
