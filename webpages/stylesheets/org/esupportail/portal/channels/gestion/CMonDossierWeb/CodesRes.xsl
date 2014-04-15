<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="html" indent="no" />

   <xsl:template name="codesRes">
      <table border="0" cellpadding="5" cellspacing="0" width="100%">
         <tr class="uportal-background-semidark">
            <td width="100%" height="25" class="uportal-channel-strong">QUESTIONS</td>
         </tr>
      </table>
      <p><i>Signification des codes résultats :</i></p>
      <table cellpadding="5" cellspacing="0">      
         <tr>
            <xsl:for-each select="/xml/type_resultat">
               <xsl:variable name="cod_tre" select="@cod_tre"/>
               <!-- Page résultat général :
                        /xml/diplome/session[@resultat=$cod_tre] or 
                        /xml/diplome/session[@note=$cod_tre] or 
                        /xml/etape/session[@resultat=$cod_tre] or 
                        /xml/etape/session[@note=$cod_tre]
                    Page détail des notes : 
                        /xml/etape[@noteJuin=$cod_tre] or
                        /xml/etape[@noteSep=$cod_tre] or
                        /xml/etape[@resJuin=$cod_tre] or
                        /xml/etape[@resSept=$cod_tre] or                
                        /xml/etape/element[@noteJuin=$cod_tre] or
                        /xml/etape/element[@noteSep=$cod_tre] or
                        /xml/etape/element[@resJuin=$cod_tre] or
                        /xml/etape/element[@resSept=$cod_tre]
               -->         
               <xsl:if test="/xml/diplome/session[@resultat=$cod_tre] or 
                             /xml/diplome/session[@note=$cod_tre] or 
                             /xml/etape/session[@resultat=$cod_tre] or 
                             /xml/etape/session[@note=$cod_tre] or
                             /xml/etape[@noteJuin=$cod_tre] or
                             /xml/etape[@noteSep=$cod_tre] or
                             /xml/etape[@resJuin=$cod_tre] or
                             /xml/etape[@resSept=$cod_tre] or                
                             /xml/etape/element[@noteJuin=$cod_tre] or
                             /xml/etape/element[@noteSep=$cod_tre] or
                             /xml/etape/element[@resJuin=$cod_tre] or
                             /xml/etape/element[@resSept=$cod_tre]">
                  <td rowspan="rowspan">
                     <font class="uportal-channel-strong"><xsl:value-of select="@cod_tre"/></font> 
                     <font class="uportal-text"> : <xsl:value-of select="@lib_tre"/></font>
                  </td>
               </xsl:if>
            </xsl:for-each>
            <!-- Imposé par le canal -->
            <xsl:if test="/xml/etape/session[@note='COR'] or
                          /xml/etape[@noteJuin='COR'] or
                          /xml/etape[@noteSep='COR'] or 
                          /xml/etape/element[@noteJuin='COR'] or
                          /xml/etape/element[@noteSep='COR']">
               <td rowspan="rowspan">
                  <font class="uportal-channel-strong">COR</font> 
                  <font class="uportal-text"> : Obtenu par Correspondance</font>
               </td>     
            </xsl:if>
         </tr>   
      </table>
   </xsl:template>
   
</xsl:stylesheet>

