<?xml version='1.0' encoding='UTF-8' ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:import href="genEtudiant.xsl" />
   <xsl:import href="Liens.xsl"/>
   <xsl:import href="CodesRes.xsl"/>
   <xsl:output method="html" indent="no" encoding="UTF-8"/>

   <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
   <xsl:param name="modeServant">none</xsl:param>

   <xsl:param name="prefForm" />
	<xsl:param name="mediaPath" />


   <xsl:template name="max">
   <xsl:param name="nodes"
              select="/.." />
   <xsl:choose>
      <xsl:when test="not($nodes)">NaN</xsl:when>
      <xsl:otherwise>
         <xsl:for-each select="$nodes">
            <xsl:sort data-type="number"
                      order="descending" />
            <xsl:if test="position() = 1">
               <xsl:value-of select="number(.)" />
            </xsl:if>
         </xsl:for-each>
      </xsl:otherwise>
     </xsl:choose>
   </xsl:template>

	<xsl:template name="td-gen">
	    <xsl:param name="index" select="0"/>
	    <xsl:if test="$index>0">
	        <td width="3%">&#160;</td>
	        <xsl:call-template name="td-gen">
	            <xsl:with-param name="index" select="$index - 1"/>
	        </xsl:call-template>
	    </xsl:if>
	</xsl:template> 
   <xsl:variable name="colspan">
   <xsl:call-template name="max">
	   <xsl:with-param name="nodes" select="xml/detail/DEC_OBJ_MNP" />
   </xsl:call-template>
   </xsl:variable>

   <xsl:template match="/">
      <xsl:call-template name="liensHorizontaux">
         <xsl:with-param name="annuaire" select="$modeServant" />
      </xsl:call-template>

      <xsl:apply-templates select="/xml" />
      <xsl:call-template name="codesRes"/>
      <form name="{$prefForm}MonDossierWeb" action="{$baseActionURL}" method="post">
         <input type="hidden" value="Resultat" name="action" />

         <center>
            <input type="submit" value="Retour" name="retour" class="uportal-button"/>
         </center>
      </form>
     </xsl:template>

 
    <xsl:template match="/xml">
      <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
         <TR>
            <TD WIDTH="100%" HEIGHT="40" class="uportal-background-light">
               <P ALIGN="CENTER" class="uportal-channel-strong">
                  DETAILS DES NOTES DE L'ETAPE : <xsl:value-of select="LIBELLE_ETAPE" />
               </P>
            </TD>
         </TR>
      </TABLE>

       <TABLE BORDER="0" CELLPADDING="2" CELLSPACING="0" WIDTH="100%">
         <tr>
            <td width="10%" class="uportal-channel-strong">
               Année

               <hr align="CENTER" size="1" NOSHADE="NSOSHADE" />
            </td>

            <td width="35%" class="uportal-channel-strong"><xsl:attribute name="colspan"><xsl:value-of select="$colspan + 1" /></xsl:attribute>
               UE / Matières

               <hr align="CENTER" size="1" NOSHADE="NSOSHADE" />
            </td>

            <td width="5%" class="uportal-channel-strong">
               Session

               <hr align="CENTER" size="1" NOSHADE="NSOSHADE" />
            </td>

            <td width="10%" class="uportal-channel-strong">
               Note

               <hr align="CENTER" size="1" NOSHADE="NSOSHADE" />
            </td>

             <td width="10%" class="uportal-channel-strong">
               Pt Jury

               <hr align="CENTER" size="1" NOSHADE="NSOSHADE" />
            </td>

            <td width="10%" class="uportal-channel-strong">
               Résultat

               <hr align="CENTER" size="1" NOSHADE="NSOSHADE" />
            </td>

            <td width="10%" class="uportal-channel-strong">
               Mention

               <hr align="CENTER" size="1" NOSHADE="NSOSHADE" />
            </td>

            <td width="10%" class="uportal-channel-strong">
               Rang

               <hr align="CENTER" size="1" NOSHADE="NSOSHADE" />
            </td>

         </tr>

         <xsl:for-each select="detail">
         <tr onMouseOver="javascript:this.className='uportal-background-highlight'" onMouseOut="javascript:this.className='uportal-background-content'">
               <td class="uportal-text">
					<xsl:value-of select="COD_ANU_OBJ_MNP" />/20<xsl:value-of select="FULL_COD_ANU_OBJ_MNP" />
               </td>


		<xsl:call-template name="td-gen">
         <xsl:with-param name="index" select="DEC_OBJ_MNP" />
		</xsl:call-template>
			<td class="uportal-text"><xsl:attribute name="colspan"><xsl:value-of select="($colspan)-DEC_OBJ_MNP+1"/></xsl:attribute>
                  - <xsl:value-of select="LIB_CMT_TRV" />
               </td>

             <td class="uportal-text">
               <xsl:value-of select="COD_SES_OBJ_MNP" />
            </td>         

            <td class="uportal-text">
               <xsl:if test="NOT_TRV != 'null' and NOT_TRV != ''">
                  <xsl:value-of select="NOT_TRV" />/<xsl:value-of select="BAR_NOT_TRV" />
               </xsl:if>
            </td>

             <td class="uportal-text">
               <xsl:if test="NOT_PNT_JUR_TRV != 'null' and NOT_PNT_JUR_TRV != ''">
                  <xsl:value-of select="NOT_PNT_JUR_TRV" />
              </xsl:if> 
            </td>
 
               <td class="uportal-text">
               <xsl:if test="COD_TRE != 'null' and COD_TRE != ''">
					<xsl:value-of select="COD_TRE" />
              </xsl:if>
 			   </td>
 
            <td class="uportal-text">
               <xsl:if test="LIC_MEN != 'null' and LIC_MEN != ''">
                  <xsl:value-of select="LIC_MEN" />
              </xsl:if> 
            </td>

            <td class="uportal-text">
               <xsl:if test="NBR_RNG_ETU_TRV != 'null' and NBR_RNG_ETU_TRV != ''">
                  <xsl:value-of select="NBR_RNG_ETU_TRV" />/<xsl:value-of select="NBR_TOT_RNG_TRV"/>
              </xsl:if> 
            </td>
          </tr>   
         </xsl:for-each>
      </TABLE>

   </xsl:template>

 </xsl:stylesheet>

