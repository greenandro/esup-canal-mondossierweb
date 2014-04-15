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
            <P ALIGN="CENTER" class="uportal-channel-strong">I N F O R M A T I O N S</P> 
          </TD>
        </TR>
      </TABLE>
      <xsl:choose>
              <xsl:when test="/xml/infos/LIB_INF_MINI != 'null' and /xml/infos/LIB_INF_MINI != ''">
      <TABLE BORDER="0" CELLPADDING="2" CELLSPACING="0" WIDTH="100%">
       <xsl:for-each select="/xml/infos">
		<TR>
		  <TD WIDTH="15%" class="uportal-text">
            &#160;
            </TD>
          <TD WIDTH="70%" class="uportal-text">
                  <xsl:value-of select="LIB_INF_MINI" />
         	</TD>
          <TD WIDTH="15%" class="uportal-text">
            &#160;
            </TD>
		</TR>
		</xsl:for-each>
        </TABLE>
        </xsl:when>
        <xsl:otherwise>
              <TABLE BORDER="0" CELLPADDING="2" CELLSPACING="0" WIDTH="100%">
        <TR> 
          <TD WIDTH="15%"> 
            <div align="left" class="uportal-channel-strong">&#160;<BR/>
            </div>
          </TD>
          <TD WIDTH="70%" class="uportal-channel-strong">Aucune information n'est disponible pour l'instant
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
          <TD WIDTH="15%"> 
            <div align="left" class="uportal-channel-strong">&#160;<BR/>
            </div>
          </TD>
        </TR>
        </TABLE>
        </xsl:otherwise>
        </xsl:choose>
<BR/>
       <form name="{$prefForm}MonDossierWeb" action="{$baseActionURL}?action=calRentAffiche&amp;etp={/xml/infos/etp}&amp;vet={/xml/infos/vet}" method="post">
 		<input type="hidden" value="default" name="action"/>
  		<center><input type="submit" value="suite" name="retour" class="uportal-button"/></center>
    </form>
    </xsl:template>        
 </xsl:stylesheet>      
       