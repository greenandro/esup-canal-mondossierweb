<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:import href="Identite.xsl"/>
   <xsl:import href="Liens.xsl"/>
   <xsl:output method="html" indent="yes"/>
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
            <td nowrap="true" class="uportal-channel-table-header">Adresses</td>
            <td align="right" width="100%">
               <xsl:call-template name="liensIdentite"/>
            </td>
         </tr>
      </table>
      <xsl:call-template name="identite"/>
      <xsl:apply-templates select="xml/adrAnnuelle"/>
      <br/>
      <xsl:apply-templates select="xml/adrFixe"/>
   </xsl:template>
   <xsl:template match="adrAnnuelle">
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td width="100%" height="25" class="uportal-channel-strong"> ADRESSE ANNUELLE
                  <xsl:text> </xsl:text><xsl:value-of select="anu"/>
            </td>
         </tr>
      </table>
      <xsl:call-template name="adresse"/>
   </xsl:template>
   <xsl:template match="adrFixe">
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td width="100%" height="25" class="uportal-channel-strong">ADRESSE FIXE</td>
         </tr>
      </table>
      <xsl:call-template name="adresse"/>
   </xsl:template>
   <xsl:template name="adresse">
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="150" class="uportal-text">Adresse</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="adresse1"/>
            </td>
         </tr>
         <tr>
            <td/>
            <td class="uportal-channel-strong">
               <xsl:value-of select="adresse2"/>
            </td>
         </tr>
         <xsl:if test="adresse3!=''">
            <tr>
               <td/>
               <td class="uportal-channel-strong">
                  <xsl:value-of select="adresse3"/>
               </td>
            </tr>
         </xsl:if>
         <tr>
            <td class="uportal-text">Ville</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="ville"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Pays</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="pays"/>
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Téléphone</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="tel"/>
            </td>
         </tr>
      </table>
   </xsl:template>
</xsl:stylesheet>
