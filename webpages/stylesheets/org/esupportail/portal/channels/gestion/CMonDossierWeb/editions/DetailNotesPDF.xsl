<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:date="http://exslt.org/dates-and-times"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
    <xsl:import href="./NotesPDF.xsl"/>
    <!-- http://tecfa.unige.ch/guides/tie/html/xml-xslfo/xml-xslfo-5.html#pgfId-1000051509 -->
    
    <!-- Template principal -->
    <xsl:template match="xml">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="landscapeA4" page-height="21cm" page-width="29.7cm" 
                    margin-top="1cm" margin-bottom="1cm" margin-left="1cm" margin-right="1cm">
                <!--<fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21cm"
                    margin-left="1cm" margin-right="1cm" margin-top="1cm" margin-bottom="1cm">-->
                    <fo:region-before extent="2cm"/>
                    <fo:region-body margin="1cm" margin-top="2cm"/>
                    <fo:region-after extent="1cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="landscapeA4" initial-page-number="1">
                <fo:static-content flow-name="xsl-region-before"> 
                    <fo:block font-size="14pt" text-align="center" font-family="Verdana" color="#003366">
                        ELEMENTS &amp; EPREUVES
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
                    <xsl:apply-templates select="etape"/>
                    <xsl:call-template name="signification"/>
                    <fo:block id="last-page" line-height="0"/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    <!-- Template pour l'affichage de l'étape -->
    <xsl:template match="etape">
        <fo:table>
            <fo:table-column column-width="5cm"/>
            <fo:table-column/>
            <fo:table-body>
                <fo:table-row background-color="#e4e7eb" font-family="Verdana" font-size="11pt" color="#003366">
                    <fo:table-cell padding-top="0.1cm" padding-left="0.1cm">
                        <fo:block>
                            <fo:inline font-weight="bold">NOTES &amp; RESULTATS</fo:inline>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell padding-top="0.1cm">
                        <xsl:if test="/xml/etape/@eta_avc_vet='A'">
                         <fo:block text-align="center">
                            <fo:inline font-weight="bold">Notes données à titre informatif dans l'attente de la validation par le jury de diplôme</fo:inline>   
                        </fo:block>
                        </xsl:if>
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
            <fo:table-column column-width="2cm"/>
            <fo:table-header>
                <fo:table-row font-family="Verdana" font-size="11pt" color="#003366">
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Année</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Code</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Libellé</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Session 1</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Résultat</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Session 2</fo:inline></fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-after-width="0.1mm" border-after-style="solid">
                        <fo:block><fo:inline font-weight="bold">Résultat</fo:inline></fo:block>
                    </fo:table-cell>
                </fo:table-row>   
            </fo:table-header>
            <fo:table-body>
                <!-- Etape -->
                <xsl:call-template name="row"/>
                <!-- Eléments et épreuves -->
                <xsl:for-each select="//element | //epreuve">
                    <xsl:call-template name="row"/>
                </xsl:for-each>
            </fo:table-body>    
        </fo:table>
    </xsl:template>

    <!-- Template pour l'affichage d'un élément ou épreuve -->
    <xsl:template name="row">
        <fo:table-row font-family="Verdana" font-size="11pt" color="#294364">
            <fo:table-cell padding="2pt">
                <fo:block><fo:inline font-weight="bold"><xsl:value-of select="@annee"/></fo:inline></fo:block>
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:choose>
                    <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <fo:block>
                            <fo:inline font-weight="bold"><xsl:value-of select="@code"/></fo:inline>
                        </fo:block>
                    </xsl:when>
                    <xsl:when test="local-name(.) = 'epreuve'">
                        <fo:block font-style="italic">
                            <xsl:value-of select="@code"/>
                        </fo:block>        
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block>
                            <xsl:value-of select="@code"/>
                        </fo:block>
                    </xsl:otherwise>    
                </xsl:choose>
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:choose>
                    <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <fo:block>
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="@libelle"/>
                            </fo:inline>
                        </fo:block>
                    </xsl:when>
                    <xsl:when test="local-name(.) = 'epreuve'">
                        <fo:block font-style="italic" margin-left="{@level*2}mm">
                            <xsl:value-of select="@libelle"/>
                        </fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- http://www.w3schools.com/xslfo/obj_inline.asp "style="text-indent:{@level}em 
                            http://www.w3schools.com/xslfo/xslfo_blocks.asp
                        -->
                        <fo:block margin-left="{@level*2}mm"> <!-- text-indent="{@level*2}mm"  -->
                            <xsl:value-of select="@libelle"/>
                        </fo:block>
                    </xsl:otherwise>
                </xsl:choose>        
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:choose>
                    <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <fo:block>
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="@noteJuin"/>
                            </fo:inline>
                        </fo:block>
                    </xsl:when>
                    <xsl:when test="local-name(.) = 'epreuve'">
                        <fo:block font-style="italic">
                            <xsl:value-of select="@noteJuin"/>
                        </fo:block>
                    </xsl:when>    
                    <xsl:otherwise>
                        <fo:block><xsl:value-of select="@noteJuin"/></fo:block>
                    </xsl:otherwise>
                </xsl:choose>    
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:choose>
                    <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <fo:block>
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="@resJuin"/>
                            </fo:inline>
                        </fo:block>
                    </xsl:when>
                    <xsl:when test="local-name(.) = 'epreuve'">
                        <fo:block font-style="italic">
                            <xsl:value-of select="@resJuin"/>
                        </fo:block>        
                    </xsl:when>    
                    <xsl:otherwise>
                        <fo:block><xsl:value-of select="@resJuin"/></fo:block>
                    </xsl:otherwise>
                </xsl:choose>            
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:choose>
                    <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <fo:block>
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="@noteSep"/>
                            </fo:inline>
                        </fo:block>
                    </xsl:when>
                    <xsl:when test="local-name(.) = 'epreuve'">
                        <fo:block font-style="italic">
                            <xsl:value-of select="@noteSep"/>
                        </fo:block>        
                    </xsl:when>    
                    <xsl:otherwise>
                        <fo:block><xsl:value-of select="@noteSep"/></fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:table-cell>
            <fo:table-cell padding="2pt">
                <xsl:choose>
                    <xsl:when test="(@level=1) or (local-name(.) = 'etape')">
                        <fo:block>
                            <fo:inline font-weight="bold">
                                <xsl:value-of select="@resSept"/>
                            </fo:inline>
                        </fo:block>
                    </xsl:when>
                    <xsl:when test="local-name(.) = 'epreuve'">
                        <fo:block font-style="italic">
                            <xsl:value-of select="@resSept"/>
                        </fo:block>        
                    </xsl:when>    
                    <xsl:otherwise>
                        <fo:block><xsl:value-of select="@resSept"/></fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>    
    
</xsl:stylesheet>
