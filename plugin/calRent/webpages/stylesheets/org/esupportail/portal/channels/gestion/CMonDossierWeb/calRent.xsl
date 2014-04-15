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
            <P ALIGN="CENTER" class="uportal-channel-strong">C A L E N D R I E R&#160;&#160;D E&#160;&#160;R E N T R E E </P> 
          </TD>
        </TR>
      </TABLE>
      <xsl:choose>
              <xsl:when test="/xml/etapes/etape/libEtp != 'null' and /xml/etapes/etape/libEtp != ''">
      <TABLE BORDER="0" CELLPADDING="2" CELLSPACING="0" WIDTH="100%">
        <TR> 
          <TD WIDTH="15%"> 
            <div align="left" class="uportal-channel-strong">&#160;<BR/>
            </div>
          </TD>
          <TD WIDTH="70%" class="uportal-channel-strong">Liste des calendriers avec étapes
            <HR ALIGN="CENTER" SIZE="1" NOSHADE="NOSHADE"/>
          </TD>
          <TD WIDTH="15%"> 
            <div align="left" class="uportal-channel-strong">&#160;<BR/>
            </div>
          </TD>
        </TR>
        <xsl:for-each select="/xml/etapes/etape">
		<TR onMouseOver="javascript:this.className='uportal-background-highlight'" onMouseOut="javascript:this.className='uportal-background-content'">
		  <TD class="uportal-text">
            &#160;
            </TD>
          <TD class="uportal-text">
                <a href="{$baseActionURL}?action=calRentPageInfo&amp;etp={etp}&amp;vet={vet}">
                  <xsl:value-of select="libEtp" /></a>
         	</TD>
          <TD class="uportal-text">
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
          <TD WIDTH="70%" class="uportal-channel-strong">Aucun calendrier n'est disponible pour l'instant
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
       <form name="{$prefForm}MonDossierWeb" action="{$baseActionURL}" method="post">
 		<input type="hidden" value="default" name="action"/>
  		<center><input type="submit" value="Retour" name="retour" class="uportal-button"/></center>
    </form>
    </xsl:template>        
 </xsl:stylesheet>      
       