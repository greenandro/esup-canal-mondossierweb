<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:import href="Liens.xsl" />

   <xsl:output method="html" indent="no" />

   <xsl:param name="baseActionURL">
   </xsl:param>

   <xsl:param name="prefForm">
   </xsl:param>

   <xsl:param name="mediaPath" />

   <xsl:param name="modeServant">none</xsl:param>

   <xsl:template match="/">
      <xsl:call-template name="liensHorizontaux">
         <xsl:with-param name="annuaire" select="$modeServant" />
      </xsl:call-template>

      <table cellspacing="0" cellpadding="5" width="100%" border="0">
         <tr>
            <td nowrap="true" class="uportal-channel-table-header">Accueil</td>

            <td width="100%">
            </td>
         </tr>
      </table>

      <p align="justify" class="uportal-channel-text">Ce canal a pour but de vous permettre de consulter votre dossier Etudiant. En acc�dant � votre dossier, vous trouverez les informations suivantes : 
      <ul>
         <li>Etat-civil : Nom, pr�noms, date de naissance, num�ro de dossier, baccalaur�at...</li>

         <li>Adresses : Adresse annuelle, Adresse fixe</li>

         <li>Inscriptions : La liste de toutes vos inscriptions � l'Universit�...</li>

         <li>Calendrier des exmanens : Dates, Salles, Dur�es...</li>

         <li>Notes et r�sultats : Tous vos r�sultats aux dipl�mes, ann�es interm�diaires, �l�ments p�dagogiques et �preuves. 
<!-- Decommenter la ligne ci dessous et commenter celle ci dessus en cas d utilisation du plugin Resultat -->
<!--
         <li>Notes et r�sultats : Vos notes et r�sultats de l'ann�e en cours
-->
         <br />

         <b>Remarque :</b>

         Les r�sultats sont visibles d�s leur publication par les Scolarit�s.</li>
      </ul>
      </p>

      <br />
   </xsl:template>
</xsl:stylesheet>

