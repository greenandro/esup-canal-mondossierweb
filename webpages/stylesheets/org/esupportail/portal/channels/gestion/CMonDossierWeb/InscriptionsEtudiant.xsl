<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:import href="Identite.xsl"/>
   <xsl:import href="Liens.xsl"/>
   <xsl:output method="html" indent="no"/>
   <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
   <xsl:param name="prefForm"/>
   <xsl:param name="modeServant">none</xsl:param>
   <xsl:param name="mediaPath"/>
   <xsl:template match="/">
      <xsl:call-template name="liensHorizontaux">
         <xsl:with-param name="annuaire" select="$modeServant"/>
      </xsl:call-template>
      <table cellspacing="0" cellpadding="5" width="100%" border="0">
         <tr>
            <td nowrap="true" class="uportal-channel-table-header">Inscriptions</td>
            <td align="right" width="100%">
               <xsl:call-template name="liensIdentite"/>
            </td>
         </tr>
      </table>
      <xsl:call-template name="identite"/>
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td width="100%" height="25" class="uportal-channel-strong"><xsl:value-of select="xml/etablissementDef/@lib_etb"/></td>
         </tr>
      </table>
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="14%" class="uportal-channel-strong">Année <hr align="CENTER" size="1"
                  noshade="noshade"/>
            </td>
            <td width="10%" class="uportal-channel-strong">Code <hr align="CENTER" size="1"/>
            </td>
            <td width="9%" class="uportal-channel-strong">Vers. <hr align="CENTER" size="1"
                  noshade="noshade"/>
            </td>
            <td width="67%" class="uportal-channel-strong">Etape <hr align="CENTER" size="1"
                  noshade="noshade"/>
            </td>
         </tr>
      </table>
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <xsl:apply-templates select="xml/inscriptions/inscription[@id='IAE']"/>
      </table>
      <br/>
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td width="100%" height="25" class="uportal-channel-strong">AUTRES CURSUS</td>
         </tr>
      </table>
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="14%" class="uportal-channel-strong">Année <hr align="CENTER" size="1"
                  noshade="noshade"/>
            </td>
            <td width="10%" class="uportal-channel-strong">Type <hr align="CENTER" size="1"
                  noshade="noshade"/>
            </td>
            <td width="16%" class="uportal-channel-strong">Spécialité <hr align="CENTER" size="1"
                  noshade="noshade"/>
            </td>
            <td width="46%" class="uportal-channel-strong">Etablissement <hr align="CENTER" size="1"
                  noshade="noshade"/>
            </td>
            <td width="14%" class="uportal-channel-strong">Résultat <hr align="CENTER" size="1"
                  noshade="noshade"/>
            </td>
         </tr>
      </table>
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <xsl:apply-templates select="xml/inscriptions/inscription[@id='DAC']"/>
      </table>
      <br/>
      <xsl:call-template name="premiere"/>
   </xsl:template>
   <xsl:template match="inscription">
      <tr onMouseOver="javascript:this.className='uportal-background-highlight'"
         onMouseOut="javascript:this.className='uportal-background-content'">
         <td width="14%" class="uportal-text">
            <xsl:value-of select="annee"/>
         </td>
         <xsl:if test="@id='IAE'">
            <td width="10%" class="uportal-text">
               <xsl:value-of select="code"/>
            </td>
            <td width="9%" class="uportal-text">
               <xsl:value-of select="vers"/>
            </td>
            <td width="67%" class="uportal-text">
               <xsl:value-of select="etape"/>
            </td>
         </xsl:if>
         <xsl:if test="@id='DAC'">
            <td width="10%" class="uportal-text">
               <xsl:value-of select="type"/>
            </td>
            <td width="16%" class="uportal-text">
               <xsl:value-of select="specialite"/>
            </td>
            <td width="46%" class="uportal-text">
               <xsl:value-of select="etablissement"/>
            </td>
            <td width="14%" class="uportal-text">
               <xsl:value-of select="resultat"/>
            </td>
         </xsl:if>
      </tr>
   </xsl:template>
   <xsl:template name="premiere">
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td width="100%" height="25" class="uportal-channel-strong">PREMIERE INSCRIPTION
               UNIVERSITE</td>
         </tr>
      </table>
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="150" class="uportal-text">Année</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="xml/inscriptions/@anneeDebut"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Etablissement</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="xml/inscriptions/@etablissementDebut"/>
            </td>
         </tr>
      </table>
   </xsl:template>
</xsl:stylesheet>
