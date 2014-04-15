<?xml version='1.0' encoding='UTF-8' ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="genEtudiant.xsl"/>
<xsl:import href="Liens.xsl"/>
<xsl:output method="html" encoding="ISO-8859-1" indent="no" />
<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
<xsl:param name="mediaPath" />
<xsl:param name="prefForm"/>
   <xsl:param name="modeServant">none</xsl:param>

<xsl:template match="/">
      <xsl:call-template name="liensHorizontaux">
         <xsl:with-param name="annuaire" select="$modeServant" />
      </xsl:call-template>
       <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
        <TR> 
          <TD WIDTH="100%" HEIGHT="40" class="uportal-background-light"> 
            <P ALIGN="CENTER" class="uportal-channel-table-caption">CALENDRIER DE RENTREE POUR L'ETAPE : <xsl:value-of select="/xml/calendriers/calendrier/LIB_ETP"/></P> 
          </TD>
        </TR>
      </TABLE>
      <TABLE BORDER="0" CELLPADDING="2" CELLSPACING="0" WIDTH="100%">
        <TR> 
          <TD WIDTH="10%"> 
            <div align="left" class="uportal-channel-strong">Date<BR/>
            </div>
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
          <TD WIDTH="10%">
            <div align="left" class="uportal-channel-strong">Heure<BR/>
            </div>
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
          <TD WIDTH="40%"> 
            <div align="left" class="uportal-channel-strong">Lieu<BR/>
            </div>
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
          <TD WIDTH="40%"> 
            <div align="left" class="uportal-channel-strong">Commentaire<BR/>
            </div>
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
        </TR>
        <xsl:for-each select="/xml/calendriers/calendrier">
		<TR onMouseOver="javascript:this.className='uportal-background-highlight'" onMouseOut="javascript:this.className='uportal-background-content'">
		  <TD class="uportal-text">
		  <xsl:if test="DAT_DEB != 'null' and DAT_DEB != ''">
			<xsl:value-of select="DAT_DEB" /> 
		  </xsl:if>           
		  </TD>
          <TD class="uportal-text">
		  <xsl:if test="HRE_DEB != 'null' and HRE_DEB != ''">
		  <xsl:choose>
		  <xsl:when test="MIN_DEB = '0'">
			<xsl:value-of select="HRE_DEB" />h<xsl:value-of select="MIN_DEB" />0
			</xsl:when>
			<xsl:otherwise>
			<xsl:value-of select="HRE_DEB"/>h<xsl:value-of select="MIN_DEB" />
			</xsl:otherwise>
			</xsl:choose> 
		  </xsl:if>           
         	</TD>
          <TD class="uportal-text">
		  <xsl:if test="LIB_LIEU != 'null' and LIB_LIEU != ''">
			<xsl:value-of select="LIB_LIEU" /> 
		  </xsl:if>           
            </TD>
          <TD class="uportal-text">
		  <xsl:if test="COMMENTAIRE != 'null' and COMMENTAIRE != ''">
			<xsl:value-of select="COMMENTAIRE" /> 
		  </xsl:if>           
            </TD>
		</TR>
		</xsl:for-each>
        </TABLE>
<BR/>
       <form name="{$prefForm}MonDossierWeb" action="{$baseActionURL}" method="post">
 		<input type="hidden" value="calRent" name="action"/>
  		<center><input type="submit" value="Retour" name="retour" class="uportal-button"/></center>
    </form>
    </xsl:template>        
 </xsl:stylesheet>      
       