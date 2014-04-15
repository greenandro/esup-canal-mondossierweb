<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:import href="genEtudiant.xsl"/>
   <xsl:import href="Liens.xsl"/>
   <xsl:output method="html" indent="no"/>
   <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
   <xsl:param name="prefForm"/>
   <xsl:param name="modeServant">none</xsl:param>
   <xsl:param name="mediaPath"/>
   <xsl:param name="servantMailTo"/>
   <xsl:template match="/">
      <xsl:call-template name="liensHorizontaux">
         <xsl:with-param name="annuaire" select="$modeServant"/>
      </xsl:call-template>
      <table cellspacing="0" cellpadding="5" width="100%" border="0">
         <tr>
            <td nowrap="true" class="uportal-channel-table-header">Etat-civil</td>
            <td width="100%"/>
         </tr>
      </table>
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td height="25" class="uportal-channel-strong">GENERALITES</td>
         </tr>
      </table>
      <xsl:apply-templates select="xml/generalites"/>
      <xsl:apply-templates select="xml/naissance"/>
      <xsl:apply-templates select="xml/inscrUniv"/>
      <xsl:apply-templates select="xml/bacs"/>
   </xsl:template>
   <xsl:template match="generalites">
      <xsl:call-template name="genEtudiant">
         <xsl:with-param name="dossier" select="dossier"/>
         <xsl:with-param name="NNE" select="NNE"/>
         <xsl:with-param name="nom" select="nom"/>
         <xsl:with-param name="nom_usage" select="nom_usage"/>
         <xsl:with-param name="prenom" select="prenom"/>
         <xsl:with-param name="prenom2" select="prenom2"/>
         <xsl:with-param name="prenom3" select="prenom3"/>
         <xsl:with-param name="email" select="email"/>
         <xsl:with-param name="servantMailTo" select="$servantMailTo"/>
      </xsl:call-template>
   </xsl:template>
   <xsl:template match="naissance">
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td class="uportal-text">Nationalité</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="nationalite"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Né(e) le</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="datNaiss"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">A</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="lieu"/>
            </td>
         </tr>
         <tr>
            <td width="150" class="uportal-text">Département ou Pays</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="depPays"/>
            </td>
         </tr>
      </table>
   </xsl:template>
   <xsl:template match="inscrUniv">
      <br/>
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td height="25" class="uportal-channel-strong">INSCRIPTION UNIVERSITE</td>
         </tr>
      </table>
      <table BORDER="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="150" class="uportal-text">Année</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="annee"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Etablissement</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="etablissement"/>
            </td>
         </tr>
      </table>
   </xsl:template>
   <xsl:template match="bacs">
      <br/>
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td height="25" class="uportal-channel-strong">BAC</td>
         </tr>
      </table>
      <xsl:apply-templates select="bac"/>
   </xsl:template>
   <xsl:template match="bac">
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="150" class="uportal-text">Bac</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="type"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Obtenu en</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="datObtention"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Mention</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="mention"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Type établissement</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="typEtabliss"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Etablissement</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="etablissement"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Département</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="departement"/>
            </td>
         </tr>
      </table>
   </xsl:template>
</xsl:stylesheet>
