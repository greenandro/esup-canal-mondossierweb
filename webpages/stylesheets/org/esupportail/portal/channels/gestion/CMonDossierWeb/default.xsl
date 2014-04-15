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

      <p align="justify" class="uportal-channel-text">Ce canal a pour but de vous permettre de consulter votre dossier Etudiant. En accédant à votre dossier, vous trouverez les informations suivantes : 
      <ul>
         <li>Etat-civil : Nom, prénoms, date de naissance, numéro de dossier, baccalauréat...</li>

         <li>Adresses : Adresse annuelle, Adresse fixe</li>

         <li>Inscriptions : La liste de toutes vos inscriptions à l'Université...</li>

         <li>Calendrier des exmanens : Dates, Salles, Durées...</li>

         <li>Notes et résultats : Tous vos résultats aux diplômes, années intermédiaires, éléments pédagogiques et épreuves. 
<!-- Decommenter la ligne ci dessous et commenter celle ci dessus en cas d utilisation du plugin Resultat -->
<!--
         <li>Notes et résultats : Vos notes et résultats de l'année en cours
-->
         <br />

         <b>Remarque :</b>

         Les résultats sont visibles dès leur publication par les Scolarités.</li>
      </ul>
      </p>

      <br />
   </xsl:template>
</xsl:stylesheet>

