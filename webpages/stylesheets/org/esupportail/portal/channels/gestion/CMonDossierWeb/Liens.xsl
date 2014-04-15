<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="html" indent="no" />

   <xsl:param name="baseActionURL">baseActionURL_false</xsl:param>

<!-- action des liens -->
   <xsl:variable name="action_etat_civil">etat_civil</xsl:variable>

   <xsl:variable name="action_adresses">adresses</xsl:variable>

   <xsl:variable name="action_inscriptions">inscriptions</xsl:variable>

   <xsl:variable name="action_calendrier">calendrier</xsl:variable>

<!--Decommenter cette ligne correspondant au plugin calendrier d'examen -->
<!--   <xsl:variable name="action_calendrier">calExam</xsl:variable>-->
   <xsl:variable name="action_notes">notes</xsl:variable>

<!-- ligne correspondant au plugin d'affichage des notes -->
<!-- Decommenter la ligne ci dessous et commenter celle du dessus pour utiliser le plugin -->
<!--   <xsl:variable name="action_notes">Resultat</xsl:variable> -->
   <xsl:variable name="action_autreDossier">monDossierWebFinished&amp;autre=servantAnnuaire</xsl:variable>

   <xsl:variable name="action_retour">monDossierWebFinished&amp;sommaire=default</xsl:variable>

<!-- Decommenter cette ligne correspondant au plugin calendrier de rentree -->
<!--   <xsl:variable name="action_calRent">calRent</xsl:variable> -->
<!-- libelle des liens -->
   <xsl:variable name="libelle_etat_civil">Etat-civil</xsl:variable>

   <xsl:variable name="libelle_adresses">Adresses</xsl:variable>

   <xsl:variable name="libelle_inscriptions">Inscriptions</xsl:variable>

   <xsl:variable name="libelle_calendrier">Calendrier des examens</xsl:variable>

   <xsl:variable name="libelle_notes">Notes et résultats</xsl:variable>

   <xsl:variable name="libelle_autreDossier">Autre dossier</xsl:variable>

   <xsl:variable name="libelle_retour">Retour au sommaire</xsl:variable>

<!-- Decommenter cette ligne correspondant au calendrier de rentree -->
<!--   <xsl:variable name="libelle_calRent">Calendrier de rentrée</xsl:variable> -->
<!-- images des liens -->
   <xsl:variable name="image_etat_civil">info.gif</xsl:variable>

   <xsl:variable name="image_adresses">home.gif</xsl:variable>

   <xsl:variable name="image_inscriptions">modify.gif</xsl:variable>

   <xsl:variable name="image_calendrier">paste.gif</xsl:variable>

   <xsl:variable name="image_notes">folder.gif</xsl:variable>

   <xsl:variable name="image_retour">back.gif</xsl:variable>

   <xsl:variable name="image_calRent">calendrier.gif</xsl:variable>

<!-- Modele horizontal -->
   <xsl:param name="contact">none</xsl:param>

   <xsl:param name="servantMailTo">none</xsl:param>

   <xsl:template name="liensHorizontaux">
      <xsl:param name="annuaire">none</xsl:param>

      <br />

      <table cellspacing="0" cellpadding="5" border="0">
         <tr>
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_etat_civil}">
                  <img alt="{$libelle_etat_civil}" border="0" src="{$mediaPath}/{$image_etat_civil}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_etat_civil}">
                  <xsl:value-of select="$libelle_etat_civil" />
               </a>
            </td>

            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_adresses}">
                  <img alt="{$libelle_adresses}" border="0" src="{$mediaPath}/{$image_adresses}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_adresses}">
                  <xsl:value-of select="$libelle_adresses" />
               </a>
            </td>

            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_inscriptions}">
                  <img alt="{$libelle_inscriptions}" border="0" src="{$mediaPath}/{$image_inscriptions}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_inscriptions}">
                  <xsl:value-of select="$libelle_inscriptions" />
               </a>
            </td>

            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_calendrier}">
                  <img alt=" {$libelle_calendrier}" border="0" src="{$mediaPath}/{$image_calendrier}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_calendrier}">
                  <xsl:value-of select="$libelle_calendrier" />
               </a>
            </td>

            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_notes}">
                  <img alt="{$libelle_notes}" border="0" src="{$mediaPath}/{$image_notes}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_notes}">
                  <xsl:value-of select="$libelle_notes" />
               </a>
            </td>

<!-- decommenter ces lignes pour utiliser le calendrier de rentree -->
<!--
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_calRent}">
                  <img alt="{$libelle_calRent}" border="0" src="{$mediaPath}/{$image_calendrier}" />
               </a>
            </td>
 
            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_calRent}">
                  <xsl:value-of select="$libelle_calRent" />
               </a>
            </td>
-->
            <xsl:choose>
               <xsl:when test="$annuaire!='none'">
                  <td width="5%"> </td>
                  <td align="right" width="100%">
                  <table>
                      <tr>
                       <td valign="absmiddle">
                           <a href="{$baseActionURL}?{$action_autreDossier}">
                              <img src="{$mediaPath}/find.gif" border="0" alt="{$libelle_autreDossier}" />
                           </a>
                       </td>    
                       <td class="uportal-channel-subtitle" nowrap="true">
                           <a href="{$baseActionURL}?{$action_autreDossier}">
                              <xsl:value-of select="$libelle_autreDossier" />
                           </a>
                       </td>
                       <td width="5%"> </td>
                       <td valign="absmiddle">
                           <a href="{$baseActionURL}?{$action_retour}">
                              <img alt="{$libelle_retour}" border="0" src="{$mediaPath}/{$image_retour}" />
                           </a>
                       </td>      
                       <td class="uportal-channel-subtitle" nowrap="true">
                           <a href="{$baseActionURL}?{$action_retour}">
                              <xsl:value-of select="$libelle_retour" />
                           </a>
                       </td>                    
                     </tr>
                  </table>   
                  </td>
               </xsl:when>

               <xsl:when test="$contact!='none'">
                  <td width="5%"> </td>

                  <td class="uportal-channel-subtitle" nowrap="true" align="right" width="100%">
                     <xsl:choose>
                        <xsl:when test="$servantMailTo=1">
                           <a href="{$baseActionURL}?action=sendMail&amp;dest=scolarite">Nous contacter</a>
                        </xsl:when>

                        <xsl:otherwise>
                           <a href="mailto:{$contact}">Nous contacter</a>
                        </xsl:otherwise>
                     </xsl:choose>
                  </td>
               </xsl:when>
            </xsl:choose>
         </tr>
      </table>

      <hr size="1"/>
   </xsl:template>

<!-- Modele vertical -->
   <xsl:template name="liensVerticaux">
      <xsl:param name="annuaire">none</xsl:param>

      <table>
         <tr>
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_etat_civil}">
                  <img alt="{$libelle_etat_civil}" border="0" src="{$mediaPath}/{$image_etat_civil}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_etat_civil}">
                  <xsl:value-of select="$libelle_etat_civil" />
               </a>
            </td>
         </tr>

         <tr>
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_adresses}">
                  <img alt="{$libelle_adresses}" border="0" src="{$mediaPath}/{$image_adresses}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_adresses}">
                  <xsl:value-of select="$libelle_adresses" />
               </a>
            </td>
         </tr>

         <tr>
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_inscriptions}">
                  <img alt="{$libelle_inscriptions}" border="0" src="{$mediaPath}/{$image_inscriptions}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_inscriptions}">
                  <xsl:value-of select="$libelle_inscriptions" />
               </a>
            </td>
         </tr>

         <tr>
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_calendrier}">
                  <img alt="{$libelle_calendrier}" border="0" src="{$mediaPath}/{$image_calendrier}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_calendrier}">
                  <xsl:value-of select="$libelle_calendrier" />
               </a>
            </td>
         </tr>

         <tr>
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_notes}">
                  <img alt="{$libelle_notes}" border="0" src="{$mediaPath}/{$image_notes}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_notes}">
                  <xsl:value-of select="$libelle_notes" />
               </a>
            </td>
         </tr>

<!-- decommenter ces lignes pour utiliser le calendrier de rentree -->
<!--
         <tr>
            <td valign="absmiddle">
               <a href="{$baseActionURL}?action={$action_calRent}">
                  <img alt="{$libelle_calRent}" border="0" src="{$mediaPath}/{$image_calendrier}" />
               </a>
            </td>

            <td class="uportal-channel-subtitle" nowrap="true">
               <a href="{$baseActionURL}?action={$action_calRent}">
                  <xsl:value-of select="$libelle_calRent" />
               </a>
            </td>
         </tr>
-->
         <xsl:if test="$annuaire!='none'">
            <tr>
               <td> </td>
            </tr>

            <tr>
               <td valign="absmiddle">
                  <a href="{$baseActionURL}?{$action_autreDossier}">
                     <img src="{$mediaPath}/zoom.gif" border="0" alt="{$libelle_autreDossier}" />
                  </a>
               </td>

               <td class="uportal-channel-subtitle" nowrap="true">
                  <a href="{$baseActionURL}?{$action_autreDossier}">
                     <xsl:value-of select="$libelle_autreDossier" />
                  </a>
               </td>
            </tr>

            <tr>
               <td valign="absmiddle">
                  <a href="{$baseActionURL}?{$action_retour}">
                     <img alt="{$libelle_retour}" border="0" src="{$mediaPath}/{$image_retour}" />
                  </a>
               </td>

               <td class="uportal-channel-subtitle" nowrap="true">
                  <a href="{$baseActionURL}?{$action_retour}">
                     <xsl:value-of select="$libelle_retour" />
                  </a>
               </td>
            </tr>
         </xsl:if>
      </table>
   </xsl:template>
</xsl:stylesheet>

