<?xml version='1.0' encoding='UTF-8' ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="genEtudiant.xsl"/>
<xsl:import href="Liens.xsl"/>
<xsl:output method="html" encoding="ISO-8859-1" indent="no" />
<xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
<xsl:param name="prefForm"/>
<xsl:param name="mediaPath" />
<xsl:param name="modeServant">none</xsl:param>

<xsl:template match="/">
      <xsl:call-template name="liensHorizontaux">
         <xsl:with-param name="annuaire" select="$modeServant" />
      </xsl:call-template>
       <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
        <TR> 
          <TD WIDTH="100%" HEIGHT="40" class="uportal-background-light"> 
            <P ALIGN="CENTER" class="uportal-channel-table-caption">CALENDRIER D'EXAMENS POUR L'ETAPE : <xsl:value-of select="/xml/calendriers/calendrier/LIB_ETP"/></P> 
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
            <div align="left" class="uportal-channel-strong">Heure deb.<BR/>
            </div>
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
          <TD WIDTH="10%">
            <div align="left" class="uportal-channel-strong">Heure fin<BR/>
            </div>
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
          <TD WIDTH="35%"> 
            <div align="left" class="uportal-channel-strong">Epreuve<BR/>
            </div>
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
          <TD WIDTH="35%"> 
            <div align="left" class="uportal-channel-strong">Localisation<BR/>
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
		  <xsl:if test="HEURE_DEB != 'null' and HEURE_DEB != ''">
			<xsl:value-of select="HEURE_DEB" />
		  </xsl:if>           
         	</TD>
          <TD class="uportal-text">
		  <xsl:if test="HEURE_FIN != 'null' and HEURE_FIN != ''">
			<xsl:value-of select="HEURE_FIN" />
		  </xsl:if>           
         	</TD>
          <TD class="uportal-text">
		  <xsl:if test="LIB_EPR != 'null' and LIB_EPR != ''">
			<xsl:value-of select="LIB_EPR" /> 
		  </xsl:if>           
            </TD>
          <TD class="uportal-text">
          <xsl:for-each select='localisation'>
		  <xsl:if test="LIB_BATIMENT != 'null' and LIB_BATIMENT != ''">
			<xsl:value-of select="LIB_BATIMENT" /><BR/><xsl:value-of select="LIB_SALLE" /><BR/>
		  </xsl:if>        
		  </xsl:for-each>   
            </TD>
		</TR>
		<TR>
			<TD>&#160;</TD>
			<TD>&#160;</TD>
			<TD>&#160;</TD>
			<TD>&#160;</TD>
			<TD>&#160;</TD>
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
       