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
            <td nowrap="true" class="uportal-channel-table-header">Calendrier des examens</td>
            <td align="right" width="100%">
               <xsl:call-template name="liensIdentite"/>
            </td>
         </tr>
      </table>
      <xsl:call-template name="identite"/>
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td width="100%" height="25" class="uportal-channel-strong">CALENDRIER</td>
         </tr>
      </table>
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="11%" class="uportal-channel-strong">Date <hr align="CENTER" size="1"
                  noshade="NOSHADE"/>
            </td>
            <td width="9%" class="uportal-channel-strong">Heure <hr align="CENTER" size="1"
                  noshade="NOSHADE"/>
            </td>
            <td width="9%" class="uportal-channel-strong">Durée <hr align="left" size="1"
                  noshade="NOSHADE"/>
            </td>
            <td width="21%" class="uportal-channel-strong">Bâtiment <hr align="CENTER" size="1"
                  noshade="NOSHADE"/>
            </td>
            <td width="7%" class="uportal-channel-strong">Salle <hr align="CENTER" size="1"
                  noshade="NOSHADE"/>
            </td>
            <td width="36%" class="uportal-channel-strong">Examen <hr align="CENTER" size="1"
                  noshade="NOSHADE"/>
            </td>
            <xsl:if test="count(xml/examens/examen/place) != 0">
               <td width="7%" class="uportal-channel-strong">N&#186; de place <hr align="CENTER" size="1" noshade="NOSHADE"/>
               </td>
            </xsl:if>   
         </tr>
      </table>
      <xsl:choose>
         <xsl:when test="count(xml/examens/examen) = 0">
            <p align="CENTER" class="uportal-text">Pas de calendrier disponible</p>
         </xsl:when>
         <xsl:otherwise>
            <table border="0" cellpadding="2" cellspacing="0" width="100%">
               <xsl:apply-templates select="xml/examens/examen"/>
            </table>
            <xsl:if test="xml/examens/cmtCalExamen != ''">
               <p>
                  <hr size="1"/>
                  <b><xsl:value-of select="xml/examens/cmtCalExamen"/></b>
               </p>
            </xsl:if>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
   <xsl:template match="examen">
      <tr onMouseOver="javascript:this.className='uportal-background-highlight'"
         onMouseOut="javascript:this.className='uportal-background-content'">
         <td width="11%" class="uportal-text">
            <xsl:value-of select="date"/>
         </td>
         <td width="9%" class="uportal-text">
            <xsl:value-of select="heure"/>
         </td>
         <td width="9%" class="uportal-text">
            <xsl:value-of select="duree"/>
         </td>
         <td width="21%" class="uportal-text">
            <xsl:value-of select="batiment"/>
         </td>
         <td width="7%" class="uportal-text">
            <xsl:value-of select="salle"/>
         </td>
         <td width="36%" class="uportal-text">
            <xsl:value-of select="epreuve"/>
         </td>
         <xsl:if test="place">
            <td width="7%" class="uportal-text"><xsl:value-of select="place"/></td>
         </xsl:if> 
      </tr>
   </xsl:template>
</xsl:stylesheet>
