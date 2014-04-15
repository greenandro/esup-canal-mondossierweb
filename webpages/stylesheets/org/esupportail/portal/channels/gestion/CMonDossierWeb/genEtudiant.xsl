<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="html" indent="yes" />

   <xsl:template name="genEtudiant">
      <xsl:param name="dossier"/>
      <xsl:param name="NNE"/>
      <xsl:param name="nom"/>
      <xsl:param name="nom_usage"/>
      <xsl:param name="prenom"/>
      <xsl:param name="prenom2"/>
      <xsl:param name="prenom3"/>
      <xsl:param name="email"/>
      <xsl:param name="servantMailTo"/>
      <table border="0" cellpadding="2" cellspacing="0" width="100%">
         <tr>
            <td width="150" class="uportal-text">Dossier</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="$dossier" />
            </td>
         </tr>
         <tr>
            <td class="uportal-text">NNE</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="$NNE" />
            </td>
         </tr>
         <tr>
            <td class="uportal-text">Nom</td>
            <td>
               <table border="0" cellspacing="0" cellpadding="0">
                  <tr>
                     <td class="uportal-channel-strong">
                        <xsl:value-of select="$nom" />
                     </td>
                     <td width="5">
                     </td>
                     <td class="uportal-channel-strong">
                        <xsl:value-of select="$prenom" />
                        <xsl:if test="$prenom2!=''">
                           - <xsl:value-of select="$prenom2" />
                        </xsl:if>
                        <xsl:if test="$prenom3!=''">
                           - <xsl:value-of select="$prenom3" />
                        </xsl:if>
                     </td>
                  </tr>
               </table>
            </td>
         </tr>
         <xsl:if test="$nom_usage!='' ">
         <tr>
            <td class="uportal-text">Nom d'Usage</td>
            <td class="uportal-channel-strong">
               <xsl:value-of select="$nom_usage" />
            </td>
         </tr>
         </xsl:if>
         <xsl:if test="$email!=''">
            <tr>
               <td class="uportal-text">Email</td>
               <td class="uportal-channel-strong">
                  <xsl:choose>
                     <xsl:when test="$servantMailTo='1'">
                        <a href="{$baseActionURL}?action=sendMail">
                           <xsl:value-of select="$email" />
                        </a>
                     </xsl:when>
                     <xsl:otherwise>
                        <a href="mailto:{$email}">
                           <xsl:value-of select="$email" />
                        </a>
                     </xsl:otherwise>
                  </xsl:choose>
               </td>
            </tr>
         </xsl:if>
      </table>
   </xsl:template>
</xsl:stylesheet>

