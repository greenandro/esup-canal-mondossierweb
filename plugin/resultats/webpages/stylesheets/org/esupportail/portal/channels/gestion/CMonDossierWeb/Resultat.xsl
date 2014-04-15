<?xml version='1.0' encoding='UTF-8' ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:import href="genEtudiant.xsl" />
   <xsl:import href="Liens.xsl"/>
   <xsl:import href="CodesRes.xsl"/>
   <xsl:output method="html" encoding="ISO-8859-1" indent="no" />

   <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
   <xsl:param name="modeServant">none</xsl:param>

   <xsl:param name="prefForm" />
<xsl:param name="mediaPath" />

   <xsl:template match="/">
      <xsl:call-template name="liensHorizontaux">
         <xsl:with-param name="annuaire" select="$modeServant" />
      </xsl:call-template>
      <TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
         <TR>
            <TD WIDTH="100%" HEIGHT="40" class="uportal-background-light">
               <P ALIGN="CENTER" class="uportal-channel-strong">
                        <xsl:if test="not(xml/diplome/LIB_CMT_TRV)">
                           AUCUN RESULTAT DISPONIBLE
                        </xsl:if>
	                    <xsl:if test="xml/diplome/LIB_CMT_TRV">
	                       RESULTATS DISPONIBLES
	                    </xsl:if>
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

            <td width="35%" class="uportal-channel-strong">
               Diplôme

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

         <xsl:for-each select="xml/diplome">
         <tr onMouseOver="javascript:this.className='uportal-background-highlight'" onMouseOut="javascript:this.className='uportal-background-content'">
               <td class="uportal-text">
					<xsl:value-of select="COD_ANU_OBJ_MNP" />/20<xsl:value-of select="FULL_COD_ANU_OBJ_MNP" />
               </td>

              <td class="uportal-text">
                <a href="{$baseActionURL}?action=detailResultat&amp;rvn={COD_RVN}&amp;ind={COD_IND}&amp;libelle={LIB_CMT_TRV}">
                  <xsl:value-of select="LIB_CMT_TRV" /></a>
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


	  <xsl:call-template name="codesRes"/>	
      <form name="{$prefForm}MonDossierWeb" action="{$baseActionURL}" method="post">
         <input type="hidden" value="default" name="action" />

         <center>
            <input type="submit" value="Retour" name="retour" class="uportal-button"/>
         </center>
      </form>
   </xsl:template>


</xsl:stylesheet>

