<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:import href="genEtudiant.xsl"/>
    <xsl:output method="html" indent="yes"/>
    <xsl:param name="prefForm"/>
    <xsl:param name="modeServant">none</xsl:param>
    <xsl:param name="baseActionURL"/>
    <xsl:param name="servantMailTo"/>
    <!-- Template pour l'affichage des liens -->
    <xsl:template name="liensIdentite">
        <xsl:if test="xml/etudiant/dossier!=''">
        <script language="javascript"> 
        function <xsl:value-of select="$prefForm"/>_affIdent() {
            document.getElementById('<xsl:value-of select="$prefForm"/>_genEtudiant').style.display='';
            document.getElementById('<xsl:value-of select="$prefForm"/>_masquerIdent').style.display='';
            document.getElementById('<xsl:value-of select="$prefForm"/>_afficherIdent').style.display='none'; 
        } 
        function <xsl:value-of select="$prefForm"/>_masqueIdent() {
            document.getElementById('<xsl:value-of select="$prefForm"/>_genEtudiant').style.display='none';
            document.getElementById('<xsl:value-of select="$prefForm"/>_masquerIdent').style.display='none';
            document.getElementById('<xsl:value-of select="$prefForm"/>_afficherIdent').style.display=''; 
        } 
        </script>
        <table>
            <tr>
                <td width="100%" id="{$prefForm}_afficherIdent">
                    <a href="#" onclick="javascript:{$prefForm}_affIdent()">
                        <xsl:choose>
                            <xsl:when test="$modeServant=1"> Afficher l'identité de l'étudiant </xsl:when>
                            <xsl:otherwise> Afficher mon identité </xsl:otherwise>
                        </xsl:choose>
                    </a>
                </td>
                <td width="100%" id="{$prefForm}_masquerIdent" style="display: none">
                    <a href="#" onclick="javascript:{$prefForm}_masqueIdent()">
                        <xsl:choose>
                            <xsl:when test="$modeServant=1"> Masquer l'identité de l'étudiant </xsl:when>
                            <xsl:otherwise> Masquer mon identité </xsl:otherwise>
                        </xsl:choose>
                    </a>
                </td>
            </tr>
        </table>
        </xsl:if>
    </xsl:template>
    <!-- Template pour l'affichage de l'identité de l'étudiant -->
    <xsl:template name="identite">
        <table cellspacing="0" cellpadding="5" width="100%" border="0" id="{$prefForm}_genEtudiant"
            style="display: none">
            <tr class="uportal-background-semidark">
                <td width="100%" height="25" class="uportal-channel-strong">ETUDIANT</td>
            </tr>
            <tr>
                <td>
                    <xsl:call-template name="genEtudiant">
                        <xsl:with-param name="dossier" select="xml/etudiant/dossier"/>
                        <xsl:with-param name="NNE" select="xml/etudiant/NNE"/>
                        <xsl:with-param name="nom" select="xml/etudiant/nom"/>
                        <xsl:with-param name="nom_usage" select="xml/etudiant/nom_usage"/>
                        <xsl:with-param name="prenom" select="xml/etudiant/prenom"/>
                        <xsl:with-param name="prenom2" select="xml/etudiant/prenom2"/>
                        <xsl:with-param name="prenom3" select="xml/etudiant/prenom3"/>
                        <xsl:with-param name="email" select="xml/etudiant/email"/>
                        <xsl:with-param name="servantMailTo" select="$servantMailTo"/>
                    </xsl:call-template>
                </td>
            </tr>
            <tr>
                <td/>
            </tr>
        </table>
    </xsl:template>
</xsl:stylesheet>
