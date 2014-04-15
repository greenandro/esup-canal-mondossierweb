<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:import href="Identite.xsl"/>
   <xsl:import href="Liens.xsl"/>
   <xsl:import href="CodesRes.xsl"/>
   <xsl:output method="html" indent="no"/>
   <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>
   <xsl:param name="prefForm"/>
   <xsl:param name="modeServant">none</xsl:param>
   <xsl:param name="vueEtudiant">none</xsl:param>
   <xsl:param name="code">none</xsl:param>
   <xsl:param name="codeAnnu">none</xsl:param>
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
            <input type="hidden" name="action" value="detailNotesPDF"/>
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
            <td nowrap="true" class="uportal-channel-table-header">Notes</td>
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action=notes">
                  <img src="{$mediaPath}/back.gif" border="0" alt="Retour"/>
               </a>
            </td>
            <td class="uportal-label" nowrap="true">
               <a href="{$baseActionURL}?action=notes">Retour</a>
            </td>
            <td align="right" width="100%">
               <xsl:call-template name="liensIdentite"/>
            </td>
         </tr>
      </table>
      <xsl:if test="$modeServant!='none'">
         <form name="formVue" action="{$baseActionURL}" method="post">
            <input type="hidden" name="action" value="detailNotes"/>
            <input type="hidden" name="isProf" value=""/>
            <input type="hidden" name="code" value="{$code}"/>
            <input type="hidden" name="codeAnnu" value="{$codeAnnu}"/>
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
            <td height="25" class="uportal-channel-strong" nowrap="nowrap">ELEMENTS &amp;
               EPREUVES</td>
            <xsl:if test="$notesPDF='true'">
               <td nowrap="true" class="uportal-channel-strong" valign="absmiddle" align="left">
                  <a href="#" onClick="javascript:document.{$prefForm}_paramsPdf.submit()">
                     <img src="{$mediaPath}docpdf.gif" border="0" alt="Fichier PDF" title="Fichier PDF"/>
                  </a>
               </td>
            </xsl:if>
            <td nowrap="true"  class="uportal-channel-table-header" valign="absmiddle" align="right" width="100%">
               <xsl:if test="/xml/etape/@eta_avc_vet='A'">
                  Notes données à titre informatif dans l'attente de la validation par le jury de diplôme
               </xsl:if>
            </td>   
         </tr>
      </table>
      <xsl:apply-templates select="xml/etape"/>
      <xsl:if test="count(xml/etape)=0">
         <p class="uportal-text">Pas de résultat disponible</p>
      </xsl:if>
      <br/>
      <xsl:call-template name="codesRes"/>
   </xsl:template>
   <xsl:template match="etape">
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="7%" class="uportal-channel-strong">Année <hr size="1" noshade="NSOSHADE"/>
            </td>
            <td width="11%" class="uportal-channel-strong">Code <hr size="1" noshade="NSOSHADE"/>
            </td>
            <td width="51%" class="uportal-channel-strong">Libellé <hr size="1" noshade="NSOSHADE"/>
            </td>
            <td width="8%" class="uportal-channel-strong" nowrap="true">Session 1 <hr size="1"
               noshade="NSOSHADE"/>
            </td>
            <td width="8%" class="uportal-channel-strong">Résultat <hr size="1" noshade="NSOSHADE"/>
            </td>
            <td width="8%" class="uportal-channel-strong" nowrap="true">Session 2 <hr size="1"
               noshade="NSOSHADE"/>
            </td>
            <td class="uportal-channel-strong">Résultat <hr size="1" noshade="NSOSHADE"/>
            </td>
         </tr>
         <xsl:for-each select=". | //element | //epreuve">
            <xsl:call-template name="note"/>
         </xsl:for-each>
      </table>
   </xsl:template>
   <xsl:template name="note">
      <xsl:choose>
         <xsl:when
            test="(local-name(.) = 'epreuve') and (count(../epreuve)=1) and (../@noteJuin = @noteJuin) and (../@noteSep = @noteSep)"/>
         <xsl:otherwise>
            <tr onMouseOver="javascript:this.className='uportal-background-highlight'"
               onMouseOut="javascript:this.className='uportal-background-content'">
               <td width="7%" class="uportal-channel-strong">
                  <xsl:if test="local-name(.) = 'etape'">
                     <xsl:value-of select="@annee"/>
                  </xsl:if>
               </td>
               <td width="11%">
                  <xsl:choose>
                     <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <font class="uportal-channel-strong">
                           <xsl:value-of select="@code"/>
                        </font>
                     </xsl:when>
                     <xsl:when test="local-name(.) = 'epreuve'">
                        <font class="uportal-text">
                           <i>
                              <xsl:value-of select="@code"/>
                           </i>
                        </font>
                     </xsl:when>
                     <xsl:otherwise>
                        <font class="uportal-text">
                           <xsl:value-of select="@code"/>
                        </font>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
               <td width="54%">
                  <xsl:choose>
                     <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <font class="uportal-channel-strong">
                           <div style="text-indent:{@level}em">
                              <xsl:value-of select="@libelle"/>
                           </div>
                        </font>
                     </xsl:when>
                     <xsl:when test="local-name(.) = 'epreuve'">
                        <font class="uportal-text">
                           <i>
                              <div style="text-indent:{@level}em">
                                 <xsl:value-of select="@libelle"/>
                              </div>
                           </i>
                        </font>
                     </xsl:when>
                     <xsl:otherwise>
                        <font class="uportal-text">
                           <div style="text-indent:{@level}em">
                              <xsl:value-of select="@libelle"/>
                           </div>
                        </font>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
               <td width="8%">
                  <xsl:choose>
                     <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <font class="uportal-channel-strong">
                           <xsl:value-of select="@noteJuin"/>
                        </font>
                     </xsl:when>
                     <xsl:when test="local-name(.) = 'epreuve'">
                        <font class="uportal-text">
                           <i>
                              <xsl:value-of select="@noteJuin"/>
                           </i>
                        </font>
                     </xsl:when>
                     <xsl:otherwise>
                        <font class="uportal-text">
                           <xsl:value-of select="@noteJuin"/>
                        </font>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
               <td width="8%">
                  <xsl:choose>
                     <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <font class="uportal-channel-strong">
                           <xsl:value-of select="@resJuin"/>
                        </font>
                     </xsl:when>
                     <xsl:when test="local-name(.) = 'epreuve'">
                        <font class="uportal-text">
                           <i>
                              <xsl:value-of select="@resJuin"/>
                           </i>
                        </font>
                     </xsl:when>
                     <xsl:otherwise>
                        <font class="uportal-text">
                           <xsl:value-of select="@resJuin"/>
                        </font>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
               <td width="8%">
                  <xsl:choose>
                     <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <font class="uportal-channel-strong">
                           <xsl:value-of select="@noteSep"/>
                        </font>
                     </xsl:when>
                     <xsl:when test="local-name(.) = 'epreuve'">
                        <font class="uportal-text">
                           <i>
                              <xsl:value-of select="@noteSep"/>
                           </i>
                        </font>
                     </xsl:when>
                     <xsl:otherwise>
                        <font class="uportal-text">
                           <xsl:value-of select="@noteSep"/>
                        </font>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
               <td>
                  <xsl:choose>
                     <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <font class="uportal-channel-strong">
                           <xsl:value-of select="@resSept"/>
                        </font>
                     </xsl:when>
                     <xsl:when test="local-name(.) = 'epreuve'">
                        <font class="uportal-text">
                           <i>
                              <xsl:value-of select="@resSept"/>
                           </i>
                        </font>
                     </xsl:when>
                     <xsl:otherwise>
                        <font class="uportal-text">
                           <xsl:value-of select="@resSept"/>
                        </font>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
            </tr>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
</xsl:stylesheet>
