<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:date="http://exslt.org/dates-and-times"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
    
    <!-- Template principal -->
    <xsl:template match="xml">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="landscapeA4" page-height="21cm" page-width="29.7cm" 
                    margin-top="1cm" margin-bottom="1cm" margin-left="1cm" margin-right="1cm">
                    <fo:region-before extent="2cm"/>
                    <fo:region-body margin="1cm" margin-top="2cm"/>
                    <fo:region-after extent="1cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="landscapeA4" initial-page-number="1">
                <fo:static-content flow-name="xsl-region-before"> 
                 <fo:block font-size="14pt" text-align="center" font-family="Verdana" color="#003366">  
                        RESULTATS
                    </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-family="Verdana" font-size="8pt" text-align="right" font-style="italic" color="#003366">
                        <xsl:text>Date d'édition : </xsl:text>
                        <xsl:variable name="now" select="date:date-time()"/>
                        <xsl:value-of select="date:day-in-month($now)"/>
                        <xsl:text>/</xsl:text>
                        <xsl:value-of select="date:month-in-year($now)"/>
                        <xsl:text>/</xsl:text>
                        <xsl:value-of select="date:year($now)"/>
                    </fo:block> 
                    <fo:block font-family="Verdana" font-size="10pt" text-align="center" color="#003366">
                        Page <fo:page-number/> / <fo:page-number-citation ref-id="last-page"/>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <xsl:apply-templates select="etudiant"/>
                    <xsl:call-template name="table">
                        <xsl:with-param name="name">diplome</xsl:with-param>
                    </xsl:call-template>
                    <xsl:call-template name="table">
                        <xsl:with-param name="name">etape</xsl:with-param>
                    </xsl:call-template>
                    <xsl:call-template name="signification"/>
                    <fo:block id="last-page" line-height="0"/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    <!-- Template pour l'affichage des informations de l'étudiant -->
    <xsl:template match="etudiant">
        <fo:table>
            <fo:table-column/>
            <fo:table-body>
            <fo:table-row background-color="#e4e7eb" font-family="Verdana" font-size="11pt" color="#003366">
                <fo:table-cell padding-top="0.1cm" padding-left="0.1cm">
                    <fo:block><fo:inline font-weight="bold">ETUDIANT</fo:inline></fo:block>
                </fo:table-cell>
            </fo:table-row>
            </fo:table-body>
        </fo:table>
        <fo:table table-layout="fixed" space-before.optimum="4pt" space-after.optimum="20pt" width="100%">
            <fo:table-column column-width="2cm"/>
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row font-family="Verdana" font-size="11pt" color="#003366">
                    <fo:table-cell>
                        <fo:block>Dossier</fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block><fo:inline font-weight="bold"><xsl:value-of select="dossier"/></fo:inline></fo:block>
                    </fo:table-cell>
                </fo:table-row>    
                <fo:table-row font-family="Verdana" font-size="11pt" color="#003366">
                    <fo:table-cell>
                        <fo:block>NNE</fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block><fo:inline font-weight="bold"><xsl:value-of select="NNE"/></fo:inline></fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row font-family="Verdana" font-size="11pt" color="#003366">
                    <fo:table-cell>
                        <fo:block>Nom</fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block white-space-collapse="false">
                            <fo:inline font-weight="bold" space-end="15mm">
                                <xsl:value-of select="nom"/><xsl:text> </xsl:text><xsl:value-of select="prenom"/>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row font-family="Verdana" font-size="11pt" color="#003366">
                    <fo:table-cell>
                        <fo:block>Email</fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block><fo:inline font-weight="bold"><xsl:value-of select="email"/></fo:inline></fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>    
        </fo:table>
    </xsl:template>
    
    <!-- Template pour l'affichage d'un tableau -->
    <xsl:template name="table">
        <xsl:param name="name"/>
        <fo:table>
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row background-color="#e4e7eb" font-family="Verdana" font-size="11pt" color="#003366">
                    <fo:table-cell padding-top="0.1cm" padding-left="0.1cm">
                        <fo:block>
                            <fo:inline font-weight="bold">
                                <xsl:choose>
                                    <xsl:when test="$name='diplome'">DIPLOMES</xsl:when>
                                    <xsl:when test="$name='etape'">ETAPES</xsl:when>
                                </xsl:choose>
                            </fo:inline>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
        <fo:table table-layout="fixed" width="100%" space-before.optimum="4pt" space-after.optimum="20pt">
            <fo:table-column column-width="2cm"/>
            <fo:table-column column-width="3cm"/>
            <fo:table-column column-width="12cm"/>
            <fo:table-column column-width="2.5cm"/>
            <fo:table-column column-width="2cm"/>
            <fo:table-column column-width="2.5cm"/>
            <fo:table-header>
                <fo:table-row font-family="Verdana" font-size="11pt" color="#003366">
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Année</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Code/Vers.</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Diplôme</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Session</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Note</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Résultat</fo:inline></fo:block>
                    </fo:table-cell>
                </fo:table-row>   
            </fo:table-header>
            <fo:table-body>
                <xsl:choose>
                    <xsl:when test="$name='diplome'">
                        <xsl:for-each select="//diplome">
                            <xsl:call-template name="row"/>
                        </xsl:for-each>        
                    </xsl:when>
                    <xsl:when test="$name='etape'">
                        <xsl:for-each select="//etape">
                            <xsl:call-template name="row"/>
                        </xsl:for-each>        
                    </xsl:when>
                </xsl:choose>
            </fo:table-body>    
        </fo:table>
    </xsl:template>
   
    <!-- Template pour l'affichage d'un diplôme ou d'une étape -->
    <xsl:template name="row">
        <fo:table-row font-family="Verdana" font-size="11pt" color="#294364">
            <fo:table-cell padding="2pt">
                <xsl:if test="position()=1">
                     <xsl:attribute name="padding-top">5pt</xsl:attribute>
                </xsl:if>
                <fo:block><xsl:value-of select="@annee"/></fo:block>
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:if test="position()=1">
                     <xsl:attribute name="padding-top">5pt</xsl:attribute>
                </xsl:if>
                <fo:block>
                    <xsl:value-of select="@code"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:if test="position()=1">
                     <xsl:attribute name="padding-top">5pt</xsl:attribute>
                </xsl:if>
                <fo:block> 
                    <xsl:value-of select="@libelle"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
        <xsl:apply-templates select="session"/>
    </xsl:template>    
    
    <!-- Template pour l'affichage des sessions -->
    <xsl:template match="session">
        <xsl:if test="(@note != 'null' and @note != ' ') or (@resultat != ' ' and @resultat != 'null')">
            <fo:table-row font-family="Verdana" font-size="11pt" color="#294364">  
              <fo:table-cell padding="2pt">
                  <fo:block><xsl:text> </xsl:text></fo:block>
              </fo:table-cell>
              <fo:table-cell padding="2pt">
                  <fo:block><xsl:text> </xsl:text></fo:block>
              </fo:table-cell>
              <fo:table-cell padding="2pt">
                  <fo:block><xsl:text> </xsl:text></fo:block>
              </fo:table-cell>
            <fo:table-cell padding="2pt">
                <fo:block><xsl:value-of select="@type"/></fo:block>
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:if test="@note != 'null'">
                    <fo:block><xsl:value-of select="@note"/></fo:block>
                </xsl:if>
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:if test="@resultat != 'null'">
                    <fo:block><xsl:value-of select="@resultat"/></fo:block>
                </xsl:if>
            </fo:table-cell>
          </fo:table-row>
        </xsl:if>
    </xsl:template>
        
    <!-- Template pour l'affichage des significations des codes résultats -->
    <xsl:template name="signification">
        <fo:table table-layout="fixed" space-before.optimum="4pt" space-after.optimum="4pt">
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row font-family="Verdana" font-size="11pt" color="#003366">
                 <fo:table-cell border-after-width="0.1mm" border-before-style="solid" padding-top="0.3cm">
                        <fo:block font-style="italic">Signification des codes résultats :</fo:block>
                    </fo:table-cell>
                </fo:table-row>
           </fo:table-body>
        </fo:table>         
        <fo:table table-layout="fixed" space-before.optimum="4pt" space-after.optimum="4pt">
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row font-family="Verdana" font-size="11pt" color="#294364">
                    <fo:table-cell>
                        <fo:block>
                            <xsl:for-each select="/xml/type_resultat">
                                <xsl:variable name="cod_tre" select="@cod_tre"/>
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
                                     <fo:inline font-weight="bold"><xsl:value-of select="@cod_tre"/></fo:inline>
                                      : <xsl:value-of select="@lib_tre"/>&#160;&#160;&#160;
                                </xsl:if>
                            </xsl:for-each>
                            <!-- Imposé par le canal -->
                            <xsl:if test="/xml/etape/session[@note='COR'] or
                             /xml/etape[@noteJuin='COR'] or
                             /xml/etape[@noteSep='COR'] or 
                             /xml/etape/element[@noteJuin='COR'] or
                             /xml/etape/element[@noteSep='COR']">
                                  <fo:inline font-weight="bold">COR</fo:inline>
                                  : Obtenu par Correspondance
                            </xsl:if>      
                        </fo:block>
                    </fo:table-cell>   
                </fo:table-row>
            </fo:table-body>    
        </fo:table>
    </xsl:template>
    
</xsl:stylesheet>
