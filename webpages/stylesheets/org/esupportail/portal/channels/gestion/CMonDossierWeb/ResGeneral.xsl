<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:import href="Identite.xsl"/>
   <xsl:import href="Liens.xsl"/>
   <xsl:import href="CodesRes.xsl"/>
   <xsl:output method="html" indent="yes"/>
   <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
   <xsl:param name="prefForm"/>
   <xsl:param name="modeServant">none</xsl:param>
   <xsl:param name="vueEtudiant">none</xsl:param>
   <xsl:param name="mediaPath"/>
   <xsl:param name="baseDownloadURL"/>
   <!-- Indique si l'édition pdf des notes est activée ou pas -->
   <xsl:param name="notesPDF">false</xsl:param>
   
   <xsl:template match="/">
      <xsl:call-template name="liensHorizontaux">
         <xsl:with-param name="annuaire" select="$modeServant"/>
      </xsl:call-template>
      <!-- Formulaire pour le passage des paramètres lors de l'appel du pdf -->
      <xsl:if test="$notesPDF='true'">
         <form name="{$prefForm}_paramsPdf" action="{$baseDownloadURL}" method="post"> 
            <input type="hidden" name="action" value="notesPDF"/>
            <!-- Ajout test CC - 04/06/08 -->
            <xsl:if test="$modeServant!='none'">
	            <xsl:choose>
	               <xsl:when test="$vueEtudiant = 'none'">
	                  <input type="hidden" name="isProf" value="O"/>               
	               </xsl:when>
	               <xsl:otherwise>
	                  <input type="hidden" name="isProf" value="N"/>
	               </xsl:otherwise>
	            </xsl:choose>
            </xsl:if>
            <input type="hidden" name="code" value="{/xml/etape/@code}/{/xml/etape/@version}"/>
            <input type="hidden" name="codeAnnu" value="{/xml/etape/@annee}"/>
         </form>
      </xsl:if>
      <table cellspacing="0" cellpadding="5" width="100%" border="0">
         <tr>
            <td nowrap="true" class="uportal-channel-table-header">Résultats</td>
            <xsl:if test="$notesPDF='true'">
               <td nowrap="true" class="uportal-channel-strong" valign="absmiddle" align="left">
                  <a href="#" onClick="javascript:document.{$prefForm}_paramsPdf.submit()">
                     <img src="{$mediaPath}docpdf.gif" border="0" alt="Fichier PDF" title="Fichier PDF"/>
                  </a>
               </td>
            </xsl:if>
            <td align="right" width="100%">
               <xsl:call-template name="liensIdentite"/>
            </td>
         </tr>
      </table>
      <xsl:if test="$modeServant!='none'">
         <form name="formVue" action="{$baseActionURL}" method="post">
            <input type="hidden" name="isProf" value=""/>
            <input type="hidden" name="action" value="notes"/>
            <table>
               <tr>
                  <td>
                     <input type="checkbox" name="checkVue"
                        onClick="javascript:if (document.formVue.checkVue.checked) document.formVue.isProf.value='N'; else document.formVue.isProf.value='O'; document.formVue.submit();">
                        <xsl:if test="$vueEtudiant != 'none'">
                           <xsl:attribute name="checked">
                              <xsl:value-of select="$vueEtudiant"/>
                           </xsl:attribute>
                        </xsl:if>
                     </input>
                  </td>
                  <td class="uportal-text">Voir comme un étudiant</td>
               </tr>
               <tr>
                  <td/>
                  <td class="uportal-channel-text">Cette option vous permet de vérifier ce que
                     l'étudiant verra lorsqu'il consultera ses résultats</td>
               </tr>
            </table>
         </form>
      </xsl:if>
      <xsl:call-template name="identite"/>
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td width="100%" height="25" class="uportal-channel-strong">DIPLOMES</td>
         </tr>
      </table>
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="13%" class="uportal-channel-strong">Année <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="15%" class="uportal-channel-strong">Code / Vers. <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="30%" class="uportal-channel-strong">Diplôme <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="13%" colspan="2" class="uportal-channel-strong">Session <hr align="CENTER"
               size="1" noshade="NSOSHADE"/>
            </td>
            <td width="8%" class="uportal-channel-strong">Note <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="8%" class="uportal-channel-strong">Résultat <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
         </tr>
         <xsl:for-each select="xml/diplome">
            <tr onMouseOver="javascript:this.className='uportal-background-highlight'"
               onMouseOut="javascript:this.className='uportal-background-content'">
               <td colspan="7">
                  <table border="0" width="100%" cellpadding="0" cellspacing="0">
                     <tr>
                        <td class="uportal-text" width="15%">
                           <xsl:value-of select="@annee"/>
                        </td>
                        <td class="uportal-text" width="17%">
                           <xsl:value-of select="@code"/>
                        </td>
                        <td class="uportal-text" width="35%">
                           <xsl:value-of select="@libelle"/>
                        </td>
                        <td colspan="3"/>
                     </tr>
                     <xsl:for-each select="session">
                        <xsl:call-template name="session"/>
                     </xsl:for-each>
                  </table>
               </td>
            </tr>
         </xsl:for-each>
      </table>
      <br/>
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td WIDTH="100%" height="25" class="uportal-channel-strong">ETAPES</td>
         </tr>
      </table>
      <!-- Formulaire permettant de gerer les codes d'etapes contenant des #
         Interpreter comme des ancres si passage en GET -->
      <form name="{$prefForm}_params" action="{$baseActionURL}" method="post">
         <input type="hidden" name="action" value="detailNotes"/>
         <input type="hidden" name="code"/>
         <input type="hidden" name="codeAnnu"/>
      </form>    
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="13%" class="uportal-channel-strong">Année <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="15%" class="uportal-channel-strong">Code / Vers. <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="30%" class="uportal-channel-strong">Etape <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="13%" colspan="2" class="uportal-channel-strong">Session <hr align="CENTER"
               size="1" noshade="NSOSHADE"/>
            </td>
            <td width="8%" class="uportal-channel-strong">Note <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="8%" class="uportal-channel-strong">Résultat <hr align="CENTER" size="1"
               noshade="NSOSHADE"/>
            </td>
         </tr>
         <xsl:for-each select="xml/etape">
            <tr onMouseOver="javascript:this.className='uportal-background-highlight'"
               onMouseOut="javascript:this.className='uportal-background-content'">
               <td colspan="7">
                  <table border="0" width="100%" cellpadding="0" cellspacing="0">
                     <tr>
                        <td class="uportal-text" width="15%">
                           <xsl:value-of select="@annee"/>
                        </td>
                        <td class="uportal-text" width="17%">
                           <xsl:choose>
                              <xsl:when test="local-name(.) = 'etape'">
                                 <table>
                                    <tr>
                                       <td valign="absmiddle">
                                          <xsl:call-template name="liensEtapes">
                                             <xsl:with-param name="label">img</xsl:with-param>
                                          </xsl:call-template>
                                       </td>
                                       <td class="uportal-text">
                                          <xsl:call-template name="liensEtapes">
                                             <xsl:with-param name="label"><xsl:value-of select="@code"/></xsl:with-param>
                                          </xsl:call-template>
                                       </td>
                                    </tr>
                                 </table>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:value-of select="@code"/>
                              </xsl:otherwise>
                           </xsl:choose>
                        </td>
                        <td class="uportal-text" width="35%">
                           <xsl:choose>
                              <xsl:when test="local-name(.) = 'etape'">
                                 <xsl:call-template name="liensEtapes">
                                    <xsl:with-param name="label"><xsl:value-of select="@libelle"/></xsl:with-param>
                                 </xsl:call-template>
                              </xsl:when>
                              <xsl:otherwise>
                                 <xsl:value-of select="@libelle"/>
                              </xsl:otherwise>
                           </xsl:choose>
                        </td>
                        <td colspan="3"/>
                     </tr>
                     <xsl:for-each select="session">
                        <xsl:call-template name="session"/>
                     </xsl:for-each>
                  </table>
               </td>
            </tr>
         </xsl:for-each>
      </table>
      <br/>
      <xsl:call-template name="codesRes"/>
   </xsl:template>
   <xsl:template name="session">
      <xsl:if
         test="(@note != 'null' and @note != ' ') or (@resultat != ' ' and @resultat != 'null')">
         <tr>
            <td class="uportal-text" colspan="3"/>
            <td class="uportal-text" width="15%">
               <xsl:value-of select="@type"/>
            </td>
            <td class="uportal-text" width="10%">
               <xsl:if test="@note != 'null'">
                  <xsl:value-of select="@note"/>
               </xsl:if>
            </td>
            <td class="uportal-text">
               <xsl:if test="@resultat != 'null'">
                  <xsl:value-of select="@resultat"/>
               </xsl:if>
            </td>
         </tr>
      </xsl:if>
   </xsl:template>
   
   <!-- Template permettant de construire les liens Etapes -->
   <xsl:template name="liensEtapes">
      <xsl:param name="label"/>
      <a href="#" onclick="javascript:document.{$prefForm}_params.code.value='{@code}';document.{$prefForm}_params.codeAnnu.value='{@codeAnnu}';document.{$prefForm}_params.submit()">
         <xsl:choose>
            <xsl:when test="$label='img'">
               <img alt="Détail des notes" border="0" src="{$mediaPath}/folderopen.gif"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="$label"/>
            </xsl:otherwise>
         </xsl:choose>    
      </a>
   </xsl:template>
   
</xsl:stylesheet>
